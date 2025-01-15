package school.redrover;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import school.redrover.page.home.HomePage;
import school.redrover.page.pipeline.PipelineConfigurePage;
import school.redrover.page.pipeline.PipelineProjectPage;
import school.redrover.runner.BaseTest;
import school.redrover.runner.TestUtils;

import java.util.List;
import java.util.Map;

@Epic("02 Pipeline")
public class PipelineProjectTest extends BaseTest {

    private final static String PIPELINE_NAME = "TestName";
    private final static String SELECT_VALUE = "cleanWs: Delete workspace when build is done";
    private final static String EXPECTED_RESULT = SELECT_VALUE.split(":")[0].trim();
    private final static String PROJECT_NAME = "PipelineName";
    private final static String NEW_PROJECT_NAME = "NewPipelineName";
    private final static String EMPTY_NAME_ERROR_MESSAGE = "» This field cannot be empty, please enter a valid name";
    private final static String DUPLICATE_NAME_ERROR_MESSAGE = "» A job already exists with the name ";
    private final static String SAME_NAME_WARNING_MESSAGE = "The new name is the same as the current name.";
    private final static String PIPELINE_SCRIPT = """
            pipeline {agent any\n stages {
            stage('Build') {steps {echo 'Building the application'}}
            stage('Test') {steps {error 'Test stage failed due to an error'}}
            }
            """;
    private final static List<String> PIPELINE_STAGES = List.of("Start", "Build", "Test", "End");
    private final static List<String> SIDEBAR_ITEM_LIST_PROJECT_PAGE = List.of("Status", "Changes", "Build Now",
            "Configure", "Delete Pipeline", "Stages", "Rename", "Pipeline Syntax");

    @DataProvider
    public Object[][] providerUnsafeCharacters() {

        return new Object[][]{
                {"\\"}, {"]"}, {":"}, {"#"}, {"&"}, {"?"}, {"!"}, {"@"},
                {"$"}, {"%"}, {"^"}, {"*"}, {"|"}, {"/"}, {"<"}, {">"},
                {"["}, {";"}
        };
    }

    @Test
    @Story("US_00.002 Create Pipeline Project")
    @Description("TC_00.002.01 Create Pipeline Project with valid name via sidepanel")
    public void testCreateProjectWithValidNameViaSidebar() {
        List<String> itemList = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .clickSaveButton()
                .gotoHomePage()
                .getItemList();

        Allure.step(String.format("Expected result: Project with valid name '%s' is displayed on the Home page", PROJECT_NAME));
        Assert.assertListContainsObject(
                itemList,
                PROJECT_NAME,
                "Project is not created");
    }

    @Test
    @Story("US_00.002 Create Pipeline Project")
    @Description("TC_00.002.02 Create Pipeline Project with empty name via sidepanel")
    public void testCreateWithEmptyName() {
        String emptyNameMessage = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName("")
                .selectPipeline()
                .getEmptyNameMessage();

        Allure.step("Expected result: Error message " + "'" + EMPTY_NAME_ERROR_MESSAGE + "'" +
                " is displayed", () -> {
            Assert.assertEquals(emptyNameMessage, EMPTY_NAME_ERROR_MESSAGE); });
    }

    @Test
    @Story("US_00.002 Create Pipeline Project")
    @Description("TC_00.002.04 Create Pipeline Project with duplicate name via sidepanel")
    public void testCreateWithDuplicateName() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        String errorMessage = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipeline()
                .getErrorMessage();

