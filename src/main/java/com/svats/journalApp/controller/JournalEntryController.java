package com.svats.journalApp.controller;

import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.entity.User;
import com.svats.journalApp.service.JournalEntryService;
import com.svats.journalApp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.svats.journalApp.controller.ControllerHelpers.handleExceptionOptional;

@RestController
@RequestMapping("/journal-entry")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Boolean> createEntry(@RequestBody CreateJournalEntryDTO createJournalEntryDTO) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return journalEntryService.create(username, createJournalEntryDTO.toJournalEntry());
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
    public ResponseEntity<JournalEntry> getById(@PathVariable String entryId) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            ObjectId objectId = new ObjectId(entryId);
            return journalEntryService.getById(username, objectId);
        });
    }

    @DeleteMapping("/id/{entryId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable String entryId) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            ObjectId objectId = new ObjectId(entryId);
            return journalEntryService.deleteById(username, objectId);
        }, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{entryId}")
    public ResponseEntity<Boolean> updateById(@PathVariable String entryId, @RequestBody JournalEntry newEntry) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            ObjectId objectId = new ObjectId(entryId);
            return journalEntryService.updateById(username, objectId, newEntry);
        });
    }

    @Data
    @AllArgsConstructor
    private static class CreateJournalEntryDTO {
        @NonNull
        private String title;
        private String content;

        JournalEntry toJournalEntry() {
            return JournalEntry.builder().title(title).content(content).localDate(LocalDateTime.now()).build();
        }
    }

}
