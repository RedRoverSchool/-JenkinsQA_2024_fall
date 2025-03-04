package school.redrover.runner;

import io.qameta.allure.httpclient.AllureHttpClientRequest;
import io.qameta.allure.httpclient.AllureHttpClientResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseAPIHttpTest {

    protected String crumb;
    protected String crumbRequestField;
    protected static String token;

    protected static final Logger logger = LoggerFactory.getLogger(BaseAPIHttpTest.class);

    protected static String getBasicAuthWithPassword() {
        String auth = ProjectUtils.getUserName() + ":" + ProjectUtils.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    protected static String getBasicAuthWithToken() {
        String auth = ProjectUtils.getUserName() + ":" + token;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    protected static CloseableHttpClient createHttpClientWithPasswordAuthAndAllureLogging() {
        return HttpClientBuilder.create()
                .addInterceptorFirst(new AllureHttpClientRequest())
                .addInterceptorLast(new AllureHttpClientResponse())
                .addInterceptorFirst((HttpRequestInterceptor) (request, context) ->
                        request.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithPassword()))
                .build();
    }

    protected static CloseableHttpClient createHttpClientWithTokenAuthAndAllureLogging() {
        return HttpClientBuilder.create()
                .addInterceptorFirst(new AllureHttpClientRequest())
                .addInterceptorLast(new AllureHttpClientResponse())
                .addInterceptorFirst((HttpRequestInterceptor) (request, context) ->
                        request.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthWithToken()))
                .build();
    }

    @BeforeMethod
    protected void setUp() {
        try (CloseableHttpClient httpClient = createHttpClientWithPasswordAuthAndAllureLogging()) {
            HttpGet httpGet = new HttpGet(ProjectUtils.getUrl() + "crumbIssuer/api/json");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String jsonString = EntityUtils.toString(response.getEntity());
                Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
                Assert.assertNotNull(jsonString);

                JSONObject jsonResponse = new JSONObject(jsonString);
                crumb = jsonResponse.getString("crumb");
                crumbRequestField = jsonResponse.getString("crumbRequestField");

                Assert.assertNotNull(crumb);
                Assert.assertNotNull(crumbRequestField);

                HttpPost httpPost = new HttpPost(
                        ProjectUtils.getUrl() + "me/descriptorByName/jenkins.security.ApiTokenProperty/generateNewToken");
                httpPost.addHeader("Jenkins-Crumb", crumb);

                try (CloseableHttpResponse postResponse = httpClient.execute(httpPost)) {
                    Assert.assertEquals(postResponse.getStatusLine().getStatusCode(), 200);

                    String postResponseBody = EntityUtils.toString(postResponse.getEntity(), StandardCharsets.UTF_8);
                    JSONObject postJsonResponse = new JSONObject(postResponseBody);
                    token = postJsonResponse.getJSONObject("data").getString("tokenValue");

                }
            }
        } catch (IOException e) {
            logger.error("IOException occurred while getting token: ", e);
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    protected String getItemByNameURL(String name) {
        return ProjectUtils.getUrl() + String.format("job/%s/api/json", TestUtils.encodeParam(name));
    }

    protected String getCreateItemURL() {
        return ProjectUtils.getUrl() + "createItem";
    }

    protected String getRenameItemURL(String name) {
        return ProjectUtils.getUrl() + String.format("job/%s/confirmRename", TestUtils.encodeParam(name));
    }

    protected String getAddDescriptionURL(String name) {
        return ProjectUtils.getUrl() + String.format("job/%s/submitDescription", TestUtils.encodeParam(name));
    }

    protected String getDeleteItemURL(String name) {
        return ProjectUtils.getUrl() + String.format("job/%s/", TestUtils.encodeParam(name));
    }

    protected String getAllProjectList() {
        return ProjectUtils.getUrl() + "api/json";
    }

    protected String getCreateItemBody(String name, String mode) {
        return "name=" + TestUtils.encodeParam(name) + "&mode=" + mode;
    }

    protected String getCreateItemCopyFromBody(String name, String nameFrom) {
        return "name=" + TestUtils.encodeParam(name) + "&mode=copy" + "&from=" + nameFrom;
    }

    protected String getRenameItemBody(String name) {
        return "newName=" + TestUtils.encodeParam(name);
    }

    protected String getAddDescriptionBody(String description) {
        return "description=" + TestUtils.encodeParam(description);
    }
}

