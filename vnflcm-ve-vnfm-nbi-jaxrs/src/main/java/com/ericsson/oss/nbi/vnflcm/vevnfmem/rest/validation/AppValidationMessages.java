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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation;

public class AppValidationMessages {

    public static final String REQUIRED_NOT_EMPTY_MESSAGE = "Required field, cannot be left empty.";

    public static final String NOT_EMPTY_MESSAGE = "Cannot be left empty."; // Condition: Parameter of JSON array is not mandatory as per the specification.
                                                                            // But the user gives an empty array

    public static final String INVALID_INTEGER_MESSAGE = "Invalid value. Should be a valid Integer.";

    public static final String INVALID_PATTERN_MESSAGE = "Invalid value. Should be of the format: ";

    public static final String INVALID_ENUM_MESSAGE = "Invalid value. Permitted value(s) are: ";

    public static final String INVALID_USERINIT_DATA_TYPE_MESSAGE = INVALID_ENUM_MESSAGE + "[ URI ].";

    public static final String INVALID_TERMINATION_TYPE_MESSAGE = INVALID_ENUM_MESSAGE + "[ GRACEFUL, FORCEFUL ].";

    public static final String INVALID_VIM_TYPE_MESSAGE = INVALID_ENUM_MESSAGE + "[ CEE, OPENSTACK ].";

    public static final String VNFDID_PATTERN_MESSAGE = INVALID_PATTERN_MESSAGE + "<VnfType>__<VnfVersion>.";

    public static final String USER_INIT_INITDATATYPE_PATTERN_MESSAGE = "Enter a valid URI. Only http:// and https:// URIs are supported.";

    public static final String USER_INIT_URI_RESOURCE_NOT_FOUND = "Unable to retrieve the resource identified by the URI.";
}
