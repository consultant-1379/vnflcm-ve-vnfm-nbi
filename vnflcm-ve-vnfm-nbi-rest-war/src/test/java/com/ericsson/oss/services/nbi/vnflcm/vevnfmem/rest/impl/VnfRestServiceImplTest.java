/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
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
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.itpf.sdk.core.util.ServiceLoadingUtil;
import com.ericsson.oss.itpf.sdk.resources.Resources;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.NfvoConfigHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl.VnfRestServiceImpl;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ChangeCurrentVnfPkgRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.LcmOperationStateType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.LcmOperationType;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.VimService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.ChangeCurrentVNFPackageDto;
import com.ericsson.oss.services.vnflcm.api_base.dto.ChangeCurrentVnfPackageOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.ContainerizedVnf;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfCreateDeleteOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfDescriptorOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfResponse;
import com.ericsson.oss.services.vnflcm.api_base.model.Vim;
import com.ericsson.oss.services.vnflcm.api_base.model.VimTenant;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OperationType;
import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;
import com.ericsson.oss.services.vnflcm.file.util.ToscaArtifactsParser;
import com.ericsson.oss.services.vnflcm.nfvo.api.NfvoService;
import com.ericsson.oss.services.vnflcmwfs.api.LcmWorkflowAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ToscaArtifactsParser.class, ReadPropertiesUtility.class, VnfRestServiceImpl.class, ServiceFinderBean.class, 
	ServiceLoadingUtil.class, ServiceLoader.class, Resources.class })
public class VnfRestServiceImplTest {

