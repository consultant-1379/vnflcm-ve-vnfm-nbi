/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model;

public enum OperationStatus {

    SUCCESS(0), PROCESSING(1), FAILED(2), CANCELLED(3), UNKNOWN(-1), NOTSTARTED(9);

    private final int value;

    OperationStatus(final int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static OperationStatus fromValue(final int v) {
        for (final OperationStatus c : OperationStatus.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid Operation status recieved");
    }
}
