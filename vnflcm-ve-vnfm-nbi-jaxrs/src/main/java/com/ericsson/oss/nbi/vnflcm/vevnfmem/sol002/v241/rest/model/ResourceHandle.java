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
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResourceHandle {

    protected String vimConnectionId;
    protected String resourceProviderId;
    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String resourceId;
    protected String vimLevelResourceType;

    public String getVimConnectionId() {
        return vimConnectionId;
    }

    public void setVimConnectionId(final String value) {
        vimConnectionId = value;
    }

    public ResourceHandle withVimConnectionId(final String value) {
        vimConnectionId = value;
        return this;
    }

    public String getResourceProviderId() {
        return resourceProviderId;
    }

    public void setResourceProviderId(final String value) {
        resourceProviderId = value;
    }

    public ResourceHandle withResourceProviderId(final String value) {
        resourceProviderId = value;
        return this;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(final String value) {
        resourceId = value;
    }

    public ResourceHandle withResourceId(final String value) {
        resourceId = value;
        return this;
    }

    public String getVimLevelResourceType() {
        return vimLevelResourceType;
    }

    public void setVimLevelResourceType(final String value) {
        vimLevelResourceType = value;
    }

    public ResourceHandle withVimLevelResourceType(final String value) {
        vimLevelResourceType = value;
        return this;
    }

}
