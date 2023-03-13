package org.telegram.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Bot extends TelegramLongPollingBot {
    private final List<Long> usersId = new ArrayList<>();
    Map<Long, String> dataMap = new HashMap<>();
    Map<String, Long> messages = new HashMap<>();
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }


    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message message = update.getMessage();
                if (message.getChatId() == -935929648) {
                    Message origin = message.getReplyToMessage();

                    if (messages.containsKey(origin.getText())) {
                        Long chatId = messages.get(origin.getText());
                        SendMessage outMess = new SendMessage();

                        outMess.setText(message.getText());
                        outMess.setChatId(String.valueOf(chatId));
                        execute(outMess);

                        return;
                    }

                }
                if (!usersId.contains(message.getChatId())) {
                    askStart(update);
                } else {
                    Message inMess = update.getMessage();
                    Long chatId = inMess.getChatId();
                    SendMessage outMess = new SendMessage();

                    String data = inMess.getText();
                    String downer = dataMap.get(update.getMessage().getFrom().getId());

                    List<String> list = List.of(downer.split("#"));

                    String response = "Цель: " + list.get(0) + "\n";
                    response += ("Мир: " + list.get(1) + "\n");
                    response += ("От: @" + inMess.getFrom().getUserName() + "\n");
                    response += ("Обращение: " + data);

                    outMess.setText(response);
                    outMess.setChatId("-935929648");
                    execute(outMess);

                    SendMessage thanks = new SendMessage();
                    thanks.setText("Спасибо за обратную связь!\nВведите любую строку чтобы снова начать бота!");
                    thanks.setChatId(String.valueOf(chatId));
                    execute(thanks);

                    messages.put(outMess.getText(), update.getMessage().getFrom().getId());

                    usersId.remove(update.getMessage().getFrom().getId());
                }
            } else if (update.hasCallbackQuery()) {
                if (!update.getCallbackQuery().getData().contains("#")) {
                    askWorld(update);
                } else {
                    Long chatId = update.getCallbackQuery().getMessage().getChatId();

                    SendMessage outMess = new SendMessage();
                    outMess.setChatId(String.valueOf(chatId));
                    outMess.setText("Опишите, пожалуйста, вашу проблему:");

                    usersId.add(update.getCallbackQuery().getFrom().getId());

                    dataMap.put(update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());

                    execute(outMess);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void askStart(Update update) throws TelegramApiException {
        dataMap.remove(update.getMessage().getFrom().getId());

        Message inMess = update.getMessage();
        Long chatId = inMess.getChatId();
        SendMessage outMess = new SendMessage();

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInlineOne = new ArrayList<>();
        InlineKeyboardButton mistake = new InlineKeyboardButton();
        mistake.setText("Сообщить об ошибке");
        mistake.setCallbackData("Сообщить об ошибке");
        rowInlineOne.add(mistake);

        List<InlineKeyboardButton> rowInlineTwo = new ArrayList<>();
        InlineKeyboardButton review = new InlineKeyboardButton();
        review.setText("Оставить отзыв");
        review.setCallbackData("Оставить отзыв");
        rowInlineTwo.add(review);

        List<InlineKeyboardButton> rowInlineThree = new ArrayList<>();
        InlineKeyboardButton other = new InlineKeyboardButton();
        other.setText("Другое");
        other.setCallbackData("Другое");
        rowInlineThree.add(other);

        rowsInline.add(rowInlineOne);
        rowsInline.add(rowInlineTwo);
        rowsInline.add(rowInlineThree);

        markupInline.setKeyboard(rowsInline);

        outMess.setChatId(String.valueOf(chatId));
        outMess.setText("Ваша цель обращения?");
        outMess.setReplyMarkup(markupInline);
        execute(outMess);
    }

    private void askWorld(Update update) throws TelegramApiException {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        String data = update.getCallbackQuery().getData();

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInlineOne = new ArrayList<>();
        InlineKeyboardButton mistake = new InlineKeyboardButton();
        mistake.setText("Мир четного и нечетного");
        mistake.setCallbackData(data + "#" + "Четное/нечетное");
        rowInlineOne.add(mistake);

        List<InlineKeyboardButton> rowInlineTwo = new ArrayList<>();
        InlineKeyboardButton review = new InlineKeyboardButton();
        review.setText("Мир логики");
        review.setCallbackData(data + "#" + "Мир_Логики");
        rowInlineTwo.add(review);

        List<InlineKeyboardButton> rowInlineThree = new ArrayList<>();
        InlineKeyboardButton other = new InlineKeyboardButton();
        other.setText("Мир умножения");
        other.setCallbackData(data + "#" + "Мир_Умножения");
        rowInlineThree.add(other);

        rowsInline.add(rowInlineOne);
        rowsInline.add(rowInlineTwo);
        rowsInline.add(rowInlineThree);

        markupInline.setKeyboard(rowsInline);

        SendMessage outMess = new SendMessage();
        outMess.setChatId(String.valueOf(chatId));
        outMess.setText("О каком мире вы хотите сообщить?");
        outMess.setReplyMarkup(markupInline);

        execute(outMess);
    }
}