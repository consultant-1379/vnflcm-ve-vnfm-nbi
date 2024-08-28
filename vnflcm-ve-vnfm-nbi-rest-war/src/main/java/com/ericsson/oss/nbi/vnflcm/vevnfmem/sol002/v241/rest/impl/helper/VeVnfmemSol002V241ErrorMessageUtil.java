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

import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VeVnfmemProblemDetail;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.ErrorMessages;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.VeVnfmemErrorType;

/**
 * This class is responsible for generating error messages.
 */
public final class VeVnfmemSol002V241ErrorMessageUtil {

    private VeVnfmemSol002V241ErrorMessageUtil() {
    }

    public static VeVnfmemProblemDetail generateErrorMessage(final VeVnfmemErrorType error, final String title, final String message) {
        final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(title, message, "", "", error.getHttpCode());
        return problemDetail;
    }

    public static VeVnfmemProblemDetail generateUnexpectedError(final VeVnfmemErrorType error) {
        final VeVnfmemProblemDetail problemDetail = new VeVnfmemProblemDetail(ErrorMessages.INTERNAL_ERROR_MSG[0],
                ErrorMessages.INTERNAL_ERROR_MSG[1], "", "", error.getHttpCode());
        return problemDetail;

    }

}
