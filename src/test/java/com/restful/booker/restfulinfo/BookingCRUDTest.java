package com.restful.booker.restfulinfo;

import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import javafx.scene.layout.Priority;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class BookingCRUDTest extends TestBase {

    static String token;

    static String userName = "admin";
    static String password = "password123";

    static String firstName = "Prime" + TestUtils.getRandomValue();
    static String lastName = "testing";
    static int totalPrice = 101;
    static boolean depositPaid = true;
    static HashMap<String, String> bookingDates;
    static String additionalNeeds = "vegeterian";
    static int bookingId;
    static int bookingId1 = 12;
    @Steps
    BookingSteps bookingSteps;

    @Steps
    AuthSteps authSteps;

    @Title("This will create token")
    @Test()
    public void test001() {
        ValidatableResponse response = authSteps.getToken(userName, password).statusCode(200);
        token = response.extract().path("token");
    }

    @Title("This will create Booking")
    @Test
    public void test002() {

        HashMap<String, String> dates = new HashMap<>();
        dates.put("checkin", "2023-01-01");
        dates.put("checkout", "2023-02-02");
        ValidatableResponse response = bookingSteps.createUser(firstName, lastName, totalPrice, depositPaid, dates, additionalNeeds);
        response.log().all().statusCode(200);
        bookingId = response.extract().path("bookingid");
    }

    @Title("Verify booking is created ")
    @Test()
    public void test003() {
        ValidatableResponse response = bookingSteps.getBookingByID(bookingId, token).statusCode(200);
        ArrayList<?> booking = response.extract().path("bookingid");

        Assert.assertThat(booking, contains(bookingId));
        //System.out.println(booking);
    }

    @Title("Update booking information")
    @Test
    public void test004() {
        firstName = firstName + "_add";

        HashMap<String, String> dates = new HashMap<>();
        dates.put("checkin", "2023-01-01");
        dates.put("checkout", "2024-02-02");
        bookingSteps.updateBookingById(bookingId, firstName, lastName, totalPrice, depositPaid, dates, additionalNeeds, token).log().all().statusCode(200);
    }

    @Title("Delete booking and verify that booking deleted ")
    @Test
    public void test005() {
        bookingSteps.deleteBooking(bookingId, token).statusCode(201);
        bookingSteps.getBooking(bookingId, token).statusCode(404);

    }
}