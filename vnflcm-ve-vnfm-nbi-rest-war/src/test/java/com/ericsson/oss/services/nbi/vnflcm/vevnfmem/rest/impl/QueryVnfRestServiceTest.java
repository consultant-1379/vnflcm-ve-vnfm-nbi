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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ResteasyServerBootstrap;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryVnfRestServiceTest {
    protected static final String ROOT_RESOURCE_PATH = "/vevnfmem";
    private static ResteasyServerBootstrap serverBootstrap;
    private static VnfService vnfServiceMock;
    private static FileResourceService fileResourceService;
    private static final String VNF_RESOURCE_URL = ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances";
    private static final String VNF_IDENTIFER = "vnf123";
    private static final String VNF_IDENTIFER2 = "vnf456";

    @BeforeClass
    public static void setupRestEasy() throws Exception {
        vnfServiceMock = Mockito.mock(VnfService.class);
        fileResourceService = Mockito.mock(FileResourceService.class);
        when(fileResourceService.exists(any(String.class))).thenReturn(true);
        /*
         * RestAssured.port = 58586; serverBootstrap = new ResteasyServerBootstrap(ROOT_RESOURCE_PATH, new NBIRestApplicationWrapper(vnfServiceMock,
         * fileResourceService)); serverBootstrap.start();
         */
    }

    @AfterClass
    public static void tearDown() {
        //        serverBootstrap.stop();
    }

    @Test
    public void testGetVNFwithInstantiationInfo() {
        /*
         * final VnfInstanceUtil vnfInstanceUtil = new VnfInstanceUtil(); final VnfResponse vnfinstance =
         * vnfInstanceUtil.createVNFInstance(VNF_IDENTIFER); vnfinstance.setInstantiationState(InstantiatedState.INSTANTIATED); try {
         * when(vnfServiceMock.queryAndUpdateVnfWithResources(VNF_IDENTIFER)).thenReturn(vnfinstance); } catch (final VNFLCMServiceException e1) { //
         * Ignored } catch (final Exception e) { // Ignored } final String responseBody =
         * expect().statusCode(Status.OK.getStatusCode()).given().when() .get(VNF_RESOURCE_URL + "/{vnfInstanceId}", VNF_IDENTIFER).prettyPrint();
         * assertEquals(vnfinstance.getVnfId(), from(responseBody).getString("id")); assertEquals(vnfinstance.getVnfName(),
         * from(responseBody).getString("vnfInstanceName")); assertEquals(vnfinstance.getVnfDescription(),
         * from(responseBody).getString("vnfInstanceDescription")); assertEquals(vnfinstance.getVnfdId(), from(responseBody).getString("vnfdId"));
         * assertEquals(vnfinstance.getVnfProvider(), from(responseBody).getString("vnfProvider")); assertEquals(vnfinstance.getVnfProductName(),
         * from(responseBody).getString("vnfProductName")); assertEquals(vnfinstance.getVnfSoftwareVersion(),
         * from(responseBody).getString("vnfSoftwareVersion")); assertEquals(vnfinstance.getVnfdVersion(),
         * from(responseBody).getString("vnfdVersion")); assertEquals(vnfinstance.getOnboardedPackageInfoId(),
         * from(responseBody).getString("vnfPkgId")); assertEquals(InstantiationState.INSTANTIATED.toString(),
         * from(responseBody).getString("instantiationState")); assertEquals(vnfinstance.getInstantiatedVnfInfo().getFlavourId(),
         * from(responseBody).getString("instantiatedVnfInfo.flavourId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo.extVirtualLinkInfo[0].id"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getResourceHandle().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo.extVirtualLinkInfo[0].resourceHandle.vimConnectionId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getResourceHandle().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo.extVirtualLinkInfo[0].resourceHandle.resourceId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getLinkPorts().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo.extVirtualLinkInfo[0].extLinkPorts[0].id"));
         *
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfcResourceInfo[0].id"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getVduId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfcResourceInfo[0].vduId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getComputeResource().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfcResourceInfo[0].computeResource.vimConnectionId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getComputeResource().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfcResourceInfo[0].computeResource.resourceId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getComputeResource().getVimLevelResourceType(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfcResourceInfo[0].computeResource.vimLevelResourceType"));
         *
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getStorageResourceIds().get(0),
         * from(responseBody).getString("instantiatedVnfInfo.vnfcResourceInfo[0].storageResourceIds[0]"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getStorageResourceIds().get(1),
         * from(responseBody).getString("instantiatedVnfInfo.vnfcResourceInfo[0].storageResourceIds[1]"));
         *
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfVirtualLinkResourceInfo[0].id"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getVirtualLinkDescId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfVirtualLinkResourceInfo[0].vnfVirtualLinkDescId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getNetworkResource().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfVirtualLinkResourceInfo[0].networkResource.vimConnectionId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getNetworkResource().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfVirtualLinkResourceInfo[0].networkResource.resourceId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getNetworkResource().getVimLevelResourceType(),
         * from(responseBody).getString("instantiatedVnfInfo.vnfVirtualLinkResourceInfo[0].networkResource.vimLevelResourceType"));
         *
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo.virtualStorageResourceInfo[0].id"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getVirtualStorageDescId(),
         * from(responseBody).getString("instantiatedVnfInfo.virtualStorageResourceInfo[0].virtualStorageDescId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getStorageResource().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo.virtualStorageResourceInfo[0].storageResource.vimConnectionId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getStorageResource().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo.virtualStorageResourceInfo[0].storageResource.resourceId"));
         * assertEquals(vnfinstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getStorageResource().getVimLevelResourceType(),
         * from(responseBody).getString("instantiatedVnfInfo.virtualStorageResourceInfo[0].storageResource.vimLevelResourceType"));
         *
         * assertEquals(vnfinstance.get_links().getSelf().getHref(), from(responseBody).getString("_links.self.href"));
         * assertEquals(vnfinstance.get_links().getInstantiate().getHref(), from(responseBody).getString("_links.instantiate.href"));
         * assertEquals(vnfinstance.get_links().getTerminate().getHref(), from(responseBody).getString("_links.terminate.href"));
         */
    }

    @Test(timeout = 10000)
    public void testGetVNFwithoutInstantiationInfo() {
        /*
         * final VnfInstanceUtil vnfInstanceUtil = new VnfInstanceUtil(); final VnfResponse vnfinstance =
         * vnfInstanceUtil.createVNFInstance(VNF_IDENTIFER); vnfinstance.setInstantiationState(InstantiatedState.NOT_INTANTIATED); try {
         * when(vnfServiceMock.queryAndUpdateVnfWithResources(VNF_IDENTIFER)).thenReturn(vnfinstance); } catch (final VNFLCMServiceException e1) { //
         * Ignored } catch (final Exception e) { // Ignored }
         *
         * final String responseBody = expect().statusCode(Status.OK.getStatusCode()).given().when() .get(VNF_RESOURCE_URL + "/{vnfInstanceId}",
         * VNF_IDENTIFER).prettyPrint();
         *
         * assertEquals(vnfinstance.getVnfId(), from(responseBody).getString("id")); assertEquals(vnfinstance.getVnfName(),
         * from(responseBody).getString("vnfInstanceName")); assertEquals(vnfinstance.getVnfDescription(),
         * from(responseBody).getString("vnfInstanceDescription")); assertEquals(vnfinstance.getVnfdId(), from(responseBody).getString("vnfdId"));
         * assertEquals(vnfinstance.getVnfProvider(), from(responseBody).getString("vnfProvider")); assertEquals(vnfinstance.getVnfProductName(),
         * from(responseBody).getString("vnfProductName")); assertEquals(vnfinstance.getVnfSoftwareVersion(),
         * from(responseBody).getString("vnfSoftwareVersion")); assertEquals(vnfinstance.getVnfdVersion(),
         * from(responseBody).getString("vnfdVersion")); assertEquals(vnfinstance.getOnboardedPackageInfoId(),
         * from(responseBody).getString("vnfPkgId")); assertEquals(InstantiationState.NOT_INSTANTIATED.toString(),
         * from(responseBody).getString("instantiationState")); assertEquals(vnfinstance.get_links().getSelf().getHref(),
         * from(responseBody).getString("_links.self.href")); assertEquals(vnfinstance.get_links().getInstantiate().getHref(),
         * from(responseBody).getString("_links.instantiate.href")); assertEquals(vnfinstance.get_links().getTerminate().getHref(),
         * from(responseBody).getString("_links.terminate.href"));
         */
    }

    @Test
    public void testGetAllVNFwithInstantiationInfo() {
        /*
         * final VnfInstanceUtil vnfInstanceUtil = new VnfInstanceUtil(); final List<VnfResponse> vnfInstances = new ArrayList<VnfResponse>(); final
         * VnfResponse vnfinstance = vnfInstanceUtil.createVNFInstance(VNF_IDENTIFER); final VnfResponse vnfinstance2 =
         * vnfInstanceUtil.createVNFInstance2(VNF_IDENTIFER2); vnfinstance.setInstantiationState(InstantiatedState.INSTANTIATED);
         * vnfinstance2.setInstantiationState(InstantiatedState.INSTANTIATED); vnfInstances.add(vnfinstance); vnfInstances.add(vnfinstance2); try {
         * when(vnfServiceMock.queryAndUpdateAllVnfWithResources(Mockito.any(QueryOpConfig.class))).thenReturn(vnfInstances); } catch (final
         * VNFLCMServiceException e1) { // Ignored } catch (final Exception e) { // Ignored } final String responseBody =
         * expect().statusCode(Status.OK.getStatusCode()).given().when().get(VNF_RESOURCE_URL).prettyPrint(); for (int i = 0; i < vnfInstances.size();
         * i++) { assertEquals(vnfInstances.get(i).getVnfId(), from(responseBody).getString("id[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getVnfName(), from(responseBody).getString("vnfInstanceName[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getVnfDescription(), from(responseBody).getString("vnfInstanceDescription[" + i + "]"));
         *
         * assertEquals(vnfInstances.get(i).getVnfdId(), from(responseBody).getString("vnfdId[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getVnfProvider(), from(responseBody).getString("vnfProvider[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getVnfProductName(), from(responseBody).getString("vnfProductName[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getVnfSoftwareVersion(), from(responseBody).getString("vnfSoftwareVersion[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getVnfdVersion(), from(responseBody).getString("vnfdVersion[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getOnboardedPackageInfoId(), from(responseBody).getString("vnfPkgId[" + i + "]"));
         * assertEquals(InstantiationState.INSTANTIATED.toString(), from(responseBody).getString("instantiationState[" + i + "]"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getFlavourId(), from(responseBody).getString("instantiatedVnfInfo[" + i +
         * "].flavourId")); assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].extVirtualLinkInfo[0].id"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getResourceHandle().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].extVirtualLinkInfo[0].resourceHandle.vimConnectionId"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getResourceHandle().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].extVirtualLinkInfo[0].resourceHandle.resourceId"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getExtVirtualLinkInfo().get(0).getLinkPorts().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].extVirtualLinkInfo[0].extLinkPorts[0].id"));
         *
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfcResourceInfo[0].id"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getVduId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfcResourceInfo[0].vduId"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getComputeResource().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfcResourceInfo[0].computeResource.vimConnectionId"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getComputeResource().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfcResourceInfo[0].computeResource.resourceId"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getComputeResource().getVimLevelResourceType(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfcResourceInfo[0].computeResource.vimLevelResourceType"));
         *
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getStorageResourceIds().get(0),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfcResourceInfo[0].storageResourceIds[0]"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfcResourceInfo().get(0).getStorageResourceIds().get(1),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfcResourceInfo[0].storageResourceIds[1]"));
         *
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfVirtualLinkResourceInfo[0].id"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getVirtualLinkDescId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfVirtualLinkResourceInfo[0].vnfVirtualLinkDescId")); assertEquals(
         * vnfInstances.get(i).getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getNetworkResource().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfVirtualLinkResourceInfo[0].networkResource.vimConnectionId"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getNetworkResource().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].vnfVirtualLinkResourceInfo[0].networkResource.resourceId")); assertEquals(
         * vnfInstances.get(i).getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().get(0).getNetworkResource() .getVimLevelResourceType(),
         * from(responseBody) .getString("instantiatedVnfInfo[" + i + "].vnfVirtualLinkResourceInfo[0].networkResource.vimLevelResourceType"));
         *
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].virtualStorageResourceInfo[0].id"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getVirtualStorageDescId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].virtualStorageResourceInfo[0].virtualStorageDescId")); assertEquals(
         * vnfInstances.get(i).getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getStorageResource().getVimConnectionId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].virtualStorageResourceInfo[0].storageResource.vimConnectionId"));
         * assertEquals(vnfInstances.get(i).getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getStorageResource().getResourceId(),
         * from(responseBody).getString("instantiatedVnfInfo[" + i + "].virtualStorageResourceInfo[0].storageResource.resourceId")); assertEquals(
         * vnfInstances.get(i).getInstantiatedVnfInfo().getVirtualStorageResourceInfo().get(0).getStorageResource() .getVimLevelResourceType(),
         * from(responseBody) .getString("instantiatedVnfInfo[" + i + "].virtualStorageResourceInfo[0].storageResource.vimLevelResourceType"));
         *
         * assertEquals(vnfInstances.get(i).get_links().getSelf().getHref(), from(responseBody).getString("_links[" + i + "].self.href"));
         * assertEquals(vnfInstances.get(i).get_links().getInstantiate().getHref(), from(responseBody).getString("_links[" + i +
         * "].instantiate.href")); assertEquals(vnfInstances.get(i).get_links().getTerminate().getHref(), from(responseBody).getString("_links[" + i +
         * "].terminate.href")); }
         */
    }

    @Test(timeout = 10000)
    public void testGetVNFNotFound() {
        /*
         * final VNFLCMServiceException vnflcmServiceException = new VNFLCMServiceException("NO VnfInfoEntity found with id : Not_Found");
         * vnflcmServiceException.setType(ExceptionType.VNF_IDENTIFIER_NOT_FOUND); try {
         * when(vnfServiceMock.queryAndUpdateVnfWithResources("Not_Found")).thenThrow(vnflcmServiceException); } catch (final VNFLCMServiceException
         * e1) { // Ignored } catch (final Exception e) { // Ignored }
         *
         * final String responseBody = expect().statusCode(Status.NOT_FOUND.getStatusCode()).given().when() .get(VNF_RESOURCE_URL +
         * "/{vnfInstanceId}", "Not_Found").prettyPrint(); assertEquals("Identifier not found", from(responseBody).getString("title"));
         * assertEquals("VNF_IDENTIFIER_NOT_FOUND", from(responseBody).getString("type")); assertEquals("NO VnfInfoEntity found with id : Not_Found",
         * from(responseBody).getString("detail")); assertEquals("Not_Found", from(responseBody).getString("instance")); assertEquals(404,
         * from(responseBody).getInt("status"));
         */
    }

    @Test(timeout = 10000)
    public void testInternalServerError() throws InterruptedException {
        /*
         * final VNFLCMServiceException vnflcmServiceException = new VNFLCMServiceException("Error getting VNF Instance");
         * vnflcmServiceException.setType(ExceptionType.ERROR_FETCHING_VNF_INSTANCE); try {
         * when(vnfServiceMock.queryAndUpdateVnfWithResources(VNF_IDENTIFER)).thenThrow(vnflcmServiceException); } catch (final VNFLCMServiceException
         * e1) { // Ignored } catch (final Exception e) { // Ignored }
         *
         * final String responseBody = expect().statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode()).given().when() .get(VNF_RESOURCE_URL +
         * "/{vnfInstanceId}", VNF_IDENTIFER).prettyPrint(); assertEquals("internalError", from(responseBody).getString("title"));
         * assertEquals("ERROR_FETCHING_VNF_INSTANCE", from(responseBody).getString("type")); assertEquals("Error getting VNF Instance",
         * from(responseBody).getString("detail")); assertEquals("vnf123", from(responseBody).getString("instance")); assertEquals(500,
         * from(responseBody).getInt("status"));
         */
    }
}
