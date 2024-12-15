package api.users;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestTest {

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

    @Test
    public void UserCreation() {
        Faker faker = new Faker();

        var name = faker.name().fullName();
        var job = faker.job().position();

        given()
                .spec(requestSpecification)
                .body("{\n"
                        + "\"name\":\"" + name + "\",\n"
                        + "\"job\":\"" + job + "\"\n"
                        + "}")

                .when()
                .post("/api/users")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_CREATED)
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .body("id", matchesRegex("\\d+"))
                .body("createdAt", notNullValue());
    }

    @Test
    public void getSingleUserTest() {

        given()
                .spec(requestSpecification)

                .when()
                .get("/api/users/4")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_OK)
//                .statusCode(200)
                .body("data.id", equalTo(4))
                .body("data.first_name", equalTo("Eve"))
                .body("data.last_name", equalTo("Holt"))
                .body("data.email", equalTo("eve.holt@reqres.in"))
                .body("support.text", equalTo("Tired of writing endless social media content? Let Content Caddy generate it for you."));
    }

    @Test
    public void getUserListTest() {

        given()
                .spec(requestSpecification)

                .when()
                .get("/api/users")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getNotExistingUserTest() {

        given()
                .spec(requestSpecification)

                .when()
                .get("/api/users/278")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .statusLine("HTTP/1.1 404 Not Found");
    }

    @Test
    public void invalidUserCreation() {
        Faker faker = new Faker();

        var name = faker.name().fullName();
        var job = faker.job().position();

        given()
                .spec(requestSpecification)
                .body("{\n"
                        + "\"name\":\"" + name + "\",\n"
                        + "\"job\":\"" + job + "\",\n"
                        + "}")

                .when()
                .post("/api/users")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .statusLine("HTTP/1.1 400 Bad Request");
    }

    @Test
    public void updateUserTest() {
        Faker faker = new Faker();

        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();

        given()
                .spec(requestSpecification)
                .body("{\n"
                        + "\"first_name\":\"" + firstName + "\",\n"
                        + "\"last_name\":\"" + lastName + "\"\n"
                        + "}")

                .when()
                .put("/api/users/2")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_OK)
                .body("first_name", equalTo(firstName))
                .body("last_name", equalTo(lastName));
    }

    @Test
    public void deleteUserTest() {

        given()
                .spec(requestSpecification)

                .when()
                .delete("/api/users/3")

                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}