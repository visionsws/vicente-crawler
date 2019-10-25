package cn.com.bluemoon.demo.analysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//行业趋势
public class XiuRenHtml extends AnalyHtmlType {

    private static Logger logger = LoggerFactory.getLogger(XiuRenHtml.class);

    @Override
    public List<Map<String, String>> analyHtml(String html) {
        Document doc = Jsoup.parse(html);
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
           /* switch (i) {
                case 0:
                    //店铺名
                    map.put("shop_name", text);
                    break;
                case 1:
                    //商品ID
                    map.put("item_id", text);
                    String uuid = dayTime + "_" + text;
                    map.put("uuid", uuid);
                    break;
            }*/

        }
        return list;
    }

    @Override
    public List<String> nextHtml(String html){
        Document doc = Jsoup.parse(html);
        List<String> list = new ArrayList<>();
        Elements tableList = doc.select("div[class=container-fluid my-4]");
        Elements trList = tableList.select("div[class=row]").select("div[class=col-2]");

        for( int i = 0; i <trList.size() ; i++) {
            Element trEle = trList.get(i);
            Elements hrefEle = trEle.select("a[href]");
            String href = hrefEle.attr("href");
            list.add(href);
        }
        return  list;
    }


}
