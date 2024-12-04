package school.redrover;

import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.page.FreestyleProjectPage;
import school.redrover.page.HomePage;
import school.redrover.runner.BaseTest;

import java.util.List;

public class FreestyleProjectTest extends BaseTest {

    private static final String PROJECT_NAME = "MyFreestyleProject";
    private static final String FREESTYLE_PROJECT = "Freestyle project";
    private static final String DESCRIPTION = "Bla-bla-bla project";
    private static final String TYPE_FREESTYLE_PROJECT = "Freestyle project";
    private static final String BUILD_NAME = "BuildName";

    @Test
    public void testCreateFreestyleProjectWithEmptyName() {
        String emptyNameMessage = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName("")
                .selectTypeProject(TYPE_FREESTYLE_PROJECT)
                .getEmptyNameMessage();

        Assert.assertEquals(emptyNameMessage, "» This field cannot be empty, please enter a valid name");
    }

    @Test
    public void testCreateFreestyleProjectWithDuplicateName() {
        String errorMessage = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectTypeProject(TYPE_FREESTYLE_PROJECT)
                .clickOkButton()
                .gotoHomePage()
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectTypeProject(TYPE_FREESTYLE_PROJECT)
                .getErrorMessage();

        Assert.assertEquals(errorMessage, "» A job already exists with the name ‘%s’".formatted(PROJECT_NAME));
    }

    @Test
    public void testCreateProjectViaCreateJobButton() {
        String actualProjectName = new HomePage(getDriver())
                .clickCreateJob()
                .enterItemName(PROJECT_NAME)
                .selectFreestyleProjectAndClickOk()
                .clickSaveButton()
                .getProjectName();

        Assert.assertEquals(actualProjectName, PROJECT_NAME);
    }

    @Test
    public void testCreateProjectViaSidebarMenu() {
        String actualProjectName = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(PROJECT_NAME)
                .selectFreestyleProjectAndClickOk()
                .clickSaveButton()
                .getProjectName();

        Assert.assertEquals(actualProjectName, PROJECT_NAME);
    }

    @Test
    public void testCreateFreestyleProjectFromMyViews() {
        List<String> projectName = new HomePage(getDriver())
                .clickMyViewsButton()
                .clickCreateJob()
                .enterItemName(PROJECT_NAME)
                .selectTypeProject(FREESTYLE_PROJECT)
                .clickOkButton()
                .gotoHomePage()
                .getItemList();

        Assert.assertEquals(projectName.size(), 1);
        Assert.assertEquals(projectName.get(0), PROJECT_NAME);
    }

   @Test(dependsOnMethods = "testCreateProjectViaCreateJobButton")
    public void testEditDescriptionOnProjectPage() {
        final String newDescription = "New " + DESCRIPTION;

        String actualDescription = new HomePage(getDriver())
                .openFreestyleProject(PROJECT_NAME)
                .clearDescription()
                .editDescription(newDescription)
                .getDescription();

        Assert.assertEquals(actualDescription, newDescription);
    }

    @Test(dependsOnMethods = "testCreateProjectViaSidebarMenu")
    public void testRenameProjectViaSidebarMenu() {
        final String newName = "New " + PROJECT_NAME;

        String actualProjectName = new HomePage(getDriver())
                .openFreestyleProject(PROJECT_NAME)
                .clickRenameOnSidebar()
                .clearOldAndInputNewProjectName(newName)
                .clickRenameButton()
                .getProjectName();

        Assert.assertEquals(actualProjectName, newName);
    }

    @Test
    public void testDeleteProjectViaChevron() {
        String pageTitle = new HomePage(getDriver())
                .createFreestyleProject(PROJECT_NAME)
                .selectDeleteFromItemMenu(PROJECT_NAME)
                .clickYesForConfirmDelete()
                .getWelcomeTitle();

        Assert.assertEquals(pageTitle, "Welcome to Jenkins!");
    }

    @Test
    public void testCheckSidebarMenuItemsOnProjectPage() {
        final List<String> templateSidebarMenu = List.of(
                "Status", "Changes", "Workspace", "Build Now", "Configure", "Delete Project", "Rename");

        List<String> actualSidebarMenu = new HomePage(getDriver())
                .createFreestyleProject(PROJECT_NAME)
                .openFreestyleProject(PROJECT_NAME)
                .getSidebarOptionList();

        Assert.assertEquals(actualSidebarMenu, templateSidebarMenu);
    }

