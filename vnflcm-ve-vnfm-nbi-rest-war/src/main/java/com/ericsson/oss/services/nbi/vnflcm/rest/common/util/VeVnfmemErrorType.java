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
public enum VeVnfmemErrorType {

    //Header related error
    USER_HEADER_NOT_FOUND(400),

    //VNF Instantiation error code
    VNF_IDENTIFIER_NOT_FOUND( 404),
    VNF_ALREADY_INSTANTIATED( 409),
    WORKFLOW_MAPPING_NOT_FOUND( 500),
    INSTANCE_OPERATION_IN_PROGRESS_INSTANTIATE(409),

    VALIDATION_ERROR( 422),
    INTERNAL_ERROR( 500),
    NOT_IMPLEMENTED(501),
    BAD_REQUEST(400),

    JSON_PARSING_ERROR(500),
    PACKAGE_ISSUE(500),
    CONFIGURATION_FILE_NOT_FOUND(500);


    private int httpCode;

    private VeVnfmemErrorType( final int httpCode) {
        this.httpCode = httpCode;
    }

     /**
     * @return the httpCode
     */
    public int getHttpCode() {
        return httpCode;
    }

}
