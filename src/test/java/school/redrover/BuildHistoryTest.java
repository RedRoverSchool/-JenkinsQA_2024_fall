package school.redrover;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.page.home.BuildHistoryPage;
import school.redrover.page.home.HomePage;
import school.redrover.runner.BaseTest;

@Epic("08 Build History")
@Story("US_08.005 Build History")
public class BuildHistoryTest extends BaseTest {

    private static final String FREESTYLE_PROJECT_NAME = "FreestyleProject";
    private static final String PIPELINE_PROJECT_NAME = "PipelineProject";

    @Test
    @Epic("00 New Item")
    @Story("US_00.001 Create Freestyle Project")
    @Description("TC_00.001.03 Create Freestyle Project")
    public void testCreateFreestyleProject() {
        String newProject = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(FREESTYLE_PROJECT_NAME)
                .selectFreestyleProjectAndClickOk()
                .clickSaveButton()
                .getHeader()
                .gotoHomePage()
                .getItemNameByOrder(1);

        Allure.step(" \uD83D\uDCCC Expected result: Created project '%s' is displayed on the Home page".formatted(FREESTYLE_PROJECT_NAME));
        Assert.assertEquals(newProject, FREESTYLE_PROJECT_NAME);
    }

    @Test(dependsOnMethods = "testCreateFreestyleProject")
    @Story("US_08.005 Build History")
    @Description("TC_08.005.01 Displaying successful build")
    public void testDisplaySuccessBuild() {

        BuildHistoryPage buildHistoryPage = new HomePage(getDriver())
                .clickScheduleBuild(FREESTYLE_PROJECT_NAME)
                .gotoBuildHistoryPageFromLeftPanel();

        Allure.step(" \uD83D\uDCCC Expected result: Check that the successful build is displayed in build history with correct details.", () -> {
            Assert.assertEquals(buildHistoryPage.getProjectName(), FREESTYLE_PROJECT_NAME);
            Assert.assertEquals(buildHistoryPage.getBuildDisplayName(), "#1");
            Assert.assertEquals(buildHistoryPage.getListOfStatuses().get(0), "stable");
            Assert.assertEquals(buildHistoryPage.getBuildStatusSignColor(), "blue");
        });
    }

    @Test(dependsOnMethods = "testDisplaySuccessBuild")
    @Description("TC_08.005.06 Check console output of successful build")
    public void testSuccessfulBuildConsoleOutputCheck() {
        String result = new HomePage(getDriver())
                .gotoBuildHistoryPageFromLeftPanel()
                .clickConsoleOutput()
                .getFinishResult();

        Allure.step(" \uD83D\uDCCC Expected result: Build result is displayed as 'SUCCESS'.");
        Assert.assertEquals(result, "SUCCESS");
    }

    @Test
    @Epic("00 New Item")
    @Description("TC_00.002.01 Create Pipeline Project with valid name via sidepanel")
    public void testCreatePipelineProjectWithoutSaveButton() {
        String pipelineProject = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PIPELINE_PROJECT_NAME)
                .selectPipelineAndClickOk()
                .getHeader()
                .gotoHomePage()
                .getItemNameByOrder(1);

