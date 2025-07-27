package pages;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductPage {

    WebDriver driver;
    WebDriverWait wait;
    Logger logger = Logger.getLogger(ProductPage.class.getName());
    public ProductPage(WebDriver driver){
        this.driver= driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

   public   void getProduct() {
        WebElement product=null;
        try {
            List<WebElement> ProductList = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.search-products-view__search-results > section > ul.product-grid__product-list > li"))
            );
            if (ProductList.isEmpty()) {
                logger.warning("Ürün bulunamadı.");
                return;
            }
            Random random = new Random();
            int randomIndex = random.nextInt(ProductList.size());
            WebElement RandomProduct = ProductList.get(randomIndex);
            product = RandomProduct.findElement(By.cssSelector("div:nth-of-type(1)>a"));
            product.click();
            logger.info("Rastgele ürün seçildi ve detayına gidildi.");
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Ürün seçilirken hata oluştu.", e);
        }

    }
    public boolean isProductPage()
    {
        try {
            WebElement productTitle =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.product-detail-info__header-name")));
            return productTitle.isDisplayed();
        }catch (Exception e){
            logger.log(Level.WARNING,"Ürün detay sayfası görüntülenmedi",e);
            return false;
        }

    }





}
