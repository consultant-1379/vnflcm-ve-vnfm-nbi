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
public enum NotificationType {
    CREATE("VnfIdentifierCreationNotification"), DELETE("VnfIdentifierDeletionNotification"), START("VnfLcmOperationOccurrenceNotification"), PROCESSING(
            "VnfLcmOperationOccurrenceNotification"), ROLLED_BACK("VnfLcmOperationOccurrenceNotification"), COMPLETED(
            "VnfLcmOperationOccurrenceNotification"), FAILED("VnfLcmOperationOccurrenceNotification"), ROLLING_BACK("VnfLcmOperationOccurrenceNotification")
    ,FAILED_TEMP("VnfLcmOperationOccurrenceNotification");

    private String value;

    NotificationType(final String notification) {
        this.value = notification;
    }

    public String getValue() {
        return value;
    }

}
