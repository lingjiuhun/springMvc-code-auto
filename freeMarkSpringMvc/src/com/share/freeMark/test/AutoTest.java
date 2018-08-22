package com.share.freeMark.test;

import com.mybatisTool.util.MybatisUtil;
import com.share.freeMark.automark.AutoMakeCode;

public class AutoTest {

	public static void main(String[] args) {
		//生成bean对象
		MybatisUtil.autoMakeBean("172.21.24.6:3306/hiaerrobot_device","root","Www#1234",
				"com.hollysys.haier.robot.device.entity", "Device", "device");
		MybatisUtil.autoMakeBean("172.21.24.6:3306/hiaerrobot_device","root","Www#1234",
				"com.hollysys.haier.robot.device.entity", "DeviceAuth", "device_auth");
		MybatisUtil.autoMakeBean("172.21.24.6:3306/hiaerrobot_device","root","Www#1234",
				"com.hollysys.haier.robot.device.entity", "DeviceRepo", "device_repo");
		MybatisUtil.autoMakeBean("172.21.24.6:3306/hiaerrobot_device","root","Www#1234",
				"com.hollysys.haier.robot.device.entity", "DeviceType", "device_type");

//		Object o = new com.hollysys.haier.robot.device.entity.Device();
//		//生成xml
//		AutoMakeCode.makeXmlMapper(o, "id", "id");
//		//生成map
//		AutoMakeCode.makeJavaMapper(o,  "id");
//		
//		o = new com.hollysys.haier.robot.device.entity.DeviceAuth();
//		//生成xml
//		AutoMakeCode.makeXmlMapper(o, "id", "id");
//		//生成map
//		AutoMakeCode.makeJavaMapper(o,  "id");
//		
//		o = new com.hollysys.haier.robot.device.entity.DeviceRepo();
//		//生成xml
//		AutoMakeCode.makeXmlMapper(o, "id", "id");
//		//生成map
//		AutoMakeCode.makeJavaMapper(o,  "id");
//		
//		o = new com.hollysys.haier.robot.device.entity.DeviceType();
//		//生成xml
//		AutoMakeCode.makeXmlMapper(o, "id", "id");
//		//生成map
//		AutoMakeCode.makeJavaMapper(o,  "id");
		
		
	}
	
}
