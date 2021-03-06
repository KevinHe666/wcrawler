package com.wuyi.wcrawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import com.wuyi.wcrawler.entity.Proxy;
import com.wuyi.wcrawler.mapper.ProxyMapper;
import com.wuyi.wcrawler.proxy.ProxyPool;
import com.wuyi.wcrawler.proxy.util.UserAgent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("wHttpClientUtil")
public class WHttpClientUtil {
	private static Log LOG = LogFactory.getLog(WHttpClientUtil.class);
	private static ProxyPool proxyPool;
	public static CloseableHttpClient httpClient;
	public static PoolingHttpClientConnectionManager cm;
	private static ProxyMapper proxyMapper;
	private static final int MAX_RETYR = 2;
	private static final int DEFAULT_MAXTOTAL = 200;
	private static final int DEFAULT_MAXPERROUTE = 20;
	private static final int DEFAULT_SOCKET_TIMEOUT = 3000;
	private static final boolean TCP_NO_DEALY = false;
	@Autowired
	public void setProxyPool(ProxyPool proxyPool) {
		WHttpClientUtil.proxyPool = proxyPool;
		proxyMapper = ApplicationContextUtil.getBean(ProxyMapper.class);
		WHttpClientUtil.proxyPool.init();
	}

	public static void init() {
		init(DEFAULT_MAXTOTAL, DEFAULT_MAXPERROUTE);
	}
	
