package com.apigee.alarm;


import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class NotificationService {

	private static final Log log = LogFactory.getLog(NotificationService.class);
	
	private static NotificationService instance = null;	
	
	private NotificationService () {		
	}
	
	public static NotificationService getInstance() {
		if (instance == null)
			instance = new NotificationService();
		return instance;
	}

	public String formatCriticalErrors(List<DeviceLogEntry> logs)
	{

		StringBuffer message = new StringBuffer();

		message.append(logs.size() + " critical errors where detected at " + (new Date()).toString() + " <br/>");

		message.append("Errors : <br/>");

		for(DeviceLogEntry log : logs)
		{
			message.append(log.toString() +"<br/>");
		}

		return message.toString();

	}

	public void sendCriticalErrors(List<DeviceLogEntry> logs, Long appId)
	{

		String subject = "Alarm Notification - Detected Critical Errors for" ;
		String body = formatCriticalErrors(logs);
		String email = MailConfig.getMailConfig().getEmailtoCC();

		Email alarmEmail = new Email(subject, body,email);



		if(!suppressAlarm(appId))
		{
			log.info("Sending critical error alarms for app : " + appId);
			AsyncMailer.send(alarmEmail);
		} else
		{
			log.info("Supressing critical error alarms for app : " + appId);
		}
	}

	public boolean suppressAlarm(Long appId)
	{
		//Check to see if alarm should be suppressed.
		
		return false;

	}

}
