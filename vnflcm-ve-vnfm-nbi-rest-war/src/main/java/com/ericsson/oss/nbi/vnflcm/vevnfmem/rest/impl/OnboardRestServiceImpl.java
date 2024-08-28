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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.NfvoConfigHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.OnboardRestService;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.OvfPkgOnboardImpl;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VimConnectionHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VcdOnboardPackageRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiVcdPackageOnboardRequest;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.vnflcm.api_base.VimService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OnboardRestServiceImpl implements OnboardRestService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @EServiceRef
    private VnfService vnfService;

    @EServiceRef
    private VimService vimService;

    @Override
    public Response pkgOnboard(InputStream inputStream, UriInfo uriInfo) {

        logger.info("OnboardRestServiceImpl : pkgOnboard() : Start.");
        String incomingRequestData = null;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            logger.debug("OnboardRestServiceImpl : pkgOnboard() : request read from stream: {}", Utils.getMaskedString(incomingRequestData));
        } catch (final Exception ex) {
            logger.error("OnboardRestServiceImpl : pkgOnboard() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        Response response = null;
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final VcdOnboardPackageRequest onboardRequest= objectMapper.readValue(incomingRequestData, VcdOnboardPackageRequest.class);
            final NbiVcdPackageOnboardRequest nbiVcdonoardReq = VimConnectionHelper.getVcdPackageOnboardRequest(onboardRequest);

            final OvfPkgOnboardImpl ovfOnboardImpl = new OvfPkgOnboardImpl(vnfService, vimService);
            response = ovfOnboardImpl.onboardPackageToVcd(nbiVcdonoardReq);
        } catch (final IOException e) {
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        return response;
    }

    private String readIncomingRequest(final InputStream inputStream) throws Exception {
        String incomingRequestData = null;

        logger.info("VnfRestServiceImpl : createVnf() : start reading raw request data from stream.");

        try (final ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            incomingRequestData = result.toString();
        } catch (final Exception e) {
            logger.error("VnfRestServiceImpl : createVnf() : error reading request from stream: {}", e.getMessage());
            throw e;
        }
        return incomingRequestData;
    }

    /**
     * @param vnfService
     * @param nfvoConfigHelper
     */
    public OnboardRestServiceImpl(VnfService vnfService,VimService vimService) {
        super();
        this.vnfService = vnfService;
        this.vimService = vimService;
    }

    public OnboardRestServiceImpl() {
    }
}
