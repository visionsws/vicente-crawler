package cn.com.bluemoon.demo.analysis;

public enum AnalyHtmlEnum {
    XIU_REN(1,"xiuren","cn.com.bluemoon.demo.analysis.XiuRenHtml","爬取秀人网页面","1");

    private Integer code;

    private String htmlName;

    private String className;

    private String msg;

    private String type;

    AnalyHtmlEnum(Integer code,String htmlName, String className, String msg, String type) {
        this.code = code;
        this.htmlName = htmlName;
        this.className = className;
        this.msg = msg;
        this.type = type;
    }

    public static AnalyHtmlEnum getHtmlEnum(String htmlName) {
        if (htmlName != null && htmlName.length() > 0) {
            for (AnalyHtmlEnum temp : AnalyHtmlEnum.values()) {
                if (temp.getHtmlName().equals(htmlName)) {
                    return temp;
                }
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getHtmlName() {
        return htmlName;
    }

    public String getClassName() {
        return className;
    }

    public String getMsg() {
        return msg;
    }

    public String getType() {
        return type;
    }
}
