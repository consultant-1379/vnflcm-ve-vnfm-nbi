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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.RequestValidator;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InterfaceInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VcdOnboardPackageRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimConnectionInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiVcdPackageOnboardRequest;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VimService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.VcdOnboardPackageOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VcdPackageOnboardRequest;
import com.ericsson.oss.services.vnflcm.api_base.model.Vim;
import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OvfPkgOnboardImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private VnfService vnfService;

    private VimService vimService;

    public OvfPkgOnboardImpl(final VnfService vnfService,final VimService vimService) {
        this.vnfService = vnfService;
        this.vimService = vimService;
    }

    public OvfPkgOnboardImpl() {
    }

    public Response onboardPackageToVcd(final NbiVcdPackageOnboardRequest vcdOnboardPackageRequest) {
        logger.info("VcdOnboardRestServiceSol002V241Impl: onboardPackageToVcd(): Start...");
        final RequestValidator<VcdOnboardPackageRequest> reqValidationHelper = new RequestValidator<VcdOnboardPackageRequest>();
        reqValidationHelper.validate(vcdOnboardPackageRequest);
        final File ovfPath = new File(vcdOnboardPackageRequest.getOvfDirectory());
        if (!ovfPath.exists()) {
            final String problemDetail = new String("OVF directory provided was not found.");
            logger.info(problemDetail);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        final String wfInstanceID;
        try {
            final VcdPackageOnboardRequest vcdPkgOnboardReq = this.createonboardRequest(vcdOnboardPackageRequest);
            final VcdOnboardPackageOpConfig onboardOpConfig = new VcdOnboardPackageOpConfig(); //kept for future requirements. Not setting anything for now.
            wfInstanceID = vnfService.onboardOvfPkgToVcd(vcdPkgOnboardReq, onboardOpConfig);
            logger.info("Workflow successfully started with ID: {} to inboard package to VCD VIM", wfInstanceID);

        } catch (final VNFLCMServiceException ex) {
            final String message = ex.getMessage();
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", message, "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        } catch (final Exception ex) {
            final String message = ex.getMessage();
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", message, "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        return Response.status(Status.ACCEPTED).header("Onboarding WorkflowInstanceId", wfInstanceID).build();
    }

    private VcdPackageOnboardRequest createonboardRequest(final NbiVcdPackageOnboardRequest onboardRequest) throws VNFLCMServiceException {
        final VcdPackageOnboardRequest packageOnboardreq = new VcdPackageOnboardRequest();
        packageOnboardreq.setCatalogName(onboardRequest.getCatalogName());
        packageOnboardreq.setOvfDirectory(onboardRequest.getOvfDirectory());
        packageOnboardreq.setOvfPackage(onboardRequest.getOvfPackage());
        packageOnboardreq.setvAppTemplateDescription(onboardRequest.getvAppTemplateDescription());
        packageOnboardreq.setvAppTemplateName(onboardRequest.getvAppTemplateName());

        //added to re-use existing code
        final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        if (onboardRequest.getVimConnectionInfo() != null
                && !onboardRequest.getVimConnectionInfo().isEmpty()) {
            // Consider the vimConnection passed in the request
            List<VimConnectionInfo> vimConnectionInfos = vnfRestServiceSol241Impl.extractVimConnectionInfoFromRequest(onboardRequest.getVimConnectionInfo());
            packageOnboardreq.setVimConnectionInfo(this.serializeObjectTojson(vimConnectionInfos));
        } else {
            // set default vim information
            final List<Vim> vims = vimService.getVims();
            logger.debug("Vims return from Service::" + vims);
            final List<VimConnectionInfo> vimConnectionInfor = new ArrayList<VimConnectionInfo>();
            if (vims != null && !vims.isEmpty()) {
                vnfRestServiceSol241Impl.getDefaultVimConnectionInfo(vims, vimConnectionInfor);
                logger.debug("Vims added in list:{}", vimConnectionInfor);
                if (vimConnectionInfor.size() == 0) {
                    final VNFLCMServiceException ex = new VNFLCMServiceException("Default VIM is not configured in the system");
                    throw ex;
                } else if (!vimConnectionInfor.get(0).getVimType().contains("VCD")) {
                    final VNFLCMServiceException ex = new VNFLCMServiceException("Default VIM is not of type VCD");
                    throw ex;
                }
                packageOnboardreq.setVimConnectionInfo(this.serializeObjectTojson(vimConnectionInfor));
            } else {
                final VNFLCMServiceException ex = new VNFLCMServiceException("Default VIM is not configured in the system");
                throw ex;
            }
        }

        return packageOnboardreq;
    }

    private String serializeObjectTojson(final Object object) {
        String value = null;
        if (object != null) {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);

            try {
                value = mapper.writer().writeValueAsString(object);
            } catch (final Exception e) {
                logger.error("Erro while serializing " + e.getMessage());

            }
        }
        return value;
    }
}
