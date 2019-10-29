package cn.com.bluemoon.demo.analysis;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

public abstract class AnalyHtmlType {

    public abstract List<Map<String,String>> analyHtml(Document doc);

    public abstract Map<String,List<String>> nextHtml(Document doc);

}
