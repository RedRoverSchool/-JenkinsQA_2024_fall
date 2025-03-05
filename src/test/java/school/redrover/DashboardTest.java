package school.redrover;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.testdata.TestDataProvider;
import school.redrover.page.home.HomePage;
import school.redrover.runner.BaseTest;
import school.redrover.runner.TestUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Epic("16 Dashboard")
public class DashboardTest extends BaseTest {

    private static final String DESCRIPTION_TEXT = "It's my workspace";
    private static final String NEW_TEXT = "Hello! ";
    private static final String TEXT_DESCRIPTION_BUTTON = "Add description";
    private final static String NEW_ITEM = "New Item";
    private static final String VALID_PIPELINE_SCRIPT = """
            pipeline {
                agent any
                stages {
                    stage('Checkout') {
                        steps {echo 'Step: Checkout code from repository'}
            """;
    private static final String INVALID_PIPELINE_SCRIPT = """
            error_pipeline {{{
                agent any
                stages {
                    stage('Checkout') {
                        steps {echo 'Step: Checkout code from repository'}
                    }
                 }
            }
            """;
    private final List<String> createdProjectList = new ArrayList<>();

    private void preparationCreateNotBuiltProject(String projectName) {
        new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(projectName)
                .selectPipelineAndClickOk()
                .clickSaveButton()
                .getHeader()
                .gotoHomePage();
    }

    private void preparationCreateDisableProject(String projectName) {
        new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(projectName)
                .selectPipelineAndClickOk()
                .clickToggleToDisableOrEnableProject()
                .clickSaveButton()
                .getHeader()
                .gotoHomePage();
    }

    private void preparationCreateSuccessBuiltProject(String projectName) {
        new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(projectName)
                .selectPipelineAndClickOk()
                .addScriptToPipeline(VALID_PIPELINE_SCRIPT)
                .clickSaveButton()
                .clickOnBuildNowItemOnSidePanelAndWait()
                .getHeader()
                .gotoHomePage();
    }

    private void preparationCreateFailedBuiltProject(String projectName) {
        new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(projectName)
                .selectPipelineAndClickOk()
                .addScriptToPipeline(INVALID_PIPELINE_SCRIPT)
                .clickSaveButton()
                .clickOnBuildNowItemOnSidePanelAndWait()
                .getHeader()
                .gotoHomePage();
    }

    @Test(dataProvider = "projectNameProvider", dataProviderClass = TestDataProvider.class)
    @Story("US_16.001 Sort Items")
    @Description("TC_16.001.01 Verify that items are sorted by Name in ascending order by default. Preconditions")
    public void testCreate(String projectName) {
        TestUtils.createFreestyleProject(getDriver(), projectName);
        createdProjectList.add(projectName);

        List<String> projectNameList = new HomePage(getDriver())
                .getItemList();

        Allure.step(" \uD83D\uDCCC Expected result: Projects created");
        Assert.assertListContainsObject(
                projectNameList,
                projectName,
                "Project is not created or displayed on Home page");
    }

    @Test(dependsOnMethods = "testCreate")
    @Story("US_16.001 Sort Items")
    @Description("TC_16.001.01 Verify that items are sorted by Name in ascending order by default.")
    public void testVerifyProjectOrderByNameASCByDefault() {
        List<String> projectNameList = new HomePage(getDriver())
                .getItemList();

        List<String> expectedList = createdProjectList.stream()
                .sorted(Comparator.naturalOrder())
                .toList();

        Allure.step(" \uD83D\uDCCC Expected result: Projects are sorted by name in ascending order");
        Assert.assertEquals(
                projectNameList,
                expectedList,
                "Projects are not sorted in ascending order by default.");
    }

