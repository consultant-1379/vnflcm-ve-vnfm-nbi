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

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VnfInfoModificationRequest {

    private String vnfInstanceName;

    private String vnfInstanceDescription;

    private String vnfPkgId;

    private final Map<String, Object> vnfConfigurableProperties = new HashMap<String, Object>();

    private Map<String, Object> metadata = new HashMap<String, Object>();

    private Map<String, Object> extensions = new HashMap<String, Object>();

    private VnfcInfoModifications vnfcInfoModifications;

    private ModifyVnfOpConfig additionalParams;

    //private List<VimConnectionInfo> vimConnectionInfo = new ArrayList<VimConnectionInfo>();

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
     * @return the vnfPkgId
     */
    public String getVnfPkgId() {
        return vnfPkgId;
    }

    /**
     * @param vnfPkgId
     *            the vnfPkgId to set
     */
    public void setVnfPkgId(final String vnfPkgId) {
        this.vnfPkgId = vnfPkgId;
    }

    /**
     * @return the vnfConfigurableProperties
     */
    public Map<String, Object> getVnfConfigurableProperties() {
        return vnfConfigurableProperties;
    }

    /**
     * @param key
     * @param value
     *            the key value pair to set in vnfConfigurableProperties
     */
    public void setVnfConfigurableProperties(final String key, final Object value) {
        this.vnfConfigurableProperties.put(key, value);
    }

    /**
     * @return the metaData
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * @param key
     * @param value
     *            the key value pair to set in metadata
     */
    public void setMetadata(final String key, final Object value) {
        this.metadata.put(key, value);
    }

    /**
     * @return the extensions
     */
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * @param key
     * @param value
     *            the key value pair to set in extensions
     */
    public void setExtensions(final String key, final Object value) {
        this.extensions.put(key, value);
    }

    /*    *//**
             * @return the additionalParams
             */

      public ModifyVnfOpConfig getAdditionalParams() { return additionalParams; }

     /**
        * @param additionalParams
        *            the additionalParams to set
        */
            public void setAdditionalParams(final ModifyVnfOpConfig additionalParams) { this.additionalParams = additionalParams; }

    /**
     * @return the vnfcInfoModifications
     */
    public VnfcInfoModifications getVnfcInfoModifications() {
        return vnfcInfoModifications;
    }

    /**
     * @param vnfcInfoModifications
     *            the vnfcInfoModifications to set
     */
    public void setVnfcInfoModifications(final VnfcInfoModifications vnfcInfoModifications) {
        this.vnfcInfoModifications = vnfcInfoModifications;
    }

    @Override
    public String toString() {
        return "VnfInfoModificationRequest [vnfInstanceName=" + vnfInstanceName + ", vnfInstanceDescription="
                + vnfInstanceDescription + ", vnfPkgId=" + vnfPkgId + ", vnfConfigurableProperties="
                + vnfConfigurableProperties + ", metadata=" + metadata + ", extensions=" + extensions
                + ", vnfcInfoModifications=" + vnfcInfoModifications + ", additionalParams=" + additionalParams + "]";
    }
}
