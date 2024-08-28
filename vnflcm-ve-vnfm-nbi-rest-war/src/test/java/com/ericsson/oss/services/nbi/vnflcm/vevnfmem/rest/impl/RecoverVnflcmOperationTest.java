/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2022
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.nbi.vnflcm.vevnfmem.rest.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl.VnfLcmOperationRestServiceImpl;
import com.ericsson.oss.services.vnflcm.api_base.LcmOperationService;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperationResponse;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfResponse;
import com.ericsson.oss.services.vnflcm.common.dataTypes.ExceptionType;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OperationStatus;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OperationType;
import com.ericsson.oss.services.vnflcmwfs.api.LcmWorkflowAuthenticationService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VnfLcmOperationRestServiceImpl.class})
public class RecoverVnflcmOperationTest {

    @Mock
    private static VnfService vnfService;
    private static LcmOperationService lcmOpServiceMock;
    private static VnfResponse vnfResponseMock;
    private static HttpHeaders httpHeaders;
    private static LcmWorkflowAuthenticationService lcmwfsAuthenticationService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        lcmOpServiceMock = Mockito.mock(LcmOperationService.class);
        vnfService = Mockito.mock(VnfService.class);
        httpHeaders = Mockito.mock(HttpHeaders.class);
        lcmwfsAuthenticationService = Mockito.mock(LcmWorkflowAuthenticationService.class);
    }

    @Test
    public void testRetryOperation() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        
        VnfResponse vnfResponse = new VnfResponse();
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);

        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doNothing().when(lcmOpServiceMock).recoverVnfLcmOperation("retry", vnfLcmOpId);

            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);
            
            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);
            
            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.retryOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testRetryOperation(): Exception occured " + e.getMessage());
        }
    }
    
    @Test
    public void testRetryOperationUnAuthorizedUser() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        
        VnfResponse vnfResponse = new VnfResponse();
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);

        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doNothing().when(lcmOpServiceMock).recoverVnfLcmOperation("retry", vnfLcmOpId);

            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);
            
            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);
            
            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.retryOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testRetryOperationUnAuthorizedUser(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testRollbackOperation() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        VnfResponse vnfResponse = new VnfResponse();
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);

        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doNothing().when(lcmOpServiceMock).recoverVnfLcmOperation("retry", vnfLcmOpId);
            
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);
            
            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);

            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.rollbackOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testRollbackOperation(): Exception occured " + e.getMessage());
        }
    }
    
    @Test
    public void testRollbackOperationUnAuthorized() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        VnfResponse vnfResponse = new VnfResponse();
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);

        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doNothing().when(lcmOpServiceMock).recoverVnfLcmOperation("retry", vnfLcmOpId);
            
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);
            
            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);

            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.rollbackOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testRollbackOperation(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testFailOperation() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        VnfResponse vnfResponse = new VnfResponse();
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);

        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doNothing().when(lcmOpServiceMock).recoverVnfLcmOperation("fail", vnfLcmOpId);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);
            
            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);

            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.failOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.OK.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testFailOperation(): Exception occured " + e.getMessage());
        }
    }
    
    @Test
    public void testFailOperationUnAuthorized() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        VnfResponse vnfResponse = new VnfResponse();
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);

        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doNothing().when(lcmOpServiceMock).recoverVnfLcmOperation("fail", vnfLcmOpId);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);
            
            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);

            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.failOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testFailOperationUnAuthorized(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testConflict() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        VnfResponse vnfResponse = new VnfResponse();
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.PROCESSING);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);
        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doNothing().when(lcmOpServiceMock).recoverVnfLcmOperation("retry", vnfLcmOpId);
            
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(false);

            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.retryOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.CONFLICT.getStatusCode(), response.getStatus());

        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testConflict(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testInvalidParam() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);
        final VNFLCMServiceException vnflcmServiceException = new VNFLCMServiceException(
                "Required parameter missing.");
        vnflcmServiceException.setType(ExceptionType.ERROR_INVALID_INPUT);
        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doThrow(vnflcmServiceException).when(lcmOpServiceMock).recoverVnfLcmOperation("retry", vnfLcmOpId);

            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(false);
            
            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.retryOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testInvalidParam(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testErrorPerformingRecoveryAction() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        lcmOperationResponse.setLastUserAction("rollback");
        final VNFLCMServiceException vnfex = new VNFLCMServiceException(
                "Recovery Action not permitted, check if operation status is correct");
        vnfex.setType(ExceptionType.ERROR_PERFORMING_RECOVERY_ACTION);
        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doThrow(vnfex).when(lcmOpServiceMock).recoverVnfLcmOperation("retry", vnfLcmOpId);

            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(false);
            
            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.retryOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testErrorPerformingRecoveryAction(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testLifecycleOpNotFound() {
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;
        final LcmOperationResponse lcmOperationResponse = new LcmOperationResponse();
        lcmOperationResponse.setVnfLcmOpOccId(vnfLcmOpId);
        lcmOperationResponse.setOperationStatus(OperationStatus.FAILED_TEMP);
        lcmOperationResponse.setOperation(OperationType.MODIFY_INFO);
        lcmOperationResponse.setCorrelationId("1a9a13cb-bade-11e6-ac94-603320524152");
        final VNFLCMServiceException ex = new VNFLCMServiceException(
                "Vnflcm operation occurence not found");
        ex.setType(ExceptionType.LIFECYCLE_OP_NOT_FOUND);
        try {
            when(lcmOpServiceMock.getLcmOperation(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            when(lcmOpServiceMock.getLcmOperationOccurence(vnfLcmOpId)).thenReturn(lcmOperationResponse);
            doThrow(ex).when(lcmOpServiceMock).recoverVnfLcmOperation("fail", vnfLcmOpId);

            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(OperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(false);
            
            final VnfLcmOperationRestServiceImpl vnflcmOpRestService = new VnfLcmOperationRestServiceImpl(vnfService, lcmwfsAuthenticationService);
            vnflcmOpRestService.setLcmOperationService(lcmOpServiceMock);
            response = vnflcmOpRestService.failOperation(vnfLcmOpId, httpHeaders);
            assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("RecoverVnflcmOperationTest: testErrorPerformingRecoveryAction(): Exception occured " + e.getMessage());
        }
    }
}