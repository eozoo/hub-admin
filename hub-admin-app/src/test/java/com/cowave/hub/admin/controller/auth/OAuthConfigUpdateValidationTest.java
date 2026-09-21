package com.cowave.hub.admin.controller.auth;

import com.cowave.hub.admin.domain.auth.entity.command.OAuthConfigUpdate;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author shanhuiming
 */
class OAuthConfigUpdateValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void statusMustBeZeroOrOne() {
        OAuthConfigUpdate command = new OAuthConfigUpdate();
        assertEquals(1, validator.validate(command).size());

        command.setStatus(-1);
        assertEquals(1, validator.validate(command).size());

        command.setStatus(2);
        assertEquals(1, validator.validate(command).size());

        command.setStatus(0);
        assertEquals(0, validator.validate(command).size());

        command.setStatus(1);
        assertEquals(0, validator.validate(command).size());
    }
}