    @Test(dependsOnMethods = "testVerifyDisplayIconDownArrowNextToNameByDefault")
    @Story("US_16.001 Sort Items")
    @Description("TC_16.001.04 Verify that items are sorted by Name in descending order ")
    public void testVerifyProjectOrderByNameDesc() {
        final List<String> expectedList = createdProjectList.stream().sorted(Comparator.reverseOrder()).toList();

        List<String> actualList = new HomePage(getDriver())
                .clickNameTableHeaderChangeOrder()
                .getItemList();

        Allure.step(" \uD83D\uDCCC Expected result: Projects are sorted by name in descending order");
        Assert.assertEquals(
                actualList,
                expectedList,
                "The project list is not sorted in descending order.");
    }

    @Test(dependsOnMethods = "testVerifyProjectOrderByNameASCByDefault")
    @Story("US_16.001 Sort Items")
    @Description("TC_16.001.05 Verify display icon down arrow next to name by default")
    public void testVerifyDisplayIconDownArrowNextToNameByDefault() {
        String titleTableHeader = new HomePage(getDriver())
                .getTitleTableHeaderWithDownArrow();

        Allure.step(" \uD83D\uDCCC Expected result: The title is 'Name' ");
        Assert.assertEquals(titleTableHeader, "Name");
    }

    @Test(dependsOnMethods = "testVerifyDisplayIconDownArrowNextToNameByDefault")
    @Story("US_16.001 Sort Items")
    @Description("TC_16.001.06 Verify display down arrow on selected column name")
    public void testDisplayDownArrowOnSelectedColumnName() {

        String titleTableHeader = new HomePage(getDriver())
                .clickStatusTableHeaderChangeOrder()
                .getTitleTableHeaderWithUpArrow();

        Allure.step(" \uD83D\uDCCC Expected result: The title of column is 'S' ");
        Assert.assertEquals(titleTableHeader, "S");
    }

    @Test
    @Story("US_16.001 Sort Items")
    @Description("TC_16.001.03 Verify that items are sorted by Status in ascending order by default")
    public void testVerifyProjectOrderByStatusASCByDefault() {
        final String successBuilt = "FPipelineProject";
        final String disabled = "APipelineProject";
        final String failedBuilt = "ZPipelineProject";
        final String notBuilt = "1PipelineProject";

        preparationCreateNotBuiltProject(notBuilt);
        preparationCreateDisableProject(disabled);
        preparationCreateSuccessBuiltProject(successBuilt);
        preparationCreateFailedBuiltProject(failedBuilt);

        List<String> projectNameList = new HomePage(getDriver())
                .clickStatusTableHeaderChangeOrder()
                .getItemList();

        Allure.step(" \uD83D\uDCCC Expected result: Projects are sorted by status in ascending order");
        Assert.assertEquals(projectNameList.size(), 4);
        Assert.assertEquals(projectNameList, List.of(notBuilt, disabled, successBuilt, failedBuilt));
    }

    @Test
    @Story("US_16.010 Log out")
    @Description("TC_16.010.01 Check the log out available")
    public void testLogOut() {
        String signInTitle = new HomePage(getDriver())
                .clickLogOut()
                .getSignInTitle();

        Allure.step(" \uD83D\uDCCC Expected result: Open sing in page");
        Assert.assertEquals(signInTitle, "Sign in to Jenkins");
    }

    @Test
    @Story("US_16.011 Dropdown menu of user")
    @Description("TC_16.011.01 Verify status ID description")
    public void testGetStatusIDDescription() {
        String adminDescription = new HomePage(getDriver())
                .openAdminDropdownMenu()
                .clickPreferencesAdminDropdownMenu()
                .clickStatusSidebar()
                .getUserIDText();

        Allure.step(" \uD83D\uDCCC Expected result: 'Jenkins User ID:' + current user ID");
        Assert.assertEquals(adminDescription, "Jenkins User ID: admin");
    }

    @Test
    @Story("US_16.011 Dropdown menu of user")
    @Description("TC_16.011.02 Check navigate credentials menu")
    public void testNavigateCredentialsMenu() {
        String pageTitleText = new HomePage(getDriver())
                .openAdminDropdownMenu()
                .clickCredentialsAdminDropdownMenu()
                .getPageTitleText();

        Allure.step(" \uD83D\uDCCC Expected result: Page title text is 'Credentials'");
        Assert.assertEquals(pageTitleText, "Credentials");
    }

