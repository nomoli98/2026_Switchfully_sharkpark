ALTER TABLE users
    ADD COLUMN membership VARCHAR(10) NOT NULL DEFAULT 'BRONZE';

ALTER TABLE users
    ADD CONSTRAINT chk_users_membership
        CHECK (membership IN ('BRONZE', 'SILVER', 'GOLD'));