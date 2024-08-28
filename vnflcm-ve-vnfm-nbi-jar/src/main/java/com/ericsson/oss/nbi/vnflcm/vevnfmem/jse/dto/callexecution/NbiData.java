package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution;

import java.io.Serializable;

public class NbiData implements Serializable {

 private static final long serialVersionUID = -2916049288655136992L;
 private String username;
 private String password;
 private String subscriptionId;
 private String enmHostname;
 private String isNfvoAvailable;
 private String isGrantSupported;
 private String notificationAckRequired;
 private String staticAuthenticationTokenName;
 private String staticAuthenticationTokenValue;
 private String nfvoType;
 private Boolean basicAuthImplSameAsEO;

 public String getUsername() {
  return username;
 }

 public void setUsername(final String username) {
  this.username = username;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(final String password) {
  this.password = password;
 }

 public String getSubscriptionId() {
  return subscriptionId;
 }

 public void setSubscriptionId(final String subscriptionId) {
  this.subscriptionId = subscriptionId;
 }

 public String getEnmHostname() {
  return enmHostname;
 }

 public void setEnmHostname(final String enmHostname) {
  this.enmHostname = enmHostname;
 }

 public String getIsNfvoAvailable() {
  return isNfvoAvailable;
 }

 public void setIsNfvoAvailable(final String isNfvoAvailable) {
  this.isNfvoAvailable = isNfvoAvailable;
 }

 
 /**
 * @return the notificationAckRequired
 */
public String getNotificationAckRequired() {
    return notificationAckRequired;
}

/**
 * @param notificationAckRequired the notificationAckRequired to set
 */
public void setNotificationAckRequired(final String notificationAckRequired) {
    this.notificationAckRequired = notificationAckRequired;
}

public String getIsGrantSupported() {
  return isGrantSupported;
 }

 public void setIsGrantSupported(final String isGrantSupported) {
  this.isGrantSupported = isGrantSupported;
 }

 /**
  * @return the staticAuthenticationTokenName
  */
 public String getStaticAuthenticationTokenName() {
  return staticAuthenticationTokenName;
 }

 /**
  * @param staticAuthenticationTokenName
  *            the staticAuthenticationTokenName to set
  */
 public void setStaticAuthenticationTokenName(
   final String staticAuthenticationTokenName) {
  this.staticAuthenticationTokenName = staticAuthenticationTokenName;
 }

 /**
  * @return the staticAuthenticationTokenValue
  */
 public String getStaticAuthenticationTokenValue() {
  return staticAuthenticationTokenValue;
 }

 /**
  * @param staticAuthenticationTokenValue
  *            the staticAuthenticationTokenValue to set
  */
 public void setStaticAuthenticationTokenValue(
   final String staticAuthenticationTokenValue) {
  this.staticAuthenticationTokenValue = staticAuthenticationTokenValue;
 }

 
 /**
 * @return the nfvoType
 */
public String getNfvoType() {
    return nfvoType;
}

/**
 * @param nfvoType the nfvoType to set
 */
public void setNfvoType(final String nfvoType) {
    this.nfvoType = nfvoType;
}

/**
 * @return the basicAuthImplSameAsEO
 */
public Boolean getBasicAuthImplSameAsEO() {
    return basicAuthImplSameAsEO;
}

/**
 * @param basicAuthImplSameAsEO the basicAuthImplSameAsEO to set
 */
public void setBasicAuthImplSameAsEO(Boolean basicAuthImplSameAsEO) {
    this.basicAuthImplSameAsEO = basicAuthImplSameAsEO;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
    return "NbiData [username=" + username + ", password=" + password + ", subscriptionId=" + subscriptionId
            + ", enmHostname=" + enmHostname + ", isNfvoAvailable=" + isNfvoAvailable + ", isGrantSupported="
            + isGrantSupported + ", notificationAckRequired=" + notificationAckRequired
            + ", staticAuthenticationTokenName=" + staticAuthenticationTokenName + ", staticAuthenticationTokenValue="
            + staticAuthenticationTokenValue + ", nfvoType=" + nfvoType + "]";
}

}
