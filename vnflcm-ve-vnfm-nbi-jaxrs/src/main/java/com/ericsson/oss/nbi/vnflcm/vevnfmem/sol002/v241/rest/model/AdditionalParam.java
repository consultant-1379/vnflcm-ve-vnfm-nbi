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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Additional parameters passed by the NFVO as input to the instantiation process, specific to the VNF being instantiated. Structure:
 *
 * JSON Structure of this object will be
 *
 * <pre>
 *  "additionalParam" : {
 *         "cloudInitFile" : "/opt/ericsson/mme_cloudInit.yaml",
 *         "cloudInitFile1" : ["/opt/ericsson/mme_cloudInit.yaml", "/opt/ericsson/mme_cloudInit.yaml"],
 *         "userInitData" : [{
 *                 "vnfcName" : "FSB",
 *                 "vnfcInstance" : "0",
 *                 "initDataType" : "URI",
 *                 "initData" : "http://localhost:8000/sql.txt"
 *         },{
 *                 "vnfcName" : "FSB",
 *                 "vnfcInstance" : "0",
 *                 "initDataType" : "URI",
 *                 "initData" : "http://localhost:8000/sql.txt"
 *         }]
 *
 *     },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalParam {

    // Additional attributes are stored in this map
    private final Map<String, Object> additionalAttributes = new HashMap<String, Object>();

    /**
     * Returns the additional attributes that are to used during instantiation
     *
     * @return the additionalAttributes
     */
    public Map<String, Object> getAdditionalAttributes() {
        return additionalAttributes;
    }

    /**
     * Used to set the additional attributes that are to be used during instantiation
     *
     * @param additionalAttributes
     *            the additionalAttributes to set
     */
    public void setAdditionalAttributes(final String key, final Object value) {
        this.additionalAttributes.put(key, value);
    }

}
