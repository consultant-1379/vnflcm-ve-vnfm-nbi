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

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

public class VeVnfmemConstraintViolationException extends ValidationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final Set<ConstraintViolation<?>> constraintViolations;

    public VeVnfmemConstraintViolationException(final String message, final Set<ConstraintViolation<?>> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }

    public VeVnfmemConstraintViolationException(final Set<ConstraintViolation<?>> constraintViolations) {
        super();
        this.constraintViolations = constraintViolations;
    }

    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }

}
