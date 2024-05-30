INSERT INTO member (created_at, deleted_at, updated_at, role, name, password, state, user_id)
SELECT CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'ADMIN', '김민우', '$2a$10$WTkzKqNlLO0PXK.q67W.G.Q1J8nU.swLVRdrLuVNV3tyEfMkOMVb.', true, 'kbsserver@naver.com'
    WHERE NOT EXISTS (SELECT 1 FROM member WHERE name = '김민우');
