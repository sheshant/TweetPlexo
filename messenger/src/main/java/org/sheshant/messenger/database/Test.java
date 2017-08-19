package org.sheshant.messenger.database;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.sheshant.messenger.model.Comment;
import org.sheshant.messenger.model.Employee;
import org.sheshant.messenger.model.Message;
import org.sheshant.messenger.model.Profile;
import org.sheshant.messenger.service.CommentService;
import org.sheshant.messenger.service.MessageService;
import org.sheshant.messenger.service.ProfileService;
 
public class Test {
    public static void main(String[] args) {
        /*Session session = HibernateUtil.getSessionFactory().openSession();
 
        session.beginTransaction();
        User user = new User();
 
        user.setUserId(1);
        user.setUsername("Mukesh");
        user.setCreatedBy("Google");
        user.setCreatedDate(new Date());
 
        session.save(user);
        session.getTransaction().commit();
 
    }*/
    	
    	Profile profile = DatabaseClass.removeProfile("sheshu");
    	System.out.println(profile.getFirstName() + " " + profile.getLastName());
//    	Profile profile = new Profile(1,"sheshu", "my first name updated", "my last name updated");
//    	ProfileService ps = new ProfileService();
//    	List<Profile> p = ps.getAllProfiles();
//    	for(int i = 0 ; i < p.size(); i ++)
//    	{
//    		System.out.println(p.get(i).getFirstName() + " " + p.get(i).getLastName());
//    	}
    	
//    	Comment c = new Comment(3,"stf updated again","shazzy");
//    	CommentService cs = new CommentService();
//    	cs.updateComment(3, c);
//    	System.out.println(DatabaseClass.getAllMessages());
//    	System.out.println(DatabaseClass.getAllMessagesYear(2017));
//    	System.out.println(DatabaseClass.getMaxMessage());
//    	Message m = new Message(1,"stf","shazzy");
//    	MessageService ms = new MessageService();
//    	ms.addMessage(m);
//    	System.out.println(DatabaseClass.getAllMessages());
////    	DatabaseClass.removeMessage(3);
//    	m.setId(1);
//    	m.setMessage("random message");
//    	System.out.println(DatabaseClass.updateMessage(m));
//    	System.out.println(DatabaseClass.getMessage(1));
//    	System.out.println(DatabaseClass.getAllMessages(0,1).get(0).getMessage());
//    	System.out.println(DatabaseClass.getAllComments(1).get(0).getMessage());
    	/*Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");// populates the data of the
                                            // configuration file

        // creating seession factory object
        SessionFactory factory = cfg.buildSessionFactory();

        // creating session object
        Session session = factory.openSession();

        // creating transaction object
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("from Employee");
        List<Employee> list = query.list();
        System.out.println(list.get(0));
        t.commit();
        session.close();*/
}
}