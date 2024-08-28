package com.ericsson.oss.services.nbi.vnflcm.vevnfmem.rest.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl.VnfPackageRestServiceImpl;
import com.ericsson.oss.services.vnflcm.api_base.VnfPackageService;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfPackage;

@PrepareForTest({VnfPackageService.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VnfPackageRestServiceImplTest {

    @Mock
    private static VnfPackageService vnfPackageServiceMock;

    private static UriInfo uriInfo = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        vnfPackageServiceMock = Mockito.mock(VnfPackageService.class);
        uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vevnfmem/vnflcm/v1/vnf_instances/"));
    }

    @Test
    public void testGetpackages() {
        final List<VnfPackage> packages = new ArrayList<>();

        final VnfPackage p1 = new VnfPackage();
        p1.setVnfdId("vnflaf");
        p1.setVnfdVersion("1.0");
        p1.setVnfProductName("VNFLAF");
        p1.setVnfSoftwareVersion("1.2.3");

        final VnfPackage p2 = new VnfPackage();
        p2.setVnfdId("vnflaf");
        p2.setVnfdVersion("1.0");
        p2.setVnfProductName("VNFLAF");
        p2.setVnfSoftwareVersion("1.2.3");

        packages.add(p1);
        packages.add(p2);

        try {
            when(vnfPackageServiceMock.getAllVnfPackagesInfo(any())).thenReturn(packages);
            final VnfPackageRestServiceImpl packageServiceImpl = new VnfPackageRestServiceImpl();
            packageServiceImpl.setUriInfo(uriInfo);
            packageServiceImpl.setVnfPackageService(vnfPackageServiceMock);
            final Response response = packageServiceImpl.getVnfPackagesInfo();

            assertEquals(Status.OK.getStatusCode(), response.getStatus());
            assertEquals(packages, response.getEntity());
        } catch (Exception e) {
            fail("VnfPackageRestServiceImplTest: testGetpackages(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testGeEmptytPackagesList() {
        final List<VnfPackage> packages = new ArrayList<>();
        try {
            when(vnfPackageServiceMock.getAllVnfPackagesInfo(any())).thenReturn(packages);
            final VnfPackageRestServiceImpl packageServiceImpl = new VnfPackageRestServiceImpl();
            packageServiceImpl.setUriInfo(uriInfo);
            packageServiceImpl.setVnfPackageService(vnfPackageServiceMock);
            final Response response = packageServiceImpl.getVnfPackagesInfo();

            assertEquals(Status.OK.getStatusCode(), response.getStatus());
            assertEquals(Collections.EMPTY_LIST, response.getEntity());
        } catch (Exception e) {
            fail("VnfPackageRestServiceImplTest: testGeEmptytPackagesList(): Exception occured " + e.getMessage());
        }
    }
}
