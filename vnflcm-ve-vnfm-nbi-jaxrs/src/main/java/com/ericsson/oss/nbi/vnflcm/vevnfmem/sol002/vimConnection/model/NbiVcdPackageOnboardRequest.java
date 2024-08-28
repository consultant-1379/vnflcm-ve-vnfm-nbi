package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimConnectionInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("deprecation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbiVcdPackageOnboardRequest {

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    private String vAppTemplateName;

    private String vAppTemplateDescription;

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    private String catalogName;

    private String ovfPackage;

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    private String ovfDirectory;

    @Valid
    protected List<VimConnectionInfo> vimConnectionInfo = new ArrayList<>();

    /**
     * @return the vAppTemplateName
     */
    public String getvAppTemplateName() {
        return vAppTemplateName;
    }

    /**
     * @param vAppTemplateName the vAppTemplateName to set
     */
    public void setvAppTemplateName(final String vAppTemplateName) {
        this.vAppTemplateName = vAppTemplateName;
    }

    /**
     * @return the vAppTemplateDescription
     */
    public String getvAppTemplateDescription() {
        return vAppTemplateDescription;
    }

    /**
     * @param vAppTemplateDescription the vAppTemplateDescription to set
     */
    public void setvAppTemplateDescription(final String vAppTemplateDescription) {
        this.vAppTemplateDescription = vAppTemplateDescription;
    }

    /**
     * @return the catalogName
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * @param catalogName the catalogName to set
     */
    public void setCatalogName(final String catalogName) {
        this.catalogName = catalogName;
    }

    /**
     * @return the ovfPackage
     */
    public String getOvfPackage() {
        return ovfPackage;
    }

    /**
     * @param ovfPackage the ovfPackage to set
     */
    public void setOvfPackage(final String ovfPackage) {
        this.ovfPackage = ovfPackage;
    }

    /**
     * @return the ovfDirectory
     */
    public String getOvfDirectory() {
        return ovfDirectory;
    }

    /**
     * @param ovfDirectory the ovfDirectory to set
     */
    public void setOvfDirectory(final String ovfDirectory) {
        this.ovfDirectory = ovfDirectory;
    }

    /**
     * @return the vimConnectionInfo
     */
    public List<VimConnectionInfo> getVimConnectionInfo() {
        return vimConnectionInfo;
    }

    /**
     * @param vimConnectionInfo the vimConnectionInfo to set
     */
    public void setVimConnectionInfo(final List<VimConnectionInfo> vimConnectionInfo) {
        this.vimConnectionInfo = vimConnectionInfo;
    }
}
