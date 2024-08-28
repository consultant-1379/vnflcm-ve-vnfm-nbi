package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.helper;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.EcmRestManipulation;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.CallExecutionData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.EcmRequestData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NfvoConfig;

public class EcmRestManipulationTest {

    @Test
    public void testIdempotencyHeaderNamePresent() {
        EcmRestManipulation ecmRestManipulation = new EcmRestManipulation();
        Map<String, String> headers = new HashMap<>();
        CallExecutionData callExecutionData = populateCallExecutionData();
        headers = ecmRestManipulation.populateEcmOperationHeaders(headers, callExecutionData);
        assertNotNull(headers.get(callExecutionData.getEmConfigList().get(0).getIdempotencyHeaderName()));
    }

    public static CallExecutionData populateCallExecutionData() {
        CallExecutionData callExecutionData = new CallExecutionData();
        callExecutionData.setEcmData(createEcmData());
        callExecutionData.setEmConfigList(createEmConfigList());
        return callExecutionData;
    }

    private static EcmRequestData createEcmData() {
        EcmRequestData ecmRequestData = new EcmRequestData();
        ecmRequestData.setEcmAuthUrl("auth123");
        ecmRequestData.setEcmAuthType("authType");
        ecmRequestData.setTenantId("ten123");
        ecmRequestData.setVdcId("vdc123");
        return ecmRequestData;
    }

    private static List<NfvoConfig> createEmConfigList() {
        List<NfvoConfig> nfvoconf = new ArrayList<NfvoConfig>();
        NfvoConfig nfvo = new NfvoConfig();
        nfvo.setIdempotencyHeaderName(UUID.randomUUID().toString());
        nfvo.setAuthType("Basic");
        nfvo.setUsername("admin");
        nfvoconf.add(nfvo);
        return nfvoconf;
    }

}
