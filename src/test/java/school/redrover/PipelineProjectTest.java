package school.redrover;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.testdata.TestDataProvider;
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
    private final static String DISABLE_PROJECT_WARNING_MESSAGE = "This project is currently disabled";
    private final static String SAME_NAME_WARNING_MESSAGE = "The new name is the same as the current name.";
    private final static String PIPELINE_SCRIPT = """
            pipeline {agent any\n stages {
            stage('Build') {steps {echo 'Building the application'}}
            stage('Test') {steps {error 'Test stage failed due to an error'}}
            }
            """;
    private final static List<String> PIPELINE_STAGES_LIST = List.of("Start", "Build", "Test", "End");
    private final static List<String> SIDEBAR_ITEM_LIST_PROJECT_PAGE = List.of("Status", "Changes", "Build Now",
            "Configure", "Delete Pipeline", "Stages", "Rename", "Pipeline Syntax");
    private final static List<String> BUILD_PERMALINK_LIST = List.of("Last build", "Last stable build", "Last successful build", "Last completed build");


    @Test
    @Story("US_00.002 Create Pipeline Project")
    @Description("TC_00.002.01 Create Pipeline Project with valid name via sidepanel")
    public void testCreateProjectWithValidNameViaSidebar() {
        List<String> itemList = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .clickSaveButton()
                .getHeader()
                .gotoHomePage()
                .getItemList();

        Allure.step(String.format("Expected result: Project with valid name '%s' is displayed on the Home page", PROJECT_NAME));
        Assert.assertListContainsObject(itemList, PROJECT_NAME, "Project is not created");
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
                " is displayed", () -> Assert.assertEquals(emptyNameMessage, EMPTY_NAME_ERROR_MESSAGE));
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

    @Test(dataProvider = "providerUnsafeCharacters", dataProviderClass = TestDataProvider.class)
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
                .getHeader()
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
                .getHeader()
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

    @Test
    @Story("US_02.005 Delete Pipeline")
    @Description("TC_02.005.01 Delete Pipeline by sidebar on the Project page")
    public void testDeleteProjectViaSidebar() {
        TestUtils.createPipelineProject(getDriver(), NEW_PROJECT_NAME);

        List<String> projectList = new HomePage(getDriver())
                .openPipelineProject(NEW_PROJECT_NAME)
                .clickDeleteButtonSidebarAndConfirm()
                .getItemList();

        Allure.step(String.format("Expected result: The Project '%s' is not displayed in the Dashboard", NEW_PROJECT_NAME));
        Assert.assertListNotContainsObject(
                projectList, NEW_PROJECT_NAME, "Project is not deleted");
    }

    @Test
    @Story("US_02.005 Delete Pipeline")
    @Description("TC_02.005.02 Delete Pipeline via chevron dropdown menu on the Dashboard")
    public void testDeleteViaChevron() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        List<String> projectList = new HomePage(getDriver())
                .selectDeleteFromItemMenuAndClickYes(PROJECT_NAME)
                .getItemList();

        Allure.step(String.format("Expected result: The Project '%s' is not displayed in the Dashboard", PROJECT_NAME));
        Assert.assertListNotContainsObject(projectList, PROJECT_NAME, "Project is not deleted");
    }

    @Test
    @Story("US_02.005 Delete Pipeline")
    @Description("TC_02.005.03 Delete Pipeline via the chevron dropdown menu  in Breadcrumb")
    public void testDeleteByChevronBreadcrumb() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        String welcomeTitle = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .openBreadcrumbDropdown()
                .clickDeleteBreadcrumbDropdownAndConfirm()
                .getWelcomeTitle();

        Allure.step(String.format("Expected result: The Project '%s' is not displayed in the Dashboard", PROJECT_NAME));
        Assert.assertEquals(welcomeTitle, "Welcome to Jenkins!");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidNameViaSidebar")
    @Story("US_02.001 View Pipeline page")
    @Description("TC_02.001.01 Verify list of sidebar items")
    public void testVerifySidebarItemsOnProjectPage() {
        List<String> actualSidebarItemList = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .getSidebarItemList();

        Allure.step("Expected result: List of sidebar items contains " + SIDEBAR_ITEM_LIST_PROJECT_PAGE);
        Assert.assertEquals(
                actualSidebarItemList, SIDEBAR_ITEM_LIST_PROJECT_PAGE,
                "List of Sidebar items on Project page don't match expected list.");
    }

    @Test
    @Story("US_02.004 Pipeline Configuration")
    @Description("TC_02.004.02 Verify tooltip text in the options list on the Configure page")
    public void testVerifyCheckboxTooltipsContainCorrectText() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        Map<String, String> labelToTooltipTextMap = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickSidebarConfigButton()
                .getCheckboxWithTooltipTextMap();

        Allure.step("Expected result: Tooltip text in the options list is correct");
        labelToTooltipTextMap.forEach((checkbox, tooltip) ->
                Assert.assertTrue(tooltip.contains("Help for feature: " + checkbox),
                        "Tooltip for feature '" + checkbox + "' does not contain the correct text"));
    }

    @Test
    @Story("US_02.001 View Pipeline page")
    @Description("TC_02.001.02 Add Project description on the Project page")
    public void testAddDescriptionToProject() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);
        final String expectedProjectDescription = "Certain_project_description";

        String actualDescription = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .editDescription(expectedProjectDescription)
                .clickSubmitButton()
                .getDescription();

        Allure.step(String.format("Expected result: Description '%s' is displayed on the Project page", expectedProjectDescription));
        Assert.assertEquals(actualDescription, expectedProjectDescription,
                "Expected description for the project is not found");
    }

    @Test
    @Story("US_02.009 Disable/Enable Project")
    @Description("TC_02.009.01 Disable project via toggle on the Configure page")
    public void testGetWarningMessageWhenDisableProject() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        PipelineProjectPage pipelineProjectPage = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickSidebarConfigButton()
                .clickToggleToDisableOrEnableProject()
                .clickSaveButton();

        Allure.step(String.format("Expected result: Warning message '%s' is displayed on the Project page", DISABLE_PROJECT_WARNING_MESSAGE));
        Assert.assertEquals(pipelineProjectPage.getWarningDisabledMessage(),
                DISABLE_PROJECT_WARNING_MESSAGE);

        Allure.step("Expected result: Button text is 'Enable'");
        Assert.assertEquals(pipelineProjectPage.getStatusButtonText(),
                "Enable");
    }

    @Test(dependsOnMethods = "testGetWarningMessageWhenDisableProject")
    @Story("US_02.009 Disable/Enable Project")
    @Description("TC_02.009.02 Verify icon on the Dashboard for disabled project")
    public void testDisableProject() {
        HomePage homePage = new HomePage(getDriver());

        Allure.step("Expected result: Disabled icon is displayed for the project");
        Assert.assertTrue(homePage.isDisableCircleSignPresent(PROJECT_NAME));
    }

    @Test(dependsOnMethods = "testDisableProject")
    @Story("US_02.009 Disable/Enable Project")
    @Description("TC_02.009.03 Enable project via toggle on the Project page")
    public void testEnableProject() {
        boolean isGreenBuildButtonPresent = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickEnableButton()
                .getHeader()
                .gotoHomePage()
                .isGreenScheduleBuildTrianglePresent(PROJECT_NAME);

        Allure.step("Expected result: Green build button is displayed for the project");
        Assert.assertTrue(isGreenBuildButtonPresent,
                "Green build button is not displayed for the project");
    }

    @Test
    @Story("US_02.003 Build now")
    @Description("TC_02.003.05 Display the list of permalinks on the Project page after running at least one build")
    public void testGetPermalinksInformationUponSuccessfulBuild() {
        TestUtils.createPipelineProject(getDriver(), PROJECT_NAME);

        List<String> permalinkList = new HomePage(getDriver())
                .clickScheduleBuild(PROJECT_NAME)
                .openPipelineProject(PROJECT_NAME)
                .getPermalinkList();

        Allure.step("Expected result: All expected permalinks are displayed");
        Assert.assertEquals(permalinkList, BUILD_PERMALINK_LIST,
                "Not all expected permalinks are present in the actual permalinks list.");
    }

    @Test(dependsOnMethods = "testGetPermalinksInformationUponSuccessfulBuild")
    @Story("US_02.003 Build now")
    @Description("TC_02.003.06 Display tooltip Success by hovering over the icon next to the Project name")
    public void testGetSuccessTooltipDisplayedWhenHoverOverGreenMark() {
        String greenMarkTooltip = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .hoverOverBuildStatusMark()
                .getStatusMarkTooltipText();

        Allure.step("Expected result: Displayed tooltip 'Success'");
        Assert.assertEquals(greenMarkTooltip, "Success");
    }

    @Test(dependsOnMethods = "testGetSuccessTooltipDisplayedWhenHoverOverGreenMark")
    @Story("US_02.003 Build now")
    @Description("TC_02.003.07 Not display sidebar button 'Delete Build # '  if keep toggle 'Don't keep this build forever'")
    public void testKeepBuildForever() {
        boolean isDeleteOptionPresent = new HomePage(getDriver())
                .openPipelineProject(PROJECT_NAME)
                .clickBuildStatusMark()
                .clickKeepThisBuildForever()
                .isDeleteBuildOptionSidebarPresent(PROJECT_NAME);

        Allure.step("Expected result: Sidebar button 'Delete Build # ' is not displayed");
        Assert.assertFalse(isDeleteOptionPresent,
                "Delete build sidebar option is displayed, but it should not be.");
    }

    @Test
    @Story("US_02.004 Pipeline Configuration")
    @Description("TC_02.004.01 Disabled Pipeline tooltip is displayed on HomePage")
    public void testPipelineDisabledTooltipOnHomePage() {
        String tooltipValue = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .clickToggleToDisableOrEnableProject()
                .clickSaveButton()
                .getHeader()
                .gotoHomePage()
                .getStatusBuild(PROJECT_NAME);

        Allure.step("Expected result: The disabled tooltip is displayed on Home page in the table of project status");
        Assert.assertEquals(tooltipValue, "Disabled");
    }

    @Test
    @Story("US_02.003 Build now")
    @Description("TC_02.003.03 Build with valid pipeline script")
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
                .getHeader()
                .gotoHomePage()
                .selectBuildNowFromItemMenu(PROJECT_NAME)
                .refreshAfterBuild()
                .getStatusBuild(PROJECT_NAME);

        Allure.step("Expected result: The 'Status of the last build' field displays 'Success' next to Pipeline project");
        Assert.assertEquals(statusBuild, "Success");
    }

    @Test
    @Story("US_02.003 Build now")
    @Description("TC_02.003.03 Build with invalid pipeline script")
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
                .getHeader()
                .gotoHomePage()
                .selectBuildNowFromItemMenu(PROJECT_NAME)
                .refreshAfterBuild()
                .getStatusBuild(PROJECT_NAME);

        Allure.step("Expected result: The 'Status of the last build' field displays 'Failed' next to Pipeline project");
        Assert.assertEquals(statusBuild, "Failed");
    }

    @Test
    @Story("US_02.006 Stages")
    @Description("TC_02.006.01 List of recent builds is displayed on Stages page")
    public void testListOfRecentBuildsISDisplayedOnStages() {

        List<WebElement> pipelineBuilds = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectPipelineAndClickOk()
                .clickSaveButton()
                .clickOnBuildNowItemOnSidePanelAndWait()
                .clickOnStagesItemOnSidePanel()
                .getAllPipelineBuilds();

        Allure.step("Expected result: The List of recent builds is displayed");
        Assert.assertFalse(pipelineBuilds.isEmpty());
    }

    @Test
    @Story("US_02.006 Stages")
    @Description("TC_02.006.02 Pipeline stages are displayed in pipeline graph")
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

        Allure.step("Expected result: The stages are displayed in the Pipeline graph");
        Assert.assertEquals(stagesNames, PIPELINE_STAGES_LIST);
    }

    @Test
    @Story("US_02.006 Stages")
    @Description("TC_02.006.03 Pipeline status icons are displayed in pipeline graph")
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

        Allure.step("Expected result: The status icons are displayed in the Pipeline graph");
        Assert.assertTrue(icons.get(0).isDisplayed(), "Green Icon must be displayed");
        Assert.assertTrue(icons.get(1).isDisplayed(), "Red Icon must be displayed");
    }

    @Test
    @Story("US_02.006 Stages")
    @Description("TC_02.006.04 Status icons color")
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

        Allure.step("Expected result: The successful stage is colored green");
        Assert.assertEquals(icons.get(0).getCssValue("color"), "rgba(30, 166, 75, 1)");

        Allure.step("Expected result: The unsuccessful stage is colored red");
        Assert.assertEquals(icons.get(1).getCssValue("color"), "rgba(230, 0, 31, 1)");
    }

    @Test
    @Story("US_02.008 Pipeline Syntax")
    @Description("TC_02.008.02 Verify Breadcrumb text on the Pipeline Syntax page")
    public void testPipelineSyntaxPageIsPresent() {
        TestUtils.createPipelineProject(getDriver(), PIPELINE_NAME);

        String bredCrumbs = new HomePage(getDriver())
                .openPipelineProject(PIPELINE_NAME)
                .gotoPipelineSyntaxPageFromLeftPanel(PIPELINE_NAME)
                .getBreadCrumb(PIPELINE_NAME);

        Allure.step("Expected result: Pipeline Syntax is displayed in the Breadcrumb on the Pipeline Syntax page");
        Assert.assertEquals(bredCrumbs, "Pipeline Syntax");
    }

    @Test(dependsOnMethods = "testPipelineSyntaxPageIsPresent")
    @Story("US_02.008 Pipeline Syntax")
    @Description("TC_02.008.01 Selecting and applying a new script via Sample Step")
    public void testSelectScript() {
        String selectItem = new HomePage(getDriver())
                .openPipelineProject(PIPELINE_NAME)
                .gotoPipelineSyntaxPageFromLeftPanel(PIPELINE_NAME)
                .selectNewStep(SELECT_VALUE)
                .getTitleOfSelectedScript(SELECT_VALUE);

        Allure.step(String.format("Expected result: The '%s' step is displayed as selected in the dropdown 'Sample Step'", SELECT_VALUE));
        Assert.assertEquals(selectItem, EXPECTED_RESULT);
    }

    @Test(dependsOnMethods = "testSelectScript")
    @Story("US_02.008 Pipeline Syntax")
    @Description("TC_02.008.03 Generate script and insert into script block on Сonfigure page")
    public void testCopyAndPasteScript() {
        String pastedText = new HomePage(getDriver())
                .openPipelineProject(PIPELINE_NAME)
                .gotoPipelineSyntaxPageFromLeftPanel(PIPELINE_NAME)
                .selectNewStep(SELECT_VALUE)
                .clickGeneratePipelineScript()
                .clickCopy()
                .getHeader()
                .gotoHomePage()
                .openPipelineProject(PIPELINE_NAME)
                .clickSidebarConfigButton()
                .pasteScript()
                .getScriptText();

        Allure.step(String.format("Expected result: Copied script '%s' is displayed in the Script block", SELECT_VALUE));
        Assert.assertEquals(pastedText, EXPECTED_RESULT);
    }
}
