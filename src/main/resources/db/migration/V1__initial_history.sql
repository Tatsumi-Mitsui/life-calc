-- 履歴エントリ
CREATE TABLE history_entry (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    income              BIGINT,
    fixed_cost_total    BIGINT NOT NULL,
    result_variable     BIGINT NOT NULL,
    saved_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 履歴明細
CREATE TABLE history_item (
    id          BIGSERIAL PRIMARY KEY,
    idx         INT NOT NULL,
    amount      BIGINT NOT NULL,
    entry_id    BIGINT NOT NULL REFERENCES history_entry(id) ON DELETE CASCADE
);