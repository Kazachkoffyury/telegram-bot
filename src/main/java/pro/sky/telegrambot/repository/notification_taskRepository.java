package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Notification_task;

import java.time.LocalDateTime;
import java.util.List;


public interface notification_taskRepository extends JpaRepository<Notification_task, Long> {

    List<Notification_task> findByDate(LocalDateTime date);
}
