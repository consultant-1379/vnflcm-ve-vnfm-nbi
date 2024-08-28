
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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper;

import static com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorMessages.INSTANCE_OPERATION_IN_PROGRESS_INSTANTIATE;
import static com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorMessages.VNF_ALREADY_INSTANTIATED;
import static com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorMessages.VNF_ID_NOT_FOUND;
import static com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorMessages.WORKFLOW_MAPPING_NOT_FOUND;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.util.StringUtils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.NfvoConfigHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.PackageManagementHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.QueryPackageHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services.NfvoCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.PackageManagementConstants;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NfvoConfig;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.ProcessDataDto;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.ResponseDTO;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.OnboardedVnfPkgInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.PackageManagementListenerData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.RequestValidator;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.AccessInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.CreateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.Credentials;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.HealVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiationState;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InterfaceInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ModifyVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.OperateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ScaleVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.TerminateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.TerminateVnfRequestType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimConnectionInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInfoModificationRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiChangeVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiInstantiateVnfRequest;
import com.ericsson.oss.services.vnflcm.api_base.RequestProcessingDetailsService;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorMessages;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.VeVnfmemErrorType;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VimService;
import com.ericsson.oss.services.vnflcm.api_base.VnfDescriptorService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;
import com.ericsson.oss.services.vnflcm.api_base.dto.ChangeCurrentVNFPackageDto;
import com.ericsson.oss.services.vnflcm.api_base.dto.ChangeCurrentVnfPackageOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.HealResponseDto;
import com.ericsson.oss.services.vnflcm.api_base.dto.ModifyVnf;
import com.ericsson.oss.services.vnflcm.api_base.dto.ModifyVnfOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.OperateVnfDto;
import com.ericsson.oss.services.vnflcm.api_base.dto.PackageManagementResponse;
import com.ericsson.oss.services.vnflcm.api_base.dto.ScaleAspectDto;
import com.ericsson.oss.services.vnflcm.api_base.dto.ScaleResponseDto;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfCreateDeleteOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfCreation;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfDescriptorDTO;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfDescriptorOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfHeal;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfHealOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfInstantiation;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfInstantiationOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfResponse;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfScale;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfScaleOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.model.TerminateVNFRequestTerminationType;
import com.ericsson.oss.services.vnflcm.api_base.model.Vim;
import com.ericsson.oss.services.vnflcm.api_base.model.VimSubTenant;
import com.ericsson.oss.services.vnflcm.api_base.model.VimTenant;
import com.ericsson.oss.services.vnflcm.api_base.model.CreateVnfResponseDetails;
import com.ericsson.oss.services.vnflcm.api_base.model.RequestProcessingDetails;
import com.ericsson.oss.services.vnflcm.common.dataTypes.ExceptionType;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OperationType;
import com.ericsson.oss.services.vnflcm.common.dataTypes.PackageManagementRequestTypes;
import com.ericsson.oss.services.vnflcm.common.dataTypes.RequestSourceType;
import com.ericsson.oss.services.vnflcm.common.dataTypes.VnfOperationalStateType;
import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;
import com.ericsson.oss.services.vnflcm.file.util.FileHandler;
import com.ericsson.oss.services.vnflcmwfs.api.LcmWorkflowAuthenticationService;
import com.ericsson.oss.services.vnflcmwfs.api.exceptions.LcmWorkflowServiceException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VnfRestServiceSol002V241Impl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String VNF_INSTANCE_ID_NULL = "VNF Instance ID cannot be empty";
    public static final String NUMBER_OF_STEPS_NEGATIVE = "Number of Steps cannot be negative";
    public static final String TYPE_VALUE_INVALID = "type: Invalid value. Permitted value(s) are: [ SCALE_OUT, SCALE_IN ]";
    public static final String ONBOARDED_PACKAGE_PATH = "vnf.hotpackage.onboarding.path";
    public static final String VNFD_DIRECTORY_ERROR_MESSAGE = "Vnfd directory for vnfdId provided does not exists.";

    private VnfService vnfService;

    private VimService vimService;

    private VnfDescriptorService vnfDescriptorService;
    
    private LcmWorkflowAuthenticationService lcmAuthenticationService;

    private RequestProcessingDetailsService requestProcessingDetailsService;

    public VnfRestServiceSol002V241Impl() {
    }

    public VnfRestServiceSol002V241Impl(final VnfService vnfService, final VimService vimService) {
        this.vnfService = vnfService;
        this.vimService = vimService;
    }

    public VnfRestServiceSol002V241Impl(final VnfService vnfService, final VnfDescriptorService vnfDescriptorService, final VimService vimService) {
        this.vnfService = vnfService;
        this.vnfDescriptorService = vnfDescriptorService;
        this.vimService = vimService;
    }

    public VnfRestServiceSol002V241Impl(VnfService vnfService, VnfDescriptorService vnfDescriptorService,
            VimService vimService, LcmWorkflowAuthenticationService lcmAuthenticationService, final RequestProcessingDetailsService requestProcessingDetailsService) {
        this.vnfService = vnfService;
        this.vnfDescriptorService = vnfDescriptorService;
        this.vimService = vimService;
        this.lcmAuthenticationService = lcmAuthenticationService;
        this.requestProcessingDetailsService = requestProcessingDetailsService;
    }

    public Response createVnf(final CreateVnfRequest createVnfRequest, @Context final UriInfo uriInfo, final NfvoConfig nfvoConfigData, String jwtToken, final String requestId) {
        logger.info("-- Starting createVnfRequest --");

        final RequestValidator<CreateVnfRequest> reqValidationHelper = new RequestValidator<CreateVnfRequest>();
        reqValidationHelper.validate(createVnfRequest);
        VnfInstance vnfInstance = null;
        String evnfmHostname = "";
        String lcmOccusUri = "";
        String onboardPackageId = null;
        boolean isPackageCreated = true;
        boolean isPkgDownloadRetried = false;
        logger.info("Starting  create VNF Descriptor in NBI ");
        try {
            createVnfDescriptor(createVnfRequest);
        } catch (final VNFLCMServiceException e) {
            logger.info("Could not create Descriptor with minimum required input vnfdId.");
            e.setType(ExceptionType.ERROR_CREATING_VNFDECRIPTOR);
            return getResponseForException(e);
        } catch (final Exception exception) {
            final String message = exception.getMessage();
            logger.error("Could not create Descriptor with minimum required input vnfdId." + message);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", message, "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        if (nfvoConfigData != null) {
            final String isNfvoAvailiable = nfvoConfigData.getIsNfvoAvailable();
            try {
                if (isNfvoAvailiable.equalsIgnoreCase("true") && null != nfvoConfigData.getPackageManagementUrl()
                        && "" != nfvoConfigData.getPackageManagementUrl().intern()) {
                    VnfDescriptorDTO vnfDescriptorDTO = queryVnfPackage(createVnfRequest);
                    if (vnfDescriptorDTO != null) {
                        onboardPackageId = vnfDescriptorDTO.getOnboardedPackageInfoId();
                    }
                    logger.info("Query Package from nfvo is done ...");
                    RequestProcessingDetails requestProcessingDetails = requestProcessingDetailsService.getRequestProcessingDetails(requestId);
                    logger.info("requestProcessingDetails:" + requestProcessingDetails);
                    if (requestProcessingDetails == null || requestProcessingDetails.getProcessingState() == null || requestProcessingDetails.getProcessingState().isEmpty()) {
                        requestProcessingDetails = new RequestProcessingDetails();
                        requestProcessingDetails.setRequestId(requestId);
                        requestProcessingDetails.setRequestHash(requestId);
                        requestProcessingDetails.setProcessingState("InitialCreateStage");
                        final Date date = new Date();
                        requestProcessingDetails.setCreationTime(new Timestamp(date.getTime()));
                        requestProcessingDetailsService.storeRequestProcessingDetails(requestProcessingDetails);
                    } else {
                        isPkgDownloadRetried = true;
                    }
                    if (requestProcessingDetails != null && requestProcessingDetails.getProcessingState() != null && !requestProcessingDetails.getProcessingState().isEmpty()
                        && (requestProcessingDetails.getProcessingState().equals("PkgOnBoardStage") || requestProcessingDetails.getProcessingState().equals("InitialCreateStage"))) {
                        logger.info("Package on board stage start");
                        isPackageCreated = onboardPackageFromNfvo(createVnfRequest, nfvoConfigData, vnfDescriptorDTO, requestId, isPkgDownloadRetried);
                        logger.info("Package on board stage End");
                    }
                } else {
                    logger.info("Manual package structure required to be created as input provided is not sufficient for Auto package Management.");
                }
            } catch (final VNFLCMServiceException ex) {
                logger.error("Exception occured on Package Management, please try again or download it manually. {}", ex);
            } catch (final Exception exception) {
                logger.error("Exception occured on Package Management, please try again or download it manually. {}", exception.getMessage());
            }
        }
        // If nfvo not found let allow the Creation of vnf, request can come from any
        // source other than nfvo Because we are exposing RESET endpoints which can be
        // called from any external client
        try {
            final VnfCreation vnfCreation = this.createVnfCreationRequest(createVnfRequest, onboardPackageId);
            final String vnfdId = vnfCreation.getVnfdId();
            final String pathToBeDeleted = PackageManagementConstants.BASE_PATH_TO_BE_CREATED + vnfdId + "/";
            final File filetodelete = new File(pathToBeDeleted);
            if (!isPackageCreated) {
                logger.error("Package artifacts have some problem");
                if (filetodelete.exists()) {
                    logger.info("Proceeding to delete package with vnfdid {}", vnfdId);
                    deletePackage(pathToBeDeleted);
                }
                final VNFLCMServiceException vnfex = new VNFLCMServiceException("Package is partially downloaded");
                vnfex.setType(ExceptionType.PACKAGE_PARTIAL_DOWNLOADED);
                return getResponseForException(vnfex);
            } else {
                final String vnfdWrapperContent = FileHandler.getVnfWrapperContent(createVnfRequest.getVnfdId());
                if (vnfdWrapperContent == null) {
                    final VNFLCMServiceException vnfex = new VNFLCMServiceException("Unable to read Wrapper file.");
                    throw vnfex;
                } else {
                    setVnfdWrapperData(vnfCreation, vnfdWrapperContent);
                }
                //DRAC Validation
                final Response dracResponse = getResponseForDomainRoleAccessCheck(vnfCreation.getVnfProductName(), jwtToken);
                if (dracResponse!=null)
                    return dracResponse;
                validateCreateVnfMandatoryParameters(vnfCreation);
                final VnfCreateDeleteOpConfig vnfCreationConfig = new VnfCreateDeleteOpConfig();
                vnfCreationConfig.setEMDriven(true);
                vnfCreationConfig.setNotificationSendByWF(false);
                vnfCreationConfig.setCorrelationId("EM");
                vnfCreationConfig.setRequestId(requestId);
                RequestProcessingDetails requestProcessingDetails = requestProcessingDetailsService.getRequestProcessingDetails(requestId);
                VnfResponse vnfResponse = null;
                if (requestProcessingDetails != null && requestProcessingDetails.getProcessingState().equals("CreateVnfComplete")) {
                    final ObjectMapper objectMapper = new ObjectMapper();
                    vnfResponse = objectMapper.readValue(requestProcessingDetails.getResponseBody(), CreateVnfResponseDetails.class).getVnfResponse();
                    logger.info("VnfResponse from the request processing details table {}", vnfResponse.toString());
                } else if (requestProcessingDetails != null && requestProcessingDetails.getProcessingState().equals("SendNotificationStage")) {
                    vnfResponse = vnfService.createVnf(vnfCreation, vnfCreationConfig);
                } else {
                    if (vnfCreation.getVnfName() != null && !vnfCreation.getVnfName().isEmpty()) {
                        validateVnfInstanceName(vnfCreation.getVnfName());
                    }
                    vnfResponse = vnfService.createVnf(vnfCreation, vnfCreationConfig);
                }
                vnfInstance = this.createvnfInstanceResponse(vnfResponse);
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
                        lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.QUERY_VNF_BASE_URI + vnfInstance.getId();
                        logger.info("lcmOccusUri {}", lcmOccusUri);
                    }
                } catch (final Exception e) {
                    logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
                }
            }
        } catch (final VNFLCMServiceException ex) {
            logger.error("Exception occured while invoking create vnf.", ex);
            return getResponseForException(ex);
        } catch (final Exception exception) {
            String message = exception.getMessage();
            if (exception.getMessage().contains("PersistenceException")) {
                message = "Could not open database connection.";
            }
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", message, "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        return Response.status(Status.CREATED).header("Location", lcmOccusUri).entity(vnfInstance).build();
    }

    private Response getResponseForDomainRoleAccessCheck(String vnfProductName, String jwtToken) {
        try {
            boolean isDracEnabled = lcmAuthenticationService.isDracEnabled();
            if(isDracEnabled) {
                logger.info("is Drac enabled - {}", isDracEnabled);
                final boolean isUserAuthorized = lcmAuthenticationService.checkDomainRoleAccess(vnfProductName, jwtToken);
                if(!isUserAuthorized) {
                    logger.error("User do not have sufficient Domain role to perform this operation on Node Type: "+ vnfProductName);
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("UnAuthorized", "You do not have sufficient Domain role to perform this operation on Node Type: "+ vnfProductName , "",Status.UNAUTHORIZED.toString(),
                            Status.UNAUTHORIZED.getStatusCode());
                    return Response.status(Status.UNAUTHORIZED).entity(problemDetail).build();
                }
            }
        }
        catch (final LcmWorkflowServiceException e) {
            logger.error("LCM Workflow Service exception while performing Domain Role Access validation: " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "LCM Workflow Service exception while performing Domain Role Access validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        catch (Exception e) {
            logger.error("Exception while performing Domain Role Access validation : " + e.getMessage(), e);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error",
                    "Exception while performing Domain Role Access validation : " + e.getMessage(), "", Status.INTERNAL_SERVER_ERROR.toString(),
                    Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return null;
    }

    private boolean onboardPackageFromNfvo(final CreateVnfRequest createVnfRequest, final NfvoConfig nfvoConfigData, final VnfDescriptorDTO vnfDescriptorDTO, final String requestId, final boolean pkgDownloadRetried) throws VNFLCMServiceException {
        boolean isPackageCreated = true;
        try {
            final boolean isPackageStructureExist = isPackageAlreadyExist(createVnfRequest.getVnfdId());
            final String isPackageDownloadRequired = isPackageDownloadRequired();
            logger.info("isPackageDownloadRequired {}", isPackageDownloadRequired);
            final VnfDescriptorDTO fetchedVnfDescriptorDTO = vnfDescriptorService.getVnfDescriptor(vnfDescriptorDTO.getVnfdId());
            logger.info("Fetched Vnf Descriptor : " + fetchedVnfDescriptorDTO.toString());
            if (isPackageStructureExist && fetchedVnfDescriptorDTO.getOnboardedPackageInfoId() != null) {
                logger.info("package structure is already exist.");
            } else if (isPackageDownloadRequired.equalsIgnoreCase("YES") && !isPackageStructureExist) {
                isPackageCreated = createVnfPackage(createVnfRequest.getVnfdId(), nfvoConfigData, vnfDescriptorDTO.getOnboardedPackageInfoId());
                logger.info(" is Auto configure packageManagement from NBI done TRUE/FALSE.... {} ", isPackageCreated);
                if (isPackageCreated) {
                    final VnfDescriptorOpConfig vnfDescriptorOpConfig = new VnfDescriptorOpConfig();
                    vnfDescriptorService.updateVnfDescriptor(vnfDescriptorDTO, vnfDescriptorOpConfig);
                }
            } else if (isPackageDownloadRequired.equalsIgnoreCase("YES") && isPackageStructureExist && fetchedVnfDescriptorDTO.getOnboardedPackageInfoId() == null) {
                final String pathToBeDeleted = PackageManagementConstants.BASE_PATH_TO_BE_CREATED + createVnfRequest.getVnfdId() + "/";
                final File filetodelete = new File(pathToBeDeleted);
                if (filetodelete.exists() && pkgDownloadRetried) {
                    logger.error("Package artifacts have some problem");
                    logger.info("Proceeding to delete package with vnfdid {}", createVnfRequest.getVnfdId());
                    deletePackage(pathToBeDeleted);
                    isPackageCreated = createVnfPackage(createVnfRequest.getVnfdId(), nfvoConfigData, vnfDescriptorDTO.getOnboardedPackageInfoId());
                    logger.info(" is Auto configure packageManagement from NBI done TRUE/FALSE.... {} ", isPackageCreated);
                }
                if (isPackageCreated) {
                    final VnfDescriptorOpConfig vnfDescriptorOpConfig = new VnfDescriptorOpConfig();
                    vnfDescriptorService.updateVnfDescriptor(vnfDescriptorDTO, vnfDescriptorOpConfig);
                }
            } else if (isPackageDownloadRequired.equalsIgnoreCase("NO") && isPackageStructureExist && fetchedVnfDescriptorDTO.getOnboardedPackageInfoId() == null) {
                logger.info("package download property is not set as Yes and package is already exist... ");
                final VnfDescriptorOpConfig vnfDescriptorOpConfig = new VnfDescriptorOpConfig();
                vnfDescriptorService.updateVnfDescriptor(vnfDescriptorDTO, vnfDescriptorOpConfig);
            } else {
                logger.info(" Either package download property is not set as Yes or package is already exist... ");
            }
        } catch (final VNFLCMServiceException ex) {
            logger.error("Exception occured on Package Management, please try again or download it manually. {}", ex);
        } catch (final Exception exception) {
            logger.error("Exception occured on Package Management, please try again or download it manually. {}", exception.getMessage());
        }
        RequestProcessingDetails requestProcessingDetails = new RequestProcessingDetails();
        requestProcessingDetails.setRequestId(requestId);
        requestProcessingDetails.setRequestHash(requestId);
        if (!isPackageCreated) {
            requestProcessingDetails.setProcessingState("PkgOnBoardStage");
        } else {
            requestProcessingDetails.setProcessingState("ServiceCreateVnfStage");
        }
        final Date date = new Date();
        requestProcessingDetails.setCreationTime(new Timestamp(date.getTime()));
        requestProcessingDetailsService.storeRequestProcessingDetails(requestProcessingDetails);
        return isPackageCreated;
    }

    /**
     * This method is to read the package download value from Env. if this value is YES then auto package download will work,
     *
     * @throws Exception
     */
    private String isPackageDownloadRequired() {
        String isPackageDownloadEnabled = null;
        try {
            isPackageDownloadEnabled = ReadPropertiesUtility.readConfigProperty(Constants.PACKAGE_DOWNLOAD);
        } catch (final Exception e) {
            logger.error("readPackageDownloadFromEnv() : Error in reading package download from environment {}", e.getMessage());
        }
        return isPackageDownloadEnabled;
    }

    /**
     * This method is to create the descriptor with basic input vnfdId as query package required to update the onboarded id corresponding to vnfdId ,
     * the case where vnfdId not exist it will create one.
     *
     * @param createVnfRequest
     * @throws NfvoCallException
     * @throws VNFLCMServiceException
     */
    private void createVnfDescriptor(final CreateVnfRequest createVnfRequest) throws NfvoCallException, VNFLCMServiceException, Exception {
        final VnfDescriptorDTO vnfDescriptorDTO = populateVnfDescriptor(createVnfRequest);
        vnfDescriptorService.createVnfDescriptor(vnfDescriptorDTO, null);
    }

    private VnfDescriptorDTO populateVnfDescriptor(final CreateVnfRequest createVnfRequest) {
        final VnfDescriptorDTO vnfDescriptorDTO = new VnfDescriptorDTO();
        vnfDescriptorDTO.setVnfdId(createVnfRequest.getVnfdId());
        return vnfDescriptorDTO;
    }

    /**
     * @param vnfdId
     * @throws NfvoCallException
     * @throws VNFLCMServiceException
     */
    private boolean createVnfPackage(final String vnfdId, final NfvoConfig nfvoConfigData, final String vnfPkgId) throws NfvoCallException, VNFLCMServiceException, Exception {
        boolean isPackageCreated = false;
        final String pathOfPackage = PackageManagementConstants.BASE_PATH_TO_BE_CREATED + vnfdId + "/";
        final File filetodelete = new File(pathOfPackage);
        logger.info("Starting createVnfPackage...");
        try {
            PackageManagementResponse packageManagementResponse = new PackageManagementResponse();
            packageManagementResponse = createPackage(vnfdId, nfvoConfigData, vnfPkgId);
            if (200 == packageManagementResponse.getResponseCode()) {
                isPackageCreated = true;
                logger.info("Fetched artifacts for NBI Request. Server responded with response code. Message is {}",
                        packageManagementResponse.getResponseCode());
            } else if (packageManagementResponse.getResponseCode() == PackageManagementConstants.DOWNLOAD_FAILED_CODE && filetodelete.exists()) {
                isPackageCreated = false;
                logger.info("Fetched artifacts partially for NBI Request. Server responded with response code. Message is {}",
                        packageManagementResponse.getResponseCode());
            } else {
                isPackageCreated = false;
                logger.error("Artifacts is not fetched for NBI Request . Server responded with response code. Message is {}",
                        packageManagementResponse.getResponseCode());
                throw new VNFLCMServiceException("Artifacts is not fetched during NBI Request . Server responded with response code. Message is {} "
                        + packageManagementResponse.getDetails());
            }
        } catch (final VNFLCMServiceException ex) {
            logger.error("Exception occured while invoking create vnf.", ex);
            throw ex;
        } catch (final Exception e) {
            logger.error("Exception occured while invoking create vnf.", e);
            throw e;
        }
        return isPackageCreated;
    }

    /**
     * @param vnfdId
     * @throws com.ericsson.oss.services.vnflcm.api.VNFLCMServiceException
     */
    private PackageManagementResponse createPackage(final String vnfdId, final NfvoConfig nfvoConfigData, final String vnfPkgId) throws VNFLCMServiceException {
        logger.info("VnfRestServiceImpl createPackage... ");
        ResponseDTO result = null;
        NfvoCallExecution vevnfmemPackageManagement = null;
        final PackageManagementHelper packageManagementHelper = new PackageManagementHelper();
        PackageManagementResponse packageManagementResponse = new PackageManagementResponse();
        final ProcessDataDto processData = new ProcessDataDto();
        final PackageManagementListenerData processPackageManagementData = populatePackageManagementData(vnfdId, vnfPkgId);
        processPackageManagementData.setRequestType(PackageManagementRequestTypes.PACKAGE_DOWNLOAD_REQUEST.value());
        processData.setPackageManagementListenerData(processPackageManagementData);
        processData.setNfvoConfigData(nfvoConfigData);
        final List<NfvoConfig> emConfigData = new ArrayList<NfvoConfig>();
        emConfigData.add(nfvoConfigData);
        processData.setEmConfigData(emConfigData);
        logger.info("vnfdId is {}, OnboardedVnfPkgInfoId is {} ", processPackageManagementData.getVnfdId(),
                processData.getPackageManagementListenerData(), this);
        vevnfmemPackageManagement = packageManagementHelper.prepareExecutionData();
        result = vevnfmemPackageManagement.processExecution(processData);
        if (null != result) {
            logger.info(" VnfRestServiceImpl.createPackage result {} ", result);
            packageManagementResponse = createPackageManagementResponseModel(result);
            logger.info(" VnfRestServiceImpl.createPackage packageManagementResponse {} ", packageManagementResponse);
        }
        return packageManagementResponse;
    }

    /**
     * @param result
     * @return
     */
    private PackageManagementResponse createPackageManagementResponseModel(final ResponseDTO result) {
        final PackageManagementResponse packageManagementResponse = new PackageManagementResponse();
        packageManagementResponse.setDetails(result.getDetails());
        packageManagementResponse.setStatus(result.getStatus());
        packageManagementResponse.setResponseCode(result.getResponseCode());
        return packageManagementResponse;
    }

    /**
     * @param packageManagementDto
     * @return
     * @throws VNFLCMServiceException
     * @throws Exception
     */
    private boolean isPackageAlreadyExist(final String vnfdId) throws VNFLCMServiceException {
        try {
            logger.info("Validating  is package exist .... ");
            if (null != vnfdId && !vnfdId.isEmpty()) {
                final String pathToBeCreated = PackageManagementConstants.BASE_PATH_TO_BE_CREATED + vnfdId + "/";
                final File file = new File(pathToBeCreated);
                if (file.exists()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (final Exception ex) {
            logger.error("Error vnfd directory path not exist.", ex);
            final VNFLCMServiceException vnfex = new VNFLCMServiceException("Error vnfd directory path not exist.", ex);
            vnfex.setType(ExceptionType.ERROR_VNFD_DIRECTORY_PATH_NOT_EXIST);
            throw vnfex;
        }
        return false;
    }

    /**
     * @param createVnfRequest
     * @throws NfvoCallException
     * @throws VNFLCMServiceException
     * @return vnfDescriptorDTO
     */
    private VnfDescriptorDTO queryVnfPackage(final CreateVnfRequest createVnfRequest) throws NfvoCallException, VNFLCMServiceException, Exception {
        String onboardPackageId = null;
        VnfDescriptorDTO vnfDescriptorDTO = null;
        logger.info("Starting queryVnfPackage...");
        try {
            final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
            NfvoConfig nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
            String vnfPkgId = getVnfPkgIdFromCreateVnfRequest(createVnfRequest);
            vnfDescriptorDTO = fetchQueryPackage(createVnfRequest.getVnfdId(),nfvoConfigData,vnfPkgId);
            if (null != vnfDescriptorDTO) {
                onboardPackageId = vnfDescriptorDTO.getOnboardedPackageInfoId();
            } else {
                logger.info("query package could not fetch onboarded package Id for vnfdId.");
                final VNFLCMServiceException vnfex = new VNFLCMServiceException("Exception occured while fetching Query package.");
                vnfex.setType(ExceptionType.ERROR_FETCHING_ONBORDED_PACKAGE_INFO_ID);
                throw vnfex;
            }
        } catch (final VNFLCMServiceException ex) {
            logger.error("Exception occured while invoking query vnf package.", ex);
            ex.setType(ExceptionType.ERROR_FETCHING_ONBORDED_PACKAGE_INFO_ID);
            throw ex;
        } catch (final Exception e) {
            logger.error("Exception occured while invoking query vnf package.", e);
            throw e;
        }
        return vnfDescriptorDTO;
    }

    /**
     * This method reads the VNF Package ID from Create VNF request.
     * EO-CM sends the vnfPkgId attribute in additional parameters. Additional parameters
     * are proprietary within EO-CM/Vnflcm, 3PP nfvo will not sent it.
     * @param createVnfRequest
     * @return
     */
    private String getVnfPkgIdFromCreateVnfRequest(final CreateVnfRequest createVnfRequest) {
        if (createVnfRequest.getAdditionalParams() != null) {
            if (createVnfRequest.getAdditionalParams().getVnfPkgId() != null) {
                return createVnfRequest.getAdditionalParams().getVnfPkgId().trim();
            } else if (createVnfRequest.getAdditionalParams().getOnboardedVnfPkgInfoId() != null) {
                return createVnfRequest.getAdditionalParams().getOnboardedVnfPkgInfoId().trim();
            }
        }
        return null;
    }
    
    /**
     * @param createVnfRequest
     * @throws VNFLCMServiceException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    private VnfDescriptorDTO fetchQueryPackage(final String vnfdId, final NfvoConfig nfvoConfigData, final String vnfPkgId)
            throws VNFLCMServiceException, JsonMappingException, JsonParseException {
        logger.info("VnfRestServiceImpl fetchOnboardedPackage.... ");
        ResponseDTO result = null;
        NfvoCallExecution vevnfmemQueryPackage = null;
        final QueryPackageHelper queryPackageHelper = new QueryPackageHelper();
        VnfDescriptorDTO vnfDescriptorDTO = new VnfDescriptorDTO();
        final ProcessDataDto processData = new ProcessDataDto();
        try {
            vnfDescriptorDTO.setVnfdId(vnfdId);
            final PackageManagementListenerData processPackageManagementData = populatePackageManagementData(vnfdId,
                    vnfPkgId);
            processPackageManagementData.setRequestType(PackageManagementRequestTypes.QUERY_PACKAGE_REQUEST.value());
            processData.setPackageManagementListenerData(processPackageManagementData);
            processData.setNfvoConfigData(nfvoConfigData);
            logger.info("vnfdId is {}, PackageManagementListenerData is {} ", processPackageManagementData.getVnfdId(),
                    processData.getPackageManagementListenerData(), this);
            vevnfmemQueryPackage = queryPackageHelper.prepareExecutionData();
            result = vevnfmemQueryPackage.processExecution(processData);
            if (null != result && null != result.getVnfPkgInfo()) {
                try {
                    logger.info(" VnfRestServiceImpl.fetchQueryPackage result {} ", result);
                    vnfDescriptorDTO = createVnfDescriptorDto(result.getVnfPkgInfo(), vnfDescriptorDTO);
                    logger.info(" VnfRestServiceImpl.fetchQueryPackage queryPackageResponse {} ", vnfDescriptorDTO);
                } catch (final Exception e) {
                    result = new ResponseDTO();
                    result.setDetails(e.getMessage());
                    result.setStatus(false);
                    logger.error("Exception while processing query package for vnfdId {} ",
                            processData.getPackageManagementListenerData().getVnfdId(), e);
                }
            }
        } catch (final VNFLCMServiceException vnfex) {
            logger.info("query package could not fetch onboarded package Id for vnfdId.");
            vnfex.setType(ExceptionType.ERROR_FETCHING_ONBORDED_PACKAGE_INFO_ID);
            throw vnfex;
        }
        return vnfDescriptorDTO;
    }

    /**
     * @param result
     * @return
     *
     */
    private VnfDescriptorDTO createVnfDescriptorDto(final OnboardedVnfPkgInfo onboardedVnfPkgInfo, final VnfDescriptorDTO vnfDescriptorDTO) {
        if (onboardedVnfPkgInfo.getId() != "") {
            vnfDescriptorDTO.setOnboardedPackageInfoId(onboardedVnfPkgInfo.getId());
        }
        if (onboardedVnfPkgInfo.getVnfdId() != "") {
            vnfDescriptorDTO.setVnfdId(onboardedVnfPkgInfo.getVnfdId());
        }
        if (onboardedVnfPkgInfo.getVnfdVersion() != "") {
            vnfDescriptorDTO.setVnfdversion(onboardedVnfPkgInfo.getVnfdVersion());
        }
        if (onboardedVnfPkgInfo.getVnfSoftwareVersion() != "") {
            vnfDescriptorDTO.setVnfSoftwareVersion(onboardedVnfPkgInfo.getVnfSoftwareVersion());
        }
        return vnfDescriptorDTO;
    }

    /**
     * @throws VNFLCMServiceException
     *
     */
    private void validateVnfInstanceName(final String vnfInstanceName) throws VNFLCMServiceException {
        logger.info("Vnf instance name is : " + vnfInstanceName);
        final VnfResponse vnfResponse = vnfService.getVnfByName(vnfInstanceName);
        if (vnfResponse != null) {
            final VNFLCMServiceException vnfex = new VNFLCMServiceException("Duplicate vnf name found in DB .");
            vnfex.setType(ExceptionType.ERROR_CREATING_VNF);
            throw vnfex;
        }
    }

    /**
     * @param vnfCreation
     * @throws VNFLCMServiceException
     */
    private void validateCreateVnfMandatoryParameters(final VnfCreation vnfCreation) throws VNFLCMServiceException {
        if (vnfCreation.getVnfProductName() == null || vnfCreation.getVnfProductName().equals("null") || vnfCreation.getVnfProductName().isEmpty()) {
            final VNFLCMServiceException vnfex = new VNFLCMServiceException("Mandatory parameter vnf product name missing .");
            vnfex.setType(ExceptionType.ERROR_CREATING_VNF);
            throw vnfex;
        }
        if (vnfCreation.getVnfSoftwareVersion() == null || vnfCreation.getVnfSoftwareVersion().equals("null")
                || vnfCreation.getVnfSoftwareVersion().isEmpty()) {
            final VNFLCMServiceException vnfex = new VNFLCMServiceException("Mandatory parameter vnf software version missing .");
            vnfex.setType(ExceptionType.ERROR_CREATING_VNF);
            throw vnfex;
        }
        if (vnfCreation.getVnfdVersion() == null || vnfCreation.getVnfdVersion().equals("null") || vnfCreation.getVnfdVersion().isEmpty()) {
            final VNFLCMServiceException vnfex = new VNFLCMServiceException("Mandatory parameter vnfd version missing .");
            vnfex.setType(ExceptionType.ERROR_CREATING_VNF);
            throw vnfex;
        }
        if (vnfCreation.getVnfProvider() == null || vnfCreation.getVnfProvider().equals("null") || vnfCreation.getVnfProvider().isEmpty()) {
            final VNFLCMServiceException vnfex = new VNFLCMServiceException("Mandatory parameter Vnf provider missing .");
            vnfex.setType(ExceptionType.ERROR_CREATING_VNF);
            throw vnfex;
        }
    }

    /**
     * @param vnfCreation
     * @param vnfdWrapperContent
     * @throws VNFLCMServiceException
     */
    private void setVnfdWrapperData(final VnfCreation vnfCreation, final String vnfdWrapperContent) throws VNFLCMServiceException {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            final JsonNode rootNode = objectMapper.readTree(vnfdWrapperContent);
            final JsonNode dataVNFDSpecificNode = rootNode.get(Constants.DATA_VNFD_SPECIFIC);
            if (dataVNFDSpecificNode != null) {
                final String vnfProductName = dataVNFDSpecificNode.path(Constants.VNF_PRODUCT_NAME).asText();
                vnfCreation.setVnfProductName(vnfProductName);
                final String vnfProvider = dataVNFDSpecificNode.path(Constants.VNF_PROVIDER).asText();
                vnfCreation.setVnfProvider(vnfProvider);
                final String vnfSoftwareVersion = dataVNFDSpecificNode.path(Constants.VNF_SOFTWARE_VERSION).asText();
                ;
                vnfCreation.setVnfSoftwareVersion(vnfSoftwareVersion);
                final String vnfdVersion = dataVNFDSpecificNode.path(Constants.VNFD_VERSION).asText();
                vnfCreation.setVnfdVersion(vnfdVersion);
                final JsonNode flavourIds = dataVNFDSpecificNode.get(Constants.FLAVOUR_ID);
                String flavourId = null;
                if (flavourIds != null && flavourIds.isArray()) {
                    flavourId = flavourIds.get(0).asText();
                    vnfCreation.setFlavourId(flavourId);
                }
                final boolean scalingByMoreThanOneStepSupported = dataVNFDSpecificNode.path(Constants.VNFLCM_OP_CONFIG).path(Constants.SCALE)
                        .path(Constants.SCALE_MORE_THAN_1_SUPPORTED).asBoolean();
                vnfCreation.setScalingByMoreThanOneStepSupported(scalingByMoreThanOneStepSupported);
                final JsonNode scalingAspects = dataVNFDSpecificNode.get(Constants.SCALE_ASPECTS);
                if (scalingAspects != null && scalingAspects.isArray()) {
                    final List<ScaleAspectDto> scaleAspects = new ArrayList<ScaleAspectDto>();
                    for (final JsonNode scalingAspect : scalingAspects) {
                        final ScaleAspectDto scaleAspectDto = new ScaleAspectDto();
                        scaleAspectDto.setAspectId(scalingAspect.path(Constants.ASPECT_ID).asText());
                        scaleAspectDto.setDescription(scalingAspect.path(Constants.DESCRIPTION).asText());
                        scaleAspectDto.setMaxScaleLevel(scalingAspect.path(Constants.MAX_SCALE_LEVEL).asInt());
                        if (scalingAspect.has(Constants.MIN_SCALE_LEVEL)) {
                            scaleAspectDto.setMinScaleLevel(scalingAspect.path(Constants.MIN_SCALE_LEVEL).asInt());
                        }
                        scaleAspectDto.setName(scalingAspect.path(Constants.NAME).asText());
                        scaleAspects.add(scaleAspectDto);
                    }
                    vnfCreation.setScaleAspect(scaleAspects);
                }
            }

        } catch (final JsonProcessingException e) {
            throw new VNFLCMServiceException("Error occured while reading vnfd wrapper file");
        } catch (final IOException e) {
            throw new VNFLCMServiceException("Error occured while reading vnfd wrapper file");
        }

    }

    public Response deleteVnfId(final String vnfInstanceId) {
        Response response = null;
        try {
            final VnfCreateDeleteOpConfig vnfDeletionConfig = new VnfCreateDeleteOpConfig();
            vnfDeletionConfig.setEMDriven(true);
            vnfDeletionConfig.setNotificationSendByWF(false);
            vnfDeletionConfig.setCorrelationId("EM");
            final boolean status = vnfService.deleteVnfId(vnfInstanceId, vnfDeletionConfig);
            if (status == true) {
                response = Response.status(Status.NO_CONTENT).build();
            } else {
                throw new VNFLCMServiceException("Internal processing error while trying to delete VNF ID: " + vnfInstanceId);
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
        return response;
    }

    private Status getHttpErrorCode(final VNFLCMServiceException exception) {
        final Status httpErrorCode;
        switch (exception.getType()) {
            case VNF_IDENTIFIER_NOT_FOUND:
                httpErrorCode = Status.NOT_FOUND;
                break;
            case VNF_IDENTIFIER_DELETE_INSTANTIATED:
                httpErrorCode = Status.PRECONDITION_FAILED;
                break;
            case VNF_OPERATION_IN_PROGRESS:
                httpErrorCode = Status.CONFLICT;
                break;
            case SCALE_NOT_SUPPORTED:
                httpErrorCode = Status.BAD_REQUEST;
                break;
            case SCALE_MORE_THAN_ONE_STEP_NOT_SUPPORTED:
                httpErrorCode = Status.BAD_REQUEST;
                break;
            default: // includes ERROR_DELETING_VNF_IDENTIFIER and UNKNOWN
                httpErrorCode = Status.INTERNAL_SERVER_ERROR;
        }
        return httpErrorCode;
    }

    public Response modifyVnf(final String vnfInstanceId, final VnfInfoModificationRequest modifyVnfInfo, @Context final UriInfo uriInfo, final NfvoConfig nfvoConfigData) {
        logger.info("Starting modifyVnfInfo");
        logger.info("modifyVnfInfo info data is {}  ", modifyVnfInfo == null ? null : Utils.getMaskedString(modifyVnfInfo.toString()));
        Response responseObj = null;
        final RequestValidator<ModifyVnfRequest> reqValidationHelper = new RequestValidator<ModifyVnfRequest>();
        reqValidationHelper.validate(modifyVnfInfo);
        final ModifyVnf modifyVnf = translateToVnfModifyDto(modifyVnfInfo);
        logger.debug("--modifyVnf --" + modifyVnf.toString());

        final ModifyVnfOpConfig modifyVnfOpConfig = new ModifyVnfOpConfig();
        modifyVnfOpConfig.setSendNotification(true);
        modifyVnfOpConfig.setStatusUpdateOnly(false);
        modifyVnfOpConfig.setRequestSourceType(RequestSourceType.NBI.value());
        modifyVnfOpConfig.setModifyRequestJson(this.serializeObjectTojson(modifyVnfInfo));
        modifyVnf.setVnfId(vnfInstanceId);

        String vnfLcmOpId = null;
        String evnfmHostname = "";
        String lcmOccusUri = "";
        boolean isPackageCreated = true;

        if (nfvoConfigData != null) {
            final String isNfvoAvailiable = nfvoConfigData.getIsNfvoAvailable();
             try {
                 if (isNfvoAvailiable.equalsIgnoreCase("true") && null != nfvoConfigData.getPackageManagementUrl() && "" != nfvoConfigData.getPackageManagementUrl().intern()) {
                     final boolean isPackageStructureExist = isPackageAlreadyExist(modifyVnf.getVnfdId());
                     logger.info("modifyVnf isPackageStructureExist : " + isPackageStructureExist);
                     final String isPackageDownloadRequired = isPackageDownloadRequired();
                     logger.info("isPackageDownloadRequired {}", isPackageDownloadRequired);
                     if (isPackageStructureExist) {
                        logger.info("package structure is already exist.");
                     }else if(isPackageDownloadRequired.equalsIgnoreCase("YES") && !isPackageStructureExist){
                         logger.info("VnfdId before calling createVnfPackage : "+modifyVnf.getVnfdId());
                         isPackageCreated = createVnfPackage(modifyVnf.getVnfdId(), nfvoConfigData,
                                 modifyVnfInfo.getVnfPkgId());
                         logger.info(" is Auto configure packageManagement from NBI done TRUE/FALSE.... {} ", isPackageCreated);
                     }else {
                         logger.info("Either package download property is not set as Yes or package is already exist... ");
                     }
                 }else {
                         logger.info("Manual package structure required to be created as input provided is not sufficient for Auto package Management.");
                 }
             }catch (final VNFLCMServiceException ex) {
                  logger.error("Exception occured on Package Management, please try again or download it manually. {}", ex);
             }catch (final Exception exception) {
                  logger.error("Exception occured on Package Management, please try again or download it manually. {}", exception.getMessage());
             }
         }
        try {
            final String pathToBeDeleted = PackageManagementConstants.BASE_PATH_TO_BE_CREATED + modifyVnf.getVnfdId()
                    + "/";
            final File filetodelete = new File(pathToBeDeleted);
            if (!isPackageCreated) {
                logger.error("Package artifacts have some problem");
                if (filetodelete.exists()) {
                    logger.info("Proceeding to delete package with vnfdid {}", modifyVnf.getVnfdId());
                    deletePackage(pathToBeDeleted);
                }
                final VNFLCMServiceException vnfex = new VNFLCMServiceException("Package is partially downloaded");
                vnfex.setType(ExceptionType.PACKAGE_PARTIAL_DOWNLOADED);
                return getResponseForException(vnfex);
            } else {
                if (modifyVnf.getVnfName() != null && !modifyVnf.getVnfName().isEmpty()) {
                    validateVnfInstanceName(modifyVnf.getVnfName());
                }
                vnfLcmOpId = vnfService.modifyVnf(modifyVnf, modifyVnfOpConfig);// updateVnflifecycleInfo(vnfId,
                                                                                // createPoDto);
                try {
                    evnfmHostname = fetchEvnfmHost();
                    try {
                        final String ingressHostName = ReadPropertiesUtility
                                .readConfigProperty(Constants.VNF_CLUSTER_DNS);
                        if (ingressHostName != null && !ingressHostName.isEmpty()) {
                            evnfmHostname = ingressHostName;
                        }
                    } catch (final Exception e) {
                        // If ingress_hostname cannot be retrieved, then we create the links with
                        // evnfmHostname
                        logger.info("ingressHostName could not be retrieved.Hence using evnfmHostname property");
                    }
                    if (evnfmHostname != null && !evnfmHostname.isEmpty()) {
                        lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI
                                + Constants.LCM_OP_OCCR_BASE_URI + vnfLcmOpId;
                        logger.info("lcmOccusUri {}", lcmOccusUri);
                    }
                } catch (final Exception e) {
                    logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
                }
                responseObj = Response.status(Status.ACCEPTED).header("Location", lcmOccusUri).build();
            }
        } catch (final VNFLCMServiceException exception) {
            logger.error(exception.getMessage());
            final Status errorCode = getHttpErrorCode(exception);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(errorCode.toString(), exception.getMessage(), vnfInstanceId,
                    errorCode.getReasonPhrase(), errorCode.getStatusCode());
            responseObj = Response.status(errorCode).entity(problemDetail).build();
        } catch (final Exception exc) {
            logger.error(exc.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.toString(), exc.getMessage(),
                    vnfInstanceId, Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseObj = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return responseObj;
    }

    public boolean packageOnboarding(final String vnfdId) throws Exception {
        logger.info("Destination package is not present, proceeding for package management from nfvo.");
        NfvoConfig nfvoConfigData = null;
        boolean isPackageCreated = false;
        try{
            final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
            nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
            final String isNfvoAvailiable = nfvoConfigData.getIsNfvoAvailable();
            if (isNfvoAvailiable.equalsIgnoreCase("true") && null != nfvoConfigData.getPackageManagementUrl()
                    && !nfvoConfigData.getPackageManagementUrl().intern().isEmpty()) {
                final VnfDescriptorDTO vnfDescriptorDTO = fetchQueryPackage(vnfdId,nfvoConfigData,"");
                if (vnfDescriptorDTO.getOnboardedPackageInfoId() != null) {
                    final String isPackageDownloadRequired = isPackageDownloadRequired();
                    if (isPackageDownloadRequired.equalsIgnoreCase("YES")) {
                        isPackageCreated = createVnfPackage(vnfdId, nfvoConfigData, "");
                        logger.info(" is Auto configure packageManagement from NBI done TRUE/FALSE.... {} ", isPackageCreated);
                        if (!isPackageCreated) {
                            logger.error("Manual package structure required to be created as input provided is not sufficient for Auto package Management.");
                            return isPackageCreated;
                        }
                    } else {
                        logger.error("Configuration parameter Package_Download is not true.");
                        return isPackageCreated;
                    }
                } else {
                    logger.error("Query of package with given vnfdId is not successful hence not downloading package.");
                    return isPackageCreated;
                }
            } else {
                logger.error("Manual package structure required to be created as input provided is not sufficient for Auto package Management.");
                return isPackageCreated;
            }
        } catch(final VNFLCMServiceException vnfex) {
            logger.error("Manual package structure required to be created as input provided is not sufficient for Auto package Management.");
            vnfex.setType(ExceptionType.VNFD_BASE_NOT_FOUND);
            throw vnfex;
        } catch(final NfvoCallException vnfex) {
            logger.error("Error in finding NFVO configuration");
            vnfex.setErrorMessage(VNFD_DIRECTORY_ERROR_MESSAGE);
            throw vnfex;
        } catch (final Exception e) {
            logger.error("Exception occured while invoking create vnf.", e);
            throw e;
        }
        return isPackageCreated;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfRestService#instantiateVnf
     * (com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.model.InstantiateVnfRequest)
     */
    public Response instantiateVnf(final String vnfInstanceId, final NbiInstantiateVnfRequest instantiateVnfRequest, @Context final UriInfo uriInfo, String userName, final String idempotencykey) {
        logger.info("-- Starting instantiate - vnfinstanceid-" + vnfInstanceId);
        final RequestValidator<InstantiateVnfRequest> reqValidationHelper = new RequestValidator<InstantiateVnfRequest>();
        reqValidationHelper.validate(instantiateVnfRequest);
        String vnfLcmOpId = null;
        String evnfmHostname = "";
        String lcmOccusUri = "";
        try {
            final VnfInstantiation vnfInstantiation = this.createVnfInstantiation(instantiateVnfRequest);
            vnfInstantiation.setVnfInstanceId(vnfInstanceId);
            vnfInstantiation.setVnfmUseCaseSource("EM");
            if (vnfInstantiation.getVimConnection() != null && !vnfInstantiation.getVimConnection().trim().isEmpty()) {
                logger.info("vimconnection to be set == "+ Utils.getMaskedString(vnfInstantiation.getVimConnection().toString()));
            }
            final VnfInstantiationOpConfig vnfInstantiationOpConfig = new VnfInstantiationOpConfig();
            vnfInstantiationOpConfig.setInstRequestJson(this.serializeObjectTojson(instantiateVnfRequest));
            vnfInstantiationOpConfig.setRequestSourceType(RequestSourceType.NBI.value());
            vnfInstantiationOpConfig.setUserName(userName);
            vnfInstantiationOpConfig.setIdempotencyKey(idempotencykey);
            vnfLcmOpId = vnfService.instantiateVNF(vnfInstantiation, vnfInstantiationOpConfig);
            try {
                evnfmHostname = fetchEvnfmHost();
                String ingressHostName = null;
                try {
                    if (System.getProperty("isTest") == null) {
                        ingressHostName = ReadPropertiesUtility.readConfigProperty(Constants.VNF_CLUSTER_DNS);
                    }
                    if (ingressHostName != null && !ingressHostName.isEmpty()) {
                        evnfmHostname = ingressHostName;
                    }
                } catch (final Exception e) {
                    // If ingress_hostname cannot be retrieved, then we create the links with evnfmHostname
                    logger.info("ingressHostName could not be retrieved.Hence using evnfmHostname property");
                }
                if (evnfmHostname != null && !evnfmHostname.isEmpty()) {
                    lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.LCM_OP_OCCR_BASE_URI + vnfLcmOpId;
                    logger.info("lcmOccusUri {}", lcmOccusUri);
                }
            } catch (final Exception e) {
                logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
            }
        } catch (final VNFLCMServiceException e) {
            logger.error("Exception occured while invoking instantiateVNF.", e);
            if(e.getType() == null ) {
                e.setType(ExceptionType.ERROR_INSTANTIATING_VNF);
            }
            return getResponseForException(e);
        } catch (final Exception exception) {
            String message = exception.getMessage();
            if (!StringUtils.isEmpty(exception.getMessage()) && exception.getMessage().contains("PersistenceException")) {
                message = "Could not open database connection.";
            }
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("internalError", message, "", "", Constants.INTERNAL_ERROR);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return Response.status(Status.ACCEPTED).header("Location", lcmOccusUri).build();
    }

    private String fetchEvnfmHost() {
        try {
            if (System.getProperty("isTest") == null) {
                return ReadPropertiesUtility.readConfigProperty(Constants.ENM_HOST_NAME);
            }
        } catch (final Exception e) {
            logger.error("readevnfmHostnameFromEnv() : Error in reading ENM host name from environment {}", e.getMessage());
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfRestService#changeVnf
     * (com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.model.ChangeVnfRequest)
     */
    public String changeVnf(final String vnfInstanceId, final NbiChangeVnfRequest nbiChangeVnfRequest, String userName) throws VNFLCMServiceException {
        logger.info("-- Starting chnage vnf - vnfinstanceid-" + vnfInstanceId);
        final RequestValidator<InstantiateVnfRequest> reqValidationHelper = new RequestValidator<InstantiateVnfRequest>();
        reqValidationHelper.validate(nbiChangeVnfRequest);
        final ChangeCurrentVNFPackageDto changeReq = this.createChangeVnfpkgdto(vnfInstanceId,nbiChangeVnfRequest);
        changeReq.setVnfInstanceId(vnfInstanceId);
        changeReq.setVnfmUseCaseSource("EM");

        if (nbiChangeVnfRequest.getVnfConfigurableProperties() != null
                && !nbiChangeVnfRequest.getVnfConfigurableProperties().isEmpty()) {
            changeReq.setVnfConfigurableProperties(nbiChangeVnfRequest.getVnfConfigurableProperties());
        }
        if (nbiChangeVnfRequest.getExtensions() != null && !nbiChangeVnfRequest.getExtensions().isEmpty()) {
            changeReq.setExtensions(nbiChangeVnfRequest.getExtensions());
        }
        if (changeReq.getVimConnectionInfo() != null && !changeReq.getVimConnectionInfo().trim().isEmpty()) {
            logger.info("vimconnection to be set == "+ Utils.getMaskedString(changeReq.getVimConnectionInfo().toString()));
        }
        final ChangeCurrentVnfPackageOpConfig changeVnfPackageOpConfig = new ChangeCurrentVnfPackageOpConfig();
        changeVnfPackageOpConfig.setChangeVnfPackageRequestJson(this.serializeObjectTojson(nbiChangeVnfRequest));
        changeVnfPackageOpConfig.setRequestSourceType(RequestSourceType.NBI.value());
        changeVnfPackageOpConfig.setUserName(userName);
        final String changeLcmOpId = vnfService.changeCurrentVnfPackage(changeReq, changeVnfPackageOpConfig);

        return changeLcmOpId;
    }


    private ChangeCurrentVNFPackageDto createChangeVnfpkgdto(final String vnfInstanceId, final NbiChangeVnfRequest nbiChangeVnfRequest) throws VNFLCMServiceException {
        final ChangeCurrentVNFPackageDto changeCurrentVNFPackageDto = new ChangeCurrentVNFPackageDto();
        changeCurrentVNFPackageDto.setVnfdId(nbiChangeVnfRequest.getVnfdId());
        final com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam additionalParam = new com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam();
        if (nbiChangeVnfRequest.getAdditionalParams() != null && nbiChangeVnfRequest.getAdditionalParams() != null) {
            additionalParam.setAdditionalAttributes(nbiChangeVnfRequest.getAdditionalParams());
            changeCurrentVNFPackageDto.setAdditionalParam(additionalParam);
        }
        if(nbiChangeVnfRequest.getVimConnectionInfo() != null && !nbiChangeVnfRequest.getVimConnectionInfo().isEmpty()) {
            final List<VimConnectionInfo> vimConnectionInfos = this.extractVimConnectionInfoFromRequest(nbiChangeVnfRequest.getVimConnectionInfo());
            changeCurrentVNFPackageDto.setVimConnectionInfo(this.serializeObjectTojson(vimConnectionInfos));
            if (disableVimCertificateValidationFlag()) {
                logger.info("Authentication Info: {}:{}:{}:{}:{}:{}:{}:{}", vimConnectionInfos.get(0).getInterfaceInfo().getIdentityEndPoint(),
                        vimConnectionInfos.get(0).getAccessInfo().getCredentials().getUsername(),
                        vimConnectionInfos.get(0).getAccessInfo().getCredentials().getPassword(),
                        vimConnectionInfos.get(0).getAccessInfo().getProjectId(), vimConnectionInfos.get(0).getAccessInfo().getProjectName(),
                        vimConnectionInfos.get(0).getAccessInfo().getDomainName(), vimConnectionInfos.get(0).getAccessInfo().getDomainName(),
                        vimConnectionInfos.get(0).getAccessInfo().getUserDomain());
                VimConnectionHelper.createAuthRequestObject(vimConnectionInfos.get(0).getInterfaceInfo().getIdentityEndPoint(),
                        vimConnectionInfos.get(0).getAccessInfo().getCredentials().getUsername(),
                        vimConnectionInfos.get(0).getAccessInfo().getCredentials().getPassword(),
                        vimConnectionInfos.get(0).getAccessInfo().getProjectId(), vimConnectionInfos.get(0).getAccessInfo().getProjectName(),
                        vimConnectionInfos.get(0).getAccessInfo().getDomainName(), vimConnectionInfos.get(0).getAccessInfo().getDomainName(),
                        vimConnectionInfos.get(0).getAccessInfo().getUserDomain());
            }
        } else {
            // setting vim connection info associated with the instantiated vnf
            final List<VimConnectionInfo> vimConnectionInfoList  = new ArrayList<VimConnectionInfo>();
            final List<com.ericsson.oss.services.vnflcm.api_base.dto.VimConnectionInfo> vimconnectioninfo = vnfService.queryVnf(vnfInstanceId).getVimConnectionInfo();
            if (vimconnectioninfo != null && !vimconnectioninfo.isEmpty()) {
                getInstantiatedVnfVimConnectionInfo(vimconnectioninfo.get(0),vimConnectionInfoList);
                logger.debug("validating the vim details");
                final List<String> missingParams = validateVimDetails(vimConnectionInfoList.get(0));
                if (missingParams.size()>0) {
                    logger.debug("vimConnectionInfo is missing following  mandatory fields - {}, hence going with default vim",missingParams.toString());
                    //setting default vim information
                    final List<Vim> vims = vimService.getVims();
                    logger.debug("Vims return from Service::" + vims);
                    final List<VimConnectionInfo> vimConnectionInfor = new ArrayList<VimConnectionInfo>();
                    if (vims != null && !vims.isEmpty()) {
                        getDefaultVimConnectionInfo(vims, vimConnectionInfor);
                        logger.debug("Vims added in list:{}", vimConnectionInfor);
                        changeCurrentVNFPackageDto.setVimConnectionInfo(this.serializeObjectTojson(vimConnectionInfor));
                    }
                } else {
                    changeCurrentVNFPackageDto.setVimConnectionInfo(this.serializeObjectTojson(vimConnectionInfoList));
                }
            }
        }
        return changeCurrentVNFPackageDto;
    }

    private void getInstantiatedVnfVimConnectionInfo(final com.ericsson.oss.services.vnflcm.api_base.dto.VimConnectionInfo VimConnectionInfo,final List<VimConnectionInfo> vimConnectionInfoList) {
        logger.debug("VnfRestServiceSol002V241Impl :: getInstantiatedVnfVimConnectionInfo()");
        final ModelMapper modelMapper = new ModelMapper();
        final VimConnectionInfo vimConnectionInfoNBI = new VimConnectionInfo();
        vimConnectionInfoNBI.setId(VimConnectionInfo.getVimId());
        vimConnectionInfoNBI.setVimId(VimConnectionInfo.getVimName());
        vimConnectionInfoNBI.setVimType(VimConnectionInfo.getVimType().name());
        final InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setIdentityEndPoint(VimConnectionInfo.getInterfaceInfo().getInterfaceEndpoint());
        vimConnectionInfoNBI.setInterfaceInfo(interfaceInfo);

        // set domain and project
        final AccessInfo accessInfo = new AccessInfo();
        accessInfo.setDomainName(VimConnectionInfo.getAccessInfo().getDomainName());
        accessInfo.setProjectId(VimConnectionInfo.getAccessInfo().getProjectId());
        accessInfo.setProjectName(VimConnectionInfo.getAccessInfo().getProjectName());
        accessInfo.setUserDomain(VimConnectionInfo.getAccessInfo().getUserDomain());
        final Credentials credentials = new Credentials();
        credentials.setUsername(VimConnectionInfo.getAccessInfo().getCredentials().getUsername());
        credentials.setPassword(VimConnectionInfo.getAccessInfo().getCredentials().getPassword());
        accessInfo.setCredentials(credentials);
        vimConnectionInfoNBI.setAccessInfo(accessInfo);

        logger.info("vimConnectionInfoNBI :{}",Utils.getMaskedString(vimConnectionInfoNBI.toString()));
        final VimConnectionInfo VimConnInfo = modelMapper.map(vimConnectionInfoNBI,VimConnectionInfo.class);
        vimConnectionInfoList.add(VimConnInfo);
    }

    private VnfInstantiation createVnfInstantiation(final NbiInstantiateVnfRequest instantiateVnfRequest)
            throws VNFLCMServiceException {
        final VnfInstantiation vnfInstantiation = new VnfInstantiation();
        vnfInstantiation.setFalvourId(instantiateVnfRequest.getFlavourId());
        vnfInstantiation.setInstantiationLevelId(instantiateVnfRequest.getInstantiationLevelId());
        final com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam additionalParam = new com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam();
        if (instantiateVnfRequest.getAdditionalParams() != null
                && instantiateVnfRequest.getAdditionalParams() != null) {
            additionalParam.setAdditionalAttributes(instantiateVnfRequest.getAdditionalParams());
            vnfInstantiation.setAdditionalParam(additionalParam);
        }
        // Decode username in VimCredential as EO-CM is sending encoded username and
        // password.
        // For VMWare user name will come as plain text for NetCracker NFVO, so not
        // decoding it before use.
        if (instantiateVnfRequest.getVimConnectionInfo() != null
                && !instantiateVnfRequest.getVimConnectionInfo().isEmpty()) {
            // Consider the vimConnection passed in the request
            List<VimConnectionInfo> vimConnectionInfos = extractVimConnectionInfoFromRequest(instantiateVnfRequest.getVimConnectionInfo());
            vnfInstantiation.setVimConnection(this.serializeObjectTojson(vimConnectionInfos));
            if (disableVimCertificateValidationFlag()) {
                logger.info("Authentication Info: {}:{}:{}:{}:{}:{}",
                        vimConnectionInfos.get(0).getInterfaceInfo().getIdentityEndPoint(),
                        vimConnectionInfos.get(0).getAccessInfo().getCredentials().getUsername(),
                        vimConnectionInfos.get(0).getAccessInfo().getProjectId(),
                        vimConnectionInfos.get(0).getAccessInfo().getProjectName(),
                        vimConnectionInfos.get(0).getAccessInfo().getDomainName(),
                        vimConnectionInfos.get(0).getAccessInfo().getUserDomain());
                VimConnectionHelper.createAuthRequestObject(
                        vimConnectionInfos.get(0).getInterfaceInfo().getIdentityEndPoint(),
                        vimConnectionInfos.get(0).getAccessInfo().getCredentials().getUsername(),
                        vimConnectionInfos.get(0).getAccessInfo().getCredentials().getPassword(),
                        vimConnectionInfos.get(0).getAccessInfo().getProjectId(),
                        vimConnectionInfos.get(0).getAccessInfo().getProjectName(),
                        vimConnectionInfos.get(0).getAccessInfo().getDomainName(),
                        vimConnectionInfos.get(0).getAccessInfo().getDomainName(),
                        vimConnectionInfos.get(0).getAccessInfo().getUserDomain());
            }
        } else {
            // set default vim information
            final List<Vim> vims = vimService.getVims();
            logger.debug("Vims return from Service::" + vims);
            final List<VimConnectionInfo> vimConnectionInfor = new ArrayList<VimConnectionInfo>();
            if (vims != null && !vims.isEmpty()) {
                getDefaultVimConnectionInfo(vims, vimConnectionInfor);
                logger.debug("Vims added in list:{}", vimConnectionInfor);
                vnfInstantiation.setVimConnection(this.serializeObjectTojson(vimConnectionInfor));
            }
            else {
                final VNFLCMServiceException ex = new VNFLCMServiceException("Default VIM is not configured in the system");
                throw ex;
            }
        }
        return vnfInstantiation;
    }

    //Default visibility. This is reused for OVF package onboarding
    List<VimConnectionInfo> extractVimConnectionInfoFromRequest(List<VimConnectionInfo> vimConnectionInfos) throws VNFLCMServiceException {
        for (final VimConnectionInfo vimConnectionInfo : vimConnectionInfos) {
            logger.debug("Checking whether userName needs to decoded for CloudType : {}", vimConnectionInfo.getVimType());
            if (vimConnectionInfo.getVimType() != null && !vimConnectionInfo.getVimType().equals(VimType.VCD.toString())) {
                logger.debug("Decoding User Name for Cloud Type {}", vimConnectionInfo.getVimType());
                if (vimConnectionInfo.getAccessInfo() != null && vimConnectionInfo.getAccessInfo().getCredentials() != null) {
                    final Credentials credentials = vimConnectionInfo.getAccessInfo().getCredentials();
                    if(credentials.getUsername() != null && !credentials.getUsername().isEmpty()) {
                        final String username = credentials.getUsername();
                        final String decodedUserName = new String(Base64.decodeBase64(username.trim()));
                        credentials.setUsername(decodedUserName);
                        vimConnectionInfo.getAccessInfo().setCredentials(credentials);
                    }
                }
            }
            if (vimConnectionInfo.getVimId() != null && !vimConnectionInfo.getVimId().isEmpty()) {
                Vim vim = null;
                try {
                   vim = vimService.getVimByName(vimConnectionInfo.getVimId());
                } catch (VNFLCMServiceException exception) {
                    vim =null;
                    logger.info("Vim id not found in db.");
                }
                if (vim != null) {
                 logger.info("Vim is not null. Hence setting vimConnectionInfo.");
              // Setting interface info and VIM Type
                 vimConnectionInfo.setId(vim.getVimName());
                 vimConnectionInfo.setVimType(vim.getVimType());
                 final InterfaceInfo interfaceInfo = new InterfaceInfo();
                 interfaceInfo.setIdentityEndPoint(vim.getAuthURL());
                 vimConnectionInfo.setInterfaceInfo(interfaceInfo);
                 if (vim.getAuthURL().contains(com.ericsson.oss.services.vnflcm.common.dataTypes.Constants.V3_VIM_AUTH)) {
                     final AccessInfo accessInfo = vimConnectionInfo.getAccessInfo();
                     if (accessInfo != null && accessInfo.getDomainName() != null && !accessInfo.getDomainName().isEmpty()) {
                         boolean vimProjFound = false;
                         boolean domainFound = false;
                         if (vim.getVimTenantInfos() != null) {
                             for (final VimTenant vimTenant : vim.getVimTenantInfos()) {
                                 if (accessInfo.getDomainName().equals(vimTenant.getTenantName())) {
                                     domainFound = true;
                                     if ((accessInfo.getProjectName() != null && !accessInfo.getProjectName().isEmpty()) ||
                                             (accessInfo.getProjectId() != null && !accessInfo.getProjectId().isEmpty())) {
                                         for (final VimSubTenant vimSubTenant : vimTenant.getVimSubTenants()) {
                                             if ((accessInfo.getProjectName() != null && accessInfo.getProjectName().equals(vimSubTenant.getSubTenantName())
                                                     || (accessInfo.getProjectId() != null && vimConnectionInfo.getAccessInfo().getProjectId().equals(vimSubTenant.getSubTenantId())))) {
                                                 logger.info("ProjectName and ProjectId is equals to SubTenantName or SubTenantId");
                                                 vimConnectionInfo.setAccessInfo(extractV3VIMAccessInfo(vimConnectionInfo, vimSubTenant));
                                                 vimProjFound = true;
                                                 break;
                                                }
                                            }
                                        } else {
                                            for (final VimSubTenant vimSubTenant : vimTenant.getVimSubTenants()) {
                                                if (vimSubTenant.isDefaultSubTenant()) {
                                                    logger.info("Set DefaultSubTenant ");
                                                    vimConnectionInfo.setAccessInfo(extractV3VIMAccessInfo(vimConnectionInfo, vimSubTenant));
                                                    vimProjFound = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (vim != null  && !domainFound) {
                                if ((accessInfo.getProjectName() != null && !accessInfo.getProjectName().isEmpty()) ||
                                        (accessInfo.getProjectId() != null && !accessInfo.getProjectId().isEmpty())) {
                                    Credentials creds = accessInfo.getCredentials();
                                    if(creds != null && creds.getUsername() !=null && !creds.getUsername().isEmpty() && creds.getPassword() != null && !creds.getPassword().isEmpty()) {
                                        logger.info("Domain name present in vimconnection info is not matching with the registered vim. Hence Updating the vim");
                                    }else {
                                        final VNFLCMServiceException ex = new VNFLCMServiceException("username/password is not found in vimConnection info for the project in domain to update vim ");
                                        ex.setType(ExceptionType.MANDATORY_PARAMETERS_MISSING);
                                        throw ex;
                                    }
                                } else {
                                    final VNFLCMServiceException ex = new VNFLCMServiceException("Project id/name is not found in vimConnection info for the domain name to update vim ");
                                    ex.setType(ExceptionType.MANDATORY_PARAMETERS_MISSING);
                                    throw ex;
                                }
                            }else if(!vimProjFound && domainFound) {
                                Credentials creds = accessInfo.getCredentials();
                                if(creds != null && creds.getUsername() !=null && !creds.getUsername().isEmpty() && creds.getPassword() != null && !creds.getPassword().isEmpty()) {
                                    logger.info("Project name present in vimconnection info is not matching with the registered vim. Hence Updating the vim");
                                } else {
                                    final VNFLCMServiceException ex = new VNFLCMServiceException("UserName/Password is not found in vimConnection info for updating the vim for new project ");
                                    ex.setType(ExceptionType.MANDATORY_PARAMETERS_MISSING);
                                    throw ex;
                                }
                            }
                        } else {
                            setDefaultDomainAndProject(vim, vimConnectionInfo);
                            logger.info("Set Default Domain and Project Values");
                        }
                    } else if (vim.getAuthURL().contains(com.ericsson.oss.services.vnflcm.common.dataTypes.Constants.V2_VIM_AUTH)) {
                        logger.info("Extract v2 Vim Information");
                        extractV2VIMInfo(vimConnectionInfo, vim);
                    } else if (vim.getVimType().contains("VMWARE_VCLOUD") || vim.getVimType().contains("VCD")) {
                        logger.info("Extract Vim Info for VCD Vim Type");
                        extractV2VIMInfo(vimConnectionInfo, vim);
                        vimConnectionInfo.setVimType("VCD");
                    }
                } else {
                    logger.info("VIM with the given id in vimConnectionInfo is not found. Hence, checking if vimConnection info contain the values to auto register vim");
                    List<String> missingParams = validateVimDetails(vimConnectionInfo);
                    if (missingParams.size()>0) {
                        logger.info("Values required for vim auto register not found");
                        final VNFLCMServiceException ex = new VNFLCMServiceException("VIM with the given vimConnectionInfo is not registered and "
                                + "Auto registration of VIM is not possible as vimConnectionInfo is missing following  mandatory fields - "+missingParams.toString());
                        ex.setType(ExceptionType.MANDATORY_PARAMETERS_MISSING);
                        throw ex;
                    }
                }
            }
        }
        return vimConnectionInfos;
    }
    /**
     * @param vimConnectionInfo
     */
    private List<String> validateVimDetails(VimConnectionInfo vimConnectionInfo) {
        // TODO Auto-generated method stub
       List<String> missingParams = new ArrayList<String>();
         if(vimConnectionInfo.getVimId() == null || vimConnectionInfo.getVimId().isEmpty()) {
            logger.info("Vim id provided is null/empty");
            missingParams.add("vimId");
        }
        if(vimConnectionInfo.getVimType() == null  || vimConnectionInfo.getVimType().isEmpty()) {
            logger.info("Vim type provided is null/empty");
            missingParams.add("vimType");
        }
        final AccessInfo accessInfo = vimConnectionInfo.getAccessInfo();
        if(accessInfo != null) {
            if((accessInfo.getProjectId() == null || accessInfo.getProjectId().isEmpty()) && (accessInfo.getProjectName() == null || accessInfo.getProjectName().isEmpty())) {
                logger.info("projectId / projectName provided is null/empty");
                missingParams.add("projectName/ProjectId");
            }
            final Credentials credentials = accessInfo.getCredentials();
            if(credentials != null) {
                if(credentials.getUsername() == null || credentials.getUsername().isEmpty()) {
                    logger.info("user name provided is null/empty");
                    missingParams.add("username");
                }
                if(credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
                    logger.info("password provided is null/empty");
                    missingParams.add("password");
                }
            }else {
                logger.info("credentialss is not present in vim connection info");
                missingParams.add("credentials");
            }

        }else {
            logger.info("AccessInfo not present in vim connection info");
            missingParams.add("accessInfo");
        }

        final InterfaceInfo interfaceInfo= vimConnectionInfo.getInterfaceInfo();
        if(interfaceInfo != null) {
            if(interfaceInfo.getIdentityEndPoint() == null || interfaceInfo.getIdentityEndPoint().isEmpty()) {
                logger.info("end point url is not not present in vim connection info");
                missingParams.add("identityEndPoint");
            }else if(interfaceInfo.getIdentityEndPoint().contains(com.ericsson.oss.services.vnflcm.common.dataTypes.Constants.V3_VIM_AUTH)){
                if(accessInfo != null && (accessInfo.getDomainName() == null || accessInfo.getDomainName().isEmpty())) {
                    logger.info("Domain Name not found for v3 version");
                    missingParams.add("domainName");
                }
            }
        }else {
            logger.info("InterfaceInfo is not present in vim connection info");
            missingParams.add("interfaceInfo");
        }
        return missingParams;
    }

    /**
     * @param vimConnectionInfo
     * @param vim
     * @throws VNFLCMServiceException
     */
    private void extractV2VIMInfo(final VimConnectionInfo vimConnectionInfo, Vim vim) throws VNFLCMServiceException {
        boolean vimFound = false;
        if (vimConnectionInfo.getAccessInfo()!=null &&((vimConnectionInfo.getAccessInfo().getProjectName() != null && !vimConnectionInfo.getAccessInfo().getProjectName().isEmpty())
            || (vimConnectionInfo.getAccessInfo().getProjectId() != null && !vimConnectionInfo.getAccessInfo().getProjectId().isEmpty()))) {
            for (final VimTenant vimTenant : vim.getVimTenantInfos()) {
                if ((vimConnectionInfo.getAccessInfo().getProjectName() != null && vimConnectionInfo.getAccessInfo().getProjectName().equals(vimTenant.getTenantName())
                    || (vimConnectionInfo.getAccessInfo().getProjectId() != null && vimConnectionInfo.getAccessInfo().getProjectId().equals(vimTenant.getTenantId())))) {

                    if (vimConnectionInfo.getAccessInfo().getCredentials() != null) {
                        if (vimConnectionInfo.getAccessInfo().getCredentials().getUsername() == null || vimConnectionInfo.getAccessInfo().getCredentials().getUsername().isEmpty()) {
                            vimConnectionInfo.getAccessInfo().getCredentials().setUsername(vimTenant.getCredentials().getUsername());
                        }
                        if (vimConnectionInfo.getAccessInfo().getCredentials().getPassword() == null || vimConnectionInfo.getAccessInfo().getCredentials().getPassword().isEmpty()) {
                            vimConnectionInfo.getAccessInfo().getCredentials().setPassword(vimTenant.getCredentials().getPassword());
                        }
                    } else {
                        final Credentials credentials = new Credentials();
                        credentials.setUsername(vimTenant.getCredentials().getUsername());
                        credentials.setPassword(vimTenant.getCredentials().getPassword());
                        vimConnectionInfo.getAccessInfo().setCredentials(credentials);
                    }
                    vimFound = true;
                    break;
                }
            }
            if (!vimFound) {
                logger.info("Project name/id presnt in vimconnection info is not found in Vim");
            }
        } else {
            logger.info("projectId or projectName not provided Hence setting default value");
            setDefaultDomainAndProject(vim, vimConnectionInfo);
        }
    }
    /**
     * @param vimConnectionInfo
     * @param vimSubTenant
     */
    private AccessInfo extractV3VIMAccessInfo(final VimConnectionInfo vimConnectionInfo,final VimSubTenant vimSubTenant) {
        final AccessInfo accessInfo = vimConnectionInfo.getAccessInfo();
        accessInfo.setProjectId(vimSubTenant.getSubTenantId());
        accessInfo.setProjectName(vimSubTenant.getSubTenantName());
        if (accessInfo.getCredentials() != null) {
            if (accessInfo.getCredentials().getUsername() == null || accessInfo.getCredentials().getUsername().isEmpty()) {
                accessInfo.getCredentials().setUsername(vimSubTenant.getCredentials().getUsername());
            }
            if (accessInfo.getCredentials().getPassword() == null || accessInfo.getCredentials().getPassword().isEmpty()) {
                accessInfo.getCredentials().setPassword(vimSubTenant.getCredentials().getPassword());
            }
        } else {
            final Credentials credentials = new Credentials();
            credentials.setUsername(vimSubTenant.getCredentials().getUsername());
            credentials.setPassword(vimSubTenant.getCredentials().getPassword());
            accessInfo.setCredentials(credentials);
            logger.info("Setting Credentials for SubTenant");
        }
        return accessInfo;
    }
    void getDefaultVimConnectionInfo(List<Vim> vims, List<VimConnectionInfo> vimConnectionInfos) throws VNFLCMServiceException {
        boolean isDefaultVIMFound = false;
        for (final Vim vim : vims) {
            if (vim.isDefaultVim()) {
                isDefaultVIMFound = true;
                logger.info("Setting VimConnectionInfo for Default Vim");
                final VimConnectionInfo vimConnectionInfo = new VimConnectionInfo();
                vimConnectionInfo.setId(vim.getVimName());
                vimConnectionInfo.setVimId(vim.getVimName());
                vimConnectionInfo.setVimType(vim.getVimType());
                final InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setIdentityEndPoint(vim.getAuthURL());
                vimConnectionInfo.setInterfaceInfo(interfaceInfo);
                setDefaultDomainAndProject(vim, vimConnectionInfo);
                vimConnectionInfos.add(vimConnectionInfo);
                break;
            }
        }
        if (!isDefaultVIMFound) {
            final VNFLCMServiceException ex = new VNFLCMServiceException("Default VIM is not configured in the system");
            throw ex;
        }
    }
    /**
     * @param vim
     * @param vimConnectionInfo
     */
    void setDefaultDomainAndProject(final Vim vim, final VimConnectionInfo vimConnectionInfo) throws VNFLCMServiceException {
        boolean defaultDomainAndProjectFound = false;
        if (vim.getVimTenantInfos() != null) {
            for (final VimTenant vimTenant : vim.getVimTenantInfos()) {
                if (vimTenant.isDefaultTenant()) {
                    final AccessInfo accessInfo = new AccessInfo();
                    if (vim.getAuthURL().contains(com.ericsson.oss.services.vnflcm.common.dataTypes.Constants.V3_VIM_AUTH)) {
                        logger.info("Set default Tenant Value for V3 vim");
                        accessInfo.setDomainName(vimTenant.getTenantName());
                        if (vimTenant.getUserDomain() != null) {
                            accessInfo.setUserDomain(vimTenant.getUserDomain());
                        } else {
                            accessInfo.setUserDomain("Default");
                            logger.info("UserDomain set to be Default");
                        }
                        final List<VimSubTenant> vimSubTenants = vimTenant.getVimSubTenants();
                        for (final VimSubTenant vimSubTenant : vimSubTenants) {
                            if (vimSubTenant.isDefaultSubTenant()) {
                                accessInfo.setProjectId(vimSubTenant.getSubTenantId());
                                accessInfo.setProjectName(vimSubTenant.getSubTenantName());
                                final Credentials credentials = new Credentials();
                                credentials.setUsername(vimSubTenant.getCredentials().getUsername());
                                credentials.setPassword(vimSubTenant.getCredentials().getPassword());
                                accessInfo.setCredentials(credentials);
                                vimConnectionInfo.setAccessInfo(accessInfo);
                                defaultDomainAndProjectFound = true;
                                break;
                            }
                        }
                    } else if (vim.getAuthURL().contains(com.ericsson.oss.services.vnflcm.common.dataTypes.Constants.V2_VIM_AUTH)) {
                        logger.info("Set default Tenant Value for V2 vim");
                        accessInfo.setProjectId(vimTenant.getTenantId());
                        accessInfo.setProjectName(vimTenant.getTenantName());
                        final Credentials credentials = new Credentials();
                        credentials.setUsername(vimTenant.getCredentials().getUsername());
                        credentials.setPassword(vimTenant.getCredentials().getPassword());
                        accessInfo.setCredentials(credentials);
                        vimConnectionInfo.setAccessInfo(accessInfo);
                        defaultDomainAndProjectFound = true;
                        break;
                    } else if (vim.getVimType().contains("VMWARE_VCLOUD") || vim.getVimType().contains("VCD")) {
                        logger.debug("Checking whether userName needs to decoded for CloudType : {}",vim.getVimType());
                        accessInfo.setProjectId(vimTenant.getTenantId());
                        accessInfo.setProjectName(vimTenant.getTenantName());
                        final Credentials credentials = new Credentials();
                        credentials.setUsername(vimTenant.getCredentials().getUsername());
                        credentials.setPassword(vimTenant.getCredentials().getPassword());
                        accessInfo.setCredentials(credentials);
                        vimConnectionInfo.setAccessInfo(accessInfo);
                        vimConnectionInfo.setVimType("VCD");
                        defaultDomainAndProjectFound = true;
                        break;
                    }
                }
            }
        }
        if (!defaultDomainAndProjectFound) {
            final VNFLCMServiceException ex = new VNFLCMServiceException("Default Domain and Project not found for the given VIM Id");
            throw ex;
        }
    }

    private boolean disableVimCertificateValidationFlag() {
        try {
            String disableCertificateValidationFlag = null;
            if (System.getProperty("isTest") == null) {
                disableCertificateValidationFlag = ReadPropertiesUtility.readConfigProperty("disableCertificateValidation");
            }
            if (disableCertificateValidationFlag != null && disableCertificateValidationFlag.equalsIgnoreCase("YES")) {
                return true;
            }
        } catch (final Exception e) {
            logger.error("disableVimCertificateValidationFlag() : Error in reading from environment {}", e.getMessage());
        }
        return false;
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

    private VnfCreation createVnfCreationRequest(final CreateVnfRequest createVnfRequest, final String onboardPackageId) {
        if (createVnfRequest != null) {
            final VnfCreation vnfCreation = new VnfCreation();
            //vnfCreation.setVnfmUseCaseSource("EM");
            if (createVnfRequest.getVnfInstanceName() != null && !createVnfRequest.getVnfInstanceName().isEmpty()) {
                vnfCreation.setVnfName(createVnfRequest.getVnfInstanceName().trim());
            }
            if (createVnfRequest.getVnfInstanceDescription() != null && !createVnfRequest.getVnfInstanceDescription().isEmpty()) {
                vnfCreation.setVnfDescription(createVnfRequest.getVnfInstanceDescription().trim());
            }
            vnfCreation.setVnfdId(createVnfRequest.getVnfdId().trim());

            if (onboardPackageId != null) {
                logger.info("onboardPackageId {}.. ", onboardPackageId);
                vnfCreation.setOnboardedPackageInfoId(onboardPackageId.trim());
            } else if (onboardPackageId == null) {
                if (createVnfRequest.getAdditionalParams() != null) {
                    if (createVnfRequest.getAdditionalParams().getVnfPkgId() != null) {
                        vnfCreation.setOnboardedPackageInfoId(createVnfRequest.getAdditionalParams().getVnfPkgId().trim());
                    } else {
                        vnfCreation.setOnboardedPackageInfoId(createVnfRequest.getVnfdId());
                    }
                } else {
                    logger.info("additional param is null");
                    vnfCreation.setOnboardedPackageInfoId(createVnfRequest.getVnfdId());
                }
            }
            if (createVnfRequest.getAdditionalParams() != null) {
                if (createVnfRequest.getAdditionalParams().getHotPackageName() != null
                        && !createVnfRequest.getAdditionalParams().getHotPackageName().isEmpty()) {
                    vnfCreation.setHotPackageName(createVnfRequest.getAdditionalParams().getHotPackageName().trim());
                }
            }
            return vnfCreation;
        }
        return null;
    }

    public VnfInstance createvnfInstanceResponse(final VnfResponse vnfResponse) {
        if (vnfResponse != null) {
            final VnfInstance vnfInstance = new VnfInstance();
            final VnfResponse vnfResponseObject = vnfResponse;
            vnfInstance.setId(vnfResponseObject.getVnfId());
            vnfInstance.setVnfInstanceName(vnfResponseObject.getVnfName());
            vnfInstance.setVnfInstanceDescription(vnfResponseObject.getVnfDescription());
            vnfInstance.setVnfdId(vnfResponseObject.getVnfdId());
            vnfInstance.setVnfProvider(vnfResponseObject.getVnfProvider());
            vnfInstance.setVnfProductName(vnfResponseObject.getVnfProductName());
            vnfInstance.setVnfSoftwareVersion(vnfResponseObject.getVnfSoftwareVersion());
            vnfInstance.setVnfdVersion(vnfResponseObject.getVnfdVersion());
            vnfInstance.setVnfPkgId(vnfResponseObject.getOnboardedPackageInfoId());
            vnfInstance.setInstantiationState(InstantiationState.NOT_INSTANTIATED);
            return vnfInstance;
        }
        return null;
    }

    private Response getResponseForException(final VNFLCMServiceException e) {
        // TODO This method needs to be made more generic

        Response response = null;
        VeVnfmemProblemDetail problemDetail = null;
        switch (e.getType()) {
            case VNF_IDENTIFIER_NOT_FOUND:
                // For Instantiate
                response = Response.status(VeVnfmemErrorType.VNF_IDENTIFIER_NOT_FOUND.getHttpCode()).entity(VeVnfmemSol002V241ErrorMessageUtil
                        .generateErrorMessage(VeVnfmemErrorType.VNF_IDENTIFIER_NOT_FOUND, "vnfInstanceId", VNF_ID_NOT_FOUND[1])).build();

                break;
            case VNF_ALREADY_INSTANTIATED:
                response = Response
                        .status(VeVnfmemErrorType.VNF_ALREADY_INSTANTIATED.getHttpCode()).entity(VeVnfmemSol002V241ErrorMessageUtil
                                .generateErrorMessage(VeVnfmemErrorType.VNF_ALREADY_INSTANTIATED, "vnfInstanceId", VNF_ALREADY_INSTANTIATED[1]))
                        .build();
                break;
            case WORKFLOW_MAPPING_RULE_NOT_FOUND:
                response = Response.status(VeVnfmemErrorType.WORKFLOW_MAPPING_NOT_FOUND.getHttpCode()).entity(VeVnfmemSol002V241ErrorMessageUtil
                        .generateErrorMessage(VeVnfmemErrorType.WORKFLOW_MAPPING_NOT_FOUND, "", WORKFLOW_MAPPING_NOT_FOUND[1])).build();
                break;

            case VNF_OPERATION_IN_PROGRESS:
                // For Instantiate
                response = Response.status(VeVnfmemErrorType.INSTANCE_OPERATION_IN_PROGRESS_INSTANTIATE.getHttpCode())
                        .entity(VeVnfmemSol002V241ErrorMessageUtil.generateErrorMessage(VeVnfmemErrorType.INSTANCE_OPERATION_IN_PROGRESS_INSTANTIATE,
                                "vnfInstanceId", INSTANCE_OPERATION_IN_PROGRESS_INSTANTIATE[1]))
                        .build();

                break;
            case MANDATORY_PARAMETERS_MISSING:
                response = Response.status(VeVnfmemErrorType.BAD_REQUEST.getHttpCode())
                        .entity(VeVnfmemSol002V241ErrorMessageUtil.generateErrorMessage(VeVnfmemErrorType.BAD_REQUEST,
                                "vnfInstanceId", e.getMessage()))
                        .build();

                break;
            case ERROR_INSTANTIATING_VNF:
            case ERROR_CREATING_VNF_OPERATION:
            case ERROR_WORKFLOW_INSTANCE:
                // For Instantiate
                response = Response.status(VeVnfmemErrorType.INTERNAL_ERROR.getHttpCode()).entity(VeVnfmemSol002V241ErrorMessageUtil
                        .generateErrorMessage(VeVnfmemErrorType.VNF_IDENTIFIER_NOT_FOUND, ErrorMessages.INTERNAL_ERROR_MSG[0], e.getMessage()))
                        .build();

                break;
            case ERROR_PARSING_JSON:
                // For create vnf
                problemDetail = new VeVnfmemProblemDetail(ErrorMessages.JSON_PARSING_ERROR_MSG[0], ErrorMessages.JSON_PARSING_ERROR_MSG[1], "",
                        e.getType().name(), VeVnfmemErrorType.JSON_PARSING_ERROR.getHttpCode());
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
                break;
            case CONFIGURATION_FILE_NOT_EXIST:
                // For create vnf
                problemDetail = new VeVnfmemProblemDetail(ErrorMessages.CONFIGURATION_FILE_NOT_FOUND[0],
                        ErrorMessages.CONFIGURATION_FILE_NOT_FOUND[1], "", e.getType().name(),
                        VeVnfmemErrorType.CONFIGURATION_FILE_NOT_FOUND.getHttpCode());
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
                break;
            case ERROR_CREATING_VNF:
                // For create vnf
                problemDetail = new VeVnfmemProblemDetail(ErrorMessages.VNF_NOT_FOUND[0], ErrorMessages.VNF_NOT_FOUND[1], "", e.getType().name(),
                        VeVnfmemErrorType.INTERNAL_ERROR.getHttpCode());
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
                break;
            case PACKAGE_PARTIAL_DOWNLOADED:
                // For create vnf
                problemDetail = new VeVnfmemProblemDetail(ErrorMessages.PACKAGE_ISSUE[0], ErrorMessages.PACKAGE_ISSUE[1], "", e.getType().name(),
                        VeVnfmemErrorType.PACKAGE_ISSUE.getHttpCode());
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
                break;
            case UNKNOWN:
            default:
                response = Response.status(VeVnfmemErrorType.INTERNAL_ERROR.getHttpCode())
                        .entity(VeVnfmemSol002V241ErrorMessageUtil.generateUnexpectedError(VeVnfmemErrorType.INTERNAL_ERROR)).build();
                break;

        }
        return response;
    }

    public Response terminateVnfInstance(final String vnfInstanceId, final TerminateVnfRequest terminateVnfRequest, @Context final UriInfo uriInfo, String userName) {

        logger.info("-- Starting terminate - vnfinstanceid-" + vnfInstanceId);
        Response responseObj = null;
        String vnfLcmOpId = null;
        String evnfmHostname = "";
        String lcmOccusUri = "";

        final RequestValidator<TerminateVnfRequest> reqValidationHelper = new RequestValidator<TerminateVnfRequest>();
        reqValidationHelper.validate(terminateVnfRequest);
        try {
            final VnfInstantiationOpConfig vnfTerminationOpConfig = new VnfInstantiationOpConfig();
            final com.ericsson.oss.services.vnflcm.api_base.model.TerminateVnfRequest svcTerminateVnfReq = new com.ericsson.oss.services.vnflcm.api_base.model.TerminateVnfRequest();
            if (terminateVnfRequest != null) {
                if (terminateVnfRequest.getTerminationType() != null && terminateVnfRequest.getTerminationType().equals(TerminateVnfRequestType.GRACEFUL)) {
                    svcTerminateVnfReq.setTerminationType(TerminateVNFRequestTerminationType.GRACEFUL);
                    svcTerminateVnfReq.setGracefulTerminationTimeout(terminateVnfRequest.getGracefulTerminationTimeout());
                } else {
                    svcTerminateVnfReq.setTerminationType(TerminateVNFRequestTerminationType.FORCEFUL);
                }
                if (terminateVnfRequest.getAdditionalParams() != null && !terminateVnfRequest.getAdditionalParams().isEmpty()) {
                    final com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam additionalParam = new com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam();
                    additionalParam.setAdditionalAttributes(terminateVnfRequest.getAdditionalParams());
                    svcTerminateVnfReq.setAdditionalParams(additionalParam);
                }
            }
            vnfTerminationOpConfig.setInstRequestJson(this.serializeObjectTojson(svcTerminateVnfReq));
            vnfTerminationOpConfig.setRequestSourceType(RequestSourceType.NBI.value());
            vnfTerminationOpConfig.setVnfmUseCaseSource("EM");
            vnfTerminationOpConfig.setUserName(userName);
            vnfLcmOpId = vnfService.terminateVnf(vnfInstanceId, vnfTerminationOpConfig);
            try {
                evnfmHostname = fetchEvnfmHost();
                String ingressHostName = null;
                try {
                    if (System.getProperty("isTest") == null) {
                        ingressHostName = ReadPropertiesUtility.readConfigProperty(Constants.VNF_CLUSTER_DNS);
                    }
                    if (ingressHostName != null && !ingressHostName.isEmpty()) {
                        evnfmHostname = ingressHostName;
                    }
                } catch (final Exception e) {
                    // If ingress_hostname cannot be retrieved, then we create the links with evnfmHostname
                    logger.info("ingressHostName could not be retrieved.Hence using evnfmHostname property");
                }
                if (evnfmHostname != null && !evnfmHostname.isEmpty()) {
                    lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.LCM_OP_OCCR_BASE_URI + vnfLcmOpId;
                    logger.info("lcmOccusUri {}", lcmOccusUri);
                }
            } catch (final Exception e) {
                logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
            }
            responseObj = Response.status(Status.ACCEPTED).header("Location", lcmOccusUri).build();
        } catch (final VNFLCMServiceException exception) {
            final Status errorCode = getHttpErrorCode(exception);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(errorCode.toString(), exception.getMessage(), vnfInstanceId,
                    errorCode.getReasonPhrase(), errorCode.getStatusCode());
            responseObj = Response.status(errorCode).entity(problemDetail).build();
        } catch (final Exception exception) {
            logger.debug(exception.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.toString(), exception.getMessage(),
                    vnfInstanceId, Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseObj = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        return responseObj;

    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfRestService#scaleVnf(java. lang.String,
     * com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.model.ScaleVnfRequest, javax.ws.rs.core.UriInfo)
     */
    public Response scaleVnf(final String vnfInstanceId, final ScaleVnfRequest scaleVnfRequest, @Context final UriInfo uriInfo, String userName, String idempotencykey) {
        logger.info("-- Starting scaling - vnfinstanceid-" + vnfInstanceId);
        Response responseObj = null;
        ScaleResponseDto scaleDto = null;
        String evnfmHostname = "";
        String lcmOccusUri = "";
        final RequestValidator<ScaleVnfRequest> reqValidationHelper = new RequestValidator<ScaleVnfRequest>();
        reqValidationHelper.validate(scaleVnfRequest);
        try {
            final VnfScaleOpConfig vnfScaleOpConfig = new VnfScaleOpConfig();
            vnfScaleOpConfig.setScaleRequestJson(this.serializeObjectTojson(scaleVnfRequest));
            vnfScaleOpConfig.setRequestSourceType(RequestSourceType.NBI.value());
            vnfScaleOpConfig.setUserName(userName);
            vnfScaleOpConfig.setIdempotencyKey(idempotencykey);
            final VnfScale vnfScale = translateVnfScaleToDto(scaleVnfRequest);
            vnfScale.setVnfInstanceId(vnfInstanceId);
            vnfScale.setVnfmUseCaseSource("EM");
            scaleDto = vnfService.scaleVNF(vnfScale, vnfScaleOpConfig);
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
                    lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.LCM_OP_OCCR_BASE_URI + scaleDto.getLcmOp().getId();
                    logger.info("lcmOccusUri {}", lcmOccusUri);
                }
            } catch (final Exception e) {
                logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
            }
            responseObj = Response.status(Status.ACCEPTED).header("Location", lcmOccusUri).build();
        } catch (final VNFLCMServiceException exception) {
            final Status errorCode = getHttpErrorCode(exception);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(errorCode.toString(), exception.getMessage(), vnfInstanceId,
                    errorCode.getReasonPhrase(), errorCode.getStatusCode());
            responseObj = Response.status(errorCode).entity(problemDetail).build();
        } catch (final Exception exception) {
            logger.debug(exception.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.toString(), exception.getMessage(),
                    vnfInstanceId, Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseObj = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return responseObj;
    }

    /**
     * @param vnfScale
     * @param scaleVnfRequest
     */
    private VnfScale translateVnfScaleToDto(final ScaleVnfRequest scaleVnfRequest) {
        VnfScale vnfScale = new VnfScale();
        vnfScale = new VnfScale();
        vnfScale.setAspectId(scaleVnfRequest.getAspectId());
        vnfScale.setNumberOfSteps(scaleVnfRequest.getNumberOfSteps());
        vnfScale.setOperationType(OperationType.valueOf(scaleVnfRequest.getType().toString()));
        final com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam additionalParam = new com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam();
        if (scaleVnfRequest.getAdditionalParams() != null && scaleVnfRequest.getAdditionalParams() != null) {
            additionalParam.setAdditionalAttributes(scaleVnfRequest.getAdditionalParams());
            vnfScale.setAdditionalParam(additionalParam);
        }
        return vnfScale;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfRestService#operateVnf(java .lang.String,
     * com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.model.OperateVnfRequest, javax.ws.rs.core.UriInfo)
     */
    public Response operateVnf(final String vnfInstanceId, final OperateVnfRequest operateVnfRequest, @Context final UriInfo uriInfo) {
        logger.info("-- Starting operate - vnfinstanceid-" + vnfInstanceId);
        Response responseObj = null;
        String evnfmHostname = "";
        String lcmOccusUri = "";
        final RequestValidator<OperateVnfRequest> reqValidationHelper = new RequestValidator<OperateVnfRequest>();
        reqValidationHelper.validate(operateVnfRequest);
        try {
            final OperateVnfDto operateVnfDto = translateOperateVnfToDto(operateVnfRequest, vnfInstanceId);
            vnfService.operateVnf(operateVnfDto);

            // @TODO : in future - add link for new LCM operation created
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
                    lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.LCM_OP_OCCR_BASE_URI;
                    logger.info("lcmOccusUri {}", lcmOccusUri);
                }
            } catch (final Exception e) {
                logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
            }

            responseObj = Response.status(Status.ACCEPTED).header("Location", lcmOccusUri).build();
        } catch (final VNFLCMServiceException exception) {
            final Status errorCode = getHttpErrorCode(exception);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(errorCode.toString(), exception.getMessage(), vnfInstanceId,
                    errorCode.getReasonPhrase(), errorCode.getStatusCode());
            responseObj = Response.status(errorCode).entity(problemDetail).build();
        } catch (final Exception exception) {
            logger.debug(exception.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.toString(), exception.getMessage(),
                    vnfInstanceId, Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseObj = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return responseObj;
    }

    /**
     * @param operateVnfRequest
     * @param vnfInstanceId
     * @return OperateVnfDto
     */
    private OperateVnfDto translateOperateVnfToDto(final OperateVnfRequest operateVnfRequest, final String vnfInstanceId) {
        final OperateVnfDto operateVnfDto = new OperateVnfDto();
        operateVnfDto.setChangeStateTo(VnfOperationalStateType.valueOf(operateVnfRequest.getChangeStateTo().toString()));
        operateVnfDto.setVnfId(vnfInstanceId);
        operateVnfDto.setVnfmUseCaseSource("EM");
        return operateVnfDto;
    }

    private ModifyVnf translateToVnfModifyDto(final VnfInfoModificationRequest modifyVnfInfo) {
        final ObjectMapper objectMapper = new ObjectMapper();
        if (modifyVnfInfo != null) {
            final ModifyVnf modifyVnf = new ModifyVnf();
            if (modifyVnfInfo.getVnfInstanceName() != null && !modifyVnfInfo.getVnfInstanceName().isEmpty()) {
                modifyVnf.setVnfName(modifyVnfInfo.getVnfInstanceName().trim());
            }
            if (modifyVnfInfo.getVnfInstanceDescription() != null
                    && !modifyVnfInfo.getVnfInstanceDescription().isEmpty()) {
                modifyVnf.setVnfDescription(modifyVnfInfo.getVnfInstanceDescription().trim());
            }
            if (modifyVnfInfo.getVnfPkgId() != null) {
                modifyVnf.setOnboardedVnfPkgInfoId(modifyVnfInfo.getVnfPkgId().trim());
            }

            // Additional parameters are generally not provided in modify request as per
            // SOL002
            // This is specific change done for TIM.
            if (modifyVnfInfo.getAdditionalParams() != null) {
                if (modifyVnfInfo.getAdditionalParams().getVnfdId() != null) {
                    modifyVnf.setVnfdId(modifyVnfInfo.getAdditionalParams().getVnfdId().trim());
                }

                if (modifyVnfInfo.getAdditionalParams().getVnfSoftwareVersion() != null) {
                    modifyVnf.setVnfSoftwareVersion(modifyVnfInfo.getAdditionalParams().getVnfSoftwareVersion());
                }

                if (modifyVnfInfo.getAdditionalParams().getVnfdVersion() != null) {
                    modifyVnf.setVnfdVersion(modifyVnfInfo.getAdditionalParams().getVnfdVersion());
                }
            } else {
                // If modify VNF has 'vnfPkgId'present, that means package needs to be updated
                // That may be the upgrade use case.
                if (modifyVnfInfo.getVnfPkgId() != null) {
                    final String vnfPkgId = modifyVnfInfo.getVnfPkgId().trim();
                    logger.info("VnfRestServiceImpl: CreateVnf() : Reading the nfvo configuration.");
                    NfvoConfig nfvoConfigData = null;
                    try {
                        final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
                        nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
                        final VnfDescriptorDTO vnfDescriptorDTO = fetchQueryPackage(null, nfvoConfigData, vnfPkgId);
                        modifyVnf.setOnboardedVnfPkgInfoId(vnfDescriptorDTO.getOnboardedPackageInfoId());
                        modifyVnf.setVnfdId(vnfDescriptorDTO.getVnfdId());
                        modifyVnf.setVnfdVersion(vnfDescriptorDTO.getVnfdversion());
                        modifyVnf.setVnfSoftwareVersion(vnfDescriptorDTO.getVnfSoftwareVersion());
                    } catch (final NfvoCallException exception) {
                        logger.warn("VnfRestServiceImpl: modifyVnf(), nfvo configuration not found. {}",
                                exception.getMessage());
                    } catch (final Exception e) {
                        logger.error("Exception occured while invoking query vnf package.", e);
                    }
                }
            }

            if (modifyVnfInfo.getVnfConfigurableProperties() != null
                    && !modifyVnfInfo.getVnfConfigurableProperties().isEmpty()) {
                try {
                    modifyVnf.setVnfConfigurableProperties(
                            objectMapper.writeValueAsString(modifyVnfInfo.getVnfConfigurableProperties()));
                } catch (final JsonProcessingException jsonProcessingException) {
                    logger.warn("Exception while parsing VnfConfigurable properties from request");
                }
            }

            if (modifyVnfInfo.getExtensions() != null && !modifyVnfInfo.getExtensions().isEmpty()) {
                try {
                    modifyVnf.setExtensions(objectMapper.writeValueAsString(modifyVnfInfo.getExtensions()));
                } catch (final JsonProcessingException jsonProcessingException) {
                    logger.warn("Exception while parsing Extensions from request");
                }
            }

            if (modifyVnfInfo.getMetadata() != null && !modifyVnfInfo.getMetadata().isEmpty()) {
                try {
                    modifyVnf.setMetadata(objectMapper.writeValueAsString(modifyVnfInfo.getMetadata()));
                } catch (final JsonProcessingException jsonProcessingException) {
                    logger.warn("Exception while parsing Metadata from request");
                }
            }
            // modifyVnf.setVnfcInfoModifications(modifyVnfInfo.getVnfcInfoModifications);
            modifyVnf.setVnfmUseCaseSource("EM");
            return modifyVnf;
        }
        return null;
    }

    /**
     * @param createVnfRequest
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    public static PackageManagementListenerData populatePackageManagementData(final String vnfdId, final String vnfPkgId) {
        final PackageManagementListenerData processPackageManagementData = new PackageManagementListenerData();
        processPackageManagementData.setVnfdId(vnfdId);
        processPackageManagementData.setVnfPkgId(vnfPkgId);
        return processPackageManagementData;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfRestService#healVnf(java.lang.String, com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.HealVnfRequest,
     * javax.ws.rs.core.UriInfo)
     */
    public Response healVnf(final String vnfInstanceId, final HealVnfRequest healVnfRequest, final UriInfo uriInfo, String userName) {
        logger.info("-- Starting healing - vnfinstanceid-" + vnfInstanceId);
        Response responseObj = null;
        HealResponseDto healDto = null;
        String evnfmHostname = "";
        String lcmOccusUri = "";
        final RequestValidator<HealVnfRequest> reqValidationHelper = new RequestValidator<HealVnfRequest>();
        reqValidationHelper.validate(healVnfRequest);
        try {
            final VnfHealOpConfig vnfHealOpConfig = new VnfHealOpConfig();
            vnfHealOpConfig.setHealRequestJson(this.serializeObjectTojson(healVnfRequest));
            vnfHealOpConfig.setRequestSourceType(RequestSourceType.NBI.value());
            vnfHealOpConfig.setUserName(userName);
            final VnfHeal vnfHeal = translateVnfHealToDto(healVnfRequest);
            vnfHeal.setVnfInstanceId(vnfInstanceId);
            vnfHeal.setVnfmUseCaseSource("EM");
            healDto = vnfService.healVNF(vnfHeal, vnfHealOpConfig);
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
                    lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VNFLCM_URI + Constants.LCM_OP_OCCR_BASE_URI + healDto.getLcmOp().getId();
                    logger.info("lcmOccusUri {}", lcmOccusUri);
                }
            } catch (final Exception e) {
                logger.error("Unable create links by reading nfvo config json file {}", e.getMessage());
            }

            responseObj = Response.status(Status.ACCEPTED).header("Location", lcmOccusUri).build();
        } catch (final VNFLCMServiceException exception) {
            final Status errorCode = getHttpErrorCode(exception);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(errorCode.toString(), exception.getMessage(), vnfInstanceId,
                    errorCode.getReasonPhrase(), errorCode.getStatusCode());
            responseObj = Response.status(errorCode).entity(problemDetail).build();
        } catch (final Exception exception) {
            logger.debug(exception.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.toString(), exception.getMessage(),
                    vnfInstanceId, Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseObj = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }
        return responseObj;
    }

    /**
     * @param healVnfRequest
     * @return
     */
    private VnfHeal translateVnfHealToDto(final HealVnfRequest healVnfRequest) {
        final VnfHeal vnfHeal = new VnfHeal();
        vnfHeal.setCause(healVnfRequest.getCause());
        if (healVnfRequest.getAdditionalParams() != null && healVnfRequest.getAdditionalParams() != null) {
            final com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam additionalParam = new com.ericsson.oss.services.vnflcm.api_base.model.AdditionalParam();
            additionalParam.setAdditionalAttributes(healVnfRequest.getAdditionalParams());
            vnfHeal.setAdditionalParam(additionalParam);
        }
        vnfHeal.setHealScript(healVnfRequest.getHealScript());
        return vnfHeal;
    }

    private void deletePackage(final String fileToDelete) throws IOException{
        try {
            Files.walk(Paths.get(fileToDelete))
            .map(Path::toFile)
            .sorted((o1, o2) -> -o1.compareTo(o2))
            .forEach(File::delete);
        } catch(final IOException ex) {
            logger.error(ex.getMessage());
            throw new IOException();
        }
    }
}
