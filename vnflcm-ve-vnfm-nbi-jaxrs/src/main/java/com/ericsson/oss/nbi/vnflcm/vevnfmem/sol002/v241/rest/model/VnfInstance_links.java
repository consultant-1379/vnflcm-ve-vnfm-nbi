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
public class VnfInstance_links {

    protected Link self;
    protected Link indicators;
    protected Link instantiate;
    protected Link terminate;
    protected Link scale;
    protected Link scaleToLevel;
    protected Link changeFlavour;
    protected Link heal;
    protected Link operate;
    protected Link changeExtConn;

    public Link getSelf() {
        return self;
    }

    public void setSelf(final Link value) {
        self = value;
    }

    public VnfInstance_links withSelf(final Link value) {
        self = value;
        return this;
    }

    public Link getIndicators() {
        return indicators;
    }

    public void setIndicators(final Link value) {
        indicators = value;
    }

    public VnfInstance_links withIndicators(final Link value) {
        indicators = value;
        return this;
    }

    public Link getInstantiate() {
        return instantiate;
    }

    public void setInstantiate(final Link value) {
        instantiate = value;
    }

    public VnfInstance_links withInstantiate(final Link value) {
        instantiate = value;
        return this;
    }

    public Link getTerminate() {
        return terminate;
    }

    public void setTerminate(final Link value) {
        terminate = value;
    }

    public VnfInstance_links withTerminate(final Link value) {
        terminate = value;
        return this;
    }

    public Link getScale() {
        return scale;
    }

    public void setScale(final Link value) {
        scale = value;
    }

    public VnfInstance_links withScale(final Link value) {
        scale = value;
        return this;
    }

    public Link getScaleToLevel() {
        return scaleToLevel;
    }

    public void setScaleToLevel(final Link value) {
        scaleToLevel = value;
    }

    public VnfInstance_links withScaleToLevel(final Link value) {
        scaleToLevel = value;
        return this;
    }

    public Link getChangeFlavour() {
        return changeFlavour;
    }

    public void setChangeFlavour(final Link value) {
        changeFlavour = value;
    }

    public VnfInstance_links withChangeFlavour(final Link value) {
        changeFlavour = value;
        return this;
    }

    public Link getHeal() {
        return heal;
    }

    public void setHeal(final Link value) {
        heal = value;
    }

    public VnfInstance_links withHeal(final Link value) {
        heal = value;
        return this;
    }

    public Link getOperate() {
        return operate;
    }

    public void setOperate(final Link value) {
        operate = value;
    }

    public VnfInstance_links withOperate(final Link value) {
        operate = value;
        return this;
    }

    public Link getChangeExtConn() {
        return changeExtConn;
    }

    public void setChangeExtConn(final Link value) {
        changeExtConn = value;
    }

    public VnfInstance_links withChangeExtConn(final Link value) {
        changeExtConn = value;
        return this;
    }

}
