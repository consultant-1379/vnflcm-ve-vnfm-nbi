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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.ericsson.oss.itpf.sdk.config.ConfigurationEnvironment;
import com.ericsson.oss.itpf.sdk.config.classic.ConfigurationEnvironmentBean;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.NfvoConfigHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NfvoConfig;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfRestService;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VeVnfmemSol002V241ModelGenerator;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VnfRestServiceSol002V241Impl;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ChangeCurrentVnfPkgRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.LcmOperationType;
import com.ericsson.oss.services.vnflcm.api_base.RequestProcessingDetailsService;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiChangeVnfRequest;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.VnflcmHttpServletContextListener;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VimService;
import com.ericsson.oss.services.vnflcm.api_base.VnfDescriptorService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.ContainerizedVnf;
import com.ericsson.oss.services.vnflcm.api_base.dto.InstantiatedVnfInfo;
import com.ericsson.oss.services.vnflcm.api_base.dto.QueryOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfInstantiateOpProcessingDetails;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfResponse;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfScaleOpProcessingDetails;
import com.ericsson.oss.services.vnflcm.api_base.model.CreateVnfResponseDetails;
import com.ericsson.oss.services.vnflcm.api_base.model.RequestProcessingDetails;
import com.ericsson.oss.services.vnflcm.common.dataTypes.InstantiatedState;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OrVnfmVersion;
import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;
import com.ericsson.oss.services.vnflcm.file.util.ToscaArtifactsParser;
import com.ericsson.oss.services.vnflcm.file.util.ToscaTemplateParser;
import com.ericsson.oss.services.vnflcm.workflowbundle.api.WorkflowBundleDescriptorService;
import com.ericsson.oss.services.vnflcm.workflowbundle.dto.WorkflowDefinitions;
import com.ericsson.oss.services.vnflcmwfs.api.LcmWorkflowAuthenticationService;
import com.ericsson.oss.services.vnflcmwfs.api.auth.model.JwtPayloadResponseDto;
import com.ericsson.oss.services.vnflcmwfs.api.exceptions.LcmWorkflowServiceException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.prometheus.client.Summary;
import io.prometheus.client.Summary.Timer;

public class VnfRestServiceImpl implements VnfRestService {

    private static final String NODE_POOLS = "node_pools";
    private static final String DATA_WORKER_NODES_SPECIFIC = "dataWorkerNodesSpecific";
    private static final String INS_REQ_ADDITIONAL_PARAMS = "additionalParams";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @EServiceRef
    private VnfService vnfService;

    @EServiceRef
    private VimService vimService;

    @EServiceRef
    private WorkflowBundleDescriptorService wfbundleDesService;

    @EServiceRef
    private LcmWorkflowAuthenticationService lcmAuthenticationService;

    @EServiceRef
    private RequestProcessingDetailsService requestProcessingDetailsService;

    @EServiceRef
    private VnfDescriptorService vnfDescriptorService;
    @EServiceRef
    private FileResourceService fileResourceService;
    @Context
    private UriInfo uriInfo;
    private boolean isUnitTesting = false;
    public static final String VNF_INSTANCE_ID_NULL = "VNF Instance ID cannot be empty";
    public static final String NUMBER_OF_STEPS_NEGATIVE = "Number of Steps cannot be negative";
    public static final String VNF_DESCRIPTOR_BASE_DIR = "/vnflcm-ext/current/vnf_package_repo";
    public static final String VNFD_DIRECTORY_ERROR_MESSAGE = "Vnfd directory for vnfdId %s does not exists.";

    private List<String> sensitiveParamsList = new ArrayList<String>();

    public VnfRestServiceImpl() {
        // TODO Auto-generated constructor stub
    }

    public VnfRestServiceImpl(VnfService vnfService, LcmWorkflowAuthenticationService lcmwfsAuthenticationService, VimService vimService) {
        this.vnfService = vnfService;
        this.lcmAuthenticationService = lcmwfsAuthenticationService;
        this.vimService = vimService;
    }



    @Override
    public Response createVnf(final InputStream inputStream, @Context final UriInfo uriInfo, @Context HttpHeaders headers) {
        logger.info("VnfRestServiceImpl : createVnf() : Start.");
        // start the request latency metric timer
        Summary.Timer createVnfRequestTimer = null;
        final String requestId = getRequestId(headers);
        logger.info("Request ID header: {} ", requestId);
        final String jwtToken = getJwtToken(headers);
        logger.info("{} user has started the create identifier operation ", getUsername(jwtToken));
        try {
            createVnfRequestTimer = VnflcmHttpServletContextListener.getCreateVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : createVnf() : error in intializing metrics collection : {}", ex.toString());
        }
        Response response = null;
     // Check if this request is a duplicate request (i.e., retried by Service Mesh) if yes processs it and return response
        if (!StringUtils.isEmpty(requestId)) {
            response = processCreateVnfIfRetried(inputStream, uriInfo, getUsername(jwtToken), requestId);
            if (response != null) {
                recordRequestDuration(createVnfRequestTimer);
                return response;
            }
        }
        //End
        String incomingRequestData = null;
        //Licence Check
        response=getResponseForLicenceCheck(LcmOperationType.CREATE_IDENTIFIER,false, createVnfRequestTimer);
        if (response!=null)
            return response;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            logger.debug("VnfRestServiceImpl : createVnf() : request read from stream: {}", incomingRequestData);
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : createVnf() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                createVnfRequestTimer.observeDuration();
            } catch (final Exception metricException) {
                logger.error("VnfRestServiceImpl : createVnf() : error in collecting request duration : {}", metricException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            logger.info("VnfRestServiceImpl: CreateVnf() : Reading the nfvo configuration.");
            NfvoConfig nfvoConfigData = null;
            try {
                final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
                nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
            } catch (final NfvoCallException exception) {
                logger.warn("VnfRestServiceImpl: CreateVnf(), nfvo configuration not found. {}", exception.getMessage());
            }
            logger.info("VnfRestServiceImpl : createVnf() : start creating models from incoming request.");
            final String solVersionSupported = OrVnfmVersion.SOL003V241.value();// Default case, if not provided in nfvoConfig
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.CreateVnfRequest createVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.CreateVnfRequest.class);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vnfDescriptorService,
                        vimService, lcmAuthenticationService, requestProcessingDetailsService);
                response = vnfRestServiceSol241Impl.createVnf(createVnfRequest, uriInfo, nfvoConfigData,jwtToken, requestId);

