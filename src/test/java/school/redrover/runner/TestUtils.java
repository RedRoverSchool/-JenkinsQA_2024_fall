package school.redrover.runner;

import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class TestUtils {

    public static class ExpectedConditions {
        public static ExpectedCondition<WebElement> elementIsNotMoving(final By locator) {
            return new ExpectedCondition<>() {
                private Point location = null;

                @Override
                public WebElement apply(WebDriver driver) {
                    WebElement element;
                    try {
                        element = driver.findElement(locator);
                    } catch (NoSuchElementException e) {
                        return null;
                    }

                    if (element.isDisplayed()) {
                        Point location = element.getLocation();
                        if (location.equals(this.location)) {
                            return element;
                        }
                        this.location = location;
                    }

                    return null;
                }
            };
        }

        public static ExpectedCondition<WebElement> elementIsNotMoving(final WebElement element) {
            return new ExpectedCondition<>() {
                private Point location = null;

                @Override
                public WebElement apply(WebDriver driver) {
                    if (element.isDisplayed()) {
                        Point location = element.getLocation();
                        if (location.equals(this.location)) {
                            return element;
                        }
                        this.location = location;
                    }

                    return null;
                }
            };
        }
    }

    public static void moveAndClickWithJavaScript(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].dispatchEvent(new Event('mouseenter'));", element);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].dispatchEvent(new Event('click'));", element);
    }

    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void createPipeline(BaseTest baseTest, String name) {
        baseTest.getDriver().findElement(By.xpath("//a[@href='/view/all/newJob']")).click();

        baseTest.getDriver().findElement(By.xpath("//input[@name='name']")).sendKeys(name);
        baseTest.getDriver().findElement(By.xpath("//li[@class='org_jenkinsci_plugins_workflow_job_WorkflowJob']")).click();
        baseTest.getDriver().findElement(By.xpath("//button[@type='submit']")).click();

        baseTest.getDriver().findElement(By.xpath("//button[@name='Submit']")).click();
    }

    public static void readFileAndCopyToClipboard(String fileName) {
        try (FileInputStream fis = new FileInputStream(Paths.get("test_data", fileName).toString())) {
            String fileContent = new String(fis.readAllBytes());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fileContent), null);
        } catch (IOException e) {
            ProjectUtils.log("File not found");
        }
    }
}
