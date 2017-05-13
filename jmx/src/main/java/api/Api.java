package api;

import mbeans.ApiExponentHandle;
import mbeans.ApiExponentHandleMBean;

import javax.management.*;

import java.lang.management.ManagementFactory;

import static spark.Spark.*;

/**
 * Created by Mateusz on 05.05.2017.
 */
public class Api {

    private static double exponent = 3;

    public static void setExponent(double val){
        exponent = val;
    }

    public static double getExponent(){
        return exponent;
    }

    private static double power(double base){
        return Math.pow(base, exponent);
    }

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName apiExponentHandlerName = new ObjectName("mbeans:type=Server,name=ApiExponent");
        ApiExponentHandle aeh = new ApiExponentHandle();
        mbs.registerMBean(aeh, apiExponentHandlerName);

        get("/potega/:base", (req, res) -> power(Double.valueOf(req.params(":base"))));
    }
}
