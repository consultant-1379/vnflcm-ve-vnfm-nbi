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
public class CpProtocolInfo {

    protected LayerProtocol layerProtocol;
    protected IpOverEthernetAddressInfo ipOverEthernet;

    public LayerProtocol getLayerProtocol() {
        return layerProtocol;
    }

    public void setLayerProtocol(final LayerProtocol value) {
        layerProtocol = value;
    }

    public CpProtocolInfo withLayerProtocol(final LayerProtocol value) {
        layerProtocol = value;
        return this;
    }

    /**
     * @return the ipOverEthernet
     */
    public IpOverEthernetAddressInfo getIpOverEthernet() {
        return ipOverEthernet;
    }

    /**
     * @param ipOverEthernet
     *            the ipOverEthernet to set
     */
    public void setIpOverEthernet(final IpOverEthernetAddressInfo ipOverEthernet) {
        this.ipOverEthernet = ipOverEthernet;
    }

    public CpProtocolInfo withIpOverEthernetAddressInfo(final IpOverEthernetAddressInfo value) {
        ipOverEthernet = value;
        return this;
    }

    @Override
    public String toString() {
        return "CpProtocolInfo [layerProtocol=" + layerProtocol + ", ipOverEthernet=" + ipOverEthernet + "]";
    }
}
