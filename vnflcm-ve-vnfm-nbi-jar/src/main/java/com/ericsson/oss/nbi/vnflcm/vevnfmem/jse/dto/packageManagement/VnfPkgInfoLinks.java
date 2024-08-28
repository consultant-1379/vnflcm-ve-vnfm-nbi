/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VnfPkgInfoLinks {

    protected Link self;
    protected Link vnfd;
    protected Link packageContent;

    public Link getSelf() {
        return self;
    }

    public void setSelf(final Link value) {
        self = value;
    }

    public VnfPkgInfoLinks withSelf(final Link value) {
        self = value;
        return this;
    }

    /**
     * @return the vnfd
     */
    public Link getVnfd() {
        return vnfd;
    }

    /**
     * @param vnfd the vnfd to set
     */
    public void setVnfd(Link vnfd) {
        this.vnfd = vnfd;
    }

    public VnfPkgInfoLinks withVnfd(final Link value) {
        vnfd = value;
        return this;
    }
    /**
     * @return the packageContent
     */
    public Link getPackageContent() {
        return packageContent;
    }

    /**
     * @param packageContent the packageContent to set
     */
    public void setPackageContent(final Link packageContent) {
        this.packageContent = packageContent;
    }

    public VnfPkgInfoLinks withPackageContent(final Link value) {
        packageContent = value;
        return this;
    }
}
