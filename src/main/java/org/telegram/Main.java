package org.telegram;

import org.telegram.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Map;

public class Main {
    private static final Map<String, String> getenv = System.getenv();

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot("ISavvy_helpbot", "6060041818:AAFAVGAAeyTTP4ADdN4cFzFeP37B9vlBpSo"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
