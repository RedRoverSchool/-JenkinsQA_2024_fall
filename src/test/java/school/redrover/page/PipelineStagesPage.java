package school.redrover.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import school.redrover.page.base.BasePage;

import java.util.ArrayList;
import java.util.List;

public class PipelineStagesPage extends BasePage {

    @FindBy(css = "a[href$='/pipeline-graph/']")
    private List<WebElement> allPipelineBuilds;

    @FindBy(css = "div[class^='TruncatingLabel']")
    private List<WebElement> stages;

    @FindBy(css = "g[class$='icon-blue']")
    private WebElement greenIcon;

    @FindBy(css = "g[class$='icon-red']")
    private WebElement redIcon;

    public PipelineStagesPage(WebDriver driver) {
        super(driver);
    }

    public List<WebElement> getAllPipelineBuilds() {
        return allPipelineBuilds;
    }

    public List<String> getAllStagesNames() {
        return stages.stream().map(el -> el.getAttribute("title")).toList();
    }

    public List<WebElement> getGreenAndRedIcons() {
        List<WebElement> icons = new ArrayList<>();
        icons.add(greenIcon);
        icons.add(redIcon);

        return icons;
    }

}
