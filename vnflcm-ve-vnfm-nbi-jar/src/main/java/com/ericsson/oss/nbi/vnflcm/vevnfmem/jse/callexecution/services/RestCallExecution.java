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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution.RestCallExecutor;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.*;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.*;
import com.ericsson.oss.services.vnflcm.common.exceptions.ConnectionFailureException;
import com.ericsson.oss.services.vnflcm.common.exceptions.RestClientException;
import com.ericsson.oss.services.vnflcm.common.exceptions.RestResponseException;
import com.ericsson.oss.services.vnflcm.common.models.RestResponse;
import com.ericsson.oss.services.vnflcm.common.util.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class RestCallExecution implements RestCallExecutor {

 private static final String X_AUTH_TOKEN = "X-Auth-Token";//Only for HPE so far.
private final Logger LOGGER = LoggerFactory.getLogger(getClass());
 private final CallExecutionType callExecutionType;


 public RestCallExecution(final CallExecutionType callExecutionType) {
  this.callExecutionType = callExecutionType;
 }

 public abstract ResponseDTO setOperationResponse(
   final RestResponse restResponse,
   final RestCallDataDto restCallData);

 public abstract ResponseDTO setOperationExceptionResponse(final Exception e);

 @Override
 public ResponseDTO executeCall(final RestCallDataDto restCallData) throws NfvoCallException {
  final String callExecutionType = this.callExecutionType.toString();
  LOGGER.info("Starting rest call execution of  " + callExecutionType);
  validateRestCallData(restCallData);

  LOGGER.info("Proceeding to send request ", this);
  ResponseDTO response = new ResponseDTO();
        try {

            LOGGER.info("Initiating ecm authentication call", this);
            LOGGER.info("Data in send Request is {} ", restCallData == null ? null : Utils.getMaskedString(restCallData.toString()));
            final String token;
            final String Key;
            final Map<String, String> headerIncludingToken;
            if (restCallData.getNbiData().getStaticAuthenticationTokenName() != null
                    && restCallData.getNbiData().getStaticAuthenticationTokenName() != "") {
                LOGGER.info("Static authentication based on the token information provided in the nfvoconfig.");
                token = restCallData.getNbiData().getStaticAuthenticationTokenValue();
                Key = restCallData.getNbiData().getStaticAuthenticationTokenName();
                headerIncludingToken = addTokenHeader(restCallData.getHeaders(), Key, token);
            } else {
                if(restCallData.getAuthCallData().getAuthType().equalsIgnoreCase(CallExecutionConstants.OAUTH2)) {
                    final Map<String, String> oauth2_headers = addTokenHeader(restCallData.getAuthCallData().getHeaders(),CallExecutionConstants.CONTENT_TYPE , CallExecutionConstants.OAUTH2_CONTENT_TYPE);
                    restCallData.getAuthCallData().setHeaders(oauth2_headers);
                    restCallData.getAuthCallData().setAuthReqBody(CallExecutionConstants.OAUTH2_AUTH_BODY);
                }
                LOGGER.info("NFVO TYPE IS: {} ", restCallData.getNbiData().getNfvoType());
                boolean basicAuthImplSameAsEO = false;
                if (restCallData.getNbiData().getBasicAuthImplSameAsEO() != null) {
                    basicAuthImplSameAsEO = restCallData.getNbiData().getBasicAuthImplSameAsEO();
                }
                if (restCallData.getNbiData().getNfvoType() == null || restCallData.getNbiData().getNfvoType().isEmpty()
                        || restCallData.getNbiData().getNfvoType().equalsIgnoreCase("ECM")
                        || restCallData.getNbiData().getNfvoType().equalsIgnoreCase("EO")
                        || restCallData.getNbiData().getNfvoType().equalsIgnoreCase("CBND") || basicAuthImplSameAsEO) {
                    LOGGER.info("Authentication token for ECM/EO or CBND (Nokia 3pp NFVO).");
                    token = executeEcmAuthenticationCall(restCallData.getAuthCallData());
                    Key = CallExecutionConstants.AUTH_TOKEN;
                } else {
                    LOGGER.info("Authentication token for HPE or other NFVO...This implementation may be vendor specific.");
                    token = getHPEAuthenticationToken(restCallData);
                    Key = X_AUTH_TOKEN;
                }
                headerIncludingToken = addTokenHeader(restCallData.getHeaders(), Key, token);
            }
            if (null == token || token.isEmpty()) {
                LOGGER.debug("Authentication failed unable to fetch token to make rest call", this);
                throw new NfvoCallException("Authentication for token failed");
            }
            LOGGER.info("url is {} header is {} req body is {} and token is {} ", restCallData.getUrl(),
                    restCallData.getHeaders(), restCallData.getReqBody(), token);
            restCallData.setHeaders(headerIncludingToken);
            LOGGER.info("url data in rest call execution");
            LOGGER.info("req body is {} ", restCallData.getReqBody() == null ? null : Utils.getMaskedString(restCallData.getReqBody().toString()));
            if (CallExecutionType.PACKAGE == this.callExecutionType || CallExecutionType.QUERYPACKAGE == this.callExecutionType) {
                LOGGER.info("start RestCallExecution.executeCall callExecutionType {} ", callExecutionType);
                final RestResponse restResponse = PackageManagementUtils.sendRestCall(restCallData.getUrl(),
                        restCallData.getHeaders(), restCallData.getReqBody());
                LOGGER.info("end RestCallExecution.executeCall callExecutionType {} ", callExecutionType);
                if (CallExecutionType.PACKAGE == this.callExecutionType) {
                    response = setOperationResponse(restResponse, restCallData);
                }
                if (CallExecutionType.QUERYPACKAGE == this.callExecutionType) {
                    response = setOperationResponse(restResponse, restCallData);
                }
            } else if (CallExecutionType.QUERY_NFVO_VDC_LIST == this.callExecutionType) {
                LOGGER.info("start RestCallExecution.executeCall callExecutionType {} ", callExecutionType);
                final RestResponse restResponse = QueryNfvoVdcListUtils.sendRestCall(restCallData.getUrl(),
                        restCallData.getHeaders(), restCallData.getReqBody());
                LOGGER.info("end RestCallExecution.executeCall callExecutionType {} ", callExecutionType);
                response = setOperationResponse(restResponse, restCallData);
        }
        }catch (final NfvoCallException e) {
            LOGGER.info("Exception Occured: NfvoCallException");
            response = setOperationExceptionResponse(e);
            return response;
        } catch (final Exception e) {
            LOGGER.info("Exception Occured: Exception");
            response = setOperationExceptionResponse(e);
            this.setErrorResponse(response, "Unknown exception in executeCall for sending call to NFVO");
        }
  return response;
 }



 private void validateRestCallData(final RestCallDataDto restCallData) throws NfvoCallException {
  LOGGER.info("Validating Rest Call Data");
  String error = "";
  if (restCallData.getAuthCallData() == null) {
   error = error + "Auth data is null in restCallDataDto   ";
  } else if (restCallData.getAuthCallData().getAuthReqBody() == null) {
   error = error + "Auth data is null ";
  } else if (restCallData.getHeaders() == null || restCallData.getHeaders().size() == 0) {
   error = error + "Empty headers for auth data  ";
  } else if (restCallData.getHeaders() == null || restCallData.getHeaders().size() == 0) {
   error = error + "Rest call headers are not set  ";
  } else if (restCallData.getMethodType().equalsIgnoreCase(CallExecutionConstants.POST_METHOD) && (restCallData.getReqBody() == null || restCallData.getReqBody().isEmpty())) {
   error = error + "Req body is empty for call  ";
  } else if (restCallData.getSuccessCode() == 0) {
   error = error + "Success code is not set  ";
  } else if (restCallData.getUrl() == null || restCallData.getUrl().isEmpty()) {
   error = error + "url is empty";
  }
  LOGGER.info("Value of validation in validateRestCallData {}", error, this);
  if (!error.trim().isEmpty()) {
   LOGGER.error(
     "Required data is not present in RestCallData. Validation failed in Rest Call Execution. Message is {} ",
     error);
   throw new NfvoCallException(
     "Required data is not present in RestCallData. Validation failed in Rest Call Execution");
  }

 }

 protected ResponseDTO setErrorResponse(final ResponseDTO response, final String message) {
  LOGGER.debug("Error is {}", message);
  final ResponseDTO res = new ResponseDTO();
  res.setStatus(false);
  res.setDetails(message);
  return res;
 }

 public RestResponse sendRestRequest(
   final RestCallDataDto restCallData) throws NfvoCallException {
  LOGGER.info("Initiating ecm authentication call", this);
  LOGGER.info("Data in send Request is {} ", restCallData);
  final String token;
  final String Key;
  final Map<String, String> headers;
  if (null != restCallData.getNbiData().getStaticAuthenticationTokenName()
    && restCallData.getNbiData().getStaticAuthenticationTokenName() != "") {
   LOGGER.info("Static authentication based on the token information provided in the nfvoconfig.");
   token = restCallData.getNbiData().getStaticAuthenticationTokenValue();
   Key = restCallData.getNbiData().getStaticAuthenticationTokenName();
   headers = addTokenHeader(restCallData.getHeaders(),Key, token);
  } else {
      if(restCallData.getAuthCallData().getAuthType().equalsIgnoreCase(CallExecutionConstants.OAUTH2)) {
          final Map<String, String> oauth2_headers = addTokenHeader(restCallData.getAuthCallData().getHeaders(),CallExecutionConstants.CONTENT_TYPE , CallExecutionConstants.OAUTH2_CONTENT_TYPE);
          restCallData.getAuthCallData().setHeaders(oauth2_headers);
          restCallData.getAuthCallData().setAuthReqBody(CallExecutionConstants.OAUTH2_AUTH_BODY);
      }
   token = executeEcmAuthenticationCall(restCallData.getAuthCallData());
   LOGGER.info(
     "Authentication token fetched to make rest call -token=",
     token);
   Key = CallExecutionConstants.AUTH_TOKEN;
   headers = addTokenHeader(restCallData.getHeaders(),Key, token);
  }
  if (null == token || token.isEmpty()) {
   LOGGER.debug("Authentication failed unable to fetch token to make rest call", this);
   throw new NfvoCallException(CallExecutionErrorCode.AUTHENTICATION_FAILED.getValue(),
     CallExecutionErrorMessage.AUTHENTICATION_FAILED.getValue());
  }

  LOGGER.info("url is {} req body is {} and token is {} ", restCallData.getUrl(), restCallData.getReqBody(),
    token);
  final RestResponse response = NotificationUtils
    .sendRestCall(restCallData.getUrl(), headers, restCallData.getReqBody());

  return response;
 }

  private Map<String, String> addTokenHeader(final Map<String, String> headers,final String Key ,final String token) {
      headers.put(Key, token);
      return headers;
     }

 private String executeEcmAuthenticationCall(final AuthenticationCallDataDto authCallData) throws NfvoCallException {
  String token = "";
  if (authCallData == null) {
   LOGGER.info("Auth data is null in ", this);
   throw new NfvoCallException(340, "Auth Data is null");
  }
  LOGGER.info("Auth data is {} ", authCallData == null ? null : Utils.getMaskedString(authCallData.toString()));
  RestResponse response = null;
  try {
   response = RestClient.doPost(authCallData.getAuthUrl(), authCallData.getHeaders(),
     authCallData.getAuthReqBody());
  } catch (RestClientException | ConnectionFailureException | RestResponseException e) {
   LOGGER.error("Exception in making rest call ", e);
   throw new NfvoCallException(CallExecutionErrorCode.UNKNOWN_ERROR_NOTIFICATION_REST_CALL.getValue(),
     "Exception in making rest call");
  } catch (final Exception e) {
   LOGGER.error("Unknown exception in executeEcmAuthentication call", e, this);
   throw new NfvoCallException(CallExecutionErrorCode.UNKNOWN_ERROR_NOTIFICATION_REST_CALL.getValue(),
     "Unknown Exception in making rest call for authentication");
  }

  if (response != null) {
   LOGGER.info("Authentication Call response code {} and response details are {}", response.getResponseCode(),
           response.getResponseBody());
   if (response.getResponseBody() != null && !response.getResponseBody().isEmpty()) {
    final ObjectMapper objectMapper = new ObjectMapper();
    JsonNode root;
    try {
     root = objectMapper.readTree(response.getResponseBody());
     if(authCallData.getAuthType().equalsIgnoreCase(CallExecutionConstants.OAUTH2)) {
         if (root.findPath("access_token") != null) {
             LOGGER.info("Fetching oauth2 access token ", this);
             token = root.path("access_token").asText();
             LOGGER.info("Token fetched successfully", this);
         } else {
             LOGGER.error("oAuth2 Authentication failed.", this);
             LOGGER.error(response.getResponseBody(), this);
             if (root.findPath("error") != null) {
                 LOGGER.error(root.path("error").asText(), this);
             }
             token = "";
         }
     } else {
         final String status = root.path("status").path("reqStatus").asText();
         if (status.equals("SUCCESS")) {
             LOGGER.info("Fetching token ", this);
             final String credentials = root.path("status").path("credentials").asText();
             token = credentials;
             LOGGER.info("Token fetched successfully", this);
         } else {
             LOGGER.error("Authentication failed.", this);
             token = "";
         }
     }
    } catch (final IOException e) {
     LOGGER.error("authentication failed ", e);
     token = "";
    }

   } else {
    LOGGER.error("unable to fetch token in authentication", this);
    token = "";
   }
  } else {
   LOGGER.error("Null response recieved from NFVO", this);
   token = "";
  }
  return token;
 }
 
 
 private String getHPEAuthenticationToken(final RestCallDataDto restCallData) throws NfvoCallException {
     final AuthenticationCallDataDto authCallData=restCallData.getAuthCallData();
      String token = "";
      if (authCallData == null) {
       LOGGER.info("getHPEAuthenticationToken(): Auth data is null in ", this);
       throw new NfvoCallException(340, "Auth Data is null");
      }
      LOGGER.info("getHPEAuthenticationToken(): Auth data is {} ", authCallData == null ? null : Utils.getMaskedString(authCallData.toString()));
      RestResponse response = null;
      try {
          //Get the Domain {{url}}/nfvd-ext/domains
          // E.G. http://127.0.0.1/nfvd-ext/domains
       response = RestClient.doPost(authCallData.getAuthUrl(), authCallData.getHeaders(),
         authCallData.getAuthReqBody());
      } catch (RestClientException | ConnectionFailureException | RestResponseException e) {
       LOGGER.error("Exception in making rest call ", e);
       throw new NfvoCallException(CallExecutionErrorCode.UNKNOWN_ERROR_NOTIFICATION_REST_CALL.getValue(),
         "Exception in making rest call");
      } catch (final Exception e) {
       LOGGER.error("Unknown exception in executeEcmAuthentication call", e, this);
       throw new NfvoCallException(CallExecutionErrorCode.UNKNOWN_ERROR_NOTIFICATION_REST_CALL.getValue(),
         "Unknown Exception in making rest call for authentication");
      }
      LOGGER.info("getHPEAuthenticationToken(): Domain Id call response is {} ", response);
      if (response != null) {
          final String domainId = getDomainId(response);
          if (domainId != null) {
              // Post call to fetch the Token from HPE
              final String url = authCallData.getAuthUrl()+"/"+domainId+"/token";
              final Map<String, String> headers = new HashMap<>();
              headers.put(CallExecutionConstants.CONTENT_TYPE, CallExecutionConstants.APPLICATION_JSON);
              final StringBuffer restPayload = new StringBuffer();
              restPayload.append("{");
              restPayload.append("\"username\": ");
              restPayload.append("\"" + restCallData.getNbiData().getUsername() + "\",");
              restPayload.append("\"password\": ");
              restPayload.append("\"" + restCallData.getNbiData().getPassword() + "\"");
              restPayload.append("}");
              RestResponse tokenResponse = null;
              try {
                  tokenResponse = RestClient.doPost(url, headers,restPayload.toString());
              } catch (RestClientException | ConnectionFailureException | RestResponseException e) {
                   LOGGER.error("Exception in making rest call ", e);
                   throw new NfvoCallException(CallExecutionErrorCode.UNKNOWN_ERROR_NOTIFICATION_REST_CALL.getValue(),
                     "Exception in making rest call");
              } catch (final Exception e) {
                   LOGGER.error("Unknown exception in executeEcmAuthentication call", e, this);
                   throw new NfvoCallException(CallExecutionErrorCode.UNKNOWN_ERROR_NOTIFICATION_REST_CALL.getValue(),
                     "Unknown Exception in making rest call for authentication");
              }
              LOGGER.info("getHPEAuthenticationToken(): tokenResponse {} ", tokenResponse);
              if (tokenResponse != null && tokenResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                  final Map<String, List<String>> respHeaders = tokenResponse.getRespHeaders();
                  token = respHeaders.get(X_AUTH_TOKEN).get(0);
              }
          }
      }
      return token;
     }

    private String getDomainId(final RestResponse response) {
        String domainId = null;
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(response.getResponseBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final JsonNode node1 = node.get("instances-info");
        final Iterator<JsonNode> instancesElements = node1.elements();
        while (instancesElements.hasNext()) {
            final JsonNode next = instancesElements.next();
            domainId = next.get("id").asText();
            LOGGER.info("getHPEAuthenticationToken(): Domain Id  {} ", domainId);
            break; // Considering only one entry for plug test
        }
        return domainId;
    }

}
