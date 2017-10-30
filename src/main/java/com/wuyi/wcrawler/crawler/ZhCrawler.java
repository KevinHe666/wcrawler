package com.wuyi.wcrawler.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuyi.wcrawler.entity.CrawlerTask;
import com.wuyi.wcrawler.mapper.ZhUserMapper;
import com.wuyi.wcrawler.entity.ZhUser;
import com.wuyi.wcrawler.util.WHttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author wuyi5
 * @date 2017/8/17
 */
@Component(value = "zhCrawler")
@Scope("prototype")
public class ZhCrawler extends Crawler {

    private static final Log LOG = LogFactory.getLog(ZhCrawler.class);
    @Autowired
    private ZhUserMapper zhUserMapper;
    private ZhUser startUser;
    private Set<ZhUser> crawlUserBuffer;
    private int bufferThreshold = 512;
    private String paramInclude = "data[*].educations,employments,answer_count,business,locations,articles_count,follower_count,gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics";
    private int paramOffset = 0;
    private int paramLimit = 20;
    private String urlToken;
    public ZhCrawler() { super();}

    @Override
    public void run() {
        try {
            for (int offset = 0; offset < 1; offset ++) {
                crawl(concatRequestUrl(this.getUrl(), this.getParamInclude(), this.getParamLimit() * offset, this.getParamLimit()));
            }
            if (crawlUserBuffer.size() > 0) {
                flush();
            }
        } catch (Exception e) {
            startUser.setStatus(CrawlerTask.ABNORMALEND);
            e.printStackTrace();
        }
        startUser.setStatus(CrawlerTask.NORMALEND);
        zhUserMapper.updateByPrimaryKey(startUser);
    }

    private String concatRequestUrl(String url, String include, int offset, int limit) {
        return url
                .concat("?")
                .concat("include=" + include)
                .concat("&")
                .concat("offset=" + offset)
                .concat("&")
                .concat("limit=" + limit);
    }

    @Override
    public void crawl(String requestUrl) {
        System.out.println("startUrl: " + requestUrl);
        String followees = WHttpClientUtil.getPage(requestUrl, false);
        if (followees == null) {
            return;
        }
        JSONArray dataArray = (JSONArray) JSON.parseObject(followees).get("data");
        for (Object object :dataArray) {
            JSONObject jsonObject = JSON.parseObject(object.toString());
            ZhUser zhUser = parseUser(jsonObject);
            addToBuffer(zhUser);
        }
    }

    public ZhUser parseUser(JSONObject jsonObject) {
        ZhUser zhUser = new ZhUser();
        zhUser.setLinkPerson(this.getUrlToken());
        zhUser.setName((String) jsonObject.get("name"));
        zhUser.setUrlToken((String) jsonObject.get("url_token"));
        zhUser.setHeadline((String) jsonObject.get("headline"));
        zhUser.setFollowerCount((Integer) jsonObject.get("follower_count"));
        zhUser.setFollowingCount((Integer) jsonObject.get("following_count"));
        zhUser.setVoteupCount((Integer) jsonObject.get("voteup_count"));
        zhUser.setThankedCount((Integer) jsonObject.get("thanked_count"));
        zhUser.setAnswerCount((Integer) jsonObject.get("answer_count"));
        zhUser.setQuestionCount((Integer) jsonObject.get("question_count"));
        zhUser.setArticlesCount((Integer) jsonObject.get("articles_count"));
        String businessName = null;
        String educationsSchoolName = null;
        String locationsName = null;

        try {
            businessName = (String) ((JSONObject) jsonObject.get("business")).get("name");
        } catch (Exception e) {
            businessName = "";
        }
        zhUser.setBusinessName(businessName);
        try {
            educationsSchoolName = (String) ((JSONObject)((JSONArray)jsonObject.get("educations")).get(0)).get("name");
        } catch (Exception e) {
            educationsSchoolName = "";
        }
        zhUser.setEducationsSchoolName(educationsSchoolName);
        try {
            locationsName = (String) (((JSONObject)((JSONArray)jsonObject.get("locations")).get(0)).get("name"));
        } catch (Exception e) {
            locationsName = "";
        }
        zhUser.setLocationsName(locationsName);

        return zhUser;
    }

    public void addToBuffer(ZhUser zhUser) {
        if (!crawlUserBuffer.contains(zhUser)) {
            Example example = new Example(ZhUser.class);
            example.createCriteria().andEqualTo("urlToken", zhUser.getUrlToken());
            List<ZhUser> zhUserList = zhUserMapper.selectByExample(example);
            if (zhUserList.size() == 0) {
                synchronized (crawlUserBuffer) {
                    crawlUserBuffer.add(zhUser);
                    if (crawlUserBuffer.size() > bufferThreshold) {
                        flush();
                    }
                }
            }
        }
    }

    public void flush() {
        synchronized (crawlUserBuffer) {
            LOG.info("flush: " + crawlUserBuffer.size() + " users insert into db...");
            zhUserMapper.insertList(new ArrayList<>(crawlUserBuffer));
            crawlUserBuffer.clear();
        }

    }

    public ZhUser getStartUser() {
        return startUser;
    }

    public void setStartUser(ZhUser startUser) {
        this.startUser = startUser;
    }

    public Set<ZhUser> getCrawlUserBuffer() {
        return crawlUserBuffer;
    }

    public void setCrawlUserBuffer(Set<ZhUser> crawlUserBuffer) {
        this.crawlUserBuffer = crawlUserBuffer;
    }

    public int getBufferThreshold() {
        return bufferThreshold;
    }

    public void setBufferThreshold(int bufferThreshold) {
        this.bufferThreshold = bufferThreshold;
    }

    public String getParamInclude() {
        return paramInclude;
    }

    public void setParamInclude(String paramInclude) {
        this.paramInclude = paramInclude;
    }

    public int getParamOffset() {
        return paramOffset;
    }

    public void setParamOffset(int paramOffset) {
        this.paramOffset = paramOffset;
    }

    public int getParamLimit() {
        return paramLimit;
    }

    public void setParamLimit(int paramLimit) {
        this.paramLimit = paramLimit;
    }

    public String getUrlToken() {
        return urlToken;
    }

    public void setUrlToken(String urlToken) {
        this.urlToken = urlToken;
    }
}
