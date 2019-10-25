package cn.com.bluemoon.demo.analysis;

import java.util.List;
import java.util.Map;

public abstract class AnalyHtmlType {

    public abstract List<Map<String,String>> analyHtml(String html);

    public abstract List<String> nextHtml(String html);

}
