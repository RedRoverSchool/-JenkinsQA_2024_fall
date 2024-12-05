package school.redrover.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.base.BaseProjectPage;

import java.util.List;

public class FreestyleProjectPage extends BaseProjectPage<FreestyleProjectPage> {

    @FindBy(tagName = "h1")
    WebElement projectName;

    @FindBy(xpath = "//tbody//tr[2]//td//a[contains(@class, 'display-name')]")
    WebElement lastBuildNumber;

    @FindBy(tagName = "h1")
    WebElement workspaceTitle;

    @FindBy(xpath = "//tr")
    List<WebElement> listOfBuilds;

    @FindBy(tagName = "dialog")
    List<WebElement> wipeOutCurrentWorkspaceDialog;

    @FindBy(css = "dialog *")
    List<WebElement> wipeOutCurrentWorkspaceDialogOptions;

    @FindBy(xpath = "//span[text()='Rename']/..")
    WebElement renameSidebar;

    @FindBy(xpath = "//a[contains(@href, 'configure')]")
    WebElement configureSidebar;

    @FindBy(xpath = "//a[@data-build-success='Build scheduled']")
    WebElement buildNowSidebar;

    @FindBy(xpath = "//tbody//tr[2]//a")
    WebElement lastBuildSuccessBuildIcon;

    @FindBy(xpath = "//a[@data-title='Delete Project']")
    WebElement deleteProjectSidebar;

    @FindBy(xpath = "//button[@data-id='ok']")
    WebElement yesButton;

    @FindBy(xpath = "//span[text()='Workspace']/..")
    WebElement workspaceSidebar;

    @FindBy(xpath = "//a[@data-title='Wipe Out Current Workspace']")
    WebElement wipeOutCurrentWorkspaceSidebar;

    public FreestyleProjectPage(WebDriver driver) {
        super(driver);
    }

    public String getProjectName() {
        return projectName.getText();
    }

    public String getLastBuildNumber() {
        return lastBuildNumber.getText();
    }

    public String getWorkspaceTitle() {
        return workspaceTitle.getText();
    }

    public List<String> getListOfBuilds() {
        return getWait5().until(ExpectedConditions.visibilityOfAllElements(listOfBuilds))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean verifyConfirmationDialogOptionsPresence(List<String> dialogOptions) {
        getWait10().until(ExpectedConditions.visibilityOfAllElements(wipeOutCurrentWorkspaceDialog));
        List<String> confirmationDialogOptions = wipeOutCurrentWorkspaceDialogOptions.stream()
                .map(WebElement::getText)
                .toList();

        for (String option : dialogOptions) {
            if (!confirmationDialogOptions.contains(option))
                return false;
        }
        return true;
    }

    public FreestyleRenamePage clickRenameSidebar() {
        getWait10().until(ExpectedConditions.visibilityOf(renameSidebar)).click();

        return new FreestyleRenamePage(getDriver());
    }

    public FreestyleConfigPage clickConfigureSidebar() {
        getWait5().until(ExpectedConditions.visibilityOf(configureSidebar)).click();

        return new FreestyleConfigPage(getDriver());
    }

    public FreestyleProjectPage clickBuildNowSidebar() {
        getWait5().until(ExpectedConditions.visibilityOf(buildNowSidebar)).click();

        return this;
    }

    public FreestyleBuildPage clickOnSuccessBuildIconForLastBuild() {
        getWait10().until(ExpectedConditions.visibilityOf(lastBuildSuccessBuildIcon)).click();

        return new FreestyleBuildPage(getDriver());
    }

    public FreestyleProjectPage clickDeleteProjectSidebar() {
        getWait10().until(ExpectedConditions.visibilityOf(deleteProjectSidebar)).click();

        return this;
    }

    public HomePage clickYesToConfirmDelete() {
        getWait10().until(ExpectedConditions.elementToBeClickable(yesButton)).click();

        return new HomePage(getDriver());
    }

    public FreestyleProjectPage clickWorkspaceSidebar() {
        getWait10().until(ExpectedConditions.visibilityOf(workspaceSidebar)).click();

        return this;
    }

    public FreestyleProjectPage clickWipeOutCurrentWorkspaceSidebar() {
        getWait10().until(ExpectedConditions.visibilityOf(wipeOutCurrentWorkspaceSidebar)).click();

        return this;
    }

    public FreestyleProjectPage clickYesToWipeOutCurrentWorkspace() {
        getWait10().until(ExpectedConditions.elementToBeClickable(yesButton)).click();
        return this;
    }
}
