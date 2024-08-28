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
public class VnfcCpInfo {

    protected String id;
    protected String cpdId;
    protected String vnfExtCpId;
    protected List<CpProtocolInfo> cpProtocolInfo = new ArrayList<CpProtocolInfo>();
    protected String vnfLinkPortId;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VnfcCpInfo withId(final String value) {
        id = value;
        return this;
    }

    public String getCpdId() {
        return cpdId;
    }

    public void setCpdId(final String value) {
        cpdId = value;
    }

    public VnfcCpInfo withCpdId(final String value) {
        cpdId = value;
        return this;
    }

    public String getVnfExtCpId() {
        return vnfExtCpId;
    }

    public void setVnfExtCpId(final String value) {
        vnfExtCpId = value;
    }

    public VnfcCpInfo withVnfExtCpId(final String value) {
        vnfExtCpId = value;
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

    public VnfcCpInfo withCpProtocolInfo(final List<CpProtocolInfo> value) {
        cpProtocolInfo = value;
        return this;
    }

    public String getVnfLinkPortId() {
        return vnfLinkPortId;
    }

    public void setVnfLinkPortId(final String value) {
        vnfLinkPortId = value;
    }

    public VnfcCpInfo withVnfLinkPortId(final String value) {
        vnfLinkPortId = value;
        return this;
    }

    @Override
    public String toString() {
        return "VnfcCpInfo [id=" + id + ", cpdId=" + cpdId + ", vnfExtCpId=" + vnfExtCpId + ", cpProtocolInfo=" + cpProtocolInfo + ", vnfLinkPortId="
                + vnfLinkPortId + "]";
    }
}
