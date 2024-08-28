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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons;

public class CallExecutionConstants {

    public static final String FAILED_MESSAGE = "errorMessage";
    public static final String NOTIFICATION = "notification";
    public static final String GRANT = "grant";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String HOSTNAME = "hostname";
    public static final String URL = "url";
    public static final String ID = "id";
    public static final String VNF_INSTANCE_ID = "vnfInstanceId";
    public static final String TENANT_ID = "tenantid";
    public static final String TENANT = "tenant";
    public static final String TENANT_NAME = "tenantName";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";
    public static final String VDC_ID = "vdcId";
    public static final String VIM_ZONE_ID = "vimZoneId";
    public static final String CONTENT_TYPE = "content-type";
    public static final String AUTH_TOKEN = "authtoken";
    public static final String OAUTH2 = "OAUTH2_CLIENT_CREDENTIALS";
    public static final String OAUTH2_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String OAUTH2_AUTH_BODY = "grant_type=client_credentials";
    public static final String OPERATION = "operation";
    public static final String VNFINSTANCE_KEY = "vnfInstance";
    public static final String VNFLCM_OPP_OCC = "vnfLcmOpOcc";
    public static final String VNF_INSTANCE_REST_URL = "/vnflcm/v1/vnf_instances/";
    public static final String OPERATION_OCC_BASE_URL = "/vnflcm/v1/vnf_lcm_op_occs/";
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    public static final String PATTERN_PASSWORD = "(?:password=(?:[^\\,\\\"\\]]*))|(?:(?:password\\\"\\:)(?:\\\")?(?:[^\\,\\\"\\]]*))";

    public static final String PATTERN_CLIENTID = "(?:clientId=(?:[^\\,\\\"\\]]*))|(?:(?:clientId\\\"\\:)(?:\\\")?(?:[^\\,\\\"\\]]*))";

    public static final String PATTERN_CLIENTSECRET = "(?:clientSecret=(?:[^\\,\\\"\\]]*))|(?:(?:clientSecret\\\"\\:)(?:\\\")?(?:[^\\,\\\"\\]]*))";

    public static final String PATTERN_AUTHORIZATION = "(?:Authorization=(?:[^\\,\\\"\\]]*))|(?:(?:Authorization\\\"\\:)(?:\\\")?(?:[^\\,\\\"\\]]*))";

}
