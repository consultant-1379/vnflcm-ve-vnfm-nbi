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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.ericsson.oss.services.vnflcm.config.properties.util.ReadPropertiesUtility;

public class URIExistenceValidator implements ConstraintValidator<URIType, String> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void initialize(final URIType enumType) {
    }

    // This class is not used yet and suppose to used in URIType.java. After adding this getting error in jenkins build
    //though it's working fine in local deployment.
    @Override
    public boolean isValid(final String urlValue, final ConstraintValidatorContext context) {
        boolean isValid = false;
        logger.debug("urlValue: {}", urlValue);
        if (urlValue == null || urlValue.trim().isEmpty() || !urlValue.matches(ValidationPatterns.HTTP_START_URL_PATTERN)) {
            // Return true. These validations will be done by use of different annotations.
            return true;
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(urlValue).openConnection();
            conn.setRequestMethod("HEAD");// Don't GET the complete file. Just issue a HEAD request.
            final String authentication = "";//ReadPropertiesUtility.getUserInitAuthentication();
            if (authentication != null && authentication != "") {
                conn.setRequestProperty("Authorization", "Basic " + authentication.trim());
            } else {
                logger.debug("Since userName or password value was not available, Authorization property is not set.");
            }
            final int response_code = conn.getResponseCode();
            isValid = HttpURLConnection.HTTP_OK == response_code;
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate() + " " + "Http response code : "
                        + response_code + " , " + conn.getResponseMessage()).addConstraintViolation();
            }

        } catch (final Exception e) {
            logger.error("Exception occured.", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return isValid;
    }
}
