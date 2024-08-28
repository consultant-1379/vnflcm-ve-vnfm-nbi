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

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AffectedVirtualLink implements Serializable {

    private static final long serialVersionUID = -1721390686728162309L;
    protected String id;
    protected String virtualLinkDescId;
    protected ResourceHandle networkResource;
    protected VirtualLinkChangeType affectedVirtualLinkChangeType;
    protected String metadata;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getVirtualLinkDescId() {
        return virtualLinkDescId;
    }

    public void setVirtualLinkDescId(final String virtualLinkDescId) {
        this.virtualLinkDescId = virtualLinkDescId;
    }

    public ResourceHandle getNetworkResource() {
        return networkResource;
    }

    public void setNetworkResource(final ResourceHandle networkResource) {
        this.networkResource = networkResource;
    }

    public VirtualLinkChangeType getAffectedVirtualLinkChangeType() {
        return affectedVirtualLinkChangeType;
    }

    public void setAffectedVirtualLinkChangeType(final VirtualLinkChangeType affectedVirtualLinkChangeType) {
        this.affectedVirtualLinkChangeType = affectedVirtualLinkChangeType;
    }

    /**
     * @return the metadata
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * @param metadata
     *            the metadata to set
     */
    public void setMetadata(final String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "AffectedVirtualLink [id=" + id + ", virtualLinkDescId=" + virtualLinkDescId + ", networkResource=" + networkResource
                + ", affectedVirtualLinkChangeType=" + affectedVirtualLinkChangeType + ", metadata=" + metadata + "]";
    }
}
