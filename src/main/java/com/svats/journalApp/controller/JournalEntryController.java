package com.svats.journalApp.controller;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.services.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mongo/journal-entry")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @GetMapping("/all")
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }

    @PostMapping("/create")
    public JournalEntry createEntry(@RequestBody JournalEntry entry) {
        entry.setLocalDate(LocalDateTime.now());
        journalEntryService.saveEntry(entry);
        return entry;
    }

    @GetMapping("/id/{entryId}")
    public JournalEntry getById(@PathVariable ObjectId entryId) {
        return journalEntryService.getById(entryId).orElse(null);
    }

    @DeleteMapping("/id/{entryId}")
    public boolean deleteById(@PathVariable ObjectId entryId) {
        return journalEntryService.deleteById(entryId);
    }

    @PutMapping("/id/{entryId}")
    public JournalEntry updateById(@PathVariable ObjectId entryId, @RequestBody JournalEntry newEntry) {
        return journalEntryService.updateById(entryId, newEntry);
    }

}
