package com.apigee.alarm;

import java.io.Serializable;
import java.util.Date;

public class DeviceLogEntry implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * This is the primary key for a log record
	 */
	private Long id;

	private String sessionId;

	/**
	 * This relates to applicaitonId from {@link CompositeApplicationConfigurationModel}
	 */
	private Long appId;

	private String applicationVersion;



	String networkType;
	String networkCountry;
	String networkCarrier;
	Boolean isNetworkRoaming;  

	//DeviceParameters
	String deviceId;
	String deviceType;
	String deviceModel;
	String devicePlatform;
	String deviceOperatingSystem;

	Float bearing;
	Double latitude;
	Double longitude;


	public static final int ASSERT = 7;
	public static final int ERROR = 6;
	public static final int WARN = 5;
	public static final int INFO = 4;
	public static final int DEBUG = 3;
	public static final int VERBOSE = 2;

	private Date timeStamp;

	private Integer logLevel;

	private String logMessage;

	public String tag;   

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}



	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}


	public Date getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;



	}


	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}


	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "[" + timeStamp.toString() + "][" + tag + "]" 
				+ "[" + devicePlatform + "]"
				+ "[" + deviceOperatingSystem + "]"
				+ "[" + deviceModel + "]"
				+ "[" + deviceId + "]"
				+   logMessage  ;
	}
	public String getApplicationVersion()
	{
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion)
	{
		this.applicationVersion = applicationVersion;
	}

	public String getNetworkCountry()
	{
		return networkCountry;
	}

	public void setNetworkCountry(String networkCountry)
	{
		this.networkCountry = networkCountry;
	}

	public String getNetworkCarrier()
	{
		return networkCarrier;
	}

	public void setNetworkCarrier(String networkCarrier)
	{
		this.networkCarrier = networkCarrier;
	}

	public Boolean getIsNetworkRoaming()
	{
		return isNetworkRoaming;
	}

	public void setIsNetworkRoaming(Boolean isNetworkRoaming)
	{
		this.isNetworkRoaming = isNetworkRoaming;
	}


	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getDeviceModel()
	{
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel)
	{
		this.deviceModel = deviceModel;
	}

	public String getDevicePlatform()
	{
		return devicePlatform;
	}

	public void setDevicePlatform(String devicePlatform)
	{
		this.devicePlatform = devicePlatform;
	}

	public String getDeviceOperatingSystem()
	{
		return deviceOperatingSystem;
	}

	public void setDeviceOperatingSystem(String deviceOperatingSystem)
	{
		this.deviceOperatingSystem = deviceOperatingSystem;
	}


	public Float getBearing()
	{
		return bearing;
	}

	public void setBearing(Float bearing)
	{
		this.bearing = bearing;
	}

	public Double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}  


	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	




}
