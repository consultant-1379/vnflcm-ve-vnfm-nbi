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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfPackageRestService;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.VnfPackageService;
import com.ericsson.oss.services.vnflcm.api_base.dto.QueryOpConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfPackage;
import com.ericsson.oss.services.vnflcm.common.dataTypes.Constants;

/**
 * @author zamibha
 *
 *         Impl class to get details of on-boarded VNF packages.
 */
public class VnfPackageRestServiceImpl implements VnfPackageRestService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @EServiceRef
    private VnfPackageService vnfPackageService;

    public static final String VNF_PRODUCT_NAME = "vnfProductName";

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getVnfPackagesInfo() {
        logger.info("VnfPackageRestServiceImpl : getVnfPackagesInfo() : get vnf packages available.");
        Response response = null;
        try {
            final QueryOpConfig queryOpConfig = new QueryOpConfig();
            final Map<String, List<String>> queryParameters = getQueryParameters();
            if (queryParameters != null) {
                if (queryParameters.size() > 1 || !queryParameters.containsKey(VNF_PRODUCT_NAME)) {
                    final String errorMessage = "Query Parameter contains parameter other than vnfProductName. No other Query Parameter allowed";
                    logger.error(errorMessage);
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), errorMessage, "",
                            "", Status.BAD_REQUEST.getStatusCode());
                    response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                    return response;
                }
                final Map<String, Map<String,List<String>>> queryParamsMap = new HashMap<>();
                queryParamsMap.put(Constants.CUSTOM_PARAMS, queryParameters);
                queryOpConfig.setQueryParameters(queryParamsMap);
            }
            final List<VnfPackage> vnfPackageList = vnfPackageService.getAllVnfPackagesInfo(queryOpConfig);
            if (vnfPackageList != null && !vnfPackageList.isEmpty()) {
                response = Response.status(Status.OK) .entity(vnfPackageList).build();
            }
            else {
                logger.debug("No on-boarded packages available.");
                response = Response.status(Status.OK) .entity(Collections.EMPTY_LIST).build();
            }
        } catch (VNFLCMServiceException e) {
            logger.error("Service exception while processing request: {}", e.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), "",
                    "", Status.INTERNAL_SERVER_ERROR.getStatusCode());
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        } catch (Exception e) {
            logger.error("Unknown exception while processing request: {}", e.getMessage());
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), "",
                    "", Status.INTERNAL_SERVER_ERROR.getStatusCode());
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
        }

        return response;
    }

    private Map<String, List<String>> getQueryParameters() {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        if (queryParameters != null && !queryParameters.isEmpty()) {
            logger.info("VnfPackageRestServiceImpl : getQueryParameters() : queryParameters {}", uriInfo.getQueryParameters());
            final Map<String, List<String>> queryParams = new HashMap<>();

            final Set<String> keySet = queryParameters.keySet();
            final Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                final String queryParam = iterator.next();
                final List<String> queryParamValue = queryParameters.get(queryParam);
                queryParams.put(queryParam, queryParamValue);
            }
            return queryParams;
        }
        return null;
    }

    /**
     * @param uriInfo the uriInfo to set
     */
    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    /**
     * @param vnfPackageService the vnfPackageService to set
     */
    public void setVnfPackageService(VnfPackageService vnfPackageService) {
        this.vnfPackageService = vnfPackageService;
    }

}
