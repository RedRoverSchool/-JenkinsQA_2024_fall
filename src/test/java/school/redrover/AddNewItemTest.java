package school.redrover;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class AddNewItemTest extends BaseTest {

    private void create() {

        getDriver().findElement(By.xpath("//*[@id='tasks']/div[1]/span/a")).click();
        getDriver().findElement(
                By.xpath("//input[@name='name']")).sendKeys(
                        "My New Projects");

        getDriver().findElement(By.cssSelector(".hudson_model_FreeStyleProject")).click();
        getDriver().findElement(By.xpath("//button[@id='ok-button']")).click();
        getDriver().findElement(By.xpath("//button[@name='Submit']")).click();
    }

    @Ignore
    @Test
    public void testInsideProject() {

        create();

        Assert.assertEquals(getDriver().findElement(By.xpath(
                        "//h1[@class='job-index-headline page-headline']")).getText(),
                "My New Projects");
    }

    @Test(dependsOnMethods = "testInsideProject")
    public void testOnDashboard() {

        getDriver().findElement(By.xpath("//*[@id='breadcrumbs']/li[1]/a")).click();

        Assert.assertEquals(getDriver().findElement(By.xpath(
                "//*[@id='job_My New Projects']/td[3]/a/span")).getText(),
                "My New Projects");
    }
}

