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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VnfLinkPort {

    protected String id;
    protected ResourceHandle resourceHandle;
    protected String cpInstanceId;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VnfLinkPort withId(final String value) {
        id = value;
        return this;
    }

    public ResourceHandle getResourceHandle() {
        return resourceHandle;
    }

    public void setResourceHandle(final ResourceHandle value) {
        resourceHandle = value;
    }

    public VnfLinkPort withResourceHandle(final ResourceHandle value) {
        resourceHandle = value;
        return this;
    }

    public String getCpInstanceId() {
        return cpInstanceId;
    }

    public void setCpInstanceId(final String value) {
        cpInstanceId = value;
    }

    public VnfLinkPort withCpInstanceId(final String value) {
        cpInstanceId = value;
        return this;
    }

}
