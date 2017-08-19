package org.sheshant.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sheshant.messenger.database.DatabaseClass;
import org.sheshant.messenger.model.Comment;
import org.sheshant.messenger.model.ErrorMessage;
import org.sheshant.messenger.model.Message;

public class CommentService {
	
//	private Map<Long, Message> messages = DatabaseClass.getMessages();
	
	public List<Comment> getAllComments(long messageId) {
		return DatabaseClass.getAllComments(messageId);
	}
	
	public Comment getComment(long messageId, long commentId) {
		ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
		Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
		
		Message message = DatabaseClass.getMessage(messageId);
		if (message == null) {
			throw new WebApplicationException(response);
		}
		Comment comment = DatabaseClass.getComment(messageId, commentId);
		if (comment == null) {
			throw new NotFoundException(response);
		}
		return comment;
	}
	
	public Comment addComment(long messageId, Comment comment) {
		comment.setId(DatabaseClass.getMaxComment() + 1);
		DatabaseClass.addComment(messageId, comment);
		return comment;
	}
	
	public Comment updateComment(long messageId, Comment comment) {
		if (comment.getId() <= 0) {
			return null;
		}
		if(getComment(messageId,comment.getId()) != null)
			DatabaseClass.updateComment(messageId, comment);
		else
		{
			ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new NotFoundException(response);
		}
		return comment;
	}
	
	public Comment removeComment(long messageId, long commentId) {
		ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
		Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
		if(getComment(messageId,commentId) != null)
			return DatabaseClass.removeComment(messageId, commentId);
		else
			throw new NotFoundException(response);
	}	
}
