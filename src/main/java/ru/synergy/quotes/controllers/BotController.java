package ru.synergy.quotes.controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.synergy.quotes.models.Chat;
import ru.synergy.quotes.models.Quote;
import ru.synergy.quotes.repositories.ChatRepository;
import ru.synergy.quotes.services.QuoteService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class BotController {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private QuoteService quoteService;
    private final TelegramBot bot;

    public BotController() {
        String botToken;
        try {
            botToken = Files.readString(Path.of("./src/main/resources/token.txt"));
            bot = new TelegramBot(botToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bot.setUpdatesListener(updates -> {
                    for (Update update : updates) {
                        handleUpdate(update);
                    }
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }
        );
    }

    private void handleUpdate(Update update) {
        String text = update.message().text();
        Long chatId = update.message().chat().id();
        var rawChat = chatRepository.findByChatId(chatId);
        Chat chat;
        if (rawChat.isPresent()) {
            chat = rawChat.get();
        } else {
            var _chat = new Chat();
            _chat.setChatId(chatId);
            _chat.setLastId(0);
            chat = chatRepository.save(_chat);
        }
        switch (text) {
            case "/start":
            case "/next":
                sendNextQuote(chat);
                break;
            case "/prev":
                sendPrevQuote(chat);
                break;
            case "/random":
                sendRandom(chat);
        }
    }

    // Получение следующего Quote
    private void sendNextQuote(Chat chat) {
        Quote quote = null;
        int newId = chat.getLastId();
        while (quote == null) {
            newId++;
            quote = quoteService.getById(newId);
        }
        chat.setLastId(quote.getQuoteId());
        chatRepository.save(chat); //обновление чата (lastId)
        sendText(chat.getChatId(), quote.getText());
    }

    // Получение предыдущего Quote
    private void sendPrevQuote(Chat chat) {
        Quote quote = null;
        int newId = chat.getLastId();
        while (quote == null) {
            newId--;
            if (newId < 2) {
                newId = 2;
            }
            quote = quoteService.getById(newId);
        }
        chat.setLastId(quote.getQuoteId());
        chatRepository.save(chat); //обновление чата (lastId)
        sendText(chat.getChatId(), quote.getText());
    }

    private void sendRandom(Chat chat) {
        Quote quote = quoteService.getRandom();
        sendText(chat.getChatId(), quote.getText());
    }

    private void sendText(long chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }


}
