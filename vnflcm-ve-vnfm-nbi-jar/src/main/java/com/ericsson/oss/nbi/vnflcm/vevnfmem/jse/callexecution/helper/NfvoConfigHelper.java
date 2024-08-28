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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionErrorCode;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NfvoConfig;
import com.ericsson.oss.services.vnflcm.api_base.LcmOperationService;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperationResponse;
import com.ericsson.oss.services.vnflcm.common.dataTypes.Constants;
import com.ericsson.oss.services.vnflcm.common.dataTypes.NfvoEndPoints;
import com.ericsson.oss.services.vnflcm.common.dataTypes.NfvoProperties;
import com.ericsson.oss.services.vnflcm.common.util.PasswordEncryptionUtil;
import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;
import com.ericsson.oss.services.vnflcm.nfvo.api.NfvoService;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.NfvoDTO;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.NfvoEndPointsDTO;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.NfvoPropertiesDTO;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.ReadNfvoConfigRequest;
import com.ericsson.oss.services.vnflcm.nfvo.api.dto.SubscriptionFilterDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NfvoConfigHelper {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public NfvoConfig readNfvoConfig(final String lcmOccId, final String notificationType) throws NfvoCallException {
        LOGGER.info("NfvoConfigHelper: readNfvoConfig(): Start reading nfvo config with lcmOccId {}.", lcmOccId);
        final ServiceFinderBean finder = new ServiceFinderBean();
        final NfvoService nfvoService = finder.find(NfvoService.class);
        final LcmOperationService lcmOpService = finder.find(LcmOperationService.class);
        final SubscriptionFilterDto subscriptionFilterDto = new SubscriptionFilterDto();
        NfvoDTO activeNfvoConfiguration = null;
        try {
            if (lcmOccId != null && !lcmOccId.isEmpty()) {
                final LcmOperationResponse lcmOperationOccurence = lcmOpService.getLcmOperation(lcmOccId);
                final String nfvoId = lcmOperationOccurence.getNfvoId();
                LOGGER.info("NfvoConfigHelper: readNfvoConfig(): nfvoId {}.", lcmOperationOccurence.getNfvoId());
                final ReadNfvoConfigRequest readNfvo = new ReadNfvoConfigRequest();
                final NfvoDTO nfvo = new NfvoDTO();
                nfvo.setId(nfvoId);
                readNfvo.setNfvo(nfvo);
                final List<NfvoDTO> nfvos = nfvoService.getNfvo(readNfvo);
                if (nfvos != null && !nfvos.isEmpty()) {
                    activeNfvoConfiguration = nfvos.get(0); // Only one with 1 Id
                    if (activeNfvoConfiguration != null) {
                        LOGGER.info("NfvoConfigHelper: readNfvoConfig(): nfvoId: {}", activeNfvoConfiguration.getId());
                    }
                } else {
                    LOGGER.info("NfvoConfigHelper : readNfvoConfig() : Small stack, probably never comes upto this point.");
                }
            } else {
                LOGGER.info("NfvoConfigHelper: readNfvoConfig(): lcmOccId is null, hence reading activeNfvoConfiguration.");
                if(notificationType != null) {
                    subscriptionFilterDto.setNotificationType(notificationType);
                }
                LOGGER.info("Subscription filter {}", subscriptionFilterDto.toString());
                activeNfvoConfiguration = nfvoService.getActiveNfvoConfiguration(subscriptionFilterDto);
                if (activeNfvoConfiguration != null) {
                    LOGGER.info("NfvoConfigHelper: readNfvoConfig(): nfvoId: {}", activeNfvoConfiguration.getId());
                }
            }
        } catch (final VNFLCMServiceException e) {
            e.printStackTrace();
        }
        if (activeNfvoConfiguration == null) {
            throw new NfvoCallException(CallExecutionErrorCode.CONFIGURATION_NOT_EXIST.getValue(),
                    "No active nfvo configuration present in database.");
        } else {
            NfvoConfig nfvoConfigData = null;
            try {
                nfvoConfigData = prepareNfvoConfig(activeNfvoConfiguration);
                // Though enm host name does not belong to nfvo config.
                // Just reading it here to avoid the multiple place changes, as we defined this in nfvoconfig.json earlier.
                final String enmHostName = readEnmHostNameFromEnv();
                nfvoConfigData.setEnmHostName(enmHostName);
                LOGGER.info("NfvoConfigHelper : readNfvoConfig() : nfvo config is read and correctly converted.");
                return nfvoConfigData;
            } catch (final Exception e) {
                LOGGER.error("Error in reading nfvo configuration", e);
                throw new NfvoCallException(CallExecutionErrorCode.CONFIGURATION_NOT_EXIST.getValue(), "Error in reading nfvo configuration.");
            }
        }
    }

    private String readEnmHostNameFromEnv() {
        try {
            return ReadPropertiesUtility.readConfigProperty(Constants.ENM_HOST_NAME);
        } catch (final Exception e) {
            LOGGER.error("readEnmHostNameFromEnv() : Error in reading ENM host name from environment {}", e.getMessage());
        }
        return null;
    }

    private NfvoConfig prepareNfvoConfig(final NfvoDTO activeNfvoConfiguration) throws Exception {
        LOGGER.info("NfvoConfigHelper : prepareNfvoConfig() : start preparing nfvo config.");
        final NfvoConfig nfvoConfig = new NfvoConfig();
        nfvoConfig.setIsNfvoAvailable("true"); // If active configuration found, it is true
        if (activeNfvoConfiguration.getIsGrantSupported() != null) {
            nfvoConfig.setIsGrantSupported(activeNfvoConfiguration.getIsGrantSupported().toString());
        }
        nfvoConfig.setIsNotificationSupported(activeNfvoConfiguration.getIsNotificationSupported());
        nfvoConfig.setAuthType(activeNfvoConfiguration.getAuthType());
        nfvoConfig.setNfvoType(activeNfvoConfiguration.getNfvoType());
        nfvoConfig.setSubscriptionId(activeNfvoConfiguration.getSubscriptionId());
        final TenancyDetails tenancyDetail = getTenancyDetail(activeNfvoConfiguration);
        if (tenancyDetail != null) {
            nfvoConfig.setVdcId(tenancyDetail.getVdcId());
            nfvoConfig.setTenantid(tenancyDetail.getTenantId());
            nfvoConfig.setTenantName(tenancyDetail.getTenantName());
        }
        nfvoConfig.setUsername(activeNfvoConfiguration.getUserName());
        nfvoConfig.setPassword(PasswordEncryptionUtil.decryptPassword(activeNfvoConfiguration.getPassword()));
        nfvoConfig.setOrVnfmVersion(activeNfvoConfiguration.getOrVnfmVersion());
        nfvoConfig.setNotificationAckRequired(activeNfvoConfiguration.getNotificationAckRequired());
        if(activeNfvoConfiguration.getIdempotencyHeaderName() != null && !activeNfvoConfiguration.getIdempotencyHeaderName().isEmpty()) {
            nfvoConfig.setIdempotencyHeaderName(activeNfvoConfiguration.getIdempotencyHeaderName());
        }

        setNfvoEndPoints(nfvoConfig, activeNfvoConfiguration);
        setNfvoProperties(nfvoConfig, activeNfvoConfiguration);
        LOGGER.info("NfvoConfigHelper : prepareNfvoConfig() : nfvo config data prepared.");
        return nfvoConfig;
    }

    /**
     * currently only 3 properties are defined, if more are required that to be supported here also
     * 
     * @param nfvoConfig
     * @param activeNfvoConfiguration
     */
    private void setNfvoProperties(final NfvoConfig nfvoConfig, final NfvoDTO activeNfvoConfiguration) {
        LOGGER.info("NfvoConfigHelper : setNfvoEndPoints() : reading nfvo properties.");
        if (activeNfvoConfiguration.getNfvoProperties() != null && !activeNfvoConfiguration.getNfvoProperties().isEmpty()) {
            final List<NfvoPropertiesDTO> nfvoProperties = activeNfvoConfiguration.getNfvoProperties();
            for (final NfvoPropertiesDTO nfvoProperty : nfvoProperties) {
                try {
                    final NfvoProperties property = NfvoProperties.fromValue(nfvoProperty.getPropKey());
                    final String value = nfvoProperty.getPropValue();
                    switch (property) {
                    case SUPPORTED_NOTIFICATIONS:
                        nfvoConfig.setNfvoSupportedNotificationTypes(value);
                        break;
                    case TOKEN_NAME:
                        nfvoConfig.setStaticAuthenticationTokenName(value);
                        break;
                    case TOKEN_VALUE:
                        nfvoConfig.setStaticAuthenticationTokenValue(value);
                        break;
                    case IS_FALLBACK_BEST_EFFORT_SUPPORTED:
                        if (value != null && !value.isEmpty()) {
                            if (value.equalsIgnoreCase("Yes")) {
                                nfvoConfig.setIsFallbackBestEffortSupported(true);
                            } else {
                                nfvoConfig.setIsFallbackBestEffortSupported(false);
                            }
                        }
                        break;
                    case BASIC_AUTH_IMPL_SUPPORTED_AS_ECM:
                        if (value != null && !value.isEmpty()) {
                            if (value.equalsIgnoreCase("Yes")) {
                                nfvoConfig.setBasicAuthImplSameAsEO(true);
                            } else {
                                nfvoConfig.setBasicAuthImplSameAsEO(false);
                            }
                        }
                        break;
                    default:
                        break;
                    }
                } catch (final IllegalArgumentException ex) {
                    LOGGER.error("NfvoConfigHelper : setNfvoEndPoints() : property {} is not valid.", nfvoProperty.getPropKey());
                }
            }
        }
    }

    /**
     * this method will set the different endpoints which are accessible from vnflcm for Grant, Notification etc.
     * 
     * @param nfvoConfig
     * @param activeNfvoConfiguration
     */
    private void setNfvoEndPoints(final NfvoConfig nfvoConfig, final NfvoDTO activeNfvoConfiguration) {
        LOGGER.info("NfvoConfigHelper : setNfvoEndPoints() : reading nfvo end points.");
        if (activeNfvoConfiguration.getNfvoEndPoints() != null && !activeNfvoConfiguration.getNfvoEndPoints().isEmpty()) {
            final List<NfvoEndPointsDTO> nfvoEndPoints = activeNfvoConfiguration.getNfvoEndPoints();
            for (final NfvoEndPointsDTO nfvoEndPoint : nfvoEndPoints) {
                try {
                    final NfvoEndPoints endPoint = NfvoEndPoints.fromValue(nfvoEndPoint.getEndPointName());
                    final String endPointValue = nfvoEndPoint.getEndPointUrl();
                    final String endPointUrl = activeNfvoConfiguration.getBaseUrl() + endPointValue;
                    switch (endPoint) {
                    case AUTHENTICATION:
                        nfvoConfig.setNfvoAuthUrl(endPointUrl);
                        break;
                    case CREATENOTIFICATION:
                        nfvoConfig.setCreateNotificationUrl(endPointUrl);
                        break;
                    case DELETENOTIFICATION:
                        nfvoConfig.setDeleteNotificationUrl(endPointUrl);
                        break;
                    case GRANT:
                        nfvoConfig.setGrantUrl(endPointUrl);
                        break;
                    case LCMNOTIFICATION:
                        nfvoConfig.setLifecycleNotificationUrl(endPointUrl);
                        break;
                    case PACKAGEMANAGEMENT:
                        nfvoConfig.setPackageManagementUrl(endPointUrl);
                        break;
                    case QUERYNFVOVDC:
                        nfvoConfig.setQueryNfvoVdcListUrl(endPointUrl);
                        break;
                    default:
                        break;
                    }
                } catch (final IllegalArgumentException ex) {
                    LOGGER.error("NfvoConfigHelper : setNfvoEndPoints() : endpoint {} is not valid.", nfvoEndPoint.getEndPointName());
                }
            }
        }
    }

    /**
     * This method will pase the tenancy details json which is stored in DB. Currently this structure is not finalized or concluded. The
     * implementation may change later base on the multi tenancy support and other changes in how to map tenancy details with vim connection
     * details.
     * 
     * @param activeNfvoConfiguration
     * @return
     */
    public TenancyDetails getTenancyDetail(final NfvoDTO activeNfvoConfiguration) {
        final String tenancyDetails = activeNfvoConfiguration.getTenancyDetails(); // This is Json
        if (tenancyDetails != null) {
            final TenancyDetails tenancyDetail = new TenancyDetails();
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                final JsonNode tenancyDetailsJsonString = objectMapper.readTree(tenancyDetails);
                if (tenancyDetailsJsonString.isArray()) {
                    final Iterator<JsonNode> elements = tenancyDetailsJsonString.elements();
                    while (elements.hasNext()) {
                        final JsonNode node = elements.next();
                        final String defaultTenant = node.get("defaultTenant").asText();
                        if ("true".equalsIgnoreCase(defaultTenant)) { // currently it's default tenant only for ECM, it may change for other nfvos
                            final String tenantId = node.get("tenantId").asText();
                            final String tenantName = node.get("tenantName").asText();
                            final JsonNode vdcDetailsNode = node.get("vdcDetails");
                            if (vdcDetailsNode.isArray()) {
                                final Iterator<JsonNode> vdcDetailsItr = vdcDetailsNode.elements();
                                while (vdcDetailsItr.hasNext()) {
                                    final JsonNode vdc = vdcDetailsItr.next();
                                    if ("true".equalsIgnoreCase(vdc.get("defaultVdc").asText())) {
                                        final String vdcId = vdc.get("id").asText();
                                        tenancyDetail.setVdcId(vdcId);
                                        break;
                                    }
                                }
                            }
                            tenancyDetail.setTenantId(tenantId);
                            tenancyDetail.setTenantName(tenantName);
                            break;
                        }
                    }
                }
                return tenancyDetail;
            } catch (final IOException e) {
                LOGGER.error("NfvoConfigHelper : getTenancyDetail() : error occured while parsing tenancy details json.");
                return null;
            }
        }
        return null;
    }

    class TenancyDetails {
        String tenantId;
        String tenantName;
        String vdcId;

        /**
         * @return the tenantId
         */
        public String getTenantId() {
            return tenantId;
        }

        /**
         * @param tenantId the tenantId to set
         */
        public void setTenantId(final String tenantId) {
            this.tenantId = tenantId;
        }

        /**
         * @return the vdcId
         */
        public String getVdcId() {
            return vdcId;
        }

        /**
         * @return the tenantName
         */
        public String getTenantName() {
            return tenantName;
        }

        /**
         * @param tenantName the tenantName to set
         */
        public void setTenantName(final String tenantName) {
            this.tenantName = tenantName;
        }

        /**
         * @param vdcId the vdcId to set
         */
        public void setVdcId(final String vdcId) {
            this.vdcId = vdcId;
        }
    }

}
