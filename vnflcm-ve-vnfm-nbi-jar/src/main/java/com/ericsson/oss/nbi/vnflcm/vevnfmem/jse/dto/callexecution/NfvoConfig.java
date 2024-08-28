/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution;

import java.io.Serializable;

/**
 * Bean relative to nfvoconfig.json.
 * 
 * @author xrohdwi
 * 
 */
public class NfvoConfig implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8443835206594709981L;
    private String nfvoType;
    private String username;
    private String password;
    private String authType;
    private String subscriptionId;
    private String tenantid;
    private String tenantName;
    private String vdcId;
    private String nfvoAuthUrl;
    private String grantUrl;
    private String enmHostName;
    private String isNfvoAvailable;
    private String isGrantSupported;
    private Boolean isNotificationSupported;
    private String lifecycleNotificationUrl;
    private String createNotificationUrl;
    private String deleteNotificationUrl;
    private String staticAuthenticationTokenName;
    private String staticAuthenticationTokenValue;
    private String nfvoSupportedNotificationTypes;
    private String packageManagementUrl;
    private String orVnfmVersion;
    private String notificationAckRequired;
    private String queryNfvoVdcListUrl;
    private Boolean isFallbackBestEffortSupported;
    private Boolean basicAuthImplSameAsEO;
    private String idempotencyHeaderName;

    /**
     * @return the isFallbackBestEffortSupported
     */
    public Boolean getIsFallbackBestEffortSupported() {
        return isFallbackBestEffortSupported;
    }

    /**
     * @param isFallbackBestEffortSupported the isFallbackBestEffortSupported to set
     */
    public void setIsFallbackBestEffortSupported(final Boolean isFallbackBestEffortSupported) {
        this.isFallbackBestEffortSupported = isFallbackBestEffortSupported;
    }

    /**
     * @return the nfvoType
     */
    public String getNfvoType() {
        return nfvoType;
    }

    /**
     * @param nfvoType the nfvoType to set
     */
    public void setNfvoType(final String nfvoType) {
        this.nfvoType = nfvoType;
    }

    /**
     * @return the authType
     */
    public String getAuthType() {
        return authType;
    }

    /**
     * @param authType the authType to set
     */
    public void setAuthType(final String authType) {
        this.authType = authType;
    }

    /**
     * @return the isNotificationSupported
     */
    public Boolean getIsNotificationSupported() {
        return isNotificationSupported;
    }

    /**
     * @param isNotificationSupported the isNotificationSupported to set
     */
    public void setIsNotificationSupported(final Boolean isNotificationSupported) {
        this.isNotificationSupported = isNotificationSupported;
    }

    public String getNfvoSupportedNotificationTypes() {
        return nfvoSupportedNotificationTypes;
    }

    public void setNfvoSupportedNotificationTypes(final String nfvoSupportedNotificationTypes) {
        this.nfvoSupportedNotificationTypes = nfvoSupportedNotificationTypes;
    }

    public String getGrantUrl() {
        return grantUrl;
    }

    public void setGrantUrl(final String grantUrl) {
        this.grantUrl = grantUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTenantid() {
        return tenantid;
    }

    public void setTenantid(final String tenantid) {
        this.tenantid = tenantid;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(final String tenantName) {
        this.tenantName = tenantName;
    }

    public String getVdcId() {
        return vdcId;
    }

    public void setVdcId(final String vdcId) {
        this.vdcId = vdcId;
    }

    public String getNfvoAuthUrl() {
        return nfvoAuthUrl;
    }

    public void setNfvoAuthUrl(final String nfvoAuthUrl) {
        this.nfvoAuthUrl = nfvoAuthUrl;
    }

    public String getEnmHostName() {
        return enmHostName;
    }

    public void setEnmHostName(final String enmHostName) {
        this.enmHostName = enmHostName;
    }

    public String getIsNfvoAvailable() {
        return isNfvoAvailable;
    }

    public void setIsNfvoAvailable(final String isNfvoAvailable) {
        this.isNfvoAvailable = isNfvoAvailable;
    }

    public String getIsGrantSupported() {
        return isGrantSupported;
    }

    public void setIsGrantSupported(final String isGrantSupported) {
        this.isGrantSupported = isGrantSupported;
    }

    public String getLifecycleNotificationUrl() {
        return lifecycleNotificationUrl;
    }

    public void setLifecycleNotificationUrl(
            final String lifecycleNotificationUrl) {
        this.lifecycleNotificationUrl = lifecycleNotificationUrl;
    }

    public String getDeleteNotificationUrl() {
        return deleteNotificationUrl;
    }

    public void setDeleteNotificationUrl(final String deleteNotificationUrl) {
        this.deleteNotificationUrl = deleteNotificationUrl;
    }

    public String getCreateNotificationUrl() {
        return createNotificationUrl;
    }

    public void setCreateNotificationUrl(final String createNotificationUrl) {
        this.createNotificationUrl = createNotificationUrl;
    }

    /**
     * @return the staticAuthenticationTokenName
     */
    public String getStaticAuthenticationTokenName() {
        return staticAuthenticationTokenName;
    }

    /**
     * @param staticAuthenticationTokenName
     *            the staticAuthenticationTokenName to set
     */
    public void setStaticAuthenticationTokenName(
            final String staticAuthenticationTokenName) {
        this.staticAuthenticationTokenName = staticAuthenticationTokenName;
    }

    /**
     * @return the staticAuthenticationTokenValue
     */
    public String getStaticAuthenticationTokenValue() {
        return staticAuthenticationTokenValue;
    }

    /**
     * @param staticAuthenticationTokenValue
     *            the staticAuthenticationTokenValue to set
     */
    public void setStaticAuthenticationTokenValue(
            final String staticAuthenticationTokenValue) {
        this.staticAuthenticationTokenValue = staticAuthenticationTokenValue;
    }
    /**
     * @return the packageManagementUrl
     */
    public String getPackageManagementUrl() {
        return packageManagementUrl;
    }

    /**
     * @param packageManagementUrl
     *            the packageManagementUrl to set
     */
    public void setPackageManagementUrl(final String packageManagementUrl) {
        this.packageManagementUrl = packageManagementUrl;
    }

    /**
     * @return the orVnfmVersion
     */
    public String getOrVnfmVersion() {
        return orVnfmVersion;
    }

    /**
     * @param orVnfmVersion the orVnfmVersion to set
     */
    public void setOrVnfmVersion(final String orVnfmVersion) {
        this.orVnfmVersion = orVnfmVersion;
    }

    /**
     * @return the notificationAckRequired
     */
    public String getNotificationAckRequired() {
        return notificationAckRequired;
    }

    /**
     * @param notificationAckRequired the notificationAckRequired to set
     */
    public void setNotificationAckRequired(final String notificationAckRequired) {
        this.notificationAckRequired = notificationAckRequired;
    }

    /**
     * @param queryNfvoVdcListUrl
     *           the queryNfvoVdcListUrl to set
     */
    public void setQueryNfvoVdcListUrl(final String queryNfvoVdcListUrl) {
        this.queryNfvoVdcListUrl = queryNfvoVdcListUrl;
    }

    /**
     * @return the queryNfvoVdcListUrl
     */
    public String getQueryNfvoVdcListUrl() {
        return queryNfvoVdcListUrl;
    }

    /**
     * @return the basicAuthImplSameAsEO
     */
    public Boolean getBasicAuthImplSameAsEO() {
        return basicAuthImplSameAsEO;
    }

    /**
     * @param basicAuthImplSameAsEO the basicAuthImplSameAsEO to set
     */
    public void setBasicAuthImplSameAsEO(final Boolean basicAuthImplSameAsEO) {
        this.basicAuthImplSameAsEO = basicAuthImplSameAsEO;
    }

    public String getIdempotencyHeaderName() {
        return idempotencyHeaderName;
    }

    public void setIdempotencyHeaderName(String idempotencyHeaderName) {
        this.idempotencyHeaderName = idempotencyHeaderName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NfvoConfig [nfvoType=" + nfvoType + ", username=" + username
                + ", password=" + password + ", authType=" + authType
                + ", subscriptionId=" + subscriptionId + ", tenantid="
                + tenantid + ", tenantName=" + tenantName + ", vdcId="
                + vdcId + ", nfvoAuthUrl=" + nfvoAuthUrl + ", grantUrl="
                + grantUrl + ", enmHostName=" + enmHostName
                + ", isNfvoAvailable=" + isNfvoAvailable
                + ", isGrantSupported=" + isGrantSupported
                + ", isNotificationSupported=" + isNotificationSupported
                + ", lifecycleNotificationUrl=" + lifecycleNotificationUrl
                + ", createNotificationUrl=" + createNotificationUrl
                + ", deleteNotificationUrl=" + deleteNotificationUrl
                + ", staticAuthenticationTokenName="
                + staticAuthenticationTokenName
                + ", staticAuthenticationTokenValue="
                + staticAuthenticationTokenValue
                + ", nfvoSupportedNotificationTypes="
                + nfvoSupportedNotificationTypes + ", packageManagementUrl="
                + packageManagementUrl + ", orVnfmVersion=" + orVnfmVersion
                + ", notificationAckRequired=" + notificationAckRequired
                + ", queryNfvoVdcListUrl=" + queryNfvoVdcListUrl
                + ", isFallbackBestEffortSupported="
                + isFallbackBestEffortSupported + 
                ", idempotencyHeaderName="+ idempotencyHeaderName+ "]";
    }

}
