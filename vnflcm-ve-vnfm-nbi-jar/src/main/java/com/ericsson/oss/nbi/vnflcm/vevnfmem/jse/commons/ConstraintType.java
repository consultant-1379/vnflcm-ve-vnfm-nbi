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

public enum ConstraintType {

    AFFINITY("affinity"), ANTI_AFFINITY("anti-affinity"), SOFT_ANTI_AFFINITY("soft-anti-affinity"), SOFT_AFFINITY("soft-affinity");

    private final String value;
    ConstraintType(final String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConstraintType fromValue(final String v) {
        for (ConstraintType c: ConstraintType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException();
    }
}
