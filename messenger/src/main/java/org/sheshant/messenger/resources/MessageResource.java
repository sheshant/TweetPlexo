package org.sheshant.messenger.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.sheshant.messenger.model.ErrorMessage;
import org.sheshant.messenger.model.Message;
import org.sheshant.messenger.model.MessageDetails;
import org.sheshant.messenger.resources.beans.MessageFilterBean;
import org.sheshant.messenger.service.MessageService;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

	MessageService messageService = new MessageService();
	
	@GET
	public List<MessageDetails> getMessages(@BeanParam MessageFilterBean filterBean, @Context UriInfo uriInfo) {
		List<MessageDetails> messageDetailsList = new ArrayList<MessageDetails>();
		List<Message> messages = null; 
		if (filterBean.getYear() > 0 && filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			messages = messageService.getAllMessagesForYearStartSize(filterBean.getYear(),filterBean.getStart(), filterBean.getSize());
		}
		else if (filterBean.getYear() > 0) {
			messages = messageService.getAllMessagesForYear(filterBean.getYear());
		}
		else if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			messages = messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		else {
			messages = messageService.getAllMessages();
		}
		for (Message message : messages)
		{
			MessageDetails m = new MessageDetails(message);
			m.addLink(getUriForSelf(uriInfo, message), "self");
			m.addLink(getUriForProfile(uriInfo, message), "profile");
			m.addLink(getUriForComments(uriInfo, message), "comments");
			messageDetailsList.add(m);
		}
		return messageDetailsList;
	}

	@POST
	public Response addMessage(Message message, @Context UriInfo uriInfo) {
		
		Message newMessage = messageService.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri).entity(newMessage).build();
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long id, Message message) {
		message.setId(id);
		return messageService.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long id) {
		if(messageService.getMessage(id) == null)
		{
			ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new NotFoundException(response);
		}
		messageService.removeMessage(id);
	}
	
	
	@GET
	@Path("/{messageId}")
	public MessageDetails getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(id);
		MessageDetails messageDetails = new MessageDetails(message);
		messageDetails.addLink(getUriForSelf(uriInfo, message), "self");
		messageDetails.addLink(getUriForProfile(uriInfo, message), "profile");
		messageDetails.addLink(getUriForComments(uriInfo, message), "comments");
		return messageDetails;
		
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
	       		.path(MessageResource.class, "getCommentResource")
	       		.path(CommentResource.class)
	       		.resolveTemplate("messageId", message.getId())
	            .build();
	    return uri.toString();
	}

	private String getUriForProfile(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
       		 .path(ProfileResource.class)
       		 .path(message.getAuthor())
             .build();
        return uri.toString();
	}

	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
		 .path(MessageResource.class)
		 .path(Long.toString(message.getId()))
		 .build()
		 .toString();
		return uri;
	}
	
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
	
}