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

import java.util.*;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeCurrentVnfPkgRequest {

    @Valid
    protected List<ExtVirtualLinkData> extVirtualLinks = new ArrayList<ExtVirtualLinkData>();

    @Valid
    protected List<ExtManagedVirtualLinkData> extManagedVirtualLinks = new ArrayList<ExtManagedVirtualLinkData>();

    // Additional parameters are stored in this map
    @Valid
    private final Map<String, Object> additionalParams = new HashMap<String, Object>();

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String VnfdId;

    @Valid
    private Map<String, Object> vnfConfigurableProperties = new HashMap<String, Object>();

    @Valid
    private Map<String, Object> extensions = new HashMap<String, Object>();

    @Valid
    protected List<VimConnectionInformation> vimConnectionInfo = new ArrayList<VimConnectionInformation>();

    public List<ExtVirtualLinkData> getExtVirtualLinks() {
        return extVirtualLinks;
    }

    public void setExtVirtualLinks(final List<ExtVirtualLinkData> value) {
        this.extVirtualLinks = value;
    }

    public ChangeCurrentVnfPkgRequest withExtVirtualLinks(final List<ExtVirtualLinkData> value) {
        this.extVirtualLinks = value;
        return this;
    }

    /**
     * Returns the additional attributes that are to used during instantiation
     *
     * @return the additionalAttributes
     */
    public Map<String, Object> getAdditionalParams() {
        return additionalParams;
    }

    /**
     * Used to set the additional attributes that are to be used during instantiation
     *
     * @param additionalAttributes
     *            the additionalAttributes to set
     */
    public void setAdditionalParams(final String key, final Object value) {
        this.additionalParams.put(key, value);
    }

    public String getVnfdId() {
        return VnfdId;
    }

    public void setVnfdId(final String value) {
        this.VnfdId = value;
    }

    public ChangeCurrentVnfPkgRequest withVnfdId(final String value) {
        this.VnfdId = value;
        return this;
    }

    /**
     * @return the extManagedVirtualLinks
     */
    public List<ExtManagedVirtualLinkData> getExtManagedVirtualLinks() {
        return extManagedVirtualLinks;
    }

    /**
     * @param extManagedVirtualLinks
     *            the extManagedVirtualLinks to set
     */
    public void setExtManagedVirtualLinks(final List<ExtManagedVirtualLinkData> extManagedVirtualLinks) {
        this.extManagedVirtualLinks = extManagedVirtualLinks;
    }

    public ChangeCurrentVnfPkgRequest withExtManagedVirtualLinks(final List<ExtManagedVirtualLinkData> value) {
        this.extManagedVirtualLinks = value;
        return this;
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
        this.vnfConfigurableProperties = vnfConfigurableProperties;
    }

    public ChangeCurrentVnfPkgRequest withVnfConfigurableProperties(final Map<String, Object> value) {
        this.vnfConfigurableProperties = value;
        return this;
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
        this.extensions = extensions;
    }

    public ChangeCurrentVnfPkgRequest withExtensions(final Map<String, Object> value) {
        this.extensions = value;
        return this;
    }

    /**
     * @return the vimConnectionInfo
     */
    public List<VimConnectionInformation> getVimConnectionInfo() {
        return vimConnectionInfo;
    }

    /**
     * @param vimConnectionInfo
     *            the vimConnectionInfo to set
     */
    public void setVimConnectionInfo(final List<VimConnectionInformation> vimConnectionInfo) {
        this.vimConnectionInfo = vimConnectionInfo;
    }

    public ChangeCurrentVnfPkgRequest withVimConnectionInfo(final List<VimConnectionInformation> value) {
        this.vimConnectionInfo = value;
        return this;
    }

}
