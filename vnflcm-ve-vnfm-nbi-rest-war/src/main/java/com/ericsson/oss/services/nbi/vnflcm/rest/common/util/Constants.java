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
package com.ericsson.oss.services.nbi.vnflcm.rest.common.util;

public class Constants {

    public static final String PERCENTAGE = "percentage";

    //Response.Status doesn't support NOT_IMPLEMENTED yet
    public static final int STATUS_NOT_IMPLEMENTED = 501;

    public static final int INTERNAL_ERROR = 500;

    public static final int SERVICE_UNAVAILABLE = 503;

    public static final int ACCEPTED = 202;

    public static final int NOT_FOUND = 404;

    public static final int UN_PROCESSABLE_ENTITY = 422;

    public static final String USER_NAME_HEADER = "X-Tor-UserID";

    public static final int BAD_REQUEST = 400;

    public static final String INTERNAL_ERROR_TITLE = "Internal Server Error";

    public static final String LCM_OP_OCCR_BASE_URI = "/vnflcm/v1/vnf_lcm_op_occs/";

    public static final String QUERY_VNF_BASE_URI = "/vnflcm/v1/vnf_instances/";

    public static final String ALL_FIELDS = "all_fields";

    public static final String FIELDS = "fields";

    public static final String EXCLUDE_FIELDS = "exclude_fields";

    public static final String EXCLUDE_DEFAULT = "exclude_default";

    public static final String DATA_VNFD_SPECIFIC = "dataVNFDSpecific";

    public static final String VNF_PRODUCT_NAME = "vnfProductName";

    public static final String VNF_PROVIDER = "vnfProvider";

    public static final String VNF_SOFTWARE_VERSION = "vnfSoftwareVersion";

    public static final String VNFD_VERSION = "vnfdVersion";

    public static final String FLAVOUR_ID = "flavourId";

    public static final String VNFLCM_OP_CONFIG = "vnfLcmOperationsConfiguration";

    public static final String SCALE = "scale";

    public static final String SCALE_MORE_THAN_1_SUPPORTED = "scalingByMoreThanOneStepSupported";

    public static final String SCALE_ASPECTS = "scalingAspect";

    public static final String ASPECT_ID = "id";

    public static final String DESCRIPTION = "description";

    public static final String MAX_SCALE_LEVEL = "maxScaleLevel";

    public static final String NAME = "name";

    public static final String PACKAGE_DOWNLOAD = "packageDownload";

    public static final String MIN_SCALE_LEVEL = "minScaleLevel";

    public static final String ENM_HOST_NAME = "enmHostName";

    public static final String VNF_CLUSTER_DNS = "vnflcmIngressHostName";

    public static final String HTTP_URI = "https://";

    public static final String VNFLCM_URI = "/vnflcm/";

    public static final String VEVNFMEM_URI = "/vevnfmem";

    public static final String ARE_VNFD_PARAMS_SENSITIVE = "areVnfdParamsSensitive";

    public static final String PAGINATION_ENABLED = "isPaginationEnabled";

    public static final String PAGE_SIZE = "queryPageSize";

    public static final String MAX_PAGE_SIZE = "maxPageSize";

    public static final String SIZE = "size";

    public static final String NEXTPAGE_OPQ_MARKER = "nextpage_opaque_marker";

    public static final String WRONG_QUERY_PARAM = "Wrong query parameter";

    public static final String CUSTOM_PARAMS = "customParams";

    public static final String STANDARD_PARAMS = "standardParams";
}