package io.helidon.example.lra.booking;

import java.net.URI;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.lra.annotation.AfterLRA;
import org.eclipse.microprofile.lra.annotation.LRAStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

@Path("/f1booking")
@ApplicationScoped
public class F1BookingResource {

    private static final Logger LOG = Logger.getLogger(F1BookingResource.class.getSimpleName());

    @GET
    @Path("/book/{seat}/{cardNumber}")
    @LRA(value = LRA.Type.REQUIRES_NEW, end = false, timeLimit = 30)
    @Produces(MediaType.APPLICATION_JSON)
    public Response makeBooking(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                @PathParam("seat") String seat,
                                @PathParam("cardNumber") String cardNumber) {
        LOG.info("Payment " + cardNumber);
        // Notice that we don't need to propagate LRA header
        // When using JAX-RS client, LRA header is propagated automatically
        ClientBuilder.newClient()
                .target("http://f1-seat-booking-service:7001")
                .path("/seat/book/"+seat)
                .request()
                .rx()
                .get()
                .whenComplete((res, t) -> {
                    if (res != null) {
                        LOG.info("Seat booking service response: "
                                + res.getStatus()
                                + " " + res.getStatusInfo().getReasonPhrase()
                                + " " +lraId);
                        res.close();
                    }
                });

        ClientBuilder.newClient()
                .target("http://f1-payment-service:7002")
                .path("/payment/confirm/"+cardNumber)
                .request()
                .rx()
                .get()
                .whenComplete((res, t) -> {
                    if (res != null) {
                        LOG.info("Payment service response: "
                                + res.getStatus()
                                + " " + res.getStatusInfo().getReasonPhrase()
                                + " " +lraId);
                        res.close();
                    }
                });
        return Response.accepted().build();
    }

    @AfterLRA
    public void onLRAEnd(URI lraId, LRAStatus status) {
        LOG.info("Payment " + status + " " + lraId);
    }

}
