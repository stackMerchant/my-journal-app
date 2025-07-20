package com.svats.journalApp.controller;

import com.svats.journalApp.entity.InMemoryJournalEntry;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/in-memory/journal")
public class InMemoryJournalEntryController {

    private Map<String, InMemoryJournalEntry> journalEntries = new HashMap<>();

    @GetMapping("/all")
    public List<InMemoryJournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping("/create")
    public void createEntry(@RequestBody InMemoryJournalEntry entry) {
        journalEntries.put(entry.getId(), entry);
        return;
    }

    @GetMapping("/id/{entryId}")
    public InMemoryJournalEntry getById(@PathVariable String entryId) {
        return journalEntries.get(entryId);
    }

    @DeleteMapping("/id/{entryId}")
    public InMemoryJournalEntry deleteById(@PathVariable String entryId) {
        return journalEntries.remove(entryId);
    }

    @PutMapping("/id/{entryId}")
    public InMemoryJournalEntry updateById(@PathVariable String entryId, @RequestBody InMemoryJournalEntry newEntry) {
        return journalEntries.put(entryId, newEntry);
    }

}