                logger.info("VnfRestServiceImpl : createVnf() : model created.");
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final IOException e) {
            logger.error("VnfRestServiceImpl : createVnf() : error creating model: {}", e.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                createVnfRequestTimer.observeDuration();
            } catch (final Exception metricException) {
                logger.error("VnfRestServiceImpl : createVnf() : error in collecting request duration : {}", metricException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        try {
            createVnfRequestTimer.observeDuration();
        } catch (final Exception metricException) {
            logger.error("VnfRestServiceImpl : createVnf() : error in collecting request duration : {}", metricException.toString());
        }
        return response;
    }

    private String getRequestId(final HttpHeaders headers) {
        String requestId = null;
        try {
            requestId = headers.getHeaderString("x-request-id");
        } catch (Exception e) {
            logger.error("Exception while fetching request id from header: {}", e.getMessage());
        }
        return requestId;
    }

    private String getJwtToken(final HttpHeaders headers) {
        String jwtToken = null;
        final List<String> authorizationHeader = headers.getRequestHeader("Authorization");
        if (authorizationHeader!=null && !authorizationHeader.isEmpty() && authorizationHeader.get(0)!= null) {
           jwtToken = authorizationHeader.get(0);
        }
        return jwtToken;
    }

    private String getUsername(final String jwtToken) {
        String username = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String payload = jwtToken.split("\\.")[1];
            final byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            final JwtPayloadResponseDto jwtResponse =  mapper.readValue(decodedBytes, JwtPayloadResponseDto.class);
            username = jwtResponse.getPreferredUsername();
        } catch (final Exception e) {
            logger.error("Exception while decoding username from jwt: {}", e.getMessage());
        }
        return username;
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

    @Override
    public Response deleteVnfId(final String vnfInstanceId, @Context HttpHeaders headers) {
        Response response = null;
        Summary.Timer deleteVnfIdentifierRequestTimer = null;
        final String jwtToken = getJwtToken(headers);
        logger.info("{} user has started the delete identifier operation on lcm operation id {}", getUsername(jwtToken), vnfInstanceId);
        try {
            deleteVnfIdentifierRequestTimer = VnflcmHttpServletContextListener.getDeleteVnfIdentifierRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : deleteVnfId() : error in intializing metrics collection : {}", ex.toString());
        }
        //Licence Check
        response=getResponseForLicenceCheck(LcmOperationType.DELETE_IDENTIFIER, false, deleteVnfIdentifierRequestTimer);
        if (response!=null)
            return response;
        //DRAC Check
        response = getResponseForDomainRoleAccessCheck(vnfInstanceId, headers, deleteVnfIdentifierRequestTimer);
        if (response!=null)
            return response;
        final String solVersionSupported = getSolVersionSupported();
        if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
            final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
            response = vnfRestServiceSol241Impl.deleteVnfId(vnfInstanceId);
        } else {
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                    Constants.ACCEPTED);
            response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
        }
        try {
            deleteVnfIdentifierRequestTimer.observeDuration();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : deleteVnfId() : error in collecting request duration : {}", ex.toString());
        }
        return response;
    }

    private String getSolVersionSupported() {
        logger.info("VnfRestServiceImpl: getSolVersionSupported() : Reading the nfvo configuration.");
        String solVersionSupported = OrVnfmVersion.SOL003V241.value(); // Default case, if not provided in nfvoConfig or unit test
        if (isUnitTesting()) { return OrVnfmVersion.SOL003V241.value(); }
        NfvoConfig nfvoConfigData = null;
        try {
            final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
            nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
        } catch (final NfvoCallException exception) {
            // Do not throw exception if nfvo not found, use default SOL version.
            logger.warn("VnfRestServiceImpl: getSolVersionSupported(), nfvo configuration not found. {}", exception.getMessage());
        }
        if (nfvoConfigData != null && nfvoConfigData.getOrVnfmVersion() != null && !nfvoConfigData.getOrVnfmVersion().isEmpty()) {
            solVersionSupported = nfvoConfigData.getOrVnfmVersion();
        }
        return solVersionSupported;
    }

    @Override
    public Response modifyVnf(final String vnfInstanceId, final InputStream inputStream, @Context final UriInfo uriInfo, @Context HttpHeaders headers) {
        Summary.Timer modifyVnfRequestTimer = null;
        try {
            modifyVnfRequestTimer = VnflcmHttpServletContextListener.getModifyVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : modifyVnf() : error in intializing metrics collection : {}", ex.toString());
        }
        logger.info("VnfRestServiceImpl : modifyVnf(): Starting modifyVnfInfo");
        Response response = null;
        //UsecaseConflict Check
        response = getResponseForUsecaseConflictException(vnfInstanceId, LcmOperationType.MODIFY_INFO, modifyVnfRequestTimer);
        if(response != null) {
            return response;
        }
        //Licence Check
        response=getResponseForLicenceCheck(LcmOperationType.MODIFY_INFO, false, modifyVnfRequestTimer);
        if (response!=null)
            return response;
        //DRAC Check
        response = getResponseForDomainRoleAccessCheck(vnfInstanceId, headers, modifyVnfRequestTimer);
        if (response!=null)
            return response;

        String incomingRequestData = null;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            logger.debug("VnfRestServiceImpl : modifyVnf() : request read from stream: {}", incomingRequestData);
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : modifyVnf() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                modifyVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : modifyVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("VnfRestServiceImpl: ModifyVnf() : Reading the nfvo configuration.");
            NfvoConfig nfvoConfigData = null;
            try {
            final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
               nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
            }catch(final NfvoCallException exception) {
               logger.warn("VnfRestServiceImpl: ModifyVnf(), nfvo configuration not found. {}",exception.getMessage());
            }
            logger.info("VnfRestServiceImpl : ModifyVnf() : start creating models from incoming request.");
            String solVersionSupported = OrVnfmVersion.SOL003V241.value();// Default case, if not provided in nfvoConfig
            if(nfvoConfigData != null && nfvoConfigData.getOrVnfmVersion() != null && !nfvoConfigData.getOrVnfmVersion().isEmpty()) {
               solVersionSupported = nfvoConfigData.getOrVnfmVersion();
            }
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInfoModificationRequest modifyVnfInfo = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInfoModificationRequest.class);
                if (vnfInstanceId == null || "".equals(vnfInstanceId.trim()) || modifyVnfInfo == null) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), VNF_INSTANCE_ID_NULL,
                            "", "", Status.BAD_REQUEST.getStatusCode());
                    response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                } else {
                    final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
                    response = vnfRestServiceSol241Impl.modifyVnf(vnfInstanceId, modifyVnfInfo, uriInfo, nfvoConfigData);
                }
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final IOException e) {
            logger.error("VnfRestServiceImpl : modifyVnf() : error creating model: {}", e.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                modifyVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : modifyVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        try {
            modifyVnfRequestTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : modifyVnf() : error in collecting request duration : {}", metricsException.toString());
        }
        return response;
    }

    @Override
    public Response changeVnfRequest(final String vnfInstanceId, final ChangeCurrentVnfPkgRequest changeCurrentVnfRequest,
                                     @Context final UriInfo uriInfo, @Context HttpHeaders headers) {
        MDC.put("vnfInstanceId", vnfInstanceId);
        Summary.Timer changeVnfRequestTimer = null;
        final String jwtToken = getJwtToken(headers);
        final String userName = getUsername(jwtToken);
        logger.info("{} user has started the change vnf operation on lcm operation id {}", userName, vnfInstanceId);
        try {
            changeVnfRequestTimer = VnflcmHttpServletContextListener.getChangeVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : changeVnfRequest() : error in intializing metrics collection : {}", ex.toString());
        }
        MDC.clear();
        logger.info("VnfRestServiceImpl : changeVnfRequest() : Starting changeVnfInfo" + vnfInstanceId);
        Response response = null;
        //UsecaseConflict Check
        response = getResponseForUsecaseConflictException(vnfInstanceId, LcmOperationType.CHANGE_VNFPKG, changeVnfRequestTimer);
        if(response != null) {
            return response;
        }
        //Licence Check
        VnfResponse vnfResponse;
        boolean isENMIntegrationRequired = false;
        try {
            vnfResponse = vnfService.queryVnf(vnfInstanceId);
            if (vnfResponse !=null && vnfResponse.getInstantiatedVnfInfo()!=null && vnfResponse.getInstantiatedVnfInfo().getFdn()!=null && !vnfResponse.getInstantiatedVnfInfo().getFdn().isEmpty() ) {
                isENMIntegrationRequired= true;
            }
        } catch (VNFLCMServiceException ex) {
            logger.error("VnfRestServiceImpl : terminateVnfInstance() : error while finding vnf with given vnfInstance Id", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                changeVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : terminateVnfInstance() : error in collecting request duration : {}",
                        metricsException.toString());
            }
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        response=getResponseForLicenceCheck(LcmOperationType.CHANGE_VNFPKG, isENMIntegrationRequired, changeVnfRequestTimer);
        if (response!=null)
            return response;
        //DRAC Check
        response = getResponseForDomainRoleAccessCheck(vnfInstanceId, headers, changeVnfRequestTimer);
        if (response!=null)
            return response;

        UriBuilder builder;
        final VnfRestServiceSol002V241Impl restService = new VnfRestServiceSol002V241Impl(vnfService, vimService);
        try {
            if (vnfInstanceId == null || "".equals(vnfInstanceId.trim()) || changeCurrentVnfRequest == null) {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), VNF_INSTANCE_ID_NULL,
                        "", "", Status.BAD_REQUEST.getStatusCode());
                response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
            } else {
                final ContainerizedVnf contanerizedVnfId = vnfService.queryVnf(vnfInstanceId).getContainerizedVnf();
                if (contanerizedVnfId == null) {
                    final boolean isVnfdIdPresent = fileResourceService
                            .exists(VNF_DESCRIPTOR_BASE_DIR + "/" + changeCurrentVnfRequest.getVnfdId());
                    if (!isVnfdIdPresent) {
                        final boolean pkgDownloadStatus = restService.packageOnboarding(changeCurrentVnfRequest.getVnfdId());
                        if(!pkgDownloadStatus) {
                            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(),
                                    String.format(VNFD_DIRECTORY_ERROR_MESSAGE, changeCurrentVnfRequest.getVnfdId()), "", "",
                                    Status.BAD_REQUEST.getStatusCode());
                            response = Response.status(Status.NOT_FOUND).entity(problemDetail).build();
                            try {
                                changeVnfRequestTimer.observeDuration();
                            } catch (final Exception metricsException) {
                                logger.error("VnfRestServiceImpl  : changeVnfRequest() : error in collecting request duration : {}", metricsException.toString());
                            }
                            MDC.clear();
                            return response;
                        }
                    }
                }

                final NbiChangeVnfRequest nbiChangeVnfRequest = com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VimConnectionHelper
                        .getChangeVnfRequest(changeCurrentVnfRequest);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
                final String changeLcmOpId = vnfRestServiceSol241Impl.changeVnf(vnfInstanceId, nbiChangeVnfRequest, userName);
                builder = uriInfo.getBaseUriBuilder();
                builder.path(Constants.LCM_OP_OCCR_BASE_URI + changeLcmOpId);
                try {
                    changeVnfRequestTimer.observeDuration();
                } catch (final Exception metricsException) {
                    logger.error("VnfRestServiceImpl  : changeVnfRequest() : error in collecting request duration : {}", metricsException.toString());
                }
                MDC.clear();
                return Response.status(Status.ACCEPTED).header("Location", builder.build()).build();
            }
        } catch (final VNFLCMServiceException exception) {
            final Status errorCode = getHttpErrorCode(exception);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(errorCode.toString(), exception.getMessage(), vnfInstanceId,
                errorCode.getReasonPhrase(), errorCode.getStatusCode());
            response = Response.status(errorCode).entity(problemDetail).build();
        } catch (final Exception exception) {
            logger.debug(exception.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.toString(), exception.getMessage(),
                    vnfInstanceId, Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Status.INTERNAL_SERVER_ERROR.getStatusCode());
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        try {
            changeVnfRequestTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl  : changeVnfRequest() : error in collecting request duration : {}", metricsException.toString());
        }
        MDC.clear();
        return response;
    }

    private Status getHttpErrorCode(final VNFLCMServiceException exception) {
        Status httpErrorCode;
        switch (exception.getType()) {
            case VNF_IDENTIFIER_NOT_FOUND:
                httpErrorCode = Status.NOT_FOUND;
                break;
            case VNF_OPERATION_IN_PROGRESS:
                httpErrorCode = Status.CONFLICT;
                break;
            case ERROR_IN_MATCHING_CHANGE_PKG_POLICY:
                httpErrorCode = Status.BAD_REQUEST;
                break;
            default: // includes ERROR_DELETING_VNF_IDENTIFIER and UNKNOWN
                httpErrorCode = Status.INTERNAL_SERVER_ERROR;
        }
        return httpErrorCode;
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

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.orvnfm.rest.VnfRestService#instantiateVnf
     * (com.ericsson.oss.nbi.vnflcm.orvnfm.rest.model.InstantiateVnfRequest)
     */
    @Override
    public Response instantiateVnf(final String vnfInstanceId, final InputStream inputStream, @Context final UriInfo uriInfo, @Context HttpHeaders headers) {
        MDC.put("vnfInstanceId", vnfInstanceId);
        Summary.Timer instantiateVnfRequestTimer = null;
        Response response = null;
        final String jwtToken = getJwtToken(headers);
        final String userName = getUsername(jwtToken);
        final String idempotencykey = getRequestId(headers);
        logger.debug("Idempotencykey: {} ", idempotencykey);
        logger.info("{} user has started the instantiate vnf operation on lcm operation id {}", userName, vnfInstanceId);
        instantiateVnfRequestTimer = startTimer(instantiateVnfRequestTimer);

        // Check if this request is a duplicate request (i.e., retried by Service Mesh) if yes processs it and return response
        if (!StringUtils.isEmpty(idempotencykey)) {
            response = processInstantiateOperationIfRetried(vnfInstanceId, inputStream, uriInfo, userName, idempotencykey);
            if (response != null) {
                stopTimer(instantiateVnfRequestTimer);
                return response;
            }
        }
        //End

        logger.info("VnfRestServiceImpl : instantiateVnf() : Starting instantiate - vnfinstanceid-" + vnfInstanceId);
        // Conflict Operation Check
        response = getResponseForUsecaseConflictException(vnfInstanceId, LcmOperationType.INSTANTIATE, instantiateVnfRequestTimer);
        if(response != null) {
            return response;
        }
        String incomingRequestData = null;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            final String incomingRequestDataLog = StringEscapeUtils.unescapeJava(incomingRequestData.toString());
            final String vnfdId = vnfService.getVnfdId(vnfInstanceId);
            final boolean isToscaVnfd = ToscaArtifactsParser.isToscaVNFD(vnfdId);
            String areVnfdParamsSensitive;
            try {
                areVnfdParamsSensitive = ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE);
            } catch (final Exception e) {
                logger.info("Unable to read property {} from configuration. Proceeding with default value 'NO'.", Constants.ARE_VNFD_PARAMS_SENSITIVE);
                areVnfdParamsSensitive = "NO";
            }
            if(isToscaVnfd && areVnfdParamsSensitive!= null && areVnfdParamsSensitive.equalsIgnoreCase("YES")) {
                this.sensitiveParamsList = ToscaTemplateParser.getSensitiveParams(vnfService.getVnfdId(vnfInstanceId),
                        LcmOperationType.INSTANTIATE.toString());
                logger.debug("VnfRestServiceImpl : instantiateVnf() : request read from stream: {}", incomingRequestDataLog == null ? null
                        : Utils.getMaskedSensitiveParams(incomingRequestDataLog.toString(), sensitiveParamsList));
            }
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : instantiateVnf() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "",
                    Constants.INTERNAL_ERROR);
            stopTimer(instantiateVnfRequestTimer);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        //Instantiate Request Validation
        VeVnfmemProblemDetail problemDetail = validateInstantiateRequest(incomingRequestData);
        if (problemDetail != null) {
            stopTimer(instantiateVnfRequestTimer);
            return Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
        }
        final String solVersionSupported = getSolVersionSupported();
        final ObjectMapper objectMapper = new ObjectMapper();
        com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest instantiateVnfRequest = null;
        boolean isENMIntegrationRequired = false;
        if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
            try {
                instantiateVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest.class);
                if (instantiateVnfRequest.getAdditionalParams()!=null && instantiateVnfRequest.getAdditionalParams().get("addVNFToOSS") !=null) {
                    logger.debug("addVNFToOSS flag value: {}", instantiateVnfRequest.getAdditionalParams().get("addVNFToOSS").toString());
                    isENMIntegrationRequired = Boolean.parseBoolean(instantiateVnfRequest.getAdditionalParams().get("addVNFToOSS").toString());
                }
                logger.debug("isENMIntegrationRequired flag = "+isENMIntegrationRequired);
            } catch (final IOException e) {
                logger.error("VnfRestServiceImpl : instantiateVnf() : error creating model: {}", e.getMessage());
                VeVnfmemProblemDetail internalProblemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "", Constants.INTERNAL_ERROR);

                stopTimer(instantiateVnfRequestTimer);
                MDC.clear();
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(internalProblemDetail).build();
            }
        }

        response=getResponseForLicenceCheck(LcmOperationType.INSTANTIATE, isENMIntegrationRequired, instantiateVnfRequestTimer);
        if (response!=null)
            return response;

        response = getResponseForDomainRoleAccessCheck(vnfInstanceId, headers, instantiateVnfRequestTimer);
        if (response!=null)
            return response;

        try {
            logger.info("VnfRestServiceImpl : instantiateVnf() : start creating models from incoming request.");
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {

                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiInstantiateVnfRequest instantiatedVnfRequest = com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VimConnectionHelper
                        .getInstantiatedVnfRequest(instantiateVnfRequest);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
               response = vnfRestServiceSol241Impl.instantiateVnf(vnfInstanceId, instantiatedVnfRequest, uriInfo, userName, idempotencykey);
            } else {
                problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "", Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final VNFLCMServiceException ex) {
            logger.error("VnfRestServiceImpl : instantiateVnf() : error creating model: {}", ex.getMessage());
            problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
        }
        catch (final Exception e) {
            logger.error("VnfRestServiceImpl : instantiateVnf() : error creating model: {}", e.getMessage());
            problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "", Constants.INTERNAL_ERROR);

            stopTimer(instantiateVnfRequestTimer);
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        stopTimer(instantiateVnfRequestTimer);
        MDC.clear();
        return response;
    }

    /**
     * @param instantiateVnfRequestTimer
     */
    private void stopTimer(Summary.Timer instantiateVnfRequestTimer) {
        try {
            instantiateVnfRequestTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : instantiateVnf() : error in collecting request duration : {}", metricsException.toString());
        }
    }

    /**
     * @param instantiateVnfRequestTimer
     * @return
     */
    private Summary.Timer startTimer(Summary.Timer instantiateVnfRequestTimer) {
        try {
            instantiateVnfRequestTimer = VnflcmHttpServletContextListener.getInstantiateVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : instantiateVnf() : error in intializing metrics collection : {}", ex.toString());
        }
        return instantiateVnfRequestTimer;
    }

    private Response getResponseForLicenceCheck(final LcmOperationType operationType, final boolean isENMIntegrationRequired, final Timer timer) {
        boolean licenceCheck=false;
        try {
            switch (operationType) {
            case INSTANTIATE:
            case TERMINATE:
            case CHANGE_VNFPKG:
                licenceCheck = lcmAuthenticationService.checkLicense(operationType.name(), isENMIntegrationRequired);
                break;
            default:
                licenceCheck = lcmAuthenticationService.checkLicense(operationType.name(), isENMIntegrationRequired);
                break;
            }
            if (!licenceCheck) {
                logger.error("User do not have sufficient Licence to perform "+operationType.name()+" operation");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("METHOD_NOT_ALLOWED", "You do not have sufficient Licence to perform "+operationType.name()+" operation" , "",Status.METHOD_NOT_ALLOWED.toString(),
                        Status.METHOD_NOT_ALLOWED.getStatusCode());
                recordRequestDuration(timer);
                return Response.status(Status.METHOD_NOT_ALLOWED).entity(problemDetail).build();
            }
        }
        catch (final LcmWorkflowServiceException e) {
            logger.error("LCM Workflow Service exception while performing Licence Check: " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "LCM Workflow Service exception while performing License validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            recordRequestDuration(timer);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        catch (Exception e) {
            logger.error("Exception while performing License validation : " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "Exception while performing License validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            recordRequestDuration(timer);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return null;
    }

    private Response getResponseForDomainRoleAccessCheck(final String vnfInstanceId, final HttpHeaders headers, final Timer timer) {
        try {
            boolean isDracEnabled = lcmAuthenticationService.isDracEnabled();
            logger.info("DRAC Flag Value: {}", isDracEnabled);
            if(isDracEnabled) {
                final VnfResponse vnfResponse = vnfService.queryVnf(vnfInstanceId);
                if (vnfResponse!=null) {
                    final String jwtToken = getJwtToken(headers);
                    final boolean isUserAuthorized = lcmAuthenticationService.checkDomainRoleAccess(vnfResponse.getVnfProductName(), jwtToken);
                    if(!isUserAuthorized) {
                        logger.error("User do not have sufficient Domain role to perform this operation on Node Type: "+ vnfResponse.getVnfProductName());
                        final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("UnAuthorized", "You do not have sufficient Domain role to perform this operation on Node Type: "+ vnfResponse.getVnfProductName() , "",Status.UNAUTHORIZED.toString(),
                                Status.UNAUTHORIZED.getStatusCode());
                        recordRequestDuration(timer);
                        return Response.status(Status.UNAUTHORIZED).entity(problemDetail).build();
                    }
                }
            }
        }
        catch (final LcmWorkflowServiceException e) {
            logger.error("LCM Workflow Service exception while performing Domain Role Access validation: " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "LCM Workflow Service exception while performing Domain Role Access validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            recordRequestDuration(timer);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        catch (Exception e) {
            logger.error("Exception while performing Domain Role Access validation : " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "Exception while performing Domain Role Access validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            recordRequestDuration(timer);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return null;
    }

    /**
     *
     * @param requestInPayload
     * @return
     */
    private VeVnfmemProblemDetail validateInstantiateRequest(final String requestInPayload) {
        logger.info("VnfRestServiceImpl : validateInstantiateRequest() : validate the instantiate request.");
        final ObjectMapper mapper = new ObjectMapper();
        VeVnfmemProblemDetail problemDetail = null;
        try {
            final JsonNode requestNode = mapper.readTree(requestInPayload);
            final JsonNode additionalParamNode = requestNode.get(INS_REQ_ADDITIONAL_PARAMS);
            if (additionalParamNode != null) {
                if (additionalParamNode.asText() != null && !additionalParamNode.asText().trim().isEmpty()) {
                    logger.debug("VnfRestServiceImpl : healVnf() : request read from stream: {}",
                            additionalParamNode == null ? null : Utils.getMaskedSensitiveParams(additionalParamNode.asText(), sensitiveParamsList));
                }
                final JsonNode dataWorkerNode = additionalParamNode.get(DATA_WORKER_NODES_SPECIFIC);
                if (dataWorkerNode != null) {
                    logger.debug("VnfRestServiceImpl : validateInstantiateRequest() :dataWorkerNode:" + dataWorkerNode.asText());
                    final JsonNode nodePoolsNode = dataWorkerNode.get(NODE_POOLS);
                    if (nodePoolsNode == null) {
                        logger.error("VnfRestServiceImpl : validateInstantiateRequest() :Node Pools not provided in request. Reject Request");
                        problemDetail = new VeVnfmemProblemDetail("nodePoolsNotProvided",
                                "Node Pools are not present in request. " + LcmOperationType.INSTANTIATE.toString() + " operation", "",
                                Status.FORBIDDEN.toString(), Status.FORBIDDEN.getStatusCode());
                        return problemDetail;
                    }
                    logger.info("VnfRestServiceImpl : validateInstantiateRequest() :nodePoolsNode:" + nodePoolsNode.asText());
                    final List<String> poolNames = new ArrayList<String>();
                    final Iterator<JsonNode> nodePools = nodePoolsNode.elements();
                    boolean nodePoolSizeMoreThanOne = false; // During Instantiation at least one node pool with count 1 is required.
                    while (nodePools.hasNext()) {
                        final JsonNode nodePool = nodePools.next();
                        final String nodePoolName = nodePool.path("name").asText();
                        poolNames.add(nodePoolName);
                        final int nodePollCount = nodePool.path("count").asInt();
                        if (nodePollCount > 0) {
                            nodePoolSizeMoreThanOne = true;
                            break;
                        }
                    }
                    if (!nodePoolSizeMoreThanOne) {
                        logger.error("VnfRestServiceImpl : validateInstantiateRequest() : nodePollCount<1.. Reject Request.");
                        problemDetail = new VeVnfmemProblemDetail("nodePoolsCountIsZero",
                                "At least one node pool size should be greater than 0. " + LcmOperationType.INSTANTIATE.toString() + " operation", "",
                                Status.FORBIDDEN.toString(), Status.FORBIDDEN.getStatusCode());
                        return problemDetail;
                    }
                    logger.info("VnfRestServiceImpl : validateInstantiateRequest() : Pool names in request:" + String.join(",", poolNames));
                    for (final String poolsInReq : poolNames) {
                        final String[] poolNamesInReq = poolsInReq.split(",");
                        final List<String> vappNames = getVappNames(requestInPayload);
                        if (vappNames != null && !vappNames.isEmpty()) {
                            for (final String vappName : vappNames) {
                                final String[] poolNamesFromDB = vappName.split(",");
                                for (final String nameInReq : poolNamesInReq) {
                                    for (final String nameFromDB : poolNamesFromDB) {
                                        if (nameInReq.equalsIgnoreCase(nameFromDB)) {
                                            logger.error("VnfRestServiceImpl : validateInstantiateRequest() :" + nameInReq
                                                    + ": This Pool already created, Reject Request. See from DB: " + nameFromDB);
                                            problemDetail = new VeVnfmemProblemDetail(
                                                    "nodePoolAlreadyCreated", nameInReq + " Node Pool already Created. "
                                                            + LcmOperationType.INSTANTIATE.toString() + " operation",
                                                    "", Status.FORBIDDEN.toString(), Status.FORBIDDEN.getStatusCode());
                                            return problemDetail;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    logger.info(
                            "VnfRestServiceImpl : validateInstantiateRequest() :This is regular request, validation of additional param not required.");
                }
            }
        } catch (final VNFLCMServiceException | IOException e) {
            logger.warn("VnfRestServiceImpl : validateInstantiateRequest() : Cannot validate request due some error: {}", e.getMessage());
        } catch (final Exception e) {
            logger.warn("VnfRestServiceImpl : validateInstantiateRequest() : Cannot validate request due some error: {}", e.getMessage());
        }
        return problemDetail;
    }

    /**
     * get vapp names from DB for VIM in request payload
     *
     * @param requestInPayload
     * @return
     * @throws VNFLCMServiceException
     */
    private List<String> getVappNames(final String requestInPayload) throws VNFLCMServiceException {
        logger.info("VnfRestServiceImpl : getVappNames() : Get vappNames from DB.");
        final List<VnfResponse> queryAllVnf = vnfService.queryAllVnf();

        if (queryAllVnf != null && !queryAllVnf.isEmpty()) {
            final List<String> vappNames = new ArrayList<>();
            for (final VnfResponse vnfResponse : queryAllVnf) {
                if (vnfResponse.getInstantiatedVnfInfo() != null && vnfResponse.getInstantiatedVnfInfo().getVappName() != null) {
                    final String vimNameInReq = getVimNameFromReqPayload(requestInPayload);
                    if (vnfResponse.getVimConnectionInfo() != null && !vnfResponse.getVimConnectionInfo().isEmpty()) {
                        if (vnfResponse.getVimConnectionInfo().get(0) != null) {
                            final String vimNameInDb = vnfResponse.getVimConnectionInfo().get(0).getVimName();
                            if (vimNameInReq.equals(vimNameInDb)) {
                                vappNames.add(vnfResponse.getInstantiatedVnfInfo().getVappName());
                            }
                        }
                    }
                }
            }
            return vappNames;
        }
        return null;
    }

    /**
     * get VIM name from request payload to match it later with DB to find the correct vapps of a VIMs
     *
     * @param requestInPayload
     * @return
     */
    private String getVimNameFromReqPayload(final String requestInPayload) {
        logger.info("VnfRestServiceImpl : getVimNameFromReqPayload() : get Vim name from request start..");
        final ObjectMapper mapper = new ObjectMapper();
        String vimName = null;
        try {
            final JsonNode requestNode = mapper.readTree(requestInPayload);
            final JsonNode vimConnectionInfoNode = requestNode.get("vimConnectionInfo");
            final Iterator<JsonNode> vimConIterator = vimConnectionInfoNode.elements();
            while (vimConIterator.hasNext()) {
                final JsonNode vimConnectionElement = vimConIterator.next();
                vimName = vimConnectionElement.path("vimId").asText();
                logger.info("VnfRestServiceImpl : getVimNameFromReqPayload() : vimName in Request Payload:{}", vimName);
                break; // As currently vnflcm supporting single VIM in request.
            }
        } catch (final IOException e) {
            logger.warn("VnfRestServiceImpl : getVimNameFromReqPayload() : Cannot validate request due some error: {}", e.getMessage());
        } catch (final Exception e) {
            logger.warn("VnfRestServiceImpl : getVimNameFromReqPayload() : Cannot validate request due some error: {}", e.getMessage());
        }
        return vimName;
    }

    @Override
    public Response terminateVnfInstance(final String vnfInstanceId, final InputStream inputStream, @Context final UriInfo uriInfo, @Context HttpHeaders headers) {
        MDC.put("vnfInstanceId", vnfInstanceId);
        Summary.Timer terminateVnfTimer = null;
        Response responseObj = null;
        final String jwtToken = getJwtToken(headers);
        final String userName = getUsername(jwtToken);
        logger.info("{} user has started the terminate vnf operation on lcm operation id {}", userName, vnfInstanceId);
        try {
            terminateVnfTimer = VnflcmHttpServletContextListener.getTerminateVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : terminateVnfInstance() : error in intializing metrics collection : {}", ex.toString());
        }
        logger.info("VnfRestServiceImpl : terminateVnfInstance(): Starting terminate - vnfinstanceid-" + vnfInstanceId);
        //UsecaseConflict Check
        responseObj = getResponseForUsecaseConflictException(vnfInstanceId, LcmOperationType.TERMINATE, terminateVnfTimer);
        if(responseObj != null) {
            return responseObj;
        }
        //Licence Check
        VnfResponse vnfResponse;
        boolean isENMIntegrationRequired = false;
        try {
            vnfResponse = vnfService.queryVnf(vnfInstanceId);
            if (vnfResponse !=null && vnfResponse.getInstantiatedVnfInfo()!=null && vnfResponse.getInstantiatedVnfInfo().getFdn()!=null && !vnfResponse.getInstantiatedVnfInfo().getFdn().isEmpty() ) {
                isENMIntegrationRequired= true;
            }
        } catch (VNFLCMServiceException ex) {
            logger.error("VnfRestServiceImpl : terminateVnfInstance() : error while finding vnf with given vnfInstance Id", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                terminateVnfTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : terminateVnfInstance() : error in collecting request duration : {}",
                        metricsException.toString());
            }
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        responseObj=getResponseForLicenceCheck(LcmOperationType.TERMINATE, isENMIntegrationRequired, terminateVnfTimer);
        if (responseObj!=null)
            return responseObj;
        //DRAC Check
        responseObj = getResponseForDomainRoleAccessCheck(vnfInstanceId, headers, terminateVnfTimer);
        if (responseObj!=null)
            return responseObj;

        String incomingRequestData = null;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            final String incomingRequestDataLog = StringEscapeUtils.unescapeJava(incomingRequestData.toString());
            final String vnfdId = vnfService.getVnfdId(vnfInstanceId);
            final boolean isToscaVnfd = ToscaArtifactsParser.isToscaVNFD(vnfdId);
            String areVnfdParamsSensitive;
            try {
                areVnfdParamsSensitive = ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE);
            } catch (final Exception e) {
                logger.info("Unable to read property {} from configuration. Proceeding with default value 'NO'.", Constants.ARE_VNFD_PARAMS_SENSITIVE);
                areVnfdParamsSensitive = "NO";
            }
            if(isToscaVnfd && areVnfdParamsSensitive!= null && areVnfdParamsSensitive.equalsIgnoreCase("YES")) {
                this.sensitiveParamsList = ToscaTemplateParser.getSensitiveParams(vnfService.getVnfdId(vnfInstanceId),
                        LcmOperationType.TERMINATE.toString());
                logger.debug("VnfRestServiceImpl : terminateVnfInstance() : request read from stream: {}", incomingRequestDataLog == null ? null
                        : Utils.getMaskedSensitiveParams(incomingRequestDataLog.toString(), sensitiveParamsList));
            }
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : terminateVnfInstance() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "",
                    Constants.INTERNAL_ERROR);
            try {
                terminateVnfTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : terminateVnfInstance() : error in collecting request duration : {}",
                        metricsException.toString());
            }
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        if (vnfInstanceId == null || "".equals(vnfInstanceId.trim())) {
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), VNF_INSTANCE_ID_NULL, "",
                    "", Status.BAD_REQUEST.getStatusCode());
            responseObj = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
        } else {
            final String solVersionSupported = getSolVersionSupported();
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                    final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.TerminateVnfRequest terminateVnfRequest = objectMapper
                            .readValue(incomingRequestData,
                                    com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.TerminateVnfRequest.class);
                    final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
                    responseObj = vnfRestServiceSol241Impl.terminateVnfInstance(vnfInstanceId, terminateVnfRequest, uriInfo, userName);
                    logger.info("VnfRestServiceImpl : terminateVnfInstance() : model created.");
                } else {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "",
                            "", Constants.ACCEPTED);
                    responseObj = Response.status(Status.ACCEPTED).entity(problemDetail).build();
                }
            } catch (final IOException e) {
                logger.error("VnfRestServiceImpl : terminateVnfInstance() : error creating model: {}", e.getMessage());
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "",
                        Constants.INTERNAL_ERROR);
                try {
                    terminateVnfTimer.observeDuration();
                } catch (final Exception metricsException) {
                    logger.error("VnfRestServiceImpl : terminateVnfInstance() : error in collecting request duration : {}",
                            metricsException.toString());
                }
                MDC.clear();
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
            }
        }
        try {
            terminateVnfTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : terminateVnfInstance() : error in collecting request duration : {}", metricsException.toString());
        }
        MDC.clear();
        return responseObj;

    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.orvnfm.rest.VnfRestService#scaleVnf(java. lang.String,
     * com.ericsson.oss.nbi.vnflcm.orvnfm.rest.model.ScaleVnfRequest, javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response scaleVnf(final String vnfInstanceId, final InputStream inputStream, @Context final UriInfo uriInfo, @Context HttpHeaders headers) {
        Summary.Timer scaleVnfRequestTimer = null;
        final String jwtToken = getJwtToken(headers);
        final String userName = getUsername(jwtToken);
        logger.info("{} user has started the scale vnf operation on lcm operation id {}", userName, vnfInstanceId);
        scaleVnfRequestTimer = startScaleVnfTimer(scaleVnfRequestTimer);
        logger.info("VnfRestServiceImpl : scaleVnf() : Start vnfInstanceId:{}", vnfInstanceId);
        Response responseObj = null;
        if (vnfInstanceId == null || "".equals(vnfInstanceId.trim())) {
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(),
                    VNF_INSTANCE_ID_NULL, "", "", Status.BAD_REQUEST.getStatusCode());
            responseObj = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
        }
        final String idempotencykey = getRequestId(headers);
        logger.debug("Idempotencykey: {} ", idempotencykey);

         // Check if this request is a duplicate request (i.e., retried by Service Mesh) if yes processs it and return response
        if (!StringUtils.isEmpty(idempotencykey)) {
            responseObj = processScaleOperationIfRetried(vnfInstanceId, inputStream, uriInfo, userName, idempotencykey);
            if (responseObj != null) {
                stopScaleVnfTimer(scaleVnfRequestTimer);
                return responseObj;
            }
        }
        //End

        //Usecase Conflict Check
        responseObj = getResponseForUsecaseConflictException(vnfInstanceId, LcmOperationType.SCALE, scaleVnfRequestTimer);
        if(responseObj != null) {
            return responseObj;
        }
        //Licence Check
        responseObj=getResponseForLicenceCheck(LcmOperationType.SCALE, false, scaleVnfRequestTimer);
        if (responseObj!=null)
            return responseObj;
        //DRAC Check
        responseObj = getResponseForDomainRoleAccessCheck(vnfInstanceId, headers, scaleVnfRequestTimer);
        if (responseObj!=null)
            return responseObj;

        String incomingRequestData = null;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            final String incomingRequestDataLog = StringEscapeUtils.unescapeJava(incomingRequestData.toString());
            final String vnfdId = vnfService.getVnfdId(vnfInstanceId);
            final boolean isToscaVnfd = ToscaArtifactsParser.isToscaVNFD(vnfdId);
            String areVnfdParamsSensitive;
            try {
                areVnfdParamsSensitive = ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE);
            } catch (final Exception e) {
                logger.info("Unable to read property {} from configuration. Proceeding with default value 'NO'.", Constants.ARE_VNFD_PARAMS_SENSITIVE);
                areVnfdParamsSensitive = "NO";
            }
            if(isToscaVnfd && areVnfdParamsSensitive!= null && areVnfdParamsSensitive.equalsIgnoreCase("YES")) {
                this.sensitiveParamsList = ToscaTemplateParser.getSensitiveParams(vnfService.getVnfdId(vnfInstanceId),
                        LcmOperationType.SCALE.toString());
                logger.debug("VnfRestServiceImpl : scaleVnf() : request read from stream: {}", incomingRequestDataLog == null ? null
                        : Utils.getMaskedSensitiveParams(incomingRequestDataLog.toString(), sensitiveParamsList));
            }
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : scaleVnf() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "",
                    Constants.INTERNAL_ERROR);
            try {
                scaleVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : scaleVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        final String solVersionSupported = getSolVersionSupported();
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                logger.info("VnfRestServiceImpl : scaleVnf() : start creating models from incoming request.");
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ScaleVnfRequest scaleVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ScaleVnfRequest.class);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
                if (vnfInstanceId == null || "".equals(vnfInstanceId.trim())) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(),
                            VNF_INSTANCE_ID_NULL, "", "", Status.BAD_REQUEST.getStatusCode());
                    responseObj = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                } else if (scaleVnfRequest.getNumberOfSteps() < 0) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(),
                            NUMBER_OF_STEPS_NEGATIVE, "", "", Status.BAD_REQUEST.getStatusCode());
                    responseObj = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                } else {
                    responseObj = vnfRestServiceSol241Impl.scaleVnf(vnfInstanceId, scaleVnfRequest, uriInfo, userName, idempotencykey);
                }

                logger.info("VnfRestServiceImpl : scaleVnf() : model created.");
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                responseObj = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final IOException e) {
            logger.error("VnfRestServiceImpl : scaleVnf() : error creating model: {}", e.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "",
                    Constants.INTERNAL_ERROR);
            try {
                scaleVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : scaleVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        try {
            scaleVnfRequestTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : scaleVnf() : error in collecting request duration : {}", metricsException.toString());
        }
        return responseObj;
    }

    /**
     * @param scaleVnfRequestTimer
     * @return
     */
    private Summary.Timer startScaleVnfTimer(Summary.Timer scaleVnfRequestTimer) {
        try {
            scaleVnfRequestTimer = VnflcmHttpServletContextListener.getScaleVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : scaleVnf() : error in intializing metrics collection : {}", ex.toString());
        }
        return scaleVnfRequestTimer;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.orvnfm.rest.VnfRestService#operateVnf(java .lang.String,
     * com.ericsson.oss.nbi.vnflcm.orvnfm.rest.model.OperateVnfRequest, javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response operateVnf(final String vnfInstanceId, final InputStream inputStream, @Context final UriInfo uriInfo, @Context HttpHeaders headers) {
        Summary.Timer operateVnfRequestTimer = null;
        try {
            operateVnfRequestTimer = VnflcmHttpServletContextListener.getHealVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : operateVnf() : error in intializing metrics collection : {}", ex.toString());
        }
        logger.info("VnfRestServiceImpl : operateVnf() : Start vnfinstanceid-" + vnfInstanceId);
        Response responseObj = null;

        String incomingRequestData = null;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            logger.debug("VnfRestServiceImpl : operateVnf() : request read from stream: {}", incomingRequestData);
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : operateVnf() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                operateVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : operateVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        final String solVersionSupported = getSolVersionSupported();
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("VnfRestServiceImpl : operateVnf() : start creating models from incoming request.");
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.OperateVnfRequest operateVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.OperateVnfRequest.class);
                if (vnfInstanceId == null || "".equals(vnfInstanceId.trim())) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), VNF_INSTANCE_ID_NULL,
                            "", "", Status.BAD_REQUEST.getStatusCode());
                    responseObj = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                } else if (operateVnfRequest == null || "".equals(vnfInstanceId.trim())) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), VNF_INSTANCE_ID_NULL,
                            "", "", Status.BAD_REQUEST.getStatusCode());
                    responseObj = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                } else {
                    final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
                    responseObj = vnfRestServiceSol241Impl.operateVnf(vnfInstanceId, operateVnfRequest, uriInfo);
                }
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                responseObj = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }

        } catch (final IOException e) {
            logger.error("VnfRestServiceImpl : operateVnf() : error creating model: {}", e.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "", Constants.INTERNAL_ERROR);
            try {
                operateVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : operateVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        ;
        try {
            operateVnfRequestTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : operateVnf() : error in collecting request duration : {}", metricsException.toString());
        }
        return responseObj;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.orvnfm.rest.VnfRestService#healVnf(java.lang.String, com.ericsson.oss.nbi.vnflcm.orvnfm.rest.HealVnfRequest,
     * javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response healVnf(final String vnfInstanceId, final InputStream inputStream, final UriInfo uriInfo, @Context HttpHeaders headers) {
        Summary.Timer healVnfRequestTimer = null;
        final String jwtToken = getJwtToken(headers);
        final String userName = getUsername(jwtToken);
        logger.info("{} user has started the heal vnf operation on lcm operation id {}", userName, vnfInstanceId);
        try {
            healVnfRequestTimer = VnflcmHttpServletContextListener.getHealVnfRequestLatency().startTimer();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : healVnf() : error in intializing metrics collection : {}", ex.toString());
        }
        logger.info("VnfRestServiceImpl : healVnf() : Start vnfInstanceId {}.", vnfInstanceId);
        Response responseObj = null;
        //UsecaseConflict Check
        String incomingRequestData = null;
        responseObj = getResponseForUsecaseConflictException(vnfInstanceId, LcmOperationType.HEAL, healVnfRequestTimer);
        if(responseObj != null) {
            return responseObj;
        }
        //Licence Check
        responseObj=getResponseForLicenceCheck(LcmOperationType.HEAL, false, healVnfRequestTimer);
        if (responseObj!=null)
            return responseObj;
        //DRAC Check
        responseObj = getResponseForDomainRoleAccessCheck(vnfInstanceId, headers, healVnfRequestTimer);
        if (responseObj!=null)
            return responseObj;
        try {
            incomingRequestData = readIncomingRequest(inputStream);
            final String incomingRequestDataLog = StringEscapeUtils.unescapeJava(incomingRequestData.toString());
            final String vnfdId = vnfService.getVnfdId(vnfInstanceId);
            final boolean isToscaVnfd = ToscaArtifactsParser.isToscaVNFD(vnfdId);
            String areVnfdParamsSensitive;
            try {
                areVnfdParamsSensitive = ReadPropertiesUtility.readConfigProperty(Constants.ARE_VNFD_PARAMS_SENSITIVE);
            } catch (final Exception e) {
                logger.info("Unable to read property {} from configuration. Proceeding with default value 'NO'.", Constants.ARE_VNFD_PARAMS_SENSITIVE);
                areVnfdParamsSensitive = "NO";
            }
            if(isToscaVnfd && areVnfdParamsSensitive!= null && areVnfdParamsSensitive.equalsIgnoreCase("YES")) {
                this.sensitiveParamsList = ToscaTemplateParser.getSensitiveParams(vnfService.getVnfdId(vnfInstanceId),
                        LcmOperationType.HEAL.toString());
                logger.debug("VnfRestServiceImpl : healVnf() : request read from stream: {}", incomingRequestDataLog == null ? null
                        : Utils.getMaskedSensitiveParams(incomingRequestDataLog.toString(), sensitiveParamsList));
            }
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : healVnf() : error reading incoming request: {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "",
                    Constants.INTERNAL_ERROR);
            try {
                healVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : healVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        final String solVersionSupported = getSolVersionSupported();
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                logger.info("VnfRestServiceImpl : healVnf() : start creating models from incoming request.");
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.HealVnfRequest healVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.HealVnfRequest.class);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
                if (vnfInstanceId == null || "".equals(vnfInstanceId.trim())) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(),
                            VNF_INSTANCE_ID_NULL, "", "", Status.BAD_REQUEST.getStatusCode());
                    responseObj = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                } else {
                    responseObj = vnfRestServiceSol241Impl.healVnf(vnfInstanceId, healVnfRequest, uriInfo, userName);
                }
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                responseObj = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final IOException e) {
            logger.error("VnfRestServiceImpl : healVnf() : error creating model: {}", e.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", e.getMessage(), "", "",
                    Constants.INTERNAL_ERROR);
            try {
                healVnfRequestTimer.observeDuration();
            } catch (final Exception metricsException) {
                logger.error("VnfRestServiceImpl : healVnf() : error in collecting request duration : {}", metricsException.toString());
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        try {
            healVnfRequestTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : healVnf() : error in collecting request duration : {}", metricsException.toString());
        }
        return responseObj;
    }

    public boolean isUnitTesting() {
        return isUnitTesting;
    }

    public void setUnitTesting(final boolean isUnitTesting) {
        this.isUnitTesting = isUnitTesting;
    }

    /**
     * @param vnfService
     *            the vnfService to set
     */
    public void setVnfService(final VnfService vnfService) {
        this.vnfService = vnfService;
    }

    public void setFileResourceService(final FileResourceService fileResourceService) {
        this.fileResourceService = fileResourceService;
    }

    @Override
    public Response subscription(final InputStream inputStream, final UriInfo uriInfo) {
        // This api needs to be enhanced, and jira to be created
        // For, plug test just return the 201 response.
        // LccnSubscriptionRequest Request needs to be populated.
        return Response.status(Status.CREATED).build();
    }
    @Override
    public Response queryVnf(final String vnfInstanceId, @Context HttpHeaders headers) {
        logger.debug("vnfInstanceId: {}", vnfInstanceId);
        Response response = null;
        try {
            // Headers are added for EO-CM, after evry notifcation send to EO-CM, there is query request back to VNFM
            // For every query request, EO-CM does not need the resources information
            // So to avoid the unnecessary calls to VIM to read resources can be avoided using this header.
            QueryOpConfig queryOpConfig = null;//new QueryOpConfig();
            if (headers != null) {
                final List<String> requestHeader = headers.getRequestHeader("readResourcesFromCloud");
                if (requestHeader != null && !requestHeader.isEmpty()) {
                    final String readResourcesFromCloudFlag = requestHeader.get(0);
                    if (readResourcesFromCloudFlag !=null && !readResourcesFromCloudFlag.isEmpty() 
                            && (readResourcesFromCloudFlag.equalsIgnoreCase("YES") || readResourcesFromCloudFlag.equalsIgnoreCase("true"))) {
                        queryOpConfig = new QueryOpConfig();
                        queryOpConfig.setReadVnfResources(true);
                    }
                }
            }
            final VnfResponse vnfInstance = vnfService.queryAndUpdateVnfWithResources(vnfInstanceId.trim(), queryOpConfig);
            final String solVersionSupported = getSolVersionSupported();
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance vnfInfo = new com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance();
                final InstantiatedVnfInfo instantiatedVnfInfo = vnfInstance.getInstantiatedVnfInfo();
                if(vnfInstance.getInstantiationState() == InstantiatedState.INSTANTIATED && vnfInstance.getVnfProductName().toLowerCase().contains("emm") && 
                        ((instantiatedVnfInfo.getVnfcResourceInfo() == null || instantiatedVnfInfo.getVnfcResourceInfo().isEmpty() ) && 
                        (instantiatedVnfInfo.getVnfVirtualLinkResourceInfo() != null && !instantiatedVnfInfo.getVnfVirtualLinkResourceInfo().isEmpty()))) {
                    // This is temporary fix, must be changed with proper error handling in service side.
                    logger.info("This seems to be the EMM Infra Workflow which only create Security and Network resources initially.");
                }else if (vnfInstance.getInstantiationState() == InstantiatedState.INSTANTIATED && (instantiatedVnfInfo.getVnfcResourceInfo() == null
                                || instantiatedVnfInfo.getVnfcResourceInfo().isEmpty())) {
                    final String errorMessage = "VNF is in Instantiated, however there are no resources are associated to it or VNFM is unable to fetch the resources details from VIM.";
                    logger.error(errorMessage);
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.SERVICE_UNAVAILABLE.getReasonPhrase(), errorMessage,
                            vnfInstanceId, "", Status.SERVICE_UNAVAILABLE.getStatusCode());
                    response = Response.status(Status.SERVICE_UNAVAILABLE).entity(problemDetail).build();
                    return response;
                }
                VeVnfmemSol002V241ModelGenerator.translateVNFInstanceToModel(vnfInstance, vnfInfo);
                response = Response.status(Status.OK).entity(vnfInfo).build();
            } else {
                logger.error("For SOL002 only version 2.4.1 is supported.");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "",
                        vnfInstanceId, "", Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final VNFLCMServiceException exception) {
            logger.error("Exception occured while invoking getVNFInstance.", exception);
            return getResponseForException(exception, vnfInstanceId, "get");
        } catch (final Exception e) {
            throw new WebApplicationException(e);
        }
        return response;
    }

    @Override
    public Response queryAllVnf(boolean queryVimResource, String paginationFlag) {
        logger.info("VnfRestServiceImpl : queryAllVnfWithResources() : get all vnfs available.");
        Response response = null;
        final QueryOpConfig queryOpConfig = new QueryOpConfig();
        Map<String, List<String>> customParamsMap = new HashMap<>();
        Map<String, Map<String, List<String>>> queryParamsMap = getQueryParameters();
        if (queryParamsMap != null) {
            customParamsMap = queryParamsMap.get(Constants.CUSTOM_PARAMS);
            queryOpConfig.setQueryParameters(queryParamsMap);
        }
        // Check if size is present in query, else set default
        int page_size = 15;
        int max_page_size = 100;
        String isPaginationEnabled = null;
        try {
            isPaginationEnabled = ReadPropertiesUtility.readConfigProperty(Constants.PAGINATION_ENABLED);
            logger.info("isPaginationEnabled = "+isPaginationEnabled);
        } catch (final Exception e) {
            logger.error("Exception while fetching pagination Enabled Config Param {}",e.getMessage());
        }
        if(paginationFlag != null && !paginationFlag.isEmpty()) {
            if(paginationFlag.equalsIgnoreCase("true")) {
                isPaginationEnabled = "YES";
                queryOpConfig.setPaginationFlag(paginationFlag);
            } else if(paginationFlag.equalsIgnoreCase("false")) {
                isPaginationEnabled = "NO";
                queryOpConfig.setPaginationFlag(paginationFlag);
            }
        }
        if (customParamsMap != null && !customParamsMap.isEmpty() && isPaginationEnabled!= null && isPaginationEnabled.equalsIgnoreCase("YES")) {
            try {
                final ConfigurationEnvironment configEnvironment = new ConfigurationEnvironmentBean();
                if (!customParamsMap.containsKey(Constants.SIZE)) {
                    final List<String> sizeParamList = new ArrayList<>();
                    final Object pageSizeConfigParam = configEnvironment.getValue(Constants.PAGE_SIZE);
                    if (pageSizeConfigParam != null && !pageSizeConfigParam.toString().isEmpty() && Integer.parseInt(pageSizeConfigParam.toString()) > 0) {
                        page_size = Integer.parseInt(pageSizeConfigParam.toString());
                        sizeParamList.add(pageSizeConfigParam.toString());
                        logger.info("Page size from config params: {}",page_size);
                    } else {
                        sizeParamList.add(String.valueOf(page_size));
                    }
                    customParamsMap.put(Constants.SIZE, sizeParamList);
                } else {
                    if (Utils.isInteger(customParamsMap.get(Constants.SIZE).get(0))) {
                        final int query_page_size = Integer.valueOf(customParamsMap.get(Constants.SIZE).get(0));
                        if (query_page_size < 0) {
                            final String errorMessage = "Invalid page number::"+query_page_size+", page number must be greater than 0";
                            logger.error(errorMessage);
                            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Constants.WRONG_QUERY_PARAM, errorMessage, "",
                                        Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST.getStatusCode());
                            response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                            return response;
                        } else if (query_page_size == 0) {
                            logger.debug("query page size is zero, returning empty response.");
                            return Response.status(Status.OK).entity(new ArrayList<>()).build();
                        }
                    } else {
                        final String errorMessage = "Invalid query page size value for size parameter::"+customParamsMap.get(Constants.SIZE).get(0);
                        logger.error(errorMessage);
                        final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Constants.WRONG_QUERY_PARAM, errorMessage, "",
                                    Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST.getStatusCode());
                        response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                        return response;
                    }
                }
                if (customParamsMap.containsKey(Constants.NEXTPAGE_OPQ_MARKER)) {
                    // Convert the nextpage_opaque_marker value to Default timezone
                    final List<String> convertedNextPageMarkers = Utils.convertTimeStampToDefault(customParamsMap, Constants.NEXTPAGE_OPQ_MARKER);
                    if (convertedNextPageMarkers == null || convertedNextPageMarkers.isEmpty()) {
                        customParamsMap.remove(Constants.NEXTPAGE_OPQ_MARKER);
                    } else {
                        customParamsMap.put(Constants.NEXTPAGE_OPQ_MARKER, convertedNextPageMarkers);
                    }
                }
                if (queryParamsMap == null) {
                    queryParamsMap = new HashMap<>();
                }
                queryParamsMap.put(Constants.CUSTOM_PARAMS, customParamsMap);
                queryOpConfig.setQueryParameters(queryParamsMap);
                page_size = Integer.valueOf(customParamsMap.get(Constants.SIZE).get(0));
                try {
                    final Object maxPageSizeConfigParam = configEnvironment.getValue(Constants.MAX_PAGE_SIZE);
                    if (maxPageSizeConfigParam != null && !maxPageSizeConfigParam.toString().isEmpty()) {
                        max_page_size = Integer.parseInt(maxPageSizeConfigParam.toString());
                        logger.info("Max Page size from config params: {}",max_page_size);
                    }
                } catch (final Exception e) {
                    logger.info("Exception while fetching maximum page size from configuration. Hence using default max page size 100");
                }
                if (page_size>max_page_size) {
                    final String errorMessage = "Maximum records per page exceeds maxPageSize:"+max_page_size+". Please provide query page size less than maxPageSize value";
                    logger.error(errorMessage);
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), errorMessage, "",
                                "", Status.BAD_REQUEST.getStatusCode());
                    response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                    return response;
               }
            } catch (final Exception e) {
                logger.error("Exception while fetching page size from configuration. Hence using the default size 15");
            }
        } else {
            try {
                if (isPaginationEnabled!= null && isPaginationEnabled.equalsIgnoreCase("YES")) {
                    final ConfigurationEnvironment configEnvironment = new ConfigurationEnvironmentBean();
                    final List<String> sizeParamList = new ArrayList<>();
                    final Object pageSizeConfigParam = configEnvironment.getValue(Constants.PAGE_SIZE);
                    if (pageSizeConfigParam != null && !pageSizeConfigParam.toString().isEmpty() && Integer.parseInt(pageSizeConfigParam.toString()) > 0) {
                        page_size = Integer.parseInt(pageSizeConfigParam.toString());
                        sizeParamList.add(pageSizeConfigParam.toString());
                        logger.info("Page size from config params: {}",page_size);
                    } else {
                        sizeParamList.add(String.valueOf(page_size));
                    }
                    final Map<String, List<String>> queryParams = new HashMap<>();
                    queryParams.put(Constants.SIZE, sizeParamList);
                    if(queryParamsMap == null) {
                        queryParamsMap = new HashMap<>();
                    }
                    final Map<String, Map<String, List<String>>> queryParametersMap = new HashMap<>();
                    queryParametersMap.put(Constants.CUSTOM_PARAMS, queryParams);
                    queryOpConfig.setQueryParameters(queryParametersMap);
                }
            } catch (final Exception e) {
                logger.error("Exception while fetching queryPageSize Config Param {}",e.getMessage());
            }
        }
        logger.info("queryVimResource: {}", queryVimResource);
        queryOpConfig.setReadVnfResources(queryVimResource);
        try {
            final List<VnfResponse> vnfInstance = vnfService.queryAndUpdateAllVnfWithResources(queryOpConfig);
            final String solVersionSupported = getSolVersionSupported();
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final List<com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance> vnfInfos = new ArrayList<com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance>();
                for (final VnfResponse iVnfResponse : vnfInstance) {
                    final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance vnfInfo = new com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance();
                    VeVnfmemSol002V241ModelGenerator.translateVNFInstanceToModel(iVnfResponse, vnfInfo);
                    vnfInfos.add(vnfInfo);
                }
                // Preparation for Header
                if (queryOpConfig.getQueryParameters() != null && !queryOpConfig.getQueryParameters().isEmpty()) {
                    final Map<String, List<String>> customParams = queryOpConfig.getQueryParameters().get(Constants.CUSTOM_PARAMS);
                    if(customParams != null && !customParams.isEmpty()) {
                        final Set<Entry<String, List<String>>> queryParamEntrySet = customParams.entrySet();
                        List<String> queryParamValues = null;
                        for (Entry<String, List<String>> entry : queryParamEntrySet) {
                            final String queryParamKey = entry.getKey();
                            if (queryParamKey.equalsIgnoreCase(Constants.SIZE)) {
                                queryParamValues = entry.getValue();
                                break;
                            }
                        }
                        if (queryParamValues != null) {
                            page_size = Integer.parseInt(queryParamValues.get(0));
                        }
                    }
                }
                if (vnfInfos != null && !vnfInfos.isEmpty() && vnfInfos.size() < page_size) {
                    final List<String> headerLinks = getHeaderLinks(vnfInstance.get(0).getCreationTime(), null);
                    response = Response.status(Status.OK).header("Link", headerLinks).entity(vnfInfos).build();
                } else if (vnfInfos != null && !vnfInfos.isEmpty() && isPaginationEnabled.equalsIgnoreCase("YES")) {
                    final List<String> headerLinks = getHeaderLinks(vnfInstance.get(0).getCreationTime(), vnfInstance.get(vnfInstance.size()-1).getCreationTime());
                    response = Response.status(Status.OK).header("Link", headerLinks).entity(vnfInfos).build();
                } else {
                    response = Response.status(Status.OK).entity(vnfInfos).build();
                }
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final VNFLCMServiceException exception) {
            logger.error("Exception occured while invoking getVNFInstance.", exception);
            return getResponseForException(exception, "", "get");
        } catch (final Exception e) {
            throw new WebApplicationException(e);
        }
        return response;
    }

    private List<String> getHeaderLinks(final String self_opaque_marker, final String nextpage_opaque_marker) {
        final List<String> headerLinks = new ArrayList<>();
        String evnfmHostname = "";
        String lcmOccusUri = "";
        try {
            evnfmHostname = fetchEvnfmHost();
            try {
                final String ingressHostName = ReadPropertiesUtility.readConfigProperty(Constants.VNF_CLUSTER_DNS);
                if (ingressHostName != null && !ingressHostName.isEmpty()) {
                    evnfmHostname = ingressHostName;
                }
            } catch (final Exception e) {
                // If ingress_hostname cannot be retrieved, then we create the links with evnfmHostname
                logger.info("ingressHostName could not be retrieved.Hence using evnfmHostname property");
            }
            if (evnfmHostname != null && !evnfmHostname.isEmpty()) {
                lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VEVNFMEM_URI + Constants.QUERY_VNF_BASE_URI.substring(0,Constants.QUERY_VNF_BASE_URI.length() - 1);
                logger.info("query vnf uri {}", lcmOccusUri);
            } else {
                return headerLinks; //Return empty
            }
        } catch (final Exception e) {
            logger.error("Unable create links {}", e.getMessage());
            return headerLinks;
        }
        // For Self link, just reduce the time by 1 milliseconds, so if any query comes this record can be queries.
        // Because in JPA query it is used as where statTime > nextpage_opaque_marker
        String updatedStartTime = "";
        try {
            final TimeZone utc = TimeZone.getTimeZone("UTC");
            final SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            final SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            sourceFormat.setTimeZone(utc);
            final Date convertedDate = sourceFormat.parse(self_opaque_marker);
            final String dateAfterRemovingZ = destFormat.format(convertedDate);
            
            final LocalDateTime dateTime = LocalDateTime.parse(dateAfterRemovingZ);
            final LocalDateTime subDateTime = dateTime.minusNanos(1000);
            final DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            updatedStartTime = subDateTime.format(ZDT_FORMATTER);
        } catch (final Exception ex) {
            logger.error("Error parsing Date {}", ex.getMessage());
            return headerLinks;
        }

        headerLinks.add("<" + lcmOccusUri +"?"+Constants.NEXTPAGE_OPQ_MARKER+"=" + updatedStartTime + ">; rel=\"self\"");
        if (nextpage_opaque_marker!= null && !nextpage_opaque_marker.isEmpty()) {
            headerLinks.add("<" + lcmOccusUri +"?"+Constants.NEXTPAGE_OPQ_MARKER+"=" + nextpage_opaque_marker + ">; rel=\"next\"");
        }
        return headerLinks;
    }

    private String fetchEvnfmHost() {
        try {
            return ReadPropertiesUtility.readConfigProperty(Constants.ENM_HOST_NAME);
        } catch (final Exception e) {
            logger.error("readevnfmHostnameFromEnv() : Error in reading ENM host name from environment {}", e.getMessage());
        }
        return null;
    }

    private Map<String, Map<String, List<String>>> getQueryParameters() {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        if (queryParameters != null && !queryParameters.isEmpty()) {
            logger.info("VnfRestServiceImpl : getQueryParameters() : queryParameters {}", uriInfo.getQueryParameters());
            final Map<String, List<String>> customQueryParams = new HashMap<>();
            final Map<String, List<String>> standardQueryParams = new HashMap<>();
            final Map<String, Map<String, List<String>>> queryParamsMap = new HashMap<>();
            final Set<String> keySet = queryParameters.keySet();
            final Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                final String queryParam = iterator.next();
                final List<String> queryParamValue = queryParameters.get(queryParam);
                if(!queryParam.contains(".")) {
                    customQueryParams.put(queryParam, queryParamValue);
                    queryParamsMap.put(Constants.CUSTOM_PARAMS, customQueryParams);
                } else {
                    standardQueryParams.put(queryParam, queryParamValue);
                    queryParamsMap.put(Constants.STANDARD_PARAMS, standardQueryParams);
                }
            }
            return queryParamsMap;
        }
        return null;
    }

    public Response getApiVersions() {
        //This api gives information about VNFLCM api versions supported by SOL
        //Refer ETSI SOL002 and SOL013 documents to know more about api versions
        logger.info("VnfRestServiceImpl : getApiVersions() : get vnflcm api versions supoorted by sol002");
        Response response = null;
        try {
            logger.debug("query param size: {}", uriInfo.getQueryParameters().size());
            if ((uriInfo.getQueryParameters().size())>0){
                final String errorMessage = "Query Parameters not supported";
                logger.error(errorMessage);
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), errorMessage, "",
                            "", Status.BAD_REQUEST.getStatusCode());
                response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                return response;
            }
            final String apiVersionList = getAllApiVersions();
            logger.info("apiVersionList : {}",apiVersionList);
            if (apiVersionList != null && !apiVersionList.isEmpty()) {
                response = Response.status(Status.OK) .entity(apiVersionList).build();
            }
        } catch (final Exception ex) {
            logger.error("Exception occured while invoking getApiVersions():{}",ex.toString());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(), "",
                        "", Status.INTERNAL_SERVER_ERROR.getStatusCode());
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return response;
    }

    private String getAllApiVersions() {
        //As of now VNFLCM api versions are 1.4.0 and 2.0.0
        //supported by SOL003 v2.7.1 and v3.3.1 respectively.
        //TODO Whenever new SOL versions are supported, 
        //the below list needs to be updated with the supported versions
        String apiVersionList = "{\"uriPrefix\":\"vevnfmem/vnflcm\",\"apiVersions\": [{\"version\":\"1.4.0\","
                + "\"isDeprecated\":false},{\"version\":\"2.0.0\",\"isDeprecated\":false}]}";
        return apiVersionList;
    }

    private Response getResponseForException(final VNFLCMServiceException vnflcmServiceException, final String vnfIdentifier,
                                             final String operationType) {
        Response response = null;
        switch (vnflcmServiceException.getType()) {
            case VNF_IDENTIFIER_NOT_FOUND:
                if ("get".equals(operationType)) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Identifier not found", vnflcmServiceException.getMessage(),
                            vnfIdentifier, vnflcmServiceException.getType().name(), Constants.NOT_FOUND);
                    response = Response.status(Status.NOT_FOUND).entity(problemDetail).build();
                }
                break;
            case ERROR_FETCHING_VNF_INSTANCE:
                if ("get".equals(operationType)) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Error in preparing the VNF details", vnflcmServiceException.getMessage(),
                            vnfIdentifier, vnflcmServiceException.getType().name(), Constants.SERVICE_UNAVAILABLE);
                    response = Response.status(Status.SERVICE_UNAVAILABLE).entity(problemDetail).build();
                }
                break;
            case UNKNOWN:
            default:
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", vnflcmServiceException.getMessage(),
                        vnfIdentifier, vnflcmServiceException.getType().name(), Constants.INTERNAL_ERROR);
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
                break;
        }
        return response;
    }

    /**
     * @return the vnfLifeCycleManagementService
     */
    public VnfService getVnfLifeCycleManagementService() {
        return vnfService;
    }

    /**
     * @param vnfLifeCycleManagementService
     *            the vnfLifeCycleManagementService to set
     */
    public void setVnfLifeCycleManagementService(final VnfService vnfLifeCycleManagementService) {
        this.vnfService = vnfLifeCycleManagementService;
    }
    private Response getResponseForUsecaseConflictException(final String vnfInstanceId, final LcmOperationType operationType, final Timer timer) {
        Response response = null;
        VnfResponse vnfResponse = null;
        boolean isAnyLcmOperationRunning;
        try {
            vnfResponse = vnfService.queryVnf(vnfInstanceId);
            isAnyLcmOperationRunning = vnfService.isLcmOperationInProgress(vnfInstanceId);
        } catch (VNFLCMServiceException vnfNotFoundEx) {
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("VNF with instanceId" + vnfInstanceId + "is not found database", vnfNotFoundEx.getMessage(), "", "", Constants.NOT_FOUND);
            response = Response.status(Status.NOT_FOUND).entity(problemDetail).build();
            recordRequestDuration(timer);
            return response;
        }
        final InstantiatedState instantiatedState = vnfResponse.getInstantiationState();
        switch (operationType) {
        case INSTANTIATE:
            if ((instantiatedState != null && instantiatedState.equals(InstantiatedState.INSTANTIATED))) {
                logger.error("VnfRestServiceImpl : instantiateVnf() : Vnf instance is already instantiated");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(
                        LcmOperationType.INSTANTIATE.toString() + " operation failed, Vnf already instantiated", "Vnf instance is already in instantiated state, "
                                + LcmOperationType.INSTANTIATE.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response =  Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            break;
        case SCALE:
            if ((instantiatedState != null && instantiatedState.equals(InstantiatedState.NOT_INTANTIATED))) {
                logger.error("VnfRestServiceImpl : scaleVnf() : Vnf instance is not instantiated");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(LcmOperationType.SCALE.toString() + " operation failed, Vnf not instantiated",
                        " operation failed, Vnf instance is not in instantiated state, " + LcmOperationType.SCALE.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            if(isAnyLcmOperationRunning){
                logger.error("VnfRestServiceImpl : scaleVnf() : Another Operation is in starting or processing or failed_temp");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(LcmOperationType.SCALE.toString() + " operation failed, Vnf operation is ongoing",
                        "Another Operation is in starting or processing or failed_temp, " + LcmOperationType.SCALE.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            break;
        case HEAL:
            if ((instantiatedState != null && instantiatedState.equals(InstantiatedState.NOT_INTANTIATED))) {
                logger.error("VnfRestServiceImpl : healVnf() : Vnf instance is not in instantiated state");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(LcmOperationType.HEAL.toString() + " operation failed, Vnf not instantiated",
                        " Vnf instance is not in instantiated state, " + LcmOperationType.HEAL.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            if(isAnyLcmOperationRunning){
                logger.error("VnfRestServiceImpl : healVnf() : Another Operation is in starting or processing or failed_temp");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(LcmOperationType.HEAL.toString() + " operation failed, Vnf operation is ongoing",
                        "There is another LCM operation running on this VNF instance, " + LcmOperationType.HEAL.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            break;
        case TERMINATE:
            if ((instantiatedState != null && instantiatedState.equals(InstantiatedState.NOT_INTANTIATED))) {
                logger.error("VnfRestServiceImpl : terminateVnf() : Vnf instance is not instantiated");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(
                        LcmOperationType.TERMINATE.toString() + " operation failed, Vnf not instantiated", "Vnf instance is not in instantiated state, "
                                + LcmOperationType.TERMINATE.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            if(isAnyLcmOperationRunning){
                logger.error("VnfRestServiceImpl : terminateVnf() : Another Operation is in starting or processing or failed_temp");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(LcmOperationType.TERMINATE.toString() + " operation failed, Vnf operation is ongoing",
                        "There is another LCM operation running on this VNF instance, " + LcmOperationType.TERMINATE.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            break;
        case CHANGE_VNFPKG:
            if ((instantiatedState != null && instantiatedState.equals(InstantiatedState.NOT_INTANTIATED))) {
                logger.error("VnfRestServiceImpl : changeVnfPkg() : Vnf instance is not instantiated");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(
                        LcmOperationType.CHANGE_VNFPKG.toString() + " operation failed, Vnf not instantiated", "Vnf instance is not in instantiated state, "
                                + LcmOperationType.CHANGE_VNFPKG.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            if(isAnyLcmOperationRunning){
                logger.error("VnfRestServiceImpl : changeVnfPkg() : Another Operation is in starting or processing or failed_temp");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(LcmOperationType.CHANGE_VNFPKG.toString() + " operation failed, Vnf operation is ongoing",
                        "There is another LCM operation running on this VNF instance, " + LcmOperationType.CHANGE_VNFPKG.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            break;
        case MODIFY_INFO:
            if(isAnyLcmOperationRunning){
                logger.error("VnfRestServiceImpl : ModifyVnf() : Another Operation is in starting or processing");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(LcmOperationType.MODIFY_INFO.toString() + " operation failed,  Vnf operation is ongoing",
                        "There is another LCM operation running on this VNF instance, " + LcmOperationType.MODIFY_INFO.toString() + " operation cannot be performed.",
                        "", Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
            break;
        default:
            break;
        }
        recordRequestDuration(timer);
        return response;
    }
    /**
     * @param timer
     */
    private void recordRequestDuration(final Timer timer) {
        try {
            timer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : recordRequestDuration() : error in collecting request duration : {}", metricsException.toString());
        }
        MDC.clear();
    }

    /**
     * @param inputStream
     * @param uriInfo
     * @param response
     * @param userName
     * @param idempotencykey
     * @return
     */
    private Response processCreateVnfIfRetried(final InputStream inputStream,
            final UriInfo uriInfo, final String jwtToken, final String idempotencykey) {
        Response response = null;
        try {
            logger.info("VnfRestServiceImpl: CreateVnf() : Reading the nfvo configuration.");
            NfvoConfig nfvoConfigData = null;
            try {
                final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
                nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
            } catch (final NfvoCallException exception) {
                logger.warn("VnfRestServiceImpl: CreateVnf(), nfvo configuration not found. {}", exception.getMessage());
            }
            String solVersionSupported = OrVnfmVersion.SOL003V241.value();// Default case, if not provided in nfvoConfig
            if (nfvoConfigData != null && nfvoConfigData.getOrVnfmVersion() != null && !nfvoConfigData.getOrVnfmVersion().isEmpty()) {
                solVersionSupported = nfvoConfigData.getOrVnfmVersion();
            }
            RequestProcessingDetails requestProcessingDetails;
            requestProcessingDetails = requestProcessingDetailsService.getRequestProcessingDetails(idempotencykey);
            if (requestProcessingDetails == null )
                return null;
            logger.info("Create VNF Retried by Service Mesh");
            logger.debug("requestProcessingDetails:" + requestProcessingDetails);
            if (requestProcessingDetails != null && StringUtils.isEmpty(requestProcessingDetails.getProcessingState())) {
                if(requestProcessingDetails.getProcessingState().equalsIgnoreCase("CreateVnfComplete")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    VnfResponse vnfResponse = objectMapper.readValue(requestProcessingDetails.getResponseBody(), CreateVnfResponseDetails.class).getVnfResponse();
                    response = getCreateVnfResponse(vnfResponse, solVersionSupported);
                }
                else {
                    response = callServiceCreateVnf(inputStream, nfvoConfigData, uriInfo, jwtToken, idempotencykey, solVersionSupported);
                }
            }
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : processCreateVnfIfRetried() : error {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return response;
    }

    private Response getCreateVnfResponse(final VnfResponse vnfResponse, final String solVersionSupported)
            throws VNFLCMServiceException {
        String evnfmHostname = "";
        String lcmOccusUri = "";
        VnfInstance vnfInstance = null;
        Response response = null;
        try {
            evnfmHostname = fetchEvnfmHost();
            try {
                final String ingressHostName = ReadPropertiesUtility.readConfigProperty(Constants.VNF_CLUSTER_DNS);
                if (ingressHostName != null && !ingressHostName.isEmpty()) {
                    evnfmHostname = ingressHostName;
                }
            } catch (final Exception e) {
                // If ingress_hostname cannot be retrieved, then we create the links with evnfmHostname
                logger.info("ingressHostName could not be retrieved.Hence using evnfmHostname property");
            }
        } catch (final Exception e) {
            logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
        }
        if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
            final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl();
            vnfInstance = vnfRestServiceSol241Impl.createvnfInstanceResponse(vnfResponse);
            if (evnfmHostname != null && !evnfmHostname.isEmpty()) {
                lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.QUERY_VNF_BASE_URI + vnfInstance.getId();
                logger.info("lcmOccusUri {}", lcmOccusUri);
            }
            response = Response.status(Status.CREATED).header("Location", lcmOccusUri).entity(vnfInstance).build();
        } else {
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                    Constants.ACCEPTED);
            response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
        }
        return response;
    }

    private Response callServiceCreateVnf(final InputStream inputStream, final NfvoConfig nfvoConfigData,
            final UriInfo uriInfo, final String jwtToken, final String requestId, final String solVersionSupported) {
        try {
            Response response = null;
            final String incomingRequestData = readIncomingRequest(inputStream);
            final ObjectMapper objectMapper = new ObjectMapper();
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.CreateVnfRequest createVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.CreateVnfRequest.class);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vnfDescriptorService,
                        vimService, lcmAuthenticationService, requestProcessingDetailsService);
                response = vnfRestServiceSol241Impl.createVnf(createVnfRequest, uriInfo, nfvoConfigData,jwtToken, requestId);

                logger.info("VnfRestServiceImpl : createVnf() : model created.");
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }

            logger.info("VnfRestServiceImpl : createVnf() : model created.");
            return response;
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : callServiceInstaniateVnf() : error {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
    }

    /**
     * @param vnfInstanceId
     * @param inputStream
     * @param uriInfo
     * @param response
     * @param userName
     * @param idempotencykey
     * @return
     */
    private Response processInstantiateOperationIfRetried(final String vnfInstanceId, final InputStream inputStream,
            final UriInfo uriInfo, final String userName, final String idempotencykey) {
        Response response = null;
        try {
            RequestProcessingDetails requestProcessingDetails;
            requestProcessingDetails = requestProcessingDetailsService.getRequestProcessingDetails(idempotencykey);
            if (requestProcessingDetails == null )
                return null;
            logger.info("Instantiate VNF Retried by Service Mesh");
            logger.debug("requestProcessingDetails:" + requestProcessingDetails);
            if (requestProcessingDetails != null
                    && !StringUtils.isEmpty(requestProcessingDetails.getProcessingState())) {
                if (requestProcessingDetails.getProcessingState().equalsIgnoreCase("finalInitializeInstantiateVNF")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    final VnfInstantiateOpProcessingDetails vnfInstantiateOpProcessingDetails = objectMapper.readValue(
                            requestProcessingDetails.getResponseBody(), VnfInstantiateOpProcessingDetails.class);
                    final String vnfLcmOpId = vnfInstantiateOpProcessingDetails.getVnfLifeCycleOperationId();
                    response = getVnfLCMOperationAcceptedResponse(vnfLcmOpId);
                } else {
                    response = callServiceInstantiateVnf(vnfInstanceId, inputStream, uriInfo, userName, idempotencykey);
                }
            }
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : callServiceInstaniateVnf() : error {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return response;
    }

    private Response callServiceInstantiateVnf(final String vnfInstanceId, final InputStream inputStream,
            final UriInfo uriInfo, final String userName, final String idempotencykey) {
        try {
            Response response = null;
            final String incomingRequestData = readIncomingRequest(inputStream);
            final String solVersionSupported = getSolVersionSupported();
            final ObjectMapper objectMapper = new ObjectMapper();
            com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest instantiateVnfRequest = null;
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                instantiateVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest.class);
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiInstantiateVnfRequest instantiatedVnfRequest = com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VimConnectionHelper
                        .getInstantiatedVnfRequest(instantiateVnfRequest);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
               response = vnfRestServiceSol241Impl.instantiateVnf(vnfInstanceId, instantiatedVnfRequest, uriInfo, userName, idempotencykey);
            }
            return response;
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : callServiceInstaniateVnf() : error {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
    }

    private Response processScaleOperationIfRetried(String vnfInstanceId, InputStream inputStream, UriInfo uriInfo2,
            String userName, String idempotencykey) {
        Response response = null;
        try {
            RequestProcessingDetails requestProcessingDetails;
            requestProcessingDetails = requestProcessingDetailsService.getRequestProcessingDetails(idempotencykey);
            if (requestProcessingDetails == null )
                return null;
            logger.info("Scale VNF Operation Retried by Service Mesh");
            logger.debug("requestProcessingDetails:" + requestProcessingDetails);
            if (requestProcessingDetails != null && !StringUtils.isEmpty(requestProcessingDetails.getProcessingState())) {
                if(requestProcessingDetails.getProcessingState().equalsIgnoreCase("initializeScaleVNFCompleted")) {
                    logger.debug("Request Processing state:  {} - Hence returning response without calling service", requestProcessingDetails.getProcessingState());
                    ObjectMapper objectMapper = new ObjectMapper();
                    final VnfScaleOpProcessingDetails vnfScaleOpProcessingDetails = objectMapper.readValue(requestProcessingDetails.getResponseBody(), VnfScaleOpProcessingDetails.class);
                    final String vnfLcmOpId = vnfScaleOpProcessingDetails.getVnfLifeCycleOperationId();
                    response = getVnfLCMOperationAcceptedResponse(vnfLcmOpId);
                }
                else {
                    logger.debug("Request Processing state: {} Hence resuming from where it stopped", requestProcessingDetails.getProcessingState());
                    response = callServiceScaleVnf(vnfInstanceId, inputStream, uriInfo, userName, idempotencykey);
                }
            }
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : callServiceInstaniateVnf() : error {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return response;
    }

    private Response callServiceScaleVnf(String vnfInstanceId, InputStream inputStream, UriInfo uriInfo2,
            String userName, String idempotencykey) {
        try {
            Response response = null;
            final String incomingRequestData = readIncomingRequest(inputStream);
            final String solVersionSupported = getSolVersionSupported();
            final ObjectMapper objectMapper = new ObjectMapper();
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                logger.info("VnfRestServiceImpl : scaleVnf() : start creating models from incoming request.");
                final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ScaleVnfRequest scaleVnfRequest = objectMapper
                        .readValue(incomingRequestData, com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ScaleVnfRequest.class);
                final VnfRestServiceSol002V241Impl vnfRestServiceSol241Impl = new VnfRestServiceSol002V241Impl(vnfService, vimService);
                logger.debug("calling vnfRestServiceSol241Impl.scaleVnf ");
                response = vnfRestServiceSol241Impl.scaleVnf(vnfInstanceId, scaleVnfRequest, uriInfo, userName, idempotencykey);
            }
            return response;
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : callServiceInstaniateVnf() : error {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
    }

    /**
     * @param scaleVnfReqeustTimer
     */
    private void stopScaleVnfTimer(Summary.Timer scaleVnfReqeustTimer) {
        try {
            scaleVnfReqeustTimer.observeDuration();
        } catch (final Exception metricsException) {
            logger.error("VnfRestServiceImpl : scaleVnf() : error in collecting request duration : {}", metricsException.toString());
        }
    }

    private Response getVnfLCMOperationAcceptedResponse(final String vnfLcmOpId)
            throws VNFLCMServiceException {
        String lcmOccusUri = "";
        try {
            String evnfmHostname = fetchEvnfmHost();
            final String ingressHostName = ReadPropertiesUtility.readConfigProperty(Constants.VNF_CLUSTER_DNS);
            if (ingressHostName != null && !ingressHostName.isEmpty()) {
                evnfmHostname = ingressHostName;
            }
            if (evnfmHostname != null && !evnfmHostname.isEmpty()) {
                lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.LCM_OP_OCCR_BASE_URI + vnfLcmOpId;
                logger.info("lcmOccusUri {}", lcmOccusUri);
            }
            return Response.status(Status.ACCEPTED).header("Location", lcmOccusUri).build();
        } catch (final Exception ex) {
            logger.error("VnfRestServiceImpl : getVnfLCMOperationAcceptedResponse() : error {}", ex.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", ex.getMessage(), "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
    }

     /**
     * VNFLCM UI is loading very slow, and one the reason identified was that /wfs/rest/definitions api from WFS is taking long time
     * This api is taking time because of Camunda performance issue, the query caumnda genrates to get all the definitions takes longer time and so the WFS
     * From Vnflcm perspective, we decided to get the response of definitions and put that in cache, and whenever UI loads read the response from cache instead of wfs
     * Update this cache whenever bundle install/uninstall is happening
     */
    @Override
    public Response getWorkflowDefinitions(final String definitionName, final String bundleId, final String refreshCache) {
        logger.info("Received request to fetch the workflow definitions.");
        try {
            final WorkflowDefinitions workflowDefinitinos = wfbundleDesService.getWorkflowDefinitions(definitionName, bundleId, refreshCache);
            logger.debug("Response of wfs rest api is:{} : {}", workflowDefinitinos.getResponseCode(),workflowDefinitinos.getResponseBody());
            final Response response = Response.ok().entity(workflowDefinitinos.getResponseBody()).build();
            return response;
        } catch (final Exception e) {
            logger.error("Error when reading workflow definitinos from WFS:{}",e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}