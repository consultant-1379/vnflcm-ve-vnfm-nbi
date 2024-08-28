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
public class InstantiateVnfRequest {

    @Valid
    protected List<ExtVirtualLinkData> extVirtualLinks = new ArrayList<ExtVirtualLinkData>();

    @Valid
    protected List<ExtManagedVirtualLinkData> extManagedVirtualLinks = new ArrayList<ExtManagedVirtualLinkData>();

    // Additional parameters are stored in this map
    private final Map<String, Object> additionalParams = new HashMap<String, Object>();

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String flavourId;

    private String instantiationLevelId;

 @Valid protected List<VimConnectionInformation> vimConnectionInfo = new ArrayList<VimConnectionInformation>();



    public List<VimConnectionInformation> getVimConnectionInfo() {
        return vimConnectionInfo;
}

    public void setVimConnectionInfo(final List<VimConnectionInformation> vimConnectionInfo) {
        this.vimConnectionInfo = vimConnectionInfo;
}

    protected String localizationLanguage;

    public List<ExtVirtualLinkData> getExtVirtualLinks() {
        return extVirtualLinks;
    }

    public void setExtVirtualLinks(final List<ExtVirtualLinkData> value) {
        this.extVirtualLinks = value;
    }

    public InstantiateVnfRequest withExtVirtualLinks(final List<ExtVirtualLinkData> value) {
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

    public String getFlavourId() {
        return flavourId;
    }

    public void setFlavourId(final String value) {
        this.flavourId = value;
    }

    public InstantiateVnfRequest withFlavourId(final String value) {
        this.flavourId = value;
        return this;
    }

    public String getInstantiationLevelId() {
        return instantiationLevelId;
    }

    public void setInstantiationLevelId(final String instantiationLevelId) {
        this.instantiationLevelId = instantiationLevelId;
    }

    public InstantiateVnfRequest withInstantiationLevelId(final String value) {
        this.instantiationLevelId = value;
        return this;
    }

    /*
     * public List<VimConnectionInfo> getVimConnectionInfo() { return vimConnectionInfo; }
     *
     * public void setVimConnectionInfo(final List<VimConnectionInfo> value) { this.vimConnectionInfo=value; }
     *
     * public InstantiateVnfRequest withVimConnectionInfo(final List<VimConnectionInfo> value) { this.vimConnectionInfo=value; return this; }
     */

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

    public InstantiateVnfRequest withExtManagedVirtualLinks(final List<ExtManagedVirtualLinkData> value) {
        this.extManagedVirtualLinks = value;
        return this;
    }

    /**
     * @return the localizationLanguage
     */
    public String getLocalizationLanguage() {
        return localizationLanguage;
    }

    /**
     * @param localizationLanguage
     *            the localizationLanguage to set
     */
    public void setLocalizationLanguage(final String localizationLanguage) {
        this.localizationLanguage = localizationLanguage;
    }

    public InstantiateVnfRequest withLocalizationLanguage(final String value) {
        this.localizationLanguage = value;
        return this;
    }

}
