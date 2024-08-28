/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.ericsson.oss.services.vnflcm.api_base.dto.KeyValuePairs;

/**
 * OnboardedVnfPkgInfo parameters passed by the NFVO as input to package Management and query package,
 * JSON Structure of this object will be
 * <pre>
 *{
   "id":"id",
   "vnfdId": "vnflaf-test13",
   "vnfProvider":"vnfProvider",
   "vnfProductName":"vnfProductName",
   "vnfSoftwareVersion":"vnfSoftwareVersion",
   "vnfdVersion":"vnfdVersion",
   "checksum": {"algorithm": "algorithm","hash": "hash"},
   "softwareImages":[
      {
         "id":"id",
         "name":"ericsson",
         "provider":"ericsson",
         "checksum": {"algorithm": "algorithm","hash": "hash"},
         "containerFormat":"OVF"
      }
   ],
   "onboardingState":"CREATED",
   "operationalState":"ENABLED",
   "usageState":"NOT_IN_USE",
   "userDefinedData": {"key":"value"},
   "_links":[{
      "self":{
         "href":"vnflcm/vnflcm/v1/vnf_instances"
      },
      "vnfd":{
         "href":"vnflcm/vnflcm/v1/vnf_instances"
      },
      "packageContent":{
         "href":"vnflcm/vnflcm/v1/vnf_instances"
      }}
   ],
   "additionalArtifacts":[
      {
         "checksum": {"algorithm": "algorithm","hash": "hash"},
         "artifactPath":"Resources/VnfdWrapperFiles/VNFD_Wrapper_VNFLAF.json",
         "metadata":{
            "key":"value"
         }
      },
      {
         "checksum": {"algorithm": "algorithm","hash": "hash"},
         "artifactPath":"vnflaf_cee.yaml",
         "metadata":{
            "key":"value"
         }
      }
   ],"deletionPending": "true"
}


 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OnboardedVnfPkgInfo implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String vnfdId;
    protected List<VnfPackageArtifactInfo> additionalArtifacts = new ArrayList<VnfPackageArtifactInfo>();
    protected String vnfProvider;
    protected String vnfProductName;
    protected String vnfSoftwareVersion;
    protected String vnfdVersion;
    protected Checksum checksum;
    protected List<VnfPackageSoftwareImageInfo> softwareImages = new ArrayList<VnfPackageSoftwareImageInfo>(); 
    protected PackageOnboardingState onboardingState;
    protected PackageOperationalState operationalState;
    protected PackageUsageState usageState;
    protected KeyValuePairs userDefinedData;
    protected VnfPkgInfoLinks _links;
    protected boolean deletionPending;
    /**
     * @return the additionalArtifacts
     */
    public List<VnfPackageArtifactInfo> getAdditionalArtifacts() {
        return additionalArtifacts;
    }

    /**
     * @param additionalArtifacts
     *            the additionalArtifacts to set
     */
    public void setAdditionalArtifacts(
            final List<VnfPackageArtifactInfo> additionalArtifacts) {
        this.additionalArtifacts = additionalArtifacts;
    }

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public OnboardedVnfPkgInfo withId(final String value) {
        id = value;
        return this;
    }

    public String getVnfdId() {
        return vnfdId;
    }

    public void setVnfdId(final String value) {
        vnfdId = value;
    }

    public OnboardedVnfPkgInfo withVnfdId(final String value) {
        vnfdId = value;
        return this;
    }

    /**
     * @return the vnfProvider
     */
    public String getVnfProvider() {
        return vnfProvider;
    }

    /**
     * @param vnfProvider
     *            the vnfProvider to set
     */
    public void setVnfProvider(final String vnfProvider) {
        this.vnfProvider = vnfProvider;
    }

    public OnboardedVnfPkgInfo withVnfProvider(final String value) {
        vnfProvider = value;
        return this;
    }

    /**
     * @return the vnfProductName
     */
    public String getVnfProductName() {
        return vnfProductName;
    }

    /**
     * @param vnfProductName
     *            the vnfProductName to set
     */
    public void setVnfProductName(final String vnfProductName) {
        this.vnfProductName = vnfProductName;
    }

    public OnboardedVnfPkgInfo withVnfProductName(final String value) {
        vnfProductName = value;
        return this;
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

    public OnboardedVnfPkgInfo withVnfSoftwareVersion(final String value) {
        vnfSoftwareVersion = value;
        return this;
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

    public OnboardedVnfPkgInfo withVnfdVersion(final String value) {
        vnfdVersion = value;
        return this;
    }
    /**
     * @return the checksum
     */
    public Checksum getChecksum() {
        return checksum;
    }

    /**
     * @param checksum the checksum to set
     */
    public void setChecksum(final Checksum checksum) {
        this.checksum = checksum;
    }

    public OnboardedVnfPkgInfo withChecksum(final Checksum value) {
        checksum = value;
        return this;
    }
    /**
     * @return the softwareImages
     */
    public List<VnfPackageSoftwareImageInfo> getSoftwareImages() {
        return softwareImages;
    }

    /**
     * @param softwareImages the softwareImages to set
     */
    public void setSoftwareImages(final List<VnfPackageSoftwareImageInfo> softwareImages) {
        this.softwareImages = softwareImages;
    }

    public OnboardedVnfPkgInfo withSoftwareImages(final List<VnfPackageSoftwareImageInfo> value) {
        softwareImages = value;
        return this;
    }
    /**
     * @return the onboardingState
     */
    public PackageOnboardingState getOnboardingState() {
        return onboardingState;
    }

    /**
     * @param onboardingState the onboardingState to set
     */
    public void setOnboardingState(final PackageOnboardingState onboardingState) {
        this.onboardingState = onboardingState;
    }

    public OnboardedVnfPkgInfo withOnboardingState(final PackageOnboardingState value) {
        onboardingState = value;
        return this;
    }
    /**
     * @return the operationalState
     */
    public PackageOperationalState getOperationalState() {
        return operationalState;
    }

    /**
     * @param operationalState the operationalState to set
     */
    public void setOperationalState(final PackageOperationalState operationalState) {
        this.operationalState = operationalState;
    }

    public OnboardedVnfPkgInfo withOperationalState(final PackageOperationalState value) {
        operationalState = value;
        return this;
    }
    /**
     * @return the usageState
     */
    public PackageUsageState getUsageState() {
        return usageState;
    }

    /**
     * @param usageState the usageState to set
     */
    public void setUsageState(final PackageUsageState usageState) {
        this.usageState = usageState;
    }

    public OnboardedVnfPkgInfo withUsageState(final PackageUsageState value) {
        usageState = value;
        return this;
    }
    /**
     * @return the userDefinedData
     */
    public KeyValuePairs getUserDefinedData() {
        return userDefinedData;
    }

    /**
     * @param userDefinedData the userDefinedData to set
     */
    public void setUserDefinedData(final KeyValuePairs userDefinedData) {
        this.userDefinedData = userDefinedData;
    }

    public OnboardedVnfPkgInfo withUserDefinedData(final KeyValuePairs value) {
        userDefinedData = value;
        return this;
    }
    /**
     * @return the _links
     */
    public VnfPkgInfoLinks get_links() {
        return _links;
    }

    /**
     * @param _links the _links to set
     */
    public void set_links(final VnfPkgInfoLinks _links) {
        this._links = _links;
    }

    public OnboardedVnfPkgInfo with_links(final VnfPkgInfoLinks value) {
        _links = value;
        return this;
    }

    /**
     * @return the deletionPending
     */
    public boolean getDeletionPending() {
        return deletionPending;
    }

    /**
     * @param deletionPending the deletionPending to set
     */
    public void setDeletionPending(final boolean deletionPending) {
        this.deletionPending = deletionPending;
    }

    public OnboardedVnfPkgInfo withDeletionPending(final boolean value) {
        deletionPending = value;
        return this;
    }

    @Override
    public String toString() {
        return "OnboardedVnfPkgInfo [id=" + id + ", vnfdId=" + vnfdId + ", additionalArtifacts=" + additionalArtifacts
                + ", vnfProvider=" + vnfProvider + ", vnfProductName=" + vnfProductName + ", vnfSoftwareVersion="
                + vnfSoftwareVersion + ", vnfdVersion=" + vnfdVersion + ", checksum=" + checksum + ", softwareImages="
                + softwareImages + ", onboardingState=" + onboardingState + ", operationalState=" + operationalState
                + ", usageState=" + usageState + ", userDefinedData=" + userDefinedData + ", _links=" + _links
                + ", deletionPending=" + deletionPending + "]";
    }
}
