package school.redrover;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseAPIHttpTest;
import school.redrover.runner.ProjectUtils;
import school.redrover.runner.TestUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Epic("Http API  Requests")
public class APIHttpTest extends BaseAPIHttpTest {  // Using Apache HttpClient

    private static final String PIPELINE_NAME = "Pipeline";
    private static final String PIPELINE_NAME_BY_XML_CREATED = "PipelineXML";
    private static final String FOLDER_NAME_BY_XML_CREATED = "FolderXML";
    private static final String FOLDER_NAME = "Folder";
    private static final String FOLDER_NEW_NAME = "NewFolderName";
    private static final String FOLDER_MODE = "com.cloudbees.hudson.plugins.folder.Folder";
    private static final String FREESTYLE_PROJECT = "NewProject";
    private static final String RENAMED_FREESTYLE_PROJECT = "RenamedFreestyle";
    private static final String MULTIBRANCH_PIPELINE_NAME = "MultibranchPipeline";
    private static final String MULTIBRANCH_PIPELINE_NAME_XML = "MultibranchPipelineXML";
    private static final String VIEW_NAME = "ViewName";
    private static final String API_JSON_URL = "api/json?pretty=true";

    private List<String> getProjectNamesFromJsonResponseList(String url, String jsonArrayKey) throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpGet httpGet = new HttpGet(url);

