-- history_entry に user_id を追加して、users と紐付け
ALTER TABLE history_entry
    ADD CONSTRAINT fk_history_entry_user
    FOREIGN KEY (user_id) REFERENCES auth_user(id)
    ON DELETE CASCADE;