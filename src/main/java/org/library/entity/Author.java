package org.library.entity;

import org.library.annotations.Entity;

@Entity
public class Author {
    String authorName;
    String authorDescription;

    public Author(String authorName, String authorDescription) {
        this.authorName = authorName;
        this.authorDescription = authorDescription;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Author)) return false;
        return this.authorName.equals(((Author) obj).authorName);
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorDescription() {
        return authorDescription;
    }

    public void setAuthorDescription(String authorDescription) {
        this.authorDescription = authorDescription;
    }
}
