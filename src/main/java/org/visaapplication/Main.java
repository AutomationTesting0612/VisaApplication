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
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@PropertySource("file:application.properties")
public class Main implements CommandLineRunner {
     WebDriver driver;
     Logger log = LogManager.getLogger(Main.class);
     WebElement selectedState;

    @Value("${port:}")
    private String port;

    @Value("${browser:}")
    private String browser;

    @Value("${fromDay:}")
    private String fromDay;

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

    @Value("${blankCalendarTimer:}")
    private String blankCalendarTimer;

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

    @Value("${stateSleepTimeCalendar:}")
    private String stateSleepTimeCalendar;

    @Value("${schedulerTime:}")
    private String schedulerTime;


    @Value("${numberInterview:}")
    private String numberInterview;

    @Value("${interviewState:}")
    private String interviewState;

    @Value("${interviewState1:}")
    private String interviewState1;

    @Value("${interviewState2:}")
    private String interviewState2;

    @Value("${interviewState3:}")
    private String interviewState3;

    @Value("${interviewState4:}")
    private String interviewState4;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


        Timer timer= new Timer();


        timer.schedule(new TimerTask() {

            @Parameters("browser")
            public void run() {
                    LocalDateTime timestamp =  LocalDateTime.now();
                    System.out.println(timestamp);
                    if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
                        openChromeBrowser();
                    } else if (browser.equals("firefox")) {
                        openFireFoxBrowser();
                    } else {
                        openEdgeBrowser();

                    }

                    WebElement ofc = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
                    String ofcPost = ofc.getText();
                    if (ofcPost.equals("OFC Post")) {
                        try {
                            selectState();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if(ofcPost.equals("Consular Posts")) {
                        try {
                            selectStateConsularPosts();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    driver.quit();

            }
        }, 0, Long.parseLong(schedulerTime));

    }

    public void selectState() throws InterruptedException {

        String switchLocation = number;
        WebDriverWait fromMontElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement nameElement=fromMontElement.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='gm_select']//li//label")));
        if (nameElement.getText()!=null) {


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
            } else {
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
                driver = new EdgeDriver(options);
            }
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText("");

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                Thread.sleep(Long.parseLong(blankCalendarTimer));
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    if (!driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
                        calendar.click();
                    }
                    selectDataMet = false;
                } else if (selectedDate.equals("")){
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    driver.findElement(By.xpath("//a[contains(text(),'Return to top')]")).click();
                    select1.selectByVisibleText("");
                    selectDataMet = true;
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
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    selectedState = select1.getFirstSelectedOption();
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet = false;
                    } else if (selectedDate2.equals("")) {
                        selectDataMet = true;
                    }

                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            WebElement ofc = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = ofc.getText();
            if (ofcPost.equals("OFC Post")) {
                WebElement element2 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element2.click();
                WebElement blankDropDown1 = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select2 = new Select(blankDropDown1);
                select2.selectByVisibleText(selectState1);
                selectedState = select2.getFirstSelectedOption();

                boolean selectDataMet2 = true;

                while (selectDataMet2) {

                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet2 = false;
                    } else if (selectedDate2.equals("")) {
                        select2.selectByVisibleText(selectState);
                        selectedState = select2.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate1 = calendar1.getAttribute("value");
                        if (selectedDate1.equals("Select Date")) {
                            calendar1.click();
                            selectDataMet2 = false;
                        } else if (selectedDate2.equals("")) {
                            selectDataMet2 = true;
                        }
                    } else {
                        selectDataMet2 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
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
            //first state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    selectedState = select1.getFirstSelectedOption();
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet = false;
                    } else if (selectedDate2.equals("")) {
                        select1.selectByVisibleText(selectState2);
                        selectedState = select1.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate3 = calendar3.getAttribute("value");
                        if (selectedDate3.equals("Select Date")) {
                            calendar3.click();
                            selectDataMet = false;
                        } else if (selectedDate3.equals("")) {
                            select1.selectByVisibleText(selectState3);
                            selectedState = select1.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate4 = calendar4.getAttribute("value");
                            if (selectedDate4.equals("Select Date")) {
                                calendar4.click();
                                selectDataMet = false;
                            } else if (selectedDate4.equals("")) {
                                select1.selectByVisibleText(selectState4);
                                selectedState = select1.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar5 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate5 = calendar5.getAttribute("value");
                                if (selectedDate5.equals("Select Date")) {
                                    calendar5.click();
                                    selectDataMet = false;
                                } else if (selectedDate5.equals("")) {
                                    selectDataMet = true;
                                }
                            }
                        }
                    }
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            WebElement ofc = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = ofc.getText();
            if (ofcPost.equals("OFC Post")) {

                WebElement element2 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element2.click();
                Select select2 = new Select(blankDropDown);

                boolean selectDataMet2 = true;

                while (selectDataMet2) {
                    select2.selectByVisibleText(selectState1);
                    selectedState = select2.getFirstSelectedOption();
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet2 = false;
                    } else if (selectedDate2.equals("")) {
                        select2.selectByVisibleText(selectState2);
                        selectedState = select2.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate1 = calendar1.getAttribute("value");

                        if (selectedDate1.equals("Select Date")) {
                            calendar1.click();
                            selectDataMet2 = false;
                        } else if (selectedDate1.equals("")) {
                            select2.selectByVisibleText(selectState3);
                            selectedState = select2.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate3 = calendar3.getAttribute("value");

                            if (selectedDate3.equals("Select Date")) {
                                calendar3.click();
                                selectDataMet2 = false;
                            } else if (selectedDate3.equals("")) {
                                select2.selectByVisibleText(selectState4);
                                selectedState = select2.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate4 = calendar4.getAttribute("value");

                                if (selectedDate4.equals("Select Date")) {
                                    calendar4.click();
                                    selectDataMet2 = false;
                                } else if (selectedDate4.equals("")) {
                                    select2.selectByVisibleText(selectState);
                                    selectedState = select2.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    String selectedDate = calendar.getAttribute("value");

                                    if (selectedDate.equals("Select Date")) {
                                        calendar.click();
                                        selectDataMet2 = false;
                                    } else if (selectedDate.equals("")) {
                                        selectDataMet2 = true;
                                    }
                                }
                            }
                        }
                    } else {
                        selectDataMet2 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
//            third location

            if (ofcPost.equals("OFC Post")) {
                WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element3.click();
                WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select3 = new Select(blankDropDown3);

                boolean selectDataMet3 = true;

                while (selectDataMet3) {
                    select3.selectByVisibleText(selectState2);
                    selectedState = select3.getFirstSelectedOption();
                    WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate3 = calendar3.getAttribute("value");

                    if (selectedDate3.equals("Select Date")) {
                        calendar3.click();
                        selectDataMet3 = false;
                    } else if (selectedDate3.equals("")) {
                        select3.selectByVisibleText(selectState3);
                        selectedState = select3.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate2 = calendar2.getAttribute("value");
                        if (selectedDate2.equals("Select Date")) {
                            calendar2.click();
                            selectDataMet3 = false;
                        } else if (selectedDate2.equals("")) {
                            select3.selectByVisibleText(selectState4);
                            selectedState = select3.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate4 = calendar4.getAttribute("value");
                            if (selectedDate4.equals("Select Date")) {
                                calendar4.click();
                                selectDataMet3 = false;
                            } else if (selectedDate4.equals("")) {
                                select3.selectByVisibleText(selectState);
                                selectedState = select3.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate = calendar.getAttribute("value");
                                if (selectedDate.equals("Select Date")) {
                                    calendar.click();
                                    selectDataMet3 = false;
                                } else if (selectedDate.equals("")) {
                                    select3.selectByVisibleText(selectState1);
                                    selectedState = select3.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    String selectedDate1 = calendar1.getAttribute("value");
                                    if (selectedDate1.equals("Select Date")) {
                                        calendar1.click();
                                        selectDataMet3 = false;
                                    } else if (selectedDate1.equals("")) {
                                        selectDataMet3 = true;
                                    }
                                }
                            }
                        }
                    } else {
                        selectDataMet3 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
//            fourth location
            if (ofcPost.equals("OFC Post")) {
                WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element4.click();
                WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select4 = new Select(blankDropDown4);

                boolean selectDataMet4 = true;

                while (selectDataMet4) {
                    select4.selectByVisibleText(selectState3);
                    selectedState = select4.getFirstSelectedOption();
                    WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate4 = calendar4.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate4.equals("Select Date")) {
                        calendar4.click();
                        selectDataMet4 = false;
                    } else if (selectedDate4.equals("")) {
                        select4.selectByVisibleText(selectState4);
                        selectedState = select4.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate1 = calendar1.getAttribute("value");
                        if (selectedDate1.equals("Select Date")) {
                            calendar1.click();
                            selectDataMet4 = false;
                        } else if (selectedDate1.equals("")) {
                            select4.selectByVisibleText(selectState);
                            selectedState = select4.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");
                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet4 = false;
                            } else if (selectedDate.equals("")) {
                                select4.selectByVisibleText(selectState1);
                                selectedState = select4.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate2 = calendar2.getAttribute("value");
                                if (selectedDate2.equals("Select Date")) {
                                    calendar2.click();
                                    selectDataMet4 = false;
                                } else if (selectedDate2.equals("")) {
                                    select4.selectByVisibleText(selectState2);
                                    selectedState = select4.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    String selectedDate3 = calendar3.getAttribute("value");
                                    if (selectedDate3.equals("Select Date")) {
                                        calendar3.click();
                                        selectDataMet4 = false;
                                    } else if (selectedDate3.equals("")) {
                                        selectDataMet4 = true;
                                    }
                                }
                            }
                        }
                    } else {
                        selectDataMet4 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
            // fifth location
            if (ofcPost.equals("OFC Post")) {
                WebElement element5 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element5.click();
                WebElement blankDropDown5 = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select5 = new Select(blankDropDown5);

                boolean selectDataMet5 = true;

                while (selectDataMet5) {
                    select5.selectByVisibleText(selectState4);
                    selectedState = select5.getFirstSelectedOption();
                    WebElement calendar5 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate5 = calendar5.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate5.equals("Select Date")) {
                        calendar5.click();
                        selectDataMet5 = false;
                    } else if (selectedDate5.equals("")) {
                        select5.selectByVisibleText(selectState);
                        selectedState = select5.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate1 = calendar1.getAttribute("value");

                        if (selectedDate1.equals("Select Date")) {
                            calendar1.click();
                            selectDataMet5 = false;
                        } else if (selectedDate1.equals("")) {
                            select5.selectByVisibleText(selectState1);
                            selectedState = select5.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate2 = calendar2.getAttribute("value");

                            if (selectedDate2.equals("Select Date")) {
                                calendar2.click();
                                selectDataMet5 = false;
                            } else if (selectedDate2.equals("")) {

                                select5.selectByVisibleText(selectState2);
                                selectedState = select5.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate3 = calendar3.getAttribute("value");

                                if (selectedDate3.equals("Select Date")) {
                                    calendar3.click();
                                    selectDataMet5 = false;
                                } else if (selectedDate3.equals("")) {
                                    select5.selectByVisibleText(selectState3);
                                    selectedState = select5.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    String selectedDate4 = calendar4.getAttribute("value");

                                    if (selectedDate4.equals("Select Date")) {
                                        calendar4.click();
                                        selectDataMet5 = false;
                                    } else if (selectedDate4.equals("")) {
                                        selectDataMet5 = true;
                                    }
                                }
                            }
                        }
                    } else {
                        selectDataMet5 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
        } else if (switchLocation.equals("four")) {
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
            //first state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    selectedState = select1.getFirstSelectedOption();
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");

                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet = false;
                    } else if(selectedDate2.equals("")) {
                        select1.selectByVisibleText(selectState2);
                        selectedState = select1.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate3 = calendar3.getAttribute("value");

                        if (selectedDate3.equals("Select Date")) {
                            calendar3.click();
                            selectDataMet = false;
                        } else if(selectedDate3.equals("")) {
                            select1.selectByVisibleText(selectState3);
                            selectedState = select1.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate4 = calendar4.getAttribute("value");

                            if (selectedDate4.equals("Select Date")) {
                                calendar4.click();
                                selectDataMet = false;
                            } else if (selectedDate4.equals("")) {
                                selectDataMet = true;
                            }
                        }
                    }
                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            WebElement ofc = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = ofc.getText();
            if (ofcPost.equals("OFC Post")) {

                WebElement element2 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element2.click();
                Select select2 = new Select(blankDropDown);

                boolean selectDataMet2 = true;

                while (selectDataMet2) {
                    select2.selectByVisibleText(selectState1);
                    selectedState = select2.getFirstSelectedOption();
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");

                    // Compare the current date with the target date
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet2 = false;
                    } else if (selectedDate2.equals("")) {
                        select2.selectByVisibleText(selectState2);
                        selectedState = select2.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate1 = calendar1.getAttribute("value");

                        if (selectedDate1.equals("Select Date")) {
                            calendar1.click();
                            selectDataMet2 = false;
                        } else if (selectedDate1.equals("")) {
                            select2.selectByVisibleText(selectState3);
                            selectedState = select2.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate3 = calendar3.getAttribute("value");

                            if (selectedDate3.equals("Select Date")) {
                                calendar3.click();
                                selectDataMet2 = false;
                            } else if (selectedDate3.equals("")) {
                                select2.selectByVisibleText(selectState);
                                selectedState = select2.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate = calendar.getAttribute("value");

                                if (selectedDate.equals("Select Date")) {
                                    calendar.click();
                                    selectDataMet2 = false;
                                } else if (selectedDate.equals("")) {
                                    selectDataMet2 = true;
                                }
                            }
                        }
                    } else {
                        selectDataMet2 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
//            third location

            if (ofcPost.equals("OFC Post")) {
                WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element3.click();
                WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select3 = new Select(blankDropDown3);

                boolean selectDataMet3 = true;

                while (selectDataMet3) {
                    select3.selectByVisibleText(selectState2);
                    selectedState = select3.getFirstSelectedOption();
                    WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate3 = calendar3.getAttribute("value");

                    if (selectedDate3.equals("Select Date")) {
                        calendar3.click();
                        selectDataMet3 = false;
                    } else if (selectedDate3.equals("")) {
                        select3.selectByVisibleText(selectState3);
                        selectedState = select3.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate2 = calendar2.getAttribute("value");

                        if (selectedDate2.equals("Select Date")) {
                            calendar2.click();
                            selectDataMet3 = false;
                        } else if (selectedDate2.equals("")) {
                            select3.selectByVisibleText(selectState);
                            selectedState = select3.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate = calendar.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                calendar.click();
                                selectDataMet3 = false;
                            } else if (selectedDate.equals("")) {
                                select3.selectByVisibleText(selectState1);
                                selectedState = select3.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate1 = calendar1.getAttribute("value");

                                if (selectedDate1.equals("Select Date")) {
                                    calendar1.click();
                                    selectDataMet3 = false;
                                } else if (selectedDate1.equals("")) {

                                    selectDataMet3 = true;
                                }
                            }
                        }
                    } else {
                        selectDataMet3 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
//            fourth location
            if (ofcPost.equals("OFC Post")) {
                WebElement element4 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element4.click();
                WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select4 = new Select(blankDropDown4);

                boolean selectDataMet4 = true;

                while (selectDataMet4) {
                    select4.selectByVisibleText(selectState3);
                    selectedState = select4.getFirstSelectedOption();
                    WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate4 = calendar4.getAttribute("value");

                    // Compare the current date with the target date
                    if (selectedDate4.equals("Select Date")) {
                        calendar4.click();
                        selectDataMet4 = false;
                    } else if (selectedDate4.equals("")) {
                        select4.selectByVisibleText(selectState);
                        selectedState = select4.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate1 = calendar1.getAttribute("value");

                        if (selectedDate1.equals("Select Date")) {
                            calendar1.click();
                            selectDataMet4 = false;
                        } else if (selectedDate1.equals("")) {
                            select4.selectByVisibleText(selectState1);
                            selectedState = select4.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate2 = calendar2.getAttribute("value");

                            if (selectedDate2.equals("Select Date")) {
                                calendar2.click();
                                selectDataMet4 = false;
                            } else if (selectedDate2.equals("")) {
                                select4.selectByVisibleText(selectState2);
                                selectedState = select4.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                String selectedDate3 = calendar3.getAttribute("value");

                                if (selectedDate3.equals("Select Date")) {
                                    calendar3.click();
                                    selectDataMet4 = false;
                                } else if (selectedDate3.equals("")) {
                                    selectDataMet4 = true;
                                }
                            }
                        }
                    } else {
                        selectDataMet4 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }

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

            //first state
            WebElement element = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            boolean selectDataMet = true;

            while (selectDataMet) {

                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else if (selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet = false;
                    } else if (selectedDate2.equals("")) {
                        select1.selectByVisibleText(selectState2);
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate3 = calendar3.getAttribute("value");
                        if (selectedDate3.equals("Select Date")) {
                            calendar3.click();
                            selectDataMet = false;
                        } else if (selectedDate3.equals("")) {
                            selectDataMet = true;
                        }
                    }
                } else {
                    selectDataMet = true;
                }
            }

            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

//            second location
            WebElement ofc = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = ofc.getText();
            if (ofcPost.equals("OFC Post")) {
                WebElement element2 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element2.click();
                Select select2 = new Select(blankDropDown);
                boolean selectDataMet2 = true;

                while (selectDataMet2) {
                    select2.selectByVisibleText(selectState1);
                    selectedState = select2.getFirstSelectedOption();
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet2 = false;
                    } else if (selectedDate2.equals("")) {
                        select2.selectByVisibleText(selectState2);
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate1 = calendar1.getAttribute("value");
                        if (selectedDate1.equals("Select Date")) {
                            calendar1.click();
                            selectDataMet2 = false;
                        } else if (selectedDate1.equals("")) {
                            select2.selectByVisibleText(selectState);
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate3 = calendar3.getAttribute("value");
                            if (selectedDate3.equals("Select Date")) {
                                calendar3.click();
                                selectDataMet2 = false;
                            } else if (selectedDate3.equals("")) {
                                selectDataMet2 = true;
                            }
                        }
                    } else {
                        selectDataMet2 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }

//            third location
            if (ofcPost.equals("OFC Post")) {
                WebElement element3 = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                element3.click();
                WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select3 = new Select(blankDropDown3);
                boolean selectDataMet3 = true;

                while (selectDataMet3) {
                    select3.selectByVisibleText(selectState2);
                    selectedState = select3.getFirstSelectedOption();
                    WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate3 = calendar3.getAttribute("value");

                    if (selectedDate3.equals("Select Date")) {
                        calendar3.click();
                        selectDataMet3 = false;
                    } else if (selectedDate3.equals("")) {
                        select3.selectByVisibleText(selectState);
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        String selectedDate2 = calendar2.getAttribute("value");

                        if (selectedDate2.equals("Select Date")) {
                            calendar2.click();
                            selectDataMet3 = false;
                        } else if (selectedDate2.equals("")) {
                            select3.selectByVisibleText(selectState1);
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            String selectedDate1 = calendar1.getAttribute("value");

                            if (selectedDate1.equals("Select Date")) {
                                calendar1.click();
                                selectDataMet3 = false;
                            } else if (selectedDate1.equals("")) {
                                selectDataMet3 = true;
                            }
                        }
                    } else {
                        selectDataMet3 = true;
                    }
                }
                Thread.sleep(Long.parseLong(sleepTimeCalendar));
                selectDate();
            }
        }
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
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 2 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            dateRange();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 3 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            dateRange();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            completeMonth();

            //Click next Month Element
            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            completeThirdMonth();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 4 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            dateRange();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            completeMonth();

            //Click next Month Element
            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            completeThirdMonth();

            //Click next Month Element
            WebDriverWait fourthMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourthElement = fourthMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            fourthElement.click();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -10) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRange();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            WebDriverWait secondMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement second = secondMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select secondMonths = new Select(second);
            WebElement textElement = secondMonths.getFirstSelectedOption();
            String validateMonthElement = textElement.getText();


            if (!validateMonthElement.equals("Dec")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeMonth();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear = new Select(nextYearElement);
            NextYear.selectByVisibleText(toSelectYear);

            toDateRange();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -9) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRange();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            WebDriverWait secondMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement second = secondMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select secondMonths = new Select(second);
            WebElement textElement = secondMonths.getFirstSelectedOption();
            String validateMonthElement = textElement.getText();

            if (!validateMonthElement.equals("Nov")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeMonth();

            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            WebDriverWait thirdMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement third = thirdMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select thirdMonths = new Select(third);
            WebElement thirdTextElement = thirdMonths.getFirstSelectedOption();
            String validateThirdMonthElement = thirdTextElement.getText();


            if (!validateThirdMonthElement.equals("Nov")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeThirdMonth();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear = new Select(nextYearElement);
            NextYear.selectByVisibleText(toSelectYear);

            toDateRange();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -8) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRange();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            WebDriverWait secondMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement second = secondMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select secondMonths = new Select(second);
            WebElement textElement = secondMonths.getFirstSelectedOption();
            String validateMonthElement = textElement.getText();


            if (!validateMonthElement.equals("Oct")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeMonth();

            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            WebDriverWait thirdMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement third = thirdMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select thirdMonths = new Select(third);
            WebElement thirdTextElement = thirdMonths.getFirstSelectedOption();
            String validateThirdMonthElement = thirdTextElement.getText();


            if (!validateThirdMonthElement.equals("Nov")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeThirdMonth();

            WebDriverWait fourthMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourthElement = fourthMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            fourthElement.click();

            WebDriverWait fourthMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourth = fourthMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select fourthMonths = new Select(fourth);
            WebElement fourthTextElement = fourthMonths.getFirstSelectedOption();
            String validateFourthMonthElement = fourthTextElement.getText();


            if (!validateFourthMonthElement.equals("Dec")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }


            completeFourthMonth();

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
        if (driver != null) {
            driver.quit();
        }
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(port));
        driver = new ChromeDriver(options);
    }

    public void openFireFoxBrowser() {
        if (driver != null) {
            driver.quit();
        }

        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("moz:debuggerAddress", "localhost:9222");
        options.addArguments("debuggerAddress", "localhost:" + Integer.parseInt(port));
        driver = new RemoteWebDriver(options);
    }

    public void openEdgeBrowser() {
        if (driver != null) {
            driver.quit();
        }
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
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
        int remainingDate = lastDate - fromDate;
        if (fromDate > toDate) {

            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    dateElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + date);
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        selectStateConsularPosts();

                } catch (NoSuchElementException exception) {
                    exception.getMessage();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        } else {
            for (int date = fromDate; date <= toDate; date++) {
                try {
                    WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    dateElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + date);
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    selectStateConsularPosts();

                } catch (NoSuchElementException exception) {
                    // The date was not found in the calendar. Continue to the next date.
                    exception.getMessage();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }

    }

    public void completeMonth() {
        WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        WebDriverWait secondYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = secondYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = secondYearElement.getAttribute("value");


        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    selectStateConsularPosts();
            } catch (NoSuchElementException exception) {
                exception.getMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void completeMonthForInterviewPage() {

        WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        WebDriverWait secondYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = secondYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = secondYearElement.getAttribute("value");

        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Interview Page date: {0}, This date is selected." + date);
                System.out.println("the Interview Page state: {0}, This state is selected." + selectedState.getText());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                jsExecutor.executeScript("arguments[0].click();", radioElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                throw new RuntimeException("The Interview Date is selected as " + date);
            } catch (NoSuchElementException exception) {
                exception.getMessage();
            }

        }
    }

    public void completeThirdMonth() {
        WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        WebDriverWait secondYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = secondYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = secondYearElement.getAttribute("value");

        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));

                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                jsExecutor.executeScript("arguments[0].click();", radioElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                selectStateConsularPosts();
            } catch (NoSuchElementException exception) {
                exception.getMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void completeThirdMonthForInterviewPage() {

        WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        WebDriverWait secondYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = secondYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = secondYearElement.getAttribute("value");


        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Interview Page date: {0}, This date is selected." + date);
                System.out.println("the Interview Page state: {0}, This state is selected." + selectedState.getText());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));

                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                jsExecutor.executeScript("arguments[0].click();", radioElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                throw new RuntimeException("The Interview Date is selected as " + date);

            } catch (NoSuchElementException exception) {
                exception.getMessage();
            }

        }
    }

    public void completeFourthMonth() {

        WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        WebDriverWait secondYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = secondYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = secondYearElement.getAttribute("value");

        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                jsExecutor.executeScript("arguments[0].click();", radioElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                selectStateConsularPosts();

            } catch (NoSuchElementException exception) {
                exception.getMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void completeFourthMonthForInterviewPage() {

        WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        WebDriverWait secondYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = secondYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = secondYearElement.getAttribute("value");

        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Interview Page date: {0}, This date is selected." + date);
                System.out.println("the Interview Page state: {0}, This state is selected." + selectedState.getText());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                jsExecutor.executeScript("arguments[0].click();", radioElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                throw new RuntimeException("The Interview Date is selected as " + date);

            } catch (NoSuchElementException exception) {
                exception.getMessage();
            }

        }
    }

    public void completeFifthMonth() {

        WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        WebDriverWait secondYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = secondYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = secondYearElement.getAttribute("value");

        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                jsExecutor.executeScript("arguments[0].click();", radioElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                selectStateConsularPosts();
            } catch (NoSuchElementException exception) {
                exception.getMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void toDateRange() {
        LocalDate endDate = LocalDate.of(Integer.parseInt(toSelectYear), Integer.parseInt(toSelectMonthDigitRange), Integer.parseInt(toDay));
        int toDate = endDate.getDayOfMonth();

        for (int date = 1; date <= toDate; date++) {
            try {
                WebElement dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Ofc Page  TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));

                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    selectStateConsularPosts();

            } catch (NoSuchElementException exception) {
                exception.getMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void selectStateConsularPosts() throws InterruptedException {

        String number = numberInterview;

        WebDriverWait fromMontElement = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement nameElement=fromMontElement.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='gm_select']//li//label")));
        if (nameElement.getText()!=null) {

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

            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element1 = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element1.click();
                Thread.sleep(Long.parseLong(blankCalendarTimer));
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    if (!driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
                        calendar.click();
                    }
                    selectDataMet = false;
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    select1.selectByVisibleText(interviewState1);
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
                driver = new EdgeDriver(options);
            }

            //first state
            WebDriverWait wait14 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element14 = wait14.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element14.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate1 = calendar1.getAttribute("value");

                    if (selectedDate1.equals("Select Date")) {
                        calendar.click();
                        selectDataMet = false;
                    } else if (selectedDate1.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

//         2nd location
            WebDriverWait wait23 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element23 = wait23.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element23.click();
            boolean selectDataMet1 = true;


            while (selectDataMet1) {
                select1.selectByVisibleText(interviewState1);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();
                WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate = calendar.getAttribute("value");
                if (selectedDate.equals("Select Date")) {
                    calendar.click();
                    selectDataMet1 = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(interviewState);
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate1 = calendar1.getAttribute("value");
                    if (selectedDate1.equals("Select Date")) {
                        calendar.click();
                        selectDataMet1 = false;
                    } else if(selectedDate1.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet1 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
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
            //first state
            WebDriverWait wait13 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element13 = wait13.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element13.click();
            WebElement blankDropDown1 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown1);

            boolean selectDataMet1 = true;

            while (selectDataMet1) {

                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();

                WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate1 = calendar1.getAttribute("value");


                if (selectedDate1.equals("Select Date")) {
                    calendar1.click();
                    selectDataMet1 = false;
                } else if(selectedDate1.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");
                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet1 = false;
                    } else if(selectedDate2.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet1 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

            //second state
            WebDriverWait wait22 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element22 = wait22.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element22.click();
            WebElement blankDropDown2 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select2 = new Select(blankDropDown2);

            boolean selectDataMet2 = true;

            while (selectDataMet2) {

                select2.selectByVisibleText(interviewState1);
                selectedState = select2.getFirstSelectedOption();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();

                WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate2 = calendar2.getAttribute("value");

                if (selectedDate2.equals("Select Date")) {
                    calendar2.click();
                    selectDataMet2 = false;
                } else if(selectedDate2.equals("")) {
                    select2.selectByVisibleText(interviewState2);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate3 = calendar3.getAttribute("value");

                    if (selectedDate3.equals("Select Date")) {
                        calendar3.click();
                        selectDataMet2 = false;
                    } else if(selectedDate3.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet2 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet2 = true;
                }
            }
        Thread.sleep(Long.parseLong(sleepTimeCalendar));
        selectDateInterviewPage();

        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

        //third state
        WebDriverWait wait32 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element32 = wait32.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
        element32.click();
        WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select3 = new Select(blankDropDown3);

        boolean selectDataMet3 = true;

        while (selectDataMet3) {

            select3.selectByVisibleText(interviewState2);
            selectedState = select3.getFirstSelectedOption();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element.click();
            WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
            String selectedDate3 = calendar3.getAttribute("value");

            if (selectedDate3.equals("Select Date")) {
                calendar3.click();
                selectDataMet3 = false;
            } else if(selectedDate3.equals("")) {
                select3.selectByVisibleText(interviewState3);
               Thread.sleep(Long.parseLong(blankCalendarTimer));
                WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate4 = calendar4.getAttribute("value");

                if (selectedDate4.equals("Select Date")) {
                    calendar4.click();
                    selectDataMet3 = false;
                } else if(selectedDate4.equals("")) {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet3 = true;
                }
            } else {
                Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                selectDataMet3 = true;
            }
        }
        Thread.sleep(Long.parseLong(sleepTimeCalendar));
        selectDateInterviewPage();

        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

        //fourth state
        WebDriverWait wait41 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element41 = wait41.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
        element41.click();
        WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select4 = new Select(blankDropDown4);

        boolean selectDataMet4 = true;

        while (selectDataMet4) {

            select4.selectByVisibleText(interviewState3);
            selectedState = select4.getFirstSelectedOption();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element.click();

            WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
            String selectedDate4 = calendar4.getAttribute("value");

            if (selectedDate4.equals("Select Date")) {
                calendar4.click();
                selectDataMet4 = false;
            } else if(selectedDate4.equals("")) {
                select4.selectByVisibleText(interviewState4);
                Thread.sleep(Long.parseLong(blankCalendarTimer));
                WebElement calendar5 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate5 = calendar5.getAttribute("value");

                if (selectedDate5.equals("Select Date")) {
                    calendar5.click();
                    selectDataMet4 = false;
                } else if (selectedDate5.equals("")) {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet4 = true;
                }
            } else {
                Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                selectDataMet4 = true;
            }
        }
        Thread.sleep(Long.parseLong(sleepTimeCalendar));
        selectDateInterviewPage();

        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

        //fifth state
        WebDriverWait wait51 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element51 = wait51.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
        element51.click();
        WebElement blankDropDown5 = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select5 = new Select(blankDropDown5);

        boolean selectDataMet5 = true;

        while (selectDataMet5) {
            select5.selectByVisibleText(interviewState4);
            selectedState = select5.getFirstSelectedOption();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element.click();

            WebElement calendar5 = driver.findElement(By.xpath("//input[@id='datepicker']"));
            String selectedDate5 = calendar5.getAttribute("value");

            // Compare the current date with the target date
            if (selectedDate5.equals("Select Date")) {
                calendar5.click();
                selectDataMet5 = false;
            } else if(selectedDate5.equals("")) {
                select5.selectByVisibleText(interviewState);
               Thread.sleep(Long.parseLong(blankCalendarTimer));
                WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate1 = calendar1.getAttribute("value");

                if (selectedDate1.equals("Select Date")) {
                    calendar1.click();
                    selectDataMet5 = false;
                } else if (selectedDate1.equals("")) {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet5 = true;
                }
            } else {
                Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                selectDataMet5 = true;
            }
        }
        Thread.sleep(Long.parseLong(sleepTimeCalendar));
        selectDateInterviewPage();
        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
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

            //first state
            WebDriverWait wait12 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element12 = wait12.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element12.click();
            WebElement blankDropDown1 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown1);

            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();

                WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate1 = calendar1.getAttribute("value");

                if (selectedDate1.equals("Select Date")) {
                    calendar1.click();
                    selectDataMet1 = false;
                } else if(selectedDate1.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                   Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");

                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet1 = false;
                    } else if (selectedDate2.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet1 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            //second state
            WebDriverWait wait22 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element22 = wait22.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element22.click();
            WebElement blankDropDown2 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select2 = new Select(blankDropDown2);
            boolean selectDataMet2 = true;

            while (selectDataMet2) {
                select2.selectByVisibleText(interviewState1);
                selectedState = select2.getFirstSelectedOption();
                WebDriverWait wait21 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element21 = wait21.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element21.click();

                WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate2 = calendar2.getAttribute("value");

                if (selectedDate2.equals("Select Date")) {
                    calendar2.click();
                    selectDataMet2 = false;
                } else if(selectedDate2.equals("")) {
                    select2.selectByVisibleText(interviewState2);
                  Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate3 = calendar3.getAttribute("value");

                    if (selectedDate3.equals("Select Date")) {
                        calendar3.click();
                        selectDataMet2 = false;
                    } else if(selectedDate3.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet2 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet2 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

            //third state
            WebDriverWait wait31 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element31 = wait31.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element31.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);

            boolean selectDataMet3 = true;

            while (selectDataMet3) {
                select3.selectByVisibleText(interviewState2);
                selectedState = select3.getFirstSelectedOption();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();

                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate3 = calendar3.getAttribute("value");

                if (selectedDate3.equals("Select Date")) {
                    calendar3.click();
                    selectDataMet3 = false;
                } else if(selectedDate3.equals("")) {
                    select3.selectByVisibleText(interviewState3);
                  Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate4 = calendar4.getAttribute("value");

                    if (selectedDate4.equals("Select Date")) {
                        calendar4.click();
                        selectDataMet3 = false;
                    } else if (selectedDate4.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet3 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet3 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

            //fourth state
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element.click();
            WebElement blankDropDown4 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown4);

            boolean selectDataMet4 = true;

            while (selectDataMet4) {
                select4.selectByVisibleText(interviewState3);
                selectedState = select4.getFirstSelectedOption();
                WebDriverWait wait4 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element4 = wait4.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element4.click();

                WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate4 = calendar4.getAttribute("value");

                if (selectedDate4.equals("Select Date")) {
                    calendar4.click();
                    selectDataMet4 = false;
                } else if(selectedDate4.equals("")) {
                    select4.selectByVisibleText(interviewState);
                  Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar5 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate5 = calendar5.getAttribute("value");

                    if (selectedDate5.equals("Select Date")) {
                        calendar5.click();
                        selectDataMet4 = false;
                    } else if(selectedDate5.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet4 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet4 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
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

            //first state
            WebDriverWait wait5 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element5 = wait5.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element5.click();
            WebElement blankDropDown1 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown1);
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();

                WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate1 = calendar1.getAttribute("value");

                if (selectedDate1.equals("Select Date")) {
                    calendar1.click();
                    selectDataMet1 = false;
                } else if (selectedDate1.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate2 = calendar2.getAttribute("value");

                    if (selectedDate2.equals("Select Date")) {
                        calendar2.click();
                        selectDataMet1 = false;
                    } else if (selectedDate2.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet1 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet1 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

            //second state
            WebDriverWait wait12 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element12 = wait12.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element12.click();
            WebElement blankDropDown2 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select2 = new Select(blankDropDown2);

            boolean selectDataMet2 = true;

            while (selectDataMet2) {

                select2.selectByVisibleText(interviewState1);
                selectedState = select2.getFirstSelectedOption();
                WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element2 = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element2.click();

                WebElement calendar2 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate2 = calendar2.getAttribute("value");

                if (selectedDate2.equals("Select Date")) {
                    calendar2.click();
                    selectDataMet2 = false;
                } else if (selectedDate2.equals("")) {
                    select2.selectByVisibleText(interviewState2);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate3 = calendar3.getAttribute("value");

                    if (selectedDate3.equals("Select Date")) {
                        calendar3.click();
                        selectDataMet2 = false;
                    } else if (selectedDate3.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet2 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet2 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            Thread.sleep(Long.parseLong(stateSleepTimeCalendar));

            //third state
            WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element3 = wait3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            element3.click();
            WebElement blankDropDown3 = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown3);

            boolean selectDataMet3 = true;

            while (selectDataMet3) {

                select3.selectByVisibleText(interviewState2);
                selectedState = select3.getFirstSelectedOption();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                element.click();
                WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                String selectedDate3 = calendar3.getAttribute("value");

                if (selectedDate3.equals("Select Date")) {
                    calendar3.click();
                    selectDataMet3 = false;
                } else if (selectedDate3.equals("")) {
                    select3.selectByVisibleText(interviewState);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    WebElement calendar4 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    String selectedDate4 = calendar4.getAttribute("value");

                    if (selectedDate4.equals("Select Date")) {
                        calendar4.click();
                        selectDataMet3 = false;
                    } else if (selectedDate4.equals("")) {
                        Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                        selectDataMet3 = true;
                    }
                } else {
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    selectDataMet3 = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDateInterviewPage();

            Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
        }

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
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 2 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            completeMonthForInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 3 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            completeMonthForInterviewPage();

            //Click next Month Element
            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            completeThirdMonthForInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if (Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == 4 && Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 0) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            completeMonthForInterviewPage();

            //Click next Month Element
            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            completeThirdMonthForInterviewPage();

            //Click next Month Element
            WebDriverWait fourthMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourthElement = fourthMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            fourthElement.click();

            completeFourthMonthForInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -10) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            WebDriverWait secondMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement second = secondMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select secondMonths = new Select(second);
            WebElement textElement = secondMonths.getFirstSelectedOption();
            String validateMonthElement = textElement.getText();


            if (!validateMonthElement.equals("Dec")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeMonthForInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -9) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            WebDriverWait secondMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement second = secondMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select secondMonths = new Select(second);
            WebElement textElement = secondMonths.getFirstSelectedOption();
            String validateMonthElement = textElement.getText();


            if (!validateMonthElement.equals("Nov")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeMonthForInterviewPage();

            //Click next Month Element
            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            WebDriverWait thirdMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement third = thirdMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select thirdMonths = new Select(third);
            WebElement textThirdElement = thirdMonths.getFirstSelectedOption();
            String validateThirdMonthElement = textThirdElement.getText();


            if (!validateThirdMonthElement.equals("Dec")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeThirdMonthForInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear = new Select(nextYearElement);
            NextYear.selectByVisibleText(toSelectYear);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toMonth) - Integer.parseInt(fromMonth) == -8) && (Integer.parseInt(toYear) - Integer.parseInt(fromYear) == 1)) {
            WebDriverWait defaultMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultElement = defaultMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select defaultMonth = new Select(defaultElement);
            defaultMonth.selectByVisibleText(fromSelectMonth);

            WebDriverWait defaultYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement defaultYearElement = defaultYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select defaultYear = new Select(defaultYearElement);
            defaultYear.selectByVisibleText(selectYear);

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            WebDriverWait secondMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement secondElement = secondMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            secondElement.click();

            WebDriverWait secondMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement second = secondMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select secondMonths = new Select(second);
            WebElement textElement = secondMonths.getFirstSelectedOption();
            String validateMonthElement = textElement.getText();


            if (!validateMonthElement.equals("Oct")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeMonthForInterviewPage();

            //Click next Month Element
            WebDriverWait thirdMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement thirdElement = thirdMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            thirdElement.click();

            WebDriverWait thirdMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement third = thirdMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select thirdMonths = new Select(third);
            WebElement textThirdElement = thirdMonths.getFirstSelectedOption();
            String validateThirdMonthElement = textThirdElement.getText();


            if (!validateThirdMonthElement.equals("Nov")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeThirdMonthForInterviewPage();

            //Click next Month Element
            WebDriverWait fourthMonthWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourthElement = fourthMonthWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            fourthElement.click();

            WebDriverWait fourthMonth = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourth = fourthMonth.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select fourthMonths = new Select(fourth);
            WebElement textFourthElement = fourthMonths.getFirstSelectedOption();
            String validateFourthMonthElement = textFourthElement.getText();


            if (!validateFourthMonthElement.equals("Dec")) {
                WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(nextYearElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeFourthMonthForInterviewPage();

            WebDriverWait defaultMonthWaits = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextMonthElement = defaultMonthWaits.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            Select nextMonth = new Select(nextMonthElement);
            nextMonth.selectByVisibleText(toSelectMonth);

            WebDriverWait nextYearWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement nextYearElement = nextYearWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            Select NextYear = new Select(nextYearElement);
            NextYear.selectByVisibleText(toSelectYear);

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
        int remainingDate = lastDate - fromDate;
        if (fromDate > toDate && fromMonth.equals(toMonth)) {

            WebElement dateElement = null;
            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));

                    dateElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    System.out.println("the Interview Page date: {0}, This date is selected." + date);
                    System.out.println("the Interview Page state: {0}, This state is selected." + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    throw new RuntimeException("The Interview Date is selected as " + date);

                } catch (NoSuchElementException exception) {
                    exception.getMessage();
                }
            }
        } else if (fromDate < toDate && fromMonth.equals(toMonth)) {
            WebElement dateElement = null;
            for (int date = fromDate; date <= toDate; date++) {
                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));

                    dateElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    System.out.println("the Interview Page date: {0}, This TimeStamp is selected." + date);
                    System.out.println("the Interview Page state: {0}, This TimeStamp is selected." + selectedState.getText());

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    throw new RuntimeException("The Interview Date is selected as " + date);
                } catch (NoSuchElementException exception) {
                    exception.getMessage();
                }
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
        int remainingDate = lastDate - fromDate;
        if (fromDate > toDate && !fromMonth.equals(toMonth)) {

            WebElement dateElement = null;
            for (int date = fromDate; date <= lastDate; date++) {

                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));

                    dateElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    System.out.println("the Interview Page date: {0}, This TimeStamp is selected." + date);
                    System.out.println("the Interview Page state: {0}, This TimeStamp is selected." + selectedState.getText());

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);
                } catch (NoSuchElementException exception) {
                    exception.getMessage();
                }
            }
        } else if (fromDate < toDate && !fromMonth.equals(toMonth)) {
            WebElement dateElement = null;
            for (int date = fromDate; date <= toDate; date++) {
                try {
                    dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    dateElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    log.log(Level.INFO, "TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    log.log(Level.INFO, "Date: {0}, This Date is selected." + date);
                    log.log(Level.INFO, "Selected Sate: {0}, The Selected State is . " + selectedState.getText());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                        jsExecutor.executeScript("arguments[0].click();", radioElement);
                        driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                        playAudio("file_example_WAV_1MG.wav");
                        throw new RuntimeException("The Interview Date is selected as " + date);

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
        WebElement dateElement = null;
        for (int date = 1; date <= toDate; date++) {

            try {
                dateElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                dateElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Interview Page date: {0}, This TimeStamp is selected." + date);
                System.out.println("the Interview Page state: {0}, This TimeStamp is selected." + selectedState.getText());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    throw new RuntimeException("The Interview Date is selected as " + date);

            } catch (NoSuchElementException exception) {
                exception.getMessage();
            }
        }
    }
}