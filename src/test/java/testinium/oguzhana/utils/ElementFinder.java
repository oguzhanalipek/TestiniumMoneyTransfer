package testinium.oguzhana.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testinium.oguzhana.base.BaseTest;

public class ElementFinder extends BaseTest {
    private static final Logger logger = LogManager.getLogger(ElementFinder.class);

    public By getElementInfoToBy(ElementInfo elementInfo) {
        if (elementInfo == null) {
            logger.error("ElementInfo null olamaz");
            throw new IllegalArgumentException("ElementInfo null olamaz");
        }

        String type = elementInfo.getType();
        String value = elementInfo.getValue();

        if (type == null || value == null) {
            logger.error("Element type veya value null olamaz - Type: '{}', Value: '{}'", type, value);
            throw new IllegalArgumentException("Element type veya value null olamaz");
        }

        try {
            switch (type.toLowerCase()) {
                case "css":
                    return By.cssSelector(value);
                case "name":
                    return By.name(value);
                case "id":
                    return By.id(value);
                case "xpath":
                    return By.xpath(value);
                case "linktext":
                    return By.linkText(value);
                case "partiallinktext":
                    return By.partialLinkText(value);
                default:
                    logger.error("Gecersiz element type: '{}'", type);
                    throw new IllegalArgumentException("Gecersiz element type: " + type);
            }
        } catch (Exception e) {
            logger.error("Element olusturulurken hata: Type: '{}', Value: '{}'", type, value);
            throw e;
        }
    }

    public WebElement findElement(String key, WebDriver driver, int timeout) {
        try {
            WebDriverWait webDriverWait = new WebDriverWait(driver, timeout);
            By infoParam = getElementInfoToBy(findElementInfoByKey(key));

            // Elementin varlığını bekle
            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.presenceOfElementLocated(infoParam));

            // Elementi görünür alana kaydır
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                        webElement);
            } catch (JavascriptException e) {
                logger.warn("Element scroll islemi basarisiz: '{}'", key);

            }

            logger.debug("Element bulundu: '{}'", key);
            return webElement;

        } catch (TimeoutException e) {
            logger.error("'{}' elementi {} saniye icinde bulunamadi", key, timeout);
            throw e;
        } catch (Exception e) {
            logger.error("'{}' elementi bulunurken hata olustu", key);
            throw e;
        }
    }


}
