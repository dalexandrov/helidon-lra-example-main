
package io.helidon.example.lra.payments;

import java.net.URI;
import java.util.Collections;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.helidon.microprofile.server.Server;

import org.eclipse.microprofile.lra.annotation.AfterLRA;
import org.eclipse.microprofile.lra.annotation.LRAStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

@Path("/payment")
public class PaymentResource {

    private static final Logger LOG = Logger.getLogger(PaymentResource.class.getName());
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    @GET
    @Path("/confirm/{cardNumber}")
    @LRA(value = LRA.Type.MANDATORY, end = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response makePayment(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                @PathParam("cardNumber") String cardNumber) {
        if (cardNumber.equals("0000-0000-0000")) {
            LOG.warning("⛔️ Payment " + cardNumber);
            throw new IllegalStateException("Card " + cardNumber + " is not valid! " + lraId);
        }
        LOG.info("Payment " + cardNumber + " " + lraId);
        return Response.ok(JSON.createObjectBuilder().add("result", "success").build()).build();
    }

    @AfterLRA
    public void afterLra(URI lraId, LRAStatus lraStatus) {
        LOG.info("LRA finished " + lraId.toASCIIString() + " " + lraStatus.name());
    }
}
