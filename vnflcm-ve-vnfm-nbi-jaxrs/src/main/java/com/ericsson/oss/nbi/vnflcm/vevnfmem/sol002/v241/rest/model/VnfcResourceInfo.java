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
public class VnfcResourceInfo {

    protected String id;
    protected String vduId;
    //    protected String resourceName;
    protected ResourceHandle computeResource;
    protected List<String> storageResourceIds = new ArrayList<String>();;
    protected String reservationId;
    protected List<VnfcCpInfo> vnfcCpInfo = new ArrayList<VnfcCpInfo>();
    protected KeyValuePairs metadata;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VnfcResourceInfo withId(final String value) {
        id = value;
        return this;
    }

    public String getVduId() {
        return vduId;
    }

    public void setVduId(final String value) {
        vduId = value;
    }

    public VnfcResourceInfo withVduId(final String value) {
        vduId = value;
        return this;
    }

    /*    *//**
             * @return the resourceName
             */

    /*
     * public String getResourceName() { return resourceName; }
     * 
     *//**
        * @param resourceName
        *            the resourceName to set
        *//*
           * public void setResourceName(final String resourceName) { this.resourceName = resourceName; }
           * 
           * public VnfcResourceInfo withResourceName(final String value) { this.resourceName=value; return this; }
           */

    public ResourceHandle getComputeResource() {
        return computeResource;
    }

    public void setComputeResource(final ResourceHandle value) {
        computeResource = value;
    }

    public VnfcResourceInfo withComputeResource(final ResourceHandle value) {
        computeResource = value;
        return this;
    }

    public List<String> getStorageResourceIds() {
        return storageResourceIds;
    }

    public void setStorageResourceIds(final List<String> value) {
        storageResourceIds = value;
    }

    public VnfcResourceInfo withStorageResourceIds(final List<String> value) {
        storageResourceIds = value;
        return this;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(final String value) {
        reservationId = value;
    }

    public VnfcResourceInfo withReservationId(final String value) {
        reservationId = value;
        return this;
    }

    public List<VnfcCpInfo> getVnfcCpInfo() {
        return vnfcCpInfo;
    }

    public void setVnfcCpInfo(final List<VnfcCpInfo> value) {
        vnfcCpInfo = value;
    }

    public VnfcResourceInfo withVnfcCpInfo(final List<VnfcCpInfo> value) {
        vnfcCpInfo = value;
        return this;
    }

    public KeyValuePairs getMetadata() {
        return metadata;
    }

    public void setMetadata(final KeyValuePairs value) {
        metadata = value;
    }

    public VnfcResourceInfo withMetadata(final KeyValuePairs value) {
        metadata = value;
        return this;
    }

}
