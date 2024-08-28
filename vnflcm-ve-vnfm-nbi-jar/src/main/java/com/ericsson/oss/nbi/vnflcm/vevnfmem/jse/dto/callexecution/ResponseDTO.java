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

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.OnboardedVnfPkgInfo;

public class ResponseDTO implements Serializable {

    private static final long serialVersionUID = 3466745585853496858L;
    private boolean status;
    private String details;
    private String correlationId;
    private int responseCode;
    private int errorCode;
    private OnboardedVnfPkgInfo vnfPkgInfo;
    /**
     * @return
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(final boolean status) {
        this.status = status;
    }

    /**
     * @return
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details
     */
    public void setDetails(final String details) {
        this.details = details;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * @return the errorCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @param i the errorCode to set
     */
    public void setResponseCode(final int resCode) {
        this.responseCode = resCode;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public OnboardedVnfPkgInfo getVnfPkgInfo() {
        return vnfPkgInfo;
    }

    public void setVnfPkgInfo(OnboardedVnfPkgInfo vnfPkgInfo) {
        this.vnfPkgInfo = vnfPkgInfo;
    }

    @Override
    public String toString() {
        return "ResponseDTO [status=" + status + ", details=" + details + ", correlationId=" + correlationId
                + ", responseCode=" + responseCode + ", errorCode=" + errorCode + ", vnfPkgInfo=" + vnfPkgInfo + "]";
    }
}
