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

import javax.validation.constraints.NotNull;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class IpAddresses {

    @NotNull(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE)
    protected Type type;
    protected String[] fixedAddresses;
    protected String numDynamicAddresses;
    protected AddressRange addressRange;
    protected String subnetId;
    protected String[] addresses;
    protected Boolean isDynamic;

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final Type type) {
        this.type = type;
    }

    public IpAddresses withType(final Type value) {
        type = value;
        return this;
    }

    /**
     * @return the addresses
     */
    public String[] getAddresses() {
        return addresses;
    }

    /**
     * @param addresses
     *            the addresses to set
     */
    public void setAddresses(final String[] addresses) {
        this.addresses = addresses;
    }

    public IpAddresses withAddresses(final String[] value) {
        addresses = value;
        return this;
    }

    /**
     * @return the fixedAddresses
     */
    public String[] getFixedAddresses() {
        return fixedAddresses;
    }

    /**
     * @param fixedAddresses
     *            the fixedAddresses to set
     */
    public void setFixedAddresses(final String[] fixedAddresses) {
        this.fixedAddresses = fixedAddresses;
    }

    public IpAddresses withFixedAddresses(final String[] value) {
        fixedAddresses = value;
        return this;
    }

    /**
     * @return the numDynamicAddresses
     */
    public String getNumDynamicAddresses() {
        return numDynamicAddresses;
    }

    /**
     * @param numDynamicAddresses
     *            the numDynamicAddresses to set
     */
    public void setNumDynamicAddresses(final String numDynamicAddresses) {
        this.numDynamicAddresses = numDynamicAddresses;
    }

    public IpAddresses withNumDynamicAddresses(final String value) {
        numDynamicAddresses = value;
        return this;
    }

    /**
     * @return the addressRange
     */
    public AddressRange getAddressRange() {
        return addressRange;
    }

    /**
     * @param addressRange
     *            the addressRange to set
     */
    public void setAddressRange(final AddressRange addressRange) {
        this.addressRange = addressRange;
    }

    public IpAddresses withAddressRange(final AddressRange value) {
        addressRange = value;
        return this;
    }

    /**
     * @return the subnetId
     */
    public String getSubnetId() {
        return subnetId;
    }

    /**
     * @param subnetId
     *            the subnetId to set
     */
    public void setSubnetId(final String subnetId) {
        this.subnetId = subnetId;
    }

    public IpAddresses withSubnetId(final String value) {
        subnetId = value;
        return this;
    }

    /**
     * @return the isDynamic
     */
    public Boolean getIsDynamic() {
        return isDynamic;
    }

    /**
     * @param isDynamic
     *            the isDynamic to set
     */
    public void setIsDynamic(final Boolean isDynamic) {
        this.isDynamic = isDynamic;
    }

    public IpAddresses withIsDynamic(final Boolean value) {
        isDynamic = value;
        return this;
    }

    @Override
    public String toString() {
        return "IpAddresses [type=" + type + ", fixedAddresses=" + Arrays.toString(fixedAddresses) + ", numDynamicAddresses=" + numDynamicAddresses
                + ", addressRange=" + addressRange + ", subnetId=" + subnetId + ", addresses=" + Arrays.toString(addresses) + ", isDynamic="
                + isDynamic + "]";
    }
}
