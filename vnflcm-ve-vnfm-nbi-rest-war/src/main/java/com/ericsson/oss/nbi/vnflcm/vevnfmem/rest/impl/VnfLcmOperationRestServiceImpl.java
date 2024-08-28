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

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.NfvoConfigHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NfvoConfig;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfLcmOperationRestService;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper.VnfLcmOperationRestServiceSol002V241Impl;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.vnflcm.api_base.LcmOperationService;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperationResponse;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfResponse;
import com.ericsson.oss.services.vnflcm.common.dataTypes.ExceptionType;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OperationStatus;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OrVnfmVersion;
import com.ericsson.oss.services.vnflcmwfs.api.LcmWorkflowAuthenticationService;
import com.ericsson.oss.services.vnflcmwfs.api.exceptions.LcmWorkflowServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VnfLcmOperationRestServiceImpl implements VnfLcmOperationRestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VnfLcmOperationRestServiceImpl.class);

    private boolean isUnitTesting;

    @EServiceRef
    private LcmOperationService lcmOperationService;
    @Context
    private UriInfo uriInfo;

    @EServiceRef
    private VnfService vnfService;

    @EServiceRef
    private LcmWorkflowAuthenticationService lcmAuthenticationService;
    
    public VnfLcmOperationRestServiceImpl() {
	}
    
    public VnfLcmOperationRestServiceImpl(VnfService vnfService, LcmWorkflowAuthenticationService lcmAuthenticationService) {
    	this.vnfService = vnfService;
    	this.lcmAuthenticationService = lcmAuthenticationService;
    }

    @Override
    public Response getLcmOperationOcc(final String vnfLcmOpOccId) {
        LOGGER.info("Getting LcmOperationOcc from service for vnfLcmOpOccId : ", vnfLcmOpOccId);
        Response response = null;
        try {
            if (vnfLcmOpOccId == null) {
                LOGGER.error("Bad request from client recieved", this);
                final VNFLCMServiceException vnfex = new VNFLCMServiceException("Bad request from client recieved, operation id is null");
                vnfex.setType(ExceptionType.MANDATORY_PARAMETERS_MISSING);
                throw vnfex;
            }
            final String solVersionSupported = getSolVersionSupported();
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final VnfLcmOperationRestServiceSol002V241Impl vnfLcmOperationRestServiceSol003V241Impl = new VnfLcmOperationRestServiceSol002V241Impl(
                        lcmOperationService);
                response = vnfLcmOperationRestServiceSol003V241Impl.getLcmOperationOcc(vnfLcmOpOccId);
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking getLcmOperationOcc", e, this);
            throw new WebApplicationException(e);
        }
        return response;
    }

    @Override
    public Response getAllLcmOperationOcc(final String paginationFlag) {
        LOGGER.info("Getting all LcmOperationOcc from service ");
        Response response = null;
        final String solVersionSupported = getSolVersionSupported();
        if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
            final VnfLcmOperationRestServiceSol002V241Impl vnfLcmOperationRestServiceSol002V241Impl = new VnfLcmOperationRestServiceSol002V241Impl(
                    lcmOperationService);
            response = vnfLcmOperationRestServiceSol002V241Impl.getAllLcmOperationOcc(uriInfo, paginationFlag);
        } else {
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                    Constants.ACCEPTED);
            response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
        }
        return response;
    }

    private String getSolVersionSupported() {
        LOGGER.info("VnfLcmOperationRestServiceImpl: getSolVersionSupported() : Reading the nfvo configuration.");
        String solVersionSupported = OrVnfmVersion.SOL003V241.value(); // Default case, if not provided in nfvoConfig or unit test running
        if (isUnitTesting()) {
            return solVersionSupported;
        }
        NfvoConfig nfvoConfigData = null;
        try {
            final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
            nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
        } catch (final NfvoCallException exception) {
            LOGGER.warn("VnfLcmOperationRestServiceImpl: getSolVersionSupported(), nfvo configuration not found. {}", exception.getMessage());
        }
        if (nfvoConfigData != null && nfvoConfigData.getOrVnfmVersion() != null && !nfvoConfigData.getOrVnfmVersion().isEmpty()) {
            solVersionSupported = nfvoConfigData.getOrVnfmVersion();
        }
        return solVersionSupported;
    }

    @Override
    public Response retryOperation(final String vnfLcmOpOccId, @Context HttpHeaders headers) {
        LOGGER.info("VnflcmOperationRestServiceImpl()::Retry Operation for vnfLcmOpOccId {} ",vnfLcmOpOccId);
        Response response = null;
        try {
            final LcmOperationResponse lcmOperationResponse = (LcmOperationResponse) lcmOperationService
                    .getLcmOperation(vnfLcmOpOccId);
            response=getResponseForLicenceCheck(lcmOperationResponse);
            if (response!=null)
                return response;

            response = getResponseForDomainRoleAccessCheck(lcmOperationResponse.getVnfInstanceId(), headers);
            if (response!=null)
                return response;

            final OperationStatus opStatus = lcmOperationResponse.getOperationStatus();
            if (opStatus.value() == OperationStatus.FAILED_TEMP.value()) {
                lcmOperationService.recoverVnfLcmOperation("retry", vnfLcmOpOccId);
                response = Response.status(Status.ACCEPTED).build();
            } else {
                LOGGER.error(
                        "VnfLcmOperationRestServiceImpl : Operation State not in FAILED_TEMP, hence retry not possible");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.CONFLICT.toString(),
                        "Operation State not in FAILED_TEMP, " + "or recovery/retry is not possible at present",
                        vnfLcmOpOccId, Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
        } catch (final VNFLCMServiceException exception) {
            LOGGER.error("Exception occured while invoking retryOperation", exception);
            response = getResponseForException(exception, vnfLcmOpOccId, "get");
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking retryOperation", e, this);
            throw new WebApplicationException(e);
        }
        return response;
    }

    @Override
    public Response failOperation(final String vnfLcmOpOccId, @Context HttpHeaders headers) {
        LOGGER.info("VnflcmOperationRestServiceImpl()::Failing the Operation for vnfLcmOpOccId {} ",vnfLcmOpOccId);
        Response response = null;
        try {
            final LcmOperationResponse lcmOperationResponse = (LcmOperationResponse) lcmOperationService
                    .getLcmOperation(vnfLcmOpOccId);
            response=getResponseForLicenceCheck(lcmOperationResponse);
            if (response!=null)
                return response;

            response = getResponseForDomainRoleAccessCheck(lcmOperationResponse.getVnfInstanceId(), headers);
            if (response!=null)
                return response;

            final OperationStatus opStatus = lcmOperationResponse.getOperationStatus();
            if (opStatus.value() == OperationStatus.FAILED_TEMP.value()) {
                lcmOperationService.recoverVnfLcmOperation("fail", vnfLcmOpOccId);
                response = Response.status(Status.OK).build();
            } else {
                LOGGER.error("VnfLcmOperationRestServiceImpl : Operation State not in FAILED_TEMP");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.CONFLICT.toString(),
                        "Operation State not in FAILED_TEMP, or recovery/fail is not possible at present",
                        vnfLcmOpOccId, Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
        } catch (final VNFLCMServiceException exception) {
            LOGGER.error("Exception occured while invoking failOperation", exception);
            response = getResponseForException(exception, vnfLcmOpOccId, "get");
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking failOperation", e, this);
            throw new WebApplicationException(e);
        }
        return response;
    }

    @Override
    public Response rollbackOperation(final String vnfLcmOpOccId, @Context HttpHeaders headers) {
        LOGGER.info("VnflcmOperationRestServiceImpl()::Rollback the Operation  for vnfLcmOpOccId {} ",vnfLcmOpOccId);
        Response response = null;
        try {
            final LcmOperationResponse lcmOperationResponse = (LcmOperationResponse) lcmOperationService
                    .getLcmOperation(vnfLcmOpOccId);
            response=getResponseForLicenceCheck(lcmOperationResponse);
            if (response!=null)
                return response;

            response = getResponseForDomainRoleAccessCheck(lcmOperationResponse.getVnfInstanceId(), headers);
            if (response!=null)
                return response;

            final OperationStatus opStatus = lcmOperationResponse.getOperationStatus();
            if (opStatus.value() == OperationStatus.FAILED_TEMP.value()) {
                lcmOperationService.recoverVnfLcmOperation("rollback", vnfLcmOpOccId);
                response = Response.status(Status.ACCEPTED).build();
            } else {
                LOGGER.error("VnfLcmOperationRestServiceImpl : Operation State not in FAILED_TEMP");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.CONFLICT.toString(),
                        "Operation State not in FAILED_TEMP, or recovery/rollback is not possible at present",
                        vnfLcmOpOccId, Status.CONFLICT.toString(), Status.CONFLICT.getStatusCode());
                response = Response.status(Status.CONFLICT).entity(problemDetail).build();
            }
        } catch (final VNFLCMServiceException exception) {
            LOGGER.error("Exception occured while invoking rollbackOperation", exception);
            response = getResponseForException(exception, vnfLcmOpOccId, "get");
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking rollbackOperation", e, this);
            throw new WebApplicationException(e);
        }
        return response;
    }

    private Response getResponseForException(final VNFLCMServiceException vnflcmServiceException, final String vnfLcmOpOccId, final String operationType) {
        Response response = null;
        switch (vnflcmServiceException.getType()) {
            case LIFECYCLE_OP_NOT_FOUND:
                if ("get".equals(operationType)) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Vnflcm operation occurence not found", vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(),
                            Constants.NOT_FOUND);
                    response = Response.status(Status.NOT_FOUND).entity(problemDetail).build();
                }
                break;
            case ERROR_FETCHING_LIFECYCLEOP:
                final VeVnfmemProblemDetail problemdetail = new VeVnfmemProblemDetail("Error getting VNF Life Cycle Operation", vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(),
                        Constants.INTERNAL_ERROR);
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemdetail).build();
                break;
            case ERROR_PERFORMING_RECOVERY_ACTION:
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Recover action not permitted", vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(),
                        Constants.INTERNAL_ERROR);
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
                break;
            case ERROR_INVALID_INPUT:
                final VeVnfmemProblemDetail probdetail = new VeVnfmemProblemDetail("Required parameter missing.", vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(),
                        Constants.BAD_REQUEST);
                response = Response.status(Status.BAD_REQUEST).entity(probdetail).build();
                break;
            default:
                final VeVnfmemProblemDetail probDetail = new VeVnfmemProblemDetail("Internal Server Error occurred", vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(),
                        Constants.INTERNAL_ERROR);
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(probDetail).build();
                break;
        }
        return response;
    }

    private Response getResponseForLicenceCheck(final LcmOperationResponse lcmOperationResponse) {
        // TODO Auto-generated method stub
        boolean licenceCheck=false;
        try {
            VnfResponse vnfResponse;
            Boolean isENMIntegrationRequired = false;
            switch (lcmOperationResponse.getOperation()) {
            case INSTANTIATION:
                final ObjectMapper mapper = new ObjectMapper();
                final String lcmRequestJson = lcmOperationResponse.getLcmRequestJson();
                if (lcmRequestJson != null) {
                    final InstantiateVnfRequest instantiateVnfRequestObject = mapper.readValue(lcmRequestJson, InstantiateVnfRequest.class);
                    if (instantiateVnfRequestObject.getAdditionalParams()!=null && instantiateVnfRequestObject.getAdditionalParams().get("addVNFToOSS") !=null) {
                        LOGGER.debug("addVNFToOSS flag value: {}", instantiateVnfRequestObject.getAdditionalParams().get("addVNFToOSS").toString());
                        isENMIntegrationRequired = Boolean.parseBoolean(instantiateVnfRequestObject.getAdditionalParams().get("addVNFToOSS").toString());
                    }
                }
                licenceCheck = lcmAuthenticationService.checkLicense(lcmOperationResponse.getOperation().name(), isENMIntegrationRequired);
                LOGGER.debug("Licence Check for Operaion Type: {} is {}", lcmOperationResponse.getOperation().name(), licenceCheck);
                break;
            case TERMINATION:
                vnfResponse = vnfService.queryVnf(lcmOperationResponse.getVnfInstanceId());
                if (vnfResponse !=null && vnfResponse.getInstantiatedVnfInfo()!=null && vnfResponse.getInstantiatedVnfInfo().getFdn()!=null && !vnfResponse.getInstantiatedVnfInfo().getFdn().isEmpty() ) {
                    isENMIntegrationRequired= true;
                }
                licenceCheck = lcmAuthenticationService.checkLicense(lcmOperationResponse.getOperation().name(), isENMIntegrationRequired);
                LOGGER.debug("Licence Check for Operaion Type: {} is {}", lcmOperationResponse.getOperation().name(), licenceCheck);
                break;
            default:
                licenceCheck = lcmAuthenticationService.checkLicense(lcmOperationResponse.getOperation().name(), isENMIntegrationRequired);
                LOGGER.debug("Licence Check for Operaion Type: {} is {}", lcmOperationResponse.getOperation().name(), licenceCheck);
                break;
            }
            if (!licenceCheck) {
                LOGGER.error("User do not have sufficient Licence to perform "+lcmOperationResponse.getOperation().name()+" operation");
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("METHOD_NOT_ALLOWED", "You do not have sufficient Licence to perform "+lcmOperationResponse.getOperation().name()+" operation" , "",Status.METHOD_NOT_ALLOWED.toString(),
                        Status.METHOD_NOT_ALLOWED.getStatusCode());
                MDC.clear();
                return Response.status(Status.METHOD_NOT_ALLOWED).entity(problemDetail).build();
            }
        }
        catch (final LcmWorkflowServiceException e) {
            LOGGER.error("LCM Workflow Service exception while performing License check: " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "LCM Workflow Service exception while performing License check : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        catch (Exception e) {
            LOGGER.error("Exception while performing License check : " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "Exception while performing License check : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return null;
    }

    private Response getResponseForDomainRoleAccessCheck(final String vnfInstanceId, final HttpHeaders headers) {
        try {
            boolean isDracEnabled = lcmAuthenticationService.isDracEnabled();
            if(isDracEnabled) {
                final VnfResponse vnfResponse = vnfService.queryVnf(vnfInstanceId);
                if (vnfResponse!=null) {
                    final String jwtToken = getJwtToken(headers);
                    final boolean isUserAuthorized = lcmAuthenticationService.checkDomainRoleAccess(vnfResponse.getVnfProductName(), jwtToken);
                    if(!isUserAuthorized) {
                        LOGGER.error("User do not have sufficient Domain role to perform this operation on Node Type: "+ vnfResponse.getVnfProductName());
                        final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("UnAuthorized", "You do not have sufficient Domain role to perform this operation on Node Type: "+ vnfResponse.getVnfProductName() , "",Status.UNAUTHORIZED.toString(),
                                Status.UNAUTHORIZED.getStatusCode());
                        MDC.clear();
                        return Response.status(Status.UNAUTHORIZED).entity(problemDetail).build();
                    }
                }
            }
        }
        catch (final LcmWorkflowServiceException e) {
            LOGGER.error("LCM Workflow Service exception while performing Domain Role Access validation: " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "LCM Workflow Service exception while performing Domain Role Access validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        catch (Exception e) {
            LOGGER.error("Exception while performing Domain Role Access validation : " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "Exception while performing Domain Role Access validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            MDC.clear();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return null;
    }

    @Override
    public Response updateLcmOperationOccStatus(final String vnfLcmOpOccId) {
        LOGGER.info("Updating LcmOperationOcc from service for vnfLcmOpOccId : ", vnfLcmOpOccId);
        Response response = null;
        try {
            final String solVersionSupported = getSolVersionSupported();
            if (solVersionSupported.equalsIgnoreCase(OrVnfmVersion.SOL003V241.value())) {
                final VnfLcmOperationRestServiceSol002V241Impl vnfLcmOperationRestServiceSol002V241Impl = new VnfLcmOperationRestServiceSol002V241Impl(lcmOperationService);
                response = vnfLcmOperationRestServiceSol002V241Impl.updateLcmOperationOccStatus(vnfLcmOpOccId, uriInfo);
            } else {
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("For SOL002 only version 2.4.1 is supported.", "", "", "",
                        Constants.ACCEPTED);
                response = Response.status(Status.ACCEPTED).entity(problemDetail).build();
            }
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking updateLcmOperationOccStatus", e, this);
            throw new WebApplicationException(e);
        }
        return response;
    }

    private String getJwtToken(HttpHeaders headers) {
        // TODO Auto-generated method stub
        String jwtToken = null;
        final List<String> authorizationHeader = headers.getRequestHeader("Authorization");
        if (authorizationHeader!=null && !authorizationHeader.isEmpty() && authorizationHeader.get(0)!= null) {
           jwtToken = authorizationHeader.get(0);
        }
        return jwtToken;
    }

    public boolean isUnitTesting() {
        return isUnitTesting;
    }

    public void setUnitTesting(final boolean isUnitTesting) {
        this.isUnitTesting = isUnitTesting;
    }

    public void setLcmOperationService(final LcmOperationService vnfService) {
        this.lcmOperationService = vnfService;
    }
}
