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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.config.ConfigurationEnvironment;
import com.ericsson.oss.itpf.sdk.config.classic.ConfigurationEnvironmentBean;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.Utils;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfLcmOperation;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.vnflcm.api_base.LcmOperationService;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperation;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperationConfig;
import com.ericsson.oss.services.vnflcm.api_base.dto.LcmOperationResponse;
import com.ericsson.oss.services.vnflcm.api_base.dto.QueryOpConfig;
import com.ericsson.oss.services.vnflcm.common.dataTypes.OperationStatus;
import com.ericsson.oss.services.vnflcm.common.dataTypes.QuerySelectors;
import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;

public class VnfLcmOperationRestServiceSol002V241Impl {

    private static final Logger LOGGER = LoggerFactory.getLogger(VnfLcmOperationRestServiceSol002V241Impl.class);

    private LcmOperationService lcmOperationService;

    @Context
    private UriInfo uriInfo;

    @SuppressWarnings("serial")
    private static Map<String, String> ramlToModelMapping = new HashMap<String, String>() {
        {
            put("operationState", "status");
            put("operation", "operationType");
            put("id", "id");
            put("startTime", "startTime");
            put("stateEnteredTime", "stateEnteredTime");
            put("vnfInstanceId", "vnfInstanceId");
            put("grantId", "grantId");
        }
    };

    @SuppressWarnings("serial")
    private static Map<String, String> operationStateMap = new HashMap<String, String>() {
        {
            put("STARTING", "10");
            put("COMPLETED", "0");
            put("PROCESSING", "1");
            put("FAILED", "2");
            put("ROLLED_BACK", "11");
            put("ROLLING_BACK", "12");
        }
    };

    private static final String INVALID_OP_AND_STATE_VALUE = "99999";

    @SuppressWarnings("serial")
    private static Map<String, String> operationMap = new HashMap<String, String>() {
        {
            put("INSTANTIATE", "0");
            put("TERMINATE", "1");
            put("SCALE_OUT", "2");
            put("SCALE_IN", "3");
        }
    };

    @SuppressWarnings("serial")
    private static List<String> excludeFieldsList = new ArrayList<String>() {
        {
            add("operationParams");
        }
    };

    @SuppressWarnings("serial")
    private static List<String> excludeDefaultList = new ArrayList<String>() {
        {
            add("resourceChanges");
        }
    };

    public VnfLcmOperationRestServiceSol002V241Impl() {

    }

    public VnfLcmOperationRestServiceSol002V241Impl(final LcmOperationService lcmOperationService) {
        this.lcmOperationService = lcmOperationService;
    }

    public Response getLcmOperationOcc(final String vnfLcmOpOccId) {
        LOGGER.info("Getting LcmOperationOcc from service for vnfLcmOpOccId : ", vnfLcmOpOccId);
        VnfLcmOperation lcmOperation = new VnfLcmOperation();
        try {
            final LcmOperationResponse lcmOperationResponse = lcmOperationService.getLcmOperationOccurence(vnfLcmOpOccId.trim());
            lcmOperation = VeVnfmemSol002V241LcmOperationInfo.getVnflcmOperation(lcmOperationResponse);
        } catch (final VNFLCMServiceException exception) {
            LOGGER.error("Exception occured while invoking getLcmOperationOcc", exception, this);
            return getResponseForException(exception, vnfLcmOpOccId, "get");
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking getLcmOperationOcc", e, this);
            throw new WebApplicationException(e);
        }
        return Response.status(Status.OK).entity(lcmOperation).build();
    }

