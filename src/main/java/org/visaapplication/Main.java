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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
//@PropertySource("file:src/main/resources/application.properties")
@PropertySource("file:application.properties")
public class Main implements CommandLineRunner {
     WebDriver driver;
     Logger log = LogManager.getLogger(Main.class);
     WebElement selectedState;
    WebElement webElement;
    WebElement blankDropDown;
    Select select1;
    String selectedDate;

    Select month;

    Select year;

    JavascriptExecutor jsExecutor;

    WebDriverWait webDriverWait;

    Timer timer;

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

        if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
            openChromeBrowser();
        } else {
            openEdgeBrowser();

        }

        timer = new Timer();

        timer.schedule(new TimerTask() {

            @Parameters("browser")
            public void run() {
            System.out.println(LocalDateTime.now());
            webElement = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            if (webElement.getText().equals("OFC Post")) {
                try {
                    selectState();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if(webElement.getText().equals("Consular Posts")) {
                try {
                    selectStateConsularPosts();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            }
        }, 0, Long.parseLong(schedulerTime));

    }

    public void selectState() throws InterruptedException {

        driver.quit();
        if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
            openChromeBrowser();
        } else {
            openEdgeBrowser();
        }

        String switchLocation = number;
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement=webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='gm_select']//li//label")));
        if (webElement.getText()!=null) {


        if (switchLocation.equals("one")) {
            webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);
            select1.selectByVisibleText("");

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                Thread.sleep(Long.parseLong(blankCalendarTimer));
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");
                // Compare the current date with the target date
                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    if (!driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
                        webElement.click();
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
            webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);
            select1.selectByVisibleText(selectState);
            selectedState = select1.getFirstSelectedOption();
            boolean selectDataMet = true;

            while (selectDataMet) {
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    selectedState = select1.getFirstSelectedOption();
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet = false;
                    } else if (selectedDate.equals("")) {
                        selectDataMet = true;
                    }

                } else {
                    selectDataMet = true;
                }
            }
            Thread.sleep(Long.parseLong(sleepTimeCalendar));
            selectDate();

            webElement = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = webElement.getText();
            if (ofcPost.equals("OFC Post")) {
                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select2 = new Select(blankDropDown);
                select2.selectByVisibleText(selectState1);
                selectedState = select2.getFirstSelectedOption();

                boolean selectDataMet2 = true;

                while (selectDataMet2) {

                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet2 = false;
                    } else if (selectedDate.equals("")) {
                        select2.selectByVisibleText(selectState);
                        selectedState = select2.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");
                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet2 = false;
                        } else if (selectedDate.equals("")) {
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
            //first state
            webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    selectedState = select1.getFirstSelectedOption();
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet = false;
                    } else if (selectedDate.equals("")) {
                        select1.selectByVisibleText(selectState2);
                        selectedState = select1.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");
                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet = false;
                        } else if (selectedDate.equals("")) {
                            select1.selectByVisibleText(selectState3);
                            selectedState = select1.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");
                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet = false;
                            } else if (selectedDate.equals("")) {
                                select1.selectByVisibleText(selectState4);
                                selectedState = select1.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");
                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
                                    selectDataMet = false;
                                } else if (selectedDate.equals("")) {
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

            webElement = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = webElement.getText();
            if (ofcPost.equals("OFC Post")) {

                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                Select select2 = new Select(webElement);

                boolean selectDataMet2 = true;

                while (selectDataMet2) {
                    select2.selectByVisibleText(selectState1);
                    selectedState = select2.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet2 = false;
                    } else if (selectedDate.equals("")) {
                        select2.selectByVisibleText(selectState2);
                        selectedState = select2.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");

                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet2 = false;
                        } else if (selectedDate.equals("")) {
                            select2.selectByVisibleText(selectState3);
                            selectedState = select2.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate= webElement.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet2 = false;
                            } else if (selectedDate.equals("")) {
                                select2.selectByVisibleText(selectState4);
                                selectedState = select2.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");

                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
                                    selectDataMet2 = false;
                                } else if (selectedDate.equals("")) {
                                    select2.selectByVisibleText(selectState);
                                    selectedState = select2.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    selectedDate = webElement.getAttribute("value");

                                    if (selectedDate.equals("Select Date")) {
                                        webElement.click();
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
                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select3 = new Select(blankDropDown);

                boolean selectDataMet3 = true;

                while (selectDataMet3) {
                    select3.selectByVisibleText(selectState2);
                    selectedState = select3.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet3 = false;
                    } else if (selectedDate.equals("")) {
                        select3.selectByVisibleText(selectState3);
                        selectedState = select3.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");
                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet3 = false;
                        } else if (selectedDate.equals("")) {
                            select3.selectByVisibleText(selectState4);
                            selectedState = select3.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");
                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet3 = false;
                            } else if (selectedDate.equals("")) {
                                select3.selectByVisibleText(selectState);
                                selectedState = select3.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");
                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
                                    selectDataMet3 = false;
                                } else if (selectedDate.equals("")) {
                                    select3.selectByVisibleText(selectState1);
                                    selectedState = select3.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    selectedDate = webElement.getAttribute("value");
                                    if (selectedDate.equals("Select Date")) {
                                        webElement.click();
                                        selectDataMet3 = false;
                                    } else if (selectedDate.equals("")) {
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
                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select4 = new Select(blankDropDown);

                boolean selectDataMet4 = true;

                while (selectDataMet4) {
                    select4.selectByVisibleText(selectState3);
                    selectedState = select4.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet4 = false;
                    } else if (selectedDate.equals("")) {
                        select4.selectByVisibleText(selectState4);
                        selectedState = select4.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");
                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet4 = false;
                        } else if (selectedDate.equals("")) {
                            select4.selectByVisibleText(selectState);
                            selectedState = select4.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");
                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet4 = false;
                            } else if (selectedDate.equals("")) {
                                select4.selectByVisibleText(selectState1);
                                selectedState = select4.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");
                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
                                    selectDataMet4 = false;
                                } else if (selectedDate.equals("")) {
                                    select4.selectByVisibleText(selectState2);
                                    selectedState = select4.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    selectedDate = webElement.getAttribute("value");
                                    if (selectedDate.equals("Select Date")) {
                                        webElement.click();
                                        selectDataMet4 = false;
                                    } else if (selectedDate.equals("")) {
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
                blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select5 = new Select(blankDropDown);

                boolean selectDataMet5 = true;

                while (selectDataMet5) {
                    select5.selectByVisibleText(selectState4);
                    selectedState = select5.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet5 = false;
                    } else if (selectedDate.equals("")) {
                        select5.selectByVisibleText(selectState);
                        selectedState = select5.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");

                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet5 = false;
                        } else if (selectedDate.equals("")) {
                            select5.selectByVisibleText(selectState1);
                            selectedState = select5.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet5 = false;
                            } else if (selectedDate.equals("")) {

                                select5.selectByVisibleText(selectState2);
                                selectedState = select5.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");

                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
                                    selectDataMet5 = false;
                                } else if (selectedDate.equals("")) {
                                    select5.selectByVisibleText(selectState3);
                                    selectedState = select5.getFirstSelectedOption();
                                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                    selectedDate = webElement.getAttribute("value");

                                    if (selectedDate.equals("Select Date")) {
                                        webElement.click();
                                        selectDataMet5 = false;
                                    } else if (selectedDate.equals("")) {
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
            //first state
            webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    selectedState = select1.getFirstSelectedOption();
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet = false;
                    } else if(selectedDate.equals("")) {
                        select1.selectByVisibleText(selectState2);
                        selectedState = select1.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        WebElement calendar3 = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = calendar3.getAttribute("value");

                        if (selectedDate.equals("Select Date")) {
                            calendar3.click();
                            selectDataMet = false;
                        } else if(selectedDate.equals("")) {
                            select1.selectByVisibleText(selectState3);
                            selectedState = select1.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet = false;
                            } else if (selectedDate.equals("")) {
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

            webElement = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = webElement.getText();
            if (ofcPost.equals("OFC Post")) {

                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                Select select2 = new Select(webElement);

                boolean selectDataMet2 = true;

                while (selectDataMet2) {
                    select2.selectByVisibleText(selectState1);
                    selectedState = select2.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    // Compare the current date with the target date
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet2 = false;
                    } else if (selectedDate.equals("")) {
                        select2.selectByVisibleText(selectState2);
                        selectedState = select2.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");

                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet2 = false;
                        } else if (selectedDate.equals("")) {
                            select2.selectByVisibleText(selectState3);
                            selectedState = select2.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet2 = false;
                            } else if (selectedDate.equals("")) {
                                select2.selectByVisibleText(selectState);
                                selectedState = select2.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");

                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
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
                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select3 = new Select(blankDropDown);

                boolean selectDataMet3 = true;

                while (selectDataMet3) {
                    select3.selectByVisibleText(selectState2);
                    selectedState = select3.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate= webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet3 = false;
                    } else if (selectedDate.equals("")) {
                        select3.selectByVisibleText(selectState3);
                        selectedState = select3.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");

                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet3 = false;
                        } else if (selectedDate.equals("")) {
                            select3.selectByVisibleText(selectState);
                            selectedState = select3.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet3 = false;
                            } else if (selectedDate.equals("")) {
                                select3.selectByVisibleText(selectState1);
                                selectedState = select3.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");

                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
                                    selectDataMet3 = false;
                                } else if (selectedDate.equals("")) {

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
                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select4 = new Select(blankDropDown);

                boolean selectDataMet4 = true;

                while (selectDataMet4) {
                    select4.selectByVisibleText(selectState3);
                    selectedState = select4.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    // Compare the current date with the target date
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet4 = false;
                    } else if (selectedDate.equals("")) {
                        select4.selectByVisibleText(selectState);
                        selectedState = select4.getFirstSelectedOption();
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");

                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet4 = false;
                        } else if (selectedDate.equals("")) {
                            select4.selectByVisibleText(selectState1);
                            selectedState = select4.getFirstSelectedOption();
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet4 = false;
                            } else if (selectedDate.equals("")) {
                                select4.selectByVisibleText(selectState2);
                                selectedState = select4.getFirstSelectedOption();
                                Thread.sleep(Long.parseLong(blankCalendarTimer));
                                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                                selectedDate = webElement.getAttribute("value");

                                if (selectedDate.equals("Select Date")) {
                                    webElement.click();
                                    selectDataMet4 = false;
                                } else if (selectedDate.equals("")) {
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

            //first state
            webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);
            boolean selectDataMet = true;

            while (selectDataMet) {

                select1.selectByVisibleText(selectState);
                selectedState = select1.getFirstSelectedOption();
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");
                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet = false;
                } else if (selectedDate.equals("")) {
                    select1.selectByVisibleText(selectState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet = false;
                    } else if (selectedDate.equals("")) {
                        select1.selectByVisibleText(selectState2);
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");
                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet = false;
                        } else if (selectedDate.equals("")) {
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
            webElement = driver.findElement(By.xpath("(//label[@class='control-label'])[1]"));
            String ofcPost = webElement.getText();
            if (ofcPost.equals("OFC Post")) {
                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                Select select2 = new Select(blankDropDown);
                boolean selectDataMet2 = true;

                while (selectDataMet2) {
                    select2.selectByVisibleText(selectState1);
                    selectedState = select2.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    // Compare the current date with the target date
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet2 = false;
                    } else if (selectedDate.equals("")) {
                        select2.selectByVisibleText(selectState2);
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");
                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet2 = false;
                        } else if (selectedDate.equals("")) {
                            select2.selectByVisibleText(selectState);
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");
                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet2 = false;
                            } else if (selectedDate.equals("")) {
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
                webElement = driver.findElement(By.xpath("//label[contains(text(),'')]"));
                webElement.click();
                blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
                Select select3 = new Select(blankDropDown);
                boolean selectDataMet3 = true;

                while (selectDataMet3) {
                    select3.selectByVisibleText(selectState2);
                    selectedState = select3.getFirstSelectedOption();
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet3 = false;
                    } else if (selectedDate.equals("")) {
                        select3.selectByVisibleText(selectState);
                        Thread.sleep(Long.parseLong(blankCalendarTimer));
                        webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                        selectedDate = webElement.getAttribute("value");

                        if (selectedDate.equals("Select Date")) {
                            webElement.click();
                            selectDataMet3 = false;
                        } else if (selectedDate.equals("")) {
                            select3.selectByVisibleText(selectState1);
                            Thread.sleep(Long.parseLong(blankCalendarTimer));
                            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                            selectedDate = webElement.getAttribute("value");

                            if (selectedDate.equals("Select Date")) {
                                webElement.click();
                                selectDataMet3 = false;
                            } else if (selectedDate.equals("")) {
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

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        month = new Select(webElement);
        month.selectByVisibleText(fromSelectMonth);

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        year = new Select(webElement);
        year.selectByVisibleText(selectYear);

        if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 0 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRange();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 1 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRange();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -11) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRange();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            year = new Select(webElement);
            year.selectByVisibleText(toSelectYear);

            toDateRange();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 2 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRange();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 3 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRange();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeThirdMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 4 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(fromSelectMonth);

            dateRange();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeThirdMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRange();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -10) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRange();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            webElement = month.getFirstSelectedOption();
            String validateMonthElement = webElement.getText();


            if (!validateMonthElement.equals("Dec")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            year = new Select(webElement);
            year.selectByVisibleText(toSelectYear);

            toDateRange();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -9) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRange();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            webElement = month.getFirstSelectedOption();
            String validateMonthElement = webElement.getText();

            if (!validateMonthElement.equals("Dec")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            webElement = month.getFirstSelectedOption();
            String validateThirdMonthElement = webElement.getText();


            if (!validateThirdMonthElement.equals("Nov")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                Select NextYear = new Select(webElement);
                NextYear.selectByVisibleText(toSelectYear);
            }

            completeThirdMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            year = new Select(webElement);
            year.selectByVisibleText(toSelectYear);

            toDateRange();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -8) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRange();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            webElement = month.getFirstSelectedOption();
            String validateMonthElement = webElement.getText();


            if (!validateMonthElement.equals("Oct")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            webElement = month.getFirstSelectedOption();
            String validateThirdMonthElement = webElement.getText();


            if (!validateThirdMonthElement.equals("Nov")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeThirdMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            webElement = month.getFirstSelectedOption();
            String validateFourthMonthElement = webElement.getText();


            if (!validateFourthMonthElement.equals("Dec")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }


            completeFourthMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            year = new Select(webElement);
            year.selectByVisibleText(toSelectYear);

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
                    webElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    webElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                    System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());
                    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                        jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                        jsExecutor.executeScript("arguments[0].click();", webElement);
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
                    webElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                    webElement.click(); // click on the date
                    LocalDate timestamp = LocalDate.now();
                    System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                    System.out.println("the Ofc Page date: {0}, This Date is selected." + date);
                    System.out.println("the Ofc Page state: {0}, This State is selected." + selectedState.getText());
                    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                    jsExecutor.executeScript("arguments[0].click();", webElement);
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
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = webElement.getAttribute("value");

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = webElement.getAttribute("value");


        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                webElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                webElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                    jsExecutor.executeScript("arguments[0].click();", webElement);
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

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = webElement.getAttribute("value");

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = webElement.getAttribute("value");

        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                webElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                webElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Interview Page date: {0}, This date is selected." + date);
                System.out.println("the Interview Page state: {0}, This state is selected." + selectedState.getText());

                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                jsExecutor.executeScript("arguments[0].click();", webElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                throw new RuntimeException("The Interview Date is selected as " + date);
            } catch (NoSuchElementException exception) {
                exception.getMessage();
            }

        }
    }

    public void completeThirdMonth() {
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = webElement.getAttribute("value");

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = webElement.getAttribute("value");

        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                webElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                webElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Ofc Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Ofc Page date: {0}, This date is selected." + date);
                System.out.println("the Ofc Page state: {0}, This state is selected." + selectedState.getText());

                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));

                jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                jsExecutor.executeScript("arguments[0].click();", webElement);
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

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = webElement.getAttribute("value");

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
        String yearElement = webElement.getAttribute("value");


        LocalDate endDate = LocalDate.of(Integer.parseInt(yearElement), Integer.parseInt(monthElement) + 1, Integer.parseInt(toDay));
        int endDay = endDate.lengthOfMonth();

        for (int date = 1; date <= endDay; date++) {

            try {
                webElement = driver.findElement(By.xpath(String.format("//a[@data-date='%s']", date)));
                webElement.click(); // click on the date
                LocalDate timestamp = LocalDate.now();
                System.out.println("the Interview Page TimeStamp: {0}, This TimeStamp is selected." + timestamp);
                System.out.println("the Interview Page date: {0}, This date is selected." + date);
                System.out.println("the Interview Page state: {0}, This state is selected." + selectedState.getText());

                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));

                jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                jsExecutor.executeScript("arguments[0].click();", webElement);
                driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                playAudio("file_example_WAV_1MG.wav");
                throw new RuntimeException("The Interview Date is selected as " + date);

            } catch (NoSuchElementException exception) {
                exception.getMessage();
            }

        }
    }

    public void completeFourthMonth() {

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
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

                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                jsExecutor = (JavascriptExecutor) driver;
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

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
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

                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                jsExecutor = (JavascriptExecutor) driver;
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

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']//option[@selected='selected']")));
        String monthElement = secondElement.getAttribute("value");

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement secondYearElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']//option[@selected='selected']")));
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
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement radioElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                jsExecutor = (JavascriptExecutor) driver;
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

                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement radioElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));

                    jsExecutor = (JavascriptExecutor) driver;
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

        driver.quit();
        if (browser.equals("chrome") || browser.equals("chromium") || browser.equals("brave")) {
            openChromeBrowser();
        } else {
            openEdgeBrowser();
        }

        String number = numberInterview;

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement nameElement=webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='gm_select']//li//label")));
        if (nameElement.getText()!=null) {

        if (number.equals("one")) {

             blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
             select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();
                Thread.sleep(Long.parseLong(blankCalendarTimer));
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    if (!driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
                        webElement.click();
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

            //first state
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);

            boolean selectDataMet = true;

            while (selectDataMet) {
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet = false;
                    } else if (selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            boolean selectDataMet1 = true;


            while (selectDataMet1) {
                select1.selectByVisibleText(interviewState1);
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");
                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet1 = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(interviewState);
                    Thread.sleep(Long.parseLong(stateSleepTimeCalendar));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet1 = false;
                    } else if(selectedDate.equals("")) {
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
            //first state
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);

            boolean selectDataMet1 = true;

            while (selectDataMet1) {

                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");


                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet1 = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");
                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet1 = false;
                    } else if(selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select2 = new Select(blankDropDown);

            boolean selectDataMet2 = true;

            while (selectDataMet2) {

                select2.selectByVisibleText(interviewState1);
                selectedState = select2.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet2 = false;
                } else if(selectedDate.equals("")) {
                    select2.selectByVisibleText(interviewState2);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet2 = false;
                    } else if(selectedDate.equals("")) {
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

        //third stat
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
        webElement.click();
        blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select3 = new Select(blankDropDown);

        boolean selectDataMet3 = true;

        while (selectDataMet3) {

            select3.selectByVisibleText(interviewState2);
            selectedState = select3.getFirstSelectedOption();
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
            selectedDate = webElement.getAttribute("value");

            if (selectedDate.equals("Select Date")) {
                webElement.click();
                selectDataMet3 = false;
            } else if(selectedDate.equals("")) {
                select3.selectByVisibleText(interviewState3);
               Thread.sleep(Long.parseLong(blankCalendarTimer));
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet3 = false;
                } else if(selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
        blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select4 = new Select(blankDropDown);

        boolean selectDataMet4 = true;

        while (selectDataMet4) {

            select4.selectByVisibleText(interviewState3);
            selectedState = select4.getFirstSelectedOption();
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();

            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
            selectedDate = webElement.getAttribute("value");

            if (selectedDate.equals("Select Date")) {
                webElement.click();
                selectDataMet4 = false;
            } else if(selectedDate.equals("")) {
                select4.selectByVisibleText(interviewState4);
                Thread.sleep(Long.parseLong(blankCalendarTimer));
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet4 = false;
                } else if (selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
        blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
        Select select5 = new Select(blankDropDown);

        boolean selectDataMet5 = true;

        while (selectDataMet5) {
            select5.selectByVisibleText(interviewState4);
            selectedState = select5.getFirstSelectedOption();
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();

            webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
            selectedDate = webElement.getAttribute("value");

            // Compare the current date with the target date
            if (selectedDate.equals("Select Date")) {
                webElement.click();
                selectDataMet5 = false;
            } else if(selectedDate.equals("")) {
                select5.selectByVisibleText(interviewState);
               Thread.sleep(Long.parseLong(blankCalendarTimer));
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet5 = false;
                } else if (selectedDate.equals("")) {
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

            //first state
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);

            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet1 = false;
                } else if(selectedDate.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                   Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet1 = false;
                    } else if (selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select2 = new Select(blankDropDown);
            boolean selectDataMet2 = true;

            while (selectDataMet2) {
                select2.selectByVisibleText(interviewState1);
                selectedState = select2.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet2 = false;
                } else if(selectedDate.equals("")) {
                    select2.selectByVisibleText(interviewState2);
                  Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet2 = false;
                    } else if(selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown);

            boolean selectDataMet3 = true;

            while (selectDataMet3) {
                select3.selectByVisibleText(interviewState2);
                selectedState = select3.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate= webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet3 = false;
                } else if(selectedDate.equals("")) {
                    select3.selectByVisibleText(interviewState3);
                  Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet3 = false;
                    } else if (selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select4 = new Select(blankDropDown);

            boolean selectDataMet4 = true;

            while (selectDataMet4) {
                select4.selectByVisibleText(interviewState3);
                selectedState = select4.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet4 = false;
                } else if(selectedDate.equals("")) {
                    select4.selectByVisibleText(interviewState);
                  Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet4 = false;
                    } else if(selectedDate.equals("")) {
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

            //first state
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            select1 = new Select(blankDropDown);
            boolean selectDataMet1 = true;

            while (selectDataMet1) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();
                select1.selectByVisibleText(interviewState);
                selectedState = select1.getFirstSelectedOption();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet1 = false;
                } else if (selectedDate.equals("")) {
                    select1.selectByVisibleText(interviewState1);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet1 = false;
                    } else if (selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select2 = new Select(blankDropDown);

            boolean selectDataMet2 = true;

            while (selectDataMet2) {

                select2.selectByVisibleText(interviewState1);
                selectedState = select2.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();

                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet2 = false;
                } else if (selectedDate.equals("")) {
                    select2.selectByVisibleText(interviewState2);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet2 = false;
                    } else if (selectedDate.equals("")) {
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
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
            webElement.click();
            blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select3 = new Select(blankDropDown);

            boolean selectDataMet3 = true;

            while (selectDataMet3) {

                select3.selectByVisibleText(interviewState2);
                selectedState = select3.getFirstSelectedOption();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return to top')]")));
                webElement.click();
                webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                selectedDate = webElement.getAttribute("value");

                if (selectedDate.equals("Select Date")) {
                    webElement.click();
                    selectDataMet3 = false;
                } else if (selectedDate.equals("")) {
                    select3.selectByVisibleText(interviewState);
                    Thread.sleep(Long.parseLong(blankCalendarTimer));
                    webElement = driver.findElement(By.xpath("//input[@id='datepicker']"));
                    selectedDate = webElement.getAttribute("value");

                    if (selectedDate.equals("Select Date")) {
                        webElement.click();
                        selectDataMet3 = false;
                    } else if (selectedDate.equals("")) {
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

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
        month = new Select(webElement);
        month.selectByVisibleText(fromSelectMonth);

        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
        year = new Select(webElement);
        year.selectByVisibleText(selectYear);

        if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 0 || Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {
            dateRangeInterviewPage();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 1 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRangeInterviewPageDifferentMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -11) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRangeInterviewPageDifferentMonth();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            year = new Select(webElement);
            year.selectByVisibleText(toSelectYear);

            toDateRangeInterviewPage();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 2 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeMonthForInterviewPage();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 3 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeMonthForInterviewPage();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeThirdMonthForInterviewPage();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if (Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == 4 && Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 0) {

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeMonthForInterviewPage();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeThirdMonthForInterviewPage();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            completeFourthMonthForInterviewPage();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -10) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            webElement = month.getFirstSelectedOption();
            String validateMonthElement = webElement.getText();


            if (!validateMonthElement.equals("Dec")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeMonthForInterviewPage();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -9) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            select1 = new Select(webElement);
            webElement = select1.getFirstSelectedOption();
            String validateMonthElement = webElement.getText();


            if (!validateMonthElement.equals("Nov")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeMonthForInterviewPage();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            select1 = new Select(webElement);
            webElement = select1.getFirstSelectedOption();
            String validateThirdMonthElement = webElement.getText();


            if (!validateThirdMonthElement.equals("Dec")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeThirdMonthForInterviewPage();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            year = new Select(webElement);
            year.selectByVisibleText(toSelectYear);

            toDateRangeInterviewPage();
        } else if ((Integer.parseInt(toSelectMonthDigitRange) - Integer.parseInt(selectMonthDigitRange) == -8) && (Integer.parseInt(toSelectYear) - Integer.parseInt(selectYear) == 1)) {

            dateRangeInterviewPageDifferentMonth();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            select1 = new Select(webElement);
            webElement = select1.getFirstSelectedOption();
            String validateMonthElement = webElement.getText();


            if (!validateMonthElement.equals("Oct")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeMonthForInterviewPage();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            select1 = new Select(webElement);
            webElement = select1.getFirstSelectedOption();
            String validateThirdMonthElement = webElement.getText();


            if (!validateThirdMonthElement.equals("Nov")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeThirdMonthForInterviewPage();

            //Click next Month Element
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Next')]")));
            webElement.click();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            select1 = new Select(webElement);
            webElement = select1.getFirstSelectedOption();
            String validateFourthMonthElement = webElement.getText();


            if (!validateFourthMonthElement.equals("Dec")) {
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
                year = new Select(webElement);
                year.selectByVisibleText(toSelectYear);
            }

            completeFourthMonthForInterviewPage();

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-month']")));
            month = new Select(webElement);
            month.selectByVisibleText(toSelectMonth);

            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@class='ui-datepicker-year']")));
            year = new Select(webElement);
            year.selectByVisibleText(toSelectYear);

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
                    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                    jsExecutor.executeScript("arguments[0].click();", webElement);
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

                    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    jsExecutor = (JavascriptExecutor) driver;
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

                    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement radioElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                        jsExecutor = (JavascriptExecutor) driver;
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
                    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                        jsExecutor = (JavascriptExecutor) driver;
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                        jsExecutor.executeScript("arguments[0].click();", webElement);
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

                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                    jsExecutor.executeScript("arguments[0].click();", webElement);
                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    playAudio("file_example_WAV_1MG.wav");
                    throw new RuntimeException("The Interview Date is selected as " + date);

            } catch (NoSuchElementException exception) {
                exception.getMessage();
            }
        }
    }
}