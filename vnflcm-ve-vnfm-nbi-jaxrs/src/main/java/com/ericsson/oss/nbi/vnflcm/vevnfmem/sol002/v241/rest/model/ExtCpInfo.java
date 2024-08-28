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
public class ExtCpInfo {

    protected String id;
    protected String cpdId;
    protected List<CpProtocolInfo> cpProtocolInfo = new ArrayList<CpProtocolInfo>();
    protected String extLinkPortId;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public ExtCpInfo withId(final String value) {
        id = value;
        return this;
    }

    public String getCpdId() {
        return cpdId;
    }

    public void setCpdId(final String value) {
        cpdId = value;
    }

    public ExtCpInfo withCpdId(final String value) {
        cpdId = value;
        return this;
    }

    /**
     * @return the cpProtocolInfo
     */
    public List<CpProtocolInfo> getCpProtocolInfo() {
        return cpProtocolInfo;
    }

    /**
     * @param cpProtocolInfo
     *            the cpProtocolInfo to set
     */
    public void setCpProtocolInfo(final List<CpProtocolInfo> cpProtocolInfo) {
        this.cpProtocolInfo = cpProtocolInfo;
    }

    public ExtCpInfo withCpProtocolInfo(final List<CpProtocolInfo> value) {
        cpProtocolInfo = value;
        return this;
    }

    public String getExtLinkPortId() {
        return extLinkPortId;
    }

    public void setExtLinkPortId(final String value) {
        extLinkPortId = value;
    }

    public ExtCpInfo withExtLinkPortId(final String value) {
        extLinkPortId = value;
        return this;
    }
}
