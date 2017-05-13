package client;

import mbeans.ApiExponentHandle;
import mbeans.ApiExponentHandleMBean;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Mateusz on 05.05.2017.
 */
public class Client {

    public static class ClientListener implements NotificationListener {
        public void handleNotification(Notification notification,
                                       Object handback) {
            echo("\nReceived notification:");
            echo("\tClassName: " + notification.getClass().getName());
            echo("\tSource: " + notification.getSource());
            echo("\tType: " + notification.getType());
            echo("\tMessage: " + notification.getMessage());
            if (notification instanceof AttributeChangeNotification) {
                AttributeChangeNotification acn =
                        (AttributeChangeNotification) notification;
                echo("\tAttributeName: " + acn.getAttributeName());
                echo("\tAttributeType: " + acn.getAttributeType());
                echo("\tNewValue: " + acn.getNewValue());
                echo("\tOldValue: " + acn.getOldValue());
            }
        }
    }

    public static void main(String[] args) throws Exception {

        echo("\nOpen the connection to the server");
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        ClientListener listener = new ClientListener();

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        Set<ObjectName> names = mbsc.queryNames(null, null);

        ObjectName mxbeanName =
                new ObjectName("mbeans:type=Server,name=ApiExponent");

        mbsc.addNotificationListener(mxbeanName, listener, null, null);


        ApiExponentHandleMBean mxbeanProxy =
                JMX.newMXBeanProxy(mbsc, mxbeanName, ApiExponentHandleMBean.class);

        mxbeanProxy.setExponent(7);


        echo("\nWaiting for notification...");
        sleep(2000);

        waitForEnterPressed();


        echo("\nClose the connection to the server");
        jmxc.close();
        echo("\nBye! Bye!");
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void waitForEnterPressed() {
        try {
            echo("\nPress <Enter> to continue...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
