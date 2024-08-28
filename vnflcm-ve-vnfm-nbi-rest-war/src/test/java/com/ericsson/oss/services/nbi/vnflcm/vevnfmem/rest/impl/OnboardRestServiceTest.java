/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

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

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.OnboardRestService;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl.OnboardRestServiceImpl;
import com.ericsson.oss.services.vnflcm.api_base.VimService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;

@PrepareForTest({OnboardRestService.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OnboardRestServiceTest {

    private static UriInfo uriInfo = null;

    @Mock
    private static VnfService vnfServiceMock;

    @Mock
    private static VimService vimServiceMock;

//    @Mock
//    private static NfvoConfigHelper nfvoConfigHelper;

    @Mock
    private static OnboardRestServiceImpl onboardSvcImpl;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        vnfServiceMock = Mockito.mock(VnfService.class);
        vimServiceMock = Mockito.mock(VimService.class);
        uriInfo = new ResteasyUriInfo(new URI("localhost:8080"), new URI("/vevnfmem/vcd/pkgOnboard"));
        when(vnfServiceMock.onboardOvfPkgToVcd(any(), any())).thenReturn("f7d51efd-0637-11ec-9191-fa163eac580e");
        onboardSvcImpl = new OnboardRestServiceImpl(vnfServiceMock,vimServiceMock);
    }

    @Test
    public void testInvalidOvfDir() {
        try {
            final String reqPayload = "{ \"vAppTemplateName\": \"template\","
                    + " \"vAppTemplateDescription\": \"Dummy template description\", \"catalogName\": \"catalog\", \"ovfPackage\": \"package\","
                    + " \"ovfDirectory\": \"/a/b/c\","
                    + " \"vimConnectionInfo\": [{ \"id\": \"4408b119-eb54-11e7-bae0-fa163eb90b5c\", \"vimId\": \"vim1\","
                    + " \"vimType\": \"CEE\",\"interfaceInfo\": { \"identityEndPoint\": \"https://ieatcee01cic.athtem.eei.ericsson.se:5000/v2.0\" },"
                    + "\"accessInfo\": { \"projectId\": \"cab32f669c18404d8bed0fae6bf088aa\","
                    + "\"credentials\": { \"username\": \"enZpY2NoYQ==\", \"password\": \"IzI3MTJAVmNAMjcxMiM=\" } } }] }";

            final Response response = makeonboardingCall(reqPayload);
            assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        } catch (final Exception e) {
            fail("OnboardRestServiceTest: testInvalidOvfDir(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testInvalidInput() {
        try {
            final String reqPayload = "{ \"vAppTemplateName\": \"template\","
                    + " \"vAppTemplateDescription\": \"Dummy template description\", \"catalogName\": \"catalog\", \"ovfPackage\": \"package\","
                    + " \"ovfDirectory\": \"/a/b/c\","
                    + " \"vimConnectionInfo\": { \"id\": \"4408b119-eb54-11e7-bae0-fa163eb90b5c\", \"vimId\": \"vim1\","
                    + " \"vimType\": \"CEE\",\"interfaceInfo\": { \"identityEndPoint\": \"https://ieatcee01cic.athtem.eei.ericsson.se:5000/v2.0\" },"
                    + "\"accessInfo\": { \"projectId\": \"cab32f669c18404d8bed0fae6bf088aa\","
                    + "\"credentials\": { \"username\": \"enZpY2NoYQ==\", \"password\": \"IzI3MTJAVmNAMjcxMiM=\" } } } }";

            final Response response = makeonboardingCall(reqPayload);
            assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        } catch (final Exception e) {
            fail("OnboardRestServiceTest: testInvalidOvfDir(): Exception occured " + e.getMessage());
        }
    }

    @Test
    public void testVimRegistrationFailureForOvfOnboard() {
        Response response = null;
        try {
            final String reqPayload = "{ \"vAppTemplateName\": \"template\","
                    + " \"vAppTemplateDescription\": \"Dummy template description\", \"catalogName\": \"catalog\", \"ovfPackage\": \"package\","
                    + " \"ovfDirectory\": \"src/test/java\","
                    + " \"vimConnectionInfo\": [{ \"id\": \"4408b119-eb54-11e7-bae0-fa163eb90b5c\", \"vimId\": \"vim1\","
                    + " \"vimType\": \"CEE\",\"interfaceInfo\": { \"identityEndPoint\": \"https://ieatcee01cic.athtem.eei.ericsson.se:5000/v2.0\" },"
                    + "\"accessInfo\": { \"projectId\": \"cab32f669c18404d8bed0fae6bf088aa\","
                    + "\"credentials\": { \"username\": \"enZpY2NoYQ==\", \"password\": \"IzI3MTJAVmNAMjcxMiM=\" } } }] }";

            response = makeonboardingCall(reqPayload);
        } catch (final Exception e) {
            assertTrue(Status.INTERNAL_SERVER_ERROR.getStatusCode() == response.getStatus());
            assertTrue(e.getMessage().contains("Auto registration of VIM is not possible"));
        }
    }

    private Response makeonboardingCall(final String payload) {
        final InputStream stream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8));
        //onboardSvcImpl = new OnboardRestServiceImpl(vnfServiceMock, nfvoConfigHelper);
        return onboardSvcImpl.pkgOnboard(stream, uriInfo);
    }
}
