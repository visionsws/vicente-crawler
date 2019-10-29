package cn.com.bluemoon.demo.job;

import cn.com.bluemoon.demo.entity.CrawlResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 最简单的一个爬虫任务
 * @author shiweisen
 * @date 2019/6/27
 */
@Getter
@Setter
@NoArgsConstructor
public class SimpleCrawlJob extends DefaultAbstractCrawlJob {

    private static Logger logger = LoggerFactory.getLogger(SimpleCrawlJob.class);


    /**
     * 存储爬取的结果
     */
    private CrawlResult crawlResult;


    /**
     * 批量查询的结果
     */
    private List<CrawlResult> crawlResults = new ArrayList<>();



    public SimpleCrawlJob(int depth) {
        super(depth);
    }


    @Override
    protected void visit(CrawlResult crawlResult) {
        crawlResults.add(crawlResult);
    }


    @Override
    public CrawlResult getCrawlResult() {
        if(crawlResults.size() == 0) {
            return null;
        }
        return crawlResults.get(0);
    }
}
