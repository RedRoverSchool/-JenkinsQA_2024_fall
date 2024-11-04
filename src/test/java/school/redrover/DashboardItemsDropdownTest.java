package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

import java.time.Duration;
import java.util.List;

public class DashboardItemsDropdownTest extends BaseTest {

    private WebDriverWait setWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(15));
    }

    @Test
    public void testNewItem() {
        WebDriverWait wait = setWait();

        WebElement dashboardButton = getDriver().findElement(
                By.cssSelector("#breadcrumbs > li.jenkins-breadcrumbs__list-item"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(dashboardButton).perform();

        WebElement buttonDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#breadcrumbs > li.jenkins-breadcrumbs__list-item > a > button")));
        actions.moveToElement(buttonDropdown).click().perform();

        //Навожу на общий контейнер после клика
        WebElement containerDD = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#tippy-3 > div > div ")));
        actions.moveToElement(containerDD).perform();

        //Ожидаю пока спиннер закроется
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("#tippy-3 > div > div > p")));

        //Чек элементов в дропдаун с задержкой теперь должно всё пройти в CI
        List<WebElement> listDD = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("#tippy-3 > div > div > div > a")));
        Assert.assertFalse(listDD.isEmpty(), "Dropdown - empty");

        WebElement newItem = listDD.get(0);
        actions.moveToElement(newItem).click().perform();
    }
}