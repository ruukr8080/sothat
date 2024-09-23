ALTER TABLE account_roles
    ADD CONSTRAINT fk_account_roles_on_account FOREIGN KEY (account_account_id) REFERENCES account (account_id);

ALTER TABLE account
DROP
COLUMN id;

ALTER TABLE refresh_token
    ALTER COLUMN rt_key DROP NOT NULL;

ALTER TABLE account
    ADD CONSTRAINT pk_account PRIMARY KEY (account_id);

ALTER TABLE refresh_token
    ADD CONSTRAINT pk_refresh_token PRIMARY KEY (id);