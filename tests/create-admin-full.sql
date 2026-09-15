-- Create roles
INSERT INTO roles (name, description, is_system, created_at, updated_at)
VALUES 
    ('ROLE_ADMIN', 'Administrator', true, NOW(), NOW()),
    ('ROLE_USER', 'Regular user', true, NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- Create enterprise
INSERT INTO enterprises (name, code, created_at, updated_at)
VALUES ('Default Enterprise', 'DEF', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- Create department
INSERT INTO departments (name, enterprise_id, code, created_at, updated_at)
SELECT 'Default Department', e.id, 'DEF_DEPT', NOW(), NOW()
FROM enterprises e
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'DEF_DEPT');

-- Create admin user (password: admin123, BCrypt hash)
INSERT INTO users (
    username, password, email, first_name, last_name, 
    is_active, enterprise_id, department_id, created_at, updated_at
)
SELECT 
    'admin',
    '$2a$10$zFFqEzIYiAIuhXCK55weCejQYZu2p3k9BdXRAhFAB93L2SudLy9Nu',
    'admin@example.com',
    'Admin',
    'User',
    true,
    e.id,
    d.id,
    NOW(),
    NOW()
FROM enterprises e, departments d
WHERE e.code = 'DEF' 
  AND d.code = 'DEF_DEPT'
  AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Assign roles to admin
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin'
  AND r.name IN ('ROLE_ADMIN', 'ROLE_USER')
  AND NOT EXISTS (
    SELECT 1 FROM users_roles ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