        Allure.step(" \uD83D\uDCCC Expected result: Created project '%s' is displayed on the Home page".formatted(PIPELINE_PROJECT_NAME));
        Assert.assertEquals(pipelineProject, PIPELINE_PROJECT_NAME);
    }

    @Test(dependsOnMethods = "testCreatePipelineProjectWithoutSaveButton")
    @Description("TC_08.005.02 Displaying first failed build with status 'broken since this build'")
    public void testDisplayFirstFailedBuild() {

        BuildHistoryPage buildHistoryPage = new HomePage(getDriver())
                .openPipelineProject(PIPELINE_PROJECT_NAME)
                .clickOnBuildNowItemOnSidePanelAndWait()
                .getHeader()
                .gotoHomePage()
                .gotoBuildHistoryPageFromLeftPanel();

        Allure.step(" \uD83D\uDCCC Expected result: Verify the first failed build is displayed with correct details.", () -> {
            Assert.assertEquals(buildHistoryPage.getProjectName(), PIPELINE_PROJECT_NAME);
            Assert.assertEquals(buildHistoryPage.getBuildDisplayName(), "#1");
            Assert.assertEquals(buildHistoryPage.getListOfStatuses().get(0), "broken since this build");
            Assert.assertEquals(buildHistoryPage.getCssValueOfStatusByIndex(0, "color"), "rgba(230, 0, 31, 1)");
            Assert.assertEquals(buildHistoryPage.getBuildStatusSignColor(), "red");
        });
    }

    @Test(dependsOnMethods = "testDisplayFirstFailedBuild")
    @Description("TC_08.005.07 Check console output of failed build")
    public void testFailedBuildConsoleOutputCheck() {
        String result = new HomePage(getDriver())
                .gotoBuildHistoryPageFromLeftPanel()
                .clickConsoleOutput()
                .getFinishResult();

        Allure.step(" \uD83D\uDCCC Expected result: Build result is displayed as 'FAILURE'.");
        Assert.assertEquals(result, "FAILURE");
    }

    @Test(dependsOnMethods = "testFailedBuildConsoleOutputCheck")
    @Description("TC_08.005.03 Displaying next failed build with status 'broken for a long time'")
    public void testDisplayNextFailedBuild() {
        BuildHistoryPage buildHistoryPage = new HomePage(getDriver())
                .clickScheduleBuild(PIPELINE_PROJECT_NAME)
                .gotoBuildHistoryPageFromLeftPanel();

        Allure.step(" \uD83D\uDCCC Expected result: Verify the next failed build is displayed with correct details.", () -> {
            Assert.assertEquals(buildHistoryPage.getProjectName(), PIPELINE_PROJECT_NAME);
            Assert.assertEquals(buildHistoryPage.getBuildDisplayName(), "#2");
            Assert.assertEquals(buildHistoryPage.getListOfStatuses().get(0), "broken for a long time");
            Assert.assertEquals(buildHistoryPage.getCssValueOfStatusByIndex(0, "color"), "rgba(20, 20, 31, 1)");
            Assert.assertEquals(buildHistoryPage.getBuildStatusSignColor(), "red");
        });
    }

    @Test(dependsOnMethods = "testDisplayNextFailedBuild")
    @Description("TC_08.005.04 Displaying first successful build after failed with status 'back to normal'")
    public void testDisplayFirstSuccessfulBuildAfterFailed() {
        BuildHistoryPage buildHistoryPage = new HomePage(getDriver())
                .openPipelineProject(PIPELINE_PROJECT_NAME)
                .clickSidebarConfigButton()
                .clickSaveButton()
                .getHeader()
                .gotoHomePage()
                .clickScheduleBuild(PIPELINE_PROJECT_NAME)
                .gotoBuildHistoryPageFromLeftPanel();

        Allure.step(" \uD83D\uDCCC Expected result: Verify the first successful build is displayed with correct details.", () -> {
            Assert.assertEquals(buildHistoryPage.getProjectName(), PIPELINE_PROJECT_NAME);
            Assert.assertEquals(buildHistoryPage.getBuildDisplayName(), "#3");
            Assert.assertEquals(buildHistoryPage.getListOfStatuses().get(0), "back to normal");
            Assert.assertEquals(buildHistoryPage.getBuildStatusSignColor(), "blue");
        });
    }

    @Test(dependsOnMethods = "testDisplayFirstSuccessfulBuildAfterFailed")
    @Description("TC_08.005.05 Displaying next successful build after successful with status 'stable'")
    public void testDisplayNextSuccessfulBuildAfterSuccessful() {
        BuildHistoryPage buildHistoryPage = new HomePage(getDriver())
                .clickScheduleBuild(PIPELINE_PROJECT_NAME)
                .gotoBuildHistoryPageFromLeftPanel();

        Allure.step(" \uD83D\uDCCC Expected result: Verify the next successful build is displayed with correct details.", () -> {
            Assert.assertEquals(buildHistoryPage.getProjectName(), PIPELINE_PROJECT_NAME);
            Assert.assertEquals(buildHistoryPage.getBuildDisplayName(), "#4");
            Assert.assertEquals(buildHistoryPage.getListOfStatuses().get(0), "stable");
            Assert.assertEquals(buildHistoryPage.getBuildStatusSignColor(), "blue");
        });
    }
}
