package com.svats.journalApp.controller;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.entity.User;
import com.svats.journalApp.services.JournalEntryService;
import com.svats.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.svats.journalApp.controller.ControllerHelpers.*;

@RestController
@RequestMapping("/journal-entry")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }

    @GetMapping("/all/{username}")
    public ResponseEntity<List<JournalEntry>> getAllByUsername(@PathVariable String username) {
        return handleExceptionOptional(() -> {
            return userService.findByUsername(username).map(User::getJournalEntries);
        });
    }

    @PostMapping("/{username}")
    public ResponseEntity<Boolean> createEntry(@PathVariable String username, @RequestBody JournalEntry entry) {
        return handleExceptionOptional(() -> {
            return journalEntryService.save(username, entry);
        }, HttpStatus.CREATED);
    }

    @GetMapping("/id/{entryId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId entryId) {
        return handleExceptionOptional(() -> journalEntryService.getById(entryId));
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
        return handleExceptionOptional(() -> journalEntryService.updateById(entryId, newEntry));
    }

}
