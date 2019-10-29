package cn.com.bluemoon.demo.filter;

import cn.com.bluemoon.demo.entity.CrawlMeta;
import cn.com.bluemoon.demo.entity.CrawlResult;
import cn.com.bluemoon.demo.fetcher.FetchQueue;
import cn.com.bluemoon.demo.fetcher.JobCount;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihui on 2017/7/6.
 */
@Slf4j
public class ResultFilter {


    public static void filter(CrawlMeta crawlMeta,
                              CrawlResult crawlResult,
                              FetchQueue fetchQueue,
                              int maxDepth) {
        int count = 0;
        long start = System.currentTimeMillis();
        try {
            // 解析返回的网页中的链接，将满足条件的扔到爬取队列中
            int currentDepth = crawlMeta.getCurrentDepth();
            if (currentDepth >= maxDepth) {
                return;
            }


            // 当前的网址中可以继续爬的链接数
            List<String> nextUrls = crawlResult.getNextUrls();
            for(String nextUrl: nextUrls) {
                CrawlMeta meta = new CrawlMeta(
                        JobCount.genId(),
                        crawlMeta.getJobId(),
                        currentDepth + 1,
                        nextUrl,
                        crawlMeta.getHtmlName());
                if (fetchQueue.addSeed(meta)) {
                    if (log.isDebugEnabled()) {
                        log.debug("put into queue! parentUrl:{} url: {} depth: {}",
                                crawlMeta.getUrl(),
                                nextUrl,
                                currentDepth + 1);
                    }

                    count++;
                }
            }

        } finally { // 上一层爬完计数+1
            fetchQueue.finishJob(crawlMeta, count, maxDepth);

            long end = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug("url {} subUrl counts: {}, filter result cost: {}ms, currentDepth: {} \n\n",
                        crawlMeta.getUrl(),
                        count, end - start,
                        crawlMeta.getCurrentDepth());
            }
        }

    }




}
