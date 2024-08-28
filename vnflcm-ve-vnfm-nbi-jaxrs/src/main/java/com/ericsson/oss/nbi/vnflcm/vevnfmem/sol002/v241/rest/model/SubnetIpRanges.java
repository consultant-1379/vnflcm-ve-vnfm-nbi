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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubnetIpRanges {

    protected String minIpAddress;
    protected String maxIpAddress;

    public String getMinIpAddress() {
        return minIpAddress;
    }

    public void setMinIpAddress(final String value) {
        minIpAddress = value;
    }

    public SubnetIpRanges withMinIpAddress(final String value) {
        minIpAddress = value;
        return this;
    }

    public String getMaxIpAddress() {
        return maxIpAddress;
    }

    public void setMaxIpAddress(final String value) {
        maxIpAddress = value;
    }

    public SubnetIpRanges withMaxIpAddress(final String value) {
        maxIpAddress = value;
        return this;
    }
}
