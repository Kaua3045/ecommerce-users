package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionCommand;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionOutput;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.delete.DeletePermissionUseCase;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.infrastructure.permissions.models.CreatePermissionApiInput;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;

@ControllerTest(controllers = PermissionAPI.class)
public class PermissionAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreatePermissionUseCase createPermissionUseCase;

    @MockBean
    private DeletePermissionUseCase deletePermissionUseCase;

    @Test
    void givenAValidCommandWithDescription_whenCallCreatePermission_thenShouldReturnAnPermissionId() throws Exception {
        // given
        final var aName = "create-role";
        final var aDescription = "Create a new role";
        final var aId = "123";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.right(CreatePermissionOutput.from(aId)));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                        Objects.equals(aName, cmd.name()) &&
                                Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAValidCommandWithNullDescription_whenCallCreatePermission_thenShouldReturnAnPermissionId() throws Exception {
        // given
        final var aName = "create-role";
        final String aDescription = null;
        final var aId = "123";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.right(CreatePermissionOutput.from(aId)));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAnInvalidCommandExistingName_whenCallCreatePermission_thenShouldReturnDomainException() throws Exception {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        final var expectedErrorMessage = "Permission already exists";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAnInvalidCommandNullName_whenCallCreatePermission_thenShouldReturnDomainException() throws Exception {
        final String aName = null;
        final var aDescription = "Create a new role";
        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAnInvalidCommandBlankName_whenCallCreatePermission_thenShouldReturnDomainException() throws Exception {
        final var aName = "";
        final String aDescription = null;
        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthLessThan3_whenCallCreatePermission_thenShouldReturnDomainException() throws Exception {
        final var aName = "cr ";
        final var aDescription = "Create a new role";
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthMoreThan50_whenCallCreatePermission_thenShouldReturnDomainException() throws Exception {
        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Create a new role";
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAnInvalidCommandDescriptionLengthMoreThan255_whenCallCreatePermission_thenShouldReturnDomainException() throws Exception {
        final var aName = "create-role";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";

        final var aInput = new CreatePermissionApiInput(aName, aDescription);

        Mockito.when(createPermissionUseCase.execute(Mockito.any(CreatePermissionCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createPermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAValidCommandWithPermissionId_whenCallDeletePermission_thenShouldReturnOk() throws Exception {
        // given
        final var aPermission = Permission.newPermission("create-role", "Create a new role");
        final var aId = aPermission.getId().getValue();

        final var request = MockMvcRequestBuilders.delete("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(deletePermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAnInvalidPermissionId_whenCallDeletePermission_thenShouldReturnOk() throws Exception {
        // given
        final var aId = "invalid";

        final var request = MockMvcRequestBuilders.delete("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(deletePermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }
}
