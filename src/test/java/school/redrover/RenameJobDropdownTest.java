package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

import java.util.List;
import java.time.Duration;

public class RenameJobDropdownTest extends BaseTest {

    @Test(description = "rename job with dropdown")
    public void tesRenameJobDropdown() {

        final String jobName = "TestBuild";
        final String jobNewName = "TestBuild_NewName";

        createJob(jobName);
        findEndClickLinkInDropdown(jobName, "Rename");
        renameJob(jobNewName);

        Assert.assertEquals(getDriver().findElement(By
                        .xpath("//*[@class='jenkins-table__link model-link inside']"))
                .getText(), jobNewName);

    }

    private void renameJob(String jobNewName) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        WebElement inputFieldNewName = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='newName']")));
        inputFieldNewName.clear();
        inputFieldNewName.sendKeys(jobNewName);

        Actions actionsRenameButton = new Actions(getDriver());
        actionsRenameButton.moveToElement(getDriver().findElement(By.xpath("//*[@id='bottom-sticker']/div/button")))
                .click()
                .perform();

        getDriver().findElement(By.id("jenkins-home-link")).click();

    }

    private void findEndClickLinkInDropdown(String jobName, String linkName ) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(1));
        WebElement jobNameLink = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By
                        .xpath("//a[@href='job/" + jobName + "/']")));
//                        .xpath("//*[@id='job_TestBuild']/td[3]/a/span")));

        Actions actionDropdownChevron = new Actions(getDriver());
        actionDropdownChevron
                .moveToElement(jobNameLink)
                .pause(Duration.ofSeconds(2))
//                .moveToElement(getDriver().findElement(
                .scrollToElement(getDriver().findElement(
                       By.xpath("//a[@href='job/" + jobName + "/']//button[@class='jenkins-menu-dropdown-chevron']")))
//                       By.xpath("//*[@id='job_TestBuild']/td[3]/a/button")))
                .pause(Duration.ofSeconds(2))
                .moveToElement(getDriver().findElement(
                        By.xpath("//a[@href='job/" + jobName + "/']//button[@class='jenkins-menu-dropdown-chevron']")))
                .pause(Duration.ofSeconds(2))
                .click()
                .pause(Duration.ofSeconds(2))
                .perform();

        List<WebElement> dropdownLinks = getDriver().findElements(By.xpath("//div[@class='tippy-content']//a"));
        List<WebElement> dropdownButtons = getDriver().findElements(By.xpath("//div[@class='tippy-content']//button"));

        for (WebElement dropdownLink : dropdownLinks) {
            if (dropdownLink.getText().equals(linkName )) {
                getDriver().get(dropdownLink.getAttribute("href"));
            }
        }
    }

    private void clickLinkInDropdown(WebElement dropdownLink) {

        Actions actionsDropdownLink = new Actions(getDriver());
        actionsDropdownLink.moveToElement(dropdownLink)
                .click()
                .perform();

    }

    private void createJob(String projectName) {

        getDriver().findElement(By.xpath("//*[@id='main-panel']/div[2]/div/section[1]/ul/li/a")).click();
        getDriver().findElement(By.xpath("//*[@id='name']")).sendKeys(projectName);
        getDriver().findElement(By.xpath("//*[@id='j-add-item-type-standalone-projects']/ul/li[1]")).click();
        getDriver().findElement(By.xpath("//*[@id='ok-button']")).click();
        getDriver().findElement(By.xpath("//*[@id='bottom-sticker']/div/button[1]")).click();
        getDriver().findElement(By.xpath("//*[@id='breadcrumbs']/li[1]/a")).click();

    }

}