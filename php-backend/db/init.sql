CREATE TABLE users
(
    id    UUID NOT NULL,
    name  TEXT NOT NULL,
    email TEXT NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE memberships
(
    id      UUID NOT NULL,
    user_id UUID REFERENCES users(id),
    role    TEXT NOT NULL,

    CONSTRAINT pk_memberships PRIMARY KEY (id)
);

INSERT INTO users VALUES('8b54349c-5d40-40a3-b03b-40af01f9bafb', 'John Smith', 'JSmith@example.com');
INSERT INTO users VALUES('f57975d2-e6ae-4f4a-aada-ee6cdcede0d1', 'Mary Doe', 'MDoe@example.com');
INSERT INTO users VALUES('62801bc1-d7a7-4b97-be5d-6eb0afb5bc08', 'Joe Blogs', 'JBlogs@example.com');
INSERT INTO users VALUES('79ee0f46-c2bd-4b7e-a670-adf1a2537385', 'Bob James', 'BJames@example.com');

INSERT INTO memberships VALUES('00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c', '8b54349c-5d40-40a3-b03b-40af01f9bafb', 'instructor');
INSERT INTO memberships VALUES('5e264f6d-708e-43e6-8932-5928b9ce9a62', 'f57975d2-e6ae-4f4a-aada-ee6cdcede0d1', 'student');
INSERT INTO memberships VALUES('735f68ee-40c1-48ae-8629-db7137c5d6f7', '62801bc1-d7a7-4b97-be5d-6eb0afb5bc08', 'student');

