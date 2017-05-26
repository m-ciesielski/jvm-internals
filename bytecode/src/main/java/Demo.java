import dummy.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Demo {

    String policz1(){
        String napis = "";
        napis += "Hello";
        napis += "World";
        napis += "!";
        return napis;
    }

    String policz2(){
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append("World");
        sb.append("!");
        return sb.toString();
    }

    void invokeAllDummyMethods() throws ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException,
            InstantiationException, InvocationTargetException {

        long elapsedTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            //System.out.println(String.format("Invoking sum for dummy.DummyClass%d", i));
            Class c = Class.forName(String.format("dummy.DummyClass%d", i));
            Method m = c.getMethod("sum", int.class, int.class);
            Object o = c.newInstance();
            m.invoke(o, 1, 2);
        }

        elapsedTime = System.currentTimeMillis() - elapsedTime;
        System.out.println(elapsedTime);

    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Demo demo = new Demo();
        demo.invokeAllDummyMethods();
    }
}
