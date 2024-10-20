import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaksimSTest {

    @Test
    public void testText() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        WebElement selectDropdown = driver.findElement(By.name("my-select"));
        selectDropdown.click();

        WebElement selectOption = driver.findElement(By.cssSelector("[value='2']"));
        selectOption.click();

        WebElement submitButton = driver.findElement(By.cssSelector("[type='submit']"));
        submitButton.click();

        WebElement messageText = driver.findElement(By.id("message"));

        Assert.assertEquals(messageText.getText(),"Received!");
        driver.quit();
    }

    @Test
    public void testCheck() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        WebElement checkInput = driver.findElement(By.id("my-check-2"));
        checkInput.click();

        Assert.assertEquals(checkInput.getDomProperty("checked"),"true");

        driver.quit();
    }

    @Test
    public void testSortingPrice() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.saucedemo.com/");

        WebElement loginInput = driver.findElement(By.id("user-name"));
        loginInput.sendKeys("standard_user");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("secret_sauce");

        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        WebElement sortingSelect = driver.findElement(By.className("product_sort_container"));
        sortingSelect.click();

        WebElement lohiOption = driver.findElement(By.cssSelector("[value='lohi']"));
        lohiOption.click();

        List<WebElement> priceList = driver.findElements(By.className("inventory_item_price"));

        List<Double> prices = new ArrayList<>();

        for (WebElement priceElement: priceList) {
           String priceText = priceElement.getText().replace("$", "");
            prices.add(Double.parseDouble(priceText));
        }

        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);

        Assert.assertEquals(prices,sortedPrices);

        driver.quit();
    }

    @Test
    public void testSelectingOption() {

        WebDriver driver = new ChromeDriver();

        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        WebElement dropdownSelect = driver.findElement(By.cssSelector("[name='my-select']"));
        Select select = new Select(dropdownSelect);
        select.selectByVisibleText("Three");

        WebElement threeOption = driver.findElement(By.cssSelector("[value='3']"));
        Assert.assertTrue(threeOption.isSelected());

        driver.quit();
    }
}
