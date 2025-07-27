package pages;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.fail;

public class CartPage {

    WebDriver driver;
    WebDriverWait wait;
    Logger logger = Logger.getLogger(CartPage.class.getName());
    public CartPage(WebDriver driver){
        this.driver= driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void addBasket(){

        int step=0;
        while (step<3){
            try {
                WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[data-qa-action='add-to-cart']")));
                addToCartButton.click();
                logger.info("Ürün sepete eklendi.");
                break;
            }catch (StaleElementReferenceException e)
            {
                logger.warning("Buton bulunamadı, tekrar deneniyor...");
                step++;
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException ex)
                {
                    logger.log(Level.SEVERE, "Thread durdu", ex);
                }
            }
        }
        List<WebElement> AvailableSizeList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("button[data-qa-action='size-in-stock']")));
        Random random= new Random();
        int randomIndex = random.nextInt(AvailableSizeList.size());
        WebElement size=AvailableSizeList.get(randomIndex);
        String ProductName= driver.findElement(By.cssSelector("h1[data-qa-qualifier='product-detail-info-name']")).getText();


        String ProducPrice="";
        try {
            WebElement discountedPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ins[data-qa-id='price-container-current']>span>div>span")));
            ProducPrice = discountedPrice.getText();
        } catch (TimeoutException e) {
            WebElement normalPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("span.money-amount__main")));
            ProducPrice = normalPrice.getText();
        }

        writeProductInfo("src/test/resources/ProductInfo.txt", ProducPrice,ProductName);
        logger.info("Ürün bilgileri dosyaya yazıldı: " + ProductName + " - " + ProducPrice);
        size.click();
        logger.info("Ürün beden seçimi yapıldı.");
        try {
            WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.add-to-cart-notification__container")));
            wait.until(ExpectedConditions.invisibilityOf(notification));
        } catch (TimeoutException e) {
            logger.warning("Bildirim çıkmadı veya çok uzun sürdü.");
        }
    }
    public void writeProductInfo(String FilePath, String ProductPrice, String ProductName)
    {
        try{
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(FilePath,true));
            // true : Dosya var içeriğini sildirmez
            bufferedWriter.write("Ürün adı : " + ProductName);
            bufferedWriter.newLine();
            bufferedWriter.write("Ürün fiyatı: "+ ProductPrice);
            bufferedWriter.newLine();
            bufferedWriter.write("-------------------");
            bufferedWriter.newLine();
            bufferedWriter.close();

        }catch (IOException e) {
            logger.log(Level.SEVERE, "Dosyaya yazma sırasında hata oluştu.", e);
        }
    }
    public void comparePrice(){

        try {
            try {
                // Önce modal'daki sepete git butonunu dene
                WebElement cartButtonInModal = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[data-qa-action='nav-to-cart'] span")));
                cartButtonInModal.click();
                logger.info("Modal üzerindeki sepete git butonuna tıklandı.");
            } catch (TimeoutException e) {
                // Eğer modal'daki buton görünmüyorsa header'daki butona tıkla
                logger.warning("Modal sepet butonu bulunamadı, header butonu deneniyor.");
                WebElement cartButtonInHeader = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("a[layout-header-go-to-cart']")));
                cartButtonInHeader.click();
                logger.info("Header üzerindeki sepete git butonuna tıklandı.");
            }


            WebElement ProductPriceInCartElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.shop-cart-item-pricing__current span.money-amount__main")));
            String ProductPriceInCartPage = ProductPriceInCartElement.getText();
            String ProductPriceInProductPage = readFromFile("src/test/resources/ProductInfo.txt");
            if (ProductPriceInCartPage.equals(ProductPriceInProductPage)) {
                logger.info("Sepet sayfası ve ürün sayfasındaki fiyatlar eşleşiyor.");
            } else {
                logger.warning("Fiyatlar eşleşmiyor! Ürün: " + ProductPriceInProductPage + " - Sepet: " + ProductPriceInCartPage);
            }
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Fiyat karşılaştırması sırasında hata oluştu.", e);
        }

    }

    public String readFromFile(String filePath) {
        String lastPrice = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String tempPrice = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Ürün fiyatı: ")) {
                    tempPrice = line.substring("Ürün fiyatı: ".length()).trim();
                }
                if (line.startsWith("-------------------") && tempPrice != null) {
                    lastPrice = tempPrice; // Son bulunan fiyatı sakla
                    tempPrice = null;
                }
            }
            if (tempPrice != null) {
                lastPrice = tempPrice;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Dosyadan okuma sırasında hata oluştu.", e);
        }
        return lastPrice;
    }
    public boolean increaseProductQuantity(){
        try {
            WebElement plusButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[data-qa-id='add-order-item-unit']")));
            plusButton.click();

            WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.zds-quantity-selector__units.shop-cart-item-quantity")));

            // Bekle: input değeri "2" olana kadar
            boolean isUpdated = wait.until(ExpectedConditions.textToBePresentInElementValue(quantityInput, "2"));

            if (isUpdated) {
                logger.info("Ürün adeti 2'ye çıkarıldı.");
                return true;
            } else {
                logger.warning("Ürün adeti 2 olamadı.");
                return false;
            }

        }catch (Exception e) {
            logger.log(Level.SEVERE, "Ürün adeti artırılırken hata oluştu.", e);
            return false;
        }
    }
    public void deleteProductFromCart(){

        try{
            Thread.sleep(2000);
            WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.shop-cart-item-image__remove-item")));
            deleteBtn.click();
        }catch (InterruptedException e) {
            logger.warning("Silme işlemi sırasında kesinti oldu.");
            fail("Silme sırasında kesinti: " + e.getMessage());

        } catch (TimeoutException e) {
            logger.warning("Beklenen element görünmedi ama işlem tamamlanmış olabilir: " + e.getMessage());
        }
    }
    public void closeModalIfPresent() {
        try {
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.zds-modal button.close")));
            closeBtn.click();
            logger.info("Modal kapatıldı.");
        } catch (TimeoutException e) {

        }
    }
    public  boolean isPriceCorrect(){

        WebElement ProductPriceInCartElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.shop-cart-item-pricing__current span.money-amount__main")));
        String cardPrice = ProductPriceInCartElement.getText();
        String ProductPriceInProductPage = readFromFile("src/test/resources/ProductInfo.txt");
        return cardPrice.equals(ProductPriceInProductPage);
    }
    public boolean isProductInCart() {
        try {
            WebElement cartButtonInModal = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[data-qa-action='nav-to-cart'] span")));
            cartButtonInModal.click();

            WebElement productInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.shop-cart-grid-items")));
            return productInCart.isDisplayed();
        } catch (TimeoutException e) {
            logger.warning("Sepette ürün bulunamadı: " + e.getMessage());
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ürün kontrolü sırasında hata oluştu.", e);
            return false;
        }
    }
    public boolean isQuantityIncreased(){
        try {

            String quantityText = driver.findElement(By.cssSelector("input.zds-quantity-selector__units shop-cart-item-quantity")).getAttribute("value");
            int quantity = Integer.parseInt(quantityText);
            return  (quantity >1) ? true:false;


        }catch (Exception e) {
            logger.log(Level.SEVERE, "Ürün adeti artırılırken hata oluştu.", e);
            return false;
        }
    }

    public boolean isEmptyCart(){

        try{
            WebElement emptyTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.zds-empty-state__title")));
            return emptyTitle.isDisplayed();
        }catch (Exception e){
            return false;
        }

    }
}
