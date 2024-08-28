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

public final class ErrorMessages {

    public static final String[] USER_HEADER_NOT_FOUND_MSG = { "Unable to process the request as required header with userid not found.",
            "The header X-Tor-UserID was either missing or it had no value." };

    public static final String[] NOT_SUPPORTED_MSG = { "This API is not yet supported.", "Implementation is not yet available for this API." };

    public static final String[] BAD_REQUEST_MSG = { "Provide the mandatory arguments.", "Some of the parameters are null or empty.",
            "Either VimSoftwareImageId or VimSoftwareImageName must have the value for a Vim asset." };

    public static final String[] INTERNAL_ERROR_MSG = { "Unable to process the request by application.",
            "This is an internal system error, please check the log for more details." };

    public static final String[] INIT_DATA_COPY_ERROR_MSG = { "Unable to copy the Init data file." };

    public static final String[] VNF_ID_NOT_FOUND = { "Unable to instantiate VNF node for the instance identifier not present in the application.",
            "The VNF instance identifier is not found in the application." };

    public static final String[] VNF_ID_NOT_FOUND_TERMINATE = { "Unable to terminate the VNF due to non-occurrence of VNF instance identifier.",
            "The VNF instance identifier is not found in the application." };

    public static final String[] VNF_NOT_INSTANTIATED_TERMINATE = {
            "Unable to terminate VNF instance identifier since the VNF is not yet instantiated.",
            "No VNF node with the VNF instance identifier is in INSTANTIATED state." };

    public static final String[] GET_VNF_ID_NOT_FOUND = {
            "Unable to fetch VNF instance due to non-occurrence of VNF instance identifier in the application.",
            "The VNF instance identifier is either deleted/not created in the application." };

    public static final String[] VNF_ALREADY_INSTANTIATED = { "Unable to instantiate VNF node since the VNF is already in instantiated state.",
            "A VNF node with the instance identifier is already in instantiated state." };

    public static final String[] LIFE_CYCLE_OP_NOT_FOUND = {
            "Unable to fetch operation status of VNF lifecycle due to non occurence of VNF lifecycle operation identifier.",
            "The VNF lifecycle operation identifier not found in the application." };

    public static final String[] WORKFLOW_MAPPING_NOT_FOUND = {
            "VNF workflows are not supported to run over NBI when vnfType and vnfVersion are not mapped in the mappings.xml against the workflow bundle name.",
            "Unable to find the mapping rule matching the input parameters." };

    public static final String[] DELETE_VNF_IDENTIFIER_NOT_FOUND = { "Unable to find the VNF instance identifier to delete.",
            "No VNF found with given instance identifier." };

    public static final String[] DELETE_VNF_IDENTIFIER_ALREADY_INSTANTIATED = {
            "Unable to delete VNF instance identifier since the VNF is in instantiated state.",
            "A VNF with given instance identifier is in INSTANTIATED state." };

    public static final String[] DELETE_VNF_IDENTIFIER_OPERATION_IN_PROGRESS = {
            "Unable to delete VNF instance identifier since the VNF lifecycle operation is in progress.",
            "The lifecycle operation is in progress for the VNF with the instance identifier." };

    public static final String[] INSTANCE_OPERATION_IN_PROGRESS_TERMINATE = {
            "Unable to terminate VNF instance as VNF lifecycle operation is in progress.", "Lifecycle operation is in progress for  VNF." };

    public static final String[] INSTANCE_OPERATION_IN_PROGRESS_INSTANTIATE = {
            "Unable to instantiate VNF instance as VNF lifecycle operation is in progress.", "Lifecycle operation is in progress for VNF." };

    public static final String[] JSON_PARSING_ERROR_MSG = { "Unable to process configuration file.", "Configuration file not in correct format." };

    public static final String[] CONFIGURATION_FILE_NOT_FOUND = { "Configuration file not found.",
            "Configuration file not found, check file directory." };

    public static final String[] VNF_NOT_FOUND = { "VNF not found.",
            "Internal system error, check all mandatory vnf properties in request and configuration file, check no duplicate vnf instance name." };

    public static final String[] PACKAGE_ISSUE = { "Package partially downloaded",
    "Package artifacts have problem, check the files. Package will be deleted." };

    private ErrorMessages() {
    }
}