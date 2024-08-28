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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.ericsson.oss.services.vnflcm.api_base.dto.KeyValuePairs;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class  VnfPackageArtifactInfo {

    protected String artifactPath;
    protected Checksum checksum;
    protected KeyValuePairs metadata;

    public String getArtifactPath() {
        return artifactPath;
    }

    public void setArtifactPath(final String value) {
        artifactPath = value;
    }

    public VnfPackageArtifactInfo withArtifactPath(final String value) {
        artifactPath = value;
        return this;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public void setChecksum(final Checksum value) {
        checksum = value;
    }

    public VnfPackageArtifactInfo withChecksum(final Checksum value) {
        checksum = value;
        return this;
    }

    public KeyValuePairs getMetadata() {
        return metadata;
    }

    public void setMetadata(final KeyValuePairs value) {
        metadata = value;
    }

    public VnfPackageArtifactInfo withMetadata(final KeyValuePairs value) {
        metadata = value;
        return this;
    }

    @Override
    public String toString() {
        return "VnfPackageArtifactInfo [artifactPath=" + artifactPath + ", metadata="
                + metadata + "]";
    }
}
