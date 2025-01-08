package testinium.oguzhana.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ElementFinder {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DEFAULT_DIRECTORY_PATH = "elementValues";

    static ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();

    public static void addElementInfoByKey(String key, ElementInfo elementInfo) {
        elementMapList.put(key, elementInfo);
    }

    public void initMap(File[] fileList) {
        Type elementType = new TypeToken<List<ElementInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<ElementInfo> elementInfoList = null;
        for (File file : fileList) {
            try {
                elementInfoList = gson
                        .fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream()
                        .forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
            } catch (FileNotFoundException e) {
                logger.warn(e + " not found");
            }
        }
    }

    public File[] getFileList() {
        File[] fileList = new File("src/test/resources/" + DEFAULT_DIRECTORY_PATH).listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
        if (fileList == null) {
            logger.warn(
                    "File Directory Is Not Found! Please Check Directory Location. Default Directory Path = {}",
                    DEFAULT_DIRECTORY_PATH);
            throw new NullPointerException();
        }
        return fileList;
    }

    public ElementInfo findElementInfoByKey(String key) {
        return (ElementInfo) elementMapList.get(key);
    }

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

    WebElement findElement(String key, WebDriver driver, int timeout) {
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
