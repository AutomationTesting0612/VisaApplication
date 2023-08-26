package org.visaapplication;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    static FileReader reader;
    static WebDriver driver;

    private static final Logger log = LogManager.getLogger(Main.class);
    private static Date timestamp = new Date();

    static {
        try {
            reader = new FileReader("src/main/resources/application.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static Properties properties = new Properties();


    public static void main(String[] args) throws Exception {


        properties.load(reader);

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
//           options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("start-maximized");
//        options.addArguments("--disable-extensions");
//        options.addArguments("-private");
        options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));
        driver = new ChromeDriver(options);

//        driver.get("https://www.usvisascheduling.com/en-US/");
//        Thread.sleep(50000L);
//        driver.findElement(By.xpath("//input[@id='signInName']")).sendKeys("kanikanagori");
//        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("P@55word");

        LocalDate targetDate = LocalDate.of(2023, 9, 5);
        boolean conditionMet = true;

        // Start the application loop
        while (conditionMet) {
            LocalDate currentDate = LocalDate.now();

            // Compare the current date with the target date
            if (currentDate.isAfter(targetDate) || currentDate.isEqual(targetDate)) {
                System.out.println("Demo Application is only used for particular time period.");
                break; // Exit the loop
            } else {
                conditionMet = false;
            }
        }

//        --------image------
//        System.setProperty("java.library.path", "libs\\opencv_java320.dll");
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        WebElement captchaImageElement=  driver.findElement(By.xpath("//img[@id='captchaImage']"));
//        File imageFile = captchaImageElement.getScreenshotAs(OutputType.FILE);
//        String path = "capachaImages/capacha.png";
//        FileHandler.copy(imageFile, new File(path));
//        Thread.sleep(4000L);
//        ITesseract tesseract = new Tesseract();
//        String absolutePath = "tessdata";
//        tesseract.setDatapath(absolutePath);
//        tesseract.setVariable("user_defined_dpi", "100");
////        tesseract.setLanguage("eng");
//        try {
//        Mat image = Imgcodecs.imread(path);
//        Mat grayscaleImage = new Mat();
//        Imgproc.cvtColor(image, grayscaleImage, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.threshold(grayscaleImage, grayscaleImage, 0, 250, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//        Imgcodecs.imwrite(path, grayscaleImage);
//        String cap = tesseract.doOCR(new File(path));
//        cap = cap.replace(" ", "");
//        System.out.println("The Image is done " + cap);
//            WebElement capchaTextField= driver.findElement(By.xpath("//input[@id='extension_atlasCaptchaResponse']"));
//
//            capchaTextField.sendKeys(cap);
//        } catch (TesseractException e) {
//            System.err.println(e.getMessage());
//        }

//        Thread.sleep(10000L);
//
//        driver.findElement(By.xpath("//button[@id='continue']")).click();
//        Thread.sleep(5000L);
//        String motherMaidenQuestion = null;
//        String spouseQuestion = null;
//        String foodQuestion = null;
//        String cityQuestion = null;
//
//        try {
//            motherMaidenQuestion = driver.findElement(By.xpath("//p[contains(text(),'What is your mother's maiden name?')]")).getText();
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//        try {
//            spouseQuestion = driver.findElement(By.xpath("//p[contains(text(),'Where did you meet your spouse?')]")).getText();
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//        try {
//            foodQuestion = driver.findElement(By.xpath("//p[contains(text(),'What is your least favorite food?')]")).getText();
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//        try {
//            cityQuestion = driver.findElement(By.xpath("//p[contains(text(),'In what city or town was your first job?')]")).getText();
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//        String securityQuestion = driver.findElement(By.xpath("//label[contains(text(),'Security Question 1*')]")).getText();
//        WebElement spouseQuestionCondition = null;
//        WebElement foodQuestionCondition = null;
//        WebElement maidenNameQuestionCondition = null;
//        WebElement cityNameQuestionCondition = null;
//        try {
//            spouseQuestionCondition = driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/..//p[contains(text(),'')]"));
//            if (securityQuestion.equalsIgnoreCase("Security Question 1*") && spouseQuestionCondition.getText().equalsIgnoreCase("Where did you meet your spouse?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/..//p[contains(text(),'')]/../../../li[3]/div/input[@class='textInput']")).sendKeys("Delhi");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//        try {
//            foodQuestionCondition = driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/..//p[contains(text(),'')]"));
//            if (securityQuestion.equalsIgnoreCase("Security Question 1*") && foodQuestionCondition.getText().equalsIgnoreCase("What is your least favorite food?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/..//p[contains(text(),'')]/../../../li[3]/div/input[@class='textInput']")).sendKeys("Biryani");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//        try {
//            maidenNameQuestionCondition = driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/../p[contains(text(),'')]"));
//            if (securityQuestion.equalsIgnoreCase("Security Question 1*") && maidenNameQuestionCondition.getText().equalsIgnoreCase("What is your mother's maiden name?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/..//p[contains(text(),'')]/../../../li[3]/div/input[@class='textInput']")).sendKeys("AA");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//        try {
//            cityNameQuestionCondition = driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/../p[contains(text(),'')]"));
//            if (securityQuestion.equalsIgnoreCase("Security Question 1*") && maidenNameQuestionCondition.getText().equalsIgnoreCase("In what city or town was your first job?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[2]//div//label[contains(text(),'Security Question 1*')]/..//p[contains(text(),'')]/../../../li[3]/div/input[@class='textInput']")).sendKeys("Delhi");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//
//        String securityQuestion2 = driver.findElement(By.xpath("//label[contains(text(),'Security Question 2*')]")).getText();
//        WebElement spouseQuestionCondition2 = null;
//        WebElement foodQuestionCondition2 = null;
//        WebElement maidenNameQuestionCondition2 = null;
//        WebElement cityNameQuestionCondition2 = null;
//        try {
//            spouseQuestionCondition2 = driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]"));
//            if (securityQuestion2.equalsIgnoreCase("Security Question 2*") && spouseQuestionCondition2.getText().equalsIgnoreCase("Where did you meet your spouse?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]/../../../li[5]/div/input[@class='textInput']")).sendKeys("Delhi");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//        try {
//            foodQuestionCondition2 = driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]"));
//            if (securityQuestion2.equalsIgnoreCase("Security Question 2*") && foodQuestionCondition2.getText().equalsIgnoreCase("What is your least favorite food?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]/../../../li[5]/div/input[@class='textInput']")).sendKeys("Biryani");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//        try {
//            maidenNameQuestionCondition2 = driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]"));
//            if (securityQuestion2.equalsIgnoreCase("Security Question 2*") && maidenNameQuestionCondition2.getText().equalsIgnoreCase("What is your mother's maiden name?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]/../../../li[5]/div/input[@class='textInput']")).sendKeys("AA");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//        try {
//            cityNameQuestionCondition2 = driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]"));
//            if (securityQuestion2.equalsIgnoreCase("Security Question 2*") && maidenNameQuestionCondition2.getText().equalsIgnoreCase("In what city or town was your first job?")) {
//                driver.findElement(By.xpath("//div[@id='attributeList']//li[4]//div//label[contains(text(),'Security Question 2*')]/..//p[contains(text(),'')]/../../../li[5]/div/input[@class='textInput']")).sendKeys("Delhi");
//            }
//        } catch (NoSuchElementException e) {
//            e.getMessage();
//        }
//
//
//        driver.findElement(By.xpath("//button[@id='continue']")).click();
//
//        Thread.sleep(7000L);
//        driver.get("https://www.usvisascheduling.com/en-US/ofc-schedule/?reschedule=true");
//
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10000));

