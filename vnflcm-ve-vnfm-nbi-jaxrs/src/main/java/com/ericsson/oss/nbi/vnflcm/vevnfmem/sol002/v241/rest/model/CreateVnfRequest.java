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

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Create Vnf request.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CreateVnfRequest {

    // identifier that identifies the VNFD which defines the VNF instance to be created
    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    private String vnfdId;

    // Human-readable name of the VNF instance to be created
    private String vnfInstanceName;

    // Human-readable description of the VNF instance to be created
    private String vnfInstanceDescription;

    //Additional params for create VNF instance.
    @Valid
    //@NotNull Mismatch with current spec, may be required in Package Management.
    private VnfRequestAdditionalParams additionalParams;

    public String getVnfdId() {
        return vnfdId;
    }

    public void setVnfdId(final String vnfdId) {
        this.vnfdId = vnfdId;
    }

    public String getVnfInstanceName() {
        return vnfInstanceName;
    }

    public void setVnfInstanceName(final String vnfInstanceName) {
        this.vnfInstanceName = vnfInstanceName;
    }

    public String getVnfInstanceDescription() {
        return vnfInstanceDescription;
    }

    public void setVnfInstanceDescription(final String vnfInstanceDescription) {
        this.vnfInstanceDescription = vnfInstanceDescription;
    }

    public VnfRequestAdditionalParams getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(final VnfRequestAdditionalParams additionalParams) {
        this.additionalParams = additionalParams;
    }

    @Override
    public String toString() {
        return "[ vnfdId = " + vnfdId + " ,vnfInstanceName =  " + vnfInstanceName + " ,vnfInstanceDescription =  " + vnfInstanceDescription
                + " ,additionalParams =  " + additionalParams != null ? additionalParams.toString() : "" + " ]";
    }
}
