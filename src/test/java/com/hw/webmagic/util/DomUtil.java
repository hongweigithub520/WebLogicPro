package com.hw.webmagic.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class DomUtil {

    //jsoup的解析方式
    public  void jsoup(){
        File in = new File("E:\\travel\\" );

        if(null != in && in.exists()) {
            Document doc;
            try {
                doc = Jsoup.parse(in, "UTF-8", "");
                //解析排名，图片和简介
                String rankDesc = doc.select("div.poi-placeinfo>div.infos>div>ul>li.rank").text();
                String photoSrc = doc.select("div.poi-placeinfo>a>p.coverphoto>img").attr("src");
                String summary = doc.select("div.compo-detail-info>div.poi-detail>div>p").text();

            } catch (IOException e) {
                //logger.error("解析报错：id= {}, {}",  po.getId(), e);
            }

        }
    }

    public void selectpath(String page){
        /*List<Selectable> list = page.getHtml().xpath("//div[@class='place-home-card4']//a").nodes();

        List<> countryList = Lists.newArrayList();

        for(Selectable selectable :list) {

            Selectable urlsele = selectable.xpath("//a/@href");
            Selectable namesele = selectable.xpath("//a//span/text()");
            Selectable nameEnsele = selectable.xpath("//a//em/text()");
            if(null != urlsele && null != namesele && null != nameEnsele) {
                String url = urlsele.toString();
                String name = namesele.toString().trim();
                String nameEn = nameEnsele.toString().trim();
                String code = url.split("/")[3];
            }*/
    }
}
