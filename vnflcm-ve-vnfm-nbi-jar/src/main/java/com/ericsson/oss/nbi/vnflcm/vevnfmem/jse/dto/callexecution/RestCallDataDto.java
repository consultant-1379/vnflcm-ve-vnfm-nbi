/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * It is used to transfer data from RestCallDataCreator to Notification
 * interface. Also, used between RestCallDataCreator and
 * EcmSpecificRestCallManipulations
 * 
 * @author xrohdwi
 * 
 */
public class RestCallDataDto implements Serializable {

 private static final long serialVersionUID = 9117424404945397735L;
 private AuthenticationCallDataDto authCallData;
 private String url;
 private Map<String, String> headers;
 private String reqBody;
 private ObjectNode jsonObjectNode;
 private int successCode;
 private NbiData nbiData;
 private String contentType;
 private int contentLength;
 private OutputStream outputStream;
 private VnflcmServiceData vnflcmServiceData;
 private String methodType;

 public String getUrl() {
  return url;
 }

 public void setUrl(final String url) {
  this.url = url;
 }

 public Map<String, String> getHeaders() {
  return headers;
 }

 public void setHeaders(final Map<String, String> headers) {
  this.headers = headers;
 }

 public String getReqBody() {
  return reqBody;
 }

 public void setReqBody(final String reqBody) {
  this.reqBody = reqBody;
 }

 public ObjectNode getJsonObjectNode() {
  return jsonObjectNode;
 }

 /**
  * used for transferring data between RestCallDataCreator and
  * EcmSpecificRestCallManipulations
  * 
  * @param jsonObjectNode
  */
 public void setJsonObjectNode(final ObjectNode jsonObjectNode) {
  this.jsonObjectNode = jsonObjectNode;
 }

 public AuthenticationCallDataDto getAuthCallData() {
  return authCallData;
 }

 public void setAuthCallData(final AuthenticationCallDataDto authCallData) {
  this.authCallData = authCallData;
 }

 public int getSuccessCode() {
  return successCode;
 }

 public void setSuccessCode(final int successCode) {
  this.successCode = successCode;
 }

 public NbiData getNbiData() {
  return nbiData;
 }

 public void setNbiData(final NbiData nbiData) {
  this.nbiData = nbiData;
 }
 /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType
     *            the contentType to set
     */
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the contentLength
     */
    public int getContentLength() {
        return contentLength;
    }

    /**
     * @param contentLength
     *            the contentLength to set
     */
    public void setContentLength(final int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * @return the outputStream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * @param outputStream
     *            the outputStream to set
     */
    public void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

 /**
     * @return the vnflcmServiceData
     */
    public VnflcmServiceData getVnflcmServiceData() {
        return vnflcmServiceData;
    }

    /**
     * @param vnflcmServiceData the vnflcmServiceData to set
     */
    public void setVnflcmServiceData(final VnflcmServiceData vnflcmServiceData) {
        this.vnflcmServiceData = vnflcmServiceData;
    }

    /**
     * @return the methodType
     */
    public String getMethodType() {
        return methodType;
    }

    /**
     * @param methodType the methodType to set
     */
    public void setMethodType(final String methodType) {
        this.methodType = methodType;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RestCallDataDto [authCallData=" + authCallData + ", url=" + url + ", headers=" + headers + ", reqBody="
                + reqBody + ", jsonObjectNode=" + jsonObjectNode + ", successCode=" + successCode + ", contentType=" + contentType + ", contentLength=" + contentLength + ", outputStream="
                + outputStream + ", vnflcmServiceData=" + vnflcmServiceData + ", methodType=" + methodType + "]";
    }
}
