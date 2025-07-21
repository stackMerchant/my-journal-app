package com.svats.journalApp.service;

import com.svats.journalApp.model.JournalAnalysisForMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JournalAnalysisConsumerService {

    @Autowired
    EmailService emailService;

    @KafkaListener(topics = "journal-mails", groupId = "mail-sender")
    public void consume(JournalAnalysisForMail analysisForMail) {
        sendMail(analysisForMail);
    }

    private void sendMail(JournalAnalysisForMail analysisForMail) {
        emailService.sendMail(analysisForMail.getEmail(), "Last week analysis", analysisForMail.getAnalysis());
    }

}
