/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.services.packageManagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.CallExecutionDataRetriever;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper.NfvoConfigHelper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.PackageManagementNbiData;

public class VeVnfmemPackageManagementDataCreation implements
        CallExecutionDataRetriever {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VeVnfmemPackageManagementDataCreation.class);

    @Override
    public CallExecutionData getCallExecutionData(
            final ProcessDataDto processPackageManagementData)
            throws NfvoCallException {
        final NbiData nbiNotificationData = fetchOrvnfmData();
        final EcmRequestData ecmData = getEcmData();
        final VnflcmServiceData vnflcmServiceData = getServiceData(processPackageManagementData);
        final CallExecutionData completeData = new CallExecutionData();
        LOGGER.info("Start OrVnfmPackageManagementDataCreation.getCallExecutionData... ");
        completeData.setNbiNotificationData(nbiNotificationData);
        completeData.setEcmData(ecmData);
        completeData.setVnflcmServiceData(vnflcmServiceData);
        LOGGER.info("Fetch Package  Data successfully", this);
        return completeData;
    }

    private VnflcmServiceData getServiceData(final ProcessDataDto processDataDto) {
        final VnflcmServiceData serviceData = new VnflcmServiceData();
        serviceData.setVnfId(processDataDto.getPackageManagementListenerData()
                .getVnfdId());
        serviceData.setVnfPkgId(processDataDto.getPackageManagementListenerData().getVnfPkgId());
        return serviceData;
    }

    private NbiData fetchOrvnfmData() throws NfvoCallException {
        LOGGER.info("Start VeVnfmemPackageManagementDataCreation.getCallExecutionData... ");
        final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
        final NfvoConfig nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null); // LcmOccId will nto be available during packagemanagement
        checkMandatoryProperties(nfvoConfigData);
        final PackageManagementNbiData orvnfmData = new PackageManagementNbiData();
        orvnfmData.setUrl(nfvoConfigData.getPackageManagementUrl());
        orvnfmData.setUsername(nfvoConfigData.getUsername());
        orvnfmData.setPassword(nfvoConfigData.getPassword());
        orvnfmData.setSubscriptionId(nfvoConfigData.getSubscriptionId());
        orvnfmData.setEnmHostname(nfvoConfigData.getEnmHostName());
        orvnfmData.setIsNfvoAvailable(nfvoConfigData.getIsNfvoAvailable());
        orvnfmData.setStaticAuthenticationTokenName(nfvoConfigData
                .getStaticAuthenticationTokenName());
        orvnfmData.setStaticAuthenticationTokenValue(nfvoConfigData
                .getStaticAuthenticationTokenValue());
        orvnfmData.setNfvoType(nfvoConfigData.getNfvoType());
        LOGGER.info("End OrVnfmPackageManagementDataCreation.getCallExecutionData ");
        return orvnfmData;
    }

    public EcmRequestData getEcmData() throws NfvoCallException {
        final NfvoConfigHelper nfvoConfigHelper = new NfvoConfigHelper();
        final NfvoConfig nfvoConfigData = nfvoConfigHelper.readNfvoConfig(null, null);
        checkMandatoryProperties(nfvoConfigData);
        final EcmRequestData ecmData = new EcmRequestData();
        ecmData.setEcmAuthUrl(nfvoConfigData.getNfvoAuthUrl());
        ecmData.setEcmAuthType(nfvoConfigData.getAuthType());
        ecmData.setTenantId(nfvoConfigData.getTenantid());
        ecmData.setVdcId(nfvoConfigData.getVdcId());
        LOGGER.info("Fetched ECM specific Data successfully", this);
        return ecmData;

    }

    public void checkMandatoryProperties(final NfvoConfig nfvoConfigData)
            throws NfvoCallException {
        String message = "";
        if (nfvoConfigData == null) {
            LOGGER.error("Unable to read config data from /ericsson/vnflcm/data location");
            throw new NfvoCallException(
                    CallExecutionErrorCode.CONFIGURATION_PROP_EMPTY.getValue(),
                    CallExecutionErrorMessage.CONFIGURATION_PROP_EMPTY
                            .getValue());

        } else if (nfvoConfigData.getPassword().isEmpty()) {
            message = message + "password" + " ";
        } else if (nfvoConfigData.getUsername().isEmpty()) {
            message = message + "username" + " ";
        } else if (nfvoConfigData.getPackageManagementUrl().isEmpty()) {
            message = message + "url" + " ";
        } else if (nfvoConfigData.getTenantid().isEmpty()) {
            message = message + "tenantId" + " ";
        } else if (nfvoConfigData.getNfvoAuthUrl().isEmpty()) {
            message = message + "ecmAuthUrl" + " ";
        }

        if (!message.trim().isEmpty()) {
            LOGGER.error("Unable to read config data from /ericsson/vnflcm/data location");
            throw new NfvoCallException(
                    CallExecutionErrorCode.CONFIGURATION_PROP_EMPTY.getValue(),
                    "Empty configuration properties in nfvoconfig.json at /ericsson/vnflcm/data "
                            + message);
        }
    }
}
