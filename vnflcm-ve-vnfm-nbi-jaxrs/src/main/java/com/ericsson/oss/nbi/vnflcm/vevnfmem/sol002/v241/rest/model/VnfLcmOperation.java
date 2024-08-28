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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VnfLcmOperation {

    private String id;
    private LcmOperationStateType operationState;
    private String vnfInstanceId;
    private String grantId;
    private String stateEnteredTime;
    private String startTime;
    private LcmOperationType operation;
    private boolean isAutomaticInvocation;
    @JsonIgnore
    private String requestSourceType;
    private Object operationParams;
    private boolean isCancelPending;
    private VnflcmOperationLinks _links;
    private ResourceChanges resourceChanges;
    private Error error;

    public boolean getIsAutomaticInvocation() {
        return isAutomaticInvocation;
    }

    public void setIsAutomaticInvocation(final boolean isAutomaticInvocation) {
        this.isAutomaticInvocation = isAutomaticInvocation;
    }

    public String getRequestSourceType() {
        return requestSourceType;
    }

    public void setRequestSourceType(final String requestSourceType) {
        this.requestSourceType = requestSourceType;
    }

    /**
     * @return the cancelPending
     */
    public boolean getIsCancelPending() {
        return isCancelPending;
    }

    /**
     * @param cancelPending
     *            the cancelPending to set
     */
    public void setIsCancelPending(final boolean isCancelPending) {
        this.isCancelPending = isCancelPending;
    }

    /**
     * @return the operationState
     */
    public LcmOperationStateType getOperationState() {
        return operationState;
    }

    /**
     * @param operationState
     *            the operationState to set
     */
    public void setOperationState(final LcmOperationStateType operationState) {
        this.operationState = operationState;
    }

    /**
     * @return the operation
     */
    public LcmOperationType getOperation() {
        return operation;
    }

    /**
     * @param operation
     *            the operation to set
     */
    public void setOperation(final LcmOperationType operation) {
        this.operation = operation;
    }

    /**
     * @return the resourceChanges
     */
    public ResourceChanges getResourceChanges() {
        return resourceChanges;
    }

    /**
     * @param resourceChanges
     *            the resourceChanges to set
     */
    public void setResourceChanges(final ResourceChanges resourceChanges) {
        this.resourceChanges = resourceChanges;
    }

    /**
     * @return the _links
     */
    public VnflcmOperationLinks get_links() {
        return _links;
    }

    /**
     * @param _links
     *            the _links to set
     */
    public void set_links(final VnflcmOperationLinks _links) {
        this._links = _links;
    }

    /**
     * @return the stateEnteredTime
     */
    public String getStateEnteredTime() {
        return stateEnteredTime;
    }

    /**
     * @param stateEnteredTime
     *            the stateEnteredTime to set
     */
    public void setStateEnteredTime(final String stateEnteredTime) {
        this.stateEnteredTime = stateEnteredTime;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the vnfInstanceId
     */
    public String getVnfInstanceId() {
        return vnfInstanceId;
    }

    /**
     * @param vnfInstanceId
     *            the vnfInstanceId to set
     */
    public void setVnfInstanceId(final String vnfInstanceId) {
        this.vnfInstanceId = vnfInstanceId;
    }

    /**
     * @return the grantId
     */
    public String getGrantId() {
        return grantId;
    }

    /**
     * @param grantId
     *            the grantId to set
     */
    public void setGrantId(final String grantId) {
        this.grantId = grantId;
    }

    /**
     * @return the operationParams
     */
    public Object getOperationParams() {
        return operationParams;
    }

    /**
     * @param operationParams
     *            the operationParams to set
     */
    public void setOperationParams(final Object operationParams) {
        this.operationParams = operationParams;
    }

    /**
     * @return the error
     */
    public Error getError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(final Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "VnfLcmOperation [id=" + id + ", operationState=" + operationState + ", vnfInstanceId=" + vnfInstanceId + ", grantId=" + grantId
                + ", stateEnteredTime=" + stateEnteredTime + ", startTime=" + startTime + ", operation=" + operation + ", isAutomaticInvocation="
                + isAutomaticInvocation + ", operationParams=" + operationParams + ", isCancelPending="
                + isCancelPending + ", _links=" + _links + ", resourceChanges=" + resourceChanges + ", error=" + error + "]";
    }
}