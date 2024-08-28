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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VnfVirtualLinkResourceInfo {

    protected String id;
    protected String vnfVirtualLinkDescId;
    protected ResourceHandle networkResource;
    protected String reservationId;
    protected List<VnfLinkPortInfo> vnfLinkPorts = new ArrayList<VnfLinkPortInfo>();
    protected KeyValuePairs metadata;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VnfVirtualLinkResourceInfo withId(final String value) {
        id = value;
        return this;
    }

    public String getVnfVirtualLinkDescId() {
        return vnfVirtualLinkDescId;
    }

    public void setVnfVirtualLinkDescId(final String value) {
        vnfVirtualLinkDescId = value;
    }

    public VnfVirtualLinkResourceInfo withVnfVirtualLinkDescId(final String value) {
        vnfVirtualLinkDescId = value;
        return this;
    }

    public ResourceHandle getNetworkResource() {
        return networkResource;
    }

    public void setNetworkResource(final ResourceHandle value) {
        networkResource = value;
    }

    public VnfVirtualLinkResourceInfo withNetworkResource(final ResourceHandle value) {
        networkResource = value;
        return this;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(final String value) {
        reservationId = value;
    }

    public VnfVirtualLinkResourceInfo withReservationId(final String value) {
        reservationId = value;
        return this;
    }

    public List<VnfLinkPortInfo> getVnfLinkPorts() {
        return vnfLinkPorts;
    }

    public void setVnfLinkPorts(final List<VnfLinkPortInfo> value) {
        vnfLinkPorts = value;
    }

    public VnfVirtualLinkResourceInfo withVnfLinkPorts(final List<VnfLinkPortInfo> value) {
        vnfLinkPorts = value;
        return this;
    }

    public KeyValuePairs getMetadata() {
        return metadata;
    }

    public void setMetadata(final KeyValuePairs value) {
        metadata = value;
    }

    public VnfVirtualLinkResourceInfo withMetadata(final KeyValuePairs value) {
        metadata = value;
        return this;
    }
}
