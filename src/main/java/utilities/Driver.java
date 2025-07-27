package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;

public class Driver {

    private  static WebDriver driver;
    private Driver(){};
    public static WebDriver getDriver() {
        if(driver == null)
        {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled"); // Otomasyon tespiti engellenir
            options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation")); // 'Chrome is being controlled...' mesajı engellenir
            options.setExperimentalOption("useAutomationExtension", false); // Ek uzantıları devre dışı bırakır
            WebDriverManager.chromedriver().setup();
            driver= new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return  driver;
    }
/*
    public void CloseDriver()
    {
        if(driver != null)
        {
            driver.quit();
            driver=null;
        }
    }*/
}
