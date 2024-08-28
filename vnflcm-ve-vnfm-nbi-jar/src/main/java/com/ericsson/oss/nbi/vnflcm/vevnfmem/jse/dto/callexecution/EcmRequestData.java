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

public class EcmRequestData implements Serializable {

    private static final long serialVersionUID = -5659203522087135375L;
    private String tenantId;
    private String tenantName;
    private String vdcId;
    private String ecmAuthUrl;
    private String vimId;
    private String ecmAuthType;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getVdcId() {
        return vdcId;
    }

    public void setVdcId(final String vdcId) {
        this.vdcId = vdcId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(final String tenantName) {
        this.tenantName = tenantName;
    }

    public String getEcmAuthUrl() {
        return ecmAuthUrl;
    }

    public String getEcmAuthType() {
        return ecmAuthType;
    }

    public void setEcmAuthUrl(final String ecmAuthUrl) {
        this.ecmAuthUrl = ecmAuthUrl;
    }

    public void setEcmAuthType(final String ecmAuthType) {
        this.ecmAuthType = ecmAuthType;
    }

    public String getVimId() {
        return vimId;
    }

    public void setVimId(final String vimId) {
        this.vimId = vimId;
    }

    @Override
    public String toString() {
        return "EcmRequestData [tenantId=" + tenantId + ", tenantName=" + tenantName + ", vdcId=" + vdcId + ", ecmAuthUrl=" + ecmAuthUrl + ", vimId=" + vimId + "]";
    }

}
