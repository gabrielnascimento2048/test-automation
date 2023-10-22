package com.automation.backend;

import com.automation.helpers.BackendBaseTest;
import com.automation.models.BasicAuthentication;
import com.automation.models.Register;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import com.github.javafaker.Faker;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.SAME_THREAD)
public class GetAllEmployeesTest {

    public static String token;
    Faker faker = new Faker();


    @Test
    public void shouldRegisterNewEmployees() {
        String fakeEmail = faker.internet().emailAddress();
        String fakePassword = faker.internet().password();
        String fakeRole = faker.options().option("write", "read", "admin");

        //given
        Register registerRequest = Register.builder()
                .email(fakeEmail)
                .password(fakePassword)
                .role(fakeRole)
                .build();
        Response response = BackendBaseTest.apiClient.createNewUser(registerRequest);

        //when
        response.then().statusCode(201);

        //then
        assert response.jsonPath().get("success").equals(true);
        assert response.jsonPath().get("message").equals("created");
    }

    @Order(1)
    @Test
    public void shouldLoginUser() {
        //given
        BasicAuthentication basicAuthentication = BasicAuthentication.builder()
                .username("gabriel2.test@gmail.com")
                .password("12345")
                .build();
        //when
        Response response = BackendBaseTest.apiClient.postLogin(basicAuthentication);

        //then
        response.then().statusCode(201);
        assert response.jsonPath().getString("success").contains("true");
        assert response.jsonPath().getString("token").length() > 0;
        token = response.jsonPath().getString("token");


    }

    @Order(2)
    @Test
    public void shouldGetAllEmployees() {
        //given
        Response response = BackendBaseTest.apiClient.getAllEmployees(token);

        //when
        response.then().statusCode(200);

        //then
        assert response.jsonPath().get("employees[0].email").equals("john.doe@test.com");
        assert response.jsonPath().get("employees[0].first_name").equals("John");
        assert response.jsonPath().get("employees[0].last_name").equals("Doe");
    }

}
