/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtManagedVirtualLinkData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtVirtualLinkData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimConnectionInfo;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NbiChangeVnfRequest {

    @Valid
    protected List<ExtVirtualLinkData> extVirtualLinks = new ArrayList<ExtVirtualLinkData>();

    @Valid
    protected List<ExtManagedVirtualLinkData> extManagedVirtualLinks = new ArrayList<ExtManagedVirtualLinkData>();

    // Additional parameters are stored in this map
    private final Map<String, Object> additionalParams = new HashMap<String, Object>();

    @Valid
    protected List<VimConnectionInfo> vimConnectionInfo = new ArrayList<VimConnectionInfo>();

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String VnfdId;

    @Valid
    private Map<String, Object> vnfConfigurableProperties = new HashMap<String, Object>();;

    @Valid
    private Map<String, Object> extensions = new HashMap<String, Object>();;

    public List<ExtVirtualLinkData> getExtVirtualLinks() {
        return extVirtualLinks;
    }

    public void setExtVirtualLinks(final List<ExtVirtualLinkData> value) {
        this.extVirtualLinks=value;
    }

    public NbiChangeVnfRequest withExtVirtualLinks(final List<ExtVirtualLinkData> value) {
        this.extVirtualLinks=value;
        return this;
    }

    /**
     * Returns the additional attributes that are to used during ccvp
     * 
     * @return the additionalAttributes
     */
    public Map<String, Object> getAdditionalParams() {
        return additionalParams;
    }

    /**
     * Used to set the additional attributes that are to be used during ccvp
     * 
     * @param additionalAttributes
     *            the additionalAttributes to set
     */
    public void setAdditionalParams(final Map<String, Object> additionalParams) {
        this.additionalParams.putAll(additionalParams);
    }

    public List<VimConnectionInfo> getVimConnectionInfo() {
        return vimConnectionInfo;
    }

    public void setVimConnectionInfo(final List<VimConnectionInfo> value) {
        this.vimConnectionInfo=value;
    }

    public NbiChangeVnfRequest withVimConnectionInfo(final List<VimConnectionInfo> value) {
        this.vimConnectionInfo=value;
        return this;
    }

    /**
     * @return the extManagedVirtualLinks
     */
    public List<ExtManagedVirtualLinkData> getExtManagedVirtualLinks() {
        return extManagedVirtualLinks;
    }

    /**
     * @param extManagedVirtualLinks the extManagedVirtualLinks to set
     */
    public void setExtManagedVirtualLinks(
            final List<ExtManagedVirtualLinkData> extManagedVirtualLinks) {
        this.extManagedVirtualLinks = extManagedVirtualLinks;
    }

    public NbiChangeVnfRequest withExtManagedVirtualLinks(final List<ExtManagedVirtualLinkData> value) {
        this.extManagedVirtualLinks=value;
        return this;
    }

    public String getVnfdId() {
        return VnfdId;
    }

    public void setVnfdId(final String vnfdId) {
        this.VnfdId = vnfdId;
    }

    public Map<String, Object> getVnfConfigurableProperties() {
        return vnfConfigurableProperties;
    }

    public void setVnfConfigurableProperties(final Map<String, Object> vnfConfigurableProperties) {
        this.vnfConfigurableProperties.putAll(vnfConfigurableProperties);
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(final Map<String, Object> extensions) {
        this.extensions.putAll(extensions);;
    }
}
