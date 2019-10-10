package cn.com.bluemoon.demo.job;

import cn.com.bluemoon.demo.entity.CrawlMeta;
import cn.com.bluemoon.demo.entity.CrawlResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    /**
     * 配置项信息
     */
    private CrawlMeta crawlMeta;


    /**
     * 存储爬取的结果
     */
    private CrawlResult crawlResult;


    /**
     * 执行抓取网页
     */
    public void doFetchPage() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(crawlMeta.getUrl());
        httpGet.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.addHeader("connection", "Keep-Alive");
        httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        HttpResponse response = httpClient.execute(httpGet);
        String res = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200) { // 请求成功
            doParse(res);
        } else {
            this.crawlResult = new CrawlResult();
            this.crawlResult.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            this.crawlResult.setUrl(crawlMeta.getUrl());
        }
    }



    private void doParse(String html) {
        Document doc = Jsoup.parse(html);

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
        this.crawlResult.setResult(map);
    }
}
