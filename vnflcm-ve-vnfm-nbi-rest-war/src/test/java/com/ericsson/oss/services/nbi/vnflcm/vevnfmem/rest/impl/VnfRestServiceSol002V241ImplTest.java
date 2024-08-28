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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;


import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.ericsson.oss.itpf.sdk.config.ConfigurationEnvironment;
import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.itpf.sdk.core.util.ServiceLoadingUtil;
import com.ericsson.oss.itpf.sdk.resources.Resources;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.NfvoConfigHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services.NfvoCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NfvoConfig;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VnfRestServiceSol002V241Impl;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimConnectionInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.TerminateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.TerminateVnfRequestType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.CreateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtManagedVirtualLinkData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InterfaceInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtVirtualLinkData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.Credentials;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtLinkPortData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ResourceHandle;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInfoModificationRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.AccessInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.AdditionalParam;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiInstantiateVnfRequest;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ResteasyServerBootstrap;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VimService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.LcmOperationService;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperationResponse;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfCreateDeleteOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfCreation;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfDescriptorDTO;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfInstantiation;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfInstantiationOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfUpdate;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfUpdateOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.model.Vim;
import com.ericsson.oss.services.vnflcm.api_base.model.VimTenant;
import com.ericsson.oss.services.vnflcm.api_base.model.VimSubTenant;

import com.ericsson.oss.services.vnflcm.file.util.FileHandler;
import com.ericsson.oss.services.vnflcm.nfvo.api.NfvoService;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.NfvoDTO;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.NfvoEndPointsDTO;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.NfvoPropertiesDTO;

/**
 * Test class for VnfRestServiceSol002V241Impl
 */

