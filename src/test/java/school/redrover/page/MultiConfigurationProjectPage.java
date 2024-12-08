package school.redrover.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.base.BaseProjectPage;

public class MultiConfigurationProjectPage extends BaseProjectPage<MultiConfigurationProjectPage, MultiConfigurationConfigPage, MultiConfigurationRenamePage> {

    public MultiConfigurationProjectPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public MultiConfigurationConfigPage createProjectConfigPage() {
        return new MultiConfigurationConfigPage(getDriver());
    }

    @Override
    public MultiConfigurationRenamePage createProjectRenamePage() {
        return new MultiConfigurationRenamePage(getDriver());
    }

    public MultiConfigurationProjectPage clickDeleteProject() {
        getDriver().findElement(
                By.xpath("//div[@id='side-panel']//span[text()='Delete Multi-configuration project']")).click();

        return this;
    }

    public WebElement getDeletionPopup() {
        return getWait5().until(ExpectedConditions.visibilityOf(getDriver().findElement(
                By.xpath("//footer/following-sibling::dialog"))));
    }

    public HomePage clickDeleteOnSidebarAndConfirmDeletion() {
        getDriver().findElement(By.xpath("//a[contains(@data-title,'Delete')]")).click();
        getDriver().findElement(By.xpath("//button[@data-id='ok']")).click();

        return new HomePage(getDriver());
    }
}