//        WebElement dropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement state = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='post_select']")));
        Select select = new Select(state);
        Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
        select.selectByVisibleText(properties.getProperty("selectState"));
        Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
        WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
        calendar.click();
//        int numberOfClicks = 20;
//        for (int i = 0; i < numberOfClicks; i++) {
//            state.click();
//            calendar.click();
//            if (driver.findElement(By.xpath("//div[@id='ui-datepicker-div']")).isDisplayed()) {
//                break;
//            }
//        }
        selectState();

    }

    public static void selectState() throws InterruptedException {

        String num = properties.getProperty("num");

        selectDate();
        WebElement postText = null;
        try {
            postText = driver.findElement(By.xpath("//label[contains(text(),'Consular Posts')]"));
            if (postText.getText().equals("Consular Posts")) {
                log.log(Level.INFO, "Timestamp: {0}, This is an info TimeStamp." + timestamp);
                throw new RuntimeException("The Data has been selected from calendar");
            }
        } catch (NoSuchElementException ex) {
            ex.getMessage();
        }



        if (num.equals("one")) {
            driver.quit();
            ChromeOptions options = new ChromeOptions();
//           options.addArguments("--headless");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--ignore-ssl-errors=yes");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("start-maximized");
            options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));

            driver = new ChromeDriver(options);


           WebElement element= driver.findElement(By.xpath("//label[contains(text(),'')]"));
           element.click();

            Thread.sleep(Long.parseLong(properties.getProperty("sleepTime")));
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText("");
            select1.selectByVisibleText(properties.getProperty("selectState"));
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTime")));
            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
            calendar.click();
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectState();
        } else if (num.equals("two")) {
            driver.quit();
            ChromeOptions options = new ChromeOptions();
//           options.addArguments("--headless");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--ignore-ssl-errors=yes");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("start-maximized");
            options.setExperimentalOption("debuggerAddress", "localhost:" + Integer.parseInt(properties.getProperty("port")));

            driver = new ChromeDriver(options);
            WebElement element= driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element.click();
            WebElement blankDropDown = driver.findElement(By.xpath("//select[@id='post_select']"));
            Select select1 = new Select(blankDropDown);
            select1.selectByVisibleText(properties.getProperty("selectState1"));
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTime")));
            WebElement calendar = driver.findElement(By.xpath("//input[@id='datepicker']"));
            calendar.click();
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectDate();
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTime")));
            WebElement element1= driver.findElement(By.xpath("//label[contains(text(),'')]"));
            element1.click();
            select1.selectByVisibleText(properties.getProperty("selectState"));
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTime")));
            WebElement calendar1 = driver.findElement(By.xpath("//input[@id='datepicker']"));
            calendar1.click();
            Thread.sleep(Long.parseLong(properties.getProperty("sleepTimeCalendar")));
            selectState();
        }

    }

    public static void selectDate() throws InterruptedException {

        WebElement fromYearElement = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']"));

        Select selectYear = new Select(fromYearElement);
        selectYear.selectByVisibleText(properties.getProperty("selectYear"));


        WebElement fromMonthElement = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']"));

        Select selectMonth = new Select(fromMonthElement);
        selectMonth.selectByVisibleText(properties.getProperty("selectMonth"));

        String xpath = "//*[@class='ui-datepicker-calendar']/tbody/tr[";
        String xpath1 = "]/td";
        List<WebElement> element1 = driver.findElements(By.xpath("//*[@class='ui-datepicker-calendar']/tbody/tr"));
        List<WebElement> element2 = driver.findElements(By.xpath("//*[@class='ui-datepicker-calendar']/tbody/tr/td"));
        String xpath2 = "]/td[";
        String xpath3 = "]";
        outerLoop:
        for (int i = 1; i < element1.size(); i++) {
            String finalXpath = xpath + i + xpath1;
            List<WebElement> tableDates = driver.findElements(By.xpath(finalXpath));
//            for (int j = startDay; j <= element2.size(); j++) {
//                String dateXpath = xpath + i + xpath2 + j + xpath3;

//                List<WebElement> dateFinalXpath = driver.findElements(By.xpath(dateXpath));

//                for (WebElement dates : dateFinalXpath) {

//                    String dateText = dates.getText();
//                    if (dateText.equals(null) || dateText.equals(" ")) {
//                        continue;
//                    } else {
            for (WebElement date : tableDates) {
//                            for (int day = Integer.parseInt(properties.getProperty("startDay")); day <= Integer.parseInt(properties.getProperty("endDay")); day++) {


                // Find the cell for the current day
                if (date.getAttribute("class").equals(" greenday")) {

                    log.log(Level.INFO, "Timestamp: {0}, This is an info TimeStamp." + timestamp);
                    date.click();
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    WebElement radioElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio']")));
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", radioElement);
                    jsExecutor.executeScript("arguments[0].click();", radioElement);
//                    driver.findElement(By.xpath("//input[@id='submitbtn']")).click();
                    break outerLoop;
                } else {
                    continue;
                }
            }
        }
    }
}