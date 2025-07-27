package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ReaderExcelData;
import java.time.Duration;
import java.util.logging.Logger;


public class SearchPage {

    WebDriver driver;
    Logger logger = Logger.getLogger(SearchPage.class.getName());
    WebDriverWait wait;
    public SearchPage(WebDriver driver){
        this.driver= driver;
         wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public  void Search()
    {
        logger.info("Çerez kabul butonuna tıklanıyor.");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#onetrust-accept-btn-handler"))).click();
        logger.info("Menü butonuna tıklanıyor.");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-qa-id='layout-header-toggle-menu']"))).click();
        logger.info("ERKEK menüsüne tıklanıyor.");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='ERKEK']/parent::a"))).click();
        logger.info("TÜMÜNÜ GÖR butonuna tıklanıyor.");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='TÜMÜNÜ GÖR']"))).click();
        //element bulunduktan sonra sayfa yenilendiği için fail verdi. (stale durumu) . Bu sebeple 3 kez denedim.
        int step=0;
        //sayfa stabilize olana kadar elementi 3 kere arıyorum. (dom render oluyor)
        while (step<3){
            try {
                logger.info("Arama linkine tıklanıyor.");
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-qa-id='header-search-text-link']"))).click();
                break;
            }catch (StaleElementReferenceException e)
            {
                step++;
                logger.warning("StaleElementReferenceException ile karşılaşıldı, tekrar deneniyor. Adım : " + step);
                try{
                    Thread.sleep(500);
                }catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        String FirstSearchWord= ReaderExcelData.getCellData("src/test/resources/Proje.xlsx",0,0);
        String SecondSearchWord= ReaderExcelData.getCellData("src/test/resources/Proje.xlsx",0,1);
        logger.info("İlk arama kelimesi: " + FirstSearchWord);
        WebElement searchInput= wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input#search-home-form-combo-input")));
        searchInput.sendKeys(FirstSearchWord);
        logger.info("Arama kutusu temizleniyor.");
        searchInput.sendKeys(Keys.CONTROL+"a");
        searchInput.sendKeys(Keys.DELETE);
        logger.info("İkinci arama kelimesi: " + SecondSearchWord + " giriliyor ve ENTER basılıyor.");
        searchInput.sendKeys(SecondSearchWord + Keys.ENTER);

    }
}
