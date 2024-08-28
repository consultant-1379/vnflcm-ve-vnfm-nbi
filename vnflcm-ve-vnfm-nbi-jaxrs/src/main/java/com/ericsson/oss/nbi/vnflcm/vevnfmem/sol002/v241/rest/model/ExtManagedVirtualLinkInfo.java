/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
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
public class ExtManagedVirtualLinkInfo {

    protected String id;
    protected ResourceHandle networkResource;
    protected String vnfVirtualLinkDescId;
    protected List<VnfLinkPortInfo> vnfLinkPorts = new ArrayList<VnfLinkPortInfo>();

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public ExtManagedVirtualLinkInfo withId(final String value) {
        id = value;
        return this;
    }

    public ResourceHandle getNetworkResource() {
        return networkResource;
    }

    public void setNetworkResource(final ResourceHandle value) {
        networkResource = value;
    }

    public ExtManagedVirtualLinkInfo withNetworkResource(final ResourceHandle value) {
        networkResource = value;
        return this;
    }

    /**
     * @return the vnfVirtualLinkDescId
     */
    public String getVnfVirtualLinkDescId() {
        return vnfVirtualLinkDescId;
    }

    /**
     * @param vnfVirtualLinkDescId the vnfVirtualLinkDescId to set
     */
    public void setVnfVirtualLinkDescId(final String vnfVirtualLinkDescId) {
        this.vnfVirtualLinkDescId = vnfVirtualLinkDescId;
    }

    public ExtManagedVirtualLinkInfo withVnfVirtualLinkDescId(final String value) {
        vnfVirtualLinkDescId = value;
        return this;
    }

    /**
     * @return the vnfLinkPorts
     */
    public List<VnfLinkPortInfo> getVnfLinkPorts() {
        return vnfLinkPorts;
    }

    /**
     * @param vnfLinkPorts the vnfLinkPorts to set
     */
    public void setVnfLinkPorts(final List<VnfLinkPortInfo> vnfLinkPorts) {
        this.vnfLinkPorts = vnfLinkPorts;
    }

    public ExtManagedVirtualLinkInfo withLinkPorts(final List<VnfLinkPortInfo> value) {
        vnfLinkPorts = value;
        return this;
    }
}
