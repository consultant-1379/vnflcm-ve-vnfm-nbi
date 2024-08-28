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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpOverEthernetAddressData {

    protected String macAddress;
    protected List<IpAddresses> ipAddresses = new ArrayList<IpAddresses>();

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(final String value) {
        this.macAddress = value;
    }

    public IpOverEthernetAddressData withMacAddress(final String value) {
        this.macAddress = value;
        return this;
    }

    /**
     * @return the ipAddresses
     */
    public List<IpAddresses> getIpAddresses() {
        return ipAddresses;
    }

    /**
     * @param ipAddresses
     *            the ipAddresses to set
     */
    public void setIpAddresses(final List<IpAddresses> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public IpOverEthernetAddressData withIpAddresses(final List<IpAddresses> value) {
        this.ipAddresses = value;
        return this;
    }
}
