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

import javax.validation.Valid;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VnfExtCpConfig {

    protected String cpInstanceId;
    protected String linkPortId;
    @Valid
    protected List<CpProtocolData> cpProtocolData = new ArrayList<CpProtocolData>();

    /**
     * @return the cpInstanceId
     */
    public String getCpInstanceId() {
        return cpInstanceId;
    }

    /**
     * @param cpInstanceId
     *            the cpInstanceId to set
     */
    public void setCpInstanceId(final String cpInstanceId) {
        this.cpInstanceId = cpInstanceId;
    }

    public VnfExtCpConfig withCpInstanceId(final String value) {
        cpInstanceId = value;
        return this;
    }

    /**
     * @return the linkPortId
     */
    public String getLinkPortId() {
        return linkPortId;
    }

    /**
     * @param linkPortId
     *            the linkPortId to set
     */
    public void setLinkPortId(final String linkPortId) {
        this.linkPortId = linkPortId;
    }

    public VnfExtCpConfig withLinkPortId(final String value) {
        linkPortId = value;
        return this;
    }

    /**
     * @return the cpProtocolData
     */
    public List<CpProtocolData> getCpProtocolData() {
        return cpProtocolData;
    }

    /**
     * @param cpProtocolData
     *            the cpProtocolData to set
     */
    public void setCpProtocolData(final List<CpProtocolData> cpProtocolData) {
        this.cpProtocolData = cpProtocolData;
    }

    public VnfExtCpConfig withCpProtocolData(final List<CpProtocolData> value) {
        cpProtocolData = value;
        return this;
    }

}
