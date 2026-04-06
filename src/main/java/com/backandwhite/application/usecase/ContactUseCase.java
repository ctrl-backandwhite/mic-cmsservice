package com.backandwhite.application.usecase;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.ContactMessage;

public interface ContactUseCase {
    ContactMessage submit(ContactMessage message);

    ContactMessage findById(String id);

    PageResult<ContactMessage> findAll(int page, int size, String sortBy, boolean ascending);

    void markAsRead(String id);
}