    public Response getAllLcmOperationOcc(final UriInfo uriInfo, String paginationFlag) {
        Response response = null;
        LOGGER.info("Getting all LcmOperationOcc from service ");
        final QueryOpConfig queryOpConfig = new QueryOpConfig();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        Map<String, Map<String, List<String>>> queryParametersMap = getQueryParameters(queryParameters);

     // Check if size is present in query, else set default
        int page_size = 15;
        int max_page_size = 100;
        String isPaginationEnabled = null;
        try {
            isPaginationEnabled = ReadPropertiesUtility.readConfigProperty(Constants.PAGINATION_ENABLED);
            LOGGER.info("isPaginationEnabled = "+isPaginationEnabled);
        } catch (final Exception e) {
            LOGGER.error("Exception while fetching pagination Enabled Config Param {}",e.getMessage());
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
        if (queryParametersMap != null && !queryParametersMap.isEmpty() && isPaginationEnabled!= null && isPaginationEnabled.equalsIgnoreCase("YES")) {
            final Map<String, List<String>> customParamsMap = queryParametersMap.get(Constants.CUSTOM_PARAMS);
            try {
                final ConfigurationEnvironment configEnvironment = new ConfigurationEnvironmentBean();
                if(customParamsMap != null && !customParamsMap.isEmpty()) {
                    if (!customParamsMap.containsKey(Constants.SIZE)) {
                        final List<String> sizeParamList = new ArrayList<>();
                        final Object pageSizeConfigParam = configEnvironment.getValue(Constants.PAGE_SIZE);
                        if (pageSizeConfigParam != null && !pageSizeConfigParam.toString().isEmpty() && Integer.parseInt(pageSizeConfigParam.toString()) > 0) {
                            page_size = Integer.parseInt(pageSizeConfigParam.toString());
                            sizeParamList.add(pageSizeConfigParam.toString());
                            LOGGER.info("Page size from config params: {}",page_size);
                        } else {
                            sizeParamList.add(String.valueOf(page_size));
                        }
                        customParamsMap.put(Constants.SIZE, sizeParamList);
                    } else {
                        if (Utils.isInteger(customParamsMap.get(Constants.SIZE).get(0))) {
                            final int query_page_size = Integer.valueOf(customParamsMap.get(Constants.SIZE).get(0));
                            if (query_page_size < 0) {
                                final String errorMessage = "Invalid page number::"+query_page_size+", page number must be greater than 0";
                                LOGGER.error(errorMessage);
                                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Constants.WRONG_QUERY_PARAM, errorMessage, "",
                                        Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST.getStatusCode());
                                response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                                return response;
                            } else if (query_page_size == 0) {
                                LOGGER.debug("query page size is zero, returning empty response.");
                                return Response.status(Status.OK).entity(new ArrayList<>()).build();
                            }
                        } else {
                            final String errorMessage = "Invalid query page size value for size parameter::"+customParamsMap.get(Constants.SIZE).get(0);
                            LOGGER.error(errorMessage);
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
                    queryOpConfig.setQueryParameters(queryParametersMap);
                    page_size = Integer.valueOf(customParamsMap.get(Constants.SIZE).get(0));
                    try {
                        final Object maxPageSizeConfigParam = configEnvironment.getValue(Constants.MAX_PAGE_SIZE);
                        if (maxPageSizeConfigParam != null && !maxPageSizeConfigParam.toString().isEmpty()) {
                            max_page_size = Integer.parseInt(maxPageSizeConfigParam.toString());
                            LOGGER.info("Max Page size from config params: {}",max_page_size);
                        }
                    } catch (final Exception e) {
                        LOGGER.info("Exception while fetching maximum page size from configuration. Hence using default max page size 100");
                    }
                    if (page_size>max_page_size) {
                        final String errorMessage = "Maximum records per page exceeds maxPageSize:"+max_page_size+". Please provide query page size less than maxPageSize value";
                        LOGGER.error(errorMessage);
                        final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), errorMessage, "",
                                "", Status.BAD_REQUEST.getStatusCode());
                        response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                        return response;
                    }
                }
            } catch (final Exception e) {
                LOGGER.error("Exception while fetching page size from configuration. Hence using the default size 15");
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
                        LOGGER.info("Page size from config params: {}",page_size);
                    } else {
                        sizeParamList.add(String.valueOf(page_size));
                    }
                    final Map<String, List<String>> queryParams = new HashMap<>();
                    queryParams.put(Constants.SIZE, sizeParamList);
                    if(queryParametersMap == null) {
                        queryParametersMap = new HashMap<>();
                    }
                    queryParametersMap.put(Constants.CUSTOM_PARAMS, queryParams);
                    queryOpConfig.setQueryParameters(queryParametersMap);
                }
            } catch (final Exception e) {
                LOGGER.error("Exception while fetching queryPageSize Config Param {}",e.getMessage());
            }
        }
        if(queryParametersMap != null && queryParametersMap.get(Constants.STANDARD_PARAMS) != null && !queryParametersMap.get(Constants.STANDARD_PARAMS).isEmpty()) {
            queryOpConfig.setQueryParameters(queryParametersMap);
        }
        List<VnfLcmOperation> vnfLcmOperations = null;
        try {
            final List<LcmOperationResponse> lcmOperationResponses = lcmOperationService.getAllLcmOperationOccurence(queryOpConfig);
            if (lcmOperationResponses != null) {
                vnfLcmOperations = new ArrayList<VnfLcmOperation>();
                for (final LcmOperationResponse lcmOperationResponse : lcmOperationResponses) {
                    vnfLcmOperations.add(VeVnfmemSol002V241LcmOperationInfo.getVnflcmOperation(lcmOperationResponse));
                }
                try {
                    final List<VnfLcmOperation> vnfLcmOperationsAfterApplyingSelectors = applySelectors(vnfLcmOperations, queryParameters);
                    if (vnfLcmOperationsAfterApplyingSelectors != null && !vnfLcmOperationsAfterApplyingSelectors.isEmpty()) {
                        vnfLcmOperations = vnfLcmOperationsAfterApplyingSelectors;
                    }
                    LOGGER.debug("vnfLcmOperations after applying selectors: {}", vnfLcmOperations);
                } catch (final Exception ex) {
                    ex.printStackTrace();
                    LOGGER.error("Exception occured on applying the seletors, so return result as it is.", ex, this);
                }
            }
        } catch (final VNFLCMServiceException e) {
            LOGGER.error("Exception occured while invoking getAllLcmOperationOcc", e, this);
            return getResponseForException(e, "", "get");
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking getAllLcmOperationOcc", e, this);
            throw new WebApplicationException(e);
        }
        // Preparation for Header
        if (queryOpConfig.getQueryParameters() != null && !queryOpConfig.getQueryParameters().isEmpty()) {
            final Map<String, List<String>> customParamsMap = queryOpConfig.getQueryParameters().get(Constants.CUSTOM_PARAMS);
            if(customParamsMap != null && !customParamsMap.isEmpty()) {
                final Set<Entry<String, List<String>>> queryParamEntrySet = customParamsMap.entrySet();
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
        if (vnfLcmOperations != null && !vnfLcmOperations.isEmpty() && vnfLcmOperations.size() < page_size) {
            final List<String> headerLinks = getHeaderLinks(vnfLcmOperations.get(0).getStartTime(), null);
            return Response.status(Status.OK).header("Link", headerLinks).entity(vnfLcmOperations).build();
        } else if (vnfLcmOperations != null && !vnfLcmOperations.isEmpty() && isPaginationEnabled.equalsIgnoreCase("YES")) {
            final List<String> headerLinks = getHeaderLinks(vnfLcmOperations.get(0).getStartTime(), vnfLcmOperations.get(vnfLcmOperations.size()-1).getStartTime());
            return Response.status(Status.OK).header("Link", headerLinks).entity(vnfLcmOperations).build();
        } else {
            return Response.status(Status.OK).entity(vnfLcmOperations).build();
        }
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
                LOGGER.info("ingressHostName could not be retrieved.Hence using evnfmHostname property");
            }
            if (evnfmHostname != null && !evnfmHostname.isEmpty()) {
                lcmOccusUri = Constants.HTTP_URI + evnfmHostname + Constants.VEVNFMEM_URI + Constants.LCM_OP_OCCR_BASE_URI.substring(0,Constants.LCM_OP_OCCR_BASE_URI.length() - 1);
                LOGGER.info("lcmOccusUri {}", lcmOccusUri);
            } else {
                return headerLinks; //Return empty
            }
        } catch (final Exception e) {
            LOGGER.error("Unable create links {}", e.getMessage());
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
            LOGGER.error("Error parsing Date {}", ex.getMessage());
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
            LOGGER.error("readevnfmHostnameFromEnv() : Error in reading ENM host name from environment {}", e.getMessage());
        }
        return null;
    }
    
    private Map<String, Map<String, List<String>>> getQueryParameters(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, List<String>> filtersFromQueryParams = getFiltersFromQueryParams(queryParameters);
        if (filtersFromQueryParams != null && !filtersFromQueryParams.isEmpty()) {
            LOGGER.info("VnfLcmOperationRestServiceImpl : getQueryParameters() : queryParameters {}", queryParameters);
            final Map<String, List<String>> customQueryParams = new HashMap<>();
            final Map<String, List<String>> standardQueryParams = new HashMap<>();
            final Map<String, Map<String, List<String>>> queryParamsMap = new HashMap<>();
            final Set<String> keySet = filtersFromQueryParams.keySet();
            final Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                final String queryParamKey = iterator.next();
                List<String> queryParamValue = filtersFromQueryParams.get(queryParamKey);
                if(!queryParamKey.contains(".")) {
                    customQueryParams.put(queryParamKey, queryParamValue);
                    queryParamsMap.put(Constants.CUSTOM_PARAMS, customQueryParams);
                } else {
                    final String parameterName = queryParamKey.substring(0, queryParamKey.lastIndexOf("."));
                    final String operator = queryParamKey.substring(queryParamKey.lastIndexOf("."));
                    final String modelProperty = ramlToModelMapping.get(parameterName);
                    if ("operationState".equalsIgnoreCase(parameterName)) {
                        queryParamValue = mapOperationStateValues(queryParamValue);
                        LOGGER.info("VnfLcmOperationRestServiceImpl : getQueryParameters() : queryParamValue for operationState attribute : {}",
                                queryParamValue);
                    } else if ("operation".equalsIgnoreCase(parameterName)) {
                        queryParamValue = mapOperationValues(queryParamValue);
                        LOGGER.info("VnfLcmOperationRestServiceImpl : getQueryParameters() : queryParamValue for operation attribute : {}",
                                queryParamValue);
                    }
                    if (queryParamValue != null && !queryParamValue.isEmpty() && modelProperty != null) {
                        standardQueryParams.put(modelProperty + operator, queryParamValue);
                        queryParamsMap.put(Constants.STANDARD_PARAMS, standardQueryParams);
                    }
                }
            }
            LOGGER.info("Query parameters map: {}",queryParamsMap);
            return queryParamsMap;
        }
        return null;
    }

    private List<String> mapOperationValues(final List<String> queryParamValues) {
        final List<String> updatedQaramValue = new ArrayList<>();
        for (final String value : queryParamValues) {
            final String mapValue = operationMap.get(value);
            if (mapValue == null) {
                updatedQaramValue.add(INVALID_OP_AND_STATE_VALUE);
            } else {
                updatedQaramValue.add(mapValue);
            }
        }
        return updatedQaramValue;
    }

    private List<String> mapOperationStateValues(final List<String> queryParamValues) {
        final List<String> updatedQaramValue = new ArrayList<>();
        for (final String value : queryParamValues) {
            final String mapValue = operationStateMap.get(value);
            if (mapValue == null) {
                updatedQaramValue.add(INVALID_OP_AND_STATE_VALUE);
            } else {
                updatedQaramValue.add(mapValue);
            }
        }
        return updatedQaramValue;
    }

    /**
     * This method will seggregate the selectors from query parameter applied separatly.
     * 
     * @param queryParams
     */
    private Map<String, List<String>> getSelectorsFromQueryParams(final MultivaluedMap<String, String> queryParameters) {
        if (queryParameters != null && !queryParameters.isEmpty()) {
            final Map<String, List<String>> selectorQueryParamMap = new HashMap<>();
            final List<String> selectors = new ArrayList<String>();
            for (final QuerySelectors qs : QuerySelectors.values()) {
                selectors.add(qs.value());
            }
            final Set<String> queryParamKeySet = queryParameters.keySet();
            final Iterator<String> queryParamKeySetIterator = queryParamKeySet.iterator();

            while (queryParamKeySetIterator.hasNext()) {
                final String queryParam = queryParamKeySetIterator.next();
                if (selectors.contains(queryParam)) {
                    final List<String> queryParamValue = queryParameters.get(queryParam);
                    for (final String value : queryParamValue) {
                        final String[] queryParamValues = value.split(",");
                        selectorQueryParamMap.put(queryParam, Arrays.asList(queryParamValues));
                    }
                }
            }
            return selectorQueryParamMap;
        }
        return null;
    }

    /**
     * This method will seggregate the filters from query parameter
     * 
     * @param queryParams
     */
    private static Map<String, List<String>> getFiltersFromQueryParams(final MultivaluedMap<String, String> queryParams) {
        if (queryParams != null && !queryParams.isEmpty()) {
            final Map<String, List<String>> filterParamMap = new HashMap<>();
            final List<String> selectors = new ArrayList<String>();
            for (final QuerySelectors qs : QuerySelectors.values()) {
                selectors.add(qs.value());
            }
            final Set<String> queryParamKeySet = queryParams.keySet();
            final Iterator<String> queryParamKeySetIterator = queryParamKeySet.iterator();
            while (queryParamKeySetIterator.hasNext()) {
                final String queryParam = queryParamKeySetIterator.next();
                if (!selectors.contains(queryParam)) {
                    final List<String> queryParamValue = queryParams.get(queryParam);
                    for (final String value : queryParamValue) {
                        final String[] queryParamValues = value.split(",");
                        filterParamMap.put(queryParam, Arrays.asList(queryParamValues));
                    }
                }
            }
            return filterParamMap;
        }
        return null;
    }

    /**
     * This method applies the selectors if any provided in query param. selectors to be applied on the complex json fields. Currently supported
     * fields are : operationParams and resourceChanges
     * 
     * @param vnfLcmOperations
     * @param queryParameters
     * @return
     */
    private List<VnfLcmOperation> applySelectors(final List<VnfLcmOperation> vnfLcmOperations, final MultivaluedMap<String, String> queryParameters) {
        final Map<String, List<String>> selectorsFromQueryParams = getSelectorsFromQueryParams(queryParameters);
        LOGGER.info("VnfLcmOperationRestServiceImpl : selectorsFromQueryParams=" + selectorsFromQueryParams);
        final List<VnfLcmOperation> vnfLcmOperationsAfterApplyingSelectors = new ArrayList<>();
        if (selectorsFromQueryParams != null && !selectorsFromQueryParams.isEmpty()) {
            LOGGER.info("VnfLcmOperationRestServiceImpl : getQueryParameters() : selectors in query param {}", selectorsFromQueryParams);
            final Iterator<String> selectorKeyIterator = selectorsFromQueryParams.keySet().iterator();
            while (selectorKeyIterator.hasNext()) {
                final String selctorMapKey = selectorKeyIterator.next();
                LOGGER.info("selctorMapKey=" + selctorMapKey);
                if (selctorMapKey != null && !selctorMapKey.isEmpty()) {
                    if (selctorMapKey.equalsIgnoreCase(Constants.ALL_FIELDS)) { // return the model as is.
                        return vnfLcmOperations;
                    }
                    if (selctorMapKey.equalsIgnoreCase(Constants.EXCLUDE_DEFAULT)) {
                        applyExcludeDefaultSelector(vnfLcmOperations, vnfLcmOperationsAfterApplyingSelectors, selectorsFromQueryParams);
                    }
                    if (selctorMapKey.equalsIgnoreCase(Constants.FIELDS)) {
                        // currently there is only one field 'resourceChanges' which can be added in 'fields' selectors.
                        // So return the list as it is.
                        return vnfLcmOperations;
                    }
                    if (selctorMapKey.equalsIgnoreCase(Constants.EXCLUDE_FIELDS)) {
                        // Currently there is only one field 'operationParams' which can be added in exclude_fields selectors.
                        applyExcludeFields(vnfLcmOperations, vnfLcmOperationsAfterApplyingSelectors, selectorsFromQueryParams);
                    }
                }
            }
        } else {
            // Apply default selector, which is on the resourceChanges attribute.
            LOGGER.info("VnfLcmOperationRestServiceImpl : getQueryParameters() : no selectors provided apply default (exclude_default)");
            applyExcludeDefaultSelector(vnfLcmOperations, vnfLcmOperationsAfterApplyingSelectors, selectorsFromQueryParams);
        }
        return vnfLcmOperationsAfterApplyingSelectors;
    }

    private void applyExcludeDefaultSelector(final List<VnfLcmOperation> vnfLcmOperations,
                                             final List<VnfLcmOperation> vnfLcmOperationsAfterApplyingSelectors,
                                             Map<String, List<String>> selectorsFromQueryParams) {
        for (final VnfLcmOperation vnfLcmOperation : vnfLcmOperations) {
            //vnfLcmOperation.setResourceChanges(null);
            if (selectorsFromQueryParams == null || selectorsFromQueryParams.isEmpty()) {
                selectorsFromQueryParams = new HashMap<>();
                final List<String> excludeDefaultList = new ArrayList<String>();
                excludeDefaultList.add("resourceChanges");
                selectorsFromQueryParams.put(Constants.EXCLUDE_DEFAULT, excludeDefaultList);
            }
            final List<String> excludeFields = selectorsFromQueryParams.get(Constants.EXCLUDE_DEFAULT);
            final List<String> excludeAttributes = new ArrayList<String>();
            for (final String excludeField : excludeFields) {
                final String[] excludeAttribute = excludeField.split(",");
                excludeAttributes.addAll(Arrays.asList(excludeAttribute));
            }
            final Field[] declaredFields = vnfLcmOperation.getClass().getDeclaredFields();
            for (final Field declaredField : declaredFields) {
                if (excludeDefaultList.contains(declaredField.getName()) && excludeAttributes.contains(declaredField.getName())) {
                    // Set the new value to null for excluded field.
                    setPropertyToNull(vnfLcmOperation, declaredField, null);
                }
            }
            vnfLcmOperationsAfterApplyingSelectors.add(vnfLcmOperation);
        }
    }

    /**
     * This method excludes all those fileds from model which aer requested currently 'operationParams' is the only field to be added in this
     * selector.
     * 
     * @param vnfLcmOperations
     * @param vnfLcmOperationsAfterApplyingSelectors
     */
    private void applyExcludeFields(final List<VnfLcmOperation> vnfLcmOperations, final List<VnfLcmOperation> vnfLcmOperationsAfterApplyingSelectors,
                                    final Map<String, List<String>> selectorsFromQueryParams) {
        for (final VnfLcmOperation vnfLcmOperation : vnfLcmOperations) {
            final List<String> excludeFields = selectorsFromQueryParams.get(Constants.EXCLUDE_FIELDS);
            final List<String> excludeAttributes = new ArrayList<String>();
            for (final String excludeField : excludeFields) {
                final String[] excludeAttribute = excludeField.split(",");
                excludeAttributes.addAll(Arrays.asList(excludeAttribute));
            }
            final Field[] declaredFields = vnfLcmOperation.getClass().getDeclaredFields();
            for (final Field declaredField : declaredFields) {
                if (excludeFieldsList.contains(declaredField.getName()) && excludeAttributes.contains(declaredField.getName())) {
                    // Set the new value to null for excluded field.
                    setPropertyToNull(vnfLcmOperation, declaredField, null);
                }
            }
            vnfLcmOperationsAfterApplyingSelectors.add(vnfLcmOperation);
        }
    }

    /**
     * This method calls the setter on given property on the provided model.
     * 
     * @param model
     * @param modelAttribField
     * @param newContainingObject
     */
    private static <T> void setPropertyToNull(final T model, final Field modelAttribField, final Object newContainingObject) {
        PropertyDescriptor pd1;
        try {
            pd1 = new PropertyDescriptor(modelAttribField.getName(), model.getClass());
            pd1.getWriteMethod().invoke(model, newContainingObject);
            LOGGER.info("setProperty" + newContainingObject);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOGGER.error("VnfLcmOperationRestServiceImpl : setPropertyToNull() : issue setting the property.");
        }
    }

    private Response getResponseForException(final VNFLCMServiceException vnflcmServiceException, final String vnfLcmOpOccId,
                                             final String operationType) {
        Response response = null;
        switch (vnflcmServiceException.getType()) {
            case LIFECYCLE_OP_NOT_FOUND:
                if ("get".equals(operationType)) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Vnflcm operation occurence not found",
                            vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(), Constants.NOT_FOUND);
                    response = Response.status(Status.NOT_FOUND).entity(problemDetail).build();
                }
                break;
            case MANDATORY_PARAMETERS_MISSING:
                if ("get".equals(operationType)) {
                    final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Bad request from client recieved, operation id is null",
                            vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(), Constants.BAD_REQUEST);
                    response = Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
                }
                break;
            case ERROR_FETCHING_LIFECYCLEOP:
            case UNKNOWN:
            default:
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail("Internal Server Error occurred",
                        vnflcmServiceException.getMessage(), vnfLcmOpOccId, vnflcmServiceException.getType().name(), Constants.INTERNAL_ERROR);
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(problemDetail).build();
                break;
        }
        return response;
    }

    public Response updateLcmOperationOccStatus(final String vnfLcmOpOccId, final UriInfo uriInfo) {
        LOGGER.info("Updating LcmOperationOcc from service for vnfLcmOpOccId : {}", vnfLcmOpOccId);
        LcmOperation lcmOperation = new LcmOperation();
        final LcmOperationConfig lcmOperationConfig = new LcmOperationConfig();
        final MultivaluedMap<String,String> queryParameters = uriInfo.getQueryParameters();
        String operationState = queryParameters.getFirst("operationState");
        Integer stateValue = null;
        String lcmOperationResponse = null;
        if(operationState != null && !operationState.isEmpty()) {
            LOGGER.info("State change to {}", operationState);
            if(operationState.equalsIgnoreCase(OperationStatus.FAILED.name())) {
                stateValue = 2;
            } else if(operationState.equalsIgnoreCase(OperationStatus.CANCELLED.name())) {
                stateValue = 3;
            } else if(operationState.equalsIgnoreCase(OperationStatus.ROLLED_BACK.name())) {
                stateValue = 11;
            } else {
                final String errorMessage = "State value provided is not valid. Valid values are FAILED, CANCELLED and ROLLED_BACK";
                LOGGER.error(errorMessage);
                final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), errorMessage, "",
                        "", Status.BAD_REQUEST.getStatusCode());
                return Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
            }
        } else {
            final String errorMessage = "Operation state is mandatory query parameter. Please provide state and its value in request.";
            LOGGER.error(errorMessage);
            final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(Status.BAD_REQUEST.getReasonPhrase(), errorMessage, "",
                    "", Status.BAD_REQUEST.getStatusCode());
            return Response.status(Status.BAD_REQUEST).entity(problemDetail).build();
        }
        lcmOperation.setId(vnfLcmOpOccId);
        lcmOperation.setState(OperationStatus.fromValue(stateValue));
        try {
            if(lcmOperation.getId() != null){
                lcmOperationService.updateLcmOperation(lcmOperation, lcmOperationConfig);
                lcmOperationResponse = "{\"message\": \"OperationStatus \""+OperationStatus.fromValue(stateValue)+"\" is updated successfully.\"}";
            }
        } catch (final VNFLCMServiceException exception) {
            LOGGER.error("Exception occured while invoking updateLcmOperationOccStatus", exception, this);
            return getResponseForException(exception, vnfLcmOpOccId, "get");
        } catch (final Exception e) {
            LOGGER.error("Exception occured while invoking updateLcmOperationOccStatus", e, this);
            throw new WebApplicationException(e);
        }
        return Response.status(Status.OK).entity(lcmOperationResponse).build();
    }
}
