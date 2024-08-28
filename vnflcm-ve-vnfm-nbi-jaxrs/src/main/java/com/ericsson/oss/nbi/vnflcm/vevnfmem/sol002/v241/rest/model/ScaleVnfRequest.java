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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.EnumType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScaleVnfRequest {

    @EnumType(enumClass = OperationType.class, message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected OperationType type;

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String aspectId;

    protected int numberOfSteps = 1;

    // Additional parameters are stored in this map
    private final Map<String, Object> additionalParams = new HashMap<String, Object>();

    /**
     * @return the type
     */
    public OperationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(OperationType type) {
        this.type = type;
    }

    /**
     * @return the aspectId
     */
    public String getAspectId() {
        return aspectId;
    }

    /**
     * @param aspectId the aspectId to set
     */
    public void setAspectId(String aspectId) {
        this.aspectId = aspectId;
    }

    /**
     * @return the numberOfSteps
     */
    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    /**
     * @param numberOfSteps the numberOfSteps to set
     */
    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
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
