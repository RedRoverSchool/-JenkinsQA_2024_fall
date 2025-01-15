package school.redrover.page.home;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.ErrorPage;
import school.redrover.page.base.BasePage;
import school.redrover.page.folder.FolderConfigPage;
import school.redrover.page.freestyle.FreestyleConfigPage;
import school.redrover.page.multiConfiguration.MultiConfigurationConfigPage;
import school.redrover.page.multibranch.MultibranchPipelineConfigPage;
import school.redrover.page.organizationFolder.OrganizationFolderConfigPage;
import school.redrover.page.pipeline.PipelineConfigurePage;
import school.redrover.runner.TestUtils;

import java.util.List;

public class CreateNewItemPage extends BasePage {

    @FindBy(xpath = "//span[text()= 'Multibranch Pipeline']")
    private WebElement multibranchPipeline;

    @FindBy(xpath = "//span[text()='Folder']")
    private WebElement folder;

    @FindBy(xpath = "//span[text()='Multi-configuration project']")
    private WebElement multiConfigurationProject;

    @FindBy(xpath = "//span[text()='Freestyle project']")
    private WebElement freestyleProject;

    @FindBy(xpath = "//li[@class='org_jenkinsci_plugins_workflow_job_WorkflowJob']")
    private WebElement pipeline;

    @FindBy(id = "itemname-required")
    private WebElement emptyNameMessage;

    @FindBy(xpath = "//div[@class='add-item-name']/div[@class='input-validation-message']")
    private WebElement invalidOrSameNameMessage;

    @FindBy(id = "from")
    private WebElement copyFromField;

    @FindBy(xpath = "//li[contains(@class,'jenkins_branch_OrganizationFolder')]")
    private WebElement organizationFolder;

    @FindBy(id = "name")
    private WebElement nameField;

    @FindBy(id = "ok-button")
    private WebElement okButton;

    @FindBy(id = "add-item-panel")
    private WebElement pageField;

    @FindBy(id = "itemname-required")
    private WebElement warningMessage;

    @FindBy(xpath = "//div[@id='items']//li//label/span")
    private List<WebElement> itemsTypesList;

    public CreateNewItemPage(WebDriver driver) {
        super(driver);
    }

    @Step("Type '{name}' to name input field")
    public CreateNewItemPage enterItemName(String name) {
        nameField.sendKeys(name);

        return this;
    }

    public CreateNewItemPage selectFolderType() {
        folder.click();

        return this;
    }

    @Step("Select 'Folder' and click 'Ok' button")
    public FolderConfigPage selectFolderAndClickOk() {
        selectFolderType();
        okButton.click();

        return new FolderConfigPage(getDriver());
    }

    public FolderConfigPage nameAndSelectFolderType(String itemName) {
        enterItemName(itemName);
        selectFolderAndClickOk();

        return new FolderConfigPage(getDriver());
    }

    public MultiConfigurationConfigPage selectMultiConfigurationAndClickOk() {
        multiConfigurationProject.click();
        okButton.click();

        return new MultiConfigurationConfigPage(getDriver());
    }

    public CreateNewItemPage selectMultiConfigurationProject() {
        multiConfigurationProject.click();

        return new CreateNewItemPage(getDriver());
    }

    @Step("Select 'Freestyle project' and click 'Ok' button")
    public FreestyleConfigPage selectFreestyleProjectAndClickOk() {
        freestyleProject.click();
        okButton.click();

        return new FreestyleConfigPage(getDriver());
    }

    public FreestyleConfigPage nameAndSelectFreestyleProject(String itemName) {
        enterItemName(itemName);
        selectFreestyleProjectAndClickOk();

        return new FreestyleConfigPage(getDriver());
    }

    public MultibranchPipelineConfigPage selectMultibranchPipelineAndClickOk() {
        TestUtils.scrollToBottomWithJS(getDriver());

        multibranchPipeline.click();
        okButton.click();

        return new MultibranchPipelineConfigPage(getDriver());
    }

    public CreateNewItemPage selectMultibranchPipeline() {
        multibranchPipeline.click();

        return this;
    }

    @Step("Select 'Pipeline' and click 'Ok' button")
    public PipelineConfigurePage selectPipelineAndClickOk() {
        pipeline.click();
        okButton.click();

        return new PipelineConfigurePage(getDriver());
    }

    public OrganizationFolderConfigPage selectOrganizationFolderAndClickOk() {
        TestUtils.scrollToBottomWithJS(getDriver());
        organizationFolder.click();
        okButton.click();

        return new OrganizationFolderConfigPage(getDriver());
    }

    public String getInvalidNameMessage() {
        return invalidOrSameNameMessage.getText();
    }

    @Step("Get Error Message")
    public String getEmptyNameMessage() {
        return emptyNameMessage.getText();
    }

    @Step("Select 'Pipeline'")
    public CreateNewItemPage selectPipeline() {
        getWait10().until(ExpectedConditions.elementToBeClickable(pipeline)).click();

        return new CreateNewItemPage(getDriver());
    }

    @Step("Select 'Freestyle project'")
    public CreateNewItemPage selectFreestyleProject() {
        freestyleProject.click();

        return this;
    }

    public CreateNewItemPage selectMultibranchPipelineProject() {
        multibranchPipeline.click();

        return this;
    }
    @Step("Get Error Message")
    public String getErrorMessage() {
        return getWait5().until(ExpectedConditions.visibilityOf(invalidOrSameNameMessage)).getText();
    }

    @Step("Type '{name}' to 'Copy from' input field")
    public CreateNewItemPage enterName(String name) {
        TestUtils.scrollToElementWithJS(getDriver(), copyFromField);
        copyFromField.sendKeys(name);

        return this;
    }

    @Step("Click 'Ok' button")
    public <T> T clickOkLeadingToCofigPageOfCopiedProject(T page) {
        okButton.click();

        return page;
    }

    public ErrorPage clickOkButtonLeadingToErrorPage() {
        okButton.click();

        return new ErrorPage(getDriver());
    }

    public CreateNewItemPage clickSomewhere() {
        pageField.click();

        return this;
    }

    public String getWarningMessageText() {
        return warningMessage.getText();
    }

    public boolean getOkButton() {
        return okButton.isEnabled();
    }

    public List<WebElement> getTextList() {

        return itemsTypesList;
    }

    public List<String> getItemList() {
        return itemsTypesList.stream().map(WebElement::getText).toList();
    }
}
