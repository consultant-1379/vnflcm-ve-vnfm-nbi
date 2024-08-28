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

public enum OperationType {

    INSTANTIATION(0), TERMINATION(1), SCALE_OUT(2), SCALE_IN(3);

    private final int value;

    OperationType(final int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static OperationType fromValue(final int v) {
        for (final OperationType c : OperationType.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid Operation status recieved");
    }
}
