package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

import java.time.Duration;

public class FreestyleProject1Test extends BaseTest {
    private static final String NEW_FREESTYLE_PROJECT_NAME = "New freestyle project";

    private void createFreestyleProject() throws InterruptedException {
        getDriver().findElement(By.xpath("//*[@href='/view/all/newJob']")).click();
        getDriver().findElement(By.xpath("//*[@class='hudson_model_FreeStyleProject']")).click();
        getDriver().findElement(By.name("name")).sendKeys(NEW_FREESTYLE_PROJECT_NAME);
        getDriver().findElement(By.id("ok-button")).click();
        getDriver().findElement(By.name("Submit")).click();

    }

    @Test
    public void testCreateFreestyleProject() throws InterruptedException {
        createFreestyleProject();

        //Getting to the main page to ensure that newly created project is there
        getDriver().findElement(By.id("jenkins-name-icon")).click();
        WebElement element = getDriver().findElement(By
                .xpath("//span[contains(text(),'New freestyle project')]"));
        Assert.assertTrue(element.isDisplayed());
    }

    @Test
    public void testDeleteFreestyleProject() throws InterruptedException {
        // Step 1: Create the freestyle project
        createFreestyleProject();

        // Step 2: Navigate to the Jenkins home page
        getDriver().findElement(By.id("jenkins-name-icon")).click();

        Actions actions = new Actions(getDriver());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));

        // Step 3: Hover over project title to activate the menu dropdown
        WebElement projectTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'New freestyle project')]")));
        actions.moveToElement(projectTitle).perform();

        // Step 4: Wait for the chevron button to be clickable and move to it
        WebElement chevron = wait.until(ExpectedConditions.elementToBeClickable(By
                .xpath("//*[@id='job_New freestyle project']/td[3]/a/button")));
        actions.moveToElement(chevron).perform();

        // Step 5: Use JavaScript to click on the chevron (handles potential overlay issues)
        ((JavascriptExecutor) getDriver()).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click', " +
                        "{bubbles: true, cancelable: true, view: window, clientX: " +
                        "arguments[0].getBoundingClientRect().x + 5, " +
                        "clientY: arguments[0].getBoundingClientRect().y + 5}));", chevron);

        // Step 6: Wait for the delete option to appear and be clickable
        WebElement deleteOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(@href,'doDelete')]")));

        // Step 7: Use JavaScript to click the delete option
        ((JavascriptExecutor) getDriver()).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click', " +
                        "{bubbles: true, cancelable: true, view: window, clientX: " +
                        "arguments[0].getBoundingClientRect().x + 5, " +
                        "clientY: arguments[0].getBoundingClientRect().y + 5}));", deleteOption);

        // Step 8: Confirm deletion by clicking "Yes"
        WebElement confirmDelete = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Yes')]")));
        confirmDelete.click();

        // Step 9: Verify that the project is deleted by checking the dashboard text
        WebElement emptyDashboardHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".empty-state-block > h1")));
        Assert.assertEquals(emptyDashboardHeader.getText(), "Welcome to Jenkins!");
    }

}