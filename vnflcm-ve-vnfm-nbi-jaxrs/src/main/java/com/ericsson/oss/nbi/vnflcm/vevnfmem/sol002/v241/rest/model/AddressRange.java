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
import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AddressRange {

    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String minAddress;
    @NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected String maxAddress;

    public String getMinAddress() {
        return minAddress;
    }

    public void setMinAddress(final String value) {
        minAddress = value;
    }

    public AddressRange withMinAddress(final String value) {
        minAddress = value;
        return this;
    }

    public String getMaxAddress() {
        return maxAddress;
    }

    public void setMaxAddress(final String value) {
        maxAddress = value;
    }

    public AddressRange withMaxAddress(final String value) {
        maxAddress = value;
        return this;
    }

    @Override
    public String toString() {
        return "AddressRange [minAddress=" + minAddress + ", maxAddress=" + maxAddress + "]";
    }
}
