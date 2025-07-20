package com.svats.journalApp.service;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Autowired
    UserService userService;

    @Transactional
    public Optional<Boolean> create(String username, JournalEntry journalEntry) {
        return userService.findByUsername(username).map(user -> {
            journalEntry.setLocalDate(LocalDateTime.now()); // Update Time
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry); // Save new journal entry
            userService.addJournalEntriesToUser(user, savedEntry); // Save updated user
            return true;
        });
    }

    public Optional<JournalEntry> getById(String username, ObjectId entryId) {
        return userService.findByUsername(username).flatMap(user -> {
            boolean desiredIdExists = user.getJournalEntries().stream().anyMatch(e -> e.getId().equals(entryId));
            return desiredIdExists ? journalEntryRepository.findById(entryId) : Optional.empty();
        });
    }

    public Optional<Boolean> deleteById(String username, ObjectId entryId) {
        return userService.findByUsername(username).map(user -> {
            boolean desiredIdExists = user.getJournalEntries().stream().anyMatch(e -> e.getId().equals(entryId));
            if (desiredIdExists) {
                journalEntryRepository.deleteById(entryId);
                return true;
            } else return false;
        });
    }

    public Optional<Boolean> updateById(String username, ObjectId entryId, JournalEntry newEntry) {
        return userService.findByUsername(username).flatMap(user -> {
            boolean desiredIdExists = user.getJournalEntries().stream().anyMatch(e -> e.getId().equals(entryId));
            if (!desiredIdExists) return Optional.empty();
            return journalEntryRepository.findById(entryId).map(oldEntry -> {
                String newTitle = newEntry.getTitle();
                String newContent = newEntry.getContent();
                if (!newTitle.equals("")) oldEntry.setTitle(newTitle);
                if (newContent != null && !newContent.equals("")) oldEntry.setContent(newContent);
                journalEntryRepository.save(oldEntry);
                return true;
            });
        });
    }

}
