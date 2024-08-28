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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VnflcmOperationLinks {

    protected Link self;
    protected Link instantiate;

    /**
     * @return the self
     */
    public Link getSelf() {
        return self;
    }

    /**
     * @param self
     *            the self to set
     */
    public void setSelf(final Link self) {
        this.self = self;
    }

    /**
     * @return the instantiate
     */
    public Link getInstantiate() {
        return instantiate;
    }

    /**
     * @param instantiate
     *            the instantiate to set
     */
    public void setInstantiate(final Link instantiate) {
        this.instantiate = instantiate;
    }

    @Override
    public String toString() {
        return "VnflcmOperationLinks [self=" + self + ", instantiate=" + instantiate + "]";
    }
}
