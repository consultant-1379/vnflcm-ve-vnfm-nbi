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

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyVnfRequest {

    private String vnfInstanceName;

    private String vnfInstanceDescription;

    private String onboardedVnfPkgInfoId;

    private final Map<String, Object> vnfConfigurableProperties = new HashMap<String, Object>();

    private Map<String, Object> metadata = new HashMap<String, Object>();

    private Map<String, Object> extensions = new HashMap<String, Object>();

    private ModifyVnfOpConfig additionalParams;

    /**
     * @return the vnfInstanceName
     */
    public String getVnfInstanceName() {
        return vnfInstanceName;
    }

    /**
     * @param vnfInstanceName
     *            the vnfInstanceName to set
     */
    public void setVnfInstanceName(final String vnfInstanceName) {
        this.vnfInstanceName = vnfInstanceName;
    }

    /**
     * @return the vnfInstanceDescription
     */
    public String getVnfInstanceDescription() {
        return vnfInstanceDescription;
    }

    /**
     * @param vnfInstanceDescription
     *            the vnfInstanceDescription to set
     */
    public void setVnfInstanceDescription(final String vnfInstanceDescription) {
        this.vnfInstanceDescription = vnfInstanceDescription;
    }

    /**
     * @return the onboardedVnfPkgInfoId
     */
    public String getOnboardedVnfPkgInfoId() {
        return onboardedVnfPkgInfoId;
    }

    /**
     * @param onboardedVnfPkgInfoId
     *            the onboardedVnfPkgInfoId to set
     */
    public void setOnboardedVnfPkgInfoId(final String onboardedVnfPkgInfoId) {
        this.onboardedVnfPkgInfoId = onboardedVnfPkgInfoId;
    }

    /**
     * @return the vnfConfigurableProperties
     */
    public Map<String, Object> getVnfConfigurableProperties() {
        return vnfConfigurableProperties;
    }

    /**
     * @param vnfConfigurableProperties
     *            the vnfConfigurableProperties to set
     */
    public void setVnfConfigurableProperties(final Map<String, Object> vnfConfigurableProperties) {
        this.vnfConfigurableProperties.putAll(vnfConfigurableProperties);
    }

    /**
     * @return the metaData
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * /**
     * 
     * @param metaData
     *            the metadata to set
     */
    public void setMetadata(final Map<String, Object> metaData) {
        this.metadata.putAll(metaData);
    }

    /**
     * @return the extensions
     */
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * @param extensions
     *            the extensions to set
     */
    public void setExtensions(final Map<String, Object> extensions) {
        this.extensions.putAll(extensions);
    }

    /**
     * @return the additionalParams
     */
    public ModifyVnfOpConfig getAdditionalParams() {
        return additionalParams;
    }

    /**
     * @param additionalParams
     *            the additionalParams to set
     */
    public void setAdditionalParams(final ModifyVnfOpConfig additionalParams) {
        this.additionalParams = additionalParams;
    }

    @Override
    public String toString() {
        return "VnfInfoModifications [vnfInstanceName=" + vnfInstanceName + ", vnfInstanceDescription=" + vnfInstanceDescription
                + ", onboardedVnfPkgInfoId=" + onboardedVnfPkgInfoId + ", vnfConfigurableProperties=" + vnfConfigurableProperties + ", metadata="
                + metadata + ", extensions=" + extensions + ", additionalParams=" + additionalParams + "]";
    }

}
