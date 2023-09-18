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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main implements CommandLineRunner {

    static FileReader reader;
    static WebDriver driver;

    static EdgeDriver edgedriver;

    private static final Logger log = LogManager.getLogger(Main.class);
    private static Date timestamp = new Date();
    private static Scheduler scheduler;
    static WebElement selectedState;

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


                if (properties.getProperty("browser").equals("chrome")) {
                    openChromeBrowser();

                } else if (properties.getProperty("browser").equals("firefox")) {
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
                WebDriverWait wait = new WebDriverWait(driver, 10L);
                WebElement state = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='post_select']")));
                Select select = new Select(state);
                select.selectByVisibleText(properties.getProperty("selectState"));
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
        timer.schedule(task, 0, Long.parseLong(properties.getProperty("schedulerTime")));
    }

    public static void selectState() throws InterruptedException {

        String num = properties.getProperty("num");
        selectDate();

        if (num.equals("one")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            }
            else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText("");
            select1.selectByVisibleText(properties.getProperty("selectState"));
            selectedState = select1.getFirstSelectedOption();

            boolean selectDataMet = true;

            while (selectDataMet) {
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
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();
        } else if (num.equals("two")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();
        } else if (num.equals("five")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(properties.getProperty("selectState2"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            //fourth state
            WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element4.click();
            WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown4);
            select4.selectByVisibleText(properties.getProperty("selectState3"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            //fifth state
            WebElement element5 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element5.click();
            WebElement blankDropDown5 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select5 = new Select(blankDropDown5);
            select5.selectByVisibleText(properties.getProperty("selectState4"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();
        } else if(num.equals("four")) {
        driver.quit();
        if (properties.getProperty("browser").equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
            driver = new ChromeDriver(options);
        } else if (properties.getProperty("browser").equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
            driver = new FirefoxDriver(options);
        } else {
            EdgeOptions options = new EdgeOptions();
            driver = new EdgeDriver(options);
        }
        //second state
        WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
        element.click();
        WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select1 = new Select(blankDropDown);
        select1.selectByVisibleText(properties.getProperty("selectState1"));
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
        Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
        selectDate();

        //Third state
        WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
        element3.click();
        WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select3 = new Select(blankDropDown3);
        select3.selectByVisibleText(properties.getProperty("selectState2"));
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
        Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
        selectDate();

        //fourth state
        WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
        element4.click();
        WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select4 = new Select(blankDropDown4);
        select4.selectByVisibleText(properties.getProperty("selectState3"));
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
        Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
        selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();
    } else if (num.equals("three")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(properties.getProperty("selectState2"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();

        }

    }

    public static void selectDate() throws InterruptedException {
        //from Month
        WebDriverWait fromMontElement = new WebDriverWait(driver, 10L);
        WebElement fromMonthElement = fromMontElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectfromMonth = new Select(fromMonthElement);
        selectfromMonth.selectByVisibleText(properties.getProperty("fromSelectMonth"));
        WebElement fromMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String fromMonth = fromMonthElements.getAttribute("value");

        //from Year
        WebDriverWait yearElement = new WebDriverWait(driver, 10L);
        WebElement fromYearElement = yearElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select selectYear = new Select(fromYearElement);
        selectYear.selectByVisibleText(properties.getProperty("selectYear"));
        WebElement fromYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String fromYear = fromYearElements.getAttribute("value");

        //to Month
        WebDriverWait montElement = new WebDriverWait(driver, 10L);
        WebElement toMonthElement = montElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectMonth = new Select(toMonthElement);
        selectMonth.selectByVisibleText(properties.getProperty("toSelectMonth"));
        WebElement toMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String toMonth = toMonthElements.getAttribute("value");

        //to Year
        WebDriverWait yearToElement = new WebDriverWait(driver, 10L);
        WebElement toYearElement = yearToElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select toSelectYear = new Select(toYearElement);
        toSelectYear.selectByVisibleText(properties.getProperty("toSelectYear"));
        WebElement toYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String toYear = toYearElements.getAttribute("value");


        if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 0 || Integer.parseInt(toYear) - Integer.parseInt(fromMonth) == 0) {
            dateRange();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 1 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, 10L);
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(properties.getProperty("fromSelectMonth"));

            dateRange();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, 10L);
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(properties.getProperty("toSelectMonth"));

            toDateRange();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -11) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, 10L);
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(properties.getProperty("fromSelectMonth"));

            WebDriverWait defaultYearWait = new WebDriverWait(driver, 10L);
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(properties.getProperty("selectYear"));

            dateRange();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, 10L);
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(properties.getProperty("toSelectMonth"));

            WebDriverWait nextYearWait = new WebDriverWait(driver, 10L);
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear = new Select(nextYearElement);
            NextYear.selectByVisibleText(properties.getProperty("toSelectYear"));

            toDateRange();
        }
    }

    public static void openChromeBrowser() {
        if (driver!=null) {
            driver.quit();
        }
        if (properties.getProperty("os").equals("windows")) {
            WebDriverManager.chromedriver().setup();
//            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        } else {
            WebDriverManager.chromedriver().setup();
        }
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
        driver = new ChromeDriver(options);
    }

    public static void openFireFoxBrowser() {
        if (driver!=null) {
            driver.quit();
        }
        if (properties.getProperty("os").equals("windows")) {
            System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        } else {
            System.setProperty("webdriver.gecko.driver", "mac/geckodriver");
        }
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("moz:debuggerAddress", "localhost:9222");
        options.addArguments("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
        driver = new RemoteWebDriver(options);
    }

    public static void openEdgeBrowser() {
        if (driver!=null) {
            driver.quit();
        }
        if (properties.getProperty("os").equals("windows")) {
            System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
        } else {
            System.setProperty("webdriver.edge.driver", "mac/msedgedriver");
        }

        EdgeOptions options = new EdgeOptions();
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

    public static void dateRange() {
        LocalDate startDate = LocalDate.of(Integer.parseInt(properties.getProperty("selectYear")), Integer.parseInt(properties.getProperty("selectMonthDigitRange")), Integer.parseInt(properties.getProperty("fromDay")));
        int fromDate = startDate.getDayOfMonth();
        int lastDate = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(Integer.parseInt(properties.getProperty("toSelectYear")), Integer.parseInt(properties.getProperty("toSelectMonthDigitRange")), Integer.parseInt(properties.getProperty("toDay")));
        int toDate = endDate.getDayOfMonth();
        int remainingDate = lastDate-fromDate;
        if (fromDate> toDate) {

            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, 4L);
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
//                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
//                    jsExecutor.executeScript("arguments[0].click();", radioElement);
//                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
//                    playAudio("file_example_WAV_1MG.wav");
//                    selectStateConsularPosts();
//                    throw new RuntimeException("The Date is selected as " + date);

                } catch (NoSuchElementException exception) {
                    // The date was not found in the calendar. Continue to the next date.
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
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, 4L);
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
//                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
//                    jsExecutor.executeScript("arguments[0].click();", radioElement);
//                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
//                    playAudio("application.properties");
//                    selectStateConsularPosts();
//                    throw new RuntimeException("The Date is selected as " + date);

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

    public static void toDateRange() {
        LocalDate endDate = LocalDate.of(Integer.parseInt(properties.getProperty("toSelectYear")), Integer.parseInt(properties.getProperty("toSelectMonthDigitRange")), Integer.parseInt(properties.getProperty("toDay")));
        int toDate = endDate.getDayOfMonth();

        for (int date = 1; date<=toDate; date++) {
            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                log.log(Level.INFO, "Date: {0}, This Date is selected." + timestamp);
                log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                WebDriverWait wait = new WebDriverWait(driver, 10L);
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

    public static void selectStateConsularPosts() throws InterruptedException {

        String num = properties.getProperty("numInterview");

        if (num.equals("one")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
//            select1.selectByVisibleText(properties.getProperty("interviewState"));
            selectedState = select1.getFirstSelectedOption();

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(properties.getProperty("interviewState"));
                log.log(Level.INFO, "Date: {0}, This Date is selected." + selectedState);
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
                    select1.selectByVisibleText(properties.getProperty("interviewState1"));
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();
        } else if (num.equals("two")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();
        } else if (num.equals("five")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(properties.getProperty("selectState2"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            //fourth state
            WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element4.click();
            WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown4);
            select4.selectByVisibleText(properties.getProperty("selectState3"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            //fifth state
            WebElement element5 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element5.click();
            WebElement blankDropDown5 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select5 = new Select(blankDropDown5);
            select5.selectByVisibleText(properties.getProperty("selectState4"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();
        } else if(num.equals("four")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(properties.getProperty("selectState2"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            //fourth state
            WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element4.click();
            WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown4);
            select4.selectByVisibleText(properties.getProperty("selectState3"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();
        } else if (num.equals("three")) {
            driver.quit();
            if (properties.getProperty("browser").equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new ChromeDriver(options);
            } else if (properties.getProperty("browser").equals("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-debugger-server", "localhost:" + Integer.parseInt(properties.getProperty("port")));
                driver = new FirefoxDriver(options);
            } else {
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            //second state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            //Third state
            WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);
            select3.selectByVisibleText(properties.getProperty("selectState2"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

            WebElement element1 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
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
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDateInterviewPage();

        }

    }

    public static void selectDateInterviewPage() throws InterruptedException {
        //from Month
        WebDriverWait fromMontElement = new WebDriverWait(driver, 10L);
        WebElement fromMonthElement = fromMontElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectfromMonth = new Select(fromMonthElement);
        selectfromMonth.selectByVisibleText(properties.getProperty("fromSelectMonth"));
        WebElement fromMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String fromMonth = fromMonthElements.getAttribute("value");

        //from Year
        WebDriverWait yearElement = new WebDriverWait(driver, 10L);
        WebElement fromYearElement = yearElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select selectYear = new Select(fromYearElement);
        selectYear.selectByVisibleText(properties.getProperty("selectYear"));
        WebElement fromYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String fromYear = fromYearElements.getAttribute("value");

        //to Month
        WebDriverWait montElement = new WebDriverWait(driver, 10L);
        WebElement toMonthElement = montElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        Select selectMonth = new Select(toMonthElement);
        selectMonth.selectByVisibleText(properties.getProperty("toSelectMonth"));
        WebElement toMonthElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']"));
        String toMonth = toMonthElements.getAttribute("value");

        //to Year
        WebDriverWait yearToElement = new WebDriverWait(driver, 10L);
        WebElement toYearElement = yearToElement.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        Select toSelectYear = new Select(toYearElement);
        toSelectYear.selectByVisibleText(properties.getProperty("toSelectYear"));
        WebElement toYearElements = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']"));
        String toYear = toYearElements.getAttribute("value");


        if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 0 || Integer.parseInt(toYear) - Integer.parseInt(fromMonth) == 0) {
            dateRangeInterviewPage();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 1 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, 10L);
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(properties.getProperty("fromSelectMonth"));

            dateRangeInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, 10L);
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(properties.getProperty("toSelectMonth"));

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -11) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, 10L);
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(properties.getProperty("fromSelectMonth"));

            WebDriverWait defaultYearWait = new WebDriverWait(driver, 10L);
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(properties.getProperty("selectYear"));

            dateRangeInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, 10L);
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(properties.getProperty("toSelectMonth"));

            WebDriverWait nextYearWait = new WebDriverWait(driver, 10L);
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear = new Select(nextYearElement);
            NextYear.selectByVisibleText(properties.getProperty("toSelectYear"));

            toDateRangeInterviewPage();
        }
    }

    public static void dateRangeInterviewPage() throws InterruptedException {
        LocalDate startDate = LocalDate.of(Integer.parseInt(properties.getProperty("selectYear")), Integer.parseInt(properties.getProperty("selectMonthDigitRange")), Integer.parseInt(properties.getProperty("fromDay")));
        int fromDate = startDate.getDayOfMonth();
        int lastDate = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(Integer.parseInt(properties.getProperty("toSelectYear")), Integer.parseInt(properties.getProperty("toSelectMonthDigitRange")), Integer.parseInt(properties.getProperty("toDay")));
        int toDate = endDate.getDayOfMonth();
        int remainingDate = lastDate-fromDate;
        if (fromDate> toDate) {

            WebElement dateElement = null;
            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));

                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, 4L);
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);
                    } else {
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
        } else {
            WebElement dateElement=null;
            for (int date = fromDate; date <= toDate; date++) {
                try {
                     dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    if(!dateElement.isDisplayed()) {
                        selectStateConsularPosts();
                    }
                    dateElement.click(); // click on the date
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, 4L);
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    if (radioElement.isDisplayed()) {
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);
                    } else {
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

    public static void toDateRangeInterviewPage() throws InterruptedException {
        LocalDate endDate = LocalDate.of(Integer.parseInt(properties.getProperty("toSelectYear")), Integer.parseInt(properties.getProperty("toSelectMonthDigitRange")), Integer.parseInt(properties.getProperty("toDay")));
        int toDate = endDate.getDayOfMonth();
        WebElement dateElement=null;
        for (int date = 1; date<=toDate; date++) {

            try {
                 dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                log.log(Level.INFO, "Date: {0}, This Date is selected." + timestamp);
                log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                WebDriverWait wait = new WebDriverWait(driver, 10L);
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                if (radioElement.isDisplayed()) {
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    throw new RuntimeException("The Interview Date is selected as " + date);
                } else {
                    continue;
                }
            }
            catch (NoSuchElementException exception) {
                // The date was not found in the calendar. Continue to the next date.
                exception.getMessage();
            }
        }
        if(dateElement==null) {
            selectStateConsularPosts();
        }
    }
}