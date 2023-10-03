package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionCommand;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionOutput;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.delete.DeletePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdCommand;
import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdOutput;
import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdUseCase;
import com.kaua.ecommerce.users.application.permission.update.UpdatePermissionCommand;
import com.kaua.ecommerce.users.application.permission.update.UpdatePermissionOutput;
import com.kaua.ecommerce.users.application.permission.update.UpdatePermissionUseCase;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.infrastructure.permissions.models.CreatePermissionApiInput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.UpdatePermissionApiInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

    @MockBean
    private GetPermissionByIdUseCase getPermissionByIdUseCase;

    @MockBean
    private UpdatePermissionUseCase updatePermissionUseCase;

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

    @Test
    void givenAValidCommand_whenCallGetPermission_thenShouldReturneAnPermission() throws Exception {
        // given
        final var aName = "create-admin-role";
        final var aDescription = "Create a new admin role";
        final var aPermission = Permission.newPermission(aName, aDescription);
        final var aId = aPermission.getId().getValue();

        Mockito.when(getPermissionByIdUseCase.execute(Mockito.any(GetPermissionByIdCommand.class)))
                .thenReturn(GetPermissionByIdOutput.from(aPermission));

        final var request = MockMvcRequestBuilders.get("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(aName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(aDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", equalTo(aPermission.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", equalTo(aPermission.getUpdatedAt().toString())));

        Mockito.verify(getPermissionByIdUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAnInvalidId_whenCallGetPermission_thenShouldThrowNotFoundException() throws Exception {
        // given
        final var expectedErrorMessage = "Permission with id 123 was not found";
        final var aId = "123";

        Mockito.when(getPermissionByIdUseCase.execute(Mockito.any(GetPermissionByIdCommand.class)))
                .thenThrow(NotFoundException.with(Permission.class, aId));

        final var request = MockMvcRequestBuilders.get("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(getPermissionByIdUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAValidValues_whenCallUpdatePermission_thenShouldReturnOkAndPermissionId() throws Exception {
        // given
        final var aName = "create-an-admin-role";
        final var aDescription = "Create a new admin role";
        final var aPermission = Permission.newPermission(aName, null);
        final var aId = aPermission.getId().getValue();

        final var aInput = new UpdatePermissionApiInput(aDescription);

        Mockito.when(updatePermissionUseCase.execute(Mockito.any(UpdatePermissionCommand.class)))
                .thenReturn(Either.right(UpdatePermissionOutput.from(aPermission)));

        final var request = MockMvcRequestBuilders.patch("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdatePermissionCommand.class);

        Mockito.verify(updatePermissionUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.id());
        Assertions.assertEquals(aDescription, actualCmd.description());
    }

    @Test
    void givenAValidValuesWithNullDescription_whenCallUpdatePermission_thenShouldReturnOkAndPermissionId() throws Exception {
        // given
        final var aName = "create-an-admin-role";
        final String aDescription = null;
        final var aPermission = Permission.newPermission(aName, "Create a new admin role");
        final var aId = aPermission.getId().getValue();

        final var aInput = new UpdatePermissionApiInput(aDescription);

        Mockito.when(updatePermissionUseCase.execute(Mockito.any(UpdatePermissionCommand.class)))
                .thenReturn(Either.right(UpdatePermissionOutput.from(aPermission)));

        final var request = MockMvcRequestBuilders.patch("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdatePermissionCommand.class);

        Mockito.verify(updatePermissionUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.id());
        Assertions.assertEquals(aDescription, actualCmd.description());
    }

    @Test
    void givenAnInvalidCommandNotExistsPermission_whenCallUpdatePermission_thenShouldReturnNotFoundException() throws Exception {
        final var aId = "123";
        final var aName = "create-an-admin-role";
        final var aDescription = "Create a new admin role";
        final var expectedErrorMessage = "Permission with id 123 was not found";

        final var aInput = new UpdatePermissionApiInput(aDescription);

        Mockito.when(updatePermissionUseCase.execute(Mockito.any(UpdatePermissionCommand.class)))
                .thenThrow(NotFoundException.with(Permission.class, aId));

        final var request = MockMvcRequestBuilders.patch("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(updatePermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                        Objects.equals(aDescription, cmd.description())
        ));
    }

    @Test
    void givenAnInvalidCommandDescriptionLengthMoreThan255_whenCallUpdatePermission_thenShouldReturnDomainException() throws Exception {
        final var aName = "create-an-admin-role";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aPermission = Permission.newPermission(aName, null);
        final var aId = aPermission.getId().getValue();
        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";

        final var aInput = new UpdatePermissionApiInput(aDescription);

        Mockito.when(updatePermissionUseCase.execute(Mockito.any(UpdatePermissionCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/permissions/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updatePermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                        Objects.equals(aDescription, cmd.description())
        ));
    }
}
