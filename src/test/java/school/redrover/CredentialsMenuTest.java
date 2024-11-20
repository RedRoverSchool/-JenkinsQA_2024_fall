package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

import java.time.Duration;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

public class CredentialsMenuTest extends BaseTest {

    public void getCredentialsPage() {

        getDriver().findElement(By.className("model-link")).click();
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/user/admin/credentials']")))
                .click();
    }

    @Test
    public void testNavigateCredentialsMenu() {

        getCredentialsPage();

        Assert.assertEquals(getDriver().findElement(By.tagName("h1")).getText(), "Credentials");
    }

    @Test
    public void testAddDomainArrow() {

        getCredentialsPage();

        WebElement userAdmin = getDriver().findElement(By.cssSelector(".model-link.inside.jenkins-table__link"));

        Actions actions = new Actions(getDriver());
        actions.moveToElement(userAdmin).perform();

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        String arrowContent = (String) js.executeScript("return window.getComputedStyle(arguments[0], '::before').getPropertyValue('content');", userAdmin);


        assertTrue(!arrowContent.equals("none") && !arrowContent.isEmpty());
    }

    @Test
    public void testisDisplayedDomainElementDropdown() throws Exception {

        getCredentialsPage();

        WebElement element = getDriver().findElement(By.cssSelector(".model-link.inside.jenkins-table__link"));
        new Actions(getDriver()).moveToElement(element).perform();

        int elementWidth = element.getSize().getWidth();
        int elementHeight = element.getSize().getHeight();

        int xOffset = elementWidth - 9;
        int yOffset = elementHeight / 2;

        int attempts = 0;
        boolean arrowClicked = false;

        while (!arrowClicked && attempts < 3) {

            new Actions(getDriver()).moveToElement(element).perform();

            WebDriverWait shortWait = new WebDriverWait(getDriver(), Duration.ofMillis(500));
            shortWait.until(ExpectedConditions.visibilityOf(element));

            // Переходим к нужным координатам и кликаем
            new Actions(getDriver())
                    .moveToElement(element, xOffset, yOffset)
                    .pause(Duration.ofMillis(200)) // Короткая пауза перед кликом
                    .click()
                    .perform();

            arrowClicked = true;

        }



        WebElement element1 = getDriver().findElement(By.xpath("//div[contains(@class, 'tippy-box')]"));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element1);

        //assertTrue(addDomainElement.isDisplayed());

    }

}



