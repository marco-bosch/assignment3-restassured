package assignment3.restassured.git.tests;

import static io.restassured.RestAssured.given;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.google.gson.internal.LinkedTreeMap;

import assignment3.restassured.git.APIEndpoints;
import assignment3.restassured.git.JsonPaths;
import assignment3.restassured.git.utils.DataUtils;
import assignment3.restassured.git.utils.TestUtils;
import io.restassured.response.Response;

public class APITests extends BaseAPITest {

	String authToken = "token not found";
	String Email = null;
	String Message = "Logged out successfully";

	@Test(priority = 1)
	public void signUpAPI() {

		LinkedTreeMap<String, Object> signUpMap = TestUtils.convertJsonToMap(DataUtils
				.getDataFromExcel("Payload", "SignUpAPI").replace("uniqueEmail", faker.name().username() + "@gmail.com")
				.replace("uniquePhone", "+62-81215685596"));

		Response responseSignUpAPI = given().spec(commonSpec).body(signUpMap).when().post(APIEndpoints.signUpAPI);

		verifyAPIStatusTimeAndHeader(responseSignUpAPI, 200);

		authToken = getDataFromResponseUsingJsonPath(responseSignUpAPI, JsonPaths.authToken);
		System.out.println(authToken);

		Email = getDataFromResponseUsingJsonPath(responseSignUpAPI, JsonPaths.email);
		System.out.println(Email);

	}

	@Test(priority = 2)
	public void profileAPI() {

		Response responseProfileAPI = given().spec(commonSpec).header("authtoken", authToken).when()
				.get(APIEndpoints.profileAPI);

		System.out.println(authToken);

		verifyAPIStatusTimeAndHeader(responseProfileAPI, 200);

		String verifyEmail = getDataFromResponseUsingJsonPath(responseProfileAPI, "email");
		AssertJUnit.assertEquals(verifyEmail, Email);

	}

	@Test(priority = 3)
	public void logOutAPI() {

		Response responseLogOutAPI = given().spec(commonSpec).param("authtoken", authToken).when()
				.delete(APIEndpoints.logOutAPI);

		System.out.println(authToken);

		verifyAPIStatusTimeAndHeader(responseLogOutAPI, 200);

		String verifyResponseMessage = getDataFromResponseUsingJsonPath(responseLogOutAPI, "message");
		AssertJUnit.assertEquals(verifyResponseMessage, Message);

	}

}
