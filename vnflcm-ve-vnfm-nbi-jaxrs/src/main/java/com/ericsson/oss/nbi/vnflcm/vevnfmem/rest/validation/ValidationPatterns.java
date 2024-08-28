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

public class ValidationPatterns {

    public static final String ALPHA_NUMERIC_SPACE_UNDERSCORE = "^[a-zA-Z0-9\\s_-]+$";

    public static final String ALPHA_NUMERIC_UNDERSCORE = "^[a-zA-Z0-9_-]+$";

    public static final String HTTP_URL = "^\\(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$";

    public static final String NUMERIC = "^[\\d]+$";

    public static final String VNFDID = "^[\\S]+__[\\S]+$";

    public static final String HTTP_START_URL_PATTERN = "^(http|https)://.*$";

    public static final String NUMBER_PATTERN = "^[0-9]+$";

}
