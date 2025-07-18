package com.svats.journalApp.services;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    JournalEntryRepository journalEntryRepository;

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public Optional<JournalEntry> getById(ObjectId entryId) {
        return journalEntryRepository.findById(entryId);
    }

    public boolean deleteById(ObjectId entryId) {
        journalEntryRepository.deleteById(entryId);
        return true;
    }

    public JournalEntry updateById(ObjectId entryId, JournalEntry newEntry) {
        JournalEntry oldEntry = journalEntryRepository.findById(entryId).orElse(null);
        if (oldEntry != null) {
            String newTitle = newEntry.getTitle();
            String newContent = newEntry.getContent();
            if (newTitle != null && !newTitle.equals("")) oldEntry.setTitle(newTitle);
            if (newContent != null && !newContent.equals("")) oldEntry.setContent(newContent);
            journalEntryRepository.save(oldEntry);
        }
        return oldEntry;
    }

}
