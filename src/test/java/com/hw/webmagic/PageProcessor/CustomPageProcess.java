package com.hw.webmagic.PageProcessor;

import com.hw.webmagic.dao.CustomDetailDao;
import com.hw.webmagic.entity.CustomDetail;
import com.hw.webmagic.util.MySQLDBUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomPageProcess implements PageProcessor {


    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(100);
    private CustomDetailDao custdao = new CustomDetailDao();
    Connection con = null;

    public void process(Page page) {
        CustomDetail cduser = new CustomDetail();

        //拿到标签下面的具体的内容 /text()
        //获取标签里面的属性 @href
        //https://www.trjcn.com/investor/info/76.html
        //拿到自身想要的url详情页面，添加待执行队列
        if (page.getUrl().regex("https://www.trjcn.com/investor/home/search.html*").match()) {

            List<String> urls = page.getHtml().xpath("//*[@id=\"J_content\"]")
                    .links()
                    .regex("https://www.trjcn.com/investor/info.*html").all();
            //丢在待爬取的url队列中去
            page.addTargetRequests(urls);
            //拿到的urls 如何去重
        } else {
            //Selectable address =page.getHtml().xpath("//*[@id=\"page\"]/div[3]/div/div[2]/div[1]/div[1]/text()");

            Document doc = Jsoup.parse(page.getHtml().get());
            //从详情页面获取数据----图片
            String avatar = "https://www.trjcn.com" + doc.select("div.zytzr-con-left>div.pic>img").attr("src");
            //Node.absUrl(String key)

            //从详情页面获取数据----姓名
            String name = doc.select("div.zytzr-con-right>h3").text();
            String company = doc.select("div.zytzr-con-right p").get(0).text();
            String position = doc.select("div.zytzr-con-right p").get(1).text();
            String address = doc.select("div.zy-right div.zy-right-item").get(0).select("div.zy-item-info").text();
            String type = doc.select("div.zy-right div.zy-right-item").get(2).select("div.zy-item-info").text();
            String phase = doc.select("div.zy-right div.zy-right-item").get(3).select("div.zy-item-info").text();
            String field = doc.select("div.zy-right div.zy-right-item").get(4).select("div.zy-item-info").text();


            Elements Elementslist = doc.select("div.content-left div.directly-con-title");

            for (Element element : Elementslist) {
                String leftname = element.text();

                if (leftname.equals("个人介绍")) {
                    //返回第一个兄弟节点
                    String userDesc = element.nextElementSibling().text();
                    cduser.setUserDesc(userDesc);
                }
                if (leftname.equals("投资案例")) {
                    String CompanyDesc = element.nextElementSibling().text();
                    cduser.setCompanyDesc(CompanyDesc);
                }
            }
            //Selectable  =page.getHtml().xpath("//*[@id=\"page\"]/div[4]/div[1]/div[1]/div[2]/p/text()");


            //把所有的数据都往数据库丢
            cduser.setAvatar(avatar);
            cduser.setName(name);
            cduser.setCompany(company);
            cduser.setPosition(position);
            cduser.setAddress(address);
            cduser.setType(type);
            cduser.setPhase(phase);
            cduser.setField(field);

            try {
                con = MySQLDBUtil.getConnection();
                int addSum = custdao.add(con, cduser);
                if (addSum > 0) {
                    //System.out.println("保存成功");
                    System.out.println(cduser);
                } else {
                    System.out.println("false");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //RedisScheduler scheduler = new RedisScheduler(new JedisPool("10.100.124.206", 6379));
//    	FileCacheQueueScheduler scheduler = new FileCacheQueueScheduler("urls");
        //QueueScheduler scheduler = new QueueScheduler();
        //spider.setScheduler(scheduler);
        //https://www.trjcn.com/investor/home/search.html?_=1&page=3

        // 配置动态代理 0.7版本以后
         HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
         httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));


        for (int i = 1; i < 7; i++) {
            String url = "https://www.trjcn.com/investor/home/search.html?_=1&page=" + i;
            Spider.create(new CustomPageProcess())
                    .setDownloader(httpClientDownloader)
                    .addUrl(url)
                    .setScheduler(new QueueScheduler()) //默认自动去除，关键是对应的去除数据不能入库
                    .addPipeline(new FilePipeline("D:/webmagic"))
                    .thread(2).run();
        }

    }


 /*   public static void getInfo() throws InterruptedException {

        // 配置动态代理
        // HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        // httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new
        // Proxy("forward.xdaili.cn", 80)));
        // spider.setDownloader(httpClientDownloader);

        // 配置混播代理
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        String json = httpGet();
        System.out.println("初始化代理");
        System.out.println("初始化代理");
        System.out.println("初始化代理");
        System.out.println("初始化代理");
        System.out.println(json);
        System.out.println("初始化代理");
        System.out.println("初始化代理");
        System.out.println("初始化代理");
        System.out.println("初始化代理");
        JSONObject jo = JSONObject.parseObject(json);
        List<Map<String, String>> ips = (List<Map<String, String>>) jo.get("RESULT");
        httpClientDownloader.setProxyProvider(
                SimpleProxyProvider.from(new Proxy(ips.get(0).get("ip"), Integer.parseInt(ips.get(0).get("port"))),
                        new Proxy(ips.get(1).get("ip"), Integer.parseInt(ips.get(1).get("port"))),
                        new Proxy(ips.get(2).get("ip"), Integer.parseInt(ips.get(2).get("port"))),
                        new Proxy(ips.get(3).get("ip"), Integer.parseInt(ips.get(3).get("port"))),
                        new Proxy(ips.get(4).get("ip"), Integer.parseInt(ips.get(4).get("port")))));

        spider.setDownloader(httpClientDownloader);
        spider.start();
        Thread.sleep(12000);
        spider.stop();
        Thread.sleep(3000);
        System.gc();
        getInfo();
    }
}*/
    /*// 自定义实现Pipeline接口
    class MysqlPipeline implements Pipeline {

        public MysqlPipeline() {
        }

        public void process(ResultItems resultitems, Task task) {
            Map<String, Object> mapResults = resultitems.getAll();
            Iterator<Entry<String, Object>> iter = mapResults.entrySet().iterator();
            Map.Entry<String, Object> entry;
            // 输出到控制台
            while (iter.hasNext()) {
                entry = iter.next();
                System.out.println(entry.getKey() + "：" + entry.getValue());
            }
            // 持久化
            News news = new News();
            if (!mapResults.get("Title").equals("")) {
                news.setTitle((String) mapResults.get("Title"));
                news.setContent((String) mapResults.get("Content"));
            }
            try {
                InputStream is = Resources.getResourceAsStream("conf.xml");
                SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
                SqlSession session = sessionFactory.openSession();
                session.insert("add", news);
                session.commit();
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
