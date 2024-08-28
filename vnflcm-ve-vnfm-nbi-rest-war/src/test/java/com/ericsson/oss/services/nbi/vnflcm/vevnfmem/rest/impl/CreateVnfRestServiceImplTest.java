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
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ResteasyServerBootstrap;
import com.ericsson.oss.services.nbi.vnflcm.vevnfmem.rest.impl.VeVnfmemRestResourceTest.VnfRestServiceWrapper;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfResponse;
import com.ericsson.oss.services.vnflcm.file.util.FileHandler;

/**
 * Test class for VNFDFRestResourceImpl
 */

@PrepareForTest({ VnfRestServiceWrapper.class, ConfigurationEnvironment.class, ServiceLoadingUtil.class, ServiceLoader.class, Resources.class,
        ServiceFinderBean.class, FileResourceService.class, FileHandler.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateVnfRestServiceImplTest {

    private static final String IDENTIFER = "1a9a13cb-bade-11e6-ac94-603320524153";
    public static final String ONBOARDED_PACKAGE_PATH = "vnf.hotpackage.onboarding.path";

    protected static final String ROOT_RESOURCE_PATH = "/vnflcm";
    private static ResteasyServerBootstrap serverBootstrap;
    private static VnfService vnfServiceMock;
    private static FileResourceService fileResourceService;
    private static String createVnfRequest = "{     \"vnfdId\":\"abc_32\",     \"additionalParams\":{         \"hotPackageName\":\"test\",         \"vnfSoftwareVersion\":\"abc\",         \"vnfProductName\":\"abc\",         \"onboardedVnfPkgInfoId\":\"abc\",         \"vnfdVersion\":\"abc\"     } }";

    //private static Resource resource;
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

    /**
     * To test instantiate VNF with JSON
     */
    /**
     * To test instantiate VNF with JSON
     */
    @Test(timeout = 100000)
    public void testCreateVnfWithJson() {
        /*
         * final VnfResponse vnfResponse = createVnfResponse(); final String vnfdWrapper =
         * "{\"dataVNFDSpecific\": {\"vnfdId\": \"vnflaf\",\"vnfdVersion\": \"2.2.28-SNAPSHOT\",\"vnfProvider\": \"Ericsson\",\"vnfProductName\": \"mme\",\"vnfSoftwareVersion\": \"16B\",\"flavourId\": [\"cee\"],\"vnfLcmOperationsConfiguration\": {\"scale\": {}},\"scalingAspect\":[{\"aspectId\":\"processing\" ,\"name\":\"processing\",\"description\":\"\",\"maxScaleLevel\":4},{\"aspectId\":\"database\" ,\"name\":\"database\",\"description\":\"\",\"maxScaleLevel\":15} ]},\"VnfConfigurableProperties\":{\"isAutoScaleEnabled\": \"true\",\"isAutoHealingEnabled\":\"true\"},\"instantiateVnfOpConfig\": {\"servicesImage\":\"ERICvnflafdevimage_CXP9032638-4.0.21.qcow2\",\"services_flavor\":\"m1.medium\",\"ip_version\":4,\"external_net_id\":\"491522c2-f023-4162-b097-55a9a71972f3\",\"external_subnet_cidr\":\"131.160.162.0/25\",\"external_subnet_gateway\":\"131.160.162.1\",\"external_ip_for_services_vm\":\"131.160.162.19\",\"cloudManagerType\":\"CEE\",\"ossType\":\"OSSRC\",\"ossMasterHostName\":\"masterservice\",\"ossMasterHostIP\":\"1.1.1.1\",\"ossNotificationServiceHost\":\"notificationservice\",\"ossNotificationServiceIP\":\"1.1.1.1\",\"ossUserName\":\"root\"},\"scaleVnfOpConfig\": {\"servicesImage\":\"ERICvnflafdevimage_CXP9032638-4.0.21.qcow2\",\"services_flavor\":\"m1.medium\",\"ip_version\":4,\"external_net_id\":\"491522c2-f023-4162-b097-55a9a71972f3\",\"external_subnet_cidr\":\"131.160.162.0/25\",\"external_subnet_gateway\":\"131.160.162.1\",\"external_ip_for_services_vm\":\"131.160.162.55\",\"external_ipv4_for_db_vm\": \"131.160.162.90\",\"internal_ipv4_for_db_vm\": \"172.16.100.9\",\"cloudManagerType\":\"CEE\",\"ossType\":\"OSSRC\",\"ossMasterHostName\":\"masterservice\",\"ossMasterHostIP\":\"1.1.1.1\",\"ossNotificationServiceHost\":\"notificationservice\",\"ossNotificationServiceIP\":\"1.1.1.1\",\"ossUserName\":\"root\"}}"
         * ; try { when(vnfServiceMock.createVnf(any(VnfCreation.class), any(VnfCreateDeleteOpConfig.class))).thenReturn(vnfResponse); } catch (final
         * VNFLCMServiceException e1) { e1.printStackTrace(); } try { PowerMockito.mockStatic(FileHandler.class);
         * PowerMockito.when(FileHandler.getVnfWrapperContent(Mockito.anyString())).thenReturn(vnfdWrapper); final String responseBody =
         * expect().statusCode(Status.CREATED.getStatusCode()).given().contentType("application/json")
         * .body(createVnfRequest).when().post(ROOT_RESOURCE_PATH + "/vnflcm/v1/vnf_instances").prettyPrint();
         * assertNotNull(from(responseBody).getString("id")); } catch (final Exception e) { e.printStackTrace(); }
         */
    }

    private VnfResponse createVnfResponse() {
        final VnfResponse vnfResp = new VnfResponse();
        vnfResp.setHotPackageName("test");
        vnfResp.setOnboardedPackageInfoId("abc");
        vnfResp.setVnfdId("abc_32");
        vnfResp.setVnfdVersion("abc");
        vnfResp.setVnfName("abc");
        vnfResp.setVnfSoftwareVersion("abc");
        vnfResp.setVnfId(IDENTIFER);
        return vnfResp;
    }
}