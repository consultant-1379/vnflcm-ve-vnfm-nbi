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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CpProtocolData {

    @NotNull(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected LayerProtocol layerProtocol;
    @Valid
    protected IpOverEthernetAddressData ipOverEthernet;

    public LayerProtocol getLayerProtocol() {
        return layerProtocol;
    }

    public void setLayerProtocol(final LayerProtocol value) {
        layerProtocol = value;
    }

    public CpProtocolData withLayerProtocol(final LayerProtocol value) {
        layerProtocol = value;
        return this;
    }

    /**
     * @return the ipOverEthernet
     */
    public IpOverEthernetAddressData getIpOverEthernet() {
        return ipOverEthernet;
    }

    /**
     * @param ipOverEthernet
     *            the ipOverEthernet to set
     */
    public void setIpOverEthernet(final IpOverEthernetAddressData ipOverEthernet) {
        this.ipOverEthernet = ipOverEthernet;
    }

    public CpProtocolData withIpOverEthernetData(final IpOverEthernetAddressData value) {
        ipOverEthernet = value;
        return this;
    }

}
