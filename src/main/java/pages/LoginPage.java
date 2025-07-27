package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
public class LoginPage {

    WebDriver driver;
    Logger logger = Logger.getLogger(LoginPage.class.getName());
    WebDriverWait wait;
    public  LoginPage(WebDriver Driver){

        this.driver =Driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void Login(String email, String pass)
    {
        try{

            logger.info("Login işlemi başlatıldı.");
            wait.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector("button#onetrust-accept-btn-handler")))).click();
            logger.info("Çerez bildirimi kabul edildi.");
            wait.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector("a[data-qa-id= 'layout-header-user-logon']")))).click();
           // logger.info("Giriş yap bağlantısına tıklandı.");
           // wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(("button[data-qa-id = 'oauth-logon-button']")))).click();
           logger.info("Email adresi girildi.");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[data-qa-input-qualifier='logonId']"))).sendKeys(email);
            logger.info("Şifre girildi.");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(("input[data-qa-input-qualifier = 'password']")))).sendKeys(pass);
            logger.info("Giriş formu gönderildi.");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(("button[data-qa-id='logon-form-submit']")))).click();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Giriş işlemi sırasında hata oluştu.", e);
        }

    }
    public  boolean isLoginErrorPopupVisible(){
        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h1.zds-dialog-title.zds-alert-dialog__title")));
            return errorMessage.isDisplayed();

        } catch (Exception e) {
            return  false;

        }

    }
    public  boolean isLoginSucces(){
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("a[data-qa-id='layout-header-user-account']"))).isDisplayed();
        }catch (Exception e) {
            return false;
        }
    }
}
