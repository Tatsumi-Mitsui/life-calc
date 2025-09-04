-- ゲストユーザー（id=0）を用意して整合性を取る
INSERT INTO app_user(id, email, password, display_name, role_id, saved_at)
VALUES (
    0,
    'guest@example.com',
    '!',
    'ゲスト',
    (SELECT id FROM roles WHERE name = 'USER'),
    now()
);

-- 既存 history_entry.user_id と app_user をひも付け
ALTER TABLE history_entry
    ADD CONSTRAINT fk_history_entry_user
    FOREIGN KEY (user_id) REFERENCES app_user(id);