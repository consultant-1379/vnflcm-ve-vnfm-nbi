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

public enum PlacementScopes {

    NFVI_POP("nfvi_pop"), ZONE("zone"), ZONE_GROUP("zone_group"), NFVI_NODE("nfvi_node");
    private final String value;
    PlacementScopes(final String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlacementScopes fromValue(final String v) {
        for (PlacementScopes p: PlacementScopes.values()) {
            if (p.value.equalsIgnoreCase(v)) {
                return p;
            }
        }
        throw new IllegalArgumentException();
    }
}
