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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.CallExecutionDataRetriever;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallDataCreator;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallExecutor;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services.NfvoCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.services.packageManagement.*;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMPackageManagementService;

/**
 * @author xnareku
 * 
 */
public class PackageManagementHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @EServiceRef
    private VNFLCMPackageManagementService packageManagementService;
    public NfvoCallExecution prepareExecutionData() {
        NfvoCallExecution nfvoCallExecution = null;
        final CallExecutionDataRetriever packageManagementDataRetrieval = new VeVnfmemPackageManagementDataCreation();
        final RestCallDataCreator packageManagementRestCallDataCreator = new VeVnfmemPackageManagementRestCallDataCreation();
        final RestCallExecutor pkgMgtRestCallExecutor = new VeVnfmemPackageManagementRestCallExecution(
                CallExecutionType.PACKAGE);
        nfvoCallExecution = new VeVnfmemPackageManagementCallExecution(
                packageManagementDataRetrieval,
                packageManagementRestCallDataCreator, pkgMgtRestCallExecutor);
        LOGGER.info("Returning execution for Package",
                PackageManagementHelper.class);

        return nfvoCallExecution;
    }
}
