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

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VimConnectionInfo {

    protected String id;
    protected String vimId;

    protected String vimType;

    @Valid
    protected InterfaceInfo interfaceInfo;

    @Valid
    protected AccessInfo accessInfo;
    protected KeyValuePairs extra;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VimConnectionInfo withId(final String value) {
        id = value;
        return this;
    }

    public String getVimId() {
        return vimId;
    }

    public void setVimId(final String value) {
        vimId = value;
    }

    public VimConnectionInfo withVimId(final String value) {
        vimId = value;
        return this;
    }

    public String getVimType() {
        return vimType;
    }

    public void setVimType(final String vimType) {
        this.vimType = vimType;
    }

    public InterfaceInfo getInterfaceInfo() {
        return interfaceInfo;
    }

    public void setInterfaceInfo(final InterfaceInfo value) {
        interfaceInfo = value;
    }

    public VimConnectionInfo withInterfaceInfo(final InterfaceInfo value) {
        interfaceInfo = value;
        return this;
    }

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(final AccessInfo value) {
        accessInfo = value;
    }

    public VimConnectionInfo withAccessInfo(final AccessInfo value) {
        accessInfo = value;
        return this;
    }

    public KeyValuePairs getExtra() {
        return extra;
    }

    public void setExtra(final KeyValuePairs value) {
        extra = value;
    }

    public VimConnectionInfo withExtra(final KeyValuePairs value) {
        extra = value;
        return this;
    }

    @Override
    public String toString() {
        return "VimConnection [id=" + id + ",vimId=" + vimId + ",vimType=" + vimType + ",interfaceInfo=" + interfaceInfo
                + ",accessInfo=" + accessInfo + "]";
    }
}
