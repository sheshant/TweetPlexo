package org.sheshant.messenger.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.sheshant.messenger.database.*;
import org.sheshant.messenger.exception.DataNotFoundException;
import org.sheshant.messenger.model.ErrorMessage;
import org.sheshant.messenger.model.Message;

public class MessageService {
	

	
	public MessageService() {
	}
	
	public List<Message> getAllMessages() { 
		return DatabaseClass.getAllMessages();
	}
	
	public List<Message> getAllMessagesForYearStartSize(int year,int start,int size) {
		return DatabaseClass.getAllMessagesForYearStartSize(year,start,size);
	}
	
	public List<Message> getAllMessagesForYear(int year) {
		return DatabaseClass.getAllMessagesYear(year);
	}
	
	public List<Message> getAllMessagesPaginated(int start, int size) {
		return DatabaseClass.getAllMessages(start,size);
	}
	
	
	public Message getMessage(long id) {
		Message message = DatabaseClass.getMessage(id);
		if(message == null)
		{
			ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new NotFoundException(response);
		}
		return message;
	}
	
	public Message addMessage(Message message) {
		message.setId(DatabaseClass.getMaxMessage() + 1);
		DatabaseClass.addMessage(message);
		return message;
	}
	
	public Message updateMessage(Message message) {
		if (message.getId() <= 0) {
			return null;
		}
		DatabaseClass.updateMessage(message);
		return message;
	}
	
	public Message removeMessage(long id) {
		return DatabaseClass.removeMessage(id);
	}
}
