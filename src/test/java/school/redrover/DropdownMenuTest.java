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

public class DropdownMenuTest extends BaseTest {

    private void createItem() {
        getDriver().findElement(By.xpath("//a[@href=\"/view/all/newJob\"]")).click();
        getDriver().findElement(By.id("name")).sendKeys("first");
        getDriver().findElement(By.xpath("//li[@class=\"hudson_model_FreeStyleProject\"]")).click();
        getDriver().findElement(By.id("ok-button")).click();
        WebElement saveButton = getDriver().findElement(By.xpath("//button[@name='Submit']"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(saveButton).click().perform();
        getDriver().findElement(By.xpath("//*[@id=\"breadcrumbs\"]/li[1]/a")).click();
    }

    @Test
    public void testDropDownMenu() {
        createItem();

        Actions actions = new Actions(getDriver());
        WebElement dropdownButton = getDriver().findElement(By.xpath("//*[@data-href='http://localhost:8080/job/first/']"));
        actions.moveToElement(dropdownButton)
                .perform();

        WebDriverWait waiter = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"job_first\"]/td[3]/a/button"))).click();

        List<String> dropDownItems = getDriver().findElements(By.xpath("//*[@class=\"jenkins-dropdown__item \"]")).stream()
                .map( WebElement :: getText).toList();
        System.out.println(dropDownItems);

        List<String> strings = List.of(
                "Changes",
                "Workspace",
                "Build Now",
                "Configure",
                "Delete Project",
                "Rename"
        );

        Assert.assertEquals(dropDownItems, strings);
    }
}
