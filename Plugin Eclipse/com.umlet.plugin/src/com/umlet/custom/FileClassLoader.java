package com.umlet.custom;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.umlet.control.Umlet;

public class FileClassLoader extends ClassLoader {
	
	public FileClassLoader() {
		super();
	}
	
	public FileClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		Class c = null;
		try {
			byte data[]=loadClassData(className);
			c=defineClass(className,data,0,data.length);
			if(c==null) throw new ClassNotFoundException(className);
		} catch(IOException e) {
			throw new ClassNotFoundException(className);
		}
		return c;
	}

	private byte[] loadClassData(String className) throws IOException {
		File f = new File(Umlet.getInstance().getHomePath() + Umlet.getCustomElementPath() + "tmp/" + className + ".class");
		byte buff[] = new byte[(int)f.length()];
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream (fis);
		dis.readFully(buff);
		dis.close();
		return buff;
	}
}
