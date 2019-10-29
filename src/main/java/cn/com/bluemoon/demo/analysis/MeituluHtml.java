package cn.com.bluemoon.demo.analysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//行业趋势
public class MeituluHtml extends AnalyHtmlType {

    private static Logger logger = LoggerFactory.getLogger(XiuRenHtml.class);

    @Override
    public List<Map<String, String>> analyHtml(Document doc) {
        List<Map<String, String>> list = new ArrayList<>();
        Elements weizhiEle = doc.select("div.weizhi").select("h1");
        String title = weizhiEle.text();
        System.out.println(title);
        Elements pList = doc.select("div[class=c_l]").select("p");
        for( int i = 0; i <pList.size() ; i++){
            Element pEle = pList.get(i);
            String text = pEle.text();
            System.out.println(text);

        }
        Elements picList = doc.select("div[class=content]>center>img");
        for( int i = 0; i <picList.size() ; i++){
            Element trEle = picList.get(i);
            Map<String,String> map = new HashMap<>();
            //扩展名为.jpg的图片
            String pic = trEle.attr("src");
            String picTitle = trEle.attr("alt");
            map.put("imgUrl",pic);
            map.put("picTitle",picTitle);
            list.add(map);
        }
        return list;
    }

    @Override
    public Map<String,List<String>> nextHtml(Document doc){
        Map<String,List<String>> map = new HashMap<>(2);
        //下一页的地址
        List<String> pageList = new ArrayList<>();
        Elements tableList = doc.select("div[id=pages]");
        Elements trList = tableList.select("a");
        for( int i = 0; i <trList.size() ; i++) {
            Element link  = trList.get(i);
            String href = link.attr("abs:href");
            String text = link.text();
            if ("下一页".equals(text)){
                pageList.add(href);
            }
        }
        map.put("pageList",pageList);

        //爬取另外一个页面的地址
        List<String> nextUrlList = new ArrayList<>();
        Elements ulEle = doc.select("ul[class=img]");
        Elements liList = ulEle.select("li");
        for( int i = 0; i <liList.size() ; i++) {
            Element liEle  = liList.get(i);
            Element aEle  = liEle.selectFirst("a");
            String href = aEle.attr("href");
            nextUrlList.add(href);

        }
        map.put("nextUrlList",nextUrlList);
        return  map;
    }


}
