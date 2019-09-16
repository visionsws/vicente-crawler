package cn.com.bluemoon.demo.service.impl;

import cn.com.bluemoon.demo.entity.CrawlMeta;
import cn.com.bluemoon.demo.entity.CrawlResult;
import cn.com.bluemoon.demo.job.SimpleCrawlJob;
import cn.com.bluemoon.demo.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

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
        String url = "https://my.oschina.net/u/566591/blog/1031575";
        Set<String> selectRule = new HashSet<>();
        selectRule.add("div[class=title]"); // 博客标题
        selectRule.add("div[class=blog-body]"); // 博客正文

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


}