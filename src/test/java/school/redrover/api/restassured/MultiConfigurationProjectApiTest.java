package school.redrover.api.restassured;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.model.ProjectListResponse;
import school.redrover.model.ProjectResponse;
import school.redrover.runner.BaseApiTest;
import school.redrover.runner.TestUtils;

import static io.restassured.RestAssured.given;
import static school.redrover.runner.TestApiUtils.requestSpec;
import static school.redrover.runner.TestApiUtils.responseSpec;

@Epic("API")
@Story("Multi-Configuration Project")
public class MultiConfigurationProjectApiTest extends BaseApiTest {

    private final String MULTI_CONFIG_NAME = "MultiConfigurationProject";
    private final String MULTI_CONFIG_MODE = "hudson.matrix.MatrixProject";
    private final String MULTI_CONFIG_NAME_XML = "MultiConfigurationProjectXML";
    private final String DESCRIPTION = "Add description to Project!";

    private static String getCreateItemPath() {return "createItem";}
    private static String getAddDescriptionToCreatedItemPath(String name) {return "job/%s/submitDescription".formatted(name);}

    private static void createNewProjectXML(String name, String xmlFile) {
        given()
                .spec(requestSpec())
                .contentType(ContentType.XML)
                .queryParam("name", name)
                .body(TestUtils.loadPayload(xmlFile))
                .when()
                .post(getCreateItemPath())
                .then()
                .spec(responseSpec(200, 500L));
    }

    private static Response getResponseGetProjectByName(String name) {
        return given()
                .spec(requestSpec())
                .when()
                .get("job/%s/api/json".formatted(name))
                .then()
                .spec(responseSpec(200,500L))
                .extract()
                .response();
    }

    @Test
    @Description("00.003.09 Create Multi-Configuration Project with valid name")
    public void testCreateMultiConfigWithValidName() {
        given()
                .spec(requestSpec())
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("name", MULTI_CONFIG_NAME)
                .formParam("mode", MULTI_CONFIG_MODE)
                .when()
                .post("createItem")
                .then()
                .spec(responseSpec(302, 500L));

        Response responseGetCreatedItem = given()
                .spec(requestSpec())
                .when()
                .get("/job/%s/api/json".formatted(MULTI_CONFIG_NAME))
                .then()
                .spec(responseSpec(200,500L))
                .extract()
                .response();

        ProjectResponse getItemByNameResponse = responseGetCreatedItem.as(ProjectResponse.class);

        Allure.step(String.format("Expected result: fullName is '%s'", MULTI_CONFIG_NAME));
        Assert.assertEquals(getItemByNameResponse.getFullName(), MULTI_CONFIG_NAME);
        Allure.step("(ERR)Expected result: description is null");
        Assert.assertEquals(getItemByNameResponse.getDescription(),null);
        Allure.step(String.format("Expected result: _class is '%s'", MULTI_CONFIG_MODE));
        Assert.assertEquals(getItemByNameResponse.get_class(), MULTI_CONFIG_MODE);

        Response responseGetAllProjectList = given()
                .spec(requestSpec())
                .when()
                .get("/api/json")
                .then()
                .spec(responseSpec(200, 500L))
                .extract()
                .response();

        ProjectListResponse projectListresponse = responseGetAllProjectList.as(ProjectListResponse.class);

        boolean findItemByName = projectListresponse.getJobs().stream()
                .anyMatch(project -> project.getName().equals(MULTI_CONFIG_NAME));

        Allure.step("Expected result: Project name found in the list");
        Assert.assertTrue(findItemByName, "Project name not found in the list");
    }

    @Test
    @Description("00.003.10 Create Multi-Configuration Project with valid name XML")
    public void testCreateProjectWithValidNameXML() {

        given()
                .spec(requestSpec())
                .contentType(ContentType.XML)
                .queryParam("name", MULTI_CONFIG_NAME_XML)
                .body(TestUtils.loadPayload("create-empty-multi-config.xml"))
                .when()
                .post("createItem ")
                .then()
                .spec(responseSpec(200, 500L));

        Response responseGetCreatedItem = given()
                .spec(requestSpec())
                .when()
                .get("job/%s/api/json".formatted(MULTI_CONFIG_NAME_XML))
                .then()
                .spec(responseSpec(200,500L))
                .extract()
                .response();

        ProjectResponse getItemByNameResponse = responseGetCreatedItem.as(ProjectResponse.class);

        Allure.step(String.format("Expected result: fullName is '%s'", MULTI_CONFIG_NAME_XML));
        Assert.assertEquals(getItemByNameResponse.getFullName(), MULTI_CONFIG_NAME_XML);
        Allure.step("Expected result: description is empty");
        Assert.assertEquals(getItemByNameResponse.getDescription(),"");
        Allure.step(String.format("Expected result: _class is '%s'", MULTI_CONFIG_MODE));
        Assert.assertEquals(getItemByNameResponse.get_class(),MULTI_CONFIG_MODE);

        Response responseGetAllProjectList = given()
                .spec(requestSpec())
                .when()
                .get("api/json")
                .then()
                .spec(responseSpec(200, 500L))
                .extract()
                .response();

        ProjectListResponse projectListresponse = responseGetAllProjectList.as(ProjectListResponse.class);

        boolean findItemByName = projectListresponse.getJobs().stream()
                .anyMatch(project -> project.getName().equals(MULTI_CONFIG_NAME_XML));

        Allure.step("Expected result: Project name found in the list");
        Assert.assertTrue(findItemByName, "Project name not found in the list");
    }

