package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification_task;
import pro.sky.telegrambot.repository.notification_taskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final notification_taskRepository notification_taskRepository;
    private final TelegramBot telegramBot;



    public TelegramBotUpdatesListener(pro.sky.telegrambot.repository.notification_taskRepository notification_taskRepository, TelegramBot telegramBot) {
        this.notification_taskRepository = notification_taskRepository;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            long chatId = update.message().chat().id();
                if (update.message().text().equals("/start")) {
                    String messageText = "задай нотификацию в виде:28.04.2022 22:17 сдать курсовую";
                    SendMessage message = new SendMessage(chatId, messageText);
                    SendResponse response = telegramBot.execute(message);
                }

                else  saveNotification(update.message().text(),chatId);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public String parsingMessage(int number,String text) {

        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
             text = matcher.group(number);
        }

        return text;
    }

    public void saveNotification(String text,Long chatId) {
      try {LocalDateTime date = LocalDateTime.parse(parsingMessage(1,text), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
          String notification = parsingMessage(3,text);
          logger.info("дата:{}",date);
          logger.info("нотификация:{}",notification);
          Notification_task notication_task = new Notification_task();
          notication_task.setId_chat(chatId);
          notication_task.setNotification(notification);
          notication_task.setDate(date);
          notification_taskRepository.save(notication_task);

      }
      catch (Exception e ) {
        logger.info("ловим исключение");
      }



    }

    @Scheduled( cron = "0 0/1 * * * *" )
    public void searchNotification() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Notification_task> current_tasks = notification_taskRepository.findByDate(currentTime);
        if (current_tasks.size()>0){
            logger.info("есть таска для пуша");//для отладки
            current_tasks.forEach(current_task -> {
                SendMessage message = new SendMessage(current_task.getId_chat(), current_task.getNotification());
                SendResponse response = telegramBot.execute(message);
            });

        }
        else logger.info(("пусто"));// для отладки

    }




}
