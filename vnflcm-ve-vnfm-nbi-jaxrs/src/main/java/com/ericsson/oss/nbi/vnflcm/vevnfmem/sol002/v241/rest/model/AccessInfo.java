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

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessInfo {

    protected String projectId;
    protected String projectName;
    protected String domainName;
    protected String userDomain;
    @Valid
    protected Credentials credentials;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String value) {
        projectId = value;
    }

    public AccessInfo withProjectId(final String value) {
        projectId = value;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(final String value) {
        projectName = value;
    }

    public AccessInfo withProjectName(final String value) {
        projectName = value;
        return this;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(final String value) {
        domainName = value;
    }

    public String getUserDomain() {
        return userDomain;
    }

    public void setUserDomain(final String userDomain) {
        this.userDomain = userDomain;
    }

    public AccessInfo withDomainName(final String value) {
        domainName = value;
        return this;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials value) {
        credentials = value;
    }

    public AccessInfo withCredentials(final Credentials value) {
        credentials = value;
        return this;
    }

    @Override
    public String toString() {
        return "{projectId=" + projectId + ",projectName=" + projectName + ",domainName=" + domainName + ",userDomain="
                + userDomain + ",credentials=" + credentials + "}";
    }
}
