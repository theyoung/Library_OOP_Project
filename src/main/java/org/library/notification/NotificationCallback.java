package org.library.notification;

import org.library.entity.BookItem;

public interface NotificationCallback<T> {
    void triggerStatusChanged(T bookItem);
}
