/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumType, Object> {

    private EnumType enumType;

    @Override
    public void initialize(final EnumType enumType) {
        this.enumType = enumType;
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean isValid = false;
        if (value == null || value.toString().trim().isEmpty()) {
            return isValid;
        }
        final Enum<?>[] enumConstants = this.enumType.enumClass().getEnumConstants();

        if (enumConstants != null) {
            for (final Enum<?> enumConstant : enumConstants) {
                if (enumConstant.toString().equals(value.toString())) {
                    isValid = true;
                    break;
                }
            }
        }
        return isValid;
    }
}
