package com.hw.webmagic.PageProcessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;

public class QuestionBankPageProcess implements PageProcessor {
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(100)
            .addCookie("grwng_uid","d016d36c-4829-4c6f-b9c0-7e9d9ac14876")
            .addHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
    @Override
    public void process(Page page) {
           //page.getHtml().regex("https://api.growingio.com/v2/9212495b3b2ffbcd/web/action?stm=1585146448714")
        String company =page.getHtml().xpath("//*[@id=\"questionModule\"]/ul/li[1]/div/div[1]/div/div[1]/text()").toString();
        System.out.print(company);


    }

    @Override
    public Site getSite() {
        return site;
    }
    //https://wx.233.com/tiku/chapter/534
    //https://api.growingio.com/v2/9212495b3b2ffbcd/web/action?stm=1585146448714
    //https://api.growingio.com/v2/9212495b3b2ffbcd/web/action?stm=1585146582627
    public static void main(String[] args) {
        //RedisScheduler scheduler = new RedisScheduler(new JedisPool("10.100.124.206", 6379));
//    	FileCacheQueueScheduler scheduler = new FileCacheQueueScheduler("urls");
        //QueueScheduler scheduler = new QueueScheduler();
        //spider.setScheduler(scheduler);
        Spider.create(new QuestionBankPageProcess())
                .addUrl("https://wx.233.com/tiku/chapter/do/44589f65ae9808deb3b2d72cdd193769?isAutoDelRight=0&extractType=0&fromUrl=http://wx.233.com/tiku/chapter/534")
                .setScheduler(new QueueScheduler()) //默认自动去除，关键是对应的去除数据不能入库
                .addPipeline(new FilePipeline("D:/webmagic"))
                .thread(5).run();
    }

/*    public static void main(String[] args) {
        String url = "https://place.qyer.com/poi.php";
        Map<String, Object> paraMap = Maps.newHashMap();
        paraMap.put("action", "list_json");
        paraMap.put("type", "city");
        paraMap.put("page", 1);
        paraMap.put("pid", 50);
        paraMap.put("sort", 32);

        Request request = new Request();
        request.setUrl(url);
        request.setRequestBody(HttpRequestBody.form(paraMap, "UTF-8"));
        request.setMethod(HttpConstant.Method.POST);
        Spider.create(new CityTravelPageProcessor())
                .addRequest(request)
                .addPipeline(new ConsolePipeline())
                .run();
    }*/
}


