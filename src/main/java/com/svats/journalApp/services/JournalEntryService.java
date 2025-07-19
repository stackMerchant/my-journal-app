package com.svats.journalApp.services;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Autowired
    UserService userService;

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    @Transactional
    public Optional<Boolean> save(String username, JournalEntry journalEntry) {
        return userService.findByUsername(username).map(user -> {
            journalEntry.setLocalDate(LocalDateTime.now()); // Update Time
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry); // Save new journal entry
            user.getJournalEntries().add(savedEntry); // Add reference for user
            userService.save(user); // Save updated user
            return true;
        });
    }

    public Optional<JournalEntry> getById(ObjectId entryId) {
        return journalEntryRepository.findById(entryId);
    }

    public boolean deleteById(ObjectId entryId) {
        journalEntryRepository.deleteById(entryId);
        return true;
    }

    public Optional<JournalEntry> updateById(ObjectId entryId, JournalEntry newEntry) {
        return journalEntryRepository.findById(entryId).map(oldEntry -> {
            String newTitle = newEntry.getTitle();
            String newContent = newEntry.getContent();
            if (!newTitle.equals("")) oldEntry.setTitle(newTitle);
            if (newContent != null && !newContent.equals("")) oldEntry.setContent(newContent);
            journalEntryRepository.save(oldEntry);
            return newEntry;
        });
    }

}
