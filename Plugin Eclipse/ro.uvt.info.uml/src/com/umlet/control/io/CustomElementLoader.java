/*
 * Created on 10.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.control.io;

import com.umlet.element.base.Entity;
import java.io.*;
import com.sun.tools.javac.*; //java compiler
import com.umlet.control.*;
/**
 * @author Ludwig
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CustomElementLoader {
	private static CustomElementLoader _instance;
	ClassLoader loader;
	private static int errorCounter=0;
	private int compilerReturnCode = 0;

	public static CustomElementLoader getInstance() {
		if (_instance==null) {
			_instance=new CustomElementLoader();
		}
		return _instance;
	}
	public CustomElementLoader() {
		errorCounter=0;
	}
	
	public Entity doLoadClass(String[] fileNameStrArr, boolean cleanBuild, boolean silent) {
		Entity entity = null;
		//System.out.println("fileNameStrArr:"+fileNameStrArr[0]+","+fileNameStrArr[1]);
		try {
			//int compilerReturnCode=0;
			String typeName = fileNameStrArr[1];
			String filePath_woExtension = fileNameStrArr[0].substring(0,fileNameStrArr[0].indexOf(".java"));
			File classFile = new File(filePath_woExtension+".class");
			StringWriter compilerErrorMessageSW = new StringWriter();
			if((cleanBuild)||(!classFile.exists())) {
				//System.out.println("CLEAN BUILD");
				compilerErrorMessageSW = new StringWriter(); //catch compiler messages
				PrintWriter compilerErrorMessagePW = new PrintWriter(compilerErrorMessageSW);			
				String sourceFilePath=filePath_woExtension+".java";
				compilerReturnCode = Main.compile(new String[] {"-classpath",Umlet.getInstance().getHomePath()+"umlet.jar",sourceFilePath},compilerErrorMessagePW);
				//compilerReturnCode = Main.compile(new String[] {sourceFilePath},compilerErrorMessagePW);
			}// else System.out.println("JUST LOAD CLASS:"+typeName);
			if(compilerReturnCode==0) {
				loader = new FileClassLoader(filePath_woExtension+".class"); //filename of the class-file
				Class c = loader.loadClass(typeName); //load class by type name
				entity = (Entity)c.newInstance();
				entity.loadJavaSource(filePath_woExtension+".java"); //load the java source into the entity
			} else {
				System.err.println("Compiler returned:"+compilerReturnCode);
				if(!silent) Umlet.getInstance().showCompilerMessage(compilerErrorMessageSW.toString());
				else {
					//count errors
					errorCounter++;
				}
			}
		}
		catch (Exception e) {
			Umlet.getInstance().showCompilerMessage(e.getMessage());
			e.printStackTrace();
			
		}
		return entity;
	}
	
	public int getErrorCounter() {
		return errorCounter;
	}
	
	public void resetErrorCounter() {
		errorCounter=0;
	}
	
	public int getCompilerReturnCode() {
		return compilerReturnCode;
	}
}
