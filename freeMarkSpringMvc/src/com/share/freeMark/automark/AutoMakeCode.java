package com.share.freeMark.automark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.share.freeMark.util.AttrUtil;
import com.share.freeMark.util.ModelBean;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AutoMakeCode {

	public static void main(String[] args) {
		//
		 // 1.创建实体类对象
		 // 2.调用generalAll(对象，数据库主键，与数据库主键对应属性)
		 // 
//		HyAccount bo = new HyAccount();
//		 generalAll(bo,"account_id","accountId");
//		ConstructionApprovalRecord sr = new ConstructionApprovalRecord();
//		generalAll(sr,"ar_id","arId");
		
	}
	
	/***
	 * 创建Bean对象
	 * @param o      操作对象bjStr
	 */
	public static void makeModelBean(String objStr){
		try {
			ModelBean jmb= JSON.parseObject(objStr, ModelBean.class);
			String beanFullName=jmb.getPackageName()+"."+jmb.getModelName();
			
			Configuration cfg = new Configuration();
			String relativelyPath=System.getProperty("user.dir");
//			String path = AutoTest.class.getResource("/").getPath()+"ftl";
			String path = relativelyPath+"/src/ftl";
			try {
				
				Map<String,Object> root = new HashMap<String, Object>();
				
				root.put("packageName", jmb.getPackageName());
				root.put("modelName", jmb.getModelName());
				root.put("modelNote", jmb.getModelNote());
				root.put("dbPid", jmb.getDbPid());
				root.put("pid", jmb.getPid());
				root.put("propertys", jmb.getPropertys());
				
				
				cfg.setDirectoryForTemplateLoading(new File(path));
				Template template = cfg.getTemplate("/model.ftl");
				StringWriter out = new StringWriter();
				template.process(root, out);
				
				System.out.println(out.toString());
				String beanPathName=	relativelyPath +"/src/"+beanFullName.replace(".", "/")+".java";
				writeFile(beanPathName,out.toString());
			} catch (IOException e) {
				System.out.println("Cause==>" + e.getCause());
			} catch (TemplateException e) {
				System.out.println("Cause==>" + e.getCause());
			}
		} catch (Exception e) {
		}
	}
	
	/******
	 * 根据类全名获取类实例
	 * @param classFullName
	 * @return
	 */
	public static Object getBeanInstance(String classFullName){
		Object o=null;
		try {
			Class clazz = Class.forName(classFullName);//根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名
		    o=clazz.newInstance();
		   System.out.println(getClassName(o));
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	/****
	 *生成所有
	 * @param o      操作对象
	 * @param dbPid  数据库主键字段
	 * @param pid    对象主键
	 * @param makeType    创建类型: 1 全部生成，2仅生成后端，3仅生成前端
	 * @param parentPath    前端生成路径 例如：modules/system/test 
	 */
	public static void generalAll(Object o,String dbPid,String pid,Integer makeType,String... parentPath){
		/*********************后端服务代码创建************************/
		if(makeType==1 ||makeType ==2){
		makeXmlMapper(o,dbPid,pid);
		makeJavaMapper(o,pid);
		makeIService(o,pid);
		makeServiceImpl(o,pid);
		makeController(o,pid,parentPath);
		}
		
/**********************前端页面和js代码*********************************/	
		if(makeType ==1 ||makeType ==3){
		makeAddjs(o,pid,parentPath);
		makeAddjsp(o,pid,parentPath);
		makeDetailjs(o,pid,parentPath);
		makeDetailjsp(o,pid,parentPath);
		makeEditjs(o,pid,parentPath);
		makeEditjsp(o,pid,parentPath);
		makeListjs(o,pid,parentPath);
		makeListjsp(o,pid,parentPath);
		}
	}
	
	
	/************
	 * 列表jsp实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/jsp/,无此参数则默认modules/system/模块/jsp/
	 */
	public static void makeListjsp(Object o,String pid,String... parentPath ){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		}
		
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String listJSPName="list_"+lcfoSimpleclassName;
		root.put("listJSPName", listJSPName);
		
		String listJSName="list_"+lcfoSimpleclassName;
		root.put("listJSName", listJSName);
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/ListJSP.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName="";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/jsp/"+listJSPName+".jsp";
			}else{
				javaPathName= relativelyPath +"/WebContent/"+parentPackageName+"/jsp/"+listJSPName+".jsp";
			}
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/****
	 * 列表js实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/js/,无此参数则默认modules/system/模块/js/
	 */
	public static void makeListjs(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		}
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String listJSName="list_"+lcfoSimpleclassName;
		root.put("listJSName", listJSName);
		root.put("pidType", getFiledType(o,pid));
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/ListJS.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName="";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/js/"+listJSName+".js";
			}else{
				javaPathName=	relativelyPath +"/WebContent/"+parentPackageName+"/js/"+listJSName+".js";
			}
			
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/************
	 * 编辑jsp实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/jsp/,无此参数则默认modules/system/模块/jsp/
	 */
	public static void makeEditjsp(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		}
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String editJSPName="edit_"+lcfoSimpleclassName;
		root.put("editJSPName", editJSPName);
		
		String editJSName="edit_"+lcfoSimpleclassName;
		root.put("editJSName", editJSName);
		root.put("pidType", getFiledType(o,pid));
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/EditJSP.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName="";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/jsp/"+editJSPName+".jsp";
			}else{
				javaPathName=	relativelyPath +"/WebContent/"+parentPackageName+"/jsp/"+editJSPName+".jsp";
			}
			
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/****
	 * 编辑js实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/js/,无此参数则默认modules/system/模块/js/
	 */
	public static void makeEditjs(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		}
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String editJSName="edit_"+lcfoSimpleclassName;
		root.put("editJSName", editJSName);
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/EditJS.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName= "";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/js/"+editJSName+".js";
			}else{
				javaPathName=	relativelyPath +"/WebContent/"+parentPackageName+"/js/"+editJSName+".js";
			}
			
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/************
	 * 详情jsp实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/jsp/,无此参数则默认modules/system/模块/jsp/
	 */
	public static void makeDetailjsp(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		};
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String detailJSPName="detail_"+lcfoSimpleclassName;
		root.put("detailJSPName", detailJSPName);
		
		String detailJSName="detail_"+lcfoSimpleclassName;
		root.put("detailJSName", detailJSName);

		root.put("pidType", getFiledType(o,pid));
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/DetailJSP.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName= "";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/jsp/"+detailJSPName+".jsp";
			}else{
				javaPathName=	relativelyPath +"/WebContent/"+parentPackageName+"/jsp/"+detailJSPName+".jsp";
			}
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	
	/****
	 * 详情js实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/js/,无此参数则默认modules/system/模块/js/
	 */
	public static void makeDetailjs(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		}
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String detailJSName="detail_"+lcfoSimpleclassName;
		root.put("detailJSName", detailJSName);
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/DetailJS.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName= "";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/js/"+detailJSName+".js";
			}else{
				javaPathName=	relativelyPath +"/WebContent/"+parentPackageName+"/js/"+detailJSName+".js";
			}
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	
	/************
	 * 新增jsp实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/jsp/,无此参数则默认modules/system/模块/jsp/
	 */
	public static void makeAddjsp(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		}
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String addJSPName="add_"+lcfoSimpleclassName;
		root.put("addJSPName", addJSPName);
		
		String addJSName="add_"+lcfoSimpleclassName;
		root.put("addJSName", addJSName);
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/AddJSP.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName= "";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/jsp/"+addJSPName+".jsp";
			}else{
				javaPathName=	relativelyPath +"/WebContent/"+parentPackageName+"/jsp/"+addJSPName+".jsp";
			}
			
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/****
	 * 新增js实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 * @param parentPath 可选 ,格式：modules/system/testinfo/js/,无此参数则默认modules/system/模块/js/
	 */
	public static void makeAddjs(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		//服务实现接口
		String packagePath =getClassPackage(o);
		if(packagePath.endsWith("model")){//以model结尾，取上级目录结构
			packagePath = packagePath.replace(".model","");
		}else{//否则，取当前目录
			//packagePath =
		}
		
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		
		
		String lcfoSimpleclassName=toLowerCaseFirstOne(simpleclassName);
		
		String addJSName="add_"+lcfoSimpleclassName;
		root.put("addJSName", addJSName);
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/AddJS.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName= "";
			//根据参数路径动态设置
			if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
				javaPathName= relativelyPath +"/WebContent/"+parentPath[0]+"/js/"+addJSName+".js";
			}else{
				javaPathName=	relativelyPath +"/WebContent/"+parentPackageName+"/js/"+addJSName+".js";
			}
			
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/*********************后端服务代码创建************************/	
	/***********
	 * 控制器方法实现
	 * @param o      操作对象
	 * @param pid    对象主键
	 */
	public static void makeController(Object o,String pid,String... parentPath){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);

		String iServiceName=getClassPackage(o).replace("model", "services")+".I"+simpleclassName+"Service";
		String simpleIServiceName="I"+simpleclassName+"Service";

		
		//服务实现接口
		String packagePath = getClassPackage(o).replace("model", "services")+".impl";
		String serviceImplName=packagePath+"."+simpleclassName+"ServiceImpl";
		String simpleServiceImplName=simpleclassName+"ServiceImpl";
		
		//控制器
		packagePath = getClassPackage(o).replace("model", "controller");
		String controllerName = packagePath+"."+simpleclassName+"Controller";
		String simpleControllerName=simpleclassName+"Controller";
		
