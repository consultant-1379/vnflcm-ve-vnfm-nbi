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
public class InstantiatedVnfInfo {

    protected String flavourId;
    protected VnfOperationalStateType vnfState;
    protected List<ScaleInfo> scaleStatus = new ArrayList<ScaleInfo>();
    protected List<ExtCpInfo> extCpInfo = new ArrayList<ExtCpInfo>();
    protected List<ExtVirtualLinkInfo> extVirtualLinkInfo = new ArrayList<ExtVirtualLinkInfo>();
    protected List<ExtManagedVirtualLinkInfo> extManagedVirtualLinkInfo = new ArrayList<ExtManagedVirtualLinkInfo> ();
    protected List<VnfcResourceInfo> vnfcResourceInfo = new ArrayList<VnfcResourceInfo>();
    protected List<VnfVirtualLinkResourceInfo> vnfVirtualLinkResourceInfo = new ArrayList<VnfVirtualLinkResourceInfo>();
    protected List<VirtualStorageResourceInfo> virtualStorageResourceInfo = new ArrayList<VirtualStorageResourceInfo>();

    public String getFlavourId() {
        return flavourId;
    }

    public void setFlavourId(final String value) {
        flavourId = value;
    }

    public InstantiatedVnfInfo withFlavourId(final String value) {
        flavourId = value;
        return this;
    }

    public VnfOperationalStateType getVnfState() {
        return vnfState;
    }

    public void setVnfState(final VnfOperationalStateType value) {
        vnfState = value;
    }

    public InstantiatedVnfInfo withVnfState(final VnfOperationalStateType value) {
        vnfState = value;
        return this;
    }

    public List<ScaleInfo> getScaleStatus() {
        return scaleStatus;
    }

    public void setScaleStatus(final List<ScaleInfo> value) {
        scaleStatus = value;
    }

    public InstantiatedVnfInfo withScaleStatus(final List<ScaleInfo> value) {
        scaleStatus = value;
        return this;
    }

    public List<ExtCpInfo> getExtCpInfo() {
        return extCpInfo;
    }

    public void setExtCpInfo(final List<ExtCpInfo> value) {
        extCpInfo = value;
    }

    public InstantiatedVnfInfo withExtCpInfo(final List<ExtCpInfo> value) {
        extCpInfo = value;
        return this;
    }

    public List<ExtVirtualLinkInfo> getExtVirtualLinkInfo() {
        return extVirtualLinkInfo;
    }

    public void setExtVirtualLinkInfo(final List<ExtVirtualLinkInfo> value) {
        extVirtualLinkInfo = value;
    }

    public InstantiatedVnfInfo withExtVirtualLinkInfo(final List<ExtVirtualLinkInfo> value) {
        extVirtualLinkInfo = value;
        return this;
    }

    /**
     * @return the extManagedVirtualLinkInfo
     */
    public List<ExtManagedVirtualLinkInfo> getExtManagedVirtualLinkInfo() {
        return extManagedVirtualLinkInfo;
    }

    /**
     * @param extManagedVirtualLinkInfo the extManagedVirtualLinkInfo to set
     */
    public void setExtManagedVirtualLinkInfo(final List<ExtManagedVirtualLinkInfo> extManagedVirtualLinkInfo) {
        this.extManagedVirtualLinkInfo = extManagedVirtualLinkInfo;
    }

    public InstantiatedVnfInfo withExtManagedVirtualLinkInfo(final List<ExtManagedVirtualLinkInfo> value) {
        extManagedVirtualLinkInfo = value;
        return this;
    }

    public List<VnfcResourceInfo> getVnfcResourceInfo() {
        return vnfcResourceInfo;
    }

    public void setVnfcResourceInfo(final List<VnfcResourceInfo> value) {
        vnfcResourceInfo = value;
    }

    public InstantiatedVnfInfo withVnfcResourceInfo(final List<VnfcResourceInfo> value) {
        vnfcResourceInfo = value;
        return this;
    }

    public List<VnfVirtualLinkResourceInfo> getVnfVirtualLinkResourceInfo() {
        return vnfVirtualLinkResourceInfo;
    }

    public void setVnfVirtualLinkResourceInfo(final List<VnfVirtualLinkResourceInfo> value) {
        vnfVirtualLinkResourceInfo = value;
    }

    public InstantiatedVnfInfo withVnfVirtualLinkResourceInfo(final List<VnfVirtualLinkResourceInfo> value) {
        vnfVirtualLinkResourceInfo = value;
        return this;
    }

    public List<VirtualStorageResourceInfo> getVirtualStorageResourceInfo() {
        return virtualStorageResourceInfo;
    }

    public void setVirtualStorageResourceInfo(final List<VirtualStorageResourceInfo> value) {
        virtualStorageResourceInfo = value;
    }

    public InstantiatedVnfInfo withVirtualStorageResourceInfo(final List<VirtualStorageResourceInfo> value) {
        virtualStorageResourceInfo = value;
        return this;
    }

}
