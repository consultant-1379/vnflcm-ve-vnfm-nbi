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
public class ScaleInfo {

    protected String aspectId;
    protected int scaleLevel;

    public String getAspectId() {
        return aspectId;
    }

    public void setAspectId(final String value) {
        aspectId = value;
    }

    public ScaleInfo withAspectId(final String value) {
        aspectId = value;
        return this;
    }

    public int getScaleLevel() {
        return scaleLevel;
    }

    public void setScaleLevel(final int value) {
        scaleLevel = value;
    }

    public ScaleInfo withScaleLevel(final int value) {
        scaleLevel = value;
        return this;
    }

}
