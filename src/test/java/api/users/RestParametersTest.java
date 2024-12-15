package api.users;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class RestParametersTest {

    @BeforeMethod
    public void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri("https://reqres.in")
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    @DataProvider
    public Object[][] userParametersDataProvide() {
        return new Object[][]{
                {1, "george.bluth@reqres.in", "George", "Bluth"},
                {8, "lindsay.ferguson@reqres.in", "Lindsay", "Ferguson"},
                {12, "rachel.howell@reqres.in", "Rachel", "Howell"}
        };
    }

    @Test(dataProvider = "userParametersDataProvide")
    public void getUsersTest(Integer userId, String email, String firstName, String lastName) {

        given()
                .spec(requestSpecification)
                .pathParam("userId", userId)

                .when()
                .get("/api/users/{userId}")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_OK)
                .body("data.id", equalTo(userId))
                .body("data.first_name", equalTo(firstName))
                .body("data.last_name", equalTo(lastName))
                .body("data.email", equalTo(email));
    }
}