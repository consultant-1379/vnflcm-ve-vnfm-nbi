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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.services.packageManagement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services.RestCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.ResponseDTO;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.RestCallDataDto;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.QueryPackageResponseDto;
import com.ericsson.oss.services.vnflcm.common.models.RestResponse;

public class VeVnfmemQueryPackageRestCallExecution extends RestCallExecution {

    private static final Logger LOGGER = LoggerFactory.getLogger(VeVnfmemQueryPackageRestCallExecution.class);
    public VeVnfmemQueryPackageRestCallExecution(final CallExecutionType callExecutionType) {
        super(callExecutionType);
    }

    @Override
        public ResponseDTO setOperationResponse(RestResponse restResponse,
            RestCallDataDto restCallData) {
        final QueryPackageResponseDto response = new QueryPackageResponseDto();
        LOGGER.info("Start OrVnfmQueryPackageRestCallExecution setOperationResponse {}", restResponse.getResponseBody());
        if(restResponse.getResponseBody()!=null){
            response.setDetails(restResponse.getResponseBody());
        }else {
            response.setDetails("");
        }
        if (restResponse.getResponseCode() == restCallData.getSuccessCode()) {
            LOGGER.debug("OrVnfmQueryPackageRestCallExecution restResponse.getResponseCode {}", restResponse.getResponseCode());
            response.setStatus(true);
            response.setResponseCode(restResponse.getResponseCode());
        }else{
            LOGGER.debug("OrVnfmQueryPackageRestCallExecution restResponse.getResponseCode {}", restResponse.getResponseCode());
            response.setStatus(false);
            response.setErrorCode(restResponse.getResponseCode());
        }
        LOGGER.info("End OrVnfmQueryPackageRestCallExecution setOperationResponse ");
        return response;
    }

    @Override
    public ResponseDTO setOperationExceptionResponse(Exception e) {
        QueryPackageResponseDto response = new QueryPackageResponseDto();
        LOGGER.info("Start OrVnfmQueryPackageRestCallExecution setOperationExceptionResponse {}", e.getMessage());
        response.setStatus(false);
        if(e!=null){
            response.setDetails(e.getMessage());
        }else{
            response.setDetails("Unknown Exception Occured");
            LOGGER.info("End OrVnfmQueryPackageRestCallExecution setOperationExceptionResponse {}"+ e.getMessage());
        }
        LOGGER.info("End OrVnfmQueryPackageRestCallExecution setOperationExceptionResponse {}", e.getMessage());

        return response;
    }

}
