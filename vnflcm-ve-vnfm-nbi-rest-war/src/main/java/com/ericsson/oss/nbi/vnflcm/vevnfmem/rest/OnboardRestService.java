/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(OnboardRestService.PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface OnboardRestService {

    String PATH = "/vcd";

    /**
    * This operation onboards OVF package to VCD VIM.
    *
    * @param pkgOnboardRequest
    *            the package onboard request.
    * @return Response containing information about the onboarded package
    */
    @Path("/pkgOnboard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @POST
    Response pkgOnboard(final InputStream inputStream, @Context UriInfo uriInfo);
}
