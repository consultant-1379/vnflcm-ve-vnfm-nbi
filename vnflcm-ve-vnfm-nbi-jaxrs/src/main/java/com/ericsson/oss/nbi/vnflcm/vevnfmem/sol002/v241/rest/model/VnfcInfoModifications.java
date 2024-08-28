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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VnfcInfoModifications {

    protected String id;
    protected KeyValuePairs vnfcConfigurableProperties;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the vnfcConfigurableProperties
     */
    public KeyValuePairs getVnfcConfigurableProperties() {
        return vnfcConfigurableProperties;
    }

    /**
     * @param vnfcConfigurableProperties
     *            the vnfcConfigurableProperties to set
     */
    public void setVnfcConfigurableProperties(final KeyValuePairs vnfcConfigurableProperties) {
        this.vnfcConfigurableProperties = vnfcConfigurableProperties;
    }

}
