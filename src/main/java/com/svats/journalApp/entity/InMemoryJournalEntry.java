package com.svats.journalApp.entity;

import lombok.Data;

@Data
public class InMemoryJournalEntry {
    private String id;
    private String title;
    private String content;
}
