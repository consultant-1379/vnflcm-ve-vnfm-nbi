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

import java.util.HashSet;
import java.util.Set;

import javax.validation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestValidator<T> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method that will trigger annotations placed on the properties of the given object.
     *
     * @param requestObject
     * @return
     * @throws VeVnfmemConstraintViolationException
     */
    public void validate(final Object requestObject) throws VeVnfmemConstraintViolationException {
        if (requestObject != null) {
            @SuppressWarnings("unchecked")
            final T reqObject = (T) requestObject;
            logger.info("Validating " + reqObject.getClass());
            final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

            final Set<ConstraintViolation<T>> constraintViolations = validator.validate(reqObject);

            if (constraintViolations != null && constraintViolations.size() > 0) {
                logger.error("Validation failed, throwing VeVnfmemConstraintViolationException");
                throw new VeVnfmemConstraintViolationException("Validation Failed.", new HashSet<ConstraintViolation<?>>(constraintViolations));
            }
        }

    }

}
