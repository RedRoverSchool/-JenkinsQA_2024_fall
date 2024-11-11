package school.redrover;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class OrganizationFolder2Test extends BaseTest {
    private static final String ORGANIZATION_FOLDER_NAME = "NewOrgFolder";

    @Test @Ignore
    public void testCreateOrganizationFolder() {
        getDriver().findElement(By.xpath("//a[.='New Item']")).click();
        getDriver().findElement(By.id("name")).sendKeys(ORGANIZATION_FOLDER_NAME);
        getDriver().findElement(By.xpath("//label/span[text() ='Organization Folder']")).click();
        getDriver().findElement(By.xpath("//button[@id='ok-button']")).click();
        getDriver().findElement(By.xpath("//button[@name='Submit']")).click();

        Assert.assertEquals(getDriver().findElement(By.xpath("//h1")).getText(), ORGANIZATION_FOLDER_NAME);
    }
}
