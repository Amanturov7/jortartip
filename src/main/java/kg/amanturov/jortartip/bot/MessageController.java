package kg.amanturov.jortartip.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/telegram")
public class MessageController {

    @Autowired
    private MyTelegramBot myTelegramBot;

    @PostMapping("/sendToChannel")
    public String sendToChannel(@RequestBody MessageRequest messageRequest) {
        myTelegramBot.sendMessageToChannel(messageRequest.getMessage());
        return "Message sent to channel";
    }

    @PostMapping("/sendPhotoWithCaptionToChannel")
    public String sendPhotoWithCaptionToChannel(@RequestBody PhotoRequest photoRequest) {
        myTelegramBot.sendPhotoWithCaptionToChannel(photoRequest.getPhotoUrl(), photoRequest.getCaption());
        return "Photo with caption sent to channel";
    }

    public static class MessageRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class PhotoRequest {
        private String photoUrl;
        private String caption;

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;

        }
    }
}