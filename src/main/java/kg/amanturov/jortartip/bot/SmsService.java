package kg.amanturov.jortartip.bot;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

public class SmsService {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public void sendSms(String toPhoneNumber, String message) {
        Twilio.init(accountSid, authToken);
        Message.creator(new PhoneNumber(toPhoneNumber), new PhoneNumber(fromPhoneNumber), message).create();
    }
}
