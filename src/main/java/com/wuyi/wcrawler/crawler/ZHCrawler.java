package com.wuyi.wcrawler.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    private ZhUserMapper zhUserDao;
    public ZhCrawler() { super();}

    @Override
    public void run() {
        crawl(this.getUrl());
    }

    @Override
    public void crawl(String url) {
        System.out.println("startUrl: " + url);
        String followees = WHttpClientUtil.getPage(url, false);
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
            List<ZhUser> zhUserList = zhUserDao.selectByExample(example);
            if (zhUserList.size() == 0) {
                zhUserDao.insert(zhUser);
                System.out.println("User " + zhUser.getName() + "insert into table...");
            }
        }
    }

}
