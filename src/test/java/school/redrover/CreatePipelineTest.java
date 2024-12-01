package school.redrover;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;


public class CreatePipelineTest extends BaseTest {

    @Test
    public void testPipeline () {

        getDriver().findElement(By.xpath("//*[@id='tasks']/div[1]/span/a/span[1]")).click();
        getDriver().findElement(By.id("name")).sendKeys("test");
        getDriver().findElement(By.className("org_jenkinsci_plugins_workflow_job_WorkflowJob")).click();
        getDriver().findElement(By.id("ok-button")).click();

        getDriver().findElement(By.xpath("//label[contains(text(), 'Do not allow the pipeline to resume if the controller restarts')]")).click();
        getDriver().findElement(By.xpath("//label[contains(text(), 'Throttle builds')]")).click();

        getDriver().findElement(By.name("Submit")).click();

        String pipelineName = getDriver().findElement(By.xpath("//*[@id='main-panel']/div[1]/div[1]/h1")).getText();

        Assert.assertEquals(pipelineName, "test");

    }



}
