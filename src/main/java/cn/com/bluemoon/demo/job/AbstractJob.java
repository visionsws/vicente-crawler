package cn.com.bluemoon.demo.job;

import cn.com.bluemoon.demo.entity.CrawlResult;

public abstract class AbstractJob implements IJob {

    @Override
    public void beforeRun() {
    }

    @Override
    public void afterRun() {
    }


    @Override
    public void run() {
        this.beforeRun();


        try {
            this.doFetchPage();
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.afterRun();
    }


    /**
     * 具体的抓去网页的方法， 需要子类来补全实现逻辑
     *
     * @throws Exception
     */
    public abstract void doFetchPage() throws Exception;


    /**
     * 解析完网页后的回调方法
     *
     * @param crawlResult
     */
    protected abstract void visit(CrawlResult crawlResult);
}
