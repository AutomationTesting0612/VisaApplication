package org.visaapplication;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main implements CommandLineRunner {

    static FileReader reader;
    static WebDriver driver;

    static EdgeDriver edgedriver;

    private static final Logger log = LogManager.getLogger(Main.class);
    private static Date timestamp = new Date();
    private static Scheduler scheduler;

    static {
        try {
            reader = new FileReader("application.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static Properties properties = new Properties();


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TimerTask task = new TimerTask() {

            public void run() {

                if(properties.getProperty("browser").equals("chrome")) {
                    openChromeBrowser();
                } else if(properties.getProperty("browser").equals("firefox")){
                    openFireFoxBrowser();
                } else {
                    openEdgeBrowser();

                }


                LocalDate targetDate = LocalDate.of(2023, 9, 30);
                boolean conditionMet = true;

                // Start the application loop
                while (conditionMet) {
                    LocalDate currentDate = LocalDate.now();

                    // Compare the current date with the target date
                    if (currentDate.isAfter(targetDate) || currentDate.isEqual(targetDate)) {
                        throw new RuntimeException("Demo Application is only used for particular time period.");
                    } else {
                        conditionMet = false;
                    }

                }
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement state = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='post_select']")));
                Select select = new Select(state);
                select.selectByVisibleText(properties.getProperty("selectState"));
                WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element1.click();
                boolean selectDataMet = true;

                while(selectDataMet) {
                    WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
//                    String jsScript = "document.getElementById('datepicker').value = 'Select Date';";
//                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//                    jsExecutor.executeScript(jsScript);
////                    ((JavascriptExecutor) driver).executeScript(jsScript);

                    String selectedDate = calendar.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        calendar.click();
                        if (!driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
                            calendar.click();
                        }
                        selectDataMet = false;
                    } else {
                        selectDataMet = true;
                    }
                }
                try {
                    selectState();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Long.parseLong(properties.getProperty("schedulerTime")));
    }

    public static void selectState() throws InterruptedException {

        String num = properties.getProperty("num");

        selectDate();

        if (num.equals("one")) {
            driver.quit();
            if(properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--ignore-ssl-errors=yes");
                options.addArguments("--ignore-certificate-errors");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("start-maximized");

                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--ignore-ssl-errors=yes");
                options.addArguments("--ignore-certificate-errors");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("start-maximized");

                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--ignore-ssl-errors=yes");
                options.addArguments("--ignore-certificate-errors");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("start-maximized");

                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new EdgeDriver(options);
            }
           WebElement element= driver.findElement(By.xpath("//label[contains(text(),'')]"));
           element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText("");
            select1.selectByVisibleText(properties.getProperty("selectState"));

            boolean selectDataMet = true;

            while(selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
//                    WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
//                    element.click();
                    calendar.click();
                    if (!driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
                        calendar.click();
                    }
                    selectDataMet = false;
                } else {
                    selectDataMet=true;
                }
            }
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();
        } else if (num.equals("two")) {
            driver.quit();
            if(properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--ignore-ssl-errors=yes");
                options.addArguments("--ignore-certificate-errors");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("start-maximized");

                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--ignore-ssl-errors=yes");
                options.addArguments("--ignore-certificate-errors");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("start-maximized");

                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--ignore-ssl-errors=yes");
                options.addArguments("--ignore-certificate-errors");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("start-maximized");

                options.addArguments("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new EdgeDriver(options);
            }
            WebElement element= driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
            boolean selectDataMet = true;

            while(selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet=true;
                }
            }
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
            boolean selectDataMet1 = true;

            while(selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1=true;
                }
            }
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();
        }

    }

    public static void selectDate() throws InterruptedException {

//        WebElement fromYearElement = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']"));
        WebDriverWait yearElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement fromYearElement = yearElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select selectYear = new Select(fromYearElement);
        selectYear.selectByVisibleText(properties.getProperty("selectYear"));


//        WebElement fromMonthElement = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']"));
        WebDriverWait montElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement fromMonthElement = montElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));

        Select selectMonth = new Select(fromMonthElement);
        selectMonth.selectByVisibleText(properties.getProperty("selectMonth"));

        String xpath = "//*[@class='ui-datepicker-calendar']/tbody/tr[";
        String xpath1 = "]/td";
        List<WebElement> element1 = driver.findElements(By.xpath("//*[@class='ui-datepicker-calendar']/tbody/tr"));
        List<WebElement> element2 = driver.findElements(By.xpath("//*[@class='ui-datepicker-calendar']/tbody/tr/td"));
        String xpath2 = "]/td[";
        String xpath3 = "]";
        for (int i = 1; i <= element1.size(); i++) {
            String finalXpath = xpath + i + xpath1;
            List<WebElement> tableDates = driver.findElements(By.xpath(finalXpath));
            for (WebElement date : tableDates) {
                if (date.getAttribute("class").equals(" greenday") || date.getAttribute("class").equals(" ui-datepicker-week-end greenday")) {
                    log.log(Level.INFO, "Timestamp: {0}, This is an info TimeStamp." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date.getText());
                    date.click();
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    throw new RuntimeException("The Date has been selected");
                } else {
                    continue;
                }
            }
        }
    }

    public static void openChromeBrowser() {
        if (properties.getProperty("os").equals("windows")) {

            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", "mac/chromedriver");
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("start-maximized");

        options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
        driver = new ChromeDriver(options);
    }

    public static void openFireFoxBrowser(){
        if (properties.getProperty("os").equals("windows")) {
            System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        } else {
            System.setProperty("webdriver.gecko.driver", "mac/geckodriver");
        }
        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--ignore-ssl-errors=yes");
//        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("start-maximized");
        options.setCapability("moz:debuggerAddress", "localhost:9222");
//        options.addArguments("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
        driver = new RemoteWebDriver(options);
    }

    public static void openEdgeBrowser() {
        if (properties.getProperty("os").equals("windows")) {
            System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
        } else {
            System.setProperty("webdriver.edge.driver", "mac/msedgedriver");
        }

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("start-maximized");
//        options.setCapability("browserName","webview2");
        options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
        // Create a RemoteWebDriver instance by attaching to the existing session
        driver = new EdgeDriver(options);
    }

    public static void playAudio(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a clip resource
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Play the audio
            clip.start();

            // Wait until the audio finishes playing
            while (!clip.isRunning()) {
                Thread.sleep(10);
            }
            while (clip.isRunning()) {
                Thread.sleep(10);
            }

            // Close the clip
            clip.close();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}