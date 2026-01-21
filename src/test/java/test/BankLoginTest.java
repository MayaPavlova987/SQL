package test;

import data.DataHelper;
import data.SQLHelper;
import org.junit.jupiter.api.*;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.SQLHelper.*;

public class BankLoginTest {
    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoWithTestData();

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist user from data")
    void shouldLoginSuccessfullyWithCodeFromDatabase() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error if user is not exist in base")
    void shouldGetErrorIfRandomUserWhichNotIntoBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification with invalid login")
    void shouldGetErrorNotificationWithInvalidLogin() {
        var invalidAuthInfo = DataHelper.getAuthInfoWithInvalidLogin();
        loginPage.login(invalidAuthInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification with invalid password")
    void shouldGetErrorNotificationWithInvalidPassword() {
        var invalidAuthInfo = DataHelper.getAuthInfoWithInvalidPassword();
        loginPage.login(invalidAuthInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }

}