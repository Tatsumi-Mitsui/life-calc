-- ユーザーテーブル
CREATE TABLE auth_user (
    id              BIGSERIAL PRIMARY KEY,
    display_name    VARCHAR(30) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ロールテーブル
CREATE TABLE auth_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

--ユーザーとロールの中間テーブル（ManyToMany）
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES auth_user(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES auth_role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);