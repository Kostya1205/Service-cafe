package com.yulkost.service.bot;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class YulkostTelegramBot extends TelegramLongPollingBot{
    @Override
    public String getBotUsername() { return "yulkost_bot"; }
    @Override
    public String getBotToken() { return "6192725078:AAF6ZHs8lbRx-tMoAIRaicp80oK6rztXC9c"; }
    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getFirstName();

            switch (messageText){
                case "/start":
                    startBot(chatId, memberName);
                    break;
                case "/id":
                    ChatIdBot(chatId, memberName);
                    break;
                default: log.info("Unexpected message");
            }
        }
    }
    private void startBot(long chatId, String userName) {
        sendMessage(chatId,"Привет, " + userName + "! Если тебе нужен твой ChatId нажми на /id");
    }
    private void ChatIdBot(long chatId, String userName) {
        sendMessage(chatId,userName +" Ваш chatId:");
        sendMessage(chatId, Long.toString(chatId));
    }

    public void sendMessage(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

}