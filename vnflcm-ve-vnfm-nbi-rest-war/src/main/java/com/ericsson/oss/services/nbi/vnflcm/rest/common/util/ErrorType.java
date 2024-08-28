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

/**
 * Internal error codes and the corresponding HTTP status codes.
 *
 */
public enum ErrorType {

    //Header related error
    USER_HEADER_NOT_FOUND("9001", 400),

    //VNF Instantiation error code
    VNF_IDENTIFIER_NOT_FOUND("1101", 404), VNF_ALREADY_INSTANTIATED("1102", 409), WORKFLOW_MAPPING_NOT_FOUND("1103",
            500), INSTANCE_OPERATION_IN_PROGRESS_INSTANTIATE("1104", 409),

    VALIDATION_ERROR("1002", 422), INTERNAL_ERROR("1003", 500), NOT_IMPLEMENTED("1004", 501),

    //VNF Terminate error code
    VNF_IDENTIFIER_NOT_FOUND_TERMINATE("1501", 404), VNF_NOT_INSTANTIATED_TERMINATE("1502", 409), INSTANCE_OPERATION_IN_PROGRESS_TERMINATE("1503",
            409),

    //GET Lifecycle occurrences error code
    LIFE_CYCLE_OP_ID_NOTFOUND("1201", 404),

    //DELETE VNF Idenitifier error code
    DELETE_VNF_IDENTIFIER_NOT_FOUND("1401", 404), DELETE_VNF_IDENTIFIER_ALREADY_INSTANTIATED("1402",
            409), DELETE_VNF_IDENTIFIER_OPERATION_IN_PROGRESS("1403", 409),

    //GET VNF IDENTIFIER error code
    GET_VNF_IDENTIFIER_NOT_FOUND("1301", 404);

    private String internalErrorCode;
    private int httpCode;

    private ErrorType(final String internalErrorCode, final int httpCode) {
        this.internalErrorCode = internalErrorCode;
        this.httpCode = httpCode;
    }

    /**
     * @return the internalErrorCode
     */
    public String getInternalErrorCode() {
        return internalErrorCode;
    }

    /**
     * @return the httpCode
     */
    public int getHttpCode() {
        return httpCode;
    }

}