	public static void init(int maxTotal, int maxPerRoute) {
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(
					KeyStore.getInstance(KeyStore.getDefaultType()),
					new TrustStrategy() {
						
						@Override
						public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
							// TODO Auto-generated method stub
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslSFactory = new SSLConnectionSocketFactory(sslContext);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = 
					RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", sslSFactory)
					.build();
			cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			/*
			 * 设置连接池的最大连接数
			 * */
			cm.setMaxTotal(maxTotal);
			/*
			 * 设置每个路由的最大连接数
			 * */
			cm.setDefaultMaxPerRoute(maxPerRoute);
			SocketConfig socketConfig = SocketConfig.custom()
					.setSoTimeout(DEFAULT_SOCKET_TIMEOUT)
					.setTcpNoDelay(TCP_NO_DEALY)
					.build();
			cm.setDefaultSocketConfig(socketConfig);
			/*
			 * http重试策略
			 * */
			HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
				@Override
				public boolean retryRequest(IOException exception, int executionCOunt, HttpContext ctx) {
					// TODO Auto-generated method stub
					if(executionCOunt > MAX_RETYR) {
						return false;
					}
					if (exception instanceof InterruptedIOException) {  
			            // Timeout  
			            return true;  
			        } else if (exception instanceof UnknownHostException) {  
			            // Unknown host  
			            return true;  
			        } else if (exception instanceof ConnectException) {  
			            // Connection refused  
			            return true;  
			        } else if (exception instanceof SSLException)  {  
			            // SSL handshake exception  
			            return true;  
			        }  
			        return false;  
				}
			};
			HttpClientBuilder httpClientBuilder =
					HttpClients.custom().setConnectionManager(cm)
					.setRetryHandler(retryHandler);

			httpClient = httpClientBuilder.build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static CloseableHttpClient createHttpClient() {
		if(httpClient == null) {
			init();
		}
		return httpClient;
	}

	public static HttpRequestBase setProxy(HttpRequestBase requestBase, Proxy proxy) {
		requestBase.setConfig(RequestConfig.custom().setProxy(new HttpHost(proxy.getIp(), Integer.valueOf(proxy.getPort()))).build());
		return requestBase;
	}

	public static HttpRequestBase setUserAgent(HttpRequestBase requestBase, String value) {
		requestBase.setHeader("User-Agent", value);
		return requestBase;
	}
	public static HttpRequestBase setOauth(HttpRequestBase requestBase) {
		return setOauth(requestBase, "c3cef7c66a1843f8b3a9e6a1e3160e20");
	}

	public static HttpRequestBase setOauth(HttpRequestBase requestBase, String value) {
		requestBase.setHeader("authorization", "oauth " + value);
		return requestBase;
	}

	public static HttpRequestBase createGet(String url, boolean oauthFlag) {
		HttpGet get = new HttpGet(url);
		get = (HttpGet) setUserAgent(get, UserAgent.getUA());
		get = oauthFlag ? (HttpGet) setOauth(get) : get;
		return get;
	}

	/** only for proxy test */
	public static String getPage(String url, Proxy proxy) {
		httpClient = createHttpClient();
		HttpGet get = (HttpGet) createGet(url, false);
		get = (HttpGet) setProxy(get, proxy);
		HttpResponse response = getHttpResponse(httpClient, get);
		if(response != null && isResponseOK(response)) {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				LOG.error("Test getPage() failed...");
			}
		}
		return null;
	}

	public static String getPage(String url, boolean proxyFlag, boolean oauthFlag) {
		httpClient = createHttpClient();
		HttpGet get = (HttpGet) createGet(url, oauthFlag);
		return getPage(httpClient, get, proxyFlag);
	}

	public static String getPage(CloseableHttpClient httpClient, HttpRequestBase requestBase, boolean proxyFlag) {
		/* 设置代理 */
		Proxy proxy = null;
		if (proxyFlag) {
			proxy = proxyPool.getProxy();
			requestBase = setProxy(requestBase, proxy);
		}
		long proxyStartTimestamp = System.currentTimeMillis();
		HttpResponse response = getHttpResponse(httpClient, requestBase);
		long proxyEndTimestamp = System.currentTimeMillis();
		if(response != null && isResponseOK(response)) {
			try {
				if (proxyFlag) {
					proxySuccess(proxy, proxyStartTimestamp, proxyEndTimestamp);
				}
				return EntityUtils.toString(response.getEntity(), "utf-8");
			} catch (IOException e) {
				LOG.error("EntityUtils.toString failed.");
			}
		} else if (proxyFlag){
			proxyFail(proxy);
		}
		/** 失败重试 */
		return getPageRetry(httpClient, requestBase);
	}

	public static String getPageRetry(CloseableHttpClient httpClient, HttpRequestBase requestBase) {
		int tries = 0;
		boolean ok = false;
		HttpResponse response = null;

		while ((tries++) < MAX_RETYR) {
			LOG.info(String.format("getPageRetry()--" + tries));
			/**
			 * 每次重试,都重新获取代理
			 * */
			Proxy proxy = proxyPool.getProxy();
			requestBase = setProxy(requestBase, proxy);
			long proxyStartTimestamp = System.currentTimeMillis();
			long proxyEndTimestamp;
			if((response = getHttpResponse(httpClient, requestBase)) != null && isResponseOK(response)) {
				proxyEndTimestamp = System.currentTimeMillis();
				ok = true;
				proxySuccess(proxy, proxyStartTimestamp, proxyEndTimestamp);
				break;
			} else {
				proxyFail(proxy);
			}
		}
		/**
		 * 如果MAX_RETYR次重试后,还是没有返回OK(200状态码)
		 * */
		if(!ok) {
			LOG.error("Get page retry failed after " + MAX_RETYR + " times' try.");
			return null;
		} else {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				LOG.info("java.net.SocketTimeoutException: Read timed out");
			}
		}
		return null;
	}

	public static HttpResponse getHttpResponse(CloseableHttpClient httpClient, HttpRequestBase requestBase) {
		try {
			return httpClient.execute(requestBase);
		} catch (IOException e) {
			LOG.error("Get response failed.");
		}
		return null;
	}

	public static boolean isResponseOK(HttpResponse response) {
		return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
	}

	public static void proxySuccess(Proxy proxy, long startStmp, long endStmp) {
		int oldSuccessTimes = proxy.getSuccessTimes();
		proxy.setSuccessTimes(oldSuccessTimes + 1);
		proxy.setSuccessProbability(((oldSuccessTimes + 1) * 1.0) / (oldSuccessTimes + 1 + proxy.getFailureTimes()));
		proxy.setLastSuccessTimestamp(endStmp);
		proxy.setLastSuccessTimeConsume(endStmp - startStmp);
		proxy.setAvgSuccessTimeConsume((proxy.getAvgSuccessTimeConsume() * oldSuccessTimes + proxy.getLastSuccessTimeConsume()) / (oldSuccessTimes + 1));
		proxyMapper.updateByPrimaryKeySelective(proxy);
	}

	public static void proxyFail(Proxy proxy) {
		proxy.setFailureTimes(proxy.getFailureTimes() + 1);
		proxyMapper.updateByPrimaryKeySelective(proxy);
	}
}
