package com.kaua.ecommerce.users.domain;

import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

public class EntityTest {

    static class SampleIdentifier extends Identifier {
        private final String value;

        public SampleIdentifier(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Test
    void testValidEntityCreation() {
        SampleIdentifier sampleId = new SampleIdentifier(UUID.randomUUID().toString());
        Entity<SampleIdentifier> entity = createEntity(sampleId);

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(sampleId, entity.getId());
    }

    @Test
    void testEntityEqualityAndHashCode() {
        final var uuid1 = UUID.randomUUID().toString();
        final var uuid2 = UUID.randomUUID().toString();
        SampleIdentifier id1 = new SampleIdentifier(uuid1);
        SampleIdentifier id2 = new SampleIdentifier(uuid1); // Same as id1
        SampleIdentifier id3 = new SampleIdentifier(uuid2);

        Entity<SampleIdentifier> entity1 = createEntity(id1);

        Entity<SampleIdentifier> entity2 = createEntity(id2);

        Entity<SampleIdentifier> entity3 = createEntity(id3);

        Assertions.assertEquals(entity1.getClass(), entity2.getClass());
        Assertions.assertEquals(entity1, entity1);
        Assertions.assertNotEquals(entity1, entity3);
        Assertions.assertNotEquals(entity1.hashCode(), entity2.hashCode());
        Assertions.assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    private Entity<SampleIdentifier> createEntity(SampleIdentifier id) {
        return new Entity<>(id, Collections.emptyList()) {
            @Override
            public void validate(ValidationHandler handler) {
                // Lógica de validação simulada
            }
        };
    }

    @Test
    void testInvalidEntityCreation() {
        SampleIdentifier sampleId = new SampleIdentifier(UUID.randomUUID().toString());
        Entity<SampleIdentifier> entity = createEntity(sampleId);

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(sampleId, entity.getId());
        Assertions.assertNotEquals(entity, null);
        Assertions.assertNotEquals(entity, new Object());
    }
}
