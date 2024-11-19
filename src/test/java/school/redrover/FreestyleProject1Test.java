package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;
import school.redrover.runner.TestUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreestyleProject1Test extends BaseTest {
    private static final String NEW_FREESTYLE_PROJECT_NAME = "New freestyle project";
    private static final String RENAMED_FREESTYLE_PROJECT_NAME = "Renamed freestyle project";
    private static final String DESCRIPTION = "Some description";


    private void createFreestyleProject(String name) {
        getDriver().findElement(By.xpath("//*[@href='/view/all/newJob']")).click();
        getDriver().findElement(By.className("hudson_model_FreeStyleProject")).click();
        getDriver().findElement(By.name("name")).sendKeys(name);
        getDriver().findElement(By.id("ok-button")).click();
        getDriver().findElement(By.name("Submit")).click();

    }
    private void createFreestyleProjectWithDescription() {
        createFreestyleProject(NEW_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        Actions actions = new Actions(getDriver());
        WebElement element = getDriver()
                .findElement(By.xpath("//span[contains(text(),'" + NEW_FREESTYLE_PROJECT_NAME + "')]"));

        actions.moveToElement(element).click().perform();

        getDriver().findElement(By.id("description-link")).click();
        getDriver().findElement(By.name("description")).sendKeys(DESCRIPTION);
        getDriver().findElement(By.name("Submit")).click();

    }
    @Test
    public void testCreateFreestyleProject() {
        createFreestyleProject(NEW_FREESTYLE_PROJECT_NAME);

        getDriver().findElement(By.id("jenkins-name-icon")).click();
        WebElement element = getDriver()
                .findElement(By.xpath("//span[contains(text(),'" + NEW_FREESTYLE_PROJECT_NAME + "')]"));
        Assert.assertTrue(element.isDisplayed());
    }

    @Test
    public void testDeleteFreestyleProject() {
        createFreestyleProject(NEW_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        Actions actions = new Actions(getDriver());
        WebElement element = getDriver()
                .findElement(By.xpath("//span[contains(text(),'" + NEW_FREESTYLE_PROJECT_NAME + "')]"));

        actions.moveToElement(element).click().perform();

        getDriver().findElement((By.xpath("//*[@data-title='Delete Project']"))).click();
        getDriver().findElement(By.xpath("//button[@data-id='ok']")).click();
        String emptyDashboardHeader = getDriver().findElement(By.cssSelector(".empty-state-block > h1")).getText();

        Assert.assertEquals(emptyDashboardHeader, "Welcome to Jenkins!");
    }

    @Test
    public void testRenameFreestyleProject() throws InterruptedException {
        createFreestyleProject(NEW_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        Actions actions = new Actions(getDriver());
        WebElement element = getDriver()
                .findElement(By.xpath("//span[contains(text(),'" + NEW_FREESTYLE_PROJECT_NAME + "')]"));

        actions.moveToElement(element).click().perform();

        getDriver().findElement((By.xpath("//*[contains(@href,'confirm-rename')]"))).click();
        getDriver().findElement(By.xpath("//input[@name='newName']")).clear();
        Thread.sleep(200);

        getDriver()
                .findElement(By.xpath("//input[@name='newName']"))
                .sendKeys(RENAMED_FREESTYLE_PROJECT_NAME);

        getDriver().findElement(By.name("Submit")).click();

        String projectName = getDriver()
                .findElement(By.xpath("//*[@class='job-index-headline page-headline']")).getText();

        Assert.assertEquals(projectName, RENAMED_FREESTYLE_PROJECT_NAME);
    }

    @Test
    public void testAddFreestyleProjectDescription() {
        createFreestyleProjectWithDescription();

        String description = getDriver().findElement(By.id("description")).getText();

        Assert.assertEquals(description, DESCRIPTION);
    }

    @Test
    public void testDeleteFreestyleProjectDescription() {
        createFreestyleProjectWithDescription();

        getDriver().findElement(By.id("description-link")).click();
        getDriver().findElement(By.name("description")).clear();
        getDriver().findElement(By.name("Submit")).click();

        String description = getDriver().findElement(By.id("description")).getText();

        Assert.assertEquals(description, "");
    }

    @Test
    public void testFreestyleProjectDescriptionPreview() {
        createFreestyleProject(NEW_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        Actions actions = new Actions(getDriver());
        WebElement element = getDriver()
                .findElement(By.xpath("//span[contains(text(),'" + NEW_FREESTYLE_PROJECT_NAME + "')]"));

        actions.moveToElement(element).click().perform();

        getDriver().findElement(By.id("description-link")).click();
        getDriver().findElement(By.name("description")).sendKeys(DESCRIPTION);
        getDriver().findElement(By.className("textarea-show-preview")).click();

        String preview = getDriver().findElement(By.className("textarea-preview")).getText();

        Assert.assertEquals(preview, DESCRIPTION);
    }

    @Test
    public void testChevronDeleteFreestyleProject() {
        createFreestyleProject(NEW_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        Actions actions = new Actions(getDriver());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        WebElement projectName = getDriver()
                .findElement(By.xpath("//span[contains(text(),'" + NEW_FREESTYLE_PROJECT_NAME + "')]"));
        actions
                .moveToElement(projectName)
                .perform();

        WebElement chevron = getDriver()
                .findElement(By.xpath("//*[@id='job_" + NEW_FREESTYLE_PROJECT_NAME + "']/td[3]/a/button"));

        TestUtils.moveAndClickWithJavaScript(getDriver(), chevron);

        wait.until(ExpectedConditions.attributeToBe(chevron, "aria-expanded", "true"));

        WebElement delete = wait.until(ExpectedConditions.visibilityOfElementLocated((
                By.xpath("//*[contains(@href,'doDelete')]"))));
        delete.click();

        getDriver().findElement(By.xpath("//button[contains(text(),'Yes')]")).click();
        String emptyDashboardHeader = getDriver().findElement(By.cssSelector(".empty-state-block > h1")).getText();

        Assert.assertEquals(emptyDashboardHeader, "Welcome to Jenkins!");
    }

    @Test
    public void testChevronRenameFreestyleProject() throws InterruptedException {
        createFreestyleProject(NEW_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        Actions actions = new Actions(getDriver());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        WebElement projectName = getDriver()
                .findElement(By.xpath("//span[contains(text(),'" + NEW_FREESTYLE_PROJECT_NAME + "')]"));
        actions
                .moveToElement(projectName)
                .perform();

        WebElement chevron = getDriver()
                .findElement(By.xpath("//*[@id='job_" + NEW_FREESTYLE_PROJECT_NAME + "']/td[3]/a/button"));

        TestUtils.moveAndClickWithJavaScript(getDriver(), chevron);

        wait.until(ExpectedConditions.attributeToBe(chevron, "aria-expanded", "true"));

        WebElement delete = wait.until(ExpectedConditions.visibilityOfElementLocated((By
                .xpath("//*[contains(@href,'confirm-rename')]"))));
        delete.click();

        getDriver().findElement(By.name("newName")).clear();
        Thread.sleep(200);
        getDriver().findElement(By.name("newName")).sendKeys(RENAMED_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.name("Submit")).click();

        String projectNameViaChevron = getDriver()
                .findElement(By.xpath("//*[@class='job-index-headline page-headline']")).getText();

        Assert.assertEquals(projectNameViaChevron, RENAMED_FREESTYLE_PROJECT_NAME);
    }

    @Test
    public void testJobNameSorting() {
        createFreestyleProject("aaa");
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        createFreestyleProject("bbb");
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        createFreestyleProject("aabb");
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        // This XPath targets the links containing the job names
        List<WebElement> jobLinks = getDriver()
                .findElements(By.xpath("//table[@id='projectstatus']//tbody//tr/td[3]/a"));

        // Extract text from the elements
        List<String> actualOrder = new ArrayList<>();
        for (WebElement link : jobLinks) {
            actualOrder.add(link.getText().trim());
        }

        // Create a copy of the list and sort it alphabetically for expected order
        List<String> expectedOrder = new ArrayList<>(actualOrder);
        Collections.sort(expectedOrder); // Ascending order

        // Verify if the actual order matches the expected order
        Assert.assertEquals(actualOrder, expectedOrder);
    }
}