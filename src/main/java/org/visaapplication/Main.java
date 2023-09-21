package org.visaapplication;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.testng.annotations.Parameters;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@PropertySource("file:application.properties")
public class Main implements CommandLineRunner {

     FileReader reader;
     WebDriver driver;

     EdgeDriver edgedriver;

    private final Logger log = LogManager.getLogger(Main.class);
    private Date timestamp = new Date();
    private Scheduler scheduler;
     WebElement selectedState;

    @Value("${port:}")
    private String port;

    @Value("${browser:}")
    private String browser;

    @Value("${fromDay:}")
    private  String fromDay;

    @Value("${fromSelectMonth:}")
    private String fromSelectMonth;

    @Value("${selectYear:}")
    private String selectYear;


    @Value("${toDay:}")
    private String toDay;

    @Value("${toSelectMonth:}")
    private String toSelectMonth;

    @Value("${toSelectYear:}")
    private String toSelectYear;

    @Value("${selectMonthDigitRange:}")
    private String selectMonthDigitRange;

    @Value("${toSelectMonthDigitRange:}")
    private String toSelectMonthDigitRange;


    @Value("${number:}")
    private String number;

    @Value("${selectState:}")
    private String selectState;

    @Value("${selectState1:}")
    private String selectState1;

    @Value("${selectState2:}")
    private String selectState2;

    @Value("${selectState3:}")
    private String selectState3;

    @Value("${selectState4:}")
    private String selectState4;

    @Value("${sleepTimeCalendar:}")
    private String sleepTimeCalendar;

    @Value("${schedulerTime:}")
    private String schedulerTime;


    @Value("${numberInterview:}")
    private String numberInterview;

    @Value("${interviewState:}")
    private String interviewState;

    @Value("${interviewState1:}")
    private String interviewState1;

    @Value("${CounselorPageTimeCalendar:}")
    private String CounselorPageTimeCalendar;


//     Properties properties = new Properties();


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        TimerTask task = new TimerTask() {

            @Parameters("browser")
            public void run() {
                if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                    openChromeBrowser();
                } else if (browser.equals("firefox")) {
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
                select.selectByVisibleText(selectState);
                selectedState = select.getFirstSelectedOption();

                WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element1.click();
                boolean selectDataMet = true;

                while (selectDataMet) {
                    WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));

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
        timer.schedule(task, 0, Long.parseLong(schedulerTime));
    }

