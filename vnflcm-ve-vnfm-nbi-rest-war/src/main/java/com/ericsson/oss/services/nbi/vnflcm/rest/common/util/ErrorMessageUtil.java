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
package com.ericsson.oss.services.nbi.vnflcm.rest.common.util;

import java.util.Date;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ErrorMessage;

/**
 * This class is responsible for generating error messages.
 */
public final class ErrorMessageUtil {

    private ErrorMessageUtil() {
    }

    public static ErrorMessage generateErrorMessage(final ErrorType error, final String userMessage, final String developerMessage) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setUserMessage(userMessage);
        errorMessage.setDeveloperMessage(developerMessage);
        errorMessage.setInternalErrorCode(error.getInternalErrorCode());
        errorMessage.setHttpStatusCode(error.getHttpCode());
        errorMessage.setTime(new Date());
        return errorMessage;
    }

    public static ErrorMessage generateNotImplementedMessage(final ErrorType error) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setUserMessage(ErrorMessages.NOT_SUPPORTED_MSG[0]);
        errorMessage.setDeveloperMessage(ErrorMessages.NOT_SUPPORTED_MSG[1]);
        errorMessage.setInternalErrorCode(error.getInternalErrorCode());
        errorMessage.setHttpStatusCode(error.getHttpCode());
        errorMessage.setTime(new Date());
        return errorMessage;
    }

    public static ErrorMessage generateBadRequestMessage(final ErrorType error) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setUserMessage(ErrorMessages.BAD_REQUEST_MSG[0]);
        errorMessage.setDeveloperMessage(ErrorMessages.BAD_REQUEST_MSG[1]);
        errorMessage.setInternalErrorCode(error.getInternalErrorCode());
        errorMessage.setHttpStatusCode(error.getHttpCode());
        errorMessage.setTime(new Date());
        return errorMessage;
    }

    public static ErrorMessage generateUnexpectedError(final ErrorType error) {
        final ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setUserMessage(ErrorMessages.INTERNAL_ERROR_MSG[0]);
        errorResponse.setDeveloperMessage(ErrorMessages.INTERNAL_ERROR_MSG[1]);

        errorResponse.setInternalErrorCode(error.getInternalErrorCode());
        errorResponse.setHttpStatusCode(error.getHttpCode());
        errorResponse.setTime(new Date());
        return errorResponse;
    }

    public static ErrorMessage generateUserHeaderMessage(final ErrorType error) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setUserMessage(ErrorMessages.USER_HEADER_NOT_FOUND_MSG[0]);
        errorMessage.setDeveloperMessage(ErrorMessages.USER_HEADER_NOT_FOUND_MSG[1]);
        errorMessage.setInternalErrorCode(error.getInternalErrorCode());
        errorMessage.setHttpStatusCode(error.getHttpCode());
        errorMessage.setTime(new Date());
        return errorMessage;
    }

}
