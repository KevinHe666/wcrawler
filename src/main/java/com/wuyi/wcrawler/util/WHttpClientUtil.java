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

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.proxy.WProxyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
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


public class WHttpClientUtil {
	private static Log LOG = LogFactory.getLog(WHttpClientUtil.class);
	public static CloseableHttpClient httpClient;
	public static PoolingHttpClientConnectionManager cm;
	private static final int MAX_RETYR = 3;
	private static final int DEFAULT_MAXTOTAL = 200;
	private static final int DEFAULT_MAXPERROUTE = 20;
	private static final int DEFAULT_SOCKET_TIMEOUT = 3000;
	private static final boolean TCP_NO_DEALY = false;
	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/602.4.8 (KHTML, like Gecko) Version/10.0.3 Safari/602.4.8";
	public static void init() {
		init(DEFAULT_MAXTOTAL, DEFAULT_MAXPERROUTE);
	}
	
	public static void init(int maxTotal, int maxPerRoute) {
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(
					KeyStore.getInstance(KeyStore.getDefaultType()),
					new TrustStrategy() {
						
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
					.setRetryHandler(retryHandler).setUserAgent(USER_AGENT);
			
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
	public static CloseableHttpClient getHttpClient() {
		if(httpClient == null) {
			init();
		}
		return httpClient;
	}

	public static void setProxy(HttpRequestBase requestBase, Proxy proxy) {
//		requestBase.setConfig(RequestConfig.custom().setProxy(new HttpHost(proxy.getIp(), proxy.getPort())));
	}
	public static String getPage(CloseableHttpClient httpClient, String url, boolean proxyFlag) {
		HttpGet get = new HttpGet(url);
		if(proxyFlag) {
			/**
			 * 设置代理
			 * */
			setProxy(get, WProxyUtil.getProxy());
		}
		return getPage(httpClient, get, proxyFlag);
	}

	public static String getPage(CloseableHttpClient httpClient, HttpRequestBase requestBase, boolean proxyFlag) {
		HttpResponse response = getHttpResponse(httpClient, requestBase);
		if(response != null && isResponseOK(response)) {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			/**
			 * 失败重试
			 * */
			return getPageRetry(httpClient, requestBase, proxyFlag);
		}
		return null;
	}



	public static String getPageRetry(CloseableHttpClient httpClient, HttpRequestBase requestBase, boolean proxyFlag) {
		int tries = 0;
		boolean ok = false;
		HttpResponse response = null;
		/**
		 * 如果之前未使用代理,则重试时用代理
		 * */
		if(!proxyFlag) {
			setProxy(requestBase, WProxyUtil.getProxy());
		}
		while (tries < MAX_RETYR) {
			if((response = getHttpResponse(httpClient, requestBase)) != null && isResponseOK(response)) {
				ok = true;
				break;
			}
		}
		/**
		 * 如果3次重试后,还是没有返回OK(200状态码)
		 * */
		if(!ok) {
			return null;
		} else {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static HttpResponse getHttpResponse(CloseableHttpClient httpClient, HttpRequestBase requestBase) {
		try {
			return httpClient.execute(requestBase);
		} catch (IOException e) {
			LOG.error("get Response failed");
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isResponseOK(HttpResponse response) {
		return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
	}

	public static String getPageTest(String url) {
		if(httpClient == null) {
			init();
		}
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			try {
				InputStream in = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				reader.close();
				return new String("");
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
}
