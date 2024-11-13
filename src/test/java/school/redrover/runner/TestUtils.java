package school.redrover.runner;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class TestUtils {

    public static class ExpectedConditions {
        public static ExpectedCondition<WebElement> elementIsNotMoving(final By locator) {
            return new ExpectedCondition<>() {
                private Point lastLocation = null; // Инициализируем здесь

                @Override
                public WebElement apply(WebDriver driver) {
                    WebElement element;
                    try {
                        element = driver.findElement(locator);
                    } catch (NoSuchElementException e) {
                        return null;
                    }

                    if (element.isDisplayed()) {
                        Point currentLocation = element.getLocation();
                        if (currentLocation.equals(lastLocation)) { // Сравниваем текущее положение с предыдущим
                            return element; // Элемент перестал двигаться
                        }
                        lastLocation = currentLocation; // Обновляем положение элемента
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

}
