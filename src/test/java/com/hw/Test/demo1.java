package com.hw.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Random;


/**
 * 新版本极验官网测试旧版滑块算法（失败）
 * 有需求可对接打码平台
 *
 */
public class demo1 {

    public static void main(String[] args) throws Exception {
        run1();
    }

    public static void decodeBase64ToImage(String base64) {
        Decoder decoder = Base64.getDecoder();
        try {
            File file = new File("E:\\a1.png");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream write = new FileOutputStream(new File("E:\\a1.png"));
            byte[] decoderBytes = decoder.decode(base64.replace("data:image/png;base64,", ""));
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decodeBase64ToImage1(String base64) {
        Decoder decoder = Base64.getDecoder();
        try {
            File file = new File("E:\\a2.png");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream write = new FileOutputStream(new File("E:\\a2.png"));
            byte[] decoderBytes = decoder.decode(base64.replace("data:image/png;base64,", ""));
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void doGet(String url) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;

        HttpEntity entity = null;
        String s = "";
        try {
            httpget.addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
            response = httpclient.execute(httpget);

            entity = response.getEntity();

            System.out.println("doGet" + response.getStatusLine().getStatusCode());
            s = EntityUtils.toString(entity, "UTF-8");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpclient.close();

        }
    }

    // 判断像素是否相差过大
    private static boolean isSimilar(BufferedImage image1, BufferedImage image2, int x, int y) {
        int pixel1 = image1.getRGB(x, y);
        int pixel2 = image2.getRGB(x, y);

        int[] rgb1 = getRGB(pixel1);
        int[] rgb2 = getRGB(pixel2);

        for (int i = 0; i < 3; i++) {
            if (Math.abs(rgb1[i] - rgb2[i]) > 80) {
                return false;
            }
        }
        return true;
    }

    // 返回RGB数组
    private static int[] getRGB(int pixel) {
        int[] rgb = new int[3];
        rgb[0] = (pixel & 0xff0000) >> 16;
        rgb[1] = (pixel & 0xff00) >> 8;
        rgb[2] = (pixel & 0xff);
        return rgb;
    }

    // 计算移动距离
    private static int getDiffLocation(BufferedImage image1, BufferedImage image2) {
        int i = 0;
        for (int x = 0; x < 260; x++) {
            for (int y = 0; y < 116; y++) {
                if (isSimilar(image1, image2, x, y) == false) {
                    return x;
                }
            }
        }
        return i;
    }

    public static void run1() throws Exception {
        System.getProperties().setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32(1)\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\Chrome\\Chrone.exe");
        WebDriver driver = new ChromeDriver(options);
        //	 driver.manage().window().maximize();
        driver.get("http://www.geetest.com/type/");
        //暴力延迟
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@class='products-content']/ul/li[3]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@class='products-content']/ul/li[2]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@class='geetest_radar_btn']/div[@class='geetest_radar_tip']/span")).click();
        System.out.println(driver.findElement(By.xpath("//div[@class='geetest_radar_tip']/span")).getText());
        Thread.sleep(1000);
        //执行js 获取验证码图片 画布转base64
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String jsstr = "var oCanvas =document.getElementsByClassName('geetest_canvas_fullbg geetest_fade geetest_absolute')[0];return oCanvas.toDataURL();";
        String o = (String) js.executeScript(jsstr);
        decodeBase64ToImage(o);
        //System.out.println(o);
        String jsstr1 = "var oCanvas =document.getElementsByClassName('geetest_canvas_bg geetest_absolute')[0];return oCanvas.toDataURL();";
        String o1 = (String) js.executeScript(jsstr1);
        decodeBase64ToImage1(o1);
        // driver.findElement(By.className("geetest_radar_tip_content")).click();

        // 获取移动距离
        Random random = new Random();
        BufferedImage image1 = ImageIO.read(new FileInputStream("E:/a1.png"));
        BufferedImage image2 = ImageIO.read(new FileInputStream("E:/a2.png"));
        int ranAddLoc = -2;
        int loc = (getDiffLocation(image1, image2) - 5) + ranAddLoc;
        System.out.println(loc);
        //移动算法
        List<Integer> trackList = getTrackList3(loc);
        Thread.sleep(200);
        // 找到滑动的圆球
        WebElement slider = driver.findElement(By.xpath("//div[@class='geetest_slider_button']"));

        int y = slider.getSize().height;

        // 鼠标按住滑块
        Actions actions = new Actions(driver);
        actions.clickAndHold(slider).perform();
        int a = 0;
        Point  start =slider.getLocation();
        System.out.println(slider.getLocation().toString());
        //actions.moveToElement(slider,loc, y).perform();
        Thread.sleep(500+random.nextInt(500));
        //actions.moveToElement(slider,200, y).perform();
        //transform: translate(0px, 0px);
        //div.style.transform = "translate(500px)";

		/*String jsstr3 = "var e =document.getElementsByClassName('geetest_slider_button')[0];e.style.transform = 'translate("+100+"px,0px)';";
		js.executeScript(jsstr3);*/
        //Thread.sleep(1000);
        //System.out.println(slider.getLocation().toString());
        //actions.moveToElement(slider,0, y).perform();
		/*String jsstr4 = "var d =document.getElementsByClassName('geetest_slider_button')[0];d.style.transform = 'translate("+20+"px,0px)';";
		js.executeScript(jsstr4);*/
        //Thread.sleep(1000);
        System.out.println(slider.getLocation().toString());
        for (int i = 0; i < trackList.size(); i++) {
            a += trackList.get(i);
            //System.out.println("滑了" + a);

            actions.clickAndHold(slider).moveByOffset(trackList.get(i), 0);
            if(i<trackList.size()*4/5) {
                actions.pause(random.nextInt(20)+100);
            }else {
                actions.pause(random.nextInt(20)+200);
            }

            //	actions.wait(200);
            //actions.moveToElement(slider,a, y - random.nextInt(1)-5).build().perform();
            //actions.moveToElement(slider).moveByOffset(xOffset, yOffset)
            //Thread.sleep(1000);

            //System.out.println(slider.getLocation().toString());
			/*if(slider.getLocation().getX()-start.getX()-loc>-20) {
				actions.moveToElement(slider,loc, y - random.nextInt(1)-5).build().perform();
				System.out.println("callback");
				break;
			}*/
        }


        //Thread.sleep(200);
        System.out.println("释放前"+slider.getLocation().toString());
        actions.release(slider).build().perform();
        System.out.println("释放后"+slider.getLocation().toString());
    }
    //获取轨迹
    public static List<Integer> getTrackList(int loc) {
        List<Integer> list = new ArrayList<>();
        list.add(loc * 4 / 15);
        list.add(loc * 2 / 15);
        list.add(loc * 4 / 15);
        list.add(loc * 5 / 24);
        list.add(loc - (loc * 4 / 15 + loc * 2 / 15 + loc * 4 / 15 + loc * 5 / 24));
        /*
         * list.add(loc*2/3); list.add(loc-loc*2/3);
         */
        /*
         * System.out.println(loc*4/15+loc*2/15+loc*4/15+loc*5/24+loc-(loc*4/15+loc*2/15
         * +loc*4/15+loc*5/24)); System.out.println(loc);
         */
        return list;
    }
    //获取轨迹2
    @Test
    public  void getTrackList2() {
        int loc;
        loc=100;
        List<Integer> list = new ArrayList<>();
        double mid = loc * 4.0 / 5;
        // 计算间隔
        double t = 0.2;
        // 初速度
        double v = 0.0;
        double current = 0.0;
        while (current < loc) {
            double a;
            if (current < mid) {
                // 加速度为正2
                a = loc*1.0;
            } else {
                // 加速度为负3
                a = -loc*1.5;
            }
            // 初速度v0
            double v0 = v;
            // 当前速度v = v0 + at
            v = v0 + a * t;
            // 移动距离x = v0t + 1/2 * a * t^2
            double move = v0 * t + 1 / 2 * a * t * t;
            // 当前位移
            current += move;
            // 加入轨迹
            // int mov1=;
            System.out.println(move);
            System.out.println(" "+current);
            list.add((int) (move));
        }
        //return null;
    }
    //获取轨迹3
    //	@Test
    public  static List<Integer> getTrackList3(int loc) {
			/*int loc;
			loc=100;*/
        List<Integer> list = new ArrayList<>();
        int mid = loc * 4/ 5;
        // 计算间隔
        double t = 0.2;
        // 初速度
        double v = 0;
        int current = 0;
        while (current < loc) {
            double a;
            if (current < mid) {
                // 加速度为正2
                a = loc*1.0;
            } else {
                // 加速度为负3
                a = -loc*1.5;
            }
            // 初速度v0
            double v0 = v;
            // 当前速度v = v0 + at
            v = v0 + a * t;
            // 移动距离x = v0t + 1/2 * a * t^2
            int move = (int) (v0 * t + 1 / 2 * a * t * t);
            // 当前位移
            current += move;
            // 加入轨迹
            // int mov1=;

            list.add(move);
        }
        list.add(loc-current);
        int a=0;
			/*for (Integer integer : list) {
				a+=integer;
				System.out.println(integer);
				System.out.println(a);
			}*/
        return list;
    }

}