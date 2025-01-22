package school.redrover.page.component;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.SearchBoxPage;
import school.redrover.page.SearchPage;
import school.redrover.page.base.BaseComponent;
import school.redrover.page.base.BasePage;
import school.redrover.page.home.HomePage;
import school.redrover.page.manage.ManageJenkinsPage;

import java.util.List;

public class HeaderComponent<T extends BasePage<T>> extends BaseComponent<T> {

    @FindBy(id = "jenkins-home-link")
    private WebElement logo;

    @FindBy(xpath = "//a[contains(@class,'main-search')]")
    private WebElement iconQuestion;

    @FindBy(id = "search-box")
    private WebElement searchBox;

    @FindBy(xpath = "//div[@class='yui-ac-bd']/ul/li[1]")
    private WebElement firstSuggestion;

    @FindBy(xpath = "//div[@class='yui-ac-bd']/ul/li[3]")
    private WebElement suggestedManage;

    @FindBy(css = "a[id='visible-am-button'] svg")
    private WebElement iconBell;

    @FindBy(xpath = "//div[@class='am-list']/p/a")
    private WebElement linkWithPopup;

    @FindBy(xpath = "//div[@class='yui-ac-bd']/ul/li[not(contains(@style, 'display: none'))]")
    private List<WebElement> listSuggestion;

    public HeaderComponent(WebDriver driver, T owner) {
        super(driver, owner);
    }

    @Step("Go to Home Page")
    public HomePage gotoHomePage() {
        logo.click();

        return new HomePage(getDriver());
    }

    public SearchBoxPage gotoSearchBox() {
        iconQuestion.click();

        return new SearchBoxPage(getDriver());
    }

    public HomePage enterSearch(String text) {
        searchBox.sendKeys(text);

        return new HomePage(getDriver());
    }

    public SearchPage pressEnter() {
        searchBox.sendKeys(Keys.ENTER);

        return new SearchPage(getDriver());
    }

    public SearchPage typeTextIntoSearchFieldAndPressEnter(String text) {
        searchBox.sendKeys(text, Keys.ENTER);

        return new SearchPage(getDriver());
    }

    public HomePage getSuggestion() {
        getWait2().until(ExpectedConditions.elementToBeClickable(firstSuggestion));
        new Actions(getDriver()).moveToElement(firstSuggestion).click().perform();

        return new HomePage(getDriver());
    }

    public HomePage clickBell() {
        iconBell.click();

        return new HomePage(getDriver());
    }

    public ManageJenkinsPage clickLinkWithPopup() {
        linkWithPopup.click();

        return new ManageJenkinsPage(getDriver());
    }

    public List<String> getListSuggestion() {
        getWait5().until(ExpectedConditions.elementToBeClickable(firstSuggestion));

        return listSuggestion.stream().map(WebElement::getText).toList();
    }

    public ManageJenkinsPage clickOnSuggestedManage() {
        getWait2().until(ExpectedConditions.elementToBeClickable(suggestedManage)).click();

        return new ManageJenkinsPage(getDriver());
    }

}
