package com.svats.journalApp.controller;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.entity.User;
import com.svats.journalApp.services.JournalEntryService;
import com.svats.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.svats.journalApp.controller.ControllerHelpers.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Boolean> createEntry(@RequestBody JournalEntry entry) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return journalEntryService.create(username, entry);
        }, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<JournalEntry>> getAllEntriesForUser() {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userService.findByUsername(username).map(User::getJournalEntries);
        });
    }

    @GetMapping("/id/{entryId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId entryId) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return journalEntryService.getById(username, entryId);
        });
    }

    @DeleteMapping("/id/{entryId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable ObjectId entryId) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return journalEntryService.deleteById(username, entryId);
        }, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{entryId}")
    public ResponseEntity<Boolean> updateById(@PathVariable ObjectId entryId, @RequestBody JournalEntry newEntry) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return journalEntryService.updateById(username, entryId, newEntry);
        });
    }

}
