/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.EnumType;

public final class OperateVnfRequest {
    
    @EnumType(enumClass = VnfOperationalStateType.class, message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    private VnfOperationalStateType changeStateTo;
    private StopType stopType;
//    private String vnfcInstanceId;
    private KeyValuePairs additionalParams;
    
/*    *//**
     * @return the vnfcInstanceId
     *//*
    public String getVnfcInstanceId() {
        return vnfcInstanceId;
    }

    *//**
     * @param vnfcInstanceId the vnfcInstanceId to set
     *//*
    public void setVnfcInstanceId(final String vnfcInstanceId) {
        this.vnfcInstanceId = vnfcInstanceId;
    }
*/
    /**
     * @return the changeStateTo
     */
    public VnfOperationalStateType getChangeStateTo() {
        return changeStateTo;
    }

    /**
     * @param changeStateTo the changeStateTo to set
     */
    public void setChangeStateTo(final VnfOperationalStateType changeStateTo) {
        this.changeStateTo = changeStateTo;
    }

    /**
     * @return the stopType
     */
    public StopType getStopType() {
        return stopType;
    }

    /**
     * @param stopType the stopType to set
     */
    public void setStopType(final StopType stopType) {
        this.stopType = stopType;
    }

/*    *//**
     * @return the gracefulStopTimeout
     *//*
    public Integer getGracefulStopTimeout() {
        return gracefulStopTimeout;
    }

    *//**
     * @param gracefulStopTimeout the gracefulStopTimeout to set
     *//*
    public void setGracefulStopTimeout(final Integer gracefulStopTimeout) {
        this.gracefulStopTimeout = gracefulStopTimeout;
    }*/

    /**
     * @return the additionalParams
     */
    public KeyValuePairs getAdditionalParams() {
        return additionalParams;
    }

    /**
     * @param additionalParams the additionalParams to set
     */
    public void setAdditionalParams(final KeyValuePairs additionalParams) {
        this.additionalParams = additionalParams;
    }
}
