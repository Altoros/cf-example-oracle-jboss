package controller;

import com.sun.jersey.spi.resource.Singleton;
import jdbc.JDBCService;
import jdbc.JDBCServiceFactory;
import jdbc.MessageData;
import json.JsonHttpMessageConverter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author: Andrey Kozlov
 */
@Path("/")
@Singleton
public class MessageDataController {

    private final JDBCService jdbcService;

    public MessageDataController(){
        jdbcService = JDBCServiceFactory.getJdbcService();
    }

    @GET
    @Path("/lastMessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastMessage(){
        Integer lastMessage = jdbcService.getLastMessage();
        return okJsonResponse(lastMessage);
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessageCount(){
        Integer messageCount = jdbcService.getMessageCount();
        return okJsonResponse(messageCount);
    }

    @GET
    @Path("/messagedata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessageData(){
        MessageData messageData = jdbcService.getMessageData();
        return okJsonResponse(messageData);
    }

    private static Response okJsonResponse(Object src) {
        return Response.ok(JsonHttpMessageConverter.instanceOf().convert(src)).build();
    }
}
