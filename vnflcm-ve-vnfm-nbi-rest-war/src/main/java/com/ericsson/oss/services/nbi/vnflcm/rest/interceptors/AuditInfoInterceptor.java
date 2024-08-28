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
package com.ericsson.oss.services.nbi.vnflcm.rest.interceptors;

import javax.ws.rs.ext.Provider;


import com.ericsson.oss.itpf.sdk.context.classic.ContextServiceBean;
import com.ericsson.oss.itpf.sdk.core.util.StringUtils;
import com.ericsson.oss.services.nbi.vnflcm.rest.common.util.Constants;
import com.ericsson.oss.services.wfs.internal.WorkflowInternalConstants;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class AuditInfoInterceptor implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditInfoInterceptor.class);
    private final ContextServiceBean ctxService = new ContextServiceBean();

    @Override
         public void filter(ContainerRequestContext requestContext) throws IOException {
             final String userName = requestContext.getHeaders().getFirst(Constants.USER_NAME_HEADER);
        LOGGER.debug("Username received in header is : {}", userName);

        if (!StringUtils.isEmpty(userName)) {
            ctxService.setContextValue(WorkflowInternalConstants.USERNAME_KEY, userName);
        } else {
                    // return (ServerResponse)
                    // Response.status(ErrorType.USER_HEADER_NOT_FOUND.getHttpCode())
                    // .entity(ErrorMessageUtil.generateUserHeaderMessage(ErrorType.USER_HEADER_NOT_FOUND)).build();

        }

    }

}