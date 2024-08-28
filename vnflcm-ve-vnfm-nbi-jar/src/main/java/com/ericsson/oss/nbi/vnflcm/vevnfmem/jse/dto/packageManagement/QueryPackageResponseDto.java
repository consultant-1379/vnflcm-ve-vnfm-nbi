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

import java.io.Serializable;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.ResponseDTO;


public class QueryPackageResponseDto extends ResponseDTO implements Serializable{

    private static final long serialVersionUID = 3466845585853496858L;
    private String vnfdId;
    private int responseCode;
    private OnboardedVnfPkgInfo onboardedVnfPkgInfo;
    /**
     * @return the onboardedVnfPkgInfo
     */
    public OnboardedVnfPkgInfo getOnboardedVnfPkgInfo() {
        return onboardedVnfPkgInfo;
    }

    /**
     * @param onboardedVnfPkgInfo the onboardedVnfPkgInfo to set
     */
    public void setOnboardedVnfPkgInfo(final OnboardedVnfPkgInfo onboardedVnfPkgInfo) {
        this.onboardedVnfPkgInfo = onboardedVnfPkgInfo;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the vnfdId
     */
    public String getVnfdId() {
        return vnfdId;
    }

    /**
     * @param vnfdId the vnfdId to set
     */
    public void setVnfdId(final String vnfdId) {
        this.vnfdId = vnfdId;
    }

    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @param i the responseCode to set
     */
    public void setResponseCode(final int resp) {
        this.responseCode = resp;
    }

    @Override
    public String toString() {
        return "QueryPackageResponseDto [vnfdId=" + vnfdId+ "responseCode=" + responseCode+ "onboardedVnfPkgInfo=" + onboardedVnfPkgInfo + "]";
        }

}
