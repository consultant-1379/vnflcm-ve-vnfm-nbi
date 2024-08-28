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
package com.ericsson.oss.services.nbi.vnflcm.vevnfmem.rest.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ServiceLoader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.ericsson.oss.itpf.sdk.config.ConfigurationEnvironment;
import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.itpf.sdk.core.util.ServiceLoadingUtil;
import com.ericsson.oss.itpf.sdk.resources.Resources;
import com.ericsson.oss.services.nbi.vnflcm.vevnfmem.rest.impl.VeVnfmemRestResourceTest.VnfRestServiceWrapper;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ResteasyServerBootstrap;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.file.util.FileHandler;

@PrepareForTest({ VnfRestServiceWrapper.class, ConfigurationEnvironment.class, ServiceLoadingUtil.class, ServiceLoader.class, Resources.class,
        ServiceFinderBean.class, FileResourceService.class, FileHandler.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeleteVnfIdRestServiceTest {
    private static ResteasyServerBootstrap serverBootstrap;
    private static VnfService vnfServiceMock;
    private static FileResourceService fileResourceService;
    private static final String VNF_IDENTIFER = "vnf123";

    @BeforeClass
    public static void setupRestEasy() throws Exception {
        vnfServiceMock = Mockito.mock(VnfService.class);
        fileResourceService = Mockito.mock(FileResourceService.class);
        when(fileResourceService.exists(any(String.class))).thenReturn(true);
        /*
         * RestAssured.port = 58586; serverBootstrap = new ResteasyServerBootstrap("/vevnfmem/vnflcm/v1/vnf_instances/", new
         * NBIRestApplicationWrapper(vnfServiceMock, fileResourceService)); serverBootstrap.start();
         */
    }

    @AfterClass
    public static void tearDown() {
        //        serverBootstrap.stop();
    }

    @Test(timeout = 100000)
    public void testDeleteVnfInstance() {
        /*
         * final VnfInstanceUtil vnfInstanceUtil = new VnfInstanceUtil(); final VnfResponse vnfinstance =
         * vnfInstanceUtil.createVNFInstance(VNF_IDENTIFER); vnfinstance.setInstantiationState(InstantiatedState.NOT_INTANTIATED); try {
         * when(vnfServiceMock.deleteVnfId(any(String.class), any(VnfCreateDeleteOpConfig.class))).thenReturn(true); } catch (final
         * VNFLCMServiceException e1) { //ignored } catch (final Exception e) { //ignored }
         * expect().statusCode(Status.NO_CONTENT.getStatusCode()).given().when().delete("/vnflcm/v1/vnf_instances/{vnfInstanceId}", VNF_IDENTIFER)
         * .prettyPrint();
         */
    }

    @Test(timeout = 100000)
    public void testDeleteInternalServerError() {
        /*
         * final VNFLCMServiceException vnflcmServiceException = new VNFLCMServiceException("Error getting VNF Instance");
         * vnflcmServiceException.setType(ExceptionType.ERROR_FETCHING_VNF_INSTANCE); try { when(vnfServiceMock.deleteVnfId(any(String.class),
         * any(VnfCreateDeleteOpConfig.class))).thenThrow(vnflcmServiceException) .thenReturn(true); } catch (final VNFLCMServiceException e1) {
         * //ignored } catch (final Exception e) { //ignored }
         */

        /*
         * final String responseBody = expect().statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode()).given().when()
         * .delete("/vnflcm/v1/vnf_instances/{vnfInstanceId}", VNF_IDENTIFER).prettyPrint(); assertEquals(Status.INTERNAL_SERVER_ERROR.toString(),
         * from(responseBody).getString("title")); assertEquals(Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), from(responseBody).getString("type"));
         * assertEquals("Error getting VNF Instance", from(responseBody).getString("detail")); assertEquals("vnf123",
         * from(responseBody).getString("instance")); assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), from(responseBody).getInt("status"));
         */
    }
}
