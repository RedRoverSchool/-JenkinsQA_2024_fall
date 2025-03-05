package school.redrover.page.home;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.*;
import school.redrover.page.base.BasePage;
import school.redrover.page.folder.FolderConfigPage;
import school.redrover.page.folder.FolderProjectPage;
import school.redrover.page.freestyle.FreestyleConfigPage;
import school.redrover.page.freestyle.FreestyleProjectPage;
import school.redrover.page.freestyle.FreestyleRenamePage;
import school.redrover.page.manage.ManageJenkinsPage;
import school.redrover.page.multiConfiguration.MultiConfigurationProjectPage;
import school.redrover.page.multibranch.MultibranchPipelineProjectPage;
import school.redrover.page.multibranch.MultibranchPipelineRenamePage;
import school.redrover.page.node.NodesProjectPage;
import school.redrover.page.organizationFolder.OrganizationFolderProjectPage;
import school.redrover.page.pipeline.PipelineProjectPage;
import school.redrover.page.pipeline.PipelineRenamePage;
import school.redrover.page.user.UserConfigPage;
import school.redrover.runner.TestUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomePage extends BasePage<HomePage> {

    @FindBy(css = "[href$='/newJob']")
    private WebElement newJob;

    @FindBy(xpath = "//a[@href='newJob']")
    private WebElement newJobContentBlock;

    @FindBy(xpath = "//a[@href = '/manage']")
    private WebElement manageJenkinsSidebar;

    @FindBy(xpath = "//button[@data-id='ok']")
    private WebElement yesButton;

    @FindBy(xpath = "//div[@class='empty-state-block']/h1")
    private WebElement welcomeTitle;

    @FindBy(xpath = "//div[@id='description']/div[1]")
    private WebElement description;

    @FindBy(xpath = "//a[contains(@class, 'task-link-no-confirm')]")
    private List<WebElement> sideBarOptionList;

    @FindBy(className = "content-block")
    private List<WebElement> contentBlock;

    @FindBy(xpath = "//tr/td/a[@class='jenkins-table__link model-link inside']")
    private List<WebElement> projectList;

    @FindBy(xpath = "//div[@id='notification-bar']")
    private WebElement buildScheduledNotificationBar;

    @FindBy(xpath = "(//button[@class='jenkins-menu-dropdown-chevron'])[1]")
    private WebElement adminDropdown;

    @FindBy(xpath = "//a[contains(@href,'/user/admin/preferences')]")
    private WebElement preferencesAdminDropdown;

    @FindBy(xpath = "//a[contains(@href,'/user/admin/credentials')]")
    private WebElement credentialsDropdown;

    @FindBy(css = "a[href^='/logout']")
    private WebElement logOut;

    @FindBy(xpath = "//a[@href ='/view/all/builds']")
    private WebElement buildHistoryLink;

    @FindBy(xpath = "//a[contains(@class,'app-progress-bar')]")
    private WebElement progressBar;

    @FindBy(xpath = "//div[@id = 'notification-bar']")
    private WebElement notificationBar;

    @FindBy(xpath = "//a[@class='jenkins-table__link jenkins-table__badge model-link inside']")
    private WebElement numberOfRuns;

    @FindBy(xpath = "//a[@href='/newView']")
    private WebElement createNewViewButton;

    @FindBy(linkText = "My Views")
    private WebElement myViewsButton;

    @FindBy(xpath = "//div[@class='tab' and not(.//a[@tooltip='New View'])]")
    private List<WebElement> viewList;

    @FindBy(xpath = "//footer/following-sibling::dialog")
    private WebElement deletionPopup;

    @FindBy(xpath = "//a[@href='/me/my-views']")
    private WebElement myViewsLink;

    @FindBy(xpath = "//table[@id='projectstatus']/thead/tr/th/a[contains(text(), 'Name')]")
    private WebElement tableNameHeaderChangeOrder;

    @FindBy(xpath = "//table[@id='projectstatus']/thead/tr/th/a[contains(text(), 'S')]")
    private WebElement statusTableHeaderChangeOrder;

    @FindBy(xpath = "//table[@id='projectstatus']/thead/tr/th/a/span[contains(text(), '  ↓')]/..")
    private WebElement titleTableHeaderWithDownArrow;

    @FindBy(xpath = "//table[@id='projectstatus']/thead/tr/th/a/span[contains(text(), '  ↑')]/..")
    private WebElement titleTableHeaderWithUpArrow;

    @FindBy(id = "description-link")
    private WebElement descriptionButton;

    @FindBy(xpath = "//textarea[contains(@class, 'jenkins-input')]")
    private WebElement descriptionTextArea;

    @FindBy(xpath = "//button[@name='Submit']")
    private WebElement saveButton;

    @FindBy(xpath = "//textarea[@name='description']")
    private WebElement clearDescriptionTextArea;

    @FindBy(css = "[class$='textarea-show-preview']")
    private WebElement previewButton;

    @FindBy(xpath = "//div[@class='textarea-preview']")
    private WebElement previewText;

    @FindBy(xpath = "//td[text()= 'No builds in the queue.' ]")
    private WebElement buildQueueText;

    @FindBy(xpath = "//div[@class='jenkins-dropdown']//a")
    private List<WebElement> breadcrumbBarMenuList;

    @FindBy(xpath = "//div[@id='executors']/div[@class='pane-header']/a")
    private WebElement buttonExpandCollaps;

    @FindBy(xpath = "div[@id='executors']/div[@class='pane-content']/div/div/a")
    private List<WebElement> nodeList;

    @FindBy(xpath = "//div[@id='breadcrumbBar']//a/button")
    private WebElement dashboardChevron;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open '{name}' project")
    private void openItem(String name) {
        getDriver().findElement(By.xpath("//td/a/span[text() = '%s']/..".formatted(name))).click();
    }

    @Step("Click on chevron and select '{menuName}' in dropdown menu for '{itemName}' project")
    private void selectMenuFromItemDropdown(String itemName, String menuName) {
        TestUtils.moveAndClickWithJS(getDriver(), getDriver().findElement(
                By.xpath("//td/a/span[text() = '%s']/../button".formatted(itemName))));

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='jenkins-dropdown__item__icon']/parent::*[contains(., '%s')]"
                        .formatted(menuName)))).click();
    }

    @Step("Check if node list is expanded")
    private boolean isExpanded() {
        return (Objects.equals(buttonExpandCollaps.getAttribute("title"), "Expand"));
    }

    @Step("Open '{name}' freestyle project")
    public FreestyleProjectPage openFreestyleProject(String name) {
        openItem(name);
        return new FreestyleProjectPage(getDriver());
    }

    @Step("Open '{name}' Pipeline Project")
    public PipelineProjectPage openPipelineProject(String name) {
        openItem(name);
        return new PipelineProjectPage(getDriver());
    }

    @Step("Open MultiConfiguration project")
    public MultiConfigurationProjectPage openMultiConfigurationProject(String name) {
        openItem(name);
        return new MultiConfigurationProjectPage(getDriver());
    }

    @Step("Open '{name}' organization folder project")
    public OrganizationFolderProjectPage openOrganisationFolderProject(String name) {
        openItem(name);
        return new OrganizationFolderProjectPage(getDriver());
    }

    @Step("Open '{name}' multibranch pipeline project")
    public MultibranchPipelineProjectPage openMultibranchPipelineProject(String name) {
        openItem(name);
        return new MultibranchPipelineProjectPage(getDriver());
    }

    @Step("Open folder '{name}'")
    public FolderProjectPage openFolder(String name) {
        openItem(name);
        return new FolderProjectPage(getDriver());
    }

    @Step("Open Manage Jenkins Page from Sidebar on Home page")
    public ManageJenkinsPage openManageJenkinsPage() {
        manageJenkinsSidebar.click();

        return new ManageJenkinsPage(getDriver());
    }

    @Step("Click New Item")
    public CreateNewItemPage clickNewItem() {
        newJob.click();

        return new CreateNewItemPage(getDriver());
    }

    @Step("Click 'Create a job'")
    public CreateNewItemPage clickNewItemContentBlock() {
        newJobContentBlock.click();

        return new CreateNewItemPage(getDriver());
    }

    @Step("Select 'Configure' for '{itemName}' from item dropdown menu")
    public FolderConfigPage selectConfigureFromItemMenu(String itemName) {
        selectMenuFromItemDropdown(itemName, "Configure");

        return new FolderConfigPage(getDriver());
    }

    @Step("Select 'Configure' for '{itemName}' from item dropdown menu")
    public FreestyleConfigPage selectConfigureFromItemMenuForFreestyle(String itemName) {
        selectMenuFromItemDropdown(itemName, "Configure");

        return new FreestyleConfigPage(getDriver());
    }

    @Step("Select 'Delete' from item menu")
    public HomePage selectDeleteFromItemMenu(String itemName) {
        selectMenuFromItemDropdown(itemName, "Delete");

        return this;
    }

    @Step("Select 'Delete' from item menu for project '{itemName}'")
    public HomePage selectDeleteFromItemMenuAndClickYes(String itemName) {
        selectDeleteFromItemMenu(itemName);
        getWait5().until(ExpectedConditions.visibilityOf(yesButton)).click();

        return this;
    }

    @Step("Select 'New Item' from folder dropdown menu")
    public CreateNewItemPage selectNewItemFromFolderMenu(String itemName) {
        selectMenuFromItemDropdown(itemName, "New Item");

        return new CreateNewItemPage(getDriver());
    }

    @Step("Select 'Build History' in dropdown for '{itemName}'")
    public BuildHistoryPage selectBuildHistoryFromItemMenu(String itemName) {
        selectMenuFromItemDropdown(itemName, "Build History");

        return new BuildHistoryPage(getDriver());
    }

    @Step("Select 'Build Now' in dropdown for '{itemName}'")
    public HomePage selectBuildNowFromItemMenu(String itemName) {
        selectMenuFromItemDropdown(itemName, "Build Now");

        return this;
    }

    @Step("Select 'Move' in dropdown for for '{itemName}'")
    public FolderProjectPage selectMoveFromItemMenuByChevron(String itemName) {
        selectMenuFromItemDropdown(itemName, "Move");

        return new FolderProjectPage(getDriver());
    }

    @Step("Get project name from the list by order '{order}'")
    public String getItemNameByOrder(int order) {

        return getDriver().findElements(By.xpath("//td/a/span")).stream()
                .skip(order - 1)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Некорректный порядок: " + order))
                .getText();
    }

    @Step("Get welcome title")
    public String getWelcomeTitle() {
        return welcomeTitle.getText();
    }

    @Step("Get description text")
    public String getDescriptionText() {
        return getWait5().until(ExpectedConditions.visibilityOf(description)).getText();
    }

    @Step("Displaying task contents in the start page sidebar")
    public List<String> getSideContent() {
        getWait2().until(ExpectedConditions.visibilityOfAllElements(sideBarOptionList));
        return sideBarOptionList.stream()
                .map(WebElement::getText)
                .toList();
    }

    @Step("Check links side panel")
    public List<String> getSideContentAttribute() {
        getWait2().until(ExpectedConditions.visibilityOfAllElements(sideBarOptionList));
        return sideBarOptionList.stream()
                .map(el -> {
                    String href = el.getAttribute("href");
                    URI uri = URI.create(href);
                    return uri.getPath();
                })
                .toList();
    }

    @Step("Displaying the contents of the main panel of the start page")
    public List<String> getContentBlock() {
        return contentBlock.stream()
                .map(WebElement::getText)
                .toList();
    }

    @Step("Get Item list from Dashboard")
    public List<String> getItemList() {
        return projectList
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    @Step("Check disable circle sign is displayed")
    public boolean isDisableCircleSignPresent(String name) {
        try {
            return getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//tr[@id='job_%s']//*[@tooltip='Disabled']".formatted(name)))).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check green schedule Build triangle is displayed")
    public boolean isGreenScheduleBuildTrianglePresent(String name) {
        return !getDriver().findElements(
                By.xpath("//td[@class='jenkins-table__cell--tight']//a[@tooltip='Schedule a Build for %s']"
                        .formatted(name))).isEmpty();
    }

    @Step("Click green triangle to schedule a build for '{name}' project")
    public HomePage clickScheduleBuild(String name) {
        getWait10().until(ExpectedConditions.visibilityOf(getDriver().findElement(
                        By.xpath("//td[@class='jenkins-table__cell--tight']//a[@tooltip='Schedule a Build for %s']".formatted(name)))))
                .click();
        getWait10().until(ExpectedConditions.attributeContains(buildScheduledNotificationBar, "class", "--visible"));
        getWait10().until(ExpectedConditions.attributeContains(buildScheduledNotificationBar, "class", "--hidden"));

        return this;
    }

    @Step("Click 'Build History'")
    public BuildHistoryPage gotoBuildHistoryPageFromLeftPanel() {
        buildHistoryLink.click();

        return new BuildHistoryPage(getDriver());
    }

    @Step("Get Status Build tooltip text by project '{projectName}'")
    public String getStatusBuild(String projectName) {

        return getDriver().findElement(By.cssSelector("#job_%s> td:nth-of-type(1) > div > svg".formatted(projectName))).getAttribute("tooltip");
    }

    @Step("Go to Pipeline Rename page from item dropdown")
    public PipelineRenamePage goToPipelineRenamePageViaDropdown(String name) {
        selectMenuFromItemDropdown(name, "Rename");

        return new PipelineRenamePage(getDriver());
    }

    @Step("Refresh after Build")
    public HomePage refreshAfterBuild() {
        getWait10().until(ExpectedConditions.invisibilityOf(progressBar));

        getDriver().navigate().refresh();

        return this;
    }

    @Step("Get notification bar status")
    public String getNotificationBarStatus() {

        return getWait2().until(ExpectedConditions.visibilityOf(notificationBar)).getText();
    }

    @Step("Get number of runs")
    public String getNumberOfRuns() {
        return numberOfRuns.getText();
    }

    @Step("Click '+ New View' button")
    public NewViewPage clickCreateNewViewButton() {
        createNewViewButton.click();

        return new NewViewPage(getDriver());
    }

    @Step("Click 'My Views' sidebar")
    public HomePage clickMyViewsButton() {
        myViewsButton.click();

        return this;
    }

    @Step("Get list of views")
    public List<String> getViewList() {
        return getWait2().until(ExpectedConditions.visibilityOfAllElements(viewList))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    @Step("Click view by name '{name}'")
    public ViewPage clickViewByName(String name) {
        getWait2().until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/view/%s/']".formatted(name)))).click();

        return new ViewPage(getDriver());
    }

    @Step("Get a deletion pop-up")
    public WebElement getDeletionPopup() {

        return getWait5().until(ExpectedConditions.visibilityOf(deletionPopup));
    }

    @Step("Go to 'My views'")
    public MyViewsPage goToMyViews() {
        myViewsLink.click();

        return new MyViewsPage(getDriver());
    }

    @Step("Click 'Name' table header change order")
    public HomePage clickNameTableHeaderChangeOrder() {
        tableNameHeaderChangeOrder.click();

        return new HomePage(getDriver());
    }

    @Step("Click 'Status of the last build' table header change order")
    public HomePage clickStatusTableHeaderChangeOrder() {
        statusTableHeaderChangeOrder.click();

        return new HomePage(getDriver());
    }

    @Step("Get title table header with down arrow")
    public String getTitleTableHeaderWithDownArrow() {
        return titleTableHeaderWithDownArrow.getText().split(" ")[0].trim();
    }

    @Step("Get title table header with up arrow")
    public String getTitleTableHeaderWithUpArrow() {
        return titleTableHeaderWithUpArrow.getText().split(" ")[0].trim();
    }

    @Step("Click description button at home page")
    public HomePage clickDescriptionButton() {
        descriptionButton.click();
        return this;
    }

    @Step("Add description text in the text area")
    public HomePage addDescription(String description) {
        descriptionTextArea.sendKeys(description);
        return this;
    }

    @Step("Click 'Save' button")
    public HomePage clickSaveButton() {
        saveButton.click();
        return this;
    }

    @Step("Clear description text area")
    public HomePage clearDescription() {
        clearDescriptionTextArea.clear();
        return this;
    }

    @Step("Get description button title")
    public String getDescriptionButtonTitle() {
        return descriptionButton.getText();
    }

    @Step("Click 'Preview' button")
    public HomePage clickPreviewButton() {
        previewButton.click();
        return this;
    }

    @Step("Get text preview")
    public String getTextPreview() {
        return previewText.getText();
    }

    @Step("Click 'Rename' in project dropdown for '{projectName}'")
    public FreestyleRenamePage clickRenameInProjectDropdown(String projectName) {
        selectMenuFromItemDropdown(projectName, "Rename");

        return new FreestyleRenamePage(getDriver());
    }

    @Step("Check items are in alphabetical order")
    public Boolean isInAlphabeticalOrder() {
        List<String> actualOrder = getItemList();
        List<String> expectedOrder = new ArrayList<>(actualOrder);
        Collections.sort(expectedOrder);

        return actualOrder.equals(expectedOrder);
    }

    @Step("Open user dropdown menu")
    public HomePage openAdminDropdownMenu() {
        new Actions(getDriver()).moveToElement(adminDropdown).click().perform();

        return this;
    }

    @Step("Click 'Configure' at user dropdown menu")
    public UserConfigPage clickPreferencesAdminDropdownMenu() {
        getWait5().until(ExpectedConditions.elementToBeClickable(preferencesAdminDropdown)).click();

        return new UserConfigPage(getDriver());
    }

    @Step("Click Log out button")
    public SignInPage clickLogOut() {
        logOut.click();

        return new SignInPage(getDriver());
    }

    @Step("Click 'Credentials' at user dropdown menu")
    public CredentialsPage clickCredentialsAdminDropdownMenu() {
        getWait2().until(ExpectedConditions.elementToBeClickable(credentialsDropdown)).click();

        return new CredentialsPage(getDriver());
    }

    @Step("Check build queue message")
    public String getBuildQueueText() {
        return buildQueueText.getText();
    }

    @Step("Open Dashboard dropdown")
    public HomePage selectBreadcrumbBarMenu() {
        new Actions(getDriver()).moveToElement(getDriver().findElement(By.xpath("//div[@id='breadcrumbBar']//a"))).perform();

        TestUtils.moveAndClickWithJS(getDriver(), getWait10().until(ExpectedConditions.visibilityOf(dashboardChevron)));

        return this;
    }

    @Step("Get BreadcrumbBar menu list")
    public List<WebElement> getBreadcrumbBarMenuList() {
        return getWait10().until(ExpectedConditions.visibilityOfAllElements(breadcrumbBarMenuList));
    }

    @Step("Get the list of Node names from the 'Build Executors Status' block on the Home page")
    public List<String> getNodeNameList() {
        clickButtonExpand();
        return nodeList
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    @Step("Click 'Expand' button")
    public void clickButtonExpand() {
        if (isExpanded()) {
            buttonExpandCollaps.click();
        }
    }

    @Step("Open node from Build Executors Status block")
    public NodesProjectPage openNodeFromBuildExecutorStatusBlock(String nodeName) {
        clickButtonExpand();
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='executors']//a[contains(@href, '%s')]".formatted(nodeName)))).click();

        return new NodesProjectPage(getDriver());
    }

    @Step("Select 'Delete Agent' from Build dropdown for '{nodeName}'")
    public HomePage selectDeleteAgentFromBuildDropdownAndClickYes(String nodeName) {
        selectMenuFromBuildDropdown(nodeName, "Delete Agent");
        getWait5().until(ExpectedConditions.visibilityOf(yesButton)).click();
        return this;
    }

    @Step("Select '{menuName}' from Build dropdown for '{nodeName}'")
    private void selectMenuFromBuildDropdown(String nodeName, String menuName) {
        clickButtonExpand();

        TestUtils.moveAndClickWithJS(getDriver(), getDriver().findElement(
                By.xpath("//div[@id='executors']//a[contains(@href, '%s')]/button".formatted(nodeName))));

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='jenkins-dropdown__item__icon']/parent::*[contains(., '%s')]"
                        .formatted(menuName)))).click();
    }

    @Step("Select 'Rename' in project dropdown for '{itemName}'")
    public MultibranchPipelineRenamePage selectRenameFromItemDropdown(String itemName) {
        selectMenuFromItemDropdown(itemName, "Rename");

        return new MultibranchPipelineRenamePage(getDriver());
    }

}