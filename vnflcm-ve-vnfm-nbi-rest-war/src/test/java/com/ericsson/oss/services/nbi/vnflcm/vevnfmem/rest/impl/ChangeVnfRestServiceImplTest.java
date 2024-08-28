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
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ResteasyServerBootstrap;
import com.ericsson.oss.services.nbi.vnflcm.vevnfmem.rest.impl.VeVnfmemRestResourceTest.VnfRestServiceWrapper;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.*;

/**
 * Test class for Change VNF
 */

@PrepareForTest({ VnfRestServiceWrapper.class, ConfigurationEnvironment.class, ServiceLoadingUtil.class, ServiceLoader.class, Resources.class,
        ServiceFinderBean.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChangeVnfRestServiceImplTest {

    private static final String ROOT_RESOURCE_PATH = "/vevnfmem";
    private static ResteasyServerBootstrap serverBootstrap;
    private static FileResourceService fileResourceService;
    private static VnfService vnfServiceMock;
    private static VnfResponse vnfResponseMock;
    private static ContainerizedVnf containerizedVnfMock;
    private static String changeVnfResponse = "http://localhost:58586/vevnfmem/vnflcm/v1/vnf_lcm_op_occs/123";
    private static String changeVnfRequest = "{\"vnfdId\":\"abc_32\",\"additionalParams\":{\"hotPackageName\":\"test\",\"vnfSoftwareVersion\":\"abc\",\"vnfProductName\":\"abc\",\"onboardedVnfPkgInfoId\":\"abc\",\"vnfdVersion\":\"abc\"}}";

    @BeforeClass
    public static void setupRestEasy() throws VNFLCMServiceException {
        vnfServiceMock = Mockito.mock(VnfService.class);
        vnfResponseMock = Mockito.mock(VnfResponse.class);
        containerizedVnfMock = Mockito.mock(ContainerizedVnf.class);
        fileResourceService = Mockito.mock(FileResourceService.class);
        when(fileResourceService.exists(any(String.class))).thenReturn(true);
        when(vnfServiceMock.queryVnf(any(String.class))).thenReturn(vnfResponseMock);
        when(vnfResponseMock.getContainerizedVnf()).thenReturn(null);
    }

    @AfterClass
    public static void tearDown() {
        //        serverBootstrap.stop();
    }

    /**
     * To test Change VNF with JSON
     */
    @Test(timeout = 100000)
    public void testChangeVnf() {
        /*
         * try { when(vnfServiceMock.changeCurrentVnfPackage(any(ChangeCurrentVNFPackageDto.class), any(ChangeCurrentVnfPackageOpConfig.class)))
         * .thenReturn("123"); } catch (final VNFLCMServiceException e1) { assertNull(e1); } try { final String responseBody =
         * expect().statusCode(Status.ACCEPTED.getStatusCode()).given().contentType("application/json")
         * .body(changeVnfRequest).post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/vnfInstanceId/change_vnfpkg").getHeader("Location");
         * assertNotNull(from(responseBody)); assertEquals(changeVnfResponse, responseBody); } catch (final Exception e) { assertNull(e); }
         */
    }

    /*
    *//**
        * To test Change VNF when no VNF directory
        *
        * @throws Exception
        */

    @Test(timeout = 100000)
    public void testChangeVnfNoDirectoryExist() throws Exception {
        /*
         * try { when(fileResourceService.exists(any(String.class))).thenReturn(false);
         * when(vnfServiceMock.changeCurrentVnfPackage(any(ChangeCurrentVNFPackageDto.class), any(ChangeCurrentVnfPackageOpConfig.class)))
         * .thenReturn("123");
         *
         * } catch (final VNFLCMServiceException e1) { assertNull(e1); } try { final String responseBody =
         * expect().statusCode(Status.NOT_FOUND.getStatusCode()).given().contentType("application/json")
         * .body(changeVnfRequest).post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/vnfInstanceId/change_vnfpkg").getBody().asString(); final
         * ObjectMapper mapper = new ObjectMapper(); final VeVnfmemProblemDetail problemDetail = mapper.readValue(responseBody,
         * VeVnfmemProblemDetail.class); assertEquals(String.format(VnfRestServiceImpl.VNFD_DIRECTORY_ERROR_MESSAGE, "abc_32"),
         * problemDetail.getDetail()); assertEquals(problemDetail.getStatus(), 400); } catch (final Exception e) { assertNull(e); }
         */
    }

    /**
     * To test Change VNF for 404(Not Found)
     */

    @Test(timeout = 100000)
    public void testChangeVnfNotFoundWithJson() {
        /*
         * try { when(vnfServiceMock.changeCurrentVnfPackage(any(ChangeCurrentVNFPackageDto.class), any(ChangeCurrentVnfPackageOpConfig.class)))
         * .thenReturn("123"); } catch (final VNFLCMServiceException e1) { assertNull(e1); } try { final int responseBody =
         * expect().statusCode(Status.NOT_FOUND.getStatusCode()).given().contentType("application/json")
         * .body(changeVnfRequest).post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/vnfInstanceId/change_package_inf").getStatusCode();
         * assertEquals(404, responseBody); } catch (final Exception e) { assertNull(e); }
         */
    }

    /**
     * To test Change VNF for 500(Internal Server Error)
     */
    @Test(timeout = 10000)
    public void testChangeVnfServerErrorWithJson() throws InterruptedException {
        /*
         * final VNFLCMServiceException vnflcmServiceException = new VNFLCMServiceException("Error getting VNF Instance");
         * vnflcmServiceException.setType(ExceptionType.ERROR_FETCHING_VNF_INSTANCE); try {
         * when(vnfResponseMock.getContainerizedVnf()).thenReturn(containerizedVnfMock);
         * when(vnfServiceMock.changeCurrentVnfPackage(any(ChangeCurrentVNFPackageDto.class), any(ChangeCurrentVnfPackageOpConfig.class)))
         * .thenThrow(vnflcmServiceException); } catch (final VNFLCMServiceException e1) { assertNotNull(e1); } catch (final Exception e) {
         * assertNull(e); }
         *
         * final String responseBody =
         * expect().statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode()).given().when().contentType("application/json")
         * .body(changeVnfRequest).post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances/vnf123/change_vnfpkg").asString();
         * assertEquals("Internal Server Error", from(responseBody).getString("title")); assertEquals("Internal Server Error",
         * from(responseBody).getString("type")); assertEquals("Error getting VNF Instance", from(responseBody).getString("detail"));
         * assertEquals("vnf123", from(responseBody).getString("instance")); assertEquals(500, from(responseBody).getInt("status"));
         */
    }

}
