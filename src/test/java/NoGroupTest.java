import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class NoGroupTest {

    @Test
    public void testSelenium() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        //driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        Assert.assertEquals(message.getText(), "Received!");

        driver.quit();
    }

    @Test
    public void testZara() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.zara.com/us/en/search/home");

        WebElement textBox = driver.findElement(By.xpath("//input[@aria-label='Search text input']"));
        textBox.sendKeys("red");
        Thread.sleep(500);
        textBox.sendKeys(Keys.RETURN);

        Thread.sleep(2000);

        WebElement itemTitle = driver.findElement(
                By.xpath("//*[@id='main']/article/div/div/section/div/section[1]/ul/li[1]/div[2]/div/div/div/div[1]/a/h2"));
        Assert.assertEquals(itemTitle.getText(), "GOLDEN BUTTON KNIT CARDIGAN");

        driver.quit();
    }

    @Test
    public void testLogin() {
        WebDriver driver=new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://practicetestautomation.com/practice-test-login/");

        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.id("submit"));

        username.sendKeys("student");
        password.sendKeys("Password123");
        login.click();

        Assert.assertTrue(driver.getCurrentUrl().contains("practicetestautomation.com/logged-in-successfully/"));

        driver.quit();
    }
    @Test
    public void testCheckIfElementExists() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://practicetestautomation.com/practice-test-login/");
        WebElement usernameField = driver.findElement(By.id("username"));

        Assert.assertNotNull(usernameField);

        driver.quit();
    }
    @Test
    public void testFindElementByXpath() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://practicetestautomation.com/practice-test-login/");
        WebElement usename = driver.findElement(By.xpath("//*[@type='text']"));

        Assert.assertTrue(usename.isDisplayed());

        driver.quit();
    }

    @Test
    public void testHoverOverButtonTotal() throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.get("https://coffee-cart.app/");

        Actions actions = new Actions(driver);

        List<WebElement> cups = driver.findElements(
                By.className("cup-body"));
        WebElement anyCup = cups.get(0);
        anyCup.click();

        Thread.sleep(1000);

        WebElement total = driver.findElement(
                By.xpath("//*[@aria-label='Proceed to checkout']"));
        actions.moveToElement(total).perform();

        Thread.sleep(1000);

        WebElement totalPreview = driver.findElement(
                By.className("cart-preview"));

        Assert.assertTrue(totalPreview.isDisplayed());

        driver.quit();

    }

    @Test
    public void testTicketSearchPage() {

        WebDriver driver = new ChromeDriver();
        driver.get("https://airmontenegro.com/en");

        WebElement agree_button = driver.findElement(By.xpath("//*[@id=\"content_wrap\"]/div[2]/button"));
        agree_button.click();
        WebElement search_filter = driver.findElement(By.xpath("//*[@id=\"reservations_table\"]"));

        Assert.assertTrue(search_filter.isDisplayed());

        driver.quit();
    }

    @Test
    public void testSearchWiki() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://en.wikipedia.org/wiki/Main_Page");

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.findElement(By.name("search"));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.findElement(By.name("search")).sendKeys("Selenium");

        driver.findElement(By.cssSelector("#searchform > div > button")).click();

        WebElement articleTitle = driver.findElement(By.xpath("//*[@id='firstHeading']/span[text()='Selenium']"));
        Assert.assertEquals(articleTitle.getText(), ("Selenium"));
        driver.quit();
    }
}
