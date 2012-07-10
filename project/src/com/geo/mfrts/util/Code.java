package com.geo.mfrts.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.geo.mfrts.objects.Thing;

public class Code {
	private static int num = 0;
	
	private static String createClass(String className, String method) {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.geo.mfrts.objects;\n");
		sb.append("public class " + className + " extends Thing {\n");
		sb.append(method);
		String date = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date());
		sb.append("public String toString() {return \"" + date + " \" + this.location.v3;}");
		sb.append("}");
		System.out.println(sb.toString());
		return sb.toString();
	}
	private static boolean compile(JavaFileObject... source) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		String classPath = System.getProperty( "java.class.path", "" ).split(":")[0];
		try {
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File(classPath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JavaCompiler.CompilationTask task = compiler.getTask(null,fileManager,null,null,null,Arrays.asList(source));
		return task.call();
	}
	
	private static String code(String className, String method) {
		String name = null;
		boolean stat = false;
		String classCode = createClass(className,method);
		try {
			RAMObject o = new RAMObject(className,classCode);
			stat = compile(o);
			name = "com.geo.mfrts.objects."+className;
			System.out.println("Compile\t" + stat + "\t"+name);
		}
		catch(Exception e) {
		}
		if (stat==false) {
			name = "com.geo.mfrts.objects.Thing";
		}
		return name;
		
	}
	public static Thing newThing(String code, float x, float y) {
		String objName = code("Dynamic",code);
		Thing thing = new Thing(x,y);
		try {
			/*ClassLoader cl = new Reloader();
			Class<?> cls = cl.loadClass(objName);
			thing = (Thing)cls.newInstance();*/
			Object o = new Reloader().loadClass(objName).newInstance();
			thing = (Thing)o;
			
			thing.init(x,y);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				System.out.println( "Your CWD is " + new File( "." ).getCanonicalPath() );
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            System.out.println( "Your classpath is " + System.getProperty( "java.class.path", "" ) );

		}
		return thing;
	}
	
	static class RAMObject extends SimpleJavaFileObject {
		private String program;
		public RAMObject(String className, String program) throws URISyntaxException {
			super(new URI(className+".java"), Kind.SOURCE);
			this.program = program;
		}
		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			return program;
		}
		public String toString() {
			return program;
		}
	}
	static class Reloader extends ClassLoader {
		@Override 
		public Class<?> loadClass(String s) throws ClassNotFoundException {
			if (!s.equals("com.geo.mfrts.objects.Dynamic")) {
				return super.loadClass(s); // need to parameterize the dynamic class here
			}
			try {
				System.out.println("Loading class:" +s);
				byte[] bytes = loadClassData(s);
				return defineClass(s,bytes, 0, bytes.length);
			}
			catch(IOException e) {
				System.out.println("Fallback");
				return super.loadClass(s);
			}
		}	 
		private byte[] loadClassData(String className) throws IOException {
			System.out.println("RELOADING");
			String s = className.replaceAll("\\.",Character.toString(File.separatorChar)) + ".class";
			s = "./bin/" + s;
			System.out.println(s);
			File f = new File(s);
			byte b[] = new byte[(int)f.length()];
			DataInputStream d = new DataInputStream(new FileInputStream(f));
			d.readFully(b);
			d.close();
			return b;
		}
	}
}
