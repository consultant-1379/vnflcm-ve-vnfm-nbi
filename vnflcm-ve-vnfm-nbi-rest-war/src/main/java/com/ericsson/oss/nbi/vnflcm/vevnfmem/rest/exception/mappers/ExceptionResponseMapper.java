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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorMessageUtil;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorType;

/**
 * Produces proper error message for all the Runtime exceptions
 */
@Provider
public class ExceptionResponseMapper implements ExceptionMapper<WebApplicationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseMapper.class);

    @Override
    public Response toResponse(final WebApplicationException ex) {
        LOGGER.error("Exception occured. ", ex);
        return Response.status(ErrorType.INTERNAL_ERROR.getHttpCode()).entity(ErrorMessageUtil.generateUnexpectedError(ErrorType.INTERNAL_ERROR))
                .build();
    }

}