package org.sheshant.messenger.database;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.sheshant.messenger.model.Comment;
import org.sheshant.messenger.model.Employee;
import org.sheshant.messenger.model.ErrorMessage;
import org.sheshant.messenger.model.Message;
import org.sheshant.messenger.model.MessageComment;
import org.sheshant.messenger.model.Profile;


public class DatabaseClass {
	
	private static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	public static ArrayList<Message> getAllMessages(){
		ArrayList<Message> list = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("from Message");
	         list = (ArrayList<Message>) query.list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return list;
	}
	public static ArrayList<Message> getAllMessages(int start,int limit){
		ArrayList<Message> list = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         String sql = "select * from Message limit :limit offset :offset";
	         SQLQuery query = session.createSQLQuery(sql);
	         query.addEntity(Message.class);
	         query.setParameter("offset", start);
	         query.setParameter("limit", limit);
	         list = (ArrayList<Message>) query.list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return list;
	}
	public static Message getMessage(long id){
		Message message = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("from Message E WHERE E.id = :messageid");
	         query.setParameter("messageid",id);
	         ArrayList<Message> list = (ArrayList<Message>) query.list();
	         if(list.size() > 0)
	        	 message = (Message) list.get(0);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      if(message == null)
	      {
				ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
				Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
				throw new NotFoundException(response);
		  }
	     return message;
	}
	public static ArrayList<Profile> getAllProfiles(){
		ArrayList<Profile> list = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("from Profile");
	         list = (ArrayList<Profile>) query.list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return list;
	}
	public static Profile getProfile(String profileName){
		Profile profile = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("from Profile E WHERE E.profileName = :profileName");
	         query.setParameter("profileName", profileName);
	         ArrayList<Profile> list = (ArrayList<Profile>) query.list();
	         if(list.size() > 0)
	        	 profile = list.get(0);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      if(profile == null)
	      {
				ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
				Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
				throw new NotFoundException(response);
		  }
	     return profile;
	}
	public static ArrayList<Comment> getAllComments(long messageid){
		ArrayList<Comment> list = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         String sql = "select * from COMMENT where ID in (Select COMMENT_ID from MESSAGE_COMMENT where MESSAGE_ID = :messageid)";
	         SQLQuery query = session.createSQLQuery(sql);
	         query.addEntity(Comment.class);
	         query.setParameter("messageid", messageid);
	         list = (ArrayList<Comment>) query.list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return list;
	}
	public static ArrayList<Message> getAllMessagesYear(int year){
		ArrayList<Message> list = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("from Message WHERE year(CREATED) = :year");
	         query.setParameter("year", year);
	         list = (ArrayList<Message>) query.list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return list;
	}
	public static long getMaxMessage(){
		long max = 0;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         String sql = "select coalesce(max(ID),1) from message";
	         SQLQuery query = session.createSQLQuery(sql);
	         max = ((BigInteger) query.uniqueResult()).longValue();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return max;
	}
	public static void addMessage(Message message){
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         session.save(message);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	}
	public static Message removeMessage(long id){
		Session session = factory.openSession();
		Message m = DatabaseClass.getMessage(id);
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createSQLQuery("CALL RemoveMessage(:id)");
	         query.setParameter("id", id);
	         query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return m;
	}
	public static Message updateMessage(Message message){
		long id = message.getId();
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         SQLQuery query = session.createSQLQuery("update Message set message = :message, author = :author WHERE id :id");
	         query.setParameter("message", message.getMessage());
	         query.setParameter("author", message.getAuthor());
	         query.setParameter("id", message.getId());
	         query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	    }
		return message;
	}
	public static ArrayList<Message> getAllMessagesForYearStartSize(int year,int start,int size){
		ArrayList<Message> list = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         String sql = "select * from Message WHERE year(CREATED) = :year limit :limit offset :offset";
	         SQLQuery query = session.createSQLQuery(sql);
	         query.addEntity(Message.class);
	         query.setParameter("year", year);
	         query.setParameter("offset", start);
	         query.setParameter("limit", size);
	         list = (ArrayList<Message>) query.list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return list;
	}
	public static Comment getComment(long messageid,long commentid){
		Comment comment = null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         System.out.println(messageid);
	         System.out.println(commentid);
	         String sql = "select * from COMMENT where ID in (Select COMMENT_ID from MESSAGE_COMMENT where MESSAGE_ID = :messageid) and ID = :commentid";
	         SQLQuery query = session.createSQLQuery(sql);
	         query.addEntity(Comment.class);
	         query.setParameter("messageid", messageid);
	         query.setParameter("commentid", commentid);
	         ArrayList<Comment> list = (ArrayList<Comment>) query.list();
	         if(list.size() > 0)
	        	 comment = list.get(0);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      if(comment == null)
	      {
				ErrorMessage errorMessage = new ErrorMessage("Not found", 404, "https://stackoverflow.com/");
				Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
				throw new NotFoundException(response);
		  }
	     return comment;
	}
	public static long getMaxMessageComment(){
		long max = 0;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         String sql = "select coalesce(max(ID),1) from MESSAGE_COMMENT";
	         SQLQuery query = session.createSQLQuery(sql);
	         max = ((BigInteger) query.uniqueResult()).longValue();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return max;
	}
	public static void addComment(long messageId, Comment comment){
		Session session = factory.openSession();
		System.out.println(DatabaseClass.getMaxMessageComment());
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         session.save(comment);
	         MessageComment messagecomment = new MessageComment(DatabaseClass.getMaxMessageComment()+1,messageId,comment.getId());
	         session.save(messagecomment);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	}
	public static long getMaxComment(){
		long max = 0;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         String sql = "select coalesce(max(ID),1) from Comment";
	         SQLQuery query = session.createSQLQuery(sql);
	         max = ((Number) query.uniqueResult()).longValue();;
	         System.out.println(max);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return max;
	}
	
	public static Comment removeComment(long messageId, long commentId){
		Session session = factory.openSession();
		Comment c = DatabaseClass.getComment(messageId, commentId);
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query1 = session.createQuery("delete from Comment where id = :id");
	         query1.setParameter("id", commentId);
	         query1.executeUpdate();
	         Query query2 = session.createQuery("delete from MessageComment where messageid = :messageid and commentid = :commentid");
	         query2.setParameter("commentid", commentId);
	         query2.setParameter("messageid", messageId);
	         query2.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return c;
	}

	public static void updateComment(long messageId, Comment comment) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         SQLQuery query = session.createSQLQuery("update Comment set message = :message, author = :author WHERE id :id");
	         query.setParameter("message", comment.getMessage());
	         query.setParameter("author", comment.getAuthor());
	         query.setParameter("id", comment.getId());
	         query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	    }
	}
	public static long getMaxProfileID(){
		long max = 0;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         String sql = "select coalesce(max(ID),1) from Profile";
	         SQLQuery query = session.createSQLQuery(sql);
	         max = ((Number) query.uniqueResult()).longValue();;
	         System.out.println(max);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	     return max;
	}
	public static Profile addProfile(Profile profile){
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         session.save(profile);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return profile;
	}
	public static Profile removeProfile(String profileName){
		Profile profile = DatabaseClass.getProfile(profileName);
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createSQLQuery("CALL RemoveProfile(:profileName)");
	         query.setParameter("profileName", profileName);
	         query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return profile;
	}
	public static Profile updateProfile(Profile profile){
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("update Profile set firstName = :firstName, lastName = :lastName where profileName = :profileName");
	         query.setParameter("firstName", profile.getFirstName());
	         query.setParameter("lastName", profile.getLastName());
	         query.setParameter("profileName", profile.getProfileName());
	         query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return profile;
	}
}
