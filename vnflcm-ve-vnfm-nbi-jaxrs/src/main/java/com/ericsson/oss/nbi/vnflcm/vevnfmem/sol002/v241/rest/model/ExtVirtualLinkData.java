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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtVirtualLinkData {

    protected String id;
    protected String vimConnectionId;
    protected String resourceProviderId;
    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String resourceId;
    @Valid
    @NotNull(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected List<VnfExtCpData> extCps = new ArrayList<VnfExtCpData>();
    @Valid
    protected List<ExtLinkPortData> extLinkPorts = new ArrayList<ExtLinkPortData>();

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        this.id = value;
    }

    public ExtVirtualLinkData withId(final String value) {
        this.id = value;
        return this;
    }

    public String getVimConnectionId() {
        return vimConnectionId;
    }

    public void setVimConnectionId(final String value) {
        this.vimConnectionId = value;
    }

    public ExtVirtualLinkData withVimConnectionId(final String value) {
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

    public ExtVirtualLinkData withResourceProviderId(final String value) {
        this.resourceProviderId = value;
        return this;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(final String value) {
        this.resourceId = value;
    }

    public ExtVirtualLinkData withResourceId(final String value) {
        this.resourceId = value;
        return this;
    }

    public List<VnfExtCpData> getExtCps() {
        return extCps;
    }

    public void setExtCps(final List<VnfExtCpData> value) {
        this.extCps = value;
    }

    public ExtVirtualLinkData withExtCps(final List<VnfExtCpData> value) {
        this.extCps = value;
        return this;
    }

    /**
     * @return the extLinkPorts
     */
    public List<ExtLinkPortData> getExtLinkPorts() {
        return extLinkPorts;
    }

    /**
     * @param extLinkPorts
     *            the extLinkPorts to set
     */
    public void setExtLinkPorts(final List<ExtLinkPortData> extLinkPorts) {
        this.extLinkPorts = extLinkPorts;
    }

    public ExtVirtualLinkData withExtLinkPorts(final List<ExtLinkPortData> value) {
        this.extLinkPorts = value;
        return this;
    }

}
