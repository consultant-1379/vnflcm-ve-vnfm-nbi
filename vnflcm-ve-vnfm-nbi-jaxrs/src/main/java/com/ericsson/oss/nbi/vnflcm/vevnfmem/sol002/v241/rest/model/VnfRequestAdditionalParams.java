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

/*
 * Additional params for create VNF instance.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VnfRequestAdditionalParams {

    //Identifier of information held by the NFVO about the specific VNF package on which the VNF is based.
    //  @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    private String vnfPkgId;

    private String onboardedVnfPkgInfoId;

    //Name of the VNF HOT package.
    // This validation is commented temporarly to continue the ECM testing, this to be uncommented
    //@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    private String hotPackageName;

    public String getVnfPkgId() {
        return vnfPkgId;
    }

    public void setVnfPkgId(final String vnfPkgId) {
        this.vnfPkgId = vnfPkgId;
    }

    public String getHotPackageName() {
        return hotPackageName;
    }

    public void setHotPackageName(final String hotPackageName) {
        this.hotPackageName = hotPackageName;
    }

    public String getOnboardedVnfPkgInfoId() {
        return onboardedVnfPkgInfoId;
    }

    public void setOnboardedVnfPkgInfoId(String onboardedVnfPkgInfoId) {
        this.onboardedVnfPkgInfoId = onboardedVnfPkgInfoId;
    }

    @Override
    public String toString() {
        return "VnfRequestAdditionalParams [vnfPkgId=" + vnfPkgId + ", onboardedVnfPkgInfoId=" + onboardedVnfPkgInfoId
                + ", hotPackageName=" + hotPackageName + "]";
    }
}
