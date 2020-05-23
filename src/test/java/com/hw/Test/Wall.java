package com.hw.Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class Wall implements PageProcessor {
    //存储过滤url
    public static Set<String> set = new HashSet();
    //列表页
    public static final String URL_LIST = "by_sub_category\\.php\\?id=226985&name=%E4%B8%9C%E4%BA%AC%E9%A3%9F%E5%B0%B8%E9%AC%BC\\+%E5%A3%81%E7%BA%B8&lang=Chinese&page=\\d+";
    //详情页
    public static final String URL_POST = "https://wall\\.alphacoders\\.com/big.php\\?i=\\d{6}&lang=Chinese";
    //原图
    public static final String IMAGE = "https://images3\\.alphacoders\\.com/\\d{3}/\\d{6}\\.png";

    private Site site = new Site().me()
            .setRetryTimes(3).setTimeOut(5000000).setSleepTime(100)
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.516.400 QQBrowser/9.4.8188.400");

    @Override
    public void process(Page page) {
        //加入列表页
        page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());

        if (page.getUrl().regex(URL_LIST).match()) {
            //如果是列表页 我们从页面中正则匹配详情页
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());

        } else {
            //如果是详情页或者主页（这边代码有问题不应该加上主页）,找到对应原图url,本意是打算用image去过滤的。加到set过滤重复url
            set.add(page.getHtml().xpath("//div[@style='width:100%;padding:10px;max-width:975px;margin:auto;']/div[@class='row']/div/span/@data-href").toString());

        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {

        Spider.create(new Wall()).
                addUrl("https://wall.alphacoders.com/by_sub_category.php?id=226985&name=%E4%B8%9C%E4%BA%AC%E9%A3%9F%E5%B0%B8%E9%AC%BC+%E5%A3%81%E7%BA%B8&lang=Chinese").thread(5)
                .run();
        FileOutputStream fos = new FileOutputStream(new File("E:/DONGJING.txt"));
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);

        for (String string : set) {
            //存储方法
        }

        //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
        bw.close();
        osw.close();
        fos.close();

    }
}

