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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.CallExecutionDataRetriever;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallDataCreator;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallExecutor;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services.NfvoCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.PackageManagementConstants;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.CallExecutionData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.ProcessDataDto;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.ResponseDTO;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.RestCallDataDto;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.OnboardedVnfPkgInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.PackageManagementResponseDto;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.VnfPackageArtifactInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VeVnfmemPackageManagementCallExecution extends NfvoCallExecution {
    private final CallExecutionDataRetriever dataRetriever;
    private final RestCallDataCreator restCallDataCreator;
    private final RestCallExecutor restCallExecutor;
    private CallExecutionData callExecutionData;
    private RestCallDataDto restCallDataDto;

    private static final Logger LOGGER = LoggerFactory.getLogger(VeVnfmemPackageManagementCallExecution.class);

    public VeVnfmemPackageManagementCallExecution(final CallExecutionDataRetriever vevnfmemPackageManagementDataCreation, final RestCallDataCreator vevnfmemRestCallDataCreation, final RestCallExecutor restCallExecutor) {
        super(restCallExecutor);
        this.dataRetriever = vevnfmemPackageManagementDataCreation;
        this.restCallDataCreator = vevnfmemRestCallDataCreation;
        this.restCallExecutor = restCallExecutor;
    }

    @Override
    public CallExecutionData fetchExecutionDetails(final ProcessDataDto processPackageManagementData) throws NfvoCallException {
        if (processPackageManagementData == null || processPackageManagementData.getPackageManagementListenerData() == null) {
            LOGGER.error("Package Management data is null ", this);
            throw new NfvoCallException("Package Management data is null ");
        }
        try {
            if (null != this.dataRetriever) {
                callExecutionData = this.dataRetriever.getCallExecutionData(processPackageManagementData);
            }
            return callExecutionData;
        } catch (final NfvoCallException e) {
            LOGGER.error("error in fetchExecutionDetails", e);
            throw e;
        } catch (final Exception e) {
            LOGGER.error("Unknown error in fetchExecutionDetails", e);
            throw new NfvoCallException("Unknown Error in fetchExecutionDetails");
        }
    }

    @Override
    public RestCallDataDto createRestRequestData(final CallExecutionData callExecutionData) throws NfvoCallException {
        if (callExecutionData == null || callExecutionData.getVnflcmServiceData() == null || callExecutionData.getEcmData() == null) {
            LOGGER.error("Unable to proceed with createRestRequestData as required data is null", this);
        }
        try {
            if (null != this.restCallDataCreator) {
                restCallDataDto = this.restCallDataCreator.createRestCallData(callExecutionData);
            }
            return restCallDataDto;
        } catch (final NfvoCallException e) {
            LOGGER.error("error in createRestRequestData ", e);
            throw e;
        } catch (final Exception e) {
            LOGGER.error("Unknown error in createRestRequestData", e);
            throw new NfvoCallException("Unknown error in createRestRequestData");
        }
    }

    @Override
    public ResponseDTO executeCall(final RestCallDataDto restCallData) throws NfvoCallException {
        LOGGER.debug("data for execute call is {} ", restCallData == null ? null : Utils.getMaskedString(restCallData.toString()), this);
        String url = null;
        String baseUrl = null;
        final String vnfdId = this.callExecutionData.getVnflcmServiceData().getVnfdId();
        final String vnfPkgId = this.callExecutionData.getVnflcmServiceData().getVnfPkgId();
        baseUrl = restCallData.getUrl().toString();
        if (vnfPkgId != null && !vnfPkgId.isEmpty()) {
            url = baseUrl + "/" + vnfPkgId;
        }else {
            url = baseUrl; // 3PP NFVO standard query packages api call.
        }
        restCallData.setUrl(url);
        LOGGER.info("executeCall url {}", url);
        final PackageManagementResponseDto packageManagementResponseDto = new PackageManagementResponseDto();
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
                        LOGGER.debug("onboardedVnfPkgInfo is {}  ", vnfPkgInfo);
                        packageManagementResponseDto.setOnboardedVnfPkgInfo(vnfPkgInfo);
                    } else {
                        LOGGER.error("No Package availble in NFVO with requested vnfdId: {}", vnfdId);
                        throw new NfvoCallException("No Package availble in NFVO with requested vnfdId: " + vnfdId);
                    }
                    packageManagementResponseDto.setResponseCode(responseDTO.getResponseCode());
                    packageManagementResponseDto.setVnfdId(vnfdId);
                    packageManagementResponseDto.setDetails(responseDTO.getDetails());
                    packageManagementResponseDto.setStatus(responseDTO.getStatus());
                    packageManagementResponseDto.setVnfPkgInfo(vnfPkgInfo);
                    downloadArtifacts(packageManagementResponseDto, restCallData);
                    LOGGER.info("End vevnfmemPackageManagementCallExecution.executeCall ");
                } else {
                    LOGGER.error("Error in Authenticate from NFVO with Response code {} ",
                            packageManagementResponseDto.getResponseCode());
                    throw new NfvoCallException("Error in Authenticate from NFVO with Response code");
                }
            } else {
                LOGGER.info(
                        "vevnfmemPackageManagementCallExecution.executeCall download cannot be processed as responseDTO.getDetails() is null ");
            }
        } catch (JsonParseException | JsonMappingException e) {
            LOGGER.error("Error in reading json file", e);
            throw new NfvoCallException("Not a valid JSON file");
        } catch (final IOException e) {
            throw new NfvoCallException("There is no vnf package exists for provided vnfdid:" + vnfdId);
        }
        return packageManagementResponseDto;
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
    /*
     * As per discussion , files size is small and no checksom validation
     * required Note:-if all iteration(all artifacts downloaded) ok, then it
     * will be considered as Ok otherwise NOK Note:- if response code is 200
     * then execute this, we need to entertain 200 ok code only in case of
     * success scenario ,partial download is not in scope
     */
    private void downloadArtifacts(final PackageManagementResponseDto packageManagementResponse, final RestCallDataDto restCallData) throws NfvoCallException {
        try {
            final PackageManagementResponseDto packageManagementResponseDto = new PackageManagementResponseDto();
            LOGGER.debug("Start downloadArtifacts packageManagementResponse {} ", packageManagementResponse == null ? null : packageManagementResponse.toString());
            String url = null;
            String baseUrl = null;
            final String vnfdId = this.callExecutionData.getVnflcmServiceData().getVnfdId();
            boolean isArtifactHaveIssue = false;
            if (null != restCallData.getUrl()) {
                baseUrl = restCallData.getUrl().toString();
                List<String> softwareImagePaths = null;
                if (packageManagementResponse.getOnboardedVnfPkgInfo().getSoftwareImages() != null && !packageManagementResponse.getOnboardedVnfPkgInfo().getSoftwareImages().isEmpty()) {
                    softwareImagePaths = packageManagementResponse.getOnboardedVnfPkgInfo().getSoftwareImages().stream().map(swIm -> swIm.getImagePath()).collect(Collectors.toList());
                }
                final List<VnfPackageArtifactInfo> additionalArtifacts = packageManagementResponse.getOnboardedVnfPkgInfo().getAdditionalArtifacts();
                LOGGER.info("vevnfmemPackageManagementCallExecution.downloadArtifacts additionalArtifacts  {} ", additionalArtifacts);
                if (null != additionalArtifacts && additionalArtifacts.size() > 0) {
                    for (VnfPackageArtifactInfo artifacts : additionalArtifacts) {
                        final String artifactPath = artifacts.getArtifactPath();
                        // Do not download the images from NFVO in vnflcm.
                        if (softwareImagePaths != null && softwareImagePaths.contains(artifactPath)) {
                            LOGGER.info("VeVnfmemPackageManagementCallExecution.downloadArtifacts do not download the image."+ artifactPath);
                            continue;
                        }
                        LOGGER.info("vevnfmemPackageManagementCallExecution.downloadArtifacts artifactPath {} ", artifactPath);
                        if (null != packageManagementResponse && PackageManagementConstants.SUCCESS_CODE == packageManagementResponse.getResponseCode() && null != packageManagementResponse.getOnboardedVnfPkgInfo().getId()) {
                            url = baseUrl + "/" + packageManagementResponse.getOnboardedVnfPkgInfo().getId() + PackageManagementConstants.ARTIFACTS + artifactPath;
                            String pathToBeCreated = PackageManagementConstants.BASE_PATH_TO_BE_CREATED + vnfdId + "/";
                            pathToBeCreated = pathToBeCreated + artifactPath;
                            LOGGER.info("vevnfmemPackageManagementCallExecution.downloadArtifacts pathToBeCreated {}", pathToBeCreated);
                            restCallData.setUrl(url);
                            //directory structure is created on vm using the pathToBeCreated where downloaded artifacts will be placed.
                            final File file = new File(pathToBeCreated);
                            final Path dest = Paths.get(pathToBeCreated);
                            while(!file.exists()) {
                                file.getParentFile().mkdirs();
                                file.createNewFile();
                                LOGGER.info("vevnfmemPackageManagementCallExecution.downloadArtifacts dest {}", dest);
                                final Map<String, String> headerIncludingAttachement;
                                headerIncludingAttachement = addTokenHeader(restCallData.getHeaders(), "Content-disposition", "attachment;filename=\"" + file.getName());
                                restCallData.setHeaders(headerIncludingAttachement);
                                ResponseDTO responseDTO;
                                responseDTO = executeRestCall(restCallData);
                                LOGGER.info("vevnfmemPackageManagementCallExecution.downloadArtifacts before StringBufferInputStream responseDTO.getDetails() {} ", responseDTO.getDetails());
                                if (responseDTO.getResponseCode() != PackageManagementConstants.SUCCESS_CODE) {
                                    LOGGER.error("Received response other than 200 OK in artifact {}, hence stopping the execution.", pathToBeCreated);
                                    packageManagementResponse.setResponseCode(PackageManagementConstants.DOWNLOAD_FAILED_CODE);
                                    isArtifactHaveIssue = true;
                                    break;
                                } else {
                                    packageManagementResponseDto.setResponseCode(responseDTO.getResponseCode());
                                    packageManagementResponseDto.setVnfdId(vnfdId);
                                    packageManagementResponseDto.setStatus(responseDTO.getStatus());
                                    try (final InputStream fis = new ByteArrayInputStream(responseDTO.getDetails().getBytes("utf-8"))){
                                        LOGGER.info("Copying file to destination.... ");
                                        Files.copy(fis, dest, StandardCopyOption.REPLACE_EXISTING);
                                        LOGGER.info("vevnfmemPackageManagementCallExecution.downloadArtifacts fis {}", fis);
                                    }
                                }
                            }
                        } else if (packageManagementResponse.getResponseCode() == PackageManagementConstants.DOWNLOAD_FAILED_CODE && isArtifactHaveIssue == true) {
                            break;
                        } else {
                            packageManagementResponseDto.setResponseCode(packageManagementResponse.getResponseCode());
                            packageManagementResponseDto.setStatus(packageManagementResponse.getStatus());
                            LOGGER.error("Response code returned is not 200, instead it return code as {}", packageManagementResponse.getResponseCode());
                            throw new NfvoCallException("Response code returned is not 200");
                        }
                    }
                } else {
                    LOGGER.error("NO additionalArtifacts are provided to download {}", additionalArtifacts);
                    throw new NfvoCallException("NO additionalArtifacts are provided to download");
                }
            } else {
                LOGGER.error("Base URL for downloadArtifacts cannot be null");
                throw new NfvoCallException("Base URL for downloadArtifacts cannot be null");
            }
        } catch (final NfvoCallException e) {
            LOGGER.error("error in downloadArtifacts ", e);
            throw e;
        } catch (final IOException e) {
            LOGGER.info("Artifacts cannot be downloded {} ", this);
            e.printStackTrace();
        } catch (final Exception e) {
            LOGGER.error("Unknown error in downloadArtifacts from NFVO", e);
            throw new NfvoCallException("Unknown error in downloadArtifacts");
        }
    }

    private ResponseDTO executeRestCall(RestCallDataDto restCallData)throws NfvoCallException {
        LOGGER.info("Start vevnfmemPackageManagementCallExecution.executeRestCall ");
        ResponseDTO responseDTO;
        responseDTO = (ResponseDTO) this.restCallExecutor.executeCall(restCallData);
        LOGGER.info("End vevnfmemPackageManagementCallExecution.executeRestCall ");
        return responseDTO;
    }

    private Map<String, String> addTokenHeader(final Map<String, String> headers, final String Key, final String token) {
        headers.put(Key, token);
        return headers;
    }
}
