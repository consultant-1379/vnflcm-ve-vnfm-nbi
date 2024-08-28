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
public class VirtualStorageResourceInfo {

    protected String id;
    protected String virtualStorageDescId;
    protected ResourceHandle storageResource;
    protected String reservationId;
    protected KeyValuePairs metadata;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VirtualStorageResourceInfo withId(final String value) {
        id = value;
        return this;
    }

    public String getVirtualStorageDescId() {
        return virtualStorageDescId;
    }

    public void setVirtualStorageDescId(final String value) {
        virtualStorageDescId = value;
    }

    public VirtualStorageResourceInfo withVirtualStorageDescId(final String value) {
        virtualStorageDescId = value;
        return this;
    }

    public ResourceHandle getStorageResource() {
        return storageResource;
    }

    public void setStorageResource(final ResourceHandle value) {
        storageResource = value;
    }

    public VirtualStorageResourceInfo withStorageResource(final ResourceHandle value) {
        storageResource = value;
        return this;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(final String value) {
        reservationId = value;
    }

    public VirtualStorageResourceInfo withReservationId(final String value) {
        reservationId = value;
        return this;
    }

    public KeyValuePairs getMetadata() {
        return metadata;
    }

    public void setMetadata(final KeyValuePairs value) {
        metadata = value;
    }

    public VirtualStorageResourceInfo withMetadata(final KeyValuePairs value) {
        metadata = value;
        return this;
    }

}
