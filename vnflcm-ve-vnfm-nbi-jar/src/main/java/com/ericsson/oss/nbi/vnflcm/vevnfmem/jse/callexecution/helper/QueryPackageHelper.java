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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.CallExecutionDataRetriever;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallDataCreator;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallExecutor;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services.NfvoCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.services.packageManagement.VeVnfmemQueryPackageDataCreation;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.services.packageManagement.VeVnfmemQueryPackageRestCallDataCreation;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.services.packageManagement.VeVnfmemQueryPackageRestCallExecution;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.services.packageManagement.VeVnfmemQueryPackageCallExecution;

/**
 * @author xnareku
 * 
 */
public class QueryPackageHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public NfvoCallExecution prepareExecutionData() {
        NfvoCallExecution nfvoCallExecution = null;
        final CallExecutionDataRetriever queryPackageDataRetrieval = new VeVnfmemQueryPackageDataCreation();
        final RestCallDataCreator queryPackageRestCallDataCreator = new VeVnfmemQueryPackageRestCallDataCreation();
        final RestCallExecutor queryPackageRestCallExecutor = new VeVnfmemQueryPackageRestCallExecution(
                CallExecutionType.QUERYPACKAGE);
        nfvoCallExecution = new VeVnfmemQueryPackageCallExecution(queryPackageDataRetrieval,queryPackageRestCallDataCreator, queryPackageRestCallExecutor);
        LOGGER.info("Returning execution for Query Package{}",QueryPackageHelper.class);

        return nfvoCallExecution;
    }
}
