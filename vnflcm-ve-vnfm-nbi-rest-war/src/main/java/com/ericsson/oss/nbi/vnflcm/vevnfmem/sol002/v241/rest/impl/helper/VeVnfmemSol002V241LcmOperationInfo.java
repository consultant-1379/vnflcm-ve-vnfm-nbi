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

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionConstants;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionErrorCode;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionErrorMessage;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NfvoConfig;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ChangeCurrentVnfPkgRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.HealVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.LcmOperationStateType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.LcmOperationType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.Link;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ResourceChanges;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ScaleVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.TerminateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInfoModificationRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfLcmOperation;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnflcmOperationLinks;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.Error;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperationResponse;
import com.ericsson.oss.services.vnflcm.common.dataTypes.Constants;
import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class VeVnfmemSol002V241LcmOperationInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(VeVnfmemSol002V241LcmOperationInfo.class);

    public static VnfLcmOperation getVnflcmOperation(final LcmOperationResponse lcmOperationResponse) {
        final VnfLcmOperation lcmOperation = new VnfLcmOperation();

        if (lcmOperationResponse != null && lcmOperationResponse.getVnfInstanceId() != null && lcmOperationResponse.getVnfInstanceId() != null) {

            lcmOperation.setId(lcmOperationResponse.getVnfLcmOpOccId());
            lcmOperation.setVnfInstanceId(lcmOperationResponse.getVnfInstanceId());
            if (lcmOperationResponse.getGrantId() != null) {
                lcmOperation.setGrantId(lcmOperationResponse.getGrantId());
            }
            if (lcmOperationResponse.getStateEnteredTime() != null) {
                lcmOperation.setStateEnteredTime(Utils.toISO8601UTC(lcmOperationResponse.getStateEnteredTime()));
            }
            if (lcmOperationResponse.getStartTime() != null) {
                lcmOperation.setStartTime(Utils.toISO8601UTC(lcmOperationResponse.getStartTime()));
            }
            final LcmOperationStateType lcmOperationStateType = getOperationStatus(lcmOperationResponse);
            if (lcmOperationStateType != null) {
                lcmOperation.setOperationState(lcmOperationStateType);
            }
            lcmOperation.setOperation(getOperationType(lcmOperationResponse));
            //lcmOperation.setRequestSourceType(lcmOperationResponse.getRequestSourceType());
            lcmOperation.setIsAutomaticInvocation(lcmOperationResponse.isAutomaticInvocation());
            lcmOperation.setIsCancelPending(lcmOperationResponse.isCancelPending());
            lcmOperation.setOperationParams(getOperationParamsToModel(lcmOperationResponse));
            lcmOperation.set_links(getVnfLcmOperationLinks(lcmOperationResponse));
            if (lcmOperationResponse.getResourceChanges() != null) {
                lcmOperation.setResourceChanges(getResourceChanges(lcmOperationResponse));
            }
            lcmOperation.setError(convertProblemDetailsJsonString(lcmOperationResponse.getErrorMessage()));
        }
        return lcmOperation;
    }

    private static Error convertProblemDetailsJsonString(final String problemDetailsJson) {
        LOGGER.info("convertProblemDetails : Start");
        final Error error = new Error();
        JsonNode rootNode = null;
        if (problemDetailsJson != null && !problemDetailsJson.isEmpty()) {
            LOGGER.info("convertProblemDetails : problemDetailsJson {}", problemDetailsJson);
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                rootNode = objectMapper.readTree(problemDetailsJson);
                final JsonNode errorNode = rootNode.get("error");
                if (errorNode != null) {
                    if (errorNode.has("title")) {
                        final String title = errorNode.get("title").textValue();
                        error.setTitle(title);
                    }
                    if (errorNode.has("details")) {
                        final String details = errorNode.get("details").textValue();
                        error.setDetails(details);
                    }
                    if (errorNode.has("status")) {
                        final String status = Integer.toString(errorNode.get("status").intValue());
                        error.setStatus(status);
                    }
                }
                return error;
            } catch (final JsonProcessingException e) {
                LOGGER.error("Error converting problemDetails :{}", e.getMessage());
            } catch (final IOException e) {
                LOGGER.error("Error converting problemDetails :{}", e.getMessage());
            } finally {
                // this block is to log error incase of unchecked exception..and not throwing the exception as the process has to proceed further...
                if ((rootNode == null)) {
                    LOGGER.error("Error Occured while parsing ProblemDetailsJson");
                    return null;
                }
            }
        }
        return null;
    }

    private static String fetchEnmHost() {
        try {
            return ReadPropertiesUtility.readConfigProperty(Constants.ENM_HOST_NAME);
        } catch (final Exception e) {
            LOGGER.error("readEnmHostNameFromEnv() : Error in reading ENM host name from environment {}", e.getMessage());
        }
        return null;
    }

    public static void checkMandatoryProperties(final NfvoConfig nfvoConfigData) throws NfvoCallException {
        String message = "";
        if (nfvoConfigData == null) {
            LOGGER.error("Unable to read config data from /ericsson/vnflcm/data location");
            throw new NfvoCallException(CallExecutionErrorCode.CONFIGURATION_PROP_EMPTY.getValue(),
                    CallExecutionErrorMessage.CONFIGURATION_PROP_EMPTY.getValue());

        } else if (nfvoConfigData.getEnmHostName().isEmpty()) {
            message = message + "enmHostName" + " ";
        }
        if (!message.trim().isEmpty()) {
            LOGGER.error("Unable to read config data from /ericsson/vnflcm/data location");
            throw new NfvoCallException(CallExecutionErrorCode.CONFIGURATION_PROP_EMPTY.getValue(),
                    "Empty configuration properties in nfvoconfig.json at /ericsson/vnflcm/data " + message);
        }
    }

    private static ResourceChanges getResourceChanges(final LcmOperationResponse lcmOperationResponse) {
        final ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(lcmOperationResponse.getResourceChanges(), ResourceChanges.class);
    }

    private static VnflcmOperationLinks getVnfLcmOperationLinks(final LcmOperationResponse lcmOperationResponse) {
        final VnflcmOperationLinks vnflcmOperationLinks = new VnflcmOperationLinks();
        String enmHostname = "";
        try {
            enmHostname = fetchEnmHost();

            final Link selfLink = new Link();
            selfLink.setHref(createHref(enmHostname, lcmOperationResponse.getVnfLcmOpOccId(), CallExecutionConstants.VNFLCM_OPP_OCC));
            vnflcmOperationLinks.setSelf(selfLink);

            final Link linkInstantiate = new Link();
            linkInstantiate.setHref(createHref(enmHostname, lcmOperationResponse.getVnfInstanceId(), CallExecutionConstants.VNFINSTANCE_KEY));
            vnflcmOperationLinks.setInstantiate(linkInstantiate);

        } catch (final Exception e) {
            LOGGER.error("Unable create links by reading nfvo config json file {}", e.getMessage());
        }
        return vnflcmOperationLinks;
    }

    private static String createHref(final String enmHostname, final String id, final String type) {
        String href = "";
        switch (type) {
            case CallExecutionConstants.VNFINSTANCE_KEY:
                href = "https://" + enmHostname + CallExecutionConstants.VNF_INSTANCE_REST_URL + id;
                break;
            case CallExecutionConstants.VNFLCM_OPP_OCC:
                href = "https://" + enmHostname + CallExecutionConstants.OPERATION_OCC_BASE_URL + id;
                break;
            default:
                break;
        }
        return href;
    }

    private static Object getOperationParamsToModel(final LcmOperationResponse lcmOperationResponse) {
        final ObjectMapper mapper = new ObjectMapper();
        final String lcmRequestJson = lcmOperationResponse.getLcmRequestJson();
        if (lcmRequestJson != null) {
            try {
                switch (lcmOperationResponse.getOperation()) {
                    case INSTANTIATION:
                        final InstantiateVnfRequest instantiateVnfRequestObject = mapper.readValue(lcmRequestJson, InstantiateVnfRequest.class);
                        return instantiateVnfRequestObject;
                    case TERMINATION:
                        // Since Terminate has different structure of additional params models in NBI and Service
                        // So needed an adapting method
                        final String convertTerminateRequest = convertTerminateRequest(lcmRequestJson);
                        TerminateVnfRequest terminateVNFRequestObject = null;
                        if (lcmRequestJson != null) {
                            terminateVNFRequestObject = mapper.readValue(convertTerminateRequest, TerminateVnfRequest.class);
                        }
                        return terminateVNFRequestObject;
                    case SCALE_OUT:
                    case SCALE_IN:
                        final ScaleVnfRequest scaleVnfRequestObject = mapper.readValue(lcmRequestJson, ScaleVnfRequest.class);
                        return scaleVnfRequestObject;
                    case HEAL:
                        final HealVnfRequest healVnfRequest = mapper.readValue(lcmRequestJson, HealVnfRequest.class);
                        return healVnfRequest;
                    case CHANGE_VNFPKG:
                        final ChangeCurrentVnfPkgRequest changeVnfRequest = mapper.readValue(lcmRequestJson, ChangeCurrentVnfPkgRequest.class);
                        return changeVnfRequest;
                    case MODIFY_INFO:
                        final VnfInfoModificationRequest modifyVnfRequest = mapper.readValue(lcmRequestJson, VnfInfoModificationRequest.class);
                        return modifyVnfRequest;
                    default:
                        throw new IllegalArgumentException("Invalid lcm operation type: " + lcmOperationResponse.getOperation());
                }
            } catch (final Exception e) {
                LOGGER.error("Error while deserializing request object " + e.getMessage());
            }
        }
        return null;
    }

    private static LcmOperationStateType getOperationStatus(final LcmOperationResponse lcmOperationResponse) {
        switch (lcmOperationResponse.getOperationStatus()) {
            case SUCCESS:
            case TERMINATED:
                return LcmOperationStateType.COMPLETED;
            case CANCELLED:
                return LcmOperationStateType.CANCELLED;
            case ROLLED_BACK:
                return LcmOperationStateType.ROLLED_BACK;
            case FAILED:
                return LcmOperationStateType.FAILED;
            case FAILED_TEMP:
                return LcmOperationStateType.FAILED_TEMP;
            case NOTSTARTED:
            case WORKFLOW_STARTED:
            case GRANT_REQ_SEND:
            case GRANT_RES_RECEIVED:
            case TERMINATION_IN_PROCESS:
                return LcmOperationStateType.PROCESSING;
            case PROCESSING:
                return LcmOperationStateType.PROCESSING;
            case STARTING:
                return LcmOperationStateType.STARTING;
            case ROLLING_BACK:
                return LcmOperationStateType.ROLLING_BACK;
            default:
                return null;
        }
    }

    private static LcmOperationType getOperationType(final LcmOperationResponse lcmOperationResponse) {
        switch (lcmOperationResponse.getOperation()) {
            case INSTANTIATION:
                return LcmOperationType.INSTANTIATE;
            case SCALE_IN:
            case SCALE_OUT:
                return LcmOperationType.SCALE;
            case TERMINATION:
                return LcmOperationType.TERMINATE;
            case HEAL:
                return LcmOperationType.HEAL;
            case CHANGE_VNFPKG:
                return LcmOperationType.CHANGE_VNFPKG;
            case MODIFY_INFO:
                return LcmOperationType.MODIFY_INFO;
            default:
                throw new IllegalArgumentException("Invalid lcm operation type: " + lcmOperationResponse.getOperation());
        }
    }

    private static String convertTerminateRequest(final String jsonRequestPayload) {
        LOGGER.info("convertTerminateRequest : Start");
        if (jsonRequestPayload != null && !jsonRequestPayload.isEmpty()) {
            LOGGER.info("convertTerminateRequest : jsonRequestPayload {}", jsonRequestPayload);
            final ObjectMapper objectMapper = new ObjectMapper();
            final ObjectNode baseTerminateRequestNode = objectMapper.createObjectNode();
            try {
                final JsonNode rootNode = objectMapper.readTree(jsonRequestPayload);
                if (rootNode.has("terminationType")) {
                    final String terminationType = rootNode.get("terminationType").asText();
                    baseTerminateRequestNode.put("terminationType", terminationType);
                }
                if (rootNode.has("gracefulTerminationTimeout")) {
                    final int gracefulTerminationTimeout = rootNode.get("gracefulTerminationTimeout").asInt();
                    baseTerminateRequestNode.put("gracefulTerminationTimeout", gracefulTerminationTimeout);
                }
                if (rootNode.has("additionalParams")) {
                    final JsonNode additionalParamsNode = rootNode.get("additionalParams");
                    final JsonNode additionalAttributesNode = additionalParamsNode.get("additionalAttributes");
                    baseTerminateRequestNode.put("additionalParams", additionalAttributesNode);
                }

                LOGGER.info("Converted Json request:{}", baseTerminateRequestNode);
                final String terminateRequest = objectMapper.writeValueAsString(baseTerminateRequestNode);
                return terminateRequest;
            } catch (final JsonProcessingException e) {
                LOGGER.error("Error converting Termiation request:{}", e.getMessage());
            } catch (final IOException e) {
                LOGGER.error("Error converting Termiation request:{}", e.getMessage());
            }
        }
        return null;
    }
}
