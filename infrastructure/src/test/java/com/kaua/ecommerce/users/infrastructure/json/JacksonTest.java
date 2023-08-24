package com.kaua.ecommerce.users.infrastructure.json;

import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.configurations.ObjectMapperConfig;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class JacksonTest {

    @Test
    void testMarshall() {
        final var aFirstName = "teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";

        final var aCreateAccountApiInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        final var actualJson = Json.writeValueAsString(aCreateAccountApiInput);

        Assertions.assertNotNull(actualJson);
    }

    @Test
    void testUnarshall() {
        final var aFirstName = "teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";

        final var json = """
                {
                    "first_name": "%s",
                    "last_name": "%s",
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(aFirstName, aLastName, aEmail, aPassword);

        final var actualJson = Json.readValue(json, CreateAccountApiInput.class);

        Assertions.assertEquals(actualJson.firstName(), aFirstName);
        Assertions.assertEquals(actualJson.lastName(), aLastName);
        Assertions.assertEquals(actualJson.email(), aEmail);
        Assertions.assertEquals(actualJson.password(), aPassword);
    }

    @Test
    void testUnarshallThrowsException() {
        Assertions.assertThrows(RuntimeException.class,
                () -> Json.readValue(null, CreateAccountApiInput.class));
    }
}
