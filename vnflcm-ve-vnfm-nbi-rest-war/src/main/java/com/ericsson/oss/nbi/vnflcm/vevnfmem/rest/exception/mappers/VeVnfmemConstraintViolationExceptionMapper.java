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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.exception.mappers;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.VeVnfmemConstraintViolationException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorType;

/**
 * OrVnfmConstraintViolationException is thrown when there are any validation errors.
 *
 */
@Provider
public class VeVnfmemConstraintViolationExceptionMapper implements ExceptionMapper<VeVnfmemConstraintViolationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VeVnfmemConstraintViolationExceptionMapper.class);

    @Override
    public Response toResponse(final VeVnfmemConstraintViolationException mcve) {
        LOGGER.error("Exception occured. ", mcve);
        //To be enhanced to show the parameter names in future.
        final Set<ConstraintViolation<?>> violations = mcve.getConstraintViolations();
        final StringBuffer detail = new StringBuffer();
        if (violations != null && !violations.isEmpty()) {
            for (final ConstraintViolation<?> cViolation : violations) {
                detail.append(cViolation.getPropertyPath() + ": " + cViolation.getMessage() + " ; ");
            }
        }
        final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail();
        problemDetail.setTitle("Validation Errors");
        problemDetail.setDetail(detail.toString());
        problemDetail.setStatus(ErrorType.VALIDATION_ERROR.getHttpCode());
        LOGGER.error("problemDetail : " + problemDetail.toJson());
        return Response.status(ErrorType.VALIDATION_ERROR.getHttpCode()).entity(problemDetail).type(MediaType.APPLICATION_JSON).build();
    }

}