@PrepareForTest({ VnfRestServiceSol002V241Impl.class, ConfigurationEnvironment.class, ServiceLoadingUtil.class, ServiceLoader.class, Resources.class,
        ServiceFinderBean.class, FileResourceService.class, FileHandler.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VnfRestServiceSol002V241ImplTest {

    private static String vnfModifyRequest = "{ \"vnfInstanceName\": \"abc\",\"vnfInstanceDescription\": \"vnfDescription\",\"vnfPkgId\": \"vnfId\",\"vnfConfigurableProperties\": \"vnfConfigurableProperties\",\"metadata\": \"metadata\",\"extensions\": \"extensions\",\"additionalParams\": \"additionalParams\"}";
    private static String vnfTerminateRequest = "{ \"terminationType\": \"GRACEFUL\",\"gracefulTerminationTimeout\": 10}";
    private static String vnfInstantiateRequest = "{ \"flavourId\": \"cee\", \"instantiationLevelId\": \"instantiation_level_1\", \"extVirtualLinks\": [{ \"id\": \"45672c22-7c12-49ed-8a4f-e4532b3026fb\", \"vimConnectionId\": \"47772c22-7c12-49ed-8a4f-e7625b3026fb\", \"resourceId\": \"45672c22-4r78-49ed-8a4f-e4532b3026fb\", \"extCps\": [{ \"cpdId\": \"SERVICES_ExtCp\", \"cpConfig\": [{ \"cpInstanceId\": \"cpInstanceId111\", \"linkPortId\": \"linkPortId222\", \"cpProtocolData\": [{ \"layerProtocol\": \"IP_OVER_ETHERNET\", \"ipOverEthernet\": { \"macAddress\": \"fa:16:3e:23:fd:d7\", \"ipAddresses\": [{ \"type\": \"IPV4\", \"fixedAddresses\": [ \"131.160.162.16\" ], \"numDynamicAddresses\": \"1\", \"addressRange\": { \"minAddress\": \"131.160.162.16\", \"maxAddress\": \"131.160.162.36\" }, \"subnetId\": \"\" }] } }] }] }], \"extLinkPorts\": [{ \"id\": \"id\", \"resourceHandle\": { \"vimConnectionId\": \"47772c22-7c12-49ed-8a4f-e7625b3026fb\", \"resourceProviderId\": \"\", \"resourceId\": \"491522c2-f023-4162-b097-55a9a71972f3\", \"vimLevelResourceType\": \"\" } }] }],\"additionalParams\":{\"servicesImage\":\"49ba9671-a9e4-4252-ba2b-bc1862a98746\",\"services_flavor\": \"d0ef440a-bc92-4f04-9ca0-d97b9b3291c4\",\"ip_version\":4,\"external_net_id\":\"491522c2-f023-4162-b097-55a9a71972f3\",\"services_vm_count\":1,\"external_subnet_cidr\":\"131.160.162.0/25\",\"external_subnet_gateway\":\"131.160.162.1\",\"external_ip_for_services_vm\":\"131.160.162.16\",\"cloudManagerType\":\"CEE\",\"ossType\":\"ENM\",\"ossMasterHostName\":\"masterservice\",\"ossMasterHostIP\":\"1.1.1.1\",\"ossNotificationServiceHost\":\"notificationservice\",\"ossNotificationServiceIP\":\"1.1.1.1\",\"ossUserName\":\"root\"}}' http://localhost:8080/vnflcm/v1/vnf_instances/\"e3b0b949-3501-11e9-aa06-fa163ea920d7\"/instantiate";
    protected static final String ROOT_RESOURCE_PATH = "/vevnfmem";

    private static ResteasyServerBootstrap serverBootstrap;
    private static FileResourceService fileResourceService;
    protected static VnfDescriptorDTO vnfDescriptorDaoImpl;
    @Mock
    protected static NfvoCallExecution orvnfmQueryPackage;

    @Mock
    protected static NfvoCallExecution orvnfmPackageManagement;
    private static VnfService vnfService;
    private static VimService vimService;

    @Mock
    private static NfvoConfig nfvoConfigData;

    @Mock
    private static NfvoConfigHelper nfvoConfigHelper;
    private static VnfUpdate vnfUpdate;
    private static VnfUpdateOpConfig vnfUpdateOpConfig;
    private static VnfInstantiation vnfInstantiation;
    @Mock
    private static VnfInstantiationOpConfig vnfInstantiationOpConfig;
    @Mock
    private static VnfCreateDeleteOpConfig vnfDeletionConfig;
    @Mock
    private static ServiceFinderBean finder;
    @Mock
    private static NfvoService nfvoService;
    @Mock
    private static LcmOperationService lcmOpService;
    @Mock
    private static LcmOperationResponse lcmOperationOccurence;
    @Mock
    private static NfvoDTO activeNfvoConfiguration;
    @Mock
    private static ConfigurationEnvironment configurationEnvironment;
    public static final String PACKAGE_DOWNLOAD = "packageDownload";
    final UriInfo uriInfo = null;

    @BeforeClass
    public static void setupRestEasy() throws Exception {
        fileResourceService = Mockito.mock(FileResourceService.class);
        when(fileResourceService.exists(any(String.class))).thenReturn(true);
        vnfDescriptorDaoImpl = Mockito.mock(VnfDescriptorDTO.class);
        orvnfmQueryPackage = Mockito.mock(NfvoCallExecution.class);
        orvnfmPackageManagement = Mockito.mock(NfvoCallExecution.class);
        vnfService = Mockito.mock(VnfService.class);
        vimService = Mockito.mock(VimService.class);
        nfvoConfigData = Mockito.mock(NfvoConfig.class);
        nfvoConfigHelper = Mockito.mock(NfvoConfigHelper.class);
        vnfUpdate = Mockito.mock(VnfUpdate.class);
        vnfUpdateOpConfig = Mockito.mock(VnfUpdateOpConfig.class);
        //vnfDeletionConfig = Mockito.mock(VnfCreateDeleteOpConfig.class);
        vnfInstantiation = Mockito.mock(VnfInstantiation.class);
        nfvoService = Mockito.mock(NfvoService.class);
        finder = Mockito.mock(ServiceFinderBean.class);
        lcmOpService = Mockito.mock(LcmOperationService.class);
        lcmOperationOccurence = Mockito.mock(LcmOperationResponse.class);
        activeNfvoConfiguration = Mockito.mock(NfvoDTO.class);
        configurationEnvironment = Mockito.mock(ConfigurationEnvironment.class);
        System.setProperty("isTest", "true");
        /*
         * RestAssured.port = 58591; serverBootstrap = new ResteasyServerBootstrap(ROOT_RESOURCE_PATH, new NBIRestApplicationWrapper(vnfService,
         * fileResourceService)); serverBootstrap.start();
         */
        System.setProperty("isTest", "true");
    }

    @AfterClass
    public static void tearDown() {
        //serverBootstrap.stop();
    }

    @Test
    public void testCreateVNFIdentifier() {
        /*
         * final String vnfdWrapper =
         * "{\"dataVNFDSpecific\": {\"vnfdId\": \"vnflaf\",\"vnfdVersion\": \"2.2.28-SNAPSHOT\",\"vnfProvider\": \"Ericsson\",\"vnfProductName\": \"mme\",\"vnfSoftwareVersion\": \"16B\",\"flavourId\": [\"cee\"],\"vnfLcmOperationsConfiguration\": {\"scale\": {}},\"scalingAspect\":[{\"aspectId\":\"processing\" ,\"name\":\"processing\",\"description\":\"\",\"maxScaleLevel\":4},{\"aspectId\":\"database\" ,\"name\":\"database\",\"description\":\"\",\"maxScaleLevel\":15} ]},\"VnfConfigurableProperties\":{\"isAutoScaleEnabled\": \"true\",\"isAutoHealingEnabled\":\"true\"},\"instantiateVnfOpConfig\": {\"servicesImage\":\"ERICvnflafdevimage_CXP9032638-4.0.21.qcow2\",\"services_flavor\":\"m1.medium\",\"ip_version\":4,\"external_net_id\":\"491522c2-f023-4162-b097-55a9a71972f3\",\"external_subnet_cidr\":\"131.160.162.0/25\",\"external_subnet_gateway\":\"131.160.162.1\",\"external_ip_for_services_vm\":\"131.160.162.19\",\"cloudManagerType\":\"CEE\",\"ossType\":\"OSSRC\",\"ossMasterHostName\":\"masterservice\",\"ossMasterHostIP\":\"1.1.1.1\",\"ossNotificationServiceHost\":\"notificationservice\",\"ossNotificationServiceIP\":\"1.1.1.1\",\"ossUserName\":\"root\"},\"scaleVnfOpConfig\": {\"servicesImage\":\"ERICvnflafdevimage_CXP9032638-4.0.21.qcow2\",\"services_flavor\":\"m1.medium\",\"ip_version\":4,\"external_net_id\":\"491522c2-f023-4162-b097-55a9a71972f3\",\"external_subnet_cidr\":\"131.160.162.0/25\",\"external_subnet_gateway\":\"131.160.162.1\",\"external_ip_for_services_vm\":\"131.160.162.55\",\"external_ipv4_for_db_vm\": \"131.160.162.90\",\"internal_ipv4_for_db_vm\": \"172.16.100.9\",\"cloudManagerType\":\"CEE\",\"ossType\":\"OSSRC\",\"ossMasterHostName\":\"masterservice\",\"ossMasterHostIP\":\"1.1.1.1\",\"ossNotificationServiceHost\":\"notificationservice\",\"ossNotificationServiceIP\":\"1.1.1.1\",\"ossUserName\":\"root\"}}"
         * ; final RestCallDataDto restCallData = new RestCallDataDto(); Map<String,String> map = new HashMap<String,String>();
         * restCallData.setHeaders(map); String url="baseUrl/?vnfdId= + vnfdId"; restCallData.setUrl(url); restCallData.setReqBody("reqBody");
         * VnfResponse vnfResponse = new VnfResponse(); vnfResponse.setVnfdId("Self-test-1"); final CreateVnfRequest createVnfRequest = new
         * CreateVnfRequest(); createVnfRequest.setVnfdId("Self-test-1"); try { NfvoConfig nfvoConfig = new NfvoConfig();
         * nfvoConfig.setOrVnfmVersion("SOL241"); VnfDescriptorDTO vnfDescriptorDTO = new VnfDescriptorDTO(); PackageManagementListenerData
         * packageManagementListenerData = new PackageManagementListenerData(); packageManagementListenerData.setRequestType("PACKAGE");
         * packageManagementListenerData.setVnfdId("Self-test-1"); final ProcessDataDto processData = new ProcessDataDto();
         * processData.setCallExecutionType(CallExecutionType.PACKAGE); processData.setCorrelationId("tsywtyqw");
         * processData.setPackageManagementListenerData(packageManagementListenerData); vnfDescriptorDTO.setVnfdId("Self-test-1");
         * when(vnfDescriptorService.createVnfDescriptor(vnfDescriptorDTO, null)).thenReturn(vnfDescriptorDTO);
         * when(vnfDescriptorService.updateVnfDescriptor(vnfDescriptorDTO, null)).thenReturn(vnfDescriptorDaoImpl); try {
         * when(ReadPropertiesUtility.readConfigProperty(Constants.PACKAGE_DOWNLOAD)).thenReturn(PACKAGE_DOWNLOAD); } catch (Exception e) {
         * e.printStackTrace(); } final VnfCreation vnfCreation = this.createVnfCreationRequest(createVnfRequest, "onboardPackageId");
         * when(vnfService.createVnf(vnfCreation,null)).thenReturn(vnfResponse); PowerMockito.mockStatic(FileHandler.class);
         * PowerMockito.when(FileHandler.getVnfWrapperContent(Mockito.anyString())).thenReturn(vnfdWrapper); final String responseBody =
         * expect().statusCode(Status.CREATED.getStatusCode()).given().contentType("application/json")
         * .body(createVnfRequest).when().post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances").prettyPrint();
         * assertNotNull(from(responseBody).getString("id")); assertNotNull(vnfResponse); } catch (VNFLCMServiceException e1) { }
         */}

    @Test
    public void testDeleteVnfId() {

        /*
         * final boolean status = true; final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153"; try {
         * when(vnfService.deleteVnfId(anyString(), any(VnfCreateDeleteOpConfig.class))).thenReturn(status); } catch (final VNFLCMServiceException e)
         * { e.printStackTrace(); } final String responseBody = expect().statusCode(Status.NO_CONTENT.getStatusCode()).given().when()
         * .delete(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/{vnfInstanceId}", vnfInstanceId).prettyPrint();
         * assertEquals(Status.NO_CONTENT.getStatusCode(), from(responseBody).getInt("status"));
         */
    }

    @Test
    public void testModifyVnf() {
        /*
         * final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153"; try { when(vnfService.modifyVnf(any(VnfUpdate.class),
         * any(VnfUpdateOpConfig.class))).thenReturn(true); } catch (VNFLCMServiceException e) { e.printStackTrace(); }//this still need to expend for
         * more coverage. final String responseBody = expect().statusCode(Status.ACCEPTED.getStatusCode()).given().contentType("application/json")
         * .body(vnfModifyRequest).when().post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/{vnfInstanceId}", vnfInstanceId).prettyPrint();
         * //assertEquals(modifyVnfInfo.getVnfInstanceName(), from(responseBody).getString("vnfInstanceName"));
         */}

    @Test
    public void testInstantiateVnf() {

        /*
         * final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153"; final InstantiateVnfRequest instantiateVnfRequest =
         * createInstantiateVnfRequest(); try { when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class)))
         * .thenReturn(instantiateVnfRequest.getFlavourId()); } catch (final VNFLCMServiceException e) { e.printStackTrace(); } final String
         * responseBody = expect().statusCode(Status.ACCEPTED.getStatusCode()).given().contentType("application/json")
         * .body(vnfInstantiateRequest).when().post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/{vnfInstanceId}/instantiate", vnfInstanceId)
         * .prettyPrint(); assertEquals(InstantiationState.INSTANTIATED.toString(), from(responseBody).getString("instantiationState"));
         */
    }
    
    @Test
    public void testInstiateVnfRequest() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        Vim vim = createVimObject();
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(vim);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null, idempotencyKey);
        assertNotNull(response);
        assertEquals(202, response.getStatus());
    }

    @Test
    public void testInstAutoRegisterVimValidationFail() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        VimConnectionInfo vimConnectionInfo = instantiateVnfRequest.getVimConnectionInfo().get(0);
        vimConnectionInfo.getAccessInfo().setDomainName("");
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(null);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null,idempotencyKey);
        assertNotNull(response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInstAutoRegisterVimValidationPass() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(null);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null, idempotencyKey);
        assertNotNull(response);
        assertEquals(202, response.getStatus());
    }
    
    @Test
    public void testInstVimWithDiffDomain() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        instantiateVnfRequest.getVimConnectionInfo().get(0).getAccessInfo().setDomainName("NewDomain");
        Vim vim =createVimObject();
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(vim);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null,idempotencyKey);
        assertNotNull(response);
        assertEquals(202, response.getStatus());
    }

    @Test
    public void testInstUpdateVimV3() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        VimConnectionInfo vimConnectionInfo = instantiateVnfRequest.getVimConnectionInfo().get(0);
        vimConnectionInfo.getAccessInfo().setDomainName("Default1");
        Vim vim = createVimObject();
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(vim);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null, idempotencyKey);
        assertNotNull(response);
        assertEquals(202, response.getStatus());
    }

    @Test
    public void testTerminateVnfInstance() {

        /*
         * final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153"; final TerminateVnfRequest terminateVnfRequest =
         * createTerminateVnfRequest(); try { when(vnfService.terminateVnf(any(String.class), any(VnfInstantiationOpConfig.class)))
         * .thenReturn(terminateVnfRequest.getTerminationType().toString()); } catch (final VNFLCMServiceException e) { e.printStackTrace(); } final
         * String responseBody = expect().statusCode(Status.ACCEPTED.getStatusCode()).given().contentType("application/json")
         * .body(vnfTerminateRequest).when().post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/{vnfInstanceId}/terminate", vnfInstanceId)
         * .prettyPrint(); assertEquals(200, from(responseBody).getInt("status"));
         */
    }
    
    @Test
    public void testTerminateVnfInstanceGraceful() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final TerminateVnfRequest terminateVnfRequest = createTerminateVnfRequestGraceful();
        try {
            when(vnfService.terminateVnf(any(String.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.terminateVnfInstance(vnfInstanceId, terminateVnfRequest, uriInfo, null);
        assertNotNull(response);
        assertEquals(202, response.getStatus());
    }

    @Test
    public void testTerminateVnfInstanceForceful() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final TerminateVnfRequest terminateVnfRequest = createTerminateVnfRequestForceful();
        try {
            when(vnfService.terminateVnf(any(String.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.terminateVnfInstance(vnfInstanceId, terminateVnfRequest, uriInfo, null);
        assertNotNull(response);
        assertEquals(202, response.getStatus());
    }

    private TerminateVnfRequest createTerminateVnfRequestGraceful() {
        final TerminateVnfRequest terminateVnfRequest = new TerminateVnfRequest();
        terminateVnfRequest.setGracefulTerminationTimeout(200);
        terminateVnfRequest.setTerminationType(TerminateVnfRequestType.GRACEFUL);
        terminateVnfRequest.setAdditionalParams("abc", "abc");
        return terminateVnfRequest;
    }

    private TerminateVnfRequest createTerminateVnfRequestForceful() {
        final TerminateVnfRequest terminateVnfRequest = new TerminateVnfRequest();
        terminateVnfRequest.setTerminationType(TerminateVnfRequestType.FORCEFUL);
        terminateVnfRequest.setAdditionalParams("abc", "abc");
        return terminateVnfRequest;
    }

    private VnfCreation createVnfCreationRequest(final CreateVnfRequest createVnfRequest, final String onboardPackageId) {
        if (createVnfRequest != null) {
            final VnfCreation vnfCreation = new VnfCreation();
            if (createVnfRequest.getVnfInstanceName() != null && !createVnfRequest.getVnfInstanceName().isEmpty()) {
                vnfCreation.setVnfName(createVnfRequest.getVnfInstanceName().trim());
            }
            if (createVnfRequest.getVnfInstanceDescription() != null && !createVnfRequest.getVnfInstanceDescription().isEmpty()) {
                vnfCreation.setVnfDescription(createVnfRequest.getVnfInstanceDescription().trim());
            }
            vnfCreation.setVnfdId(createVnfRequest.getVnfdId().trim());
            if (onboardPackageId != null) {
                vnfCreation.setOnboardedPackageInfoId(onboardPackageId.trim());
            } else if (onboardPackageId == null) {
                if (createVnfRequest.getAdditionalParams() != null) {
                    if (createVnfRequest.getAdditionalParams().getVnfPkgId() != null) {
                        vnfCreation.setOnboardedPackageInfoId(createVnfRequest.getAdditionalParams().getVnfPkgId().trim());
                    } else {
                        vnfCreation.setOnboardedPackageInfoId(createVnfRequest.getVnfdId());
                    }
                } else {
                    vnfCreation.setOnboardedPackageInfoId(createVnfRequest.getVnfdId());
                }
            }
            if (createVnfRequest.getAdditionalParams() != null) {
                if (createVnfRequest.getAdditionalParams().getHotPackageName() != null
                        && !createVnfRequest.getAdditionalParams().getHotPackageName().isEmpty()) {
                    vnfCreation.setHotPackageName(createVnfRequest.getAdditionalParams().getHotPackageName().trim());
                }
            }
            return vnfCreation;
        }
        return null;
    }

    private NbiInstantiateVnfRequest createInstantiateVnfRequest() {
        final NbiInstantiateVnfRequest instantiateVnfRequest = new NbiInstantiateVnfRequest();
        final List<ExtManagedVirtualLinkData> extManagedVirtualLinkList = new ArrayList<ExtManagedVirtualLinkData>();
        final List<VimConnectionInfo> vimConnectionInfor = new ArrayList<VimConnectionInfo>();
        final VimConnectionInfo vimConnectionInfo = new VimConnectionInfo();
        final ExtManagedVirtualLinkData extManagedVirtualLinkData = new ExtManagedVirtualLinkData();
        extManagedVirtualLinkData.setId("abc");
        extManagedVirtualLinkData.setResourceId("abc");
        extManagedVirtualLinkData.setResourceProviderId("provider");
        extManagedVirtualLinkData.setVimConnectionId("abc");
        extManagedVirtualLinkData.setVirtualLinkDescId("abc");
        extManagedVirtualLinkList.add(0, extManagedVirtualLinkData);
        instantiateVnfRequest.setFlavourId("m.small");
        final AdditionalParam additionalParams = new AdditionalParam();
        additionalParams.setAdditionalAttributes("abc", "abc");
        instantiateVnfRequest.setInstantiationLevelId("123456");
        instantiateVnfRequest.setLocalizationLanguage("EN");
        final ExtVirtualLinkData extVirtualLinkData = new ExtVirtualLinkData();
        extVirtualLinkData.setId("abc");
        extVirtualLinkData.setResourceId("12345");
        extVirtualLinkData.setResourceProviderId("ResourceProvider");
        extVirtualLinkData.setVimConnectionId("12345");
        vimConnectionInfo.setId("default");
        vimConnectionInfo.setVimId("vim1");
        vimConnectionInfo.setVimType("OPENSTACK");
        final InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setIdentityEndPoint("https://cloud12a.athtem.eei.ericsson.se:13000/v3");
        vimConnectionInfo.setInterfaceInfo(interfaceInfo);
        final AccessInfo accessInfo = new AccessInfo();
        accessInfo.setProjectId("subTenantId");
        accessInfo.setDomainName("Default");
        accessInfo.setProjectName("subTenantName");
        accessInfo.setUserDomain("Default");
        final Credentials credentials= new Credentials();
        credentials.setUsername("TestuserName");
        credentials.setPassword("TestPassword");
        accessInfo.setCredentials(credentials);
        vimConnectionInfo.setAccessInfo(accessInfo);
        vimConnectionInfor.add(vimConnectionInfo);
        final ExtLinkPortData extLinkPortData = new ExtLinkPortData();
        final List<ExtLinkPortData> extLinkPortDataList = new ArrayList<ExtLinkPortData>();
        final ResourceHandle resourceHandle = new ResourceHandle();
        extLinkPortData.setId("abc");
        resourceHandle.setResourceId("abc");
        resourceHandle.setResourceProviderId("abc");
        resourceHandle.setVimConnectionId("abc");
        resourceHandle.setVimLevelResourceType("abc");
        extLinkPortData.setResourceHandle(resourceHandle);
        extLinkPortDataList.add(0, extLinkPortData);
        extVirtualLinkData.setExtLinkPorts(extLinkPortDataList);
        final List<ExtVirtualLinkData> extVirtualLinkDataList = new ArrayList<ExtVirtualLinkData>();
        extVirtualLinkDataList.add(0, extVirtualLinkData);
        instantiateVnfRequest.setExtVirtualLinks(extVirtualLinkDataList);
        instantiateVnfRequest.setExtManagedVirtualLinks(extManagedVirtualLinkList);
        instantiateVnfRequest.setVimConnectionInfo(vimConnectionInfor);
        return instantiateVnfRequest;
    }
    
    private Vim createVimObject() {
        Vim vim = new Vim();
        vim.setAuthURL("https://cloud12a.athtem.eei.ericsson.se:13000/v3");
        vim.setVimName("vim1");
        vim.setVimType("OPENSTACK");
        List<VimTenant> vimTenantInfos = new ArrayList<VimTenant>();
        VimTenant vimTenant = new VimTenant();
        vimTenant.setDefaultTenant(true);
        vimTenant.setTenantName("Default");
        List<VimSubTenant> vimSubTenants = new ArrayList<VimSubTenant>();
        VimSubTenant vimSubTenant= new VimSubTenant();
        vimSubTenant.setSubTenantName("subTenantName");
        vimSubTenant.setSubTenantId("subTenantId");
        vimSubTenant.setDefaultSubTenant(true);
        com.ericsson.oss.services.vnflcm.api_base.model.Credentials credentials = new com.ericsson.oss.services.vnflcm.api_base.model.Credentials();
        credentials.setUsername("TestuserName");
        credentials.setPassword("TestPassword");
        vimSubTenant.setCredentials(credentials);
        vimSubTenants.add(vimSubTenant);
        vimTenant.setVimSubTenants(vimSubTenants);
        vimTenantInfos.add(vimTenant);
        vim.setVimTenantInfos(vimTenantInfos);
        
        return vim;
    }
    private void getNfvoConfigProp() {
        final NfvoDTO activeNfvoConfiguration = new NfvoDTO();
        activeNfvoConfiguration.setAuthType("normal");
        activeNfvoConfiguration.setBaseUrl("http://");
        activeNfvoConfiguration.setHostIpAddress("131.160.159.45");
        activeNfvoConfiguration.setHostName("ENM");
        activeNfvoConfiguration.setId("123");
        final NfvoEndPointsDTO nfvoEndPoint = new NfvoEndPointsDTO();
        nfvoEndPoint.setEndPointName("lifecycleNotificationUrl");
        nfvoEndPoint.setEndPointUrl("http://127.0.0.1:58080/NotificationEndpoint");
        final NfvoEndPointsDTO nfvoEndPoint1 = new NfvoEndPointsDTO();
        nfvoEndPoint1.setEndPointName("createNotificationUrl");
        nfvoEndPoint1.setEndPointUrl("http://127.0.0.1:58080/NotificationEndpoint");
        final NfvoEndPointsDTO nfvoEndPoint2 = new NfvoEndPointsDTO();
        nfvoEndPoint2.setEndPointName("deleteNotificationUrl");
        nfvoEndPoint2.setEndPointUrl("http://localhost:58080/NotificationEndpoint");
        final NfvoEndPointsDTO nfvoEndPoint3 = new NfvoEndPointsDTO();
        nfvoEndPoint3.setEndPointName("packageManagementUrl");
        nfvoEndPoint3.setEndPointUrl("http://localhost:3000/vnfpkgm/v1/vnf_packages");
        final List<NfvoEndPointsDTO> nfvoEndPoints = new ArrayList<NfvoEndPointsDTO>();
        nfvoEndPoints.add(nfvoEndPoint);
        nfvoEndPoints.add(nfvoEndPoint1);
        nfvoEndPoints.add(nfvoEndPoint2);
        nfvoEndPoints.add(nfvoEndPoint3);
        activeNfvoConfiguration.setIsGrantSupported(true);
        activeNfvoConfiguration.setIsNotificationSupported(true);
        activeNfvoConfiguration.setNfvoEndPoints(nfvoEndPoints);
        activeNfvoConfiguration.setNfvoInUse("Yes");
        final NfvoPropertiesDTO nfvoPropertiesDTO = new NfvoPropertiesDTO();
        nfvoPropertiesDTO.setPropKey("abc");
        nfvoPropertiesDTO.setPropValue("abc");
        final List<NfvoPropertiesDTO> nfvoPropertiesDTOs = new ArrayList<NfvoPropertiesDTO>();
        nfvoPropertiesDTOs.set(0, nfvoPropertiesDTO);
        activeNfvoConfiguration.setNfvoProperties(nfvoPropertiesDTOs);
        activeNfvoConfiguration.setNfvoType("nfvoType");
        activeNfvoConfiguration.setOrVnfmVersion("SOL241");
        activeNfvoConfiguration.setPassword("test");
        activeNfvoConfiguration.setSubscriptionId("3211");
        activeNfvoConfiguration.setTenancyDetails("tid666");
        activeNfvoConfiguration.setUserName("test");
    }

    private VnfInfoModificationRequest createModifyVnfRequest() {
        final VnfInfoModificationRequest modifyVnf = new VnfInfoModificationRequest();
        modifyVnf.setVnfPkgId("pkgId");
        modifyVnf.setExtensions("abc", "abc");
        modifyVnf.setMetadata("abc", "abc");
        modifyVnf.setVnfConfigurableProperties("abc", "abc");
        modifyVnf.setVnfInstanceDescription("abc");
        modifyVnf.setVnfInstanceName("abc");
        return modifyVnf;
    }
    
    @Test
    public void testInstiateVnfRequestVimId() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final Vim vim = createVimObject();
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        instantiateVnfRequest.getVimConnectionInfo().get(0).getAccessInfo().setDomainName(null);
        instantiateVnfRequest.getVimConnectionInfo().get(0).getAccessInfo().setProjectId(null);
        instantiateVnfRequest.getVimConnectionInfo().get(0).getAccessInfo().setProjectName(null);;
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(vim);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null, idempotencyKey);
        assertNotNull(response);
        assertEquals(202, response.getStatus());
    }

    @Test
    public void testInstiateVimIdandDomain() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        final Vim vim = createVimObject();
        vim.getVimTenantInfos().get(0).getVimSubTenants().get(0).setSubTenantName(null);
        vim.getVimTenantInfos().get(0).getVimSubTenants().get(0).setSubTenantId(null);

        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);

        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null,idempotencyKey);
        assertNotNull(response);
        assertEquals(202,response.getStatus());
    }

    @Test
    public void testInstiateVnfProject() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        final Vim vim = createVimObject();
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(vim);

        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null, idempotencyKey);
        assertNotNull(response);
        assertEquals(202,response.getStatus());

    }
    
    @Test
    public void testInstiateDiffDomainWithoutProject() throws URISyntaxException {

        List<PathSegment> path = new ArrayList<PathSegment>();
        UriInfo uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vnflcm/v1/vnf_instances/"));
        final String vnfInstanceId = "1a9a13cb-bade-11e6-ac94-603320524153";
        final String vnfLcmOpId = "1a9a13cb-bade-11e6-ac94-603320524152";
        final String idempotencyKey = "1a9a13cb-bade-11e6-ac94-603320524154";
        final NbiInstantiateVnfRequest instantiateVnfRequest = createInstantiateVnfRequest();
        Vim vim = createVimObject();
        instantiateVnfRequest.getVimConnectionInfo().get(0).getAccessInfo().setProjectId(null);
        instantiateVnfRequest.getVimConnectionInfo().get(0).getAccessInfo().setProjectName(null);;
        instantiateVnfRequest.getVimConnectionInfo().get(0).getAccessInfo().setDomainName("Default1");
        try {
            when(vnfService.instantiateVNF(any(VnfInstantiation.class), any(VnfInstantiationOpConfig.class))).thenReturn(vnfLcmOpId);
            when(vimService.getVimByName("vim1")).thenReturn(vim);
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        final VnfRestServiceSol002V241Impl vnfRestServiceSol002V241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        Response response = null;
        response = vnfRestServiceSol002V241Impl.instantiateVnf(vnfInstanceId, instantiateVnfRequest, uriInfo, null, idempotencyKey);
        assertNotNull(response);
        assertEquals(400, response.getStatus());

    }
}
