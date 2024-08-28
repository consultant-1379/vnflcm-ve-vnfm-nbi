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

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotBlank;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.validation.AppValidationMessages;

@Path(VnfLcmOperationRestService.PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface VnfLcmOperationRestService {

    String PATH = "/vnflcm/v1/vnf_lcm_op_occs";

    /**
     * This operation provides information about a VnfLcmOperation.
     *
     * @param vnfLcmOpOccId
     *            the VnfLcmOperation for which the info is needed.
     * @return Response containing information about the VnfLcmOperation occurrence
     */
    @Path("/{vnfLcmOpOccId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getLcmOperationOcc(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfLcmOpOccId") final String vnfLcmOpOccId);

    /**
     * This operation provides information about all VnfLcmOperation.
     *
     * @return Response containing information about all the VnfLcmOperation occurrences
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllLcmOperationOcc(@HeaderParam("Pagination") String paginationFlag);

    /**
     * This operation provides retry for provided  VnfLcmOperation.
     *
     *@param vnfLcmOpOccId
     *            the VnfLcmOperation for which retry is needed.
     * @return
     */
    @Path("/{vnfLcmOpOccId}/retry")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response retryOperation(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfLcmOpOccId") final String vnfLcmOpOccId, @Context HttpHeaders headers);

    /**
     * This operation provides fail for provided  VnfLcmOperation.
     *
     *@param vnfLcmOpOccId
     *            the VnfLcmOperation for which fail is needed.
     * @return
     */
    @Path("/{vnfLcmOpOccId}/fail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response failOperation(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfLcmOpOccId") final String vnfLcmOpOccId, @Context HttpHeaders headers);

    /**
     * This operation provides rollback for provided  VnfLcmOperation.
     *
     *@param vnfLcmOpOccId
     *            the VnfLcmOperation for which rollback is needed.
     * @return
     */
    @Path("/{vnfLcmOpOccId}/rollback")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response rollbackOperation(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfLcmOpOccId") final String vnfLcmOpOccId, @Context HttpHeaders headers);

    /**
     * This operation updates status id of life cycle operation which is in processing state.
     * 
     * @param vnfLcmOpOccId
     *            the VnfLcmOperation for which the status id need to update.
     * @return Response containing status of VnfLcmOperation occurrence
     */
    @Path("/{vnfLcmOpOccId}")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    Response updateLcmOperationOccStatus(@NotBlank(message = AppValidationMessages.REQUIRED_NOT_EMPTY_MESSAGE) @PathParam("vnfLcmOpOccId") final String vnfLcmOpOccId);
}
