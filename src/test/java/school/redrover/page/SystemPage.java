package school.redrover.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import school.redrover.page.base.BasePage;

public class SystemPage extends BasePage {

    @FindBy(id = "breadcrumbs")
    private static WebElement breadcrumbs;

    public SystemPage(WebDriver driver) {
        super(driver);
    }

    public String getBreadCrumbs() {

        return breadcrumbs.getText();
    }
}
