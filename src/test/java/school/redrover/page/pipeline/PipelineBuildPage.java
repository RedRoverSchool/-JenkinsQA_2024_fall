package school.redrover.page.pipeline;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.page.base.BasePage;

import java.util.List;

public class PipelineBuildPage extends BasePage<PipelineBuildPage> {

    @FindBy(xpath = "//button[@name='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[normalize-space(text())=\"Don't keep this build forever\"]")
    private WebElement keepThisBuildForeverButton;

    @FindBy(xpath = "//div[@id='tasks']/div")
    private List<WebElement> sidebarTaskList;

    public PipelineBuildPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click 'Keep this Build Forever' button")
    public PipelineBuildPage clickKeepThisBuildForever() {
        getWait10().until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        getWait2().until(ExpectedConditions.visibilityOf(keepThisBuildForeverButton));

        return this;
    }

    @Step("Check 'Delete Build #' sidebar item is displayed")
    public boolean isDeleteBuildOptionSidebarPresent(String name) {

        return sidebarTaskList.stream()
                .anyMatch(element -> element.getAttribute("href") != null &&
                        element.getAttribute("href").contains("/job/%s/1/confirmDelete".formatted(name)));
    }
}
