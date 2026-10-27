UPDATE users
SET role = 'ADMIN'
WHERE username = 'username'
    RETURNING id, username, role;