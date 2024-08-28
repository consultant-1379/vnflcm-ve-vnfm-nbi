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

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class VnfInstance {

    protected String id;
    protected String vnfInstanceName;
    protected String vnfInstanceDescription;
    protected String vnfdId;
    protected String vnfProvider;
    protected String vnfProductName;
    protected String vnfSoftwareVersion;
    protected String vnfdVersion;
    protected String vnfPkgId;
    protected Map<String, Object> vnfConfigurableProperties;
    protected InstantiationState instantiationState;
    protected InstantiatedVnfInfo instantiatedVnfInfo;
    protected Map<String, Object> metadata;
    protected Map<String, Object> extensions;
    protected VnfInstance_links _links;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VnfInstance withId(final String value) {
        id = value;
        return this;
    }

    public String getVnfInstanceName() {
        return vnfInstanceName;
    }

    public void setVnfInstanceName(final String value) {
        vnfInstanceName = value;
    }

    public VnfInstance withVnfInstanceName(final String value) {
        vnfInstanceName = value;
        return this;
    }

    public String getVnfInstanceDescription() {
        return vnfInstanceDescription;
    }

    public void setVnfInstanceDescription(final String value) {
        vnfInstanceDescription = value;
    }

    public VnfInstance withVnfInstanceDescription(final String value) {
        vnfInstanceDescription = value;
        return this;
    }

    public String getVnfdId() {
        return vnfdId;
    }

    public void setVnfdId(final String value) {
        vnfdId = value;
    }

    public VnfInstance withVnfdId(final String value) {
        vnfdId = value;
        return this;
    }

    public String getVnfProvider() {
        return vnfProvider;
    }

    public void setVnfProvider(final String value) {
        vnfProvider = value;
    }

    public VnfInstance withVnfProvider(final String value) {
        vnfProvider = value;
        return this;
    }

    public String getVnfProductName() {
        return vnfProductName;
    }

    public void setVnfProductName(final String value) {
        vnfProductName = value;
    }

    public VnfInstance withVnfProductName(final String value) {
        vnfProductName = value;
        return this;
    }

    public String getVnfSoftwareVersion() {
        return vnfSoftwareVersion;
    }

    public void setVnfSoftwareVersion(final String value) {
        vnfSoftwareVersion = value;
    }

    public VnfInstance withVnfSoftwareVersion(final String value) {
        vnfSoftwareVersion = value;
        return this;
    }

    public String getVnfdVersion() {
        return vnfdVersion;
    }

    public void setVnfdVersion(final String value) {
        vnfdVersion = value;
    }

    public VnfInstance withVnfdVersion(final String value) {
        vnfdVersion = value;
        return this;
    }

    public String getVnfPkgId() {
        return vnfPkgId;
    }

    public void setVnfPkgId(final String value) {
        vnfPkgId = value;
    }

    public VnfInstance withVnfPkgId(final String value) {
        vnfPkgId = value;
        return this;
    }

    public Map<String, Object> getVnfConfigurableProperties() {
        return vnfConfigurableProperties;
    }

    public void setVnfConfigurableProperties(final Map<String, Object> value) {
        vnfConfigurableProperties = value;
    }

    public VnfInstance withVnfConfigurableProperties(final Map<String, Object> value) {
        vnfConfigurableProperties = value;
        return this;
    }

    public InstantiationState getInstantiationState() {
        return instantiationState;
    }

    public void setInstantiationState(final InstantiationState value) {
        instantiationState = value;
    }

    public VnfInstance withInstantiationState(final InstantiationState value) {
        instantiationState = value;
        return this;
    }

    public InstantiatedVnfInfo getInstantiatedVnfInfo() {
        return instantiatedVnfInfo;
    }

    public void setInstantiatedVnfInfo(final InstantiatedVnfInfo value) {
        instantiatedVnfInfo = value;
    }

    public VnfInstance withInstantiatedVnfInfo(final InstantiatedVnfInfo value) {
        instantiatedVnfInfo = value;
        return this;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(final Map<String, Object> value) {
        metadata = value;
    }

    public VnfInstance withMetadata(final Map<String, Object> value) {
        metadata = value;
        return this;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(final Map<String, Object> value) {
        extensions = value;
    }

    public VnfInstance withExtensions(final Map<String, Object> value) {
        extensions = value;
        return this;
    }

    public VnfInstance_links get_links() {
        return _links;
    }

    public void set_links(final VnfInstance_links value) {
        _links = value;
    }

    public VnfInstance with_links(final VnfInstance_links value) {
        _links = value;
        return this;
    }

    @Override
    public String toString() {
        return "VnfInstance [id=" + id + ", vnfInstanceName=" + vnfInstanceName + ", vnfInstanceDescription=" + vnfInstanceDescription + ", vnfdId="
                + vnfdId + ", vnfProvider=" + vnfProvider + ", vnfProductName=" + vnfProductName + ", vnfSoftwareVersion=" + vnfSoftwareVersion
                + ", vnfdVersion=" + vnfdVersion + ", vnfPkgId=" + vnfPkgId + ", vnfConfigurableProperties=" + vnfConfigurableProperties
                + ", instantiationState=" + instantiationState + ", instantiatedVnfInfo=" + instantiatedVnfInfo + ", metadata=" + metadata
                + ", extensions=" + extensions + ", _links=" + _links + "]";
    }
}
