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
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AffectedVnfc implements Serializable {

    private static final long serialVersionUID = 2248363771501184644L;

    protected String id;
    protected String vduId;
    protected ResourceHandle computeResource;
    protected List<String> addedStorageResourceIds = new ArrayList<String>();
    protected List<String> removedStorageResourceIds = new ArrayList<String>();
    protected ChangeType changeType;
    protected String metadata;
    protected String affectedVnfcCpIds;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getVduId() {
        return vduId;
    }

    public void setVduId(final String vduId) {
        this.vduId = vduId;
    }

    public ResourceHandle getComputeResource() {
        return computeResource;
    }

    public void setComputeResource(final ResourceHandle computeResource) {
        this.computeResource = computeResource;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(final ChangeType changeType) {
        this.changeType = changeType;
    }

    public List<String> getAddedStorageResourceIds() {
        return addedStorageResourceIds;
    }

    public void setAddedStorageResourceIds(final List<String> addedStorageResourceIds) {
        this.addedStorageResourceIds = addedStorageResourceIds;
    }

    public List<String> getRemovedStorageResourceIds() {
        return removedStorageResourceIds;
    }

    public void setRemovedStorageResourceIds(final List<String> removedStorageResourceIds) {
        this.removedStorageResourceIds = removedStorageResourceIds;
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

    /**
     * @return the affectedVnfcCpIds
     */
    public String getAffectedVnfcCpIds() {
        return affectedVnfcCpIds;
    }

    /**
     * @param affectedVnfcCpIds
     *            the affectedVnfcCpIds to set
     */
    public void setAffectedVnfcCpIds(final String affectedVnfcCpIds) {
        this.affectedVnfcCpIds = affectedVnfcCpIds;
    }

    @Override
    public String toString() {
        return "AffectedVnfc [id=" + id + ", vduId=" + vduId + ", computeResource=" + computeResource + ", addedStorageResourceIds="
                + addedStorageResourceIds + ", removedStorageResourceIds=" + removedStorageResourceIds + ", changeType=" + changeType + ", metadata="
                + metadata + ", affectedVnfcCpIds=" + affectedVnfcCpIds + "]";
    }

}
