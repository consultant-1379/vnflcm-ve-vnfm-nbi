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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class KeyValuePairs {

    private Map<String, Object> keyValuePairs = new HashMap<String, Object>();

    public Map<String, Object> getKeyValuePairs() {
        return keyValuePairs;
    }

    public void setKeyValuePairs(final Map<String, Object> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }
}
