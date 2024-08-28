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
public enum CallExecutionErrorMessage {
    CONFIGURATION_PROP_NOT_EXIST("Mandatory property in nfvoconfig.json does not exist at location /ericsson/vnlcm/data"), CONFIGURATION_PROP_EMPTY(
            "Mandatory property in nfvoconfig.json is empty at location /ericsson/vnlcm/data"), REST_CALL_FAILURE("Error in making REST call"), NOTIFICATION_TYPE_NOT_SET(
            "Notification type is not populated"), NULL_NOTIFICATION_DATA("Notification Data passed for JSON creation is null"), UNKNOWN_EXCEPTION_NOTIFICATION_DATA(
            "Unknown exception in reading configuration file containing notification data"), UNSUPPORTED_NOTIFICATION_TYPE(
            "Unsupported notification type"), INVALID_JSON_FILE("Not a valid JSON file"), INVALID_NOTIFICATION_TYPE("Unsupported notification type."), UNKNOWN_ERROR_NOTIFICATION_REST_CALL(
            "Unknown error in making rest call to NFVO"), AUTHENTICATION_FAILED("Authentication for token failed.");

    private String value;

    CallExecutionErrorMessage(final String notification) {
        this.value = notification;
    }

    public String getValue() {
        return value;
    }

}
