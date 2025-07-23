package com.svats.journalApp.scheduler;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.entity.User;
import com.svats.journalApp.model.JournalAnalysisForMail;
import com.svats.journalApp.repository.UserRepositoryImpl;
import com.svats.journalApp.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    UserRepositoryImpl userRepository;

    @Autowired
    KafkaTemplate<String, JournalAnalysisForMail> kafkaTemplate;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMail() {
        List<User> users = userRepository.getUsersForAnalysis();
        LocalDateTime dateLimit = LocalDateTime.now().minusDays(7);
        for (User user: users) {
            // Filter entries
            List<JournalEntry> filteredJournalEntries = user.getJournalEntries().stream()
                    .filter(x -> x.getLocalDate().isAfter(dateLimit)).collect(Collectors.toList());
            // Get analysis
            String analysis = getAnalysis(filteredJournalEntries);
            JournalAnalysisForMail mailDTO = new JournalAnalysisForMail(user.getEmail(), analysis);
            ExceptionUtils.handleException(() -> {
                kafkaTemplate.send("journal-mails", user.getEmail(), mailDTO);
            });
        }
    }

    private String getAnalysis(List<JournalEntry> entries) {
        return entries.size() + " entries in last week";
    }

}