            httpGet.addHeader("Authorization", getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JsonArray jobs = new Gson().fromJson(responseBody, JsonObject.class).getAsJsonArray(jsonArrayKey);

                List<String> projectNames = new ArrayList<>();
                jobs.forEach(job -> projectNames.add(job.getAsJsonObject().get("name").getAsString()));

                return projectNames;
            }
        }
    }

    private List<String> getAllProjectNamesFromJsonResponseList() throws IOException {
        return getProjectNamesFromJsonResponseList(ProjectUtils.getUrl() + API_JSON_URL, "jobs");
    }

    private List<String> getAllProjectViewNamesFromJsonResponseList() throws IOException {
        return getProjectNamesFromJsonResponseList(ProjectUtils.getUrl() + API_JSON_URL, "views");
    }

    @Test
    @Story("Pipeline project")
    @Description("Create Pipeline Project with valid name")
    public void testCreatePipeline() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpPost httpPost = new HttpPost(ProjectUtils.getUrl() + "view/all/createItem/");

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("name", PIPELINE_NAME));
            nameValuePairs.add(new BasicNameValuePair("mode", "org.jenkinsci.plugins.workflow.job.WorkflowJob"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 302);
                Assert.assertListContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        PIPELINE_NAME,
                        "The project is not created");
            }
        }
    }

    @Test(dependsOnMethods = "testCreatePipeline")
    @Story("View")
    @Description("Add List View for project")
    public void testAddListViewForProject() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            String query = "name=" + VIEW_NAME;
            String payloadForProject = String.format(TestUtils.loadPayload("create-list-view.xml"), PIPELINE_NAME);

            HttpPost httpPost = new HttpPost(ProjectUtils.getUrl() + "createView?" + query);

            httpPost.setEntity(new StringEntity(payloadForProject));
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml ");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
                Assert.assertListContainsObject(
                        getAllProjectViewNamesFromJsonResponseList(),
                        VIEW_NAME,
                        "View is not created");
            }
        }
    }

    @Test(dependsOnMethods = "testAddListViewForProject")
    @Story("View")
    @Description("Delete List View")
    public void testDeleteListView() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpPost httpPost = new HttpPost(String.format(ProjectUtils.getUrl() + "view/%s/doDelete/", VIEW_NAME));
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 302);
                Assert.assertListNotContainsObject(
                        getAllProjectViewNamesFromJsonResponseList(),
                        VIEW_NAME,
                        "View is not deleted");
            }
        }
    }

    @Test
    @Story("Pipeline project")
    @Description("Create Pipeline Project with valid name(XML")
    public void testCreatePipelineXML() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            String queryString = "name=" + PIPELINE_NAME_BY_XML_CREATED;

            HttpPost httpPost = new HttpPost(ProjectUtils.getUrl() + "view/all/createItem?" + queryString);
            httpPost.setEntity(new StringEntity(TestUtils.loadPayload("create-empty-pipeline-project.xml")));

            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
                Assert.assertListContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        PIPELINE_NAME_BY_XML_CREATED,
                        "The project is not created");
            }
        }
    }

    @Test(dependsOnMethods = "testCreatePipelineXML")
    @Story("Pipeline project")
    @Description("Add description to Pipeline project")
    public void testAddDescription() throws IOException {
        final String description = "This is a description";

        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpPost httpPost = new HttpPost(
                    String.format(ProjectUtils.getUrl() + "job/%s/submitDescription", PIPELINE_NAME_BY_XML_CREATED));

            httpPost.setEntity(new StringEntity("description=" + TestUtils.encodeParam(description)));

            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 302);
            }
            HttpGet httpGet = new HttpGet(
                    String.format(ProjectUtils.getUrl() + "job/%s/api/json?pretty=true", PIPELINE_NAME_BY_XML_CREATED));
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
                Assert.assertTrue(jsonResponse.contains("\"description\" : \"" + description + "\""),
                        "Description not found in job details");
            }
        }
    }

    @Test(dependsOnMethods = "testAddDescription")
    @Story("Pipeline project")
    @Description("Delete project by HttpDelete request")
    public void testDeleteProjectByHttpDelete() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpDelete httpDelete = new HttpDelete(
                    String.format(ProjectUtils.getUrl() + "/job/%s/", PIPELINE_NAME_BY_XML_CREATED));

            httpDelete.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 204);
                Assert.assertListNotContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        PIPELINE_NAME_BY_XML_CREATED,
                        "Project is not deleted");
            }
        }
    }

    @Test(dependsOnMethods = "testDeleteListView")
    @Story("Pipeline project")
    @Description("Delete project by HttpPost request")
    public void testDeletePipelineByHttpPost() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpPost httpPost = new HttpPost(
                    String.format(ProjectUtils.getUrl() + "/job/%s/doDelete", PIPELINE_NAME));
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 302);
            }

            HttpGet httpGet = new HttpGet(
                    String.format(ProjectUtils.getUrl() + "/job/%s/api/json", PIPELINE_NAME));
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 404);
                Assert.assertListNotContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        PIPELINE_NAME,
                        "Project is not deleted");
            }
        }
    }

    @Test
    @Story("Folder")
    @Description("003 Create Folder with valid name (XML)")
    public void testCreateFolderWithValidNameXML() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            String queryString = "name=" + FOLDER_NAME_BY_XML_CREATED;

            HttpPost httpPost = new HttpPost(ProjectUtils.getUrl() + "/view/all/createItem?" + queryString);

            httpPost.setEntity(new StringEntity(TestUtils.loadPayload("create-empty-folder.xml")));
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            Allure.step("Send POST request -> Create Folder");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Allure.step("Expected result: Create item status code is 200");
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

                Allure.step(String.format("Expected result: '%s' is displayed on Dashboard", FOLDER_NAME_BY_XML_CREATED));
                Assert.assertListContainsObject(getAllProjectNamesFromJsonResponseList(), FOLDER_NAME_BY_XML_CREATED,
                        "The folder is not created");
            }

            HttpGet httpGet = new HttpGet(String.format(ProjectUtils.getUrl() + "job/%s/api/json", FOLDER_NAME_BY_XML_CREATED));
            httpGet.addHeader("Authorization", getBasicAuthWithToken());

            Allure.step("Send GET request -> Get item by name");
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(responseBody);
                String actualFullName = jsonResponse.getString("fullName");

                Allure.step("Expected result: Status code is 200");
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

                Allure.step("Expected result: fullName in json is " + FOLDER_NAME_BY_XML_CREATED);
                Assert.assertEquals(FOLDER_NAME_BY_XML_CREATED, actualFullName, "Folder didn't find");

                Allure.step("Expected result: Header 'Content-Type : application/json;charset=utf-8' ");
                Assert.assertEquals(response.getFirstHeader("Content-Type").getValue(), "application/json;charset=utf-8");
            }
        }
    }

    @Test
    @Story("Folder")
    @Description("002 Create Folder with valid name")
    public void testCreateFolderWithValidName() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {

            HttpPost postCreateItem = new HttpPost(ProjectUtils.getUrl() + "createItem");
            postCreateItem.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());
            postCreateItem.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            postCreateItem.setEntity(new StringEntity("name=" + FOLDER_NAME + "&mode=" + FOLDER_MODE));

            Allure.step("Send POST request -> Create Folder");
            try (CloseableHttpResponse postCreateItemResponse = httpClient.execute(postCreateItem)) {
                Allure.step("Expected result: Successful item creation. Status code 302");
                Assert.assertEquals(postCreateItemResponse.getStatusLine().getStatusCode(), 302);
            }

            Allure.step("Send GET request -> Get item by name");
            HttpGet getItemByName = new HttpGet(ProjectUtils.getUrl() +
                    String.format("job/%s", URLEncoder.encode(FOLDER_NAME, StandardCharsets.UTF_8)) + "/api/json");
            getItemByName.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());
            try (CloseableHttpResponse getItemByNameResponse = httpClient.execute(getItemByName)) {
                Allure.step("Expected result: Created element is found by name");
                Assert.assertEquals(getItemByNameResponse.getStatusLine().getStatusCode(), 200);

                String jsonResponse = EntityUtils.toString(getItemByNameResponse.getEntity());

                Allure.step(String.format("Expected result: fullName is '%s' response", FOLDER_NAME));
                Assert.assertTrue(jsonResponse.contains(String.format("\"fullName\":\"%s\"", FOLDER_NAME)), "Folder not found");
                Allure.step("Expected result: description is null");
                Assert.assertTrue(jsonResponse.contains("\"description\":null"));
            }
        }
    }

    @Test(dependsOnMethods = "testCreateFolderWithValidName")
    @Story("Folder")
    @Description("008 Rename Folder")
    public void testRenameFolder() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpPost postRenameItem = new HttpPost(ProjectUtils.getUrl() +
                    String.format("job/%s/confirmRename", TestUtils.encodeParam(FOLDER_NAME)));
            postRenameItem.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());
            postRenameItem.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            postRenameItem.setEntity(new StringEntity("newName=" + TestUtils.encodeParam(FOLDER_NEW_NAME)));

            Allure.step("Send POST request -> Rename Folder");
            try (CloseableHttpResponse postRenameItemResponse = httpClient.execute(postRenameItem)) {
                Allure.step("Expected result: Item renamed successful. Status code 302");
                Assert.assertEquals(postRenameItemResponse.getStatusLine().getStatusCode(), 302);

                Allure.step(String.format("Expected result: '%s' is displayed on Dashboard", FOLDER_NEW_NAME));
                Assert.assertListContainsObject(getAllProjectNamesFromJsonResponseList(), FOLDER_NEW_NAME, "List is not contain folder");
            }
        }
    }

    @Test()
    @Story("Folder")
    @Description("007 Add Description to Folder")
    public void testAddDescriptionToFolder() throws IOException {
        String description = "Add description to folder!";
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {

            HttpPost postCreateItem = new HttpPost(ProjectUtils.getUrl() + "createItem");
            postCreateItem.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());
            postCreateItem.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            postCreateItem.setEntity(new StringEntity("name=" + FOLDER_NAME + "&mode=" + FOLDER_MODE));

            Allure.step("Send POST request -> Create Folder");
            try (CloseableHttpResponse postCreateItemResponse = httpClient.execute(postCreateItem)) {
                Allure.step("Expected result: Successful item creation. Status code 302");
                Assert.assertEquals(postCreateItemResponse.getStatusLine().getStatusCode(), 302);
            }

            HttpPost postAddDescriptionItem = new HttpPost(ProjectUtils.getUrl() +
                    String.format("job/%s/submitDescription", TestUtils.encodeParam(FOLDER_NAME)));
            postAddDescriptionItem.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());
            postAddDescriptionItem.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            postAddDescriptionItem.setEntity(new StringEntity("description=" + TestUtils.encodeParam(description)));

            Allure.step("Send POST request -> Add Description to Folder");
            try (CloseableHttpResponse postAddDescriptionResponse = httpClient.execute(postAddDescriptionItem)) {
                Allure.step("Expected result: Successful add description to item. Status code 302");
                Assert.assertEquals(postAddDescriptionResponse.getStatusLine().getStatusCode(), 302);
            }

            Allure.step("Send GET request -> Get item by name");
            HttpGet getItemByName = new HttpGet(ProjectUtils.getUrl() +
                    String.format("job/%s", TestUtils.encodeParam(FOLDER_NAME)) + "/api/json");
            getItemByName.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());
            try (CloseableHttpResponse getItemByNameResponse = httpClient.execute(getItemByName)) {
                Allure.step("Expected result: Created element is found by name");
                Assert.assertEquals(getItemByNameResponse.getStatusLine().getStatusCode(), 200);

                String jsonResponse = EntityUtils.toString(getItemByNameResponse.getEntity());
                Allure.step("Expected result: Response body contains 'description: null' for folder(ERR)");
                Assert.assertTrue(jsonResponse.contains("\"description\":null"), "Description is not null");
            }
            Allure.step("Send DELETE request -> Delete Folder");
            HttpDelete deleteFolder = new HttpDelete(ProjectUtils.getUrl() + String.format("job/%s/", TestUtils.encodeParam(FOLDER_NAME)));
            deleteFolder.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());
            try (CloseableHttpResponse deleteFolderResponse = httpClient.execute(deleteFolder)) {
                Allure.step("Expected result: Delete item successful. Status code is 204");
                Assert.assertEquals(deleteFolderResponse.getStatusLine().getStatusCode(), 204);
            }
        }
    }

    @Test(dependsOnMethods = "testCreateFolderWithValidNameXML")
    @Story("Folder")
    @Description("004 Delete Folder")
    public void testDeleteFolder() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {

            HttpDelete httpDelete = new HttpDelete(String.format(ProjectUtils.getUrl() + "job/%s/", FOLDER_NAME_BY_XML_CREATED));
            httpDelete.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            Allure.step("Send DELETE request -> Delete Folder");
            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {

                Allure.step("Expected result: Delete item status code is 204");
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 204);

                Allure.step(String.format("Expected result: '%s' is not displayed on Dashboard", FOLDER_NAME_BY_XML_CREATED));
                Assert.assertListNotContainsObject(getAllProjectNamesFromJsonResponseList(), FOLDER_NAME_BY_XML_CREATED,
                        "The folder is not deleted");
            }
            HttpGet httpGet = new HttpGet(String.format(ProjectUtils.getUrl() + "job/%s/api/json", FOLDER_NAME_BY_XML_CREATED));
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            Allure.step("Send GET request -> Get item by name");
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

                Allure.step("Expected result: Item not found. Status code is 404");
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 404);
            }
        }
    }

    @Story("Freestyle Project")
    @Description("Create Freestyle Project with valid name")
    @Test
    public void testCreateFreestyleProject() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            String query = "name=" + TestUtils.encodeParam(FREESTYLE_PROJECT);

            HttpPost httpPost = new HttpPost(ProjectUtils.getUrl() + "view/all/createItem?" + query);
            httpPost.setEntity(new StringEntity(TestUtils.loadPayload("create-empty-freestyle-project.xml")));

            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
                Assert.assertListContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        FREESTYLE_PROJECT,
                        "The project is not created");
            }
        }
    }

    @Story("Freestyle Project")
    @Description("Rename Freestyle Project with valid name")
    @Test(dependsOnMethods = "testCreateFreestyleProject")
    public void testRenameFreestyleProject() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            String renameUrl = ProjectUtils.getUrl() + "job/" + FREESTYLE_PROJECT + "/doRename";

            HttpPost httpPost = new HttpPost(renameUrl);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("newName", RENAMED_FREESTYLE_PROJECT));
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 302);

                Assert.assertListContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        RENAMED_FREESTYLE_PROJECT,
                        "The project was not renamed");
            }
        }
    }

    @Story("Freestyle Project")
    @Description("Delete Freestyle project")
    @Test(dependsOnMethods = "testRenameFreestyleProject")
    public void testDeleteFreestyleProject() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            String deleteUrl = ProjectUtils.getUrl() + "job/" + RENAMED_FREESTYLE_PROJECT + "/";

            HttpDelete httpDelete = new HttpDelete(deleteUrl);

            httpDelete.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            httpDelete.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 204);
            }

            HttpGet httpGet = new HttpGet(ProjectUtils.getUrl() + "job/" + RENAMED_FREESTYLE_PROJECT + "/api/json");
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 404);
                Assert.assertListNotContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        RENAMED_FREESTYLE_PROJECT,
                        "The project was not deleted");

            }
        }
    }

    @Story("Multibranch pipeline")
    @Description("Create Multibranch pipeline with valid name")
    @Test
    public void testCreateMultibranchPipeline() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            HttpPost httpPost = new HttpPost(ProjectUtils.getUrl() + "view/all/createItem");

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("name", MULTIBRANCH_PIPELINE_NAME));
            nameValuePairs.add(new BasicNameValuePair("mode", "org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 302);
                Assert.assertListContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        MULTIBRANCH_PIPELINE_NAME,
                        "The project is not created");
            }
        }
    }

    @Story("Multibranch pipeline")
    @Description("Create Multibranch pipeline with valid name using XML")
    @Test
    public void testCreateMultibranchPipelineXML() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithAllureLogging()) {
            String queryString = "name=" + TestUtils.encodeParam(MULTIBRANCH_PIPELINE_NAME_XML);

            HttpPost httpPost = new HttpPost(ProjectUtils.getUrl() + "view/all/createItem?" + queryString);
            httpPost.setEntity(new StringEntity(TestUtils.loadPayload("create-empty-multibranch-pipeline.xml")));

            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
                Assert.assertListContainsObject(
                        getAllProjectNamesFromJsonResponseList(),
                        MULTIBRANCH_PIPELINE_NAME_XML,
                        "The project is not created"
                );
            }
        }
    }
}