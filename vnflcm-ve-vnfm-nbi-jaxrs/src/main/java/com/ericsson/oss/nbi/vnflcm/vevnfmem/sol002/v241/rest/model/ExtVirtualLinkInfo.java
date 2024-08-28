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
public class ExtVirtualLinkInfo {

    protected String id;
    protected ResourceHandle resourceHandle;
    protected List<ExtLinkPortInfo> extLinkPorts = new ArrayList<ExtLinkPortInfo>();

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public ExtVirtualLinkInfo withId(final String value) {
        id = value;
        return this;
    }

    public ResourceHandle getResourceHandle() {
        return resourceHandle;
    }

    public void setResourceHandle(final ResourceHandle value) {
        resourceHandle = value;
    }

    public ExtVirtualLinkInfo withResourceHandle(final ResourceHandle value) {
        resourceHandle = value;
        return this;
    }

    /**
     * @return the extLinkPorts
     */
    public List<ExtLinkPortInfo> getExtLinkPorts() {
        return extLinkPorts;
    }

    /**
     * @param extLinkPorts
     *            the extLinkPorts to set
     */
    public void setExtLinkPorts(final List<ExtLinkPortInfo> extLinkPorts) {
        this.extLinkPorts = extLinkPorts;
    }

    public ExtVirtualLinkInfo withLinkPorts(final List<ExtLinkPortInfo> value) {
        extLinkPorts = value;
        return this;
    }

}
