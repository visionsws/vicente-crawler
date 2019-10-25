package cn.com.bluemoon.demo.entity;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yihui on 2017/6/27.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrawlMeta {

    /**
     * 待爬去的网址
     */
    @Getter
    @Setter
    private String url;

    /**
     * 获取指定内容的规则, 因为一个网页中，你可能获取多个不同的内容， 所以放在集合中
     */
    @Setter
    @Getter
    private Set<String> selectorRules  = new HashSet<>();

    public Set<String> addSelectorRule(String rule) {
        this.selectorRules.add(rule);
        return selectorRules;
    }



}
