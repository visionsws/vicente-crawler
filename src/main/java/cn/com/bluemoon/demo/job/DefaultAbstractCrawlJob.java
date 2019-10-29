package cn.com.bluemoon.demo.job;

import cn.com.bluemoon.demo.analysis.AnalyHtmlEnum;
import cn.com.bluemoon.demo.analysis.AnalyHtmlType;
import cn.com.bluemoon.demo.config.CrawlHttpConf;
import cn.com.bluemoon.demo.entity.CrawlMeta;
import cn.com.bluemoon.demo.entity.CrawlResult;
import cn.com.bluemoon.demo.fetcher.FetchQueue;
import cn.com.bluemoon.demo.filter.ResultFilter;
import cn.com.bluemoon.demo.storage.StorageWrapper;
import cn.com.bluemoon.demo.util.FileImgUtils;
import cn.com.bluemoon.demo.util.HttpUtils;
import cn.com.bluemoon.demo.util.JsonUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public abstract class DefaultAbstractCrawlJob extends AbstractJob {

    private static Logger logger = LoggerFactory.getLogger(SimpleCrawlJob.class);
    /**
     * 配置项信息
     */
    protected CrawlMeta crawlMeta;


    /**
     * http配置信息
     */
    private CrawlHttpConf httpConf = new CrawlHttpConf();


    /**
     * 待爬取的任务队列
     */
    private FetchQueue fetchQueue;


    /**
     * 解析的结果
     */
    private CrawlResult crawlResult;

    /**
     * 爬网页的深度, 默认为0， 即只爬取当前网页
     */
    protected int depth = 0;

    public DefaultAbstractCrawlJob(int depth) {
        this.depth = depth;
    }


    /**
     * 执行抓取网页
     */
    @Override
    public void doFetchPage() throws Exception {
        doFetchNextPage();
    }


    /**
     * 执行抓取网页  https://isaob.com/beauty/20190725/18316.html
     */
    private void doFetchNextPage() throws Exception {
        HttpResponse response = HttpUtils.request(this.crawlMeta, httpConf);
        String res = EntityUtils.toString(response.getEntity(), httpConf.getCode());
        // 请求成功
        if (response.getStatusLine().getStatusCode() != 200) {
            this.crawlResult = new CrawlResult();
            this.crawlResult.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            this.crawlResult.setUrl(crawlMeta.getUrl());
            this.visit(crawlResult);
            return;
        }
        // 网页解析
        this.crawlResult = doParse(res,this.crawlMeta);

        // 回调用户的网页内容解析方法
        this.visit(crawlResult);

        // 解析返回的网页中的链接，将满足条件的扔到爬取队列中
        int currentDepth = this.crawlMeta.getCurrentDepth();

        // 结果过滤
        ResultFilter.filter(crawlMeta, crawlResult, fetchQueue, depth);
    }


    /**
     * 根据爬取规则解析html页面
     * @param html
     */
    private CrawlResult doParse(String html,CrawlMeta meta) {
        String htmlName = meta.getHtmlName();
        AnalyHtmlEnum typeEnum = AnalyHtmlEnum.getHtmlEnum(htmlName);
        logger.info("解析{}的html",typeEnum.getMsg());
        CrawlResult result = new CrawlResult();
        List<Map<String,String>> list = new ArrayList<>();
        List<String> nextUrls = new ArrayList<>();
        List<String> nextPages = new ArrayList<>();
        try {
            /* 反射，通过类的名字 */
            Class c = Class.forName(typeEnum.getClassName());
            /* 实例化对象 */
            AnalyHtmlType analyClass = (AnalyHtmlType) c.newInstance();
            Document doc = Jsoup.parse(html, meta.getUrl());
            list = analyClass.analyHtml(doc);

            Map<String,List<String>> nextMap = analyClass.nextHtml(doc);
            nextUrls = nextMap.get("nextUrlList");
            nextPages = nextMap.get("pageList");
            logger.info("解析结果list长度为:{}",list.size());
            for (Map<String,String> map : list){
                if (map.containsKey("imgUrl")){
                    String imgUrl = map.get("imgUrl");
                    String dir = map.get("dir");
                    String fileName = map.get("fileName");
                    //String saveUrl = FileImgUtils.downImage(imgUrl,dir,fileName);
                    //map.put("saveUrl",saveUrl);
                }
                System.out.println(JsonUtil.bean2Json(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setUrl(crawlMeta.getUrl());
        result.setHtmlName(htmlName);
        result.setResult(list);
        result.setNextPages(nextPages);
        result.setNextUrls(nextUrls);
        result.setStatus(CrawlResult.SUCCESS);
        return result;
    }

    protected void setResponseCode(String code) {
        httpConf.setCode(code);
    }


}

