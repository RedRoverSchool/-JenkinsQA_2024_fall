package school.redrover.runner;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import school.redrover.page.HomePage;

public class TestUtils {

    public static ExpectedCondition<Boolean> isElementInViewPort(WebElement element) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return (Boolean) js.executeScript(
                        "let rect = arguments[0].getBoundingClientRect();" +
                                "return (rect.top >= 0 && rect.left >= 0 && " +
                                "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                                "rect.right <= (window.innerWidth || document.documentElement.clientWidth));",
                        element);
            }
        };
    }

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
        new HomePage(baseTest.getDriver())
                .clickNewItem()
                .enterItemName(name)
                .selectPipelineAndClickOk()
                .clickSaveButton()
                .gotoHomePage();
    }

    public static void createFreestyleProject(BaseTest baseTest, String name) {
        new HomePage(baseTest.getDriver())
                .clickNewItem()
                .enterItemName(name)
                .selectFreestyleProjectAndClickOk()
                .clickSaveButton()
                .gotoHomePage();
    }

    public static void newItemsData(BaseTest baseTest, String itemName, String itemXpath) {
        baseTest.getDriver().findElement(By.xpath("//*[@id='tasks']/div[1]/span/a")).click();
        baseTest.getDriver().findElement(By.id("name")).sendKeys(itemName);
        baseTest.getDriver().findElement(By.xpath(itemXpath)).click();
        baseTest.getDriver().findElement(By.id("ok-button")).click();
    }

    public static void pasteTextWithJavaScript(WebDriver driver, WebElement element, String text) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value = arguments[1];", element, text);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", element);
    }

}
