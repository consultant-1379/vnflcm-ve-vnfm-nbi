/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons;

import java.io.Serializable;
import org.json.JSONObject;

public class LcmOperationProblemDetails implements Serializable {
    
    private static final long serialVersionUID = 7746423894884820398L;
    private String errorTitle;
    private String errorDetails;

    public String getErrorTitle() {
        return errorTitle;
    }
    public void setErrorTitle(final String errorTitle) {
        this.errorTitle = errorTitle;
    }
    public String getErrorDetails() {
        return errorDetails;
    }
    public void setErrorDetails(final String errorDetails) {
        this.errorDetails = errorDetails;
    }
    public String getProblemDetailsAsJsonString() {
       String error = null;
       if(errorTitle != null && errorDetails != null){
          final JSONObject errorObj = new JSONObject();
          errorObj.put("errorTitle",errorTitle );
          errorObj.put("errorDetails",errorDetails);
          error = errorObj.toString();
        }
        return error;
    }
}