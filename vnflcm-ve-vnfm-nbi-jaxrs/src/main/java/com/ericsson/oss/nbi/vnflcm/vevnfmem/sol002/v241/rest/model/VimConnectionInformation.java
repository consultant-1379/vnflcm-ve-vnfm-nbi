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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

//import com.ericsson.oss.services.nbi.vnflcm.rest.validation.AppValidationMessages;
//import com.ericsson.oss.services.nbi.vnflcm.rest.validation.EnumType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VimConnectionInformation {

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String id;
    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String vimId;

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String vimType;

    protected Map<String, Object> interfaceInfo = new HashMap<String, Object>();

    protected Map<String, Object> accessInfo = new HashMap<String, Object>();
    protected Map<String, Object> extra = new HashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VimConnectionInformation withId(final String value) {
        id = value;
        return this;
    }

    public String getVimId() {
        return vimId;
    }

    public void setVimId(final String value) {
        vimId = value;
    }

    public VimConnectionInformation withVimId(final String value) {
        vimId = value;
        return this;
    }

    public String getVimType() {
        return vimType;
    }

    public void setVimType(final String value) {
        vimType = value;
    }

    public VimConnectionInformation withVimType(final String value) {
        vimType = value;
        return this;
    }

    public Map<String, Object> getInterfaceInfo() {
        return interfaceInfo;
    }

    public void setInterfaceInfo(final Map<String, Object> value) {
        interfaceInfo = value;
    }

    public VimConnectionInformation withInterfaceInfo(final Map<String, Object> value) {
        interfaceInfo = value;
        return this;
    }

    public Map<String, Object> getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(final Map<String, Object> value) {
        accessInfo = value;
    }

    public VimConnectionInformation withAccessInfo(final Map<String, Object> value) {
        accessInfo = value;
        return this;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(final Map<String, Object> value) {
        extra = value;
    }

    public VimConnectionInformation withExtra(final Map<String, Object> value) {
        extra = value;
        return this;
    }

}
