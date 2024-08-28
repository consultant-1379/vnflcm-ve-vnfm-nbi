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

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AffectedVirtualStorage implements Serializable {

    private static final long serialVersionUID = 4021998814041346325L;
    protected String id;
    protected String virtualStorageDescId;
    protected ResourceHandle storageResource;
    protected ChangeType changeType;
    protected String metadata;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getVirtualStorageDescId() {
        return virtualStorageDescId;
    }

    public void setVirtualStorageDescId(final String virtualStorageDescId) {
        this.virtualStorageDescId = virtualStorageDescId;
    }

    public ResourceHandle getStorageResource() {
        return storageResource;
    }

    public void setStorageResource(final ResourceHandle storageResource) {
        this.storageResource = storageResource;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(final ChangeType changeType) {
        this.changeType = changeType;
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
        return "AffectedVirtualStorage [id=" + id + ", virtualStorageDescId=" + virtualStorageDescId + ", storageResource=" + storageResource
                + ", changeType=" + changeType + ", metadata=" + metadata + "]";
    }
}