    public void selectState() throws InterruptedException {

        String switchLocation = number;
        selectDate();

        if (switchLocation.equals("one")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            }
            else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText("");
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();

            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
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
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();
        } else if (switchLocation.equals("two")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState1);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();
        } else if (switchLocation.equals("five")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState1);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(selectState2);
            selectedState = select3.getFirstSelectedOption();
            boolean selectDataMet3 = true;

            while (selectDataMet3) {
                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate3 = calendar3.getAttribute("value");

                if (selectedDate3.equals("Select Date")) {
                    calendar3.click();
                    selectDataMet3 = false;
                } else {
                    selectDataMet3 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            //fourth state
            WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element4.click();
            WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown4);
            select4.selectByVisibleText(selectState3);
            selectedState = select4.getFirstSelectedOption();
            boolean selectDataMet4 = true;

            while (selectDataMet4) {
                WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate4 = calendar4.getAttribute("value");

                if (selectedDate4.equals("Select Date")) {
                    calendar4.click();
                    selectDataMet4 = false;
                } else {
                    selectDataMet4 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            //fifth state
            WebElement element5 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element5.click();
            WebElement blankDropDown5 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select5 = new Select(blankDropDown5);
            select5.selectByVisibleText(selectState4);
            selectedState = select5.getFirstSelectedOption();
            boolean selectDataMet5 = true;

            while (selectDataMet5) {
                WebElement calendar5 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate5 = calendar5.getAttribute("value");

                if (selectedDate5.equals("Select Date")) {
                    calendar5.click();
                    selectDataMet5 = false;
                } else {
                    selectDataMet5 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();
        } else if(switchLocation.equals("four")) {
        driver.quit();
        if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
            driver = new ChromeDriver(options);
        } else if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
            driver = new FirefoxDriver(options);
        } else {
            EdgeOptions options = new EdgeOptions();
            options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
            driver = new EdgeDriver(options);
        }
        //second state
        WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
        element.click();
        WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select1 = new Select(blankDropDown);
        select1.selectByVisibleText(selectState1);
        selectedState = select1.getFirstSelectedOption();
        boolean selectDataMet = true;

        while (selectDataMet) {
            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
            String selectedDate = calendar.getAttribute("value");

            if (selectedDate.equals("Select Date")) {
                calendar.click();
                selectDataMet = false;
            } else {
                selectDataMet = true;
            }
        }
        Thread.sleep(Long.parseLong(sleepTimeCalendar));
        selectDate();

        //Third state
        WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
        element3.click();
        WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select3 = new Select(blankDropDown3);
        select3.selectByVisibleText(selectState2);
        selectedState = select3.getFirstSelectedOption();
        boolean selectDataMet3 = true;

        while (selectDataMet3) {
            WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
            String selectedDate3 = calendar3.getAttribute("value");

            if (selectedDate3.equals("Select Date")) {
                calendar3.click();
                selectDataMet3 = false;
            } else {
                selectDataMet3 = true;
            }
        }
        Thread.sleep(Long.parseLong(sleepTimeCalendar));
        selectDate();

        //fourth state
        WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
        element4.click();
        WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select4 = new Select(blankDropDown4);
        select4.selectByVisibleText(selectState3);
        selectedState = select4.getFirstSelectedOption();
        boolean selectDataMet4 = true;

        while (selectDataMet4) {
            WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
            String selectedDate4 = calendar4.getAttribute("value");

            if (selectedDate4.equals("Select Date")) {
                calendar4.click();
                selectDataMet4 = false;
            } else {
                selectDataMet4 = true;
            }
        }
        Thread.sleep(Long.parseLong(sleepTimeCalendar));
        selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();
    } else if (switchLocation.equals("three")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState1);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(selectState2);
            selectedState = select3.getFirstSelectedOption();
            boolean selectDataMet3 = true;

            while (selectDataMet3) {
                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate3 = calendar3.getAttribute("value");

                if (selectedDate3.equals("Select Date")) {
                    calendar3.click();
                    selectDataMet3 = false;
                } else {
                    selectDataMet3 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

        }

    }

    public void selectDate() throws InterruptedException {
        //from Month
        WebDriverWait fromMontElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement fromMonthElement = fromMontElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectfromMonth = new Select(fromMonthElement);
        selectfromMonth.selectByVisibleText(fromSelectMonth);
        WebElement fromMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String fromMonth = fromMonthElements.getAttribute("value");

        //from Year
        WebDriverWait yearElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement fromYearElement = yearElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select selectYear1 = new Select(fromYearElement);
        selectYear1.selectByVisibleText(selectYear);
        WebElement fromYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String fromYear = fromYearElements.getAttribute("value");

        //to Month
        WebDriverWait montElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toMonthElement = montElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectMonth = new Select(toMonthElement);
        selectMonth.selectByVisibleText(toSelectMonth);
        WebElement toMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String toMonth = toMonthElements.getAttribute("value");

        //to Year
        WebDriverWait yearToElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toYearElement = yearToElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select toSelectYear1 = new Select(toYearElement);
        toSelectYear1.selectByVisibleText(toSelectYear);
        WebElement toYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String toYear = toYearElements.getAttribute("value");


        if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 0 || Integer.parseInt(toYear) - Integer.parseInt(fromMonth) == 0) {
            dateRange();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 1 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            dateRange();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -11) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRange();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear = new Select(nextYearElement);
            NextYear.selectByVisibleText(toSelectYear);

            toDateRange();
        }
    }

    public void openChromeBrowser() {
        if (driver!=null) {
            driver.quit();
        }
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
        driver = new ChromeDriver(options);
    }

    public void openFireFoxBrowser() {
        if (driver!=null) {
            driver.quit();
        }
        
            System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("moz:debuggerAddress", "localhost:9222");
        options.addArguments("debuggerAddress", "localhost:" + Integer.parseInt(port));
        driver = new RemoteWebDriver(options);
    }

    public void openEdgeBrowser() {
        if (driver!=null) {
            driver.quit();
        }
            WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
        driver = new EdgeDriver(options);

    }

    public void playAudio(String filePath) {
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

    public void dateRange() {
        LocalDate startDate = LocalDate.of(Integer.parseInt(selectYear), Integer.parseInt(selectMonthDigitRange), Integer.parseInt(fromDay));
        int fromDate = startDate.getDayOfMonth();
        int lastDate = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(Integer.parseInt(toSelectYear), Integer.parseInt(toSelectMonthDigitRange), Integer.parseInt(toDay));
        int toDate = endDate.getDayOfMonth();
        int remainingDate = lastDate-fromDate;
        if (fromDate> toDate) {

            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        selectStateConsularPosts();
                        throw new RuntimeException("The Date is selected as " + date);
                    } else {
                        boolean selectDataMet1 = true;

                        while (selectDataMet1) {
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");
                            // Compare the current date with the target date
                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet1 = false;
                            } else {
                                selectDataMet1 = true;
                            }
                        }
                        continue;
                    }
                } catch (NoSuchElementException exception) {
                    exception.getMessage();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        } else {
            for (int date = fromDate; date <= toDate; date++) {
                try {
                    WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        selectStateConsularPosts();
                        throw new RuntimeException("The Date is selected as " + date);
                    } else {
                        boolean selectDataMet1 = true;

                        while (selectDataMet1) {
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");
                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet1 = false;
                            } else {
                                selectDataMet1 = true;
                            }
                        }
                        continue;

                    }

                } catch (NoSuchElementException exception) {
                    // The date was not found in the calendar. Continue to the next date.
                    exception.getMessage();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }

    }

    public void toDateRange() {
        LocalDate endDate = LocalDate.of(Integer.parseInt(toSelectYear), Integer.parseInt(toSelectMonthDigitRange), Integer.parseInt(toDay));
        int toDate = endDate.getDayOfMonth();

        for (int date = 1; date<=toDate; date++) {
            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                if (radioElement.isDisplayed()) {
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    selectStateConsularPosts();
                    throw new RuntimeException("The Date is selected as " + date);
                } else {
                    boolean selectDataMet1 = true;

                    while (selectDataMet1) {
                        WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate = calendar.getAttribute("value");
                        // Compare the current date with the target date
                        if (selectedDate.equals("Select Date")) {
                            calendar.click();
                            selectDataMet1 = false;
                        } else {
                            selectDataMet1 = true;
                        }
                    }
                    continue;
                }
            }
            catch (NoSuchElementException exception) {
                // The date was not found in the calendar. Continue to the next date.
                exception.getMessage();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void selectStateConsularPosts() throws InterruptedException {

        String number = numberInterview;

        if (number.equals("one")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            selectedState = select1.getFirstSelectedOption();

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(interviewState);
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    if (!driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
                        calendar.click();
                    }
                    selectDataMet = false;
                } else {
                    select1.selectByVisibleText(interviewState1);
                    Thread.sleep(Long.parseLong(CounselorPageTimeCalendar));
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();
        } else if (number.equals("two")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);           }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState1);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();
        } else if (number.equals("five")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState1);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(selectState2);
            selectedState = select3.getFirstSelectedOption();
            boolean selectDataMet3 = true;

            while (selectDataMet3) {
                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate3 = calendar3.getAttribute("value");

                if (selectedDate3.equals("Select Date")) {
                    calendar3.click();
                    selectDataMet3 = false;
                } else {
                    selectDataMet3 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            //fourth state
            WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element4.click();
            WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown4);
            select4.selectByVisibleText(selectState3);
            selectedState = select4.getFirstSelectedOption();
            boolean selectDataMet4 = true;

            while (selectDataMet4) {
                WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate4 = calendar4.getAttribute("value");

                if (selectedDate4.equals("Select Date")) {
                    calendar4.click();
                    selectDataMet4 = false;
                } else {
                    selectDataMet4 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            //fifth state
            WebElement element5 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element5.click();
            WebElement blankDropDown5 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select5 = new Select(blankDropDown5);
            select5.selectByVisibleText(selectState4);
            selectedState = select5.getFirstSelectedOption();
            boolean selectDataMet5 = true;

            while (selectDataMet5) {
                WebElement calendar5 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate5 = calendar5.getAttribute("value");

                if (selectedDate5.equals("Select Date")) {
                    calendar5.click();
                    selectDataMet5 = false;
                } else {
                    selectDataMet5 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();
        } else if(number.equals("four")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState1);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(selectState2);
            selectedState = select3.getFirstSelectedOption();
            boolean selectDataMet3 = true;

            while (selectDataMet3) {
                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate3 = calendar3.getAttribute("value");

                if (selectedDate3.equals("Select Date")) {
                    calendar3.click();
                    selectDataMet3 = false;
                } else {
                    selectDataMet3 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            //fourth state
            WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element4.click();
            WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown4);
            select4.selectByVisibleText(selectState3);
            selectedState = select4.getFirstSelectedOption();
            boolean selectDataMet4 = true;

            while (selectDataMet4) {
                WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate4 = calendar4.getAttribute("value");

                if (selectedDate4.equals("Select Date")) {
                    calendar4.click();
                    selectDataMet4 = false;
                } else {
                    selectDataMet4 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();
        } else if (number.equals("three")) {
            driver.quit();
            if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new ChromeDriver(options);
            } else if (browser.equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(port));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState1);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(selectState2);
            selectedState = select3.getFirstSelectedOption();
            boolean selectDataMet3 = true;

            while (selectDataMet3) {
                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate3 = calendar3.getAttribute("value");

                if (selectedDate3.equals("Select Date")) {
                    calendar3.click();
                    selectDataMet3 = false;
                } else {
                    selectDataMet3 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else {
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

        }

    }

    public void selectDateInterviewPage() throws InterruptedException {
        //from Month
        WebDriverWait fromMontElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement fromMonthElement = fromMontElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectfromMonth = new Select(fromMonthElement);
        selectfromMonth.selectByVisibleText(fromSelectMonth);
        WebElement fromMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String fromMonth = fromMonthElements.getAttribute("value");

        //from Year
        WebDriverWait yearElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement fromYearElement = yearElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select selectYear1 = new Select(fromYearElement);
        selectYear1.selectByVisibleText(selectYear);
        WebElement fromYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String fromYear = fromYearElements.getAttribute("value");

        //to Month
        WebDriverWait montElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toMonthElement = montElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectMonth = new Select(toMonthElement);
        selectMonth.selectByVisibleText(toSelectMonth);
        WebElement toMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String toMonth = toMonthElements.getAttribute("value");

        //to Year
        WebDriverWait yearToElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toYearElement = yearToElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select toSelectYear1 = new Select(toYearElement);
        toSelectYear1.selectByVisibleText(toSelectYear);
        WebElement toYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String toYear = toYearElements.getAttribute("value");


        if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 0 || Integer.parseInt(toYear) - Integer.parseInt(fromMonth) == 0) {
            dateRangeInterviewPage();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 1 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            dateRangeInterviewPageDifferentMonth();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -11) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRangeInterviewPageDifferentMonth();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear1 = new Select(nextYearElement);
            NextYear1.selectByVisibleText(toSelectYear);

            toDateRangeInterviewPage();
        }

    }

    public void dateRangeInterviewPage() throws InterruptedException {
        LocalDate startDate = LocalDate.of(Integer.parseInt(selectYear), Integer.parseInt(selectMonthDigitRange), Integer.parseInt(fromDay));
        int fromDate = startDate.getDayOfMonth();
        int lastDate = startDate.lengthOfMonth();
        Month fromMonth = startDate.getMonth();
        LocalDate endDate = LocalDate.of(Integer.parseInt(toSelectYear), Integer.parseInt(toSelectMonthDigitRange), Integer.parseInt(toDay));
        int toDate = endDate.getDayOfMonth();
        Month toMonth = endDate.getMonth();
        int remainingDate = lastDate-fromDate;
        if (fromDate> toDate && fromMonth.equals(toMonth)) {

            WebElement dateElement = null;
            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));

                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);
                    } else {
                        boolean selectDataMet1 = true;

                        while (selectDataMet1) {
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");
                            // Compare the current date with the target date
                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet1 = false;
                            } else {
                                selectDataMet1 = true;
                            }
                        }
                        continue;
                    }
                } catch (NoSuchElementException exception) {
                    exception.getMessage();
                }
            }
            if(dateElement==null) {
                selectStateConsularPosts();
            }
        } else if(fromDate< toDate && fromMonth.equals(toMonth)) {
            WebElement dateElement=null;
            for (int date = fromDate; date <= toDate; date++) {
                try {
                     dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    if(!dateElement.isDisplayed()) {
                        selectStateConsularPosts();
                    }
                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);
                    } else {
                        boolean selectDataMet1 = true;

                        while (selectDataMet1) {
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");
                            // Compare the current date with the target date
                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet1 = false;
                            } else {
                                selectDataMet1 = true;
                            }
                        }
                        continue;
                    }
                } catch (NoSuchElementException exception) {
                    // The date was not found in the calendar. Continue to the next date.
                    exception.getMessage();
                }
            }
            if(dateElement==null) {
                selectStateConsularPosts();
            }
        }
    }

    public void dateRangeInterviewPageDifferentMonth() throws InterruptedException {
        LocalDate startDate = LocalDate.of(Integer.parseInt(selectYear), Integer.parseInt(selectMonthDigitRange), Integer.parseInt(fromDay));
        int fromDate = startDate.getDayOfMonth();
        int lastDate = startDate.lengthOfMonth();
        Month fromMonth = startDate.getMonth();
        LocalDate endDate = LocalDate.of(Integer.parseInt(toSelectYear), Integer.parseInt(toSelectMonthDigitRange), Integer.parseInt(toDay));
        int toDate = endDate.getDayOfMonth();
        Month toMonth = endDate.getMonth();
        int remainingDate = lastDate-fromDate;
        if (fromDate> toDate && !fromMonth.equals(toMonth)) {

            WebElement dateElement = null;
            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));

                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);
                    } else {
                        boolean selectDataMet1 = true;

                        while (selectDataMet1) {
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");
                            // Compare the current date with the target date
                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet1 = false;
                            } else {
                                selectDataMet1 = true;
                            }
                        }
                        continue;
                    }
                } catch (NoSuchElementException exception) {
                    exception.getMessage();
                }
            }
            if(dateElement==null) {
                selectStateConsularPosts();
            }
        } else if(fromDate< toDate && !fromMonth.equals(toMonth)) {
            WebElement dateElement=null;
            for (int date = fromDate; date <= toDate; date++) {
                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    if(!dateElement.isDisplayed()) {
                        selectStateConsularPosts();
                    }
                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);
                    } else {
                        boolean selectDataMet1 = true;

                        while (selectDataMet1) {
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");
                            // Compare the current date with the target date
                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet1 = false;
                            } else {
                                selectDataMet1 = true;
                            }
                        }
                        continue;
                    }
                } catch (NoSuchElementException exception) {
                    // The date was not found in the calendar. Continue to the next date.
                    exception.getMessage();
                }
            }
        }
    }

    public void toDateRangeInterviewPage() throws InterruptedException {
        LocalDate endDate = LocalDate.of(Integer.parseInt(toSelectYear), Integer.parseInt(toSelectMonthDigitRange), Integer.parseInt(toDay));
        int toDate = endDate.getDayOfMonth();
        WebElement dateElement=null;
        for (int date = 1; date<=toDate; date++) {

            try {
                 dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                log.log(Level.INFO, "Date: {0}, This TimeStamp is selected." + timestamp);
                log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                if (radioElement.isDisplayed()) {
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    throw new RuntimeException("The Interview Date is selected as " + date);
                } else {
                    boolean selectDataMet1 = true;

                    while (selectDataMet1) {
                        WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate = calendar.getAttribute("value");
                        if (selectedDate.equals("Select Date")) {
                            calendar.click();
                            selectDataMet1 = false;
                        } else {
                            selectDataMet1 = true;
                        }
                    }
                    continue;
                }
            }
            catch (NoSuchElementException exception) {
                exception.getMessage();
            }
        }
        if(dateElement==null) {
            selectStateConsularPosts();
        }
    }
}