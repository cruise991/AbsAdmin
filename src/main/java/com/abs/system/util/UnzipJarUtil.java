package com.abs.system.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UnzipJarUtil {

	public static void main(String[] args) throws IOException {

		unzipJar("C:\\Users\\wdg\\Desktop\\yun", "C:\\Users\\wdg\\Desktop\\yun.jar");

	}

	public static void unzipJar(String destinationDir, String jarPath) throws IOException {
		File file = new File(jarPath);
		JarFile jar = new JarFile(file);
		//创建文件夹以及路径
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			
		     JarEntry entry = (JarEntry) enums.nextElement();
		     String fileName = destinationDir + File.separator + entry.getName();
		     fileName= fileName.substring(0, fileName.lastIndexOf("/"));		    
		     File f = new File(fileName);
		     if (!f.exists()) {
		           f.mkdirs();
		     }
		}
		//将文件夹中的文件 copy到新建的文件夹中
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
		     JarEntry entry = (JarEntry) enums.nextElement();
		     String fileName = destinationDir + File.separator + entry.getName();
		     File f = new File(fileName);
		     if (!fileName.endsWith("/")) {
		         InputStream is = jar.getInputStream(entry);
		         FileOutputStream fos = new FileOutputStream(f);
		// write contents of 'is' to 'fos'
		         while (is.available() > 0) {
		             fos.write(is.read());
		         }
		         fos.close();
		         is.close();
		      }
		}
	}
}
