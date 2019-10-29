package cn.com.bluemoon.demo.entity;

import cn.com.bluemoon.demo.fetcher.JobCount;
import lombok.*;

/**
 * Created by yihui on 2017/6/27.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrawlMeta {

    /**
     * 当前任务对应的 {@link JobCount #id }
     */
    @Getter
    @Setter
    private int jobId;


    /**
     * 当前任务对应的 {@link JobCount #parentId }
     */
    @Getter
    @Setter
    private int parentJobId;

    /**
     * 当前爬取的深度
     */
    @Getter
    @Setter
    private int currentDepth = 0;

    /**
     * 待爬去的网址
     */
    @Getter
    @Setter
    private String url;

    /**
     * 获取指定网页爬取规则, 因为一每一个网页都不一样，所以需要不同的爬取规则
     */
    @Setter
    @Getter
    private String htmlName;



}
