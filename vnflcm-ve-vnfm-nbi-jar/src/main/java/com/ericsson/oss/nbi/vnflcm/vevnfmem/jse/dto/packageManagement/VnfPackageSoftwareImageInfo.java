/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VnfPackageSoftwareImageInfo {

    protected String id;
    protected String name;
    protected String provider;
    protected String version;
    protected Checksum checksum;
    protected ContainerFormat containerFormat;
    protected DiskFormat diskFormat;
    protected Date createdAt;
    protected Long minDisk;
    protected Long minRam;
    protected Long size;
    protected String imagePath ;

    public String getId() {
        return id;
    }

    public void setId(final String value) {
        id = value;
    }

    public VnfPackageSoftwareImageInfo withId(final String value) {
        id = value;
        return this;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    public VnfPackageSoftwareImageInfo withName(final String value) {
        name = value;
        return this;
    }

    /**
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(final String provider) {
        this.provider = provider;
    }

    public VnfPackageSoftwareImageInfo withProvider(final String value) {
        provider = value;
        return this;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    public VnfPackageSoftwareImageInfo withVersion(final String value) {
        version = value;
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

    public VnfPackageSoftwareImageInfo withChecksum(final Checksum value) {
        checksum = value;
        return this;
    }
    /**
     * @return the containerFormat
     */
    public ContainerFormat getContainerFormat() {
        return containerFormat;
    }

    /**
     * @param containerFormat the containerFormat to set
     */
    public void setContainerFormat(final ContainerFormat containerFormat) {
        this.containerFormat = containerFormat;
    }

    public VnfPackageSoftwareImageInfo withContainerFormat(final ContainerFormat value) {
        containerFormat = value;
        return this;
    }
    /**
     * @return the diskFormat
     */
    public DiskFormat getDiskFormat() {
        return diskFormat;
    }

    /**
     * @param diskFormat the diskFormat to set
     */
    public void setDiskFormat(final DiskFormat diskFormat) {
        this.diskFormat = diskFormat;
    }

    public VnfPackageSoftwareImageInfo withContainerFormat(final DiskFormat value) {
        diskFormat = value;
        return this;
    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    public VnfPackageSoftwareImageInfo withCreatedAt(final Date value) {
        createdAt = value;
        return this;
    }

    /**
     * @return the minDisk
     */
    public Long getMinDisk() {
        return minDisk;
    }

    /**
     * @param minDisk the minDisk to set
     */
    public void setMinDisk(Long minDisk) {
        this.minDisk = minDisk;
    }

    public VnfPackageSoftwareImageInfo withCreatedAt(final Long value) {
        minDisk = value;
        return this;
    }

    /**
     * @return the minRam
     */
    public Long getMinRam() {
        return minRam;
    }

    /**
     * @param minRam the minRam to set
     */
    public void setMinRam(final Long minRam) {
        this.minRam = minRam;
    }

    public VnfPackageSoftwareImageInfo withMinRam(final Long value) {
        minRam = value;
        return this;
    }

    /**
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(final Long size) {
        this.size = size;
    }

    public VnfPackageSoftwareImageInfo withSize(final Long value) {
        size = value;
        return this;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(final String imagePath) {
        this.imagePath = imagePath;
    }

    public VnfPackageSoftwareImageInfo withImagePath(final String value) {
        imagePath = value;
        return this;
    }
}
