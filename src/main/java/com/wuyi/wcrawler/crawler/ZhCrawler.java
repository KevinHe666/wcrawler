package com.wuyi.wcrawler.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuyi.wcrawler.Config;
import com.wuyi.wcrawler.entity.CrawlerTask;
import com.wuyi.wcrawler.entity.FollowRelation;
import com.wuyi.wcrawler.mapper.FollowRelationMapper;
import com.wuyi.wcrawler.mapper.ZhUserMapper;
import com.wuyi.wcrawler.entity.ZhUser;
import com.wuyi.wcrawler.util.WHttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.*;

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
    @Autowired
    private FollowRelationMapper followRelationMapper;
    private ZhUser startUser;
    private Set<ZhUser> crawlUserBuffer;
    private Set<FollowRelation> followRelationBuffer;
    // TODO 改回512
    private int bufferThreshold = 128;
    private String paramInclude = "data[*].educations,employments,answer_count,business,locations,articles_count,follower_count,gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics";
    private int paramOffset = 0;
    private int paramLimit = 20;
    private String urlToken;
    public ZhCrawler() {
        super();
        crawlUserBuffer = new HashSet<>();
        followRelationBuffer = new HashSet<>();
    }

    @Override
    public void run() {
        try {
            for (int offset = 0; offset < 1; offset ++) {
                crawl(concatRequestUrl(this.getUrl(), this.getParamInclude(), this.getParamLimit() * offset, this.getParamLimit()));
            }
            if (crawlUserBuffer.size() > 0) {
                flush(crawlUserBuffer, zhUserMapper, ZhUser.class);
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
        LOG.info("startUrl: " + requestUrl);
        String followees = WHttpClientUtil.getPage(requestUrl, Config.getInstance().getProxyFlag(), true);
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

        // 插入关注关系
        addFollowRelationToBuffer(this.urlToken, (String) jsonObject.get("url_token"));
        return zhUser;
    }

    public void addFollowRelationToBuffer(String follower, String followee) {
        FollowRelation followRelation = new FollowRelation();
        followRelation.setFollower(follower);
        followRelation.setFollowee(followee);
        if (!followRelationBuffer.contains(followRelation)) {
            Example example = new Example(FollowRelation.class);
            example.createCriteria()
                    .andEqualTo("follower", followRelation.getFollower())
                    .andEqualTo("followee", followRelation.getFollowee());
            List<FollowRelation> followRelationList = followRelationMapper.selectByExample(example);
            if (followRelationList == null || followRelationList.size() == 0) {
                synchronized (followRelationBuffer) {
                    followRelationBuffer.add(followRelation);
                    if (followRelationBuffer.size() > bufferThreshold) {
                        flush(followRelationBuffer, followRelationMapper, FollowRelation.class);
                    }
                }
            }
        }
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
                        flush(crawlUserBuffer, zhUserMapper, ZhUser.class);
                    }
                }
            }
        }
    }

    public <T> void flush(Set<T> buffer, Mapper<T> mapper, Class<T> clazz) {
        synchronized (buffer) {
            if (buffer.getClass().getSimpleName().contains("User")) {
                ((ZhUserMapper)mapper).insertList(new ArrayList<ZhUser>((Collection<? extends ZhUser>) buffer));
                LOG.info("buffer flush: " + buffer.size() + "users insert into db...");
            } else if (buffer.getClass().getSimpleName().contains("Relation")) {
                ((FollowRelationMapper)mapper).insertList(new ArrayList<FollowRelation>((Collection<? extends FollowRelation>) buffer));
                LOG.info("buffer flush: " + buffer.size() + "relations insert into db...");
            }
            buffer.clear();
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
