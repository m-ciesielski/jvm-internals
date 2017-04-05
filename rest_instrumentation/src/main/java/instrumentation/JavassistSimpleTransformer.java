package instrumentation;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class JavassistSimpleTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] bytes) throws IllegalClassFormatException {

		byte[] result = bytes;

		if(className != null && className.contains("rest_api")){
			try {
				String dotClassName = className.replace('/', '.');
				ClassPool cp = ClassPool.getDefault();
				CtClass ctClazz = cp.get(dotClassName);

				Class measureAnnotation = Class.forName("rest_api.annotations.Measure");
				for (CtMethod method : ctClazz.getDeclaredMethods()){
					if (method.getAnnotation(measureAnnotation) != null){
						System.out.println("Transforming " + ctClazz.getName());
						method.addLocalVariable("elapsedTime", CtClass.longType);
						method.insertBefore("elapsedTime = System.currentTimeMillis();");
						method.insertAfter(" { elapsedTime = System.currentTimeMillis() - elapsedTime; " +
								"System.out.println(\"" + method.getName() + " elapsedTime = \" + elapsedTime);}");
						method.insertAfter("{ System.out.println(\"Params:\"); for (int i=0; i< $args.length; ++i) { System.out.println($args[i]); } }");
					}
				}
				result = ctClazz.toBytecode();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}

		}


		return result;
	}

}
