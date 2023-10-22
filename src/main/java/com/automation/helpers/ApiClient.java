package com.automation.helpers;

import com.automation.models.BasicAuthentication;
import com.automation.models.Register;
import io.restassured.response.Response;

import java.util.Base64;

import static com.automation.helpers.Endpoints.*;
import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final String BASE_URL_BE = "http://localhost:5000/api/";

    public Response getAllEmployees(String accessToken) {
        return given()
                .baseUri(BASE_URL_BE)
                .header("accessToken", accessToken)
                .when()
                .get(ALL_EMPLOYEES);
    }

    public Response createNewUser(Register registerRequest) {
        return given()
                .baseUri(BASE_URL_BE)
                .multiPart("email", registerRequest.getEmail())
                .multiPart("password", registerRequest.getPassword())
                .multiPart("role", registerRequest.getRole())
                .when()
                .post(REGISTER);
    }


    public Response postLogin(BasicAuthentication basicAuthentication) {
        String base64Credentials = Base64.getEncoder().encodeToString((basicAuthentication.getUsername() + ":" + basicAuthentication.getPassword()).getBytes());
        return given()
                .baseUri(BASE_URL_BE)
                .header("Authorization", "Basic " + base64Credentials)
                .when()
                .post(LOGIN);
    }


}
