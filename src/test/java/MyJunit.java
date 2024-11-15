import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyJunit {

    static WebDriver driver;

    @BeforeAll
    public static void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headed");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @DisplayName("Get website title")
    @Test
    public void getTitle() {
        driver.get("https://demoqa.com/");
        String titleActual = driver.getTitle();
        System.out.println(titleActual);
        String titleExpected = "DEMOQA";
        assertTrue(titleActual.contains(titleExpected));
        assertEquals(titleExpected, titleActual);
    }

    @Test
    public void submitForm() {
        driver.get("https://demoqa.com/text-box");

        List<WebElement> formControls = driver.findElements(By.className("form-control"));
        formControls.get(0).sendKeys("Miraj Uddin");
        formControls.get(1).sendKeys("miraj@gmail.com");
        formControls.get(2).sendKeys("Mirpur");
        formControls.get(3).sendKeys("Dhaka");

        Utils.scroll(driver, 600); // Make sure this utility method scrolls correctly

        driver.findElement(By.id("submit")).click();

        List<WebElement> resultElem = driver.findElements(By.tagName("p"));
        String nameActual = resultElem.size() > 0 ? resultElem.get(0).getText() : null;
        String emailActual = resultElem.size() > 1 ? resultElem.get(1).getText() : null;
        String currentAddressActual = resultElem.size() > 2 ? resultElem.get(2).getText() : null;
        String permanentAddressActual = resultElem.size() > 3 ? resultElem.get(3).getText() : null;

        String nameExpected = "Miraj";
        String emailExpected = "miraj@gmail.com";
        String currentAddressExpected = "Mirpur";
        String permanentAddressExpected = "Dhaka";

        // Prevent NullPointerException by checking for null values
        Assertions.assertNotNull(nameActual);
        Assertions.assertNotNull(emailActual);
        Assertions.assertNotNull(currentAddressActual);
        Assertions.assertNotNull(permanentAddressActual);

        assertTrue(nameActual.contains(nameExpected));
        assertTrue(emailActual.contains(emailExpected));
        assertTrue(currentAddressActual.contains(currentAddressExpected));
        assertTrue(permanentAddressActual.contains(permanentAddressExpected));
    }


    @Test
   public void clickButton(){
        driver.get("https://demoqa.com/buttons");
        List<WebElement>buttonElements=driver.findElements(By.cssSelector("[type=button]"));
        Actions actions=new Actions(driver);
        actions.doubleClick(buttonElements.get(1)).perform();
        actions.contextClick(buttonElements.get(2)).perform();
        actions.click(buttonElements.get(3)).perform();

    }


    @Test
    public void hundleAlert() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        //driver.findElement(By.id("timerAlertButton")).click();
        //Thread.sleep(6000);
        //driver.switchTo().alert().accept();
        Utils.scroll(driver,600);
        driver.findElement(By.id("confirmButton")).click();
        driver.switchTo().alert().accept();
        Thread.sleep(2000);
        driver.findElement(By.id("confirmButton")).click();
        driver.switchTo().alert().dismiss();

    }

    @Test
    public void selectDate(){
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
        WebElement calendarElem=driver.findElement(By.id("datePickerMonthYearInput"));
        calendarElem.click();
        calendarElem.sendKeys(Keys.CONTROL+"a",Keys.BACK_SPACE);
        calendarElem.sendKeys("20/11/2024");
        calendarElem.sendKeys(Keys.ENTER);
    }

    @Test
    public void selectDropdown(){
        driver.get("https://demoqa.com/select-menu");

        Select selectDropdown=new Select(driver.findElement(By.id("oldSelectMenu")));
        List<WebElement> element=selectDropdown.getOptions();
        int size=element.size();
        System.out.println(size);

        selectDropdown.selectByIndex(size-1);
    }


    @Test
    public void selectDropdown2(){
        driver.get("https://demoqa.com/select-menu");
        WebElement dropdownElem=driver.findElement(By.id("withOptGroup"));
        dropdownElem.click();
        Actions actions=new Actions(driver);
        actions.keyDown(Keys.ARROW_DOWN).perform();
       actions.sendKeys(Keys.ENTER).perform();
       // dropdownElem.sendKeys(Keys.ARROW_DOWN,Keys.ENTER);
    }

    @Test
    public void mouseHover(){
        driver.get("https://www.aiub.edu/");
        Actions actions=new Actions(driver);
        List<WebElement> menuElem=driver.findElements(By.xpath("//a[contains(text(),\"About\")]"));
        actions.moveToElement(menuElem.get(1)).perform();
    }

    @Test
    public void takeScreenshot() throws IOException {
        driver.get("https://demoqa.com");
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String filePath = "./src/test/resources/screenshots/" + timestamp + ".png";
        File destinationFile = new File(filePath);
        FileUtils.copyFile(screenshot, destinationFile);
    }


    @Test
    public void uploadFile(){

        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys(System.getProperty("user.dir")+"./src/test/resources/7.png");
    }


    @Test
    public void handleMultipleTab() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(3000);
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());
        System.out.println(w.get(0));
        System.out.println(w.get(1));

// Switch to the new tab
      driver.switchTo().window(w.get(1));
      System.out.println("New tab title: " + driver.getTitle());
      String text = driver.findElement(By.id("sampleHeading")).getText();
      Assertions.assertEquals(text, "This is a sample page");

        driver.close();
        driver.switchTo().window(w.get(0));

    }



    @Test
    public void handleWindow(){
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("windowButton")).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();

        while (iterator.hasNext()) {
            String handle = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(handle)) {
                driver.switchTo().window(handle);
                String heading = driver.findElement(By.id("sampleHeading")).getText();
                assertTrue(heading.contains("This is a sample page"));
            }
        }

        driver.close();
        driver.switchTo().window(mainWindowHandle);

    }

    @Test
    public void scrapData() {
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allrows = table.findElements(By.className("rt-tr"));

        int i = 0;
        for (WebElement row : allrows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                System.out.println("num[" + i++ + "] " + cell.getText());
            }
        }
    }




    @AfterAll
    public static void closeBrowser() {
        if (driver != null) {
            driver.quit(); // Ensure the browser closes after tests are completed
        }
    }
}
