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
import org.jsoup.select.Elements;
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
     * 批量查询的结果
     */
    private List<CrawlResult> crawlResults = new ArrayList<>();


    /**
     * 爬网页的深度, 默认为0， 即只爬取当前网页
     */
    private int depth = 1;

    /**
     * 执行抓取网页
     */
    public void doFetchPage() throws Exception {
        doFetchNextPage(0, this.crawlMeta.getUrl());
        this.crawlResult = this.crawlResults.get(0);
    }


    /**
     * 执行抓取网页  https://isaob.com/beauty/20190725/18316.html
     */
    private void doFetchNextPage(int currentDepth, String url) throws Exception {
        HttpResponse response = HttpUtils.request(new CrawlMeta(url, this.crawlMeta.getSelectorRules()), httpConf);
        String res = EntityUtils.toString(response.getEntity());
        CrawlResult result;
        if (response.getStatusLine().getStatusCode() != 200) { // 请求成功
            result = new CrawlResult();
            result.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            result.setUrl(crawlMeta.getUrl());
            this.crawlResults.add(result);
            return;
        }
        result = doParse(res,"");
        // 超过最大深度， 不继续爬
        if (currentDepth > depth) {
            return;
        }
        List<String> nextUrls = result.getNextUrls();
        for(String nextUrl: nextUrls) {
            doFetchNextPage(currentDepth + 1, nextUrl);
        }
    }


    /**
     * 根据爬取规则解析html页面
     * @param html
     */
    private CrawlResult doParse(String html,String htmlName) {
        htmlName = "xiuren";
        AnalyHtmlEnum typeEnum = AnalyHtmlEnum.getHtmlEnum(htmlName);
        logger.info("解析{}的html",typeEnum.getMsg());
        CrawlResult result = new CrawlResult();
        List<Map<String,String>> list = new ArrayList<>();
        List<String> nextUrls = new ArrayList<>();
        try {
            /* 反射，通过类的名字 */
            Class c = Class.forName(typeEnum.getClassName());
            /* 实例化对象 */
            AnalyHtmlType analyClass = (AnalyHtmlType) c.newInstance();
            list = analyClass.analyHtml(html);
            nextUrls = analyClass.nextHtml(html);
            logger.info("解析结果list长度为:{}",list.size());
            for (Map<String,String> map : list){
                if (map.containsKey("imgUrl")){
                    String imgUrl = map.get("imgUrl");
                    String dir = map.get("dir");
                    String fileName = map.get("fileName");
                    String saveUrl = FileImgUtils.downImage(imgUrl,dir,fileName);
                    map.put("saveUrl",saveUrl);
                }
                System.out.println(JsonUtil.bean2Json(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setUrl(crawlMeta.getUrl());
        result.setHtmlName(htmlName);
        result.setResult(list);
        result.setNextUrls(nextUrls);
        result.setStatus(CrawlResult.SUCCESS);
        return result;
    }
}
