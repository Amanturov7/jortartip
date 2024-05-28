package kg.amanturov.jortartip.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.channel.id}")
    private String channelId;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (messageText.startsWith("/sendtocanal")) {
                String[] parts = messageText.split(" ", 2);
                if (parts.length >= 2) {
                    String messageToSend = parts[1];
                    sendMessageToChannel(messageToSend);
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Message sent to the channel");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void sendMessageToChannel(String text) {
        SendMessage message = new SendMessage();
        message.setChatId(channelId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhotoWithCaptionToChannel(String filePath, String caption) {
        try {
            Path fileToUpload = Paths.get(filePath);

            if (Files.exists(fileToUpload)) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(channelId);
                sendPhoto.setCaption(caption);
                sendPhoto.setPhoto(new InputFile(new FileInputStream(fileToUpload.toFile()), fileToUpload.getFileName().toString()));
                execute(sendPhoto);
            } else {
                System.out.println("Файл не найден по указанному пути: " + filePath);
            }
        } catch (FileNotFoundException | TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
