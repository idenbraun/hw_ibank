package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationDto;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setUp() {
        Configuration.baseUrl = "http://localhost:9999";
        open("/");
    }

    private void login(String login, String password) {
        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();
    }

    @Test
    void shouldLoginWithActiveRegisteredUser() {
        RegistrationDto user = DataGenerator.registerUser("active");

        login(user.getLogin(), user.getPassword());

        $("[test-data-id='dashboard']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Личный кабинет"));
}

    @Test
    void shouldShowErrorForBlockedUser() {
        RegistrationDto user = DataGenerator.registerUser("blocked");
        login(user.getLogin(), user.getPassword());

        $("[data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldShowErrorForWrongPassword() {
        RegistrationDto user = DataGenerator.registerUser("active");
        String wrongPassword = DataGenerator.generatePassword();

        login(user.getLogin(), wrongPassword);

        $("[data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowErrorForWrongLogin() {
        RegistrationDto user = DataGenerator.registerUser("active");
        String wrongLogin = DataGenerator.generateLogin();

        login(wrongLogin, user.getPassword());

        $("[data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowErrorForNotRegisteredUser() {
        String randomLogin = DataGenerator.generateLogin();
        String randomPassword = DataGenerator.generatePassword();

        login(randomLogin, randomPassword);

        $("[data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
