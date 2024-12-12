package school.redrover.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import school.redrover.page.base.BasePage;

public class AppearancePage extends BasePage {

    @FindBy(xpath = "//label[@for='radio-block-0']")
    private WebElement selectDarkThemes;

    @FindBy(xpath = "//label[@for='radio-block-1']")
    private WebElement selectSystemThemes;

    @FindBy(xpath = "//label[@for='radio-block-2']")
    private WebElement selectDefaultThemes;

    @FindBy(xpath = "//button[@name='Apply']")
    private WebElement selectApplyButton;

    @FindBy(css = "[class='attach-previous ']")
    private WebElement checkboxDifferentTheme;

    @FindBy(css = "html[data-theme]")
    private WebElement dataTheme;

    public AppearancePage(WebDriver driver) {
        super(driver);
    }

    public AppearancePage clickSelectDarkThemes() {
        selectDarkThemes.click();

        return this;
    }

    public AppearancePage clickSelectSystemThemes() {
        selectSystemThemes.click();

        return this;
    }

    public AppearancePage clickSelectDefaultThemes() {
        selectDefaultThemes.click();

        return this;
    }

    public AppearancePage clickApplyButton() {
        selectApplyButton.click();

        return this;
    }

    public AppearancePage clickCheckboxDifferentTheme() {
        checkboxDifferentTheme.click();

        return this;
    }

    public String getAttributeData() {
        return dataTheme.getAttribute("data-theme");
    }
}
