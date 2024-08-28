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

public class VeVnfmemQueryPackageRestCallDataCreation implements
        RestCallDataCreator {

    private final EcmRestManipulation ecmRestManipulation;
    private VnflcmServiceData queryPackageServiceData = null;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public VeVnfmemQueryPackageRestCallDataCreation() {
        ecmRestManipulation = new EcmRestManipulation();

    }

    @Override
    public RestCallDataDto createRestCallData(
            final CallExecutionData queryPackageData)
            throws NfvoCallException {
        RestCallDataDto restData = null;
        final VnflcmServiceData queryPackageServiceData = (VnflcmServiceData) queryPackageData
                .getVnflcmServiceData();
        this.queryPackageServiceData = queryPackageServiceData;
        restData = getQueryPackageRestData(queryPackageData);
        return restData;

    }

    private RestCallDataDto getQueryPackageRestData(
            final CallExecutionData queryPackageData) {
        final RestCallDataDto queryPackageRestCallData = getBaseQueryPackageRestCallData(queryPackageData);
        LOGGER.debug("queryPackageRestCallData is {} ",
                queryPackageRestCallData == null ? null : Utils.getMaskedString(queryPackageRestCallData.toString()));
        return queryPackageRestCallData;
    }

    private RestCallDataDto getBaseQueryPackageRestCallData(
            final CallExecutionData queryPackageData) {
        LOGGER.info("Start OrVnfmQueryPackageRestCallDataCreation.getBaseQueryPackageRestCallData... ");
        final RestCallDataDto restCallDataDto = new RestCallDataDto();
        final AuthenticationCallDataDto authCallData = this.ecmRestManipulation
                .createAuthenticationData(queryPackageData);
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode baseQueryPackageNode = mapper.createObjectNode();
        if (this.queryPackageServiceData.getVnfdId() != null) {
            baseQueryPackageNode.put(PackageManagementConstants.VNFD_ID, this.queryPackageServiceData.getVnfdId());
        }
        if (this.queryPackageServiceData.getVnfPkgId() != null) {
            baseQueryPackageNode.put(PackageManagementConstants.VNF_PKG_ID, this.queryPackageServiceData.getVnfPkgId());
        }
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(CallExecutionConstants.CONTENT_TYPE,
                CallExecutionConstants.APPLICATION_JSON);
        final Map<String, String> ecmHeaders = this.ecmRestManipulation
                .populateEcmOperationHeaders(headers, queryPackageData);
        PackageManagementNbiData packageManagementNbiData = (PackageManagementNbiData) queryPackageData
                .getNbiNotificationData();

        restCallDataDto.setAuthCallData(authCallData);
        restCallDataDto.setHeaders(ecmHeaders);
        restCallDataDto.setUrl(packageManagementNbiData.getUrl());
        restCallDataDto.setSuccessCode(PackageManagementConstants.SUCCESS_CODE);
        restCallDataDto.setMethodType(CallExecutionConstants.GET_METHOD);
        restCallDataDto.setJsonObjectNode(baseQueryPackageNode);
        restCallDataDto.setReqBody(baseQueryPackageNode.toString());
        restCallDataDto.setNbiData(queryPackageData
                .getNbiNotificationData());
        LOGGER.info("End OrVnfmQueryPackageRestCallDataCreation.getBaseQueryPackageRestCallData ");
        return restCallDataDto;
    }
}
