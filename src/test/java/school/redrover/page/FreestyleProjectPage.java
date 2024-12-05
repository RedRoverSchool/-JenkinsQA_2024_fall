package school.redrover.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.base.BaseProjectPage;

import java.util.List;

public class FreestyleProjectPage extends BaseProjectPage<FreestyleProjectPage> {

    public FreestyleProjectPage(WebDriver driver) {
        super(driver);
    }

    public String getProjectName() {
        return getDriver().findElement(By.tagName("h1")).getText();
    }

    public String getLastBuildNumber() {
        return getDriver().findElement(By.xpath("//tbody//tr[2]//td//a[contains(@class, 'display-name')]")).getText();
    }

    public String getWorkspaceText() {
        return getDriver().findElement(By.tagName("h1")).getText();
    }

    public List<String> getListOfBuilds() {
        return getWait5().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//tr")))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean verifyConfirmationDialogOptionsPresence(List<String> dialogOptions) {
        getWait10().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("dialog")));
        List<String> confirmationDialogOptions = getDriver().findElements(By.cssSelector("dialog *")).stream()
                .map(WebElement::getText)
                .toList();

        for (String option : dialogOptions) {
            if (!confirmationDialogOptions.contains(option))
                return false;
        }
        return true;
    }

    public FreestyleRenamePage clickRenameSidebar() {
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[text()='Rename']/.."))).click();

        return new FreestyleRenamePage(getDriver());
    }

    public FreestyleConfigPage clickConfigureSidebar() {
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@href, 'configure')]"))).click();

        return new FreestyleConfigPage(getDriver());
    }

    public FreestyleProjectPage clickBuildNowSidebar() {
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@data-build-success='Build scheduled']"))).click();

        return this;
    }

    public FreestyleBuildPage clickOnSuccessBuildIconForLastBuild() {
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody//tr[2]//a"))).click();

        return new FreestyleBuildPage(getDriver());
    }

    public FreestyleProjectPage clickDeleteProjectSidebar() {
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@data-title='Delete Project']"))).click();

        return this;
    }

    public HomePage clickYesToConfirmDelete() {
        getWait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-id='ok']"))).click();

        return new HomePage(getDriver());
    }

    public FreestyleProjectPage clickWorkspaceSidebar() {
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Workspace']/.."))).click();

        return this;
    }

    public FreestyleProjectPage clickWipeOutCurrentWorkspaceSidebar() {
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@data-title='Wipe Out Current Workspace']"))).click();

        return this;
    }

    public FreestyleProjectPage clickYesToWipeOutCurrentWorkspace() {
        getWait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-id='ok']"))).click();

        return this;
    }
}
