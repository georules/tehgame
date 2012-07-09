package com.geo.mfrts.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

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
		sb.append("}");
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
			System.out.println(o);
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
		String objName = code("Dynamic"+num++,code);
		Thing thing = new Thing(x,y);
		try {
			//Constructor c = Thing.class.getConstructor(new Class[]{Float.TYPE,Float.TYPE});
			thing = (Thing) Class.forName(objName).newInstance();
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
			System.out.println("====RAMOBJECT====\n" + program);
			System.out.println("====RAMOBJECT====");
		}
		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			return program;
		}
		public String toString() {
			return program;
		}
		
	}
}
