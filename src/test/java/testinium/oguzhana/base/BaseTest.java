package testinium.oguzhana.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.gauge.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import testinium.oguzhana.utils.ElementInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected static WebDriver driver;
    protected static Actions actions;
    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected DesiredCapabilities capabilities;
    ChromeOptions chromeOptions;
    FirefoxOptions firefoxOptions;
    EdgeOptions edgeOptions;

    String browserName = "chrome";
    String testURL = "";

    public static final String DEFAULT_DIRECTORY_PATH = "elementValues";

    static ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();

    public ElementInfo findElementInfoByKey(String key) {
        return (ElementInfo) elementMapList.get(key);
    }

    public ChromeOptions chromeOptions() {
        chromeOptions = new ChromeOptions();
        capabilities = DesiredCapabilities.chrome();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--start-maximized");

        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("disable-translate");
        chromeOptions.addArguments("--lang=tr-TR", "--force-local-strings");
        chromeOptions.addArguments("--disable-feature=ChromeLabs");
        chromeOptions.addArguments("--disable-in-product-help");

        System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver.exe");
        chromeOptions.merge(capabilities);
        return chromeOptions;
    }

    public EdgeOptions edgeOptions() {
        edgeOptions = new EdgeOptions();
        Map<String, Object> map = new HashMap<>();
        List<String> args = Arrays.asList("--start-maximized", "--disable-blink-features=AutomationControlled",
                "--inprivate", "--ignore-certificate-errors");
        map.put("args", args);
        edgeOptions.setCapability("ms:edgeOptions", map);
        System.setProperty("webdriver.edge.driver", "web_driver/msedgedriver.exe");
        edgeOptions.merge(capabilities);
        edgeOptions.setCapability("--start-fullscreen", true);
        return edgeOptions;
    }

    public FirefoxOptions firefoxOptions() {
        firefoxOptions = new FirefoxOptions();
        capabilities = DesiredCapabilities.firefox();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);

        firefoxOptions.addArguments("--private"); // Yeni argüman eklendi
        firefoxOptions.addArguments("--disable-notifications");
        firefoxOptions.addArguments("--start-fullscreen");

        firefoxOptions.merge(capabilities);
        System.setProperty("webdriver.gecko.driver", "web_driver/geckodriver");
        return firefoxOptions;
    }

    @BeforeScenario
    public void setUp() {
        logger.info("************************************  BeforeScenario  ************************************");
        logger.info("Local cihazda " + browserName + " browserda test basliyor...");

        switch (browserName.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver(chromeOptions());
                break;
            case "firefox":
                driver = new FirefoxDriver(firefoxOptions());
                break;
            case "edge":
                driver = new EdgeDriver(edgeOptions());
                break;
            default:
                throw new IllegalArgumentException("Geçersiz tarayici adi: " + browserName);
        }

        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.get(testURL);
        actions = new Actions(driver);
    }

    @AfterScenario
    public void tearDown() {
        logger.info("Driver kapatiliyor...");
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSpec
    public void afterSpec(ExecutionContext executionContext) {

        logger.info("=========================================================================" + "\r\n");
    }

    @AfterSuite
    public void afterSuite(ExecutionContext executionContext) {

        logger.info("*************************************************************************" + "\r\n");
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
        File[] fileList = new File("src/test/resources/" + DEFAULT_DIRECTORY_PATH)
                .listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
        if (fileList == null) {
            logger.warn(
                    "File Directory Is Not Found! Please Check Directory Location. Default Directory Path = {}",
                    DEFAULT_DIRECTORY_PATH);
            throw new NullPointerException();
        }
        return fileList;
    }
}
