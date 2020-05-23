package com.hw.webmagic.PageProcessor;

import com.hw.webmagic.dao.SuccDetailDao;
import com.hw.webmagic.entity.SuccDetail;
import com.hw.webmagic.util.SQLiteDBUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SuccDetailPageProcess implements PageProcessor {
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000);
    private SuccDetailDao succDetailDao=new SuccDetailDao();

    Connection con = null;
    @Override
    public void process(Page page) {
        SuccDetail succDetail=new SuccDetail();
        if (page.getUrl().regex("https://www.trjcn.com/success_case/all.html*").match()) {
            //<a href=.*</a>
            //wap_pager_data
            //System.out.print(page.getHtml());
            List<String> urls = page.getHtml().css("#wap_pager_data")
                    .links()
                    .regex("https://www.trjcn.com/success_case/detail.*html")
                    .all();
            //"https://www.trjcn.com"
            //丢在待爬取的url队列中去
            page.addTargetRequests(urls);
            //拿到的urls 如何去重,知道的去重
        } else {
            Document doc = Jsoup.parse(page.getHtml().get());
            //从详情页面获取数据----图片
            String cover =  "https://www.trjcn.com" +doc.getElementsByClass("video-box").attr("src");
            String title = doc.select("div.succ-detail-info-content>h5.fn-text2-overflow").text();
            String need = doc.select("div.succ-detail-info-list section").get(0).select("aside").text();
            String success = doc.select("div.succ-detail-info-list section").get(1).select("aside").text();
            String type = doc.select("div.succ-detail-info-list section").get(2).select("aside").text();
            String area = doc.select("div.succ-detail-info-list section").get(3).select("aside").text();
            String praise = doc.select("div.succ-detail-02").text();

        //把所有的数据都往数据库丢
            succDetail.setCover(cover);
            succDetail.setTitle(title);
            succDetail.setNeed(need);
            succDetail.setSuccess(success);
            succDetail.setType(type);
            succDetail.setArea(area);
            succDetail.setPraise(praise);
            try {
                con = SQLiteDBUtil.getConnection();
                succDetailDao.insert(con,succDetail);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //https://www.trjcn.com/success_case/detail/2681.html

        // 配置动态代理 0.7版本以后
       /* HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("36.249.119.34", 9999)
                ,new Proxy("182.34.36.100", 9999)));*/
        for (int i = 1; i <50 ; i++) {
            String url = "https://www.trjcn.com/success_case/all.html?1=1&page=" + i;
            Spider.create(new SuccDetailPageProcess())
                    .addUrl(url)
                    .setScheduler(new QueueScheduler()) //默认自动去除，关键是对应的去除数据不能入库
                    .addPipeline(new FilePipeline("D:/webmagic"))
                    .thread(3).run();
        }
    }
}