    @Test
    public void testConfigureProjectAddBuildStepsExecuteShellCommand() {
        final String testCommand = "echo \"TEST! Hello Jenkins!\"";

        String extractedText = new HomePage(getDriver())
                .createFreestyleProject(PROJECT_NAME)
                .openFreestyleProject(PROJECT_NAME)
                .clickConfigureOnSidebar()
                .clickAddBuildStep()
                .selectExecuteShellBuildStep()
                .addExecuteShellCommand(testCommand)
                .clickSaveButton()
                .clickConfigureOnSidebar()
                .getTextExecuteShellTextArea();

        Assert.assertEquals(extractedText, testCommand);
    }

    @Test
    public void testBuildProjectViaSidebarMenuOnProjectPage() {
        String buildInfo = new HomePage(getDriver())
                .createFreestyleProject(PROJECT_NAME)
                .openFreestyleProject(PROJECT_NAME)
                .clickBuildNowOnSidebar()
                .clickOnSuccessBuildIconForLastBuild()
                .getConsoleOutputText();

        Assert.assertTrue(buildInfo.contains("Finished: SUCCESS"));
    }

    @Test(dependsOnMethods = "testBuildProjectViaSidebarMenuOnProjectPage")
    public void testAddBuildDisplayName() {
        String actualBuildName = new HomePage(getDriver())
                .openFreestyleProject(PROJECT_NAME)
                .clickOnSuccessBuildIconForLastBuild()
                .clickEditBuildInformationSidebar()
                .addDisplayName(BUILD_NAME)
                .clickSaveButton()
                .getStatusTitle();

        Assert.assertTrue(actualBuildName.contains(BUILD_NAME), "Title doesn't contain build name");
    }

    @Test(dependsOnMethods = {"testBuildProjectViaSidebarMenuOnProjectPage", "testAddBuildDisplayName"})
    public void testEditBuildDisplayName() {
        final String newDisplayName = "New " + BUILD_NAME;

        String actualBuildName = new HomePage(getDriver())
                .openFreestyleProject(PROJECT_NAME)
                .clickOnSuccessBuildIconForLastBuild()
                .clickEditBuildInformationSidebar()
                .editDisplayName(newDisplayName)
                .clickSaveButton()
                .getStatusTitle();

        Assert.assertTrue(actualBuildName.contains(newDisplayName));
    }

    @Test(dependsOnMethods = "testBuildProjectViaSidebarMenuOnProjectPage")
    public void testAddBuildDescription() {
        String actualDescription = new HomePage(getDriver())
                .openFreestyleProject(PROJECT_NAME)
                .clickOnSuccessBuildIconForLastBuild()
                .clickEditBuildInformationSidebar()
                .addBuildDescription(DESCRIPTION)
                .clickSaveButton()
                .getBuildDescription();

        Assert.assertEquals(actualDescription, DESCRIPTION);
    }

    @Test(dependsOnMethods = {"testBuildProjectViaSidebarMenuOnProjectPage", "testAddBuildDescription"})
    public void testEditBuildDescription() {
        final String newDescription = "New " + DESCRIPTION;

        String actualDescription = new HomePage(getDriver())
                .openFreestyleProject(PROJECT_NAME)
                .clickOnSuccessBuildIconForLastBuild()
                .clickEditBuildInformationSidebar()
                .editBuildDescription(newDescription)
                .clickSaveButton()
                .getBuildDescription();

        Assert.assertEquals(actualDescription, newDescription);
    }

    @Test
    public void testDeleteLastBuild() {
        FreestyleProjectPage freestyleProjectPage = new HomePage(getDriver())
                .createFreestyleProject(PROJECT_NAME)
                .openFreestyleProject(PROJECT_NAME)
                .clickBuildNowOnSidebar();
        String lastBuildNumber = freestyleProjectPage.getLastBuildNumber();

                freestyleProjectPage
                .clickOnSuccessBuildIconForLastBuild()
                .clickDeleteBuildSidebar()
                .confirmDeleteBuild();

        Assert.assertListNotContainsObject(freestyleProjectPage.getListOfBuilds(), lastBuildNumber, "The last build wasn't deleted");
    }
}