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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services.NfvoCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.OnboardedVnfPkgInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.QueryPackageResponseDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VeVnfmemQueryPackageCallExecution extends NfvoCallExecution {
    private final CallExecutionDataRetriever dataRetriever;
    private final RestCallDataCreator restCallDataCreator;
    private final RestCallExecutor restCallExecutor;
    private CallExecutionData callExecutionData;
    private RestCallDataDto restCallDataDto;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VeVnfmemQueryPackageCallExecution.class);
  
    public VeVnfmemQueryPackageCallExecution(
            final CallExecutionDataRetriever orvnfmQueryPackageDataCreation,
            final RestCallDataCreator orvnfmRestCallDataCreation,
            final RestCallExecutor restCallExecutor) {
        super(restCallExecutor);
        this.dataRetriever = orvnfmQueryPackageDataCreation;
        this.restCallDataCreator = orvnfmRestCallDataCreation;
        this.restCallExecutor = restCallExecutor;
    }

    @Override
    public CallExecutionData fetchExecutionDetails(
            final ProcessDataDto processQueryPackageData)
            throws NfvoCallException {
        if (processQueryPackageData == null
                || processQueryPackageData
                        .getPackageManagementListenerData() == null) {
            LOGGER.error("Query Package data is null ", this);
            throw new NfvoCallException("Query Package data is null ");
        }
        try {
            if (null != this.dataRetriever) {
                callExecutionData = this.dataRetriever
                        .getCallExecutionData(processQueryPackageData);
            }
            return callExecutionData;
        } catch (final NfvoCallException e) {
            LOGGER.error("error in fetchExecutionDetails", e);
            throw e;
        } catch (final Exception e) {
            LOGGER.error("Unknown error in fetchExecutionDetails", e);
            throw new NfvoCallException(
                    "Unknown Error in fetchExecutionDetails");
        }
    }

    @Override
    public RestCallDataDto createRestRequestData(
            final CallExecutionData callExecutionData) throws NfvoCallException {
        if (callExecutionData == null
                || callExecutionData.getVnflcmServiceData() == null
                || callExecutionData.getEcmData() == null) {
            LOGGER.error(
                    "Unable to proceed with createRestRequestData as required data is null",
                    this);
        }
        try {
            if (null != this.restCallDataCreator) {
                restCallDataDto = this.restCallDataCreator
                        .createRestCallData(callExecutionData);
            }
            return restCallDataDto;
        } catch (final NfvoCallException e) {
            LOGGER.error("error in createRestRequestData ", e);
            throw e;
        } catch (final Exception e) {
            LOGGER.error("Unknown error in createRestRequestData", e);
            throw new NfvoCallException(
                    "Unknown error in createRestRequestData");
        }
    }

    @Override
    public ResponseDTO executeCall(final RestCallDataDto restCallData)
            throws NfvoCallException {
        LOGGER.debug("data for execute call is {} ",
                restCallData == null ? null : Utils.getMaskedString(restCallData.toString()), this);
        String url = null;
        String baseUrl = null;
        final String vnfdId = this.callExecutionData.getVnflcmServiceData().getVnfdId();
        final String vnfPkgId = this.callExecutionData.getVnflcmServiceData().getVnfPkgId();
        baseUrl = restCallData.getUrl().toString();
        if (vnfPkgId != null && !vnfPkgId.isEmpty()) {
            url = baseUrl + "/" + vnfPkgId;
        } else {
            url = baseUrl; // 3PP NFVO standard query packages api call.
        }
        restCallData.setUrl(url);
        LOGGER.info("executeCall url {}", url);
        final QueryPackageResponseDto queryPackageResponseDto = new QueryPackageResponseDto();
        ResponseDTO responseDTO;
        responseDTO = this.restCallExecutor.executeCall(restCallData);
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (null != responseDTO.getDetails()) {
                OnboardedVnfPkgInfo vnfPkgInfo = null;
                final JsonNode pkgDetilsNode = objectMapper.readTree(responseDTO.getDetails());
                if (pkgDetilsNode.isArray()) {
                    final OnboardedVnfPkgInfo[] onboardedVnfPkgInfo = objectMapper.readValue(responseDTO.getDetails(),
                            OnboardedVnfPkgInfo[].class);
                    vnfPkgInfo = matchWithVnfdId(onboardedVnfPkgInfo, vnfdId);
                } else {
                    vnfPkgInfo = objectMapper.readValue(responseDTO.getDetails(), OnboardedVnfPkgInfo.class);
                }
                if (null != responseDTO && PackageManagementConstants.SUCCESS_CODE == responseDTO.getResponseCode()) {
                    LOGGER.info("Response  code received {} ", responseDTO.getResponseCode());
                    restCallData.setUrl(baseUrl);
                    if (vnfPkgInfo != null && "" != vnfPkgInfo.getId() && "" != vnfPkgInfo.getVnfdId()) {
                        LOGGER.debug("onboardedVnfPkgInfo {} and vnfdId {} ", vnfPkgInfo.getId(),
                                vnfPkgInfo.getVnfdId());
                        queryPackageResponseDto.setOnboardedVnfPkgInfo(vnfPkgInfo);
                    }
                    queryPackageResponseDto.setResponseCode(responseDTO.getResponseCode());
                    queryPackageResponseDto.setVnfdId(vnfdId);
                    queryPackageResponseDto.setDetails(responseDTO.getDetails());
                    queryPackageResponseDto.setVnfPkgInfo(vnfPkgInfo);
                    LOGGER.info("End OrVnfmPackageManagementCallExecution.executeCall ");
                } else {
                    LOGGER.error("Error in Authenticate from NFVO with Response code {} ",
                            queryPackageResponseDto.getResponseCode());
                    throw new NfvoCallException("Error in Authenticate from NFVO with Response code");
                }
            } else {
                LOGGER.info(
                        "OrVnfmPackageManagementCallExecution.executeCall cannot be processed as responseDTO.getDetails() is null ");
            }
        } catch (JsonParseException | JsonMappingException e) {
            LOGGER.error("Error in reading json file", e);
            throw new NfvoCallException("Not a valid JSON file");
        } catch (final IOException e) {
            throw new NfvoCallException("There is no vnf package exists for provided vnfdid:" + vnfdId);
        }
        return (ResponseDTO) queryPackageResponseDto;
    }

 // This method will match the vnfdId which comes in the request with all packages from NFVO.
    private OnboardedVnfPkgInfo matchWithVnfdId(OnboardedVnfPkgInfo[] onboardedVnfPkgInfos, String vnfdId) {
        if (onboardedVnfPkgInfos != null && onboardedVnfPkgInfos.length > 0) {
            for (OnboardedVnfPkgInfo vnfPkgInfo : onboardedVnfPkgInfos) {
                LOGGER.debug("Package Data of onboarded Vnf Package {}", vnfPkgInfo.toString());
                if(vnfPkgInfo.getVnfdId() != null && !vnfPkgInfo.getVnfdId().isEmpty()) {
                    if (vnfPkgInfo.getVnfdId().equalsIgnoreCase(vnfdId)) {
                        return vnfPkgInfo;
                    }
                }
            }
        }
        return null;
    }
  }