        Allure.step(String.format("Expected result: Error message " + "'" + DUPLICATE_NAME_ERROR_MESSAGE + "'" + "'%s' is displayed", PROJECT_NAME));
        Assert.assertEquals(errorMessage, "» A job already exists with the name ‘%s’".formatted(PROJECT_NAME));
    }

    @Test(dataProvider = "providerUnsafeCharacters")
    @Story("US_00.002 Create Pipeline Project")
    @Description("TC_00.002.03 Create Pipeline Project with unsafe characters in name via sidepanel")
    public void testCreateWithUnsafeCharactersInName(String unsafeCharacter) {
        String invalidNameMessage = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(unsafeCharacter)
                .selectPipeline()
                .getInvalidNameMessage();

        Allure.step(String.format("Expected result: Error message '» '%s' is an unsafe character' is displayed", unsafeCharacter));
        Assert.assertEquals(invalidNameMessage, "» ‘%s’ is an unsafe character".formatted(unsafeCharacter));
    }

    @Test
    @Story("US_00.004 Create new item from other existing")
    @Description("TC_00.004.04 Create Pipeline Project from existing one")
    public void testCreatePipelineFromExistingOne() {
        final String secondProjectName = "Second" + PROJECT_NAME;
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        List<String> itemNameList = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(secondProjectName)
                .enterName(PROJECT_NAME)
                .clickOkLeadingToCofigPageOfCopiedProject(new PipelineConfigurePage(getDriver()))
                .gotoHomePage()
                .getItemList();

        Allure.step(String.format("Expected result: Project '%s' is displayed on Home page", secondProjectName));
        Assert.assertListContainsObject(itemNameList, secondProjectName,"Project with name '%s' didn't create".formatted(secondProjectName));
    }

    @Test
    @Story("US_02.007 Rename")
    @Description("TC_02.007.03 Rename Pipeline via sidebar on the Project page")
    public void testRenameProjectViaSidebar() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        List<String> projectList = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickRenameSidebarButton()
                .clearInputFieldAndTypeName(NEW_PROJECT_NAME)
                .clickRenameButton()
                .gotoHomePage()
                .getItemList();

        Allure.step(String.format("Expected result: Project '%s' renamed to '%s'", PROJECT_NAME, NEW_PROJECT_NAME));
        Assert.assertListContainsObject(
                projectList, NEW_PROJECT_NAME, "Project is not renamed");
    }

    @Test()
    @Story("US_02.007 Rename")
    @Description("TC_02.007.03 Rename Pipeline via sidebar on the Project page")
    public void testRenameProjectViaSidebar2() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        PipelineProjectPage projectPage = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickRenameSidebarButton()
                .clearInputFieldAndTypeName(NEW_PROJECT_NAME)
                .clickRenameButton();

        Allure.step(String.format("Expected Result: Project page title is '%s'", NEW_PROJECT_NAME));
        Assert.assertEquals(projectPage.getTitle(), NEW_PROJECT_NAME);

        Allure.step(String.format("Expected Result: Breadcrumb displays the name of the renamed project '%s'", NEW_PROJECT_NAME));
        Assert.assertEquals(projectPage.getProjectNameBreadcrumb(), NEW_PROJECT_NAME);
    }

    @Test
    @Story("US_02.007 Rename")
    @Description("TC_02.007.01 Warning message is displayed under 'New Name' field on the project renaming page")
    public void testWarningMessageOnRenameProjectPage() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        String actualWarningMessage = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickRenameSidebarButton()
                .getWarningMessage();

        Allure.step("Expected Result: Warning message " + "'" + SAME_NAME_WARNING_MESSAGE + "'" + " is displayed");
        Assert.assertEquals(actualWarningMessage, SAME_NAME_WARNING_MESSAGE);
    }

    @Test
    @Story("US_02.007 Rename")
    @Description("TC_02.007.04 Error message when new project name matches an existing one")
    public void testErrorMessageRenameDuplicateName() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        String actualErrorMessage = new HomePage(getDriver())
                .goToPipelineRenamePageViaDropdown(PROJECT_NAME)
                .clearInputFieldAndTypeName(PROJECT_NAME)
                .clickRenameButtonLeadingToError()
                .getErrorMessage();

        Allure.step("Expected Result: Error message " + "'" + SAME_NAME_WARNING_MESSAGE + "'" + " is displayed");
        Assert.assertEquals(actualErrorMessage, SAME_NAME_WARNING_MESSAGE);
    }
    

    @Test
    @Story("US_02.007 Rename")
    @Description("TC_02.007.02 Rename Pipeline via the chevron drop-down menu on the Dashboard")
    public void testRenameByChevronDashboard() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        PipelineProjectPage projectPage = new HomePage(getDriver())
                .goToPipelineRenamePageViaDropdown(PROJECT_NAME)
                .clearInputFieldAndTypeName(NEW_PROJECT_NAME)
                .clickRenameButton();

        Allure.step(String.format("Expected Result: Project page title is '%s'", NEW_PROJECT_NAME));
        Assert.assertEquals(projectPage.getTitle(), NEW_PROJECT_NAME);

        Allure.step(String.format("Expected Result: Breadcrumb displays the name of the renamed project '%s'", NEW_PROJECT_NAME));
        Assert.assertEquals(projectPage.getProjectNameBreadcrumb(), NEW_PROJECT_NAME);
    }

    @Test(dependsOnMethods = "testRenameByChevronDashboard")
    @Story("US_02.007 Rename")
    @Description("TC_02.007.02 Rename Pipeline via the chevron drop-down menu on the Dashboard")
    public void testRenameByChevronDisplayedOnHomePageWithCorrectName() {
        boolean isDisplayed = new HomePage(getDriver())
                .getItemList()
                .contains(NEW_PROJECT_NAME);

        Allure.step("Expected result: Renamed project displayed in the list of projects on the Dashboard");
        Assert.assertTrue(isDisplayed);
    }

    @Test(dependsOnMethods = "testRenameProjectViaSidebar")
    public void testDeleteProjectViaSidebar() {
        List<String> projectList = new HomePage(getDriver())
                .openPipelineProject(NEW_PROJECT_NAME)
                .clickDeleteButtonSidebarAndConfirm()
                .getItemList();

        Assert.assertListNotContainsObject(
                projectList,
                NEW_PROJECT_NAME,
                "Project is not deleted");
    }

    @Test
    public void testDeleteViaChevron() {
        List<String> projectList = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .clickSaveButton()
                .gotoHomePage()
                .selectDeleteFromItemMenuAndClickYes(PROJECT_NAME)
                .getItemList();

        Assert.assertListNotContainsObject(projectList, PROJECT_NAME, "Project is not deleted");
    }

    @Test
    public void testDeleteByChevronBreadcrumb() {
        String welcomeTitle = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .gotoHomePage()
                .openPipelineProject(PROJECT_NAME)
                .openBreadcrumbDropdown()
                .clickDeleteBreadcrumbDropdownAndConfirm()
                .getWelcomeTitle();

        Assert.assertEquals(welcomeTitle, "Welcome to Jenkins!");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidNameViaSidebar")
    @Story("US_02.001 View Pipeline page")
    @Description("TC_02.001.01 Verify list of sidebar items")
    public void testVerifySidebarItemsOnProjectPage() {
        List<String> actualSidebarItemList = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .getSidebarItemList();

        Allure.step("List of sidebar items contains " + SIDEBAR_ITEM_LIST_PROJECT_PAGE);
        Assert.assertEquals(
                actualSidebarItemList, SIDEBAR_ITEM_LIST_PROJECT_PAGE,
                "List of Sidebar items on Project page don't match expected list.");
    }

    @Test
    public void testVerifyCheckboxTooltipsContainCorrectText() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        Map<String, String> labelToTooltipTextMap = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickSidebarConfigButton()
                .getCheckboxWithTooltipTextMap();

        labelToTooltipTextMap.forEach((checkbox, tooltip) ->
                Assert.assertTrue(
                        tooltip.contains("Help for feature: " + checkbox),
                        "Tooltip for feature '" + checkbox + "' does not contain the correct text"));
    }

    @Test(dependsOnMethods = "testVerifyCheckboxTooltipsContainCorrectText")
    public void testAddDescriptionToProject() {
        final String expectedProjectDescription = "Certain_project_description";

        String actualDescription = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .editDescription(expectedProjectDescription)
                .clickSubmitButton()
                .getDescription();

        Assert.assertEquals(
                actualDescription,
                expectedProjectDescription,
                "Expected description for the project is not found");
    }

    @Test(dependsOnMethods = "testAddDescriptionToProject")
    public void testGetWarningMessageWhenDisableProject() {
        PipelineProjectPage pipelineProjectPage = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickSidebarConfigButton()
                .clickToggleToDisableOrEnableProject()
                .clickSaveButton();

        Assert.assertEquals(
                pipelineProjectPage.getWarningDisabledMessage(),
                "This project is currently disabled");
        Assert.assertEquals(
                pipelineProjectPage.getStatusButtonText(),
                "Enable");
    }

    @Test(dependsOnMethods = "testGetWarningMessageWhenDisableProject")
    public void testDisableProject() {
        HomePage homePage = new HomePage(getDriver());

        Assert.assertTrue(
                homePage.isDisableCircleSignPresent(PROJECT_NAME));
        Assert.assertFalse(
                homePage.isGreenScheduleBuildTrianglePresent(PROJECT_NAME));
    }

    @Test(dependsOnMethods = "testDisableProject")
    public void testEnableProject() {
        boolean isGreenBuildButtonPresent = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickEnableButton()
                .gotoHomePage()
                .isGreenScheduleBuildTrianglePresent(PROJECT_NAME);

        Assert.assertTrue(
                isGreenBuildButtonPresent,
                "Green build button is not displayed for the project");
    }

    @Test
    public void testGetPermalinksInformationUponSuccessfulBuild() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        List<String> permalinkList = new HomePage(getDriver())
                .clickScheduleBuild(PROJECT_NAME)
                .openPipelineProject(PROJECT_NAME)
                .getPermalinkList();

        Assert.assertTrue(
                permalinkList.containsAll(
                        List.of(
                                "Last build",
                                "Last stable build",
                                "Last successful build",
                                "Last completed build")),
                "Not all expected permalinks are present in the actual permalinks list.");
    }

    @Test(dependsOnMethods = "testGetPermalinksInformationUponSuccessfulBuild")
    public void testGetSuccessTooltipDisplayedWhenHoverOverGreenMark() {
        String greenMarkTooltip = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .hoverOverBuildStatusMark()
                .getStatusMarkTooltipText();

        Assert.assertEquals(
                greenMarkTooltip,
                "Success");
    }

    @Test(dependsOnMethods = "testGetSuccessTooltipDisplayedWhenHoverOverGreenMark")
    public void testKeepBuildForever() {
        boolean isDeleteOptionPresent = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickBuildStatusMark()
                .clickKeepThisBuildForever()
                .isDeleteBuildOptionSidebarPresent(PROJECT_NAME);

        Assert.assertFalse(
                isDeleteOptionPresent,
                "Delete build sidebar option is displayed, but it should not be.");
    }

    @Test
    public void testPipelineDisabledTooltipOnHomePage() {
        String tooltipValue = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .clickToggleToDisableOrEnableProject()
                .clickSaveButton()
                .gotoHomePage()
                .getStatusBuild(PROJECT_NAME);

        Assert.assertEquals(tooltipValue, "Disabled");
    }

    @Test
    public void testBuildWithValidPipelineScript() {
        final String validPipelineScriptFile = """
                pipeline {
                    agent any
                    stages {
                        stage('Checkout') {
                            steps {echo 'Step: Checkout code from repository'}
                        }
                     }
                }
                """;

        String statusBuild = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .enterScriptFromFile(validPipelineScriptFile)
                .clickSaveButton()
                .gotoHomePage()
                .selectBuildNowFromItemMenu(PROJECT_NAME)
                .refreshAfterBuild()
                .getStatusBuild(PROJECT_NAME);

        Assert.assertEquals(statusBuild, "Success");
    }

    @Test
    public void testBuildWithInvalidPipelineScript() {
        final String invalidPipelineScriptFile = """
                error_pipeline {{{
                    agent any
                    stages {
                        stage('Checkout') {
                            steps {echo 'Step: Checkout code from repository'}
                        }
                     }
                }
                """;

        String statusBuild = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .enterScriptFromFile(invalidPipelineScriptFile)
                .clickSaveButton()
                .gotoHomePage()
                .selectBuildNowFromItemMenu(PROJECT_NAME)
                .refreshAfterBuild()
                .getStatusBuild(PROJECT_NAME);

        Assert.assertEquals(statusBuild, "Failed");
    }

    @Test
    public void testListOfRecentBuildsISDisplayedOnStages() {

        List<WebElement> pipelineBuilds = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .clickSaveButton()
                .clickOnBuildNowItemOnSidePanelAndWait()
                .clickOnStagesItemOnSidePanel()
                .getAllPipelineBuilds();

        Assert.assertFalse(pipelineBuilds.isEmpty());
    }

    @Test
    public void testStagesAreDisplayedInPipelineGraph() {

        List<String> stagesNames = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .addScriptToPipeline(PIPELINE_SCRIPT)
                .clickSaveButton()
                .clickOnBuildNowItemOnSidePanelAndWait()
                .clickOnStagesItemOnSidePanel()
                .getAllStagesNames();

        Assert.assertEquals(stagesNames, PIPELINE_STAGES);
    }

    @Test
    public void testStatusIconsAreDisplayedInPipelineGraph() {

        List<WebElement> icons = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .addScriptToPipeline(PIPELINE_SCRIPT)
                .clickSaveButton()
                .clickOnBuildNowItemOnSidePanelAndWait()
                .clickOnStagesItemOnSidePanel()
                .getGreenAndRedIcons();

        Assert.assertTrue(icons.get(0).isDisplayed(), "Green Icon must be displayed");
        Assert.assertTrue(icons.get(1).isDisplayed(), "Red Icon must be displayed");
    }

    @Test
    public void testStatusIconsColor() {

        List<WebElement> icons = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .addScriptToPipeline(PIPELINE_SCRIPT)
                .clickSaveButton()
                .clickOnBuildNowItemOnSidePanelAndWait()
                .clickOnStagesItemOnSidePanel()
                .getGreenAndRedIcons();

        Assert.assertEquals(icons.get(0).getCssValue("color"), "rgba(30, 166, 75, 1)");
        Assert.assertEquals(icons.get(1).getCssValue("color"), "rgba(230, 0, 31, 1)");
    }

    @Test
    public void testPipelineSyntaxPageIsPresent() {
        String bredCrumbs = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PIPELINE_NAME)
                .selectPipelineAndClickOk()
                .gotoHomePage()
                .openPipelineProject(PIPELINE_NAME)
                .gotoPipelineSyntaxPageFromLeftPanel(PIPELINE_NAME)
                .getBreadCrumb(PIPELINE_NAME);

        Assert.assertEquals(bredCrumbs, "Pipeline Syntax");
    }

    @Test(dependsOnMethods = "testPipelineSyntaxPageIsPresent")
    public void testSelectScript() {
        String selectItem = new HomePage(getDriver())
                .openPipelineProject(PIPELINE_NAME)
                .gotoPipelineSyntaxPageFromLeftPanel(PIPELINE_NAME)
                .selectNewStep(SELECT_VALUE)
                .getTitleOfSelectedScript(SELECT_VALUE);

        Assert.assertEquals(selectItem, EXPECTED_RESULT);
    }

    @Test(dependsOnMethods = "testSelectScript")
    public void testCopyAndPasteScript() {
        String pastedText = new HomePage(getDriver())
                .openPipelineProject(PIPELINE_NAME)
                .gotoPipelineSyntaxPageFromLeftPanel(PIPELINE_NAME)
                .selectNewStep(SELECT_VALUE)
                .clickGeneratePipelineScript()
                .clickCopy()
                .gotoHomePage()
                .openPipelineProject(PIPELINE_NAME)
                .clickSidebarConfigButton()
                .pasteScript()
                .getScriptText();

        Assert.assertEquals(pastedText, EXPECTED_RESULT);
    }
}
