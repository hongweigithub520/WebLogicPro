package com.hw.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;

public class SeleniumTest {
    public void StartChrome(){
        //-----------------------------打开Chrome浏览器---------------------------------------------
           File file_chrome = new File("C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
           System.setProperty("webdriver.ie.driver", file_chrome.getAbsolutePath());
           WebDriver my_dr = new ChromeDriver();// 打开chrome浏览器

            my_dr.get("https://www.baidu.com");
    }

    public static void main(String[] args) {
        SeleniumTest st=new SeleniumTest();
        st.StartChrome();
    }
}
