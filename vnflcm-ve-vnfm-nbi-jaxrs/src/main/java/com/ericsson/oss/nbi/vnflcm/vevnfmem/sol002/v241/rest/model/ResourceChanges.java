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

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResourceChanges implements Serializable {

    private static final long serialVersionUID = 5095023171936122893L;
    private List<AffectedVnfc> affectedVnfcs;
    private List<AffectedVirtualLink> affectedVirtualLinks;
    private List<AffectedVirtualStorage> affectedStorages;

    /**
     * @return the affectedVirtualLinks
     */
    public List<AffectedVirtualLink> getAffectedVirtualLinks() {
        return affectedVirtualLinks;
    }

    /**
     * @param affectedVirtualLinks
     *            the affectedVirtualLinks to set
     */
    public void setAffectedVirtualLinks(final List<AffectedVirtualLink> affectedVirtualLinks) {
        this.affectedVirtualLinks = affectedVirtualLinks;
    }

    /**
     * @return the affectedStorage
     */
    public List<AffectedVirtualStorage> getAffectedStorages() {
        return affectedStorages;
    }

    /**
     * @param affectedStorage
     *            the affectedStorage to set
     */
    public void setAffectedStorages(final List<AffectedVirtualStorage> affectedStorages) {
        this.affectedStorages = affectedStorages;
    }

    /**
     * @return the affectedVnfcs
     */
    public List<AffectedVnfc> getAffectedVnfcs() {
        return affectedVnfcs;
    }

    /**
     * @param affectedVnfcs
     *            the affectedVnfcs to set
     */
    public void setAffectedVnfcs(final List<AffectedVnfc> affectedVnfcs) {
        this.affectedVnfcs = affectedVnfcs;
    }

    @Override
    public String toString() {
        return "ResourceChanges [affectedVnfcs=" + affectedVnfcs + ", affectedVirtualLinks=" + affectedVirtualLinks + ", affectedStorages="
                + affectedStorages + "]";
    }
}