    private static VnfService vnfService;
    private static HttpHeaders httpHeaders;
    private static LcmWorkflowAuthenticationService lcmwfsAuthenticationService;
    private static InputStream inputStream;
    private static UriInfo uriInfo;
    private static ChangeCurrentVnfPkgRequest changeCurrentVnfRequest;
    private static NfvoConfigHelper nfvoConfigHelper;
    private static ServiceFinderBean finder;
    private static ObjectMapper mapper;
    private static VimService vimService;
    private static Vim vim;
    private static String instantiateVnfRequest = "{ \"flavourId\": \"cee\", \"additionalParams\":{\"servicesImage\":\"ERICrhelvnflafimage_CXP9032490-7.6.2_CDB\",\"services_flavor\":\"m1.small\",\"ip_version\":4,\"services_vm_count\": 1,\"external_net_id\":\"6dee1772-81d8-400f-a3da-b1d0106e27a3\",\"external_subnet_cidr\":\"10.232.8.0/22\",\"external_subnet_gateway\":\"10.232.8.1\",\"external_ip_for_services_vm\":\"10.232.8.103\",\"cloudManagerType\":\"OPENSTACK\",\"ossType\":\"ENM\",\"ossMasterHostName\":\"masterservice\",\"ossMasterHostIP\":\"1.1.1.1\",\"ossNotificationServiceHost\":\"notificationservice\",\"ossNotificationServiceIP\":\"1.1.1.1\",\"ossUserName\":\"root\",\"addVNFToOSS\":false},\"vimConnectionInfo\": [{ \"id\": \"cab32f669c18404d8bed0fae6bf088aa\", \"vimId\": \"vim1\", \"vimType\": \"OPENSTACK\",\"interfaceInfo\": { \"identityEndPoint\": \"https://cloud12a.athtem.eei.ericsson.se:13000/v3\" }, \"accessInfo\": { \"projectId\": \"c03a08f9f7404dadbf10342521adf37a\", \"credentials\": { \"username\": \"T1JDSF9WTkZMQ01fSHVycmljYW5lX0MxMkExN19hZG1pbg==\", \"password\": \"YWRtaW4xMjM=\" } } }] }";
    private static String scaleVnfRequest = "{\"type\":\"SCALE_OUT\", \"aspectId\":\"VNFLAF-Services\", \"numberOfSteps\":1}";
    private static String changeVnfRequest = "{ \"vnfdId\" : \"vnflaf\", \"additionalParams\":{\"isResourceChange\" : true,\"servicesImage\":\"ERICrhelvnflafimage_CXP9032490-7.6.2_CDB\", \"services_vm_count\": 1, \"services_flavor\":\"CM-vnflafecm_VNFLAF-Services\", \"ip_version\":4, \"external_net_id\":\"6dee1772-81d8-400f-a3da-b1d0106e27a3\", \"external_subnet_cidr\":\"10.232.8.0/22\", \"external_subnet_gateway\":\"10.232.8.1\", \"external_ip_for_services_vm\":\"10.232.8.71\",\"cloudManagerType\":\"OPENSTACK\", \"ossType\":\"OSSRC\", \"ossMasterHostName\":\"masterservice\", \"ossMasterHostIP\":\"1.1.1.1\", \"ossNotificationServiceHost\":\"notificationservice\", \"ossNotificationServiceIP\":\"1.1.1.1\", \"ossUserName\":\"root\"}, \"extensions\" :{\"http_proxy\": \"key_1_value\", \"https_proxy\": \"key_proxy1_value\"}, \"vnfConfigurableProperties\": {\"is_autoscale_enabled\": true,\"is_autoheal_enabled\": false},\"vimConnectionInfo\": [{ \"id\": \"cab32f669c18404d8bed0fae6bf088aa\", \"vimId\": \"vim1\", \"vimType\": \"OPENSTACK\",\"interfaceInfo\": { \"identityEndPoint\": \"https://cloud12a.athtem.eei.ericsson.se:13000/v3\" }, \"accessInfo\": { \"projectId\": \"c03a08f9f7404dadbf10342521adf37a\", \"credentials\": { \"username\": \"T1JDSF9WTkZMQ01fSHVycmljYW5lX0MxMkExN19hZG1pbg==\", \"password\": \"YWRtaW4xMjM=\" } } }]}";
    private static String healVnfRequest = "{\"cause\":\"Any String\",\"additionalParams\":{\"stackId\":\"a15344e8-52a3-47a9-9ed1-96471f058542\"}}";
    private static String terminateVnfRequest = "{\"terminationType\": \"FORCEFUL\"}";
    private static String modifyVnfRequest = "{ \"vnfInstanceName\": \"abc\",\"vnfInstanceDescription\": \"vnfDescription\",\"vnfPkgId\": \"vnfId\",\"vnfConfigurableProperties\": \"vnfConfigurableProperties\",\"metadata\": \"metadata\",\"extensions\": \"extensions\",\"additionalParams\": \"additionalParams\"}";
    private static ContainerizedVnf containerizedVnf;
    private static FileResourceService fileResourceService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        vnfService = Mockito.mock(VnfService.class);
        httpHeaders = Mockito.mock(HttpHeaders.class);
        lcmwfsAuthenticationService = Mockito.mock(LcmWorkflowAuthenticationService.class);
        inputStream = Mockito.mock(InputStream.class);
        uriInfo = Mockito.mock(UriInfo.class);
        changeCurrentVnfRequest = Mockito.mock(ChangeCurrentVnfPkgRequest.class);
        nfvoConfigHelper = Mockito.mock(NfvoConfigHelper.class);
        finder = Mockito.mock(ServiceFinderBean.class);
        mapper = Mockito.mock(ObjectMapper.class);
        vimService = Mockito.mock(VimService.class);
        vim = Mockito.mock(Vim.class);
        containerizedVnf = Mockito.mock(ContainerizedVnf.class);
        fileResourceService = Mockito.mock(FileResourceService.class);
    }

    @Test
    public void testScaleVnfUnAuthorized() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();

        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.SCALE.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);

            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            response = vnfRestServiceImpl.scaleVnf(vnfInstanceId, inputStream, uriInfo, httpHeaders);

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testScaleVnfUnAuthorized(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testHealVnfUnAuthorized() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();

        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.HEAL.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);

            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            response = vnfRestServiceImpl.healVnf(vnfInstanceId, inputStream, uriInfo, httpHeaders);

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testHealVnfUnAuthorized(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testModifyVnfUnAuthorized() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();

        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);

            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            response = vnfRestServiceImpl.modifyVnf(vnfInstanceId, inputStream, uriInfo, httpHeaders);

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testModifyVnfUnAuthorized(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testChangeVnfUnAuthorized() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();

        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.CHANGE_VNFPKG.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);

            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            response = vnfRestServiceImpl.changeVnfRequest(vnfInstanceId, changeCurrentVnfRequest, uriInfo, httpHeaders);

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testChangeVnfUnAuthorized(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testInstantiateVnfUnAuthorized() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();
        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.INSTANTIATE.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);

            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            vnfRestServiceImpl.setUnitTesting(true);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);
            PowerMockito.mockStatic(ReadPropertiesUtility.class);
            PowerMockito.when(ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE)).thenReturn("NO");

            when(nfvoConfigHelper.readNfvoConfig(any(String.class),any(String.class))).thenReturn(null);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = instantiateVnfRequest.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);

            response = vnfRestServiceImpl.instantiateVnf(vnfInstanceId, byteArrayInputStream, uriInfo, httpHeaders);

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testInstantiateVnfUnAuthorized(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testInstantiateVnf() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();
        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.INSTANTIATE.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);
            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);
            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);
            PowerMockito.mockStatic(ReadPropertiesUtility.class);
            PowerMockito.when(ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE)).thenReturn("NO");

            when(nfvoConfigHelper.readNfvoConfig(any(String.class),any(String.class))).thenReturn(null);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = instantiateVnfRequest.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);
            //when(mapper.readValue(instantiateVnfRequest, InstantiateVnfRequest.class)).thenReturn(createInstantiateVnfRequest());
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            vnfRestServiceImpl.setUnitTesting(true);
            response = vnfRestServiceImpl.instantiateVnf(vnfInstanceId, byteArrayInputStream, uriInfo, httpHeaders);

            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testInstantiateVnf(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testScaleVnf() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();
        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.SCALE.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);
            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);
            PowerMockito.mockStatic(ReadPropertiesUtility.class);
            PowerMockito.when(ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE)).thenReturn("NO");

            when(nfvoConfigHelper.readNfvoConfig(any(String.class),any(String.class))).thenReturn(null);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = scaleVnfRequest.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            vnfRestServiceImpl.setUnitTesting(true);
            response = vnfRestServiceImpl.scaleVnf(vnfInstanceId, byteArrayInputStream, uriInfo, httpHeaders);
            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testScaleVnf(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testHealVnf() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();
        try {
        	List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.HEAL.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);
            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);
            PowerMockito.mockStatic(ReadPropertiesUtility.class);
            PowerMockito.when(ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE)).thenReturn("NO");

            when(nfvoConfigHelper.readNfvoConfig(any(String.class),any(String.class))).thenReturn(null);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = healVnfRequest.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            vnfRestServiceImpl.setUnitTesting(true);
            response = vnfRestServiceImpl.healVnf(vnfInstanceId, byteArrayInputStream, uriInfo, httpHeaders);

            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testHealVnf(): Exception occured " + e.getMessage());
        }
    }

    private Vim vimConfiguration() {
        Vim vim1 = new Vim();
        vim1.setAuthURL("https://cloud12a.athtem.eei.ericsson.se:13000/v3");
        vim1.setId("f0e37488bc784bdcb28856919fdf540d");
        vim1.setVimName("vim12a16");
        vim1.setVimType("ETSINFV.OPENSTACK_KEYSTONE.V_2");
        vim1.setCloudHostIp("10.32.133.136");
        VimTenant vimTenant = new VimTenant();
        vimTenant.setTenantId("default");
        vimTenant.setDefaultTenant(Boolean.TRUE);
        com.ericsson.oss.services.vnflcm.api_base.model.Credentials credentials = new com.ericsson.oss.services.vnflcm.api_base.model.Credentials();
        credentials.setUsername("ORCH_VNFLCM_Andromeda_C12A16_admin");
        credentials.setPassword("admin123");
        vimTenant.setCredentials(credentials);
        List<VimTenant> vimTenantInfos = new ArrayList<>();
        vimTenantInfos.add(vimTenant);

        com.ericsson.oss.services.vnflcm.api_base.model.VimSubTenant subTenant = new com.ericsson.oss.services.vnflcm.api_base.model.VimSubTenant();
        subTenant.setDefaultSubTenant(Boolean.TRUE);
        subTenant.setSubTenantId("1353453");
        subTenant.setSubTenantName("VimSubTenant");
        com.ericsson.oss.services.vnflcm.api_base.model.Credentials credentials2 = new com.ericsson.oss.services.vnflcm.api_base.model.Credentials();
        credentials2.setUsername("ORCH_VNFLCM_Andromeda_C12A16_admin");
        credentials2.setPassword("admin123");
        subTenant.setCredentials(credentials2);
        List<com.ericsson.oss.services.vnflcm.api_base.model.VimSubTenant> vimSubTenants = new ArrayList<>();
        vimSubTenants.add(subTenant);
        vimTenant.setVimSubTenants(vimSubTenants);
        vim1.setVimTenantInfos(vimTenantInfos);
        return vim1;
    }

    @Test
    public void testTerminateVnf() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();
        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.TERMINATE.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);
            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);
            PowerMockito.mockStatic(ReadPropertiesUtility.class);
            PowerMockito.when(ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE)).thenReturn("NO");

            when(nfvoConfigHelper.readNfvoConfig(any(String.class),any(String.class))).thenReturn(null);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = terminateVnfRequest.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);
            //when(mapper.readValue(instantiateVnfRequest, InstantiateVnfRequest.class)).thenReturn(createInstantiateVnfRequest());
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            vnfRestServiceImpl.setUnitTesting(true);
            response = vnfRestServiceImpl.terminateVnfInstance(vnfInstanceId, byteArrayInputStream, uriInfo, httpHeaders);

            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testTerminateVnf(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testChangeVnf() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();
        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.CHANGE_VNFPKG.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);
            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);

            when(nfvoConfigHelper.readNfvoConfig(any(String.class),any(String.class))).thenReturn(null);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = changeVnfRequest.getBytes();
            ObjectMapper mapper = new ObjectMapper();
            ChangeCurrentVnfPkgRequest ccvpRequest = mapper.readValue(request, ChangeCurrentVnfPkgRequest.class);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);

            when(vnfService.changeCurrentVnfPackage(any(ChangeCurrentVNFPackageDto.class), any(ChangeCurrentVnfPackageOpConfig.class))).thenReturn("123123");
            UriBuilder uriBuilder = UriBuilder.fromPath("/");
            when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);

            vnfRestServiceImpl.setFileResourceService(fileResourceService);
            when(fileResourceService.exists(any(String.class))).thenReturn(Boolean.TRUE);
            ChangeCurrentVnfPkgRequest changeCurrentVnfPkgRequest = new ChangeCurrentVnfPkgRequest();
            changeCurrentVnfPkgRequest.setVnfdId("vnfl");
            when(changeCurrentVnfRequest.getVnfdId()).thenReturn("vnfdid");
            vnfRestServiceImpl.setUnitTesting(true);
            response = vnfRestServiceImpl.changeVnfRequest(vnfInstanceId, ccvpRequest, uriInfo, httpHeaders);

            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testChangeVnf(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testTerminateVnfUnAuthorized() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();
        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.TERMINATE.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(false);
            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);

            when(nfvoConfigHelper.readNfvoConfig(any(String.class),any(String.class))).thenReturn(null);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = terminateVnfRequest.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            vnfRestServiceImpl.setUnitTesting(true);
            response = vnfRestServiceImpl.terminateVnfInstance(vnfInstanceId, byteArrayInputStream, uriInfo, httpHeaders);

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testTerminateVnfUnAuthorized(): Exception occured " + e.getMessage());
        }
    }

    /*@Test
    public void testModifyVnf() {
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524152";
        Response response = null;

        VnfResponse vnfResponse = new VnfResponse();

        try {
            List<String> authorization = new ArrayList<>();
            authorization.add("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2S0NvZGdYQUJtRFhuWnBuQ21nMjI4RmM2dXB4MHlFOXgxdE9rZkJMV3k0In0.eyJleHAiOjE2NjU1ODg0MTAsImlhdCI6MTY2NTU4ODExMCwianRpIjoiOTJmMDE3YTQtMWI5ZC00M2JjLThiZjYtMmFmYTg5ODdhN2UwIiwiaXNzIjoiaHR0cHM6Ly9pYW0uY2NkLWM3YTAwNy1pY2NyLmF0aHRlbS5lZWkuZXJpY3Nzb24uc2UvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjpbIm1hc3Rlci1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiMzgxNTJlZGYtOGU1My00OTJkLWJmYzAtZDkyMGI4ZTVmY2U3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZW8iLCJzZXNzaW9uX3N0YXRlIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkVPIEFkbWluIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJVc2VyQWRtaW4iLCJWTSBWTkZNIFdGUyIsIkUtVk5GTSBTdXBlciBVc2VyIFJvbGUiLCJFLVZORk0gVUkgVXNlciBSb2xlIiwiTWV0cmljc1ZpZXdlciIsIlZNIFZORk0gVklFVyBXRlMiLCJFLVZORk0gVXNlciBSb2xlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkxvZ1ZpZXdlciIsIlN5c3RlbV9TZWN1cml0eV9DZXJ0TUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtdXNlcnMiLCJtYW5hZ2UtY2xpZW50cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTNlMzk2NTMtZTFmNi00NmIyLWEzNzEtZGNhYjRiZTk0OTZjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ2bmZtIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AMxai_wCwilZItyZMEowQUvPoesp9xosv3emwN2VSqOfKz8MY9Fx3Y8jkPB3qWH7i0KWQxQJ54wdgJqKVz_Dxn_DUfFs23K3dROMgRCES2K5LiYkD9A-fHl8k9Q0JAWIh15VAWAZpzwiEVlioXsvtY3DkpFCxwFAEJdfcBn09_-6BLr2HpUb4cQQUZ8It1kMdaiEDpI7v2nDzkjmPU3fWxTHzkYaYQsGIIpg2_5Z2M-B0g2eJm5nrbQjifou7IWliU5-UnyYqT_Y2QRHldsZJyC9XKem5MHErVp-PQIdpmXBAlso9J6HOElSXIOxass1dsI_ayC10yfMBacHMGGl8A");
            when(httpHeaders.getRequestHeader("Authorization")).thenReturn(authorization);
            when(lcmwfsAuthenticationService.checkLicense(LcmOperationType.MODIFY_INFO.name(), false)).thenReturn(true);

            PowerMockito.mockStatic(System.class);
            PowerMockito.when(lcmwfsAuthenticationService.isDracEnabled()).thenReturn(true);

            vnfResponse.setVnfdId("1a9a13cb-bade-11e6-ac94-603320524152");
            vnfResponse.setVnfName("testvnf");
            vnfResponse.setVnfProductName("VNFLAF");
            when(vnfService.queryVnf(any(String.class))).thenReturn(vnfResponse);

            when(lcmwfsAuthenticationService.checkDomainRoleAccess("VNFLAF", authorization.get(0))).thenReturn(true);
            final byte[] buffer = new byte[8192];
            when(inputStream.read(buffer)).thenReturn(-1);

            when(vnfService.getVnfdId(vnfInstanceId)).thenReturn("testvnfdId");
            PowerMockito.mockStatic(ToscaArtifactsParser.class);
            PowerMockito.when(ToscaArtifactsParser.isToscaVNFD(any(String.class))).thenReturn(false);

            PowerMockito.mockStatic(ServiceLoadingUtil.class);
            when(nfvoConfigHelper.readNfvoConfig(null)).thenReturn(null);
            PowerMockito.mockStatic(ServiceFinderBean.class);
            when(finder.find(NfvoService.class)).thenReturn(any(NfvoService.class));
            byte[] request = modifyVnfRequest.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
            Vim vim1 = vimConfiguration();
            when(vimService.getVimByName("vim1")).thenReturn(vim1);
            VnfRestServiceImpl vnfRestServiceImpl = new VnfRestServiceImpl(vnfService, lcmwfsAuthenticationService, vimService);
            response = vnfRestServiceImpl.modifyVnf(vnfInstanceId, byteArrayInputStream, uriInfo, httpHeaders);

            assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            fail("VnfRestServiceImplTest: testModifyVnf(): Exception occured " + e.getMessage());
        }
    }*/
}
