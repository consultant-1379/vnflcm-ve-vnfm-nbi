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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * Converts date into UTC ISO 8601 format.
     *
     * @param date
     * @return String
     */
    public static String toISO8601UTC(final Date date) {
        final TimeZone tz = TimeZone.getTimeZone("UTC");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    /**
     * Converts time to default timezone
     * @param filterParameters
     * @param dateParam
     * @return
     * @throws ParseException
     * This method is very important for Pagination to work
     */
    public static List<String> convertTimeStampToDefault(final Map<String, List<String>> customParamsMap, final String dateParam)
            throws ParseException {
    	List<String> convertedNextPageMarkers = null;
        final List<String> nextPageMarkers = customParamsMap.get(dateParam);
        String nextPageOpaqueMarker = null;
        try {
            if (nextPageMarkers !=  null && !nextPageMarkers.isEmpty()) {
                nextPageOpaqueMarker = nextPageMarkers.get(0);
                log.info("nextPageOpaqueMarker received in query={}",nextPageOpaqueMarker);
                final DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                final Date date = utcFormat.parse(nextPageOpaqueMarker);
                final DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                defaultFormat.setTimeZone(TimeZone.getDefault());
                final String convertedTimeStamp = defaultFormat.format(date);
                log.info("nextPageOpaqueMarker convertedTimeStamp DB format={}",convertedTimeStamp);
                convertedNextPageMarkers = new ArrayList<>();
                convertedNextPageMarkers.add(convertedTimeStamp);
                return convertedNextPageMarkers;
            }
        } catch (final ParseException pex) {
            log.info("nextPageOpaqueMarker {} is not in correct format",nextPageOpaqueMarker);
        }
        return convertedNextPageMarkers;
    }

    public static String getMaskedString(String message) {
        try {
            if (message != null && !message.trim().isEmpty()) {
                message = message.replaceAll("[+^*? ]*", "");
                if (Pattern.compile(Pattern.quote("PASSwORD"), Pattern.CASE_INSENSITIVE).matcher(message).find()) {
                    final Pattern pattern = Pattern.compile(CallExecutionConstants.PATTERN_PASSWORD, Pattern.CASE_INSENSITIVE);
                    final Matcher matcher = pattern.matcher(message);
                    String pass ="";
                    while (matcher.find()) {
                    final String[] split = matcher.group(0).replaceAll("[\"]", "").replaceAll("[:=]", "=").split("=");
                    if(matcher.group(0).contains(":\"")) {
                         pass = split[0] + "\":\"xxxxxxxxxxxx";
                    }
                    else{
                         pass = split[0] + "=xxxxxxxxxxxxx";
                    }
                        message = message.replaceFirst(matcher.group(0), pass);
                    }
                }
                if (message.contains("clientId")) {
                    final Pattern pattern = Pattern.compile(CallExecutionConstants.PATTERN_CLIENTID);
                    final Matcher matcher = pattern.matcher(message);
                    String clientId ="";
                    while (matcher.find()) {
                    final String[] split = matcher.group(0).replaceAll("[\"]", "").replaceAll("[:=]", "=").split("=");
                    if (matcher.group(0).contains(":\"")) {
                         clientId = split[0] + "\":\"xxxxxxxxxxxx";
                    }
                    else {
                         clientId = split[0] + "=xxxxxxxxxxxxx";
                    }
                        message = message.replaceFirst(matcher.group(0), clientId);
                    }
                }
                if (message.contains("clientSecret")) {
                    final Pattern pattern = Pattern.compile(CallExecutionConstants.PATTERN_CLIENTSECRET);
                    final Matcher matcher = pattern.matcher(message);
                    String clientSecret ="";
                    while (matcher.find()) {
                    final String[] split = matcher.group(0).replaceAll("[\"]", "").replaceAll("[:=]", "=").split("=");
                    if (matcher.group(0).contains(":\"")) {
                        clientSecret = split[0] + "\":\"xxxxxxxxxxxx";
                    }
                    else {
                        clientSecret = split[0] + "=xxxxxxxxxxxxx";
                    }
                        message = message.replaceFirst(matcher.group(0), clientSecret);
                    }
                }
                if (message.contains("Authorization")) {
                    final Pattern pattern = Pattern.compile(CallExecutionConstants.PATTERN_AUTHORIZATION);
                    final Matcher matcher = pattern.matcher(message);
                    String Authorization ="";
                    while (matcher.find()) {
                    final String[] split = matcher.group(0).replaceAll("[\"]", "").replaceAll("[:=]", "=").split("=");
                    if (matcher.group(0).contains(":\"")) {
                         Authorization = split[0] + "\":\"xxxxxxxxxxxx";
                    }
                    else {
                         Authorization = split[0] + "=xxxxxxxxxxxxx";
                    }
                        message = message.replaceFirst(matcher.group(0), Authorization);
                    }
                }
            }
        } catch (final PatternSyntaxException pse) {
            log.error("Invalid Pattern:: {}", pse.getMessage());
        } catch (final Exception ex) {
            log.error("Exception occured:: {}", ex.getMessage());
        }
        return message;
    }

    public static String getMaskedSensitiveParams(String message, final List<String> sensitiveParamsList) {
        try {
            if (message != null && !message.trim().isEmpty()) {
                message = message.replaceAll("[+^*? ]*", "");
                if (sensitiveParamsList != null && !sensitiveParamsList.isEmpty()) {
                    for (int i = 0; i < sensitiveParamsList.size(); i++) {
                        final String key = sensitiveParamsList.get(i);
                        if (Pattern.compile(Pattern.quote(key)).matcher(message).find()) {
                            final Pattern pattern = Pattern.compile("(?:(?:" + key + "\\\"\\:)(?:\\\")?(?:[^\\,}\\\"\\]]*))");
                            final Matcher matcher = pattern.matcher(message);
                            while (matcher.find()) {
                                final String[] split = matcher.group(0).replaceAll("[\"]", "").replaceAll("[:=]", "=").split("=");
                                String pass = "";
                                if (matcher.group(0).contains(":\"")) {
                                    pass = split[0] + "\":\"xxxxxxxxxxxx";
                                } else {
                                    pass = split[0] + "\":xxxxxxxxxxxx";
                                }
                                message = message.replaceFirst(matcher.group(0), pass);
                            }
                        }
                    }
                }
            }
        } catch (final PatternSyntaxException pse) {
            log.error("Invalid Pattern:: {}", pse.getMessage());
        } catch (final Exception ex) {
            log.error("Exception occured:: {}", ex.getMessage());
        }
        return getMaskedString(message);
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
