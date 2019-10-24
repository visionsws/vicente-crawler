package cn.com.bluemoon.demo.job;

import cn.com.bluemoon.demo.analysis.AnalyHtmlEnum;
import cn.com.bluemoon.demo.analysis.AnalyHtmlType;
import cn.com.bluemoon.demo.config.CrawlHttpConf;
import cn.com.bluemoon.demo.entity.CrawlMeta;
import cn.com.bluemoon.demo.entity.CrawlResult;
import cn.com.bluemoon.demo.util.FileImgUtils;
import cn.com.bluemoon.demo.util.HttpUtils;
import cn.com.bluemoon.demo.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 最简单的一个爬虫任务
 * <p>
 * Created by yihui on 2017/6/27.
 */
@Getter
@Setter
public class SimpleCrawlJob extends AbstractJob {

    private static Logger logger = LoggerFactory.getLogger(SimpleCrawlJob.class);


    /**
     * 配置项信息
     */
    private CrawlMeta crawlMeta;


    /**
     * 存储爬取的结果
     */
    private CrawlResult crawlResult;

    /**
     * http配置信息
     */
    private CrawlHttpConf httpConf = new CrawlHttpConf();


    /**
     * 执行抓取网页  https://isaob.com/beauty/20190725/18316.html
     */
    public void doFetchPage() throws Exception {
        HttpResponse response = HttpUtils.request(crawlMeta, httpConf);
        String res = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200) { // 请求成功
            doParse(res);
        } else {
            this.crawlResult = new CrawlResult();
            this.crawlResult.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            this.crawlResult.setUrl(crawlMeta.getUrl());
        }
    }


    /**
     * 根据爬取规则解析html页面
     * @param html
     */
    private void doParse(String html) {
        String htmlName = "xiuren";
        AnalyHtmlEnum typeEnum = AnalyHtmlEnum.getHtmlEnum(htmlName);
        logger.info("解析{}的html",typeEnum.getMsg());
        List<Map<String,String>> list = new ArrayList<>();
        try {
            /* 反射，通过类的名字 */
            Class c = Class.forName(typeEnum.getClassName());
            /* 实例化对象 */
            AnalyHtmlType analyClass = (AnalyHtmlType) c.newInstance();
            list = analyClass.analyHtml(html);
            logger.info("解析结果list长度为:{}",list.size());
            for (Map<String,String> map : list){
                if (map.containsKey("imgUrl")){
                    String imgUrl = map.get("imgUrl");
                    String dir = map.get("dir");
                    String fileName = map.get("fileName");
                    FileImgUtils.downImage(imgUrl,dir,fileName);
                }
                System.out.println(JsonUtil.bean2Json(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* Document doc = Jsoup.parse(html);
        Map<String, List<String>> map = new HashMap<>(crawlMeta.getSelectorRules().size());
        for (String rule: crawlMeta.getSelectorRules()) {
            List<String> list = new ArrayList<>();
            for (Element element: doc.select(rule)) {

                list.add(element.text());
            }
            map.put(rule, list);
        }
        this.crawlResult = new CrawlResult();
        this.crawlResult.setHtmlDoc(doc);
        this.crawlResult.setUrl(crawlMeta.getUrl());
        this.crawlResult.setResult(map);*/
    }
}
