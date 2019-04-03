/*
 *  Copyright (C) 2011 Paul Scherrer Institute
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.psi.fda.aq;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ch.psi.fda.model.v1.Recipient;

/**
 * Agent to send out notifications to specified recipients.
 */
public class NotificationAgent {

    private final static String smsPostfix = "@sms.switch.ch";

    private Properties properties;
    private String fromAddress;
    private final List<Recipient> recipients = new ArrayList<Recipient>();

    /**
     * Constructor
     * @param host		SMTP server to send notifications to
     * @param recipients	List of recipients
     * @param from		Address string that will show up in the from field. For example: fda@psi.ch. This argument must not contain white spaces
     */
    public NotificationAgent(String host, String from){

        fromAddress = from;

        properties = new Properties();
        properties.put("mail.smtp.host", host);
    }

    
    
    public void sendNotification(String aSubject, String aBody, boolean error, boolean success) {

        for(Recipient recipient: recipients){
        	
        	if((error && recipient.isError()) || (success && recipient.isSuccess())){
	        	String receiver;
	        	
	        	// Verify mail recipients
	            if(recipient.getValue().matches("[0-9,\\\\.,-,a-z,A-Z]*@[0-9,\\\\.,a-z,A-Z]*")){
	                receiver = recipient.getValue();
	            }
	            else if(recipient.getValue().matches("[0-9]+")){
	                // Assume that it is a SMS number
	                receiver = recipient.getValue() + smsPostfix;
	            }
	            else{
	                Logger.getLogger(NotificationAgent.class.getName()).log(Level.WARNING, "Invalid email address");
	                continue;
	            }
	        	
	        	
	        	Logger.getLogger(NotificationAgent.class.getName()).log(Level.INFO, "Send notification to " + receiver);
	        	
	            //Here, no Authenticator argument is used (it is null).
	            //Authenticators are used to prompt the user for user
	            //name and password.
	            Session session = Session.getDefaultInstance(properties, null);
	            MimeMessage message = new MimeMessage(session);
	            try {
	                //The "from" address may be set in code, or set in the
	                //config file under "mail.from" ; here, the latter style is used
	                message.setFrom( new InternetAddress(fromAddress) );
	
	                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	                message.setSubject(aSubject);
	                message.setText(aBody);
	                Transport.send(message);
	            } catch (MessagingException ex) {
	                Logger.getLogger(NotificationAgent.class.getName()).log(Level.WARNING, "Failed to send notification to " + receiver, ex);
	            }
        	}
        }
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

}
