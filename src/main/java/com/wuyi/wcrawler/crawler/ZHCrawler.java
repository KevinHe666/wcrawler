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

import java.util.List;

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
    public ZhCrawler() { super();}

    @Override
    public void run() {
        for (int offset = 0; offset < 1; offset ++) {
            crawl(concatRequestUrl(this.getUrl(), this.getParamInclude(), this.getParamLimit() * offset, this.getParamLimit()));
        }
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
        JSONArray dataArray = (JSONArray) JSON.parseObject(followees).get("data");
        for (Object object :dataArray) {
            JSONObject jsonObject = JSON.parseObject(object.toString());
            ZhUser zhUser = new ZhUser();
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
            // 这样的做法相率太低,暂时先这样写(之后可以考虑用LRU缓存提高效率)
            Example example = new Example(ZhUser.class);
            example.createCriteria().andEqualTo("urlToken", zhUser.getUrlToken());
            List<ZhUser> zhUserList = zhUserMapper.selectByExample(example);
            if (zhUserList.size() == 0) {
                zhUser.setStatus(0);
                zhUserMapper.insert(zhUser);
                int curAmount = this.getCurAmount().incrementAndGet();
                if (curAmount >= this.getTarAmount()) {
                    // TODO
                }
                System.out.println("User " + zhUser.getName() + "insert into table...");
            }
        }
    }

}
