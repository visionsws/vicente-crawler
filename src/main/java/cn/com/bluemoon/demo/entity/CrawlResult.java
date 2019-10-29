package cn.com.bluemoon.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class CrawlResult {

    public static Status SUCCESS = new Status(200, "success");
    public static Status NOT_FOUND = new Status(494, "not found");

    /**
     * 爬取的网址
     */
    private String url;

    /**
     * 爬取的对应的html名称
     */
    private String htmlName;

    /**
     * 爬取的结果，一个map是一条数据
     */
    private List<Map<String, String>> result;

    /**
     * 获取下一页
     */
    private List<String> nextPages;

    /**
     * 获取下一个html
     */
    private List<String> nextUrls;

    private Status status;

    public void setStatus(int code, String msg) {
        this.status = new Status(code, msg);
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    static class Status {
        private int code;

        private String msg;
    }

}
