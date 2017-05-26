package instrumentation;

import java.lang.instrument.Instrumentation;

/**
 * Created by Mateusz on 25.05.2017.
 */
public class InstrumentationAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        //inst.addTransformer(new MyClassFileTransformer());
        inst.addTransformer(new JavassistTransformer());
        //inst.addTransformer(new AsmTransformer());
    }
}
