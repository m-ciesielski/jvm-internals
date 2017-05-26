package instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class JavassistTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] bytes) throws IllegalClassFormatException {

        byte[] result = bytes;

        if(className != null && className.contains("dummy")){
            try {
                String dotClassName = className.replace('/', '.');
                ClassPool cp = ClassPool.getDefault();
                CtClass ctClazz = cp.get(dotClassName);

                for (CtMethod method : ctClazz.getDeclaredMethods()){
                    //System.out.println("Transforming " + ctClazz.getName());
                    //method.addLocalVariable("bla", CtClass.longType);
                    method.insertBefore("System.out.println(\"Javassist Method called " + method.getName() + " \");");
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

