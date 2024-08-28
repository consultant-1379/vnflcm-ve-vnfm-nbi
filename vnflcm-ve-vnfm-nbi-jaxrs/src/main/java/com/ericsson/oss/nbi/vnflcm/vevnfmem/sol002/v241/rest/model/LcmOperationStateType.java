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

public enum LcmOperationStateType {

    STARTING(0), PROCESSING(1), COMPLETED(2), CANCELLED(3), FAILED_TEMP(13), FAILED(-1), ROLLING_BACK(9), ROLLED_BACK(10);

    private final int value;

    LcmOperationStateType(final int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static LcmOperationStateType fromValue(final int v) {
        for (final LcmOperationStateType c : LcmOperationStateType.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid Lcm operation state type recieved");
    }
}
