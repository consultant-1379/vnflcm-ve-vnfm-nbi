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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NetworkAddressInfo {

    protected String macAddress;
    protected String ipAddress;
    protected SubnetIpRanges subnetIpRanges;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(final String value) {
        macAddress = value;
    }

    public NetworkAddressInfo withMacAddress(final String value) {
        macAddress = value;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String value) {
        ipAddress = value;
    }

    public NetworkAddressInfo withIpAddress(final String value) {
        ipAddress = value;
        return this;
    }

    public SubnetIpRanges getSubnetIpRanges() {
        return subnetIpRanges;
    }

    public void setSubnetIpRanges(final SubnetIpRanges value) {
        subnetIpRanges = value;
    }

    public NetworkAddressInfo withSubnetIpRanges(final SubnetIpRanges value) {
        subnetIpRanges = value;
        return this;
    }

}
