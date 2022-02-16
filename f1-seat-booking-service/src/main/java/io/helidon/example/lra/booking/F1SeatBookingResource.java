
package io.helidon.example.lra.booking;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.logging.Logger;

@Path("/seat")
@ApplicationScoped
public class F1SeatBookingResource {

    private static final Logger LOG = Logger.getLogger(F1SeatBookingResource.class.getSimpleName());
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());


    @GET
    @Path("/book/{seat}")
    @LRA(value = LRA.Type.MANDATORY, end = false)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                  @PathParam("seat") String seat) {
        if (!seat.equals("1f")) {
            LOG.info("Creating booking for " + seat);
            return Response.ok().build();
        } else {
            LOG.info("Seat " + seat + " already booked!");
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(JSON.createObjectBuilder()
                            .add("error", "Seat " + seat + " is already reserved!")
                            .add("seat", seat)
                            .build())
                    .build();
        }
    }

    @Compensate
    public Response seatBookingFailed(URI lraId) {
        LOG.info("Seat booking failed! " + lraId);
        // If the participant status is not confirmed as completed, coordinator retries the call eventually
        return Response.ok(ParticipantStatus.Completed.name()).build();
    }

    @Complete
    public Response seatBookingSuccessful(URI lraId) {
        LOG.info("Seat booking success! " + lraId);
        return Response.ok(ParticipantStatus.Completed.name()).build();
    }

}
