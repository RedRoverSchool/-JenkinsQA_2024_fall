package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

import java.util.List;

public class ManageJenkinsTest extends BaseTest {

    @Test
    public void testManageJenkinsTab() throws InterruptedException {

        List<WebElement> tasks = getDriver().findElements(
                By.xpath("//div[@id='tasks']//a"));
        Assert.assertEquals(tasks.size(), 4);

        Assert.assertEquals(tasks.get(0).getText(), "New Item");
        Assert.assertEquals(tasks.get(1).getText(), "Build History");
        Assert.assertEquals(tasks.get(2).getText(), "Manage Jenkins");
        Assert.assertEquals(tasks.get(3).getText(), "My Views");

        Thread.sleep(2000);

        WebElement manageJenkinsTask = getDriver().findElement(
                By.xpath("//a[span[text()='Manage Jenkins']]"));
        manageJenkinsTask.click();
    }


}
