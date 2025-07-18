package com.svats.journalApp.controller;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.services.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        return handleException(() -> {
            entry.setLocalDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        });
    }

    @GetMapping("/id/{entryId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId entryId) {
        return handleOptionalAndException(() -> journalEntryService.getById(entryId));
    }

    @DeleteMapping("/id/{entryId}")
    public ResponseEntity<JournalEntry> deleteById(@PathVariable ObjectId entryId) {
        return handleException(() -> {
            journalEntryService.deleteById(entryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        });
    }

    @PutMapping("/id/{entryId}")
    public ResponseEntity<JournalEntry> updateById(@PathVariable ObjectId entryId, @RequestBody JournalEntry newEntry) {
        return handleOptionalAndException(() -> journalEntryService.updateById(entryId, newEntry));
    }

    /** HTTP Status Code Handlers */

    private static <T> ResponseEntity<T> handleOptionalAndException(Supplier<? extends Optional<T>> supplier) {
        return handleException(() -> handleOptional(supplier.get()));
    }

    private static <T> ResponseEntity<T> handleException(Supplier<? extends ResponseEntity<T>> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private static <T> ResponseEntity<T> handleOptional(Optional<T> tOpt) {
        if (tOpt.isPresent()) return new ResponseEntity<>(tOpt.get(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
