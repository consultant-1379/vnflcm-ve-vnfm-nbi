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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Additional params for modify VNF instance.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyVnfOpConfig {

    private String vnfdId;
    private String vnfSoftwareVersion;
    private String vnfdVersion;

    /**
     * @return the vnfdId
     */
    public String getVnfdId() {
        return vnfdId;
    }

    /**
     * @param vnfdId
     *            the vnfdId to set
     */
    public void setVnfdId(final String vnfdId) {
        this.vnfdId = vnfdId;
    }

    /**
     * @return the vnfSoftwareVersion
     */
    public String getVnfSoftwareVersion() {
        return vnfSoftwareVersion;
    }

    /**
     * @param vnfSoftwareVersion
     *            the vnfSoftwareVersion to set
     */
    public void setVnfSoftwareVersion(final String vnfSoftwareVersion) {
        this.vnfSoftwareVersion = vnfSoftwareVersion;
    }

    /**
     * @return the vnfdVersion
     */
    public String getVnfdVersion() {
        return vnfdVersion;
    }

    /**
     * @param vnfdVersion
     *            the vnfdVersion to set
     */
    public void setVnfdVersion(final String vnfdVersion) {
        this.vnfdVersion = vnfdVersion;
    }

    @Override
    public String toString() {
        return "ModifyVnfAdditionalParam [vnfdId=" + vnfdId + ", vnfSoftwareVersion=" + vnfSoftwareVersion + ", vnfdVersion=" + vnfdVersion + "]";
    }

}
