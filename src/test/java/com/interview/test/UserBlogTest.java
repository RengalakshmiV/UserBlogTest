package com.interview.test;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertTrue;

import com.jayway.restassured.RestAssured;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserBlogTest {
    /**
     * setup for
     */
    @Before
    public void setUp() throws Exception {
        try {


            RestAssured.port = port;
            RestAssured.useRelaxedHTTPSValidation();
            RestAssured.config().getSSLConfig().with().keystore("classpath:keystore.p12", "password");
            RestAssured.baseURI = "https://jsonplaceholder.typicode.com/users";
            RequestSpecification httpRequest = RestAssured.given();
        } catch (Exception ex) {
            System.out.println("Error while loading keystore >>>>>>>>>");
            ex.printStackTrace();
        }
    }


    /**
     *
     */
    @Test
    public void basicPingTest() {
        given().when().get("https://jsonplaceholder.typicode.com/users").then().statusCode(200);
    }


    /**
     *
     */
    @Test
    public void testUserExistence() {
        when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .assertThat()
                .body("username", hasItems("Samantha"));
        assertTrue(true);
    }

    /**
     *
     * @return
     */
    @Test
    public void testPostExistenceForUser() {

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/posts";
        RequestSpecification request = RestAssured.given();

        Response response = request.get();
        System.out.println("Response Body -> " + response.body().asString());
        System.out.println("" + response.path("userId"));
        List <Integer> noOfPosts = response.path("userId");
        int size = response.jsonPath().getList("userId").size();
        System.out.println("size ->" +size);



    }
    /**
     *
     */

    @Test
    public void postsForSamantha(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/posts";
        RequestSpecification request = RestAssured.given();
        Response response = RestAssured.given()
                .queryParam("userId", 3)
                .get(baseURI);
        assertTrue(response.asString().length() > 0);

        System.out.println("response -> " +response.asString());
    }

    @Test
    public void testValidComments() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/comments";
        RequestSpecification request = RestAssured.given();
        Response response = RestAssured.given()
                .queryParam("postId", 21)
                .get(baseURI);
        assertTrue(response.asString().length() > 0);

        System.out.println("response -> " +response.asString());
    }

    /**
     *
     */
    @Test
    public void testValidEmails() {
        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/comments";
        RequestSpecification request = RestAssured.given();
        Response response = RestAssured.given()
                .queryParam("postId", 30)
                .get(baseURI);

        List <String> emails = response.path("email");
        assertTrue(response.asString().length() > 0);

        for(String email : emails){
            Matcher matcher = pattern.matcher(email);
            System.out.println(email +" : "+ matcher.matches());
        }

        System.out.println("response -> " +emails.get(0));
    }
}
