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

public class NfvoCallException extends Exception {

    /* private static final long serialVersionUID = 1L; */

    private int errCode;

    private String errorMessage;

    public NfvoCallException(final int errCode, final String errorMessage) {
        super(errorMessage);
        this.errCode = errCode;
        this.errorMessage = errorMessage;

    }

    public NfvoCallException(final String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errCode
     */
    public int getErrCode() {
        return this.errCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrCode(final int errCode) {
        this.errCode = errCode;
    }

}
