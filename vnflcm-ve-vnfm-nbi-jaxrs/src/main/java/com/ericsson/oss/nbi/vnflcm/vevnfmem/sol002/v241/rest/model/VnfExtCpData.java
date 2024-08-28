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

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VnfExtCpData {

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String cpdId;
    @NotNull(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected List<VnfExtCpConfig> cpConfig = new ArrayList<VnfExtCpConfig>();

    public String getCpdId() {
        return cpdId;
    }

    public void setCpdId(final String value) {
        this.cpdId = value;
    }

    public VnfExtCpData withCpdId(final String value) {
        this.cpdId = value;
        return this;
    }

    /**
     * @return the cpConfig
     */
    public List<VnfExtCpConfig> getCpConfig() {
        return cpConfig;
    }

    /**
     * @param cpConfig
     *            the cpConfig to set
     */
    public void setCpConfig(final List<VnfExtCpConfig> cpConfig) {
        this.cpConfig = cpConfig;
    }

    public VnfExtCpData withCpConfig(final List<VnfExtCpConfig> value) {
        this.cpConfig = value;
        return this;
    }
}
