package cn.com.bluemoon.demo.service.impl;

import cn.com.bluemoon.demo.entity.CrawlMeta;
import cn.com.bluemoon.demo.entity.CrawlResult;
import cn.com.bluemoon.demo.job.SimpleCrawlJob;
import cn.com.bluemoon.demo.service.DemoService;
import cn.com.bluemoon.demo.util.FileImgUtils;
import cn.com.bluemoon.demo.util.HttpUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

/**
 * @author shiweisen
 * @since 2018-11-13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoServiceImplTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void test() {
        String msg = "hello demo";
        demoService.test(msg);
    }

    /**
     * 测试我们写的最简单的一个爬虫,
     *
     * 目标是爬取一篇博客
     */
    @Test
    public void testFetch() throws InterruptedException {
        String url = "https://isaob.com/beauty/20190725/18316.html";
        Set<String> selectRule = new HashSet<>();
        //selectRule.add("div[id=masonry]>div>img[title]"); // 博客标题
        selectRule.add("div#masonry>div[data-src$=.jpg]"); // 博客正文

        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url); // 设置爬取的网址
        crawlMeta.setSelectorRules(selectRule); // 设置抓去的内容


        SimpleCrawlJob job = new SimpleCrawlJob();
        job.setCrawlMeta(crawlMeta);
        Thread thread = new Thread(job, "crawler-test");
        thread.start();

        thread.join(); // 确保线程执行完毕

        CrawlResult result = job.getCrawlResult();
        System.out.println(result);
    }

    /**
     * 测试我们写的最简单的一个爬虫,
     *
     * 目标是爬取一篇博客
     */
    @Test
    public void testImgDown() {
       try {
           // 图片的网址
           String url = "http://ww2.sinaimg.cn/large/9d57a855jw1dqpv9fp4yuj.jpg";

           HttpClient httpClient = HttpClients.createDefault();
           HttpGet httpGet = new HttpGet(url);
           //httpGet.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
           //httpGet.addHeader("connection", "Keep-Alive");
           //httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
           HttpResponse response = httpClient.execute(httpGet);
           HttpEntity entity = response.getEntity();
           InputStream inputStream = entity.getContent();
           FileImgUtils.writeFile(inputStream, "d:\\ImageCrawler\\" , URLDecoder.decode("filename.jpg", "UTF-8"));
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
            // 关闭低层流。
           //httpClient.close();
        }
    }
}
