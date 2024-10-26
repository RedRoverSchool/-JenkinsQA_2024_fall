import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class GroupWnFTest {

    private static final String USER_NAME = "Akiko";
    private static final String USER_EMAIL = "iakikoaokii@gmail.com";
    private static final String USER_ADDRESS = "Moscow";
    private static final String USER_PER_ADDRESS = "Street 1, House 1";
    @Test
    public void testDemoQATextBox() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://demoqa.com/text-box");

        WebElement inputName = driver.findElement(By.id("userName"));
        inputName.sendKeys(USER_NAME);

        WebElement inputEmail = driver.findElement(By.id("userEmail"));
        inputEmail.sendKeys(USER_EMAIL);

        WebElement inputAdress = driver.findElement(By.id("currentAddress"));
        inputAdress.sendKeys(USER_ADDRESS);

        WebElement inputPerAdress = driver.findElement(By.id("permanentAddress"));
        inputPerAdress.sendKeys(USER_PER_ADDRESS);

        JavascriptExecutor js1 = (JavascriptExecutor) driver; 
        js1.executeScript("window.scrollBy(0,1000)");

        WebElement button = driver.findElement(By.xpath("//*[@id=\"submit\"]"));
        button.click();

        WebElement actualName = driver.findElement(By.id("name"));
        WebElement actualEmail = driver.findElement(By.id("email"));
        WebElement actualAddress = driver.findElement(By.xpath("//p[@id='currentAddress']"));
        WebElement actualPerAddress = driver.findElement(By.xpath("//p[@id='permanentAddress']"));

        Assert.assertEquals(actualName.getText(), "Name:" + USER_NAME);
        Assert.assertEquals(actualEmail.getText(), "Email:" + USER_EMAIL);
        Assert.assertEquals(actualAddress.getText(), "Current Address :" + USER_ADDRESS);
        Assert.assertEquals(actualPerAddress.getText(), "Permananet Address :" + USER_PER_ADDRESS);

        driver.quit();
    }

    @Test
        public void testMango() {

            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://shop.mango.com/uz/en");
            driver.getTitle();
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

            // Нашли кнопку с кукисами и нажали "accept all cookies"
            WebElement submitButton = driver.findElement(By.id("cookies.button.acceptAll"));
            submitButton.click();

            // Нааходим кнопку "Accept", что мы в Узбекистане
            WebElement submitButton1 = driver.findElement(By.id("changeCountryAccept"));
            submitButton1.click();

            // Нашли кнопку Woman и перешли в раздел женской одежды
            WebElement submitButton3 = driver.findElement(By.xpath("//a[contains(@href, 'women/promotion_7914393e')]"));
            submitButton3.click();

            // Сравниваем URL ожидаемый и фактический
            String expectedUrl = "https://shop.mango.com/uz/en/c/women/promotion_7914393e";
            String actualUrl = driver.getCurrentUrl();
            Assert.assertEquals(actualUrl, expectedUrl, "URL не соответствует ожидаемому!");

            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

            // Ищем кнопку "добавить в избранное" (сердечко wishlist под товаром)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            WebElement favouritesElement = driver.findElement(By.xpath("/html/body/div/main/div/div[4]/ul/li[2]/div/div/div[1]/div[2]/div[1]/div/button"));
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            favouritesElement.click();
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

            Assert.assertNotNull(favouritesElement, "Кнопка 'Добавить в избранное' не найдена!");

            // Проверяем, что элемент видим и доступен для клика
            Assert.assertTrue(favouritesElement.isDisplayed(), "Кнопка 'Добавить в избранное' не видима!");
            Assert.assertTrue(favouritesElement.isEnabled(), "Кнопка 'Добавить в избранное' недоступна для клика!");


            // Ищем и нажимаем кнопку wishlist
            WebElement wishListElement = driver.findElement(By.xpath("/html/body/div/header/div[1]/div[3]/ul/li[3]/a"));
            wishListElement.click();

            // Сравниваем второй URL
            String expectedUrl2 = "https://shop.mango.com/uz/en/favorites";
            String actualUrl2 = driver.getCurrentUrl();
            Assert.assertEquals(actualUrl2, expectedUrl2, "URL не соответствует ожидаемому!");

            driver.quit();
        }
}
