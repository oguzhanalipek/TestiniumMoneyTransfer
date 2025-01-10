package testinium.oguzhana.steps;

import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testinium.oguzhana.base.BaseTest;
import testinium.oguzhana.utils.ElementFinder;

public class StepImplementation extends BaseTest {

    public StepImplementation() {
        initMap(getFileList());
    }

    private static final Logger logger = LogManager.getLogger(StepImplementation.class);

    private final ElementFinder elementFinder = new ElementFinder();

    WebElement findElement(String key) {
        return elementFinder.findElement(key, driver, 30);
    }

    public boolean isElementVisible(String key, long timeout) {
        try {
            // Başlangıç zamanını al
            long startTime = System.currentTimeMillis();

            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementFinder.getElementInfoToBy(findElementInfoByKey(key))));

            // Bitiş zamanını al
            long endTime = System.currentTimeMillis();

            // Süreyi hesapla (milisaniyeden saniyeye çevir)
            double duration = (endTime - startTime) / 1000.0;

            logger.info("'{}' elementi {} saniye içinde görünür oldu (Timeout: {} sn)",
                    key, duration, timeout);
            return true;

        } catch (TimeoutException e) {
            logger.info("'{}' elementi {} saniye içinde görünür olmadı", key, timeout);
            return false;
        } catch (Exception e) {
            logger.warn("'{}' elementi kontrol edilirken hata oluştu", key);
            return false;
        }
    }

    @Step("<key> elementinin gorunur oldugu dogrulanir")
    public void verifyElementIsVisible(String key) {
        Assertions.assertTrue(isElementVisible(key, 10),
                key + " elementinin oldugu dogrulanamadi!");
        logger.info("'{}' elementinin sayfada oldugu dogrulandi!", key);
    }

    @Step("<key> elementinin text bilgisini logla")
    public void logElementText(String key) {
        if (isElementVisible(key, 10)) {
            String elementText = findElement(key).getText();
            logger.info("'{}' elementinin text icerigi: '{}'", key, elementText);
        } else {
            Assertions.fail(key + " elementi gorunur olmadi!");
        }
    }

    @Step("<key> elementinin text iceriginin <expectedText> bilgisine esit oldugu dogrulanir")
    public void verifyElementText(String key, String expectedText) {
        verifyElementIsVisible(key);
        WebElement element = findElement(key);
        String elementText = element.getText();
        Assertions.assertEquals(expectedText, elementText,
                key + " elementinin text icerigi beklenen '" + expectedText + "' ile eslesmiyor. Elementin text icerigi: '" + elementText);
        logger.info("'{}' elementinin text icerigi dogrulandi. Beklenen: '{}', Mevcut: '{}'", key, expectedText, elementText);
    }

    @Step("Mevcut URL <expectedUrl> bilgisini icerdigi dogrulanir")
    public void verifyUrlContainsText(String expectedUrl) {
        String actualUrl = driver.getCurrentUrl();

        if (actualUrl != null && actualUrl.contains(expectedUrl)) {
            logger.info("Mevcut URL beklenen '{}' bilgisini iceriyor", expectedUrl);
            logger.info("Mevcut URL: '{}'", actualUrl);
        }
    }

    @Step("<key> elementini bul, temizle ve <text> degeri girilir")
    public void clearAndSendKeys(String key, String text) {
        WebElement element = findElement(key);
        element.clear();
        element.sendKeys(text);
        logger.info("'{}' elementine '{}' degeri girildi", key, text);
    }

    @Step("<key> elementini bul ve temizle")
    public void clearElement(String key) {
        WebElement element = findElement(key);
        element.clear();
        logger.info("'{}' elementi temizlendi", key);
    }

    @Step("<key> elementini bekle ve sonra tikla")
    public void waitForElementAndClick(String key) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            if (isElementVisible(key, 10)) {
                WebElement element = findElement(key);
                wait.until(ExpectedConditions.elementToBeClickable(elementFinder.getElementInfoToBy(findElementInfoByKey(key))));
                logger.info("'{}' elementi tiklanabilir olana kadar beklendi!", key);

                element.click();
                logger.info("'{}' elementine tiklandi", key);

            }
        } catch (TimeoutException e) {
            logger.warn("'{}' elementi tiklanabilir olana kadar beklenirken zaman asimina ugradı.", key);
            throw e;
        } catch (NoSuchElementException e) {
            logger.error("'{}' elementi bulunamadi!", key);
            throw e;
        } catch (StaleElementReferenceException e) {
            logger.warn("'{}' elementi gecersiz hale geldi, sayfa yenilendi veya element degisti.", key);
            throw e;
        } catch (Exception e) {
            logger.error("'{}' elementi ile ilgili beklenmedik bir hata olustu: {}", key, e.getMessage());
            throw e;
        }
    }


}
