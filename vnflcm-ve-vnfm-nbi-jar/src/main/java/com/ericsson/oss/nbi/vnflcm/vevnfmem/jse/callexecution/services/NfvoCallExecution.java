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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallExecutor;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.*;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;

/**
 * @author xrohdwi
 * 
 */
public abstract class NfvoCallExecution {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private String correlationId;
    public RestCallDataDto restCallData;
    private final RestCallExecutor restCallExecutor;

    public NfvoCallExecution(final RestCallExecutor restCallExecutor) {
        this.restCallExecutor = restCallExecutor;

    }

    /**
     * Fetch notification details.
     * 
     * @return
     * @throws NfvoCallException
     */
    public abstract CallExecutionData fetchExecutionDetails(
            final ProcessDataDto processNotificationData)
            throws NfvoCallException;

    /**
     * Create data required for call to NFVO
     * 
     * @param data
     * @return
     * @throws NfvoCallException
     */
    public abstract RestCallDataDto createRestRequestData(
            final CallExecutionData data) throws NfvoCallException;

    /**
     * Execute call to NFVO
     * 
     * @param restCallData
     * @return
     * @throws NfvoCallException
     */
    public abstract ResponseDTO executeCall(final RestCallDataDto restCallData)
            throws NfvoCallException;

    public final ResponseDTO processExecution(
            final ProcessDataDto processDataDto) throws VNFLCMServiceException {
        LOGGER.info("Process data info is {}  ", processDataDto == null ? null : Utils.getMaskedString(processDataDto.toString()));
        final String executionType = String.valueOf(processDataDto
                .getCallExecutionType());
        LOGGER.info("Starting " + processDataDto.getCallExecutionType()
                + " operation for correlation id "
                + processDataDto.getCorrelationid());
        ResponseDTO response = null;
        CallExecutionData callExecutionData = null;
        try {
            callExecutionData = this.fetchExecutionDetails(processDataDto);
            LOGGER.info(executionType + " is {}", callExecutionData == null ? null : Utils.getMaskedString(callExecutionData.toString()));
        } catch (final NfvoCallException e) {
            LOGGER.error("Error in fetching execution details", e, this);
            return setErrorResponse(response, e);
        }
        LOGGER.info("Operation details fetch successfully", this);

        try {
            if (processDataDto.getEmConfigData() != null && !processDataDto.getEmConfigData().isEmpty()) {
                callExecutionData.setEmConfigList(processDataDto.getEmConfigData());
            }
            this.restCallData = this.createRestRequestData(callExecutionData);
        } catch (final NfvoCallException e) {
            LOGGER.error("Error in create Rest Request Data", e, this);
            return setErrorResponse(response, e);
        }
        LOGGER.info("Rest Request Data created successfully");

        try {
            response = this.executeCall(this.restCallData);
            if (response != null) {
                if(null != processDataDto.getCorrelationid()) {
                    response.setCorrelationId(processDataDto.getCorrelationid());
                }
             }
        } catch (final NfvoCallException e) {
            LOGGER.error("Error in execute call ", e, this);
            return setErrorResponse(response, e);
        }
        return response;
    }

    private ResponseDTO setErrorResponse(final ResponseDTO response,
            final NfvoCallException e) {
        response.setStatus(false);
        response.setDetails(e.getMessage());
        response.setResponseCode(e.getErrCode());
        response.setCorrelationId(this.correlationId);
        return response;
    }

}
