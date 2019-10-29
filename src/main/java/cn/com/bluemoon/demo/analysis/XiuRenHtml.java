package cn.com.bluemoon.demo.analysis;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//行业趋势
public class XiuRenHtml extends AnalyHtmlType {

    private static Logger logger = LoggerFactory.getLogger(XiuRenHtml.class);

    @Override
    public List<Map<String, String>> analyHtml(Document doc) {
        List<Map<String, String>> list = new ArrayList<>();
        Elements tableList = doc.select("div#masonry");
        Elements trList = tableList.select("div[data-fancybox=gallery]");

        for( int i = 0; i <trList.size() ; i++){
            Element trEle = trList.get(i);
            Map<String,String> map = new HashMap<>();
            //扩展名为.jpg的图片
            Elements pngs = trEle.select("img[data-src$=.jpg]");
            Elements titleEle = trEle.select("img[title]");
            String pic = pngs.attr("data-src");
            map.put("imgUrl",pic);
            String title = titleEle.attr("title");
            map.put("title",title);
            String[] titleSp = title.split(" ");
            String[] titleSp2 = titleSp[2].split("_");
            map.put("dir",titleSp2[0]);
            String[] picSp = pic.split("/");
            String fileName = picSp[picSp.length-1];
            map.put("fileName",fileName);
            list.add(map);
        }
        return list;
    }

    @Override
    public Map<String,List<String>> nextHtml(Document doc){
        Map<String,List<String>> map = new HashMap<>(2);
        List<String> nextUrlList = new ArrayList<>();
        Elements tableList = doc.select("div[class=container-fluid my-4]");
        Elements trList = tableList.select("div[class=row]").select("div[class=col-2]");

        for( int i = 0; i <trList.size() ; i++) {
            Element trEle = trList.get(i);
            Elements hrefEle = trEle.select("a[href]");
            String href = hrefEle.attr("href");
            nextUrlList.add(href);
        }
        map.put("nextUrlList",nextUrlList);
        return  map;
    }


}
