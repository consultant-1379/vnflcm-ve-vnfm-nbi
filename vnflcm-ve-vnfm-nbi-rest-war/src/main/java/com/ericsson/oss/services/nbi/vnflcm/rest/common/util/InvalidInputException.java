package com.ericsson.oss.services.nbi.vnflcm.rest.common.util;

import javax.ws.rs.core.Response;

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

/**
 * InvalidInputException thrown when input provided to the task is not valid
 * 
 */
public class InvalidInputException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Response response;

    /**
     * @return the response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * @param response
     *            the response to set
     */
    public void setResponse(final Response response) {
        this.response = response;
    }

    /**
     * Creates the invalid input exception with errMessage
     * 
     * @param errMessage
     *            the error message
     */
    public InvalidInputException(final String errMessage) {
        super(errMessage);

    }

    /**
     * Create a InvalidInputException with the given message and Rest response
     * 
     * @param message
     *            the message for this exception.
     * @param response
     *            Rest response with HTTP code and appropriate error message.
     */
    public InvalidInputException(final String message, final Response response) {
        super(message);
        this.response = response;
    }

}
