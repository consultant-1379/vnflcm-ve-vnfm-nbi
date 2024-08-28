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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtManagedVirtualLinkData {

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String id;
    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String virtualLinkDescId;
    protected String vimConnectionId;
    protected String resourceProviderId;
    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String resourceId;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        this.id = value;
    }

    public ExtManagedVirtualLinkData withId(final String value) {
        this.id = value;
        return this;
    }

    public String getVimConnectionId() {
        return vimConnectionId;
    }

    public void setVimConnectionId(final String value) {
        this.vimConnectionId = value;
    }

    public ExtManagedVirtualLinkData withVimConnectionId(final String value) {
        this.vimConnectionId = value;
        return this;
    }

    /**
     * @return the resourceProviderId
     */
    public String getResourceProviderId() {
        return resourceProviderId;
    }

    /**
     * @param resourceProviderId
     *            the resourceProviderId to set
     */
    public void setResourceProviderId(final String resourceProviderId) {
        this.resourceProviderId = resourceProviderId;
    }

    public ExtManagedVirtualLinkData withResourceProviderId(final String value) {
        this.resourceProviderId = value;
        return this;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(final String value) {
        this.resourceId = value;
    }

    public ExtManagedVirtualLinkData withResourceId(final String value) {
        this.resourceId = value;
        return this;
    }

    /**
     * @return the virtualLinkDescId
     */
    public String getVirtualLinkDescId() {
        return virtualLinkDescId;
    }

    /**
     * @param virtualLinkDescId
     *            the virtualLinkDescId to set
     */
    public void setVirtualLinkDescId(final String virtualLinkDescId) {
        this.virtualLinkDescId = virtualLinkDescId;
    }

    public ExtManagedVirtualLinkData withVirtualLinkDescId(final String value) {
        this.virtualLinkDescId = value;
        return this;
    }

}
