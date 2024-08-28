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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ChangeCurrentVnfPkgRequest;

@Path(VnfRestService.PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface VnfRestService {

    String PATH = "/vnflcm";

    /**
     * To create the VNF Instance identifier
     *
     * @param createVnfRequest
     *            the Create Vnf Request
     * @return Response containing information about the VNF Instances
     */
    @Path("/v1/vnf_instances")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @POST
    Response createVnf(final InputStream inputStream , @Context UriInfo uriInfo, @Context HttpHeaders headers);

    /**
     * To operate the VNF Instance identifier
     *
     * @param operateVnfRequest
     *            the Operate Vnf Request
     * @return Response containing information about the VNF Instances
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}/operate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    Response operateVnf(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId,
            final InputStream inputStream , @Context UriInfo uriInfo, @Context HttpHeaders headers);

    /**
     * To update a VNF Instance Identifier
     *
     * @param vnfInstanceId
     *            ID of the VNF Instance to be deleted from VNFM persistence store
     * @return Response containing status of the delete operation.
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}")
    @Consumes({MediaType.WILDCARD}) //Modify supprots "application/mergepatchjson" content-type as per SOL003
    @Produces(MediaType.APPLICATION_JSON)
    @PATCH
    Response modifyVnf(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId,
            final InputStream inputStream, @Context UriInfo uriInfo, @Context HttpHeaders headers);

    /**
     * To delete a VNF Instance Identifier
     *
     * @param vnfInstanceId
     *            ID of the VNF Instance to be deleted from VNFM persistence store
     * @return Response containing status of the delete operation.
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    Response deleteVnfId(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId, @Context HttpHeaders headers);

    /**
     *
     * @param instantiateVnfRequest
     * @return
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}/instantiate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    @POST
    Response instantiateVnf(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId,
            final InputStream inputStream, @Context UriInfo uriInfo, @Context HttpHeaders headers);

    /**
     *
     * @param terminateVnfRequest
     * @return
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}/terminate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @POST
    Response terminateVnfInstance(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId,
            final InputStream inputStream, @Context UriInfo uriInfo, @Context HttpHeaders headers);

    /**
     *
     * @param vnfInstanceId
     * @param changeVnfRequest
     * @param uriInfo
     * @return
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}/change_vnfpkg")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    @POST
    Response changeVnfRequest(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId,
            final ChangeCurrentVnfPkgRequest changeVnfRequest, @Context UriInfo uriInfo, @Context HttpHeaders headers);

    /**
     * To scale out/in the VNF Instance
     *
     * @param scaleVnfRequest
     *            the Scale Vnf Request
     * @return Response containing status of the scale operation.
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}/scale")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @POST
    Response scaleVnf(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId,
            final InputStream inputStream, @Context UriInfo uriInfo, @Context HttpHeaders headers);

    /**
     * To heal the VNF Instance
     *
     * @param healVnfRequest
     *            the Heal Vnf Request
     * @return Response containing status of the heal operation.
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}/heal")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @POST
    Response healVnf(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId,
            final InputStream inputStream, @Context UriInfo uriInfo, @Context HttpHeaders headers);

    @Path("/v1/subscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @POST
    Response subscription(final InputStream inputStream, @Context UriInfo uriInfo);

    /**
     * This operation provides information about a VNF instances.
     *
     * @param vnfInstanceId
     *            the VNF Instance for which the info is needed.
     * @return Response containing information about the VNF Instances
     */
    @Path("/v1/vnf_instances/{vnfInstanceId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response queryVnf(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfInstanceId") final String vnfInstanceId, @Context HttpHeaders headers);

    /**
     * This operation provides information about all VNF instances.
     *
     * @param vnfInstanceId
     *            the VNF Instance for which the info is needed.
     * @return Response containing information about the VNF Instances
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/v1/vnf_instances")
    Response queryAllVnf(@DefaultValue("true") @HeaderParam("queryVimResource") boolean queryVimResource, @HeaderParam("Pagination") String paginationFlag);

    /**
     * This operation provides information about the SOL002 vnflcm api versions.
     *
     * @return Response containing vnflcm api versions
     *
     */
    @Path("/api_versions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiVersions();

    /**
     * This API reads the workflow definitions from WFS component via vnflcm service/nbi 
     * @param definitionName
     * @param bundleId
     * @return
     */
    @Path("/workflow/definitions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getWorkflowDefinitions(final @QueryParam("definitionName") String definitionName, final @QueryParam("bundleId") String bundleId, final @QueryParam("refreshCache") String refreshCache);

}
