package org.library.entity;

import org.library.annotations.Entity;

import java.util.Date;

@Entity
public class Book {
    String ISBN;
    Date publishedDate;
    String title;
    String subject;
    String language;
    Author author;

    public Book(String ISBN, Date publishedDate, String title, String subject, String language, Author author) {
        this.ISBN = ISBN;
        this.publishedDate = publishedDate;
        this.title = title;
        this.subject = subject;
        this.language = language;
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
