package tests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductPage;
import pages.SearchPage;
import utilities.Driver;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;


public class ZaraTest {

    WebDriver driver;
    WebDriverWait wait;
    Logger logger = Logger.getLogger(ZaraTest.class.getName());
    @Before
    public void CreateDriver(){
        driver = Driver.getDriver();
        driver.get("https://www.zara.com/tr/");
        logger.info("WebDriver başlatıldı.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @Test
    public void loginSuccessTest()
    {
        try {

            LoginPage lp = new LoginPage(driver);
            lp.Login("kbrasenn@hotmail.com", "Test123.");
            assertTrue("Başarılı giriş olmalı ama başarısız oldu!", lp.isLoginSucces());
            logger.info("Giriş başarılı şekilde tamamlandı.");

        }catch (Exception e) {
            logger.log(Level.SEVERE, "Login testinde hata oluştu.", e);

        }
    }
    @Test
    public void loginFailTest()
    {
        try {
            LoginPage lp = new LoginPage(driver);
            lp.Login("kbrasenn@hotmail.com", "YanlisSifre.");
            assertTrue("Hatalı girişte popup bekleniyordu ama görünmedi!", lp.isLoginErrorPopupVisible());

        }catch (Exception e) {
            logger.log(Level.SEVERE, "Login testinde hata oluştu.", e);

        }
    }
    @Test
    public  void searchProductTest(){
        try {
            SearchPage sp = new SearchPage(driver);
            sp.Search();
            logger.info("Arama işlemi yapıldı.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Arama sırasında hata oluştu.", e);
        }
    }
    @Test
    public  void addToCart(){
        try {

            SearchPage sp = new SearchPage(driver);
            sp.Search();
            logger.info("Ürün arandı.");

            ProductPage productPage = new ProductPage(driver);
            CartPage cartPage = new CartPage(driver);
            cartPage.closeModalIfPresent();
            logger.info("Açılır pencere kapatıldı (varsa).");
            productPage.getProduct();
            logger.info("Ürün detayına girildi.");
            assertTrue("Ürün detay sayfası açılmadı!", productPage.isProductPage());

            cartPage.addBasket();
            logger.info("Ürün sepete eklendi.");
            assertTrue("Ürün sepete eklenemedi!", cartPage.isProductInCart());


            logger.info("Ürün fiyatları karşılaştırıldı.");
            assertTrue("Ürün fiyatları uyuşmuyor!", cartPage.isPriceCorrect());


            logger.info("Ürün adedi artırıldı.");
            assertTrue("Ürün adedi artırılamadı!", cartPage.increaseProductQuantity());

            cartPage.deleteProductFromCart();
            logger.info("Ürün silindi");
            assertTrue("Ürün sepetten silinemedi!", cartPage.isEmptyCart());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Product testinde hata oluştu.", e);
        }
    }

    @After
    public void closeDriver(){
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }


}
