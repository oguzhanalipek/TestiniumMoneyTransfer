package testinium.oguzhana.steps;

import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testinium.oguzhana.base.BaseTest;
import testinium.oguzhana.utils.ElementFinder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class StepImplementation extends BaseTest {

    public StepImplementation() {
        initMap(getFileList());
    }

    private static final Logger logger = LogManager.getLogger(StepImplementation.class);

    private final ElementFinder elementFinder = new ElementFinder();

    private static String tempData;

    WebElement findElement(String key) {
        return elementFinder.findElement(key, driver, 30);
    }

    List<WebElement> findElements(String key) {
        return driver.findElements(elementFinder.getElementInfoToBy(findElementInfoByKey(key)));
    }

    @Step("<int> saniye bekle")
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            logger.warn("'{}' elementi kontrol edilirken hata oluştu: {}", key, e.getMessage());
            return false;
        }
    }

    @Step("<key> elementinin gorunur oldugu dogrulanir")
    public void verifyElementIsVisible(String key) {
        Assertions.assertTrue(isElementVisible(key, 10),
                key + " elementinin oldugu dogrulanamadi!");
        logger.info("'{}' elementinin sayfada oldugu dogrulandi!", key);
    }

    public void javascriptClicker(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Step("<key> elementine javascript ile tikla")
    public void clickToElementWithJavaScript(String key) {
        WebElement element = findElement(key);
        javascriptClicker(element);
        logger.info(key + " elementine javascript ile tiklandi!");
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

    @Step("<key> elementinin text bilgisini al ve hafizaya kaydet")
    public void getTextElementAndSave(String key) {
        if (isElementVisible(key, 10)) {

            String elementText = findElement(key).getText();
            tempData = elementText;
            logger.info(key + " elementinin text icerigi: " + elementText +
                    "\n'" + elementText + "' bilgisi hafizaya kaydedildi!");
        } else {
            Assertions.fail(key + " elementi gorunur olmadi!");
        }
    }

    @Step("Hafizaya kaydedilen degerin formatini double deger olacak sekilde guncelle <deger> kadar arttir ve kaydet")
    public void updateTempDataFormatAndIncrease(double deger) {
        try {
            String tempDataValue = tempData;

            double numericValue = Double.parseDouble(tempDataValue.replace(",", ""));

            numericValue += deger;

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

            tempData = decimalFormat.format(numericValue);

            logger.info("Hafizaya kaydedilen bilgi guncellendi ve " + deger + " kadar arttirildi: " + tempData);
        } catch (Exception e) {
            throw new RuntimeException("Hafizaya kaydedilen degerin formatini guncelleme ve arttirma isleminde bir hata olustu: " + e.getMessage(), e);
        }
    }

    @Step("<key> elementlerinin oldugu dogrulanir")
    public void confirmIfElementsFoundAndShowElementCount(String key) {
        Assertions.assertTrue(isElementVisible(key, 10),
                "Elementin oldugu dogrulanamadi");
        logger.info("'{}' elementlerinin sayfada oldugu dogrulandi!", key);
        logger.info("'{}' keyli elementlerin sayisi: {}", key, findElements(key).size());
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

    @Step("<key> elementinin text iceriginin bellege kaydedilen deger ile ayni oldugu dogrulanir")
    public void confirmIfTempDataEqualsElementText(String key) {
        String elementText = findElement(key).getText();
        Assertions.assertEquals(tempData, elementText,
                "Bellek tutulan deger ile " + key + " elementinin text icerigi ayni degil!");
        logger.info("Beklenen Sonuc: " + tempData + " - " + "Element Text: " + elementText);
    }

    @Step("<key> elementinin olmadigi dogrulanir")
    public void confirmIfElementNotFound(String key) {
        Assertions.assertFalse(isElementVisible(key, 4),
                "Elementin olmadigi dogrulanamadi");
        logger.info(key + " elementinin sayfada olmadigi dogrulandi!");
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

    @Step("<key> elementini bul, temizle ve kaydedilen degeri yaz")
    public void clearAndSendSavedData(String key) {
        WebElement webElement = findElement(key);
        webElement.clear();
        webElement.sendKeys(tempData);
        logger.info(key + " alanina " + tempData + " degeri yazildi ");
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

    @Step("<key> dropdowninda <expectedText> bilgisini iceren secenegin secili oldugu dogrulanir")
    public void verifySelectedDropdownOptionWithText(String key, String expectedText) {
        try {
            WebElement dropdownElement = findElement(key);

            Select dropdown = new Select(dropdownElement);

            String selectedText = dropdown.getFirstSelectedOption().getText();

            if (selectedText.equals(expectedText)) {
                logger.info("Dogrulama basarili: Secili deger beklenen metne esit.");
                logger.info("Secili Text: {} - Beklenen Text: {}", selectedText, expectedText);
            } else {
                throw new AssertionError("Doğrulama başarısız: Secili deger beklenen metne esit değil!" +
                        "Bulunan: " + selectedText + ", Beklenen: " + expectedText);
            }
        } catch (Exception e) {
            throw new RuntimeException("Dropdown dogrulama sirasinda bir hata olustu: " + e.getMessage(), e);
        }
    }

    @Step("<key> dropdowninda varsayilan olarak secili bir secenek oldugu dogrulanir ve secili olan secenek loglanir")
    public void logDefaultSelectedOption(String key) {
        try {
            WebElement dropdownElement = findElement(key);

            Select dropdown = new Select(dropdownElement);

            String selectedText = dropdown.getFirstSelectedOption().getText();

            Assertions.assertNotEquals("", selectedText,
                    "Dropdown'da varsayilan olarak secili bir secenek bulunamadi!");

            logger.info("Varsayılan secili seçenek: {}", selectedText);
        } catch (Exception e) {
            throw new RuntimeException("Dropdown dogrulamasi sirasında bir hata oluştu: " + e.getMessage(), e);
        }
    }

    @Step("<key> dropdowninda hafizaya kaydedilen Account name bilgisini iceren secenegin secili oldugu dogrulanir")
    public void verifySelectedDropdownOptionWithSavedData(String key) {
        try {
            WebElement dropdownElement = findElement(key);

            Select dropdown = new Select(dropdownElement);

            String selectedText = dropdown.getFirstSelectedOption().getText();

            if (selectedText.equals(tempData)) {
                logger.info("Dogrulama basarili: Secili deger hafizaya kaydedilen text bilgisi ile ayni");
                logger.info("Secili Text: {} - Kaydedilen Text: {}", selectedText, tempData);
            } else {
                throw new AssertionError("Doğrulama başarısız: Secili deger beklenen metne esit değil!" +
                        "Bulunan: " + selectedText + ", Beklenen: " + tempData);
            }
        } catch (Exception e) {
            throw new RuntimeException("Dropdown dogrulama sirasinda bir hata olustu: " + e.getMessage(), e);
        }
    }

    public String getAttribute(String key, String attribute) {
        String attributeText = findElement(key).getAttribute(attribute);
        logger.info(key + " elementinin '" + attribute + "' attribute icerigi: " + attributeText);

        return attributeText;
    }

    @Step("<key> elementinin <attribute> attribute iceriginin <expectedStr> oldugu dogrulanir")
    public void verifyIfKeyElementAttributeEqualsExpectedText(String key, String attribute, String expectedStr) {
        String elementAttributeText = getAttribute(key, attribute);

        if (!elementAttributeText.equals(expectedStr)) {
            Assertions.fail(key + " elementinin '" + attribute + "' attribute degeri beklenen " + expectedStr + " bilgiye esit degil!");
        } else {
            logger.info(key + " elementinin '" + attribute + "' attribute degeri beklenen " + expectedStr + " bilgisine esit");
        }
    }

    @Step("<key> elementinin <attribute> attribute degerinin negatif olmadigi dogrulanir")
    public void verifyIfKeyElementValueIsNotNegative(String key, String attribute) {
        try {
            String valueAttributeText = getAttribute(key, attribute);

            double value = Double.parseDouble(valueAttributeText);

            if (value < 0) {
                Assertions.fail(key + " elementinin 'value' attribute degeri negatif: " + value);
            } else {
                logger.info(key + " elementinin 'value' attribute degeri negatif değil: " + value);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(key + " elementinin 'value' attribute degeri sayiya cevrilemedi: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(key + " elementinin 'value' attribute dogrulamasi sirasinda bir hata olustu: " + e.getMessage(), e);
        }
    }

    @Step("<key> elementinin <attribute> attribute alaninin olmadigi dogrulanir")
    public void verifyIfKeyElementDoNotHaveAttribute(String key, String attribute) {
        try {
            WebElement element = findElement(key);
            if (element == null) {
                throw new RuntimeException(key + " elementini bulanamadi!");
            }

            String attributeValue = element.getAttribute(attribute);

            if (attributeValue != null) {
                Assertions.fail(key + " elementinin '" + attribute + "' attribute alanı bulunmaktadır! Değer: " + attributeValue);
            } else {
                logger.info(key + " elementinin '" + attribute + "' attribute alanı bulunmamaktadır, doğrulama başarılı!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Attribute doğrulaması sırasında bir hata oluştu: " + e.getMessage(), e);
        }
    }

    private double parseFormattedNumber(String value) {
        // Virgül ve nokta dönüşümü
        value = value.replace(",", "");
        return Double.parseDouble(value);
    }

    @Step("<key> elementinin degerinin hafizada tutulan onceki degeri ile farkinin <fark> kadar oldugu dogrulanir")
    public void verifyDifferenceBetweenPreviousAndCurrentValue(String key, double fark) {
        try {
            String previousValueStr = tempData;
            double previousValue = parseFormattedNumber(previousValueStr);

            // Mevcut değeri al
            waitBySeconds(2);
            WebElement element = findElement(key);
            String currentValueStr = element.getText();
            double currentValue = parseFormattedNumber(currentValueStr);

            double actualDifference = previousValue - currentValue;

            // Fark kontrolü
            if (actualDifference != fark) {
                // fark beklenenden farklıysa, hata mesajı
                String errorMessage = key + " elementinin degeri ile hafizada tutulan degeri arasindaki fark beklenen "
                        + fark + " degerine esit degil! Gercek fark: " + actualDifference;
                Assertions.fail(errorMessage);
            }

            // Fark başarılı ise loglama
            logger.info(key + " elementinin degeri ile hafizada tutulan degeri arasindaki fark "
                    + actualDifference + " ve bu, beklenen " + fark + " degerine esit.");
        } catch (Exception e) {
            throw new RuntimeException("Değer farkı doğrulaması sırasında bir hata oluştu: " + e.getMessage(), e);
        }
    }

    @Step("<key> drop down menusunden text icerigi <text> olan secenek secilir")
    public void dropDownSelectWithText(String key, String text) {

        WebElement dropDown = findElement(key);
        Select select = new Select(dropDown);
        List<WebElement> dropDownList = select.getOptions();

        // Belirtilen metni içeren seçeneği bul
        WebElement selectedOption = null;
        for (WebElement option : dropDownList) {
            if (option.getText().equals(text)) {
                selectedOption = option;
                logger.info(key + " dropdown menusunden " + selectedOption.getText() + " secildi!");
                break;
            }
        }

        if (selectedOption != null) {
            select.selectByVisibleText(selectedOption.getText());
            logger.info("Drop down menüden seçilen seçenek: " + selectedOption.getText());
        } else {
            Assertions.fail("Belirtilen metni içeren bir seçenek bulunamadı: " + text);
        }
    }

    @Step("<key> elementinin degerinin sifirdan kucuk olamayacagi dogrulanir")
    public void verifyElementValueIsNotNegative(String key) {
        try {
            waitBySeconds(2);
            // Elementin değerini al
            WebElement element = findElement(key);
            String elementValueStr = element.getText();

            // Formatlı string'i double'a çevir
            double elementValue = parseFormattedNumber(elementValueStr);

            // Değeri kontrol et
            if (elementValue < 0) {
                Assertions.fail(key + " elementinin degeri sifirdan kucuk! Deger: " + elementValue);
            }

            // Doğrulama başarılıysa logla
            logger.info(key + " elementinin degeri sifirdan kucuk degil. Deger: " + elementValue);
        } catch (Exception e) {
            throw new RuntimeException("Element degeri kontrol edilirken bir hata olustu: " + e.getMessage(), e);
        }
    }

    @Step("<key> elementine BACKSPACE keyi yolla")
    public void sendKeyToElementBACKSPACE(String key) {
        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yollandi!");
    }

    @Step("<key> elementi tiklanabilir olana kadar bekle")
    public void waitUntilElementIsClickable(String key) {
        try {
            WebElement element = findElement(key);

            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.elementToBeClickable(element));

            logger.info(key + " elementi tiklanabilir hale geldi.");
        } catch (Exception e) {
            throw new RuntimeException(key + " elementi tiklanabilir hale gelmedi: " + e.getMessage(), e);
        }
    }
}
