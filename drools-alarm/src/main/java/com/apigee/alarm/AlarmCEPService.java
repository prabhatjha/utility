package com.apigee.alarm;


import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.conf.MBeansOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionClock;

public class AlarmCEPService {


	private static final String[] ASSET_FILES = {
		"/rules/DeviceLogErrors.drl"
	};

	private static final Log log = LogFactory.getLog(AlarmCEPService.class);
	

	private KnowledgeBase kbase;
	ClockTypeOption clockTypeOption;

	public AlarmCEPService(ClockTypeOption clockTypeOption)
	{
		this.clockTypeOption = clockTypeOption;
		kbase = loadRuleBase();
	}


	public void processEvents(Long appId, List<DeviceLogEntry> logs)
	{
		StatefulKnowledgeSession session = createSession(clockTypeOption);
		processEvents(appId, logs,session);
	}

	public void processEvents(Long appId, List<DeviceLogEntry> logs, StatefulKnowledgeSession session)
	{
		WorkingMemoryEntryPoint clientLogStream;	
		clientLogStream = session.getWorkingMemoryEntryPoint("clientLogStream");
		
		try {

			session.setGlobal("alarmService", NotificationService.getInstance());
			session.setGlobal("appId", appId);
			Date currentTimeStamp;

			SessionClock clock = session.getSessionClock();
			currentTimeStamp = new Date(clock.getCurrentTime());

			log.info("Inserting facts at time : " + currentTimeStamp);

			long logCount = 0;			

			for(DeviceLogEntry log : logs)
			{	
				if (clientLogStream != null) //this could be null if there is no rule file using this stream I think
				{
					clientLogStream.insert(log);
					logCount++;
				}
			}
			log.info("Inserted ClientLog Count : " + logCount);
			session.fireAllRules();
			log.info("Finished Processing events : ");
			session.dispose();		
		} catch ( Exception e ) {
			log.error("Error in complex event processing of App Id " + appId + " ", e);

		}


	}


	public StatefulKnowledgeSession createSession()
	{
		return createSession(clockTypeOption);
	}

	private StatefulKnowledgeSession createSession(ClockTypeOption clockTypeOption) {
		KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
		conf.setOption( clockTypeOption );
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession(conf,null);
		//Added a logger
		session.setGlobal( "log",  LogFactory.getLog("RulesEngine"));
		log.info("Finishing session creation");
		return session;
	}


	private KnowledgeBase loadRuleBase() {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		try {
			for( int i = 0; i < ASSET_FILES.length; i++ ) {
				builder.add( ResourceFactory.newInputStreamResource( AlarmCEPService.class.getResourceAsStream( ASSET_FILES[i] ) ),
						ResourceType.determineResourceType( ASSET_FILES[i] ));
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			System.exit( 0 );
		}
		if( builder.hasErrors() ) {
			System.err.println(builder.getErrors());
			System.exit( 0 );
		}
		KnowledgeBaseConfiguration conf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		conf.setOption( EventProcessingOption.CLOUD );
		conf.setOption( MBeansOption.ENABLED );
		//conf.setOption( MultithreadEvaluationOption.YES );
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase( "Traffic Detection", conf ); 
		kbase.addKnowledgePackages( builder.getKnowledgePackages() );
		return kbase;
	}

}
