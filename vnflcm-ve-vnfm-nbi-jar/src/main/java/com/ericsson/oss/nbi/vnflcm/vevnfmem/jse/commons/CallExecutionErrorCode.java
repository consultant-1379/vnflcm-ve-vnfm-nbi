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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons;

/**
 * @author xrohdwi
 * 
 */
public enum CallExecutionErrorCode {
    CONFIGURATION_PROP_NOT_EXIST(301), CONFIGURATION_PROP_EMPTY(302), REST_CALL_FAILURE(303), NOTIFICATION_TYPE_NOT_SET(304), NULL_NOTIFICATION_DATA(
            305), UNKNOWN_EXCEPTION_NOTIFICATION_DATA(306), UNSUPPORTED_NOTIFICATION_TYPE(307), INVALID_JSON_FILE(308), CONFIGURATION_NOT_EXIST(
            309), INVALID_NOTIFICATION_TYPE(310), UNKNOWN_ERROR_NOTIFICATION_REST_CALL(311), AUTHENTICATION_FAILED(312);

    private final int value;

    CallExecutionErrorCode(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

}
