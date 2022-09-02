package org.library.notification;

import org.library.entity.BookItem;

public interface NotificationCallback {
    void triggerStatusChanged(BookItem bookItem);
}
