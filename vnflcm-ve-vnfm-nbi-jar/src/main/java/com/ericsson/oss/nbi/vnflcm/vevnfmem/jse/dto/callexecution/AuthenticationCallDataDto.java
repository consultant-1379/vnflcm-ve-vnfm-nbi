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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution;

import java.io.Serializable;
import java.util.Map;

public class AuthenticationCallDataDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8818337538888990404L;
    private String authUrl;
    private Map<String, String> headers;
    private String authReqBody;
    private String authType;

    public String getAuthUrl() {
        return authUrl;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthUrl(final String url) {
        this.authUrl = url;
    }

    public void setAuthType(final String authType) {
        this.authType = authType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public String getAuthReqBody() {
        return authReqBody;
    }

    public void setAuthReqBody(final String authReqBody) {
        this.authReqBody = authReqBody;
    }

    @Override
    public String toString() {
        return "AuthenticationCallDataDto [url=" + authUrl + ", headers=" + headers + ", reqBody=" + authReqBody + "]";
    }

}
