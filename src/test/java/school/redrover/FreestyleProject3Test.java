package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;
import java.time.Duration;

public class FreestyleProject3Test extends BaseTest {

    private static final String PROJECT_NAME = "FreestyleProject fall2024";

    private static final String DESCRIPTION = "Bla-bla-bla project";

    private void createProjectViaSidebarMenu(String projectName) {
        getDriver().findElement(By.xpath("//a[@href='/view/all/newJob']")).click();
        getDriver().findElement(By.id("name")).sendKeys(projectName);
        getDriver().findElement(By.xpath("//li[contains(@class, 'FreeStyleProject')]")).click();
        getDriver().findElement(By.id("ok-button")).click();
        getDriver().findElement(By.xpath("//button[@name='Submit']")).click();
    }

    @Test
    public void testCreateProjectViaCreateJobButton() {
        WebElement createJobButton = getDriver().findElement(By.xpath("//a[@href='newJob']"));
        createJobButton.click();

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        WebElement newItemNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        newItemNameField.sendKeys(PROJECT_NAME);

        WebElement freestyleProjectItemType = getDriver().findElement(
                By.xpath("//li[contains(@class, 'FreeStyleProject')]"));
        freestyleProjectItemType.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@name='Submit']")));
        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Permalinks']")));

        String actualName = getDriver().findElement(By.tagName("h1")).getText();

        Assert.assertEquals(actualName, PROJECT_NAME);
    }

    @Test
    public void testCreateProjectViaSidebarMenu () {
        createProjectViaSidebarMenu(PROJECT_NAME);

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Permalinks']")));

        String actualName = getDriver().findElement(By.tagName("h1")).getText();

        Assert.assertEquals(actualName, PROJECT_NAME);
    }

    @Test
    public void addDescriptionOnProjectStatusPage() {
        WebElement newItemSidebarMenu = getDriver().findElement(By.xpath("//a[@href='/view/all/newJob']"));
        newItemSidebarMenu.click();

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        WebElement newItemNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        newItemNameField.sendKeys(PROJECT_NAME);

        WebElement freestyleProjectItemType = getDriver().findElement(
                By.xpath("//li[contains(@class, 'FreeStyleProject')]"));
        freestyleProjectItemType.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@name='Submit']")));
        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Permalinks']")));

        WebElement addDescriptionButton = getDriver().findElement(By.id("description-link"));
        addDescriptionButton.click();

        WebElement descriptionTextField = getDriver().findElement(By.tagName("textarea"));
        descriptionTextField.sendKeys(DESCRIPTION);

        WebElement submitButton = getDriver().findElement(By.xpath("//button[@name='Submit']"));
        submitButton.click();

        WebElement projectDescriptionOnStatusPage = getDriver().findElement(
                By.xpath("//div[@id='description']//div"));

        Assert.assertTrue(projectDescriptionOnStatusPage.getText().contains(DESCRIPTION));
    }
}
