package com.svats.journalApp.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InMemoryJournalEntry {
    private String id;
    private String title;
    private String content;
}