//		String dbPid="id";
//		String pid="cid";
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);

		root.put("iServiceName", iServiceName);
		root.put("simpleIServiceName", simpleServiceImplName);
		
		root.put("serviceImplName", serviceImplName);
		root.put("simpleServiceImplName", simpleServiceImplName);
		
		root.put("controllerName", controllerName);
		root.put("simpleControllerName", simpleControllerName);
		root.put("pidType", getFiledType(o,pid));
		
//		String parentPackageName =packagePath.substring(packagePath.lastIndexOf(".")+1);
		String parentPackageName ="";
		if(parentPath !=null &&parentPath.length >0 && parentPath[0]!=null && parentPath[0] !="" ){
			parentPackageName = parentPath[0];
		}else{
			parentPackageName =	packagePath.replace("com.web.", "").replace(".controller", "").replace(".", "/");
		}
		root.put("parentPackageName", parentPackageName);
		
		root.put("pid", pid);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/Controller.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName=	relativelyPath +"/src/"+controllerName.replace(".", "/")+".java";
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/*****
	 * 服务实现生成
	 * @param o      操作对象
	 * @param pid    对象主键
	 */
	public static void makeServiceImpl(Object o,String pid){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		String packagePath = getClassPackage(o).replace("model", "services")+".impl";
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		String serviceImplName=packagePath+"."+simpleclassName+"ServiceImpl";
		String simpleServiceImplName=simpleclassName+"ServiceImpl";

		String iServiceName=getClassPackage(o).replace("model", "services")+".I"+simpleclassName+"Service";
		String simpleIServiceName="I"+simpleclassName+"Service";

		String javaMapperName=className.replace("model", "dao.mysql.mybatis")+"Mapper";
		String simpleMapperName=simpleclassName+"Mapper";
		
//		String dbPid="id";
//		String pid="cid";
		
		
		root.put("packagePath", packagePath);
		root.put("className", className);
		root.put("simpleclassName", simpleclassName);
		
		root.put("iServiceName", iServiceName);
		root.put("simpleIServiceName", simpleIServiceName);
		
		root.put("serviceImplName", serviceImplName);
		root.put("simpleServiceImplName", simpleServiceImplName);
		
		root.put("javaMapperName", javaMapperName);
		root.put("simpleMapperName", simpleMapperName);
		root.put("pid", pid);
		root.put("pidType", getFiledType(o,pid));
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		root.put("attrs", attrs);
		
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/ServiceImpl.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName=	relativelyPath +"/src/"+serviceImplName.replace(".", "/")+".java";
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	
	/****
	 * 生成服务接口
	 * @param o      操作对象
	 * @param pid    对象主键
	 */
	public static void makeIService(Object o,String pid){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		String packagePath = getClassPackage(o).replace("model", "services");
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		String iServiceName=packagePath+".I"+simpleclassName+"Service";
		String simpleIServiceName="I"+simpleclassName+"Service";
//		String dbPid="id";
//		String pid="cid";
		
		root.put("className", className);
		root.put("packagePath", packagePath);
		root.put("simpleclassName", simpleclassName);
		root.put("simpleIServiceName", simpleIServiceName);
		root.put("pid", pid);
		root.put("pidType", getFiledType(o,pid));
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/IService.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName=	relativelyPath +"/src/"+iServiceName.replace(".", "/")+".java";
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	/****
	 * javaMapper生成
	 * @param o      操作对象
	 * @param pid    对象主键
	 */
	public static void makeJavaMapper(Object o,String pid){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		
		
		
		Map<String,Object> root = new HashMap<String, Object>();
		String packagePath = getClassPackage(o).replace("model", "dao.mysql.mybatis");
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		String javaMapperName=className.replace("model", "dao.mysql.mybatis")+"Mapper";
		String simpleMapperName=simpleclassName+"Mapper";
//		String dbPid="id";
//		String pid="cid";
		
		root.put("className", className);
		root.put("packagePath", packagePath);
		root.put("simpleclassName", simpleclassName);
		root.put("simpleMapperName", simpleMapperName);
		root.put("pid", pid);
		root.put("pidType", getFiledType(o,pid));
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/javaMapper.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String javaPathName=	relativelyPath +"/src/"+javaMapperName.replace(".", "/")+".java";
			writeFile(javaPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
	}
	
	
	/****
	 * xml 文件生成
	 * @param o      操作对象
	 * @param dbPid
	 * @param pid    对象主键
	 */
	public static void makeXmlMapper(Object o,String dbPid,String pid){
		System.out.println(getClassName(o));
		String className=getClassName(o);
		System.out.println(className.substring(className.lastIndexOf(".")+1));
		System.out.println(camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1));
		String simpleclassName=className.substring(className.lastIndexOf(".")+1);
		
		List<Object> attrs = new ArrayList<Object>();
		List<Map> pm= getFiledsInfo(o);
		for(Map item:pm){
			if(!"serialVersionUID".equals(item.get("name").toString())){
				attrs.add(new AttrUtil(item.get("name").toString(), item.get("type").toString(),camelToUnderline(item.get("name").toString())));
			}
		}
		
		Map<String,Object> root = new HashMap<String, Object>();
		String xmlMapperName=className.replace("model", "dao.mysql.mybatis")+"Mapper";
		String tableName= camelToUnderline(className.substring(className.lastIndexOf(".")+1)).substring(1);
//		String dbPid="id";
//		String pid="cid";
		
		root.put("mapperName", xmlMapperName);
		root.put("modelName", className);
		root.put("simpleclassName", simpleclassName);
		root.put("attrs", attrs);
		root.put("dbPid", dbPid);
		root.put("pid", pid);
		root.put("tableName", tableName);
		root.put("pidType", getFiledType(o,pid));
		
		Configuration cfg = new Configuration();
		String relativelyPath=System.getProperty("user.dir");
//		String path = AutoTest.class.getResource("/").getPath()+"ftl";
		String path = relativelyPath+"/src/ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			Template template = cfg.getTemplate("/xmlMapper.ftl");
			StringWriter out = new StringWriter();
			template.process(root, out);
			
			System.out.println(out.toString());
			String xmlPathName=	relativelyPath +"/src/"+xmlMapperName.replace(".", "/")+".xml";
			writeFile(xmlPathName,out.toString());
		} catch (IOException e) {
			System.out.println("Cause==>" + e.getCause());
		} catch (TemplateException e) {
			System.out.println("Cause==>" + e.getCause());
		}
		
	}
	
	/****
	 * 获取对象包路径
	 * @param o      操作对象
	 * @return
	 */
	public static String getClassPackage(Object o){
		return o.getClass().getPackage().getName();
	}
	/******
	 * 获取类名称
	 * @param o      操作对象
	 * @return com.test.model.HyBusiness
	 */
	public static String getClassName(Object o){
		return o.getClass().getName();
	}
	
	/***
	 * 下划线"_"
	 */
	public static final char UNDERLINE='_';  
	/***
	 * 驼峰规则转下划线
	 * @param param 驼峰写法字符串
	 */
	public static String camelToUnderline(String param){  
	       if (param==null||"".equals(param.trim())){  
	           return "";  
	       }  
	       int len=param.length();  
	       StringBuilder sb=new StringBuilder(len);  
	       for (int i = 0; i < len; i++) {  
	           char c=param.charAt(i);  
	           if (Character.isUpperCase(c)){  
	               sb.append(UNDERLINE);  
	               sb.append(Character.toLowerCase(c));  
	           }else{  
	               sb.append(c);  
	           }  
	       }  
	       return sb.toString();  
	   } 

	/****
	 * 获取对象属性和属性类型
	 * @param o      操作对象
	 * @return List<Map> 对象属性和属性类型集合
	 */
	public static List getFiledsInfo(Object o){  
    Field[] fields=o.getClass().getDeclaredFields();  
        String[] fieldNames=new String[fields.length];  
        List list = new ArrayList();  
        Map infoMap=null;  
        String typeString = null;
    for(int i=0;i<fields.length;i++){  
        infoMap = new HashMap();  
        typeString =fields[i].getType().toString();
        infoMap.put("type", typeString.indexOf(".")<0? typeString:typeString.substring(typeString.lastIndexOf(".")+1));  
        infoMap.put("name", fields[i].getName());  
//        infoMap.put("value", getFieldValueByName(fields[i].getName(), o));  
        list.add(infoMap);  
    }  
    return list;  
   }
   
	/*****
	 * 文件创建写入
	 * @param fileName 文件名
	 * @param content  文件内容
	 */
	public static void writeFile(String fileName,String content){
		FileWriter fw =null;
		BufferedWriter bw =null;
		try{
			File file = new File(fileName);
			//路径
			File fileDir = file.getParentFile();
			if  (!fileDir .exists()  && !fileDir .isDirectory())      
			{       
			    System.out.println("//路径不存在，进行创建");  
			    fileDir.mkdirs();    
			}
		   // if file doesnt exists, then create it
		   if (!file.exists()) {
		    file.createNewFile();
		   }

		   fw = new FileWriter(file.getAbsoluteFile());
		   bw = new BufferedWriter(fw);
		   bw.write(content);
		   bw.close();

		   System.out.println("Done");

		  } catch (IOException e) {
		   e.printStackTrace();
		 }finally{
			 try {
				fw.close();
				 bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}
	
	/****
	 * 字符串首字母小写转换
	 * @param name 待转换字符串
	 * @return String 转换后字符串
	 */
	public static String toLowerCaseFirstOne(String name) {
        char[] cs=name.toCharArray();
        cs[0] +=32;
        return String.valueOf(cs);
        
    }
	
	/***
	 * 获取对象指定字段的类型
	 * @param o  对象
	 * @param filedName 字段
	 * @return  字段类型
	 */
	public static String getFiledType(Object o,String filedName){
		String filedType ="";
		List<Map<String,String>> l = getFiledsInfo(o);
		for(Map<String,String> item:l){
			if(item.get("name").equals(filedName)){
				filedType = item.get("type");
				break;
			}
		}
		return filedType;
	}
	
}
