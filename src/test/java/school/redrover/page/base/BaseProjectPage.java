package school.redrover.page.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.CreateNewItemPage;

import java.util.List;

public abstract class BaseProjectPage<Self extends BaseProjectPage<?>> extends BasePage {

    @FindBy(id = "description-link")
    private WebElement descriptionButton;

    @FindBy(xpath = "//span[text()='New Item']/ancestor::a")
    private WebElement newItem;

    @FindBy(name = "description")
    private WebElement descriptionField;

    @FindBy(name = "Submit")
    private WebElement submitButton;

    @FindBy(id = "description")
    private WebElement descriptionText;

    @FindBy(xpath = "//a[contains(@href,'rename')]")
    private WebElement renameButtonViaSidebar;

    @FindBy(name = "newName")
    private WebElement newNameField;

    @FindBy(xpath = "//div[@id='main-panel']/p")
    private WebElement errorMessage;

    @FindBy(xpath = "//div[@class='task ']//span[2]")
    private List<WebElement> sidebarElementList;

    @FindBy(xpath = "//*[@id='main-panel']/h1")
    private WebElement itemName;

    @FindBy(className = "textarea-show-preview")
    private WebElement previewOption;

    @FindBy(className = "textarea-preview")
    private WebElement previewDescriptionText;

    public BaseProjectPage(WebDriver driver) {
        super(driver);
    }

    public CreateNewItemPage clickNewItem() {
        newItem.click();

        return new CreateNewItemPage(getDriver());
    }

    public Self editDescription(String text) {
        descriptionButton.click();
        descriptionField.clear();
        descriptionField.sendKeys(text);

        return (Self) this;
    }

    public Self clickSubmitButton() {
        submitButton.click();

        return (Self) this;
    }

    public Self clearDescription() {
        descriptionButton.click();
        descriptionField.clear();
        submitButton.click();

        return (Self) this;
    }

    public Self clickPreview() {
        previewOption.click();

        return (Self) this;
    }

    public Self renameItem(String newName) {
        renameButtonViaSidebar.click();
        newNameField.clear();
        newNameField.sendKeys(newName);
        submitButton.click();
        return (Self) this;
    }

    public String getPreviewDescriptionText() {
        return previewDescriptionText.getText();
    }

    public String getRenameWarningMessage() {
        return getWait10().until(ExpectedConditions.visibilityOf(errorMessage)).getText();
    }

    public String getItemName() {
        return getWait10().until(ExpectedConditions.visibilityOf(itemName)).getText();
    }

    public String getDescription() {
        return descriptionText.getText();
    }

    public List<String> getSidebarOptionList() {
        return getWait5().until(ExpectedConditions.visibilityOfAllElements(sidebarElementList))
                .stream()
                .map(WebElement::getText)
                .toList();
    }
}
