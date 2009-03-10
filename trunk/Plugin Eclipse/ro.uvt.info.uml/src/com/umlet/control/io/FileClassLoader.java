/*
 * Created on 10.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.control.io;

import java.io.*;

import com.umlet.control.Umlet;
/**
 * @author Ludwig
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileClassLoader extends ClassLoader {
	private String classFilePath; //fully qualified path to the class file (com/umlet/element/base/Note.class)

	public FileClassLoader(String path) {
		if(path!=null) this.classFilePath=path;
		else throw new IllegalArgumentException("UMLet -> FileClassLoader -> FileClassLoader(): path must not be null");
	}

	protected Class loadClass(String className,boolean resolveClassName) throws ClassNotFoundException {
		Class c=null;
		if(!classFilePath.endsWith(className+".class")) {
			if(className.indexOf("umlet")<0)
				c=findSystemClass(className); //get system-classes like java.lang.Object
		}
		if(c==null) {
			c = findLoadedClass(className); //cache
		}
		if(c==null && !classFilePath.endsWith(className+".class")) {
			c=java.lang.Class.forName(className); //get project classes like com.umlet.element.base.Entity 
		}
	    if(c == null) {
			try {
				byte data[]=loadClassData();
				c=defineClass(className,data,0,data.length);
				if(c==null) throw new ClassNotFoundException(className);
			} catch(IOException e) {
				throw new ClassNotFoundException("Error reading file: "+className+".class");
			}
		}
		if(resolveClassName) resolveClass(c);
		return c;
	}

	private byte[] loadClassData() throws IOException {
		File f = new File(classFilePath);
		byte buff[] = new byte[(int)f.length()];
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream (fis);
		dis.readFully(buff);
		dis.close();
		return buff;
	}
}
