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

public enum LcmOperationType {

    INSTANTIATE(0), TERMINATE(1), SCALE(2), HEAL(3), CHANGE_VNFPKG(4), MODIFY_INFO(5), CREATE_IDENTIFIER(6), DELETE_IDENTIFIER(7);

    private final int value;

    LcmOperationType(final int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static LcmOperationType fromValue(final int v) {
        for (final LcmOperationType c : LcmOperationType.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid Lcm operation type recieved");
    }
}
