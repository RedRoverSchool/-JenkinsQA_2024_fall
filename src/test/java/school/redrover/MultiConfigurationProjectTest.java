package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.page.HomePage;
import school.redrover.runner.BaseTest;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class MultiConfigurationProjectTest extends BaseTest {
    private static final String NAME_OF_PROJECT = "MTC project";
    private static final String DESCRIPTIONS = "Descriptions of project";
    private static final  String ERROR_MESSAGE= "» This field cannot be empty, please enter a valid name";

    private void createMultiConfigProject() {
        getDriver().findElement(By.cssSelector("[href$='newJob']")).click();
        getDriver().findElement(By.id("name")).sendKeys("MultiConfigProject");
        getDriver().findElement(By.xpath("//span[text()='Multi-configuration project']")).click();
        getDriver().findElement(By.id("ok-button")).click();
    }

    @Test(description = "Create project without descriptions")
    public void testCreateProjectWithoutDescription() {
        List<String> itemList = new HomePage(getDriver())
                .clickNewItem()
                .enterItemName(NAME_OF_PROJECT)
                .choseMultiConfigurationProject()
                .submitCreationProject()
                .goHome()
                .showCreatedProject();
        Assert.assertTrue(itemList.contains(NAME_OF_PROJECT));

    }

    @Test(description = " MultiConfigurationProjectTest | Add descriptions to existing project")
    public void testAddDescriptions() {
        testCreateProjectWithoutDescription();
        String descriptions  = new HomePage(getDriver())
                .goToMultiConfigurationProjectPage()
                .addDescriptions(DESCRIPTIONS)
                .showDescriptions();

        Assert.assertEquals(getDriver().findElement(By.xpath("//div[@id='description']/div[1]")).getText()
                , DESCRIPTIONS);
    }

    @Test
    public void testCreateProjectWithoutName() {
       String errorFieldMessage  = new HomePage(getDriver())
                .clickNewItem()
                .choseMultiConfigurationProject()
                .errorEmptyField();

        Assert.assertEquals(errorFieldMessage, ERROR_MESSAGE);
    }

    @Test
    public void testDragAndDropConfigurationMatrixBlock() {

        createMultiConfigProject();

        getDriver().findElement(By.xpath("//button[@data-section-id='configuration-matrix']")).click();

        Actions act = new Actions(getDriver());
        WebElement postBuildActionsTitle = getDriver().findElement(By.cssSelector("[id='post-build-actions']"));
        act.scrollToElement(postBuildActionsTitle).perform();

        getDriver().findElement(By.cssSelector("button[suffix='axis']")).click();

        WebElement dropdownList1 = getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[class='jenkins-dropdown__item ']")));
        dropdownList1.click();

        getDriver().findElement(By.cssSelector("input.jenkins-input.validated[name='_.name']")).sendKeys("config1");
        getDriver().findElement(By.cssSelector(".jenkins-input[name='_.valueString']")).sendKeys("value1");

        getDriver().findElement(By.cssSelector("button[suffix='axis']")).click();

        WebElement dropdownList2 = getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[class='jenkins-dropdown__item ']")));
        dropdownList2.click();

        getDriver().findElement(By.xpath("(//input[@name='_.name' and @class='jenkins-input validated  '])[2]")).sendKeys("config2");
        getDriver().findElement(By.xpath("(//input[contains(@name, 'valueString')])[2]")).sendKeys("value2");

        WebElement firstAxis = getDriver().findElement(By.xpath("(//div[@class='dd-handle'])[1]"));
        WebElement secondAxis = getDriver().findElement(By.xpath("(//div[@class='dd-handle'])[2]"));

        Actions actions = new Actions(getDriver());
        actions.moveToElement(firstAxis)
            .clickAndHold()
            .moveByOffset(0, 10)
            .moveToElement(secondAxis)
            .release()
            .perform();

        String actualFirstAxisName = getDriver()
                                         .findElement(By.xpath("(//input[@name='_.name' and @class='jenkins-input validated  '])[1]"))
                                         .getAttribute("value");
        String actualSecondAxisName = getDriver()
                                          .findElement(By.xpath("(//input[@name='_.name' and @class='jenkins-input validated  '])[2]"))
                                          .getAttribute("value");

        Assert.assertEquals(actualFirstAxisName, "config2");
        Assert.assertEquals(actualSecondAxisName, "config1");
    }

    @Test
    public void testSelectTimePeriodThrottleBuilds() {

        createMultiConfigProject();

        getDriver().findElement(By.xpath("//span[@class='jenkins-checkbox']/label[text()='Throttle builds']")).click();

        WebElement durationItemsSelect = getDriver().findElement(By.xpath("//select[@name='_.durationName']"));

        Select select = new Select(durationItemsSelect);
        select.selectByValue("month");

        String actualSelectedItemName = select.getFirstSelectedOption().getText();

        Assert.assertEquals(actualSelectedItemName, "Month");
    }
    @Test
    public void testCreateWithExistingName(){
        testCreateProjectWithoutDescription();
        String errorMessage = new  HomePage(getDriver())
                .clickNewItem()
                .enterItemName(NAME_OF_PROJECT)
                .choseMultiConfigurationProject()
                .saveInvalidData()
                .getErrorMessage();

        Assert.assertEquals(errorMessage, "A job already exists with the name " +
        "‘MTC project’");
    }
}