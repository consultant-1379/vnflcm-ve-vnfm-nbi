/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model;

import java.util.HashMap;
import java.util.Map;

public class HealVnfRequest {

    private String cause;
    // Additional parameters are stored in this map
    private final Map<String, Object> additionalParams = new HashMap<String, Object>();
    //    private String vnfcInstanceId;
    private String healScript;

    /**
     * @return the healScript
     */
    public String getHealScript() {
        return healScript;
    }

    /**
     * @param healScript
     *            the healScript to set
     */
    public void setHealScript(final String healScript) {
        this.healScript = healScript;
    }

    /**
     * @return the cause
     */
    public String getCause() {
        return cause;
    }

    /**
     * @param cause
     *            the cause to set
     */
    public void setCause(final String cause) {
        this.cause = cause;
    }

    /**
     * Returns the additional attributes that are to used during instantiation
     *
     * @return the additionalAttributes
     */
    public Map<String, Object> getAdditionalParams() {
        return additionalParams;
    }

    /**
     * Used to set the additional attributes that are to be used during instantiation
     *
     * @param additionalAttributes
     *            the additionalAttributes to set
     */
    public void setAdditionalParams(final String key, final Object value) {
        this.additionalParams.put(key, value);
    }
}
