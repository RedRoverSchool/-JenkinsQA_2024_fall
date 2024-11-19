package school.redrover;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class CheckViewTest extends BaseTest {

    private enum NameOfProjects {

        My_First_Project("My first project"),
        My_Second_Project("My second project"),
        My_Third_Project("My third project");

        NameOfProjects(String value) {
            this.value = value;
        }

        final String value;
    }

    private void createNewProject(NameOfProjects nameOfProjects) {

        getDriver().findElement(
                By.xpath("//div[@class='task ']//span//a[@it='hudson.model.Hudson@3c3c987']")).click();
        getDriver().findElement(By.xpath("//input[@name='name']")).sendKeys(nameOfProjects.value);
        getDriver().findElement(By.cssSelector(".hudson_model_FreeStyleProject")).click();
        getDriver().findElement(By.xpath("//button[@id='ok-button']")).click();
        getDriver().findElement(By.xpath("//li[@class='jenkins-breadcrumbs__list-item']")).click();
    }

    private void createView() {

        getDriver().findElement(By.xpath("//a[@tooltip='New View']")).click();
        getDriver().findElement(By.xpath("//input[@id='name']")).sendKeys("New");
        getDriver().findElement(By.xpath("//label[@for='hudson.model.ListView']")).click();
        getDriver().findElement(By.xpath("//button[@formnovalidate='formNoValidate']")).click();
        getDriver().findElement(By.xpath("//label[@title='My first project']")).click();
        getDriver().findElement(By.xpath("//button[@name='Submit']")).click();
    }

    @Test
    public void testView() {

        createNewProject(NameOfProjects.My_First_Project);
        createNewProject(NameOfProjects.My_Second_Project);
        createNewProject(NameOfProjects.My_Third_Project);
        createView();

        Assert.assertEquals(getDriver().findElement(By.xpath(
                "//a[@class='jenkins-table__link model-link inside']")).getText(),
                "My first project");
    }
}