    @Test
    @Description("00.003.11 Create Multi-Configuration Project with valid name XML")
    public void testCreateProjectWithValidNameAndDescriptionXML() {
        String description = "Create Project with Description!";

        given()
                .spec(requestSpec())
                .contentType(ContentType.XML)
                .queryParam("name", MULTI_CONFIG_NAME_XML)
                .body(TestUtils.loadPayload("create-multi-config-with-description.xml"))
                .when()
                .post("createItem ")
                .then()
                .spec(responseSpec(200, 500L));

        Response responseGetCreatedItem = given()
                .spec(requestSpec())
                .when()
                .get("job/%s/api/json".formatted(MULTI_CONFIG_NAME_XML))
                .then()
                .spec(responseSpec(200,500L))
                .extract()
                .response();

        ProjectResponse getItemByNameResponse = responseGetCreatedItem.as(ProjectResponse.class);

        Allure.step(String.format("Expected result: fullName is '%s'", MULTI_CONFIG_NAME_XML));
        Assert.assertEquals(getItemByNameResponse.getFullName(), MULTI_CONFIG_NAME_XML);
        Allure.step("Expected result: description is %s".formatted(description));
        Assert.assertEquals(getItemByNameResponse.getDescription(),description);
        Allure.step(String.format("Expected result: _class is '%s'", MULTI_CONFIG_MODE));
        Assert.assertEquals(getItemByNameResponse.get_class(),MULTI_CONFIG_MODE);

        Response responseGetAllProjectList = given()
                .spec(requestSpec())
                .when()
                .get("api/json")
                .then()
                .spec(responseSpec(200, 500L))
                .extract()
                .response();

        ProjectListResponse projectListresponse = responseGetAllProjectList.as(ProjectListResponse.class);

        boolean findItemByName = projectListresponse.getJobs().stream()
                .anyMatch(project -> project.getName().equals(MULTI_CONFIG_NAME_XML));

        Allure.step("Expected result: Project name found in the list");
        Assert.assertTrue(findItemByName, "Project name not found in the list");

        given()
                .spec(requestSpec())
                .when()
                .delete("/job/%s/".formatted(MULTI_CONFIG_NAME_XML))
                .then()
                .spec(responseSpec(204,500L));
    }

    @Test
    @Description("03.001.01 Add description to Project")
    public void testAddDescriptionToCreatedProject() {
        createNewProjectXML(MULTI_CONFIG_NAME_XML,"create-empty-multi-config.xml");

        given()
                .spec(requestSpec())
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("description", DESCRIPTION)
                .when()
                .post(getAddDescriptionToCreatedItemPath(MULTI_CONFIG_NAME_XML))
                .then()
                .spec(responseSpec(302, 500L));


        ProjectResponse getItemByNameResponse = getResponseGetProjectByName(MULTI_CONFIG_NAME_XML).as(ProjectResponse.class);

        Allure.step(String.format("Expected result: fullName is '%s'", MULTI_CONFIG_NAME_XML));
        Assert.assertEquals(getItemByNameResponse.getFullName(), MULTI_CONFIG_NAME_XML);
        Allure.step("Expected result: description is empty");
        Assert.assertEquals(getItemByNameResponse.getDescription(),DESCRIPTION);
        Allure.step(String.format("Expected result: _class is '%s'", MULTI_CONFIG_MODE));
        Assert.assertEquals(getItemByNameResponse.get_class(),MULTI_CONFIG_MODE);

    }

    @Test(dependsOnMethods = "testCreateProjectWithValidNameXML")
    @Description("03.003.01 Delete Project")
    public void testDeleteProject() {

        Allure.step("Send DELETE request -> Delete Project with name %s".formatted(MULTI_CONFIG_NAME_XML));
        given()
                .spec(requestSpec())
                .when()
                .delete("/job/%s/".formatted(MULTI_CONFIG_NAME_XML))
                .then()
                .spec(responseSpec(204,500L));

        Allure.step("Send GET request -> Get Project with name %s".formatted(MULTI_CONFIG_NAME_XML));
        given()
                .spec(requestSpec())
                .when()
                .get("/job/%s/".formatted(MULTI_CONFIG_NAME_XML))
                .then()
                .spec(responseSpec(404, 500L));
    }
}
