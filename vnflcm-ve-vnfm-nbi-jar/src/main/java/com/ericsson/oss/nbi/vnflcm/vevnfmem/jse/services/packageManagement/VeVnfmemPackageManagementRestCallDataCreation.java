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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallDataCreator;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.EcmRestManipulation;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.PackageManagementNbiData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class VeVnfmemPackageManagementRestCallDataCreation implements
        RestCallDataCreator {

    private final EcmRestManipulation ecmRestManipulation;
    private VnflcmServiceData packageManagementServiceData = null;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public VeVnfmemPackageManagementRestCallDataCreation() {
        ecmRestManipulation = new EcmRestManipulation();

    }

    @Override
    public RestCallDataDto createRestCallData(
            final CallExecutionData packageManagementData)
            throws NfvoCallException {
        RestCallDataDto restData = null;
        final VnflcmServiceData packageManagementServiceData = (VnflcmServiceData) packageManagementData
                .getVnflcmServiceData();
        this.packageManagementServiceData = packageManagementServiceData;
        restData = getPackageManagementRestData(packageManagementData);
        return restData;

    }

    private RestCallDataDto getPackageManagementRestData(
            final CallExecutionData packageManagementData) {
        final RestCallDataDto packageManagementRestCallData = getBasePackageManagementRestCallData(packageManagementData);
        LOGGER.debug("packageManagementRestCallData is {} ",
                packageManagementRestCallData == null ? null : Utils.getMaskedString(packageManagementRestCallData.toString()));
        return packageManagementRestCallData;
    }

    private RestCallDataDto getBasePackageManagementRestCallData(
            final CallExecutionData packageManagementData) {
        LOGGER.info("Start OrVnfmPackageManagementRestCallDataCreation.getBasePackageManagementRestCallData... ");
        final RestCallDataDto restCallDataDto = new RestCallDataDto();
        final AuthenticationCallDataDto authCallData = this.ecmRestManipulation
                .createAuthenticationData(packageManagementData);
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode basePackageManagementNode = mapper.createObjectNode();
        basePackageManagementNode.put(PackageManagementConstants.VNFD_ID,
                this.packageManagementServiceData.getVnfdId());
        basePackageManagementNode.put(PackageManagementConstants.VNF_PKG_ID,
                this.packageManagementServiceData.getVnfPkgId());
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(CallExecutionConstants.CONTENT_TYPE,
                CallExecutionConstants.APPLICATION_JSON);
        final Map<String, String> ecmHeaders = this.ecmRestManipulation
                .populateEcmOperationHeaders(headers, packageManagementData);
        PackageManagementNbiData packageManagementNbiData = (PackageManagementNbiData) packageManagementData
                .getNbiNotificationData();

        restCallDataDto.setAuthCallData(authCallData);
        restCallDataDto.setHeaders(ecmHeaders);
        restCallDataDto.setUrl(packageManagementNbiData.getUrl());
        restCallDataDto.setSuccessCode(PackageManagementConstants.SUCCESS_CODE);
        restCallDataDto.setMethodType(CallExecutionConstants.GET_METHOD);
        restCallDataDto.setJsonObjectNode(basePackageManagementNode);
        restCallDataDto.setReqBody(basePackageManagementNode.toString());
        restCallDataDto.setNbiData(packageManagementData
                .getNbiNotificationData());
        LOGGER.info("End OrVnfmPackageManagementRestCallDataCreation.getBasePackageManagementRestCallData ");
        return restCallDataDto;
    }
}
