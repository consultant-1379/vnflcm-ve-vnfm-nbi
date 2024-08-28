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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionConstants;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.AuthenticationCallDataDto;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.CallExecutionData;
import com.ericsson.oss.services.vnflcm.common.util.GenerateUUID;

/**
 * @author xrohdwi
 *
 */
public class EcmRestManipulation {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public AuthenticationCallDataDto createAuthenticationData(final CallExecutionData callExecutionData) {
        final AuthenticationCallDataDto authData = new AuthenticationCallDataDto();
        authData.setAuthUrl(callExecutionData.getEcmData().getEcmAuthUrl());
        authData.setAuthType(callExecutionData.getEcmData().getEcmAuthType());
        authData.setHeaders(this.populateEcmAuthenticationHeader(callExecutionData));
        authData.setAuthReqBody("");
        LOGGER.info("Auth data in createAuthentication Data is {}", authData == null ? null : Utils.getMaskedString(authData.toString()));

        return authData;
    }

    public Map<String, String> populateEcmAuthenticationHeader(final CallExecutionData notificationData) {
        final Map<String, String> headers = new HashMap<String, String>();
        final String authString = notificationData.getNbiNotificationData().getUsername() + ":"
                + notificationData.getNbiNotificationData().getPassword();
        final byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        final String authStringEnc = new String(authEncBytes);
        final String ecmAuthString = "Basic " + authStringEnc;
        headers.put(CallExecutionConstants.AUTHORIZATION, ecmAuthString);
        headers.put(CallExecutionConstants.TENANT_ID, notificationData.getEcmData().getTenantId());
        headers.put(CallExecutionConstants.CONTENT_TYPE, CallExecutionConstants.APPLICATION_JSON);
        return headers;
    }

    public Map<String, String> populateEcmOperationHeaders(final Map<String, String> headers, final CallExecutionData executionData) {
        headers.put(CallExecutionConstants.TENANT_ID, executionData.getEcmData().getTenantId());
        try {
            if(executionData.getEmConfigList().get(0).getIdempotencyHeaderName() !=null && 
                    !executionData.getEmConfigList().get(0).getIdempotencyHeaderName().isEmpty()) {
                headers.put(executionData.getEmConfigList().get(0).getIdempotencyHeaderName(), GenerateUUID.createStrUUID());
            }
        } catch (Exception e) {
            LOGGER.error("Exception while setting IdempotencyHeader in populateEcmOperationHeaders {}", e);
        }
        return headers;
    }

}
