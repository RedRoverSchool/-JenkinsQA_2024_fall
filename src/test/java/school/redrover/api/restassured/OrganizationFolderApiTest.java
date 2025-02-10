package school.redrover.api.restassured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.model.ProjectResponse;
import school.redrover.runner.BaseApiTest;
import school.redrover.runner.TestUtils;

import static io.restassured.RestAssured.given;
import static school.redrover.runner.TestApiUtils.requestSpec;
import static school.redrover.runner.TestApiUtils.responseSpec;
import static school.redrover.runner.TestUtils.loadPayload;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Epic("API")
public class OrganizationFolderApiTest extends BaseApiTest {

    private static final String ORGANIZATION_FOLDER_NAME = "OrganizationFolderName";
    private static final String RENAMED_ORGANIZATION_FOLDER_NAME = "RenamedOrganizationFolderName";
    private static final String DISPLAY_NAME_ORG_FOLDER = "DisplayNameOrgFolder";
    private static final String DESCRIPTION = "ProjectDescription";

    private Response requestProjectJson(String name) {
        return given()
                .spec(requestSpec())
                .basePath("job/%s".formatted(name))
                .when()
                .get("api/json")
                .then()
                .spec(responseSpec(200, 500L))
                .extract().response();
    }

    @Test
    @Feature("Organization folder")
    @Description("00.007.01 Create Organization folder with valid name")
    public void testCreateWithXML() {
        given()
                .spec(requestSpec())
                .queryParam("name", ORGANIZATION_FOLDER_NAME)
                .contentType(ContentType.XML)
                .body(loadPayload("create-empty-organization-folder.xml"))
                .when()
                .post("view/all/createItem")
                .then()
                .spec(responseSpec(200, 500L));

        Response response = requestProjectJson(ORGANIZATION_FOLDER_NAME);
        ProjectResponse projectResponse = response.as(ProjectResponse.class);

        Assert.assertEquals(response.getHeader("Content-Type"), "application/json;charset=utf-8");
        Assert.assertTrue(response.getTime() <= 500);

        Assert.assertEquals(projectResponse.getName(), ORGANIZATION_FOLDER_NAME);
        Assert.assertEquals(projectResponse.get_class(), "jenkins.branch.OrganizationFolder");
    }

    @Test(dependsOnMethods = "testCreateWithXML")
    @Feature("Organization folder")
    @Description("00.007.05 Get error message when create Organization Folder with the same name")
    public void testCreateSameName() {
        Response htmlResponse = given()
                .spec(requestSpec())
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", ORGANIZATION_FOLDER_NAME)
                .formParam("mode", "jenkins.branch.OrganizationFolder")
                .when()
                .post("view/all/createItem/")
                .then()
                .spec(responseSpec(400, 500L))
                .extract().response();

        Document document = Jsoup.parse(htmlResponse.asString());

        Assert.assertEquals(document.select("h1").text(), "Error");
        Assert.assertEquals(document.select("p").text(),
                String.format("A job already exists with the name ‘%s’", ORGANIZATION_FOLDER_NAME));
    }

    @Test(dependsOnMethods = "testCreateSameName")
    @Feature("Organization folder")
    @Description("06.001.04 Change configurations for Organization folder")
    public void testChangeProjectConfigurations() throws JsonProcessingException {
        String projectJson = TestUtils.loadPayload("organization-folder-configuration.json");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(projectJson, new TypeReference<>() {
        });

        jsonMap.put("enable", false);
        jsonMap.put("displayNameOrNull", DISPLAY_NAME_ORG_FOLDER);
        jsonMap.put("description", DESCRIPTION);

        String modifiedJson = objectMapper.writeValueAsString(jsonMap);

        given()
                .spec(requestSpec())
                .contentType("application/x-www-form-urlencoded")
                .formParam("json", modifiedJson)
                .when()
                .post("job/%s/configSubmit".formatted(ORGANIZATION_FOLDER_NAME))
                .then()
                .spec((responseSpec(302, 500L)))
                .extract().response();

        Response response = requestProjectJson(ORGANIZATION_FOLDER_NAME);
        ProjectResponse projectResponse = response.as(ProjectResponse.class);

        Assert.assertTrue(response.getTime() <= 500);
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json;charset=utf-8");

        Assert.assertEquals(projectResponse.getName(), ORGANIZATION_FOLDER_NAME);
        Assert.assertEquals(projectResponse.get_class(), "jenkins.branch.OrganizationFolder");
        Assert.assertEquals(projectResponse.getDescription(), DESCRIPTION);
        Assert.assertEquals(projectResponse.getDisplayName(), DISPLAY_NAME_ORG_FOLDER);

    }

    @Test(dependsOnMethods = "testChangeProjectConfigurations")
    @Feature("Organization folder")
    @Description("06.005.01 Rename Organization folder with valid name")
    public void testRename() {
        given()
                .spec(requestSpec())
                .contentType("application/x-www-form-urlencoded")
                .formParam("newName", RENAMED_ORGANIZATION_FOLDER_NAME)
                .when()
                .post("job/%s/confirmRename".formatted(ORGANIZATION_FOLDER_NAME))
                .then()
                .spec(responseSpec(302, 500L))
                .extract().response();

        Response response = requestProjectJson(RENAMED_ORGANIZATION_FOLDER_NAME);

        ProjectResponse projectResponse = response.as(ProjectResponse.class);

        Assert.assertTrue(response.getTime() <= 500);
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json;charset=utf-8");

        Assert.assertEquals(projectResponse.getName(), RENAMED_ORGANIZATION_FOLDER_NAME);
        Assert.assertEquals(projectResponse.get_class(), "jenkins.branch.OrganizationFolder");
        Assert.assertEquals(projectResponse.getDisplayName(), DISPLAY_NAME_ORG_FOLDER);
    }

    @Test(dependsOnMethods = "testRename")
    @Feature("Organization folder")
    @Description("06.005.02 Delete Organization folder")
    public void testDelete() {
        given()
                .spec(requestSpec())
                .when()
                .delete("job/%s/".formatted(RENAMED_ORGANIZATION_FOLDER_NAME))
                .then()
                .spec(responseSpec(204, 500L))
                .extract().response();

        given()
                .spec(requestSpec())
                .when()
                .get("job/%s/api/json".formatted(RENAMED_ORGANIZATION_FOLDER_NAME))
                .then()
                .spec(responseSpec(404, 500L));
    }
}