    @Test
    @Story("US_16.011 Dropdown menu of user")
    @Description("TC_16.011.03 Check user field on credentials page")
    public void testAddDomainArrow() {
        String user = new HomePage(getDriver())
                .openAdminDropdownMenu()
                .clickCredentialsAdminDropdownMenu()
                .getUserName();

        Allure.step(" \uD83D\uDCCC Expected result: User name is not empty");
        Assert.assertFalse(user.isEmpty());
    }

    @Test
    @Story("US_16.011 Dropdown menu of user")
    @Description("TC_16.011.04 Check displayed domain element dropdown")
    public void testisDisplayedDomainElementDropdown() {
        boolean itemMenuDisplayed = new HomePage(getDriver())
                .openAdminDropdownMenu()
                .clickCredentialsAdminDropdownMenu()
                .clickDropdownMenu()
                .getDisplayedItemMenu();

        Allure.step(" \uD83D\uDCCC Expected result: Item menu displayed");
        Assert.assertTrue(itemMenuDisplayed);
    }

    @Test
    @Story("US_16.007 Add Description")
    @Description("TC_16.007.01 Add Description at home page")
    public void testAddDescription() {
        String textDescription = new HomePage(getDriver())
                .clickDescriptionButton()
                .addDescription(DESCRIPTION_TEXT)
                .clickSaveButton()
                .getDescriptionText();

        Allure.step(" \uD83D\uDCCC Expected result: Description added");
        Assert.assertEquals(textDescription, DESCRIPTION_TEXT);
    }

    @Test(dependsOnMethods = "testAddDescription")
    @Story("US_16.008 Edit Description")
    @Description("TC_16.008.01 Edit Description at home page")
    public void testEditDescription() {
        String newText = new HomePage(getDriver())
                .clickDescriptionButton()
                .addDescription(NEW_TEXT)
                .clickSaveButton()
                .getDescriptionText();

        Allure.step(" \uD83D\uDCCC Expected result: Description edited");
        Assert.assertEquals(newText, NEW_TEXT + DESCRIPTION_TEXT);
    }

    @Test(dependsOnMethods = "testEditDescription")
    @Story("US_16.009 Delete Description")
    @Description("TC_16.009.01 Delete Description from home page")
    public void testDeleteDescription() {
        String descriptionButton = new HomePage(getDriver())
                .clickDescriptionButton()
                .clearDescription()
                .clickSaveButton()
                .getDescriptionButtonTitle();

        Allure.step(" \uD83D\uDCCC Expected result: Descritpion button text is 'Add description'");
        Assert.assertEquals(descriptionButton, TEXT_DESCRIPTION_BUTTON);
    }

    @Test
    @Story("US_16.007 Add Description")
    @Description("TC_16.007.02 Check Preview function")
    public void testDescriptionPreviewButton() {
        String textPreview = new HomePage(getDriver())
                .clickDescriptionButton()
                .addDescription(DESCRIPTION_TEXT)
                .clickPreviewButton()
                .getTextPreview();

        Allure.step(" \uD83D\uDCCC Expected result: The description text appears in the field below the Preview button");
        Assert.assertEquals(textPreview, DESCRIPTION_TEXT);
    }

    @Test
    @Story("US_16.003 Item context Menu > Check content")
    @Description("TC_16.003.01 'New Item' button is present in the 'Dashboard' dropdown options")
    public void testPossibilityOfCreatingNewItemFromBreadcrumbBar() {
        String newItemButton = new HomePage(getDriver())
                .selectBreadcrumbBarMenu()
                .getBreadcrumbBarMenuList().get(0).getText();

        Allure.step(String.format("Expected Result: The button '%s' is present in dropdown options.", NEW_ITEM));
        Assert.assertEquals(newItemButton, NEW_ITEM);
    }
}
