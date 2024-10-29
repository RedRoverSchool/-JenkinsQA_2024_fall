package school.redrover.old;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.Duration;

@Ignore
public class GroupAlexLeoTest {

    @Test(description = "Практика работы с чекбоксом  https://demoqa.com/checkbox")
    public void testCheckBox() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://demoqa.com/checkbox");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.findElement(By.xpath("//*[@id='tree-node']/ol/li/span/button")).click();
        driver.quit();
    }

    @Test(description = "Двойной клик https://demoqa.com/buttons")
    public void testDoubleClickButton() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://demoqa.com/buttons");
        driver.manage().window().maximize();
        WebElement doubleClickButton = driver.findElement(By.xpath("//button[@id='doubleClickBtn' and contains(text(), 'Double Click Me')]"));
        Actions actions = new Actions(driver);
        actions.doubleClick(doubleClickButton).perform();
        WebElement doubleClickMessage = driver.findElement(By.id("doubleClickMessage"));
        String doubleClickMessageText = doubleClickMessage.getText();

        Assert.assertEquals(doubleClickMessageText, "You have done a double click");
        driver.quit();

    }
    @Test(description = "Правый клик https://demoqa.com/buttons")
    public void testRightClickButton() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://demoqa.com/buttons");
        driver.manage().window().maximize();
        WebElement rightClickButton = driver.findElement(By.xpath("//button[@id='rightClickBtn' and contains(text(), 'Right Click Me')]"));
        Actions actions = new Actions(driver);
        actions.contextClick(rightClickButton).perform();
        WebElement rightClickClickMessage = driver.findElement(By.id("rightClickMessage"));
        String rightClickClickMessageText = rightClickClickMessage.getText();

        Assert.assertEquals(rightClickClickMessageText, "You have done a right click");
        driver.quit();

    }

    @Test(description = "Динамический клик https://demoqa.com/buttons")
    public void testDynamicClickButton() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://demoqa.com/buttons");
        driver.manage().window().maximize();
        WebElement dynamicClickButton = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[2]/div[2]/div[3]/button"));
        Actions actions = new Actions(driver);
        actions.click(dynamicClickButton).perform();
        WebElement dynamicClickClickMessage = driver.findElement(By.id("dynamicClickMessage"));
        String dynamicClickClickMessageText = dynamicClickClickMessage.getText();

        Assert.assertEquals(dynamicClickClickMessageText, "You have done a dynamic click");
        driver.quit();

    }

    @Test(description = "VerifyTitle https://discover.bklynlibrary.org/")
    public static void testVerifyTitle() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://discover.bklynlibrary.org/");
        String act_title = driver.getTitle();
        if(act_title.equals("Brooklyn Public Library")){
            System.out.println("Test Passed");
        }
        else{
            System.out.println("Test Failed");
        }
        driver.close();
    }
    @Test
    public void testSendMessage() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://automationintesting.online/");
        Thread.sleep(500);

        WebElement nameInput = driver.findElement(By.id("name"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nameInput);
        Thread.sleep(500);
        nameInput.sendKeys("John");

        WebElement emailInput = driver.findElement(By.xpath("//input[@id = 'email']"));
        emailInput.sendKeys("js@gmail.com");

        WebElement phoneInput = driver.findElement(By.id("phone"));
        phoneInput.sendKeys("12345678900");

        WebElement subjectInput = driver.findElement(By.xpath("//input[@id = 'subject']"));
        subjectInput.sendKeys("Double queen room");

        WebElement messageInput = driver.findElement(By.cssSelector("textarea"));
        messageInput.sendKeys("Hello! I would like to reserve a room");

        WebElement submitButton = driver.findElement(By.xpath("//button[@id = 'submitContact']"));
        submitButton.click();

        Thread.sleep(5000);
        String confirmationMessage = driver.findElement(By.xpath("//div[@class = 'row contact']//h2")).getText();
        Assert.assertEquals(confirmationMessage, "Thanks for getting in touch John!");
    }

    @Test(description = " Create an Account https://www.saucedemo.com/")
    public void testLogIn() {

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();

        WebElement usernameInput = driver.findElement(By.xpath("//input[@placeholder='Username']"));
        usernameInput.sendKeys("problem_user");

        WebElement passwordInput = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        passwordInput.sendKeys("secret_sauce");

        WebElement clickLoginButton = driver.findElement(By.xpath("//input[@class='submit-button btn_action']"));
        clickLoginButton.click();

        driver.quit();
    }
}
