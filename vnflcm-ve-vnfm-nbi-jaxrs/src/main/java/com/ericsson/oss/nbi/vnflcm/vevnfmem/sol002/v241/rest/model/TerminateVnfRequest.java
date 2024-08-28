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
import java.util.*;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TerminateVnfRequest implements Serializable {

    private static final long serialVersionUID = 2608535228801318223L;

    private TerminateVnfRequestType terminationType = TerminateVnfRequestType.FORCEFUL;

	private int gracefulTerminationTimeout = 0;

    //Additional params for terminate VNF instance.
    @Valid
    private final Map<String, Object> additionalParams = new HashMap<String, Object>();

    /**
     * @return the terminationType
     */
    @JsonProperty("terminationType")
    public TerminateVnfRequestType getTerminationType() {
        return terminationType;
    }

    /**
     * @param terminationType
     *            the terminationType to set
     */
    public void setTerminationType(final TerminateVnfRequestType terminationType) {
        this.terminationType = terminationType;
    }

    public TerminateVnfRequest withTerminationType(final TerminateVnfRequestType terminationType) {
        this.terminationType = terminationType;
        return this;
    }

	/**
	 * @return the gracefulTerminationTimeout
	 */

	@JsonProperty("gracefulTerminationTimeout")
	public int getGracefulTerminationTimeout() {
		return gracefulTerminationTimeout;
	}

	/**
	 * @param gracefulTerminationTimeout the gracefulTerminationTimeout to set
	 */
	public void setGracefulTerminationTimeout(final int gracefulTerminationTimeout) {
		this.gracefulTerminationTimeout = gracefulTerminationTimeout;
	}

	public TerminateVnfRequest withGracefulTerminationTimeout(final int gracefulTerminationTimeout) {
		this.gracefulTerminationTimeout = gracefulTerminationTimeout;
		return this;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TerminateVnfRequest terminateVnfRequest = (TerminateVnfRequest) o;
        return Objects.equals(additionalParams, terminateVnfRequest.additionalParams) &&
                Objects.equals(gracefulTerminationTimeout, terminateVnfRequest.gracefulTerminationTimeout) &&
                Objects.equals(terminationType, terminateVnfRequest.terminationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalParams, terminationType);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("class TerminateVnfRequest {\n");

        sb.append("    additionalParams: ").append(toIndentedString(additionalParams)).append("\n");
        sb.append("    gracefulTerminationTimeout: ").append(toIndentedString(gracefulTerminationTimeout)).append("\n");
        sb.append("    terminationType: ").append(toIndentedString(terminationType)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(final Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
