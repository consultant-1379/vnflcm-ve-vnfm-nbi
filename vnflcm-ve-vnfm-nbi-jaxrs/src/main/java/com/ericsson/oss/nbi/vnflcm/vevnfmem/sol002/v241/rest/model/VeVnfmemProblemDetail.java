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

/**
 * Enum class which holds the input parameter error messages.
 */
public class VeVnfmemProblemDetail {

    private String title;
    private String type;
    private String detail;
    private String instance;
    private int status;
    private ErrorAttributes additionalAttributes;

    public VeVnfmemProblemDetail() {
        super();
    }

    /**
     * @param name
     * @param message
     */
    public VeVnfmemProblemDetail(final String title, final String detail, final String instance, final String type, final int statusCode) {
        this.title = title;
        this.detail = detail;
        this.status = statusCode;
        this.type = type;
        this.instance = instance;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     *            the detail to set
     */
    public void setDetail(final String detail) {
        this.detail = detail;
    }

    /**
     * @return the instance
     */
    public String getInstance() {
        return instance;
    }

    /**
     * @param instance
     *            the instance to set
     */
    public void setInstance(final String instance) {
        this.instance = instance;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(final int status) {
        this.status = status;
    }

    /**
     * @return the additionalAttributes
     */
    public ErrorAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    /**
     * @param additionalAttributes
     *            the additionalAttributes to set
     */
    public void setAdditionalAttributes(final ErrorAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    /**
     * This method returns the error message in Json format.
     *
     * @return String containing error message. "e.g : [{"name" : "<inputArgument>", "message" : "<payload message>"}]
     */
    public String toJson() {
        return "[{\"type\" : \"" + type + "\", \"title\" : \"" + title + "\", \"detail\" : \"" + detail + "\", \"instance\" : \"" + instance
                + "\" , \"status\" : \"" + status + "\"}]";
    }

}
