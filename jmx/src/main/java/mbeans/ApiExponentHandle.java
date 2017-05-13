package mbeans;

import api.Api;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 * Created by Mateusz on 05.05.2017.
 */
public class ApiExponentHandle extends NotificationBroadcasterSupport implements ApiExponentHandleMBean {

    private int sequenceNumber = 0;

    @Override
    public void setExponent(double value) {
        Api.setExponent(value);

        String valueDesc;

        if(value < 5){
            valueDesc = "Mala wartosc";
        }
        else if (value > 5 && value < 10){
            valueDesc = "Srednia wartosc";
        }
        else{
            valueDesc = "Wysoka wartosc";
        }

        Notification notification = new AttributeChangeNotification(this,
                sequenceNumber++, System.currentTimeMillis(),
                "Zmiana wykladnika potegi: " + valueDesc, "exponent", "double", Api.getExponent(),
                value);
        notification.setUserData("zmiana wykladnika  na " + value);
        sendNotification(notification);
    }

    @Override
    public double getExponent() {
        return Api.getExponent();
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };

        String name = AttributeChangeNotification.class.getName();
        String description;
        if(getExponent() < 5){
            description = "Mala wartosc potegi";
        }
        else if (getExponent() > 5 && getExponent() < 10){
            description = "Srednia wartosc potegi";
        }
        else{
            description = "Wysoka wartosc potegi";
        }
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name,
                description);
        return new MBeanNotificationInfo[] { info };
    }
}
