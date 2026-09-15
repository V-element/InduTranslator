-- Create admin user manually for InduTranslator
-- This script creates enterprise, department, and admin user

-- Step 1: Create default enterprise if not exists
INSERT INTO enterprises (name, created_at, updated_at, code)
SELECT 'Default Enterprise', NOW(), NOW(), 'DEF'
WHERE NOT EXISTS (SELECT 1 FROM enterprises WHERE code = 'DEF');

-- Step 2: Get enterprise ID
DO $$
DECLARE
    v_enterprise_id BIGINT;
    v_department_id BIGINT;
    v_admin_role_id BIGINT;
    v_user_role_id BIGINT;
    v_admin_user_id BIGINT;
BEGIN
    -- Get enterprise ID
    SELECT id INTO v_enterprise_id FROM enterprises WHERE code = 'DEF';
    
    IF v_enterprise_id IS NULL THEN
        RAISE EXCEPTION 'Enterprise not found';
    END IF;
    
    -- Step 3: Create default department if not exists
    INSERT INTO departments (name, enterprise_id, created_at, updated_at, code)
    SELECT 'Default Department', v_enterprise_id, NOW(), NOW(), 'DEF_DEPT'
    WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'DEF_DEPT' AND enterprise_id = v_enterprise_id);
    
    -- Get department ID
    SELECT id INTO v_department_id FROM departments WHERE code = 'DEF_DEPT' AND enterprise_id = v_enterprise_id;
    
    IF v_department_id IS NULL THEN
        RAISE EXCEPTION 'Department not found';
    END IF;
    
    -- Step 4: Get role IDs
    SELECT id INTO v_admin_role_id FROM roles WHERE name = 'ROLE_ADMIN';
    SELECT id INTO v_user_role_id FROM roles WHERE name = 'ROLE_USER';
    
    IF v_admin_role_id IS NULL OR v_user_role_id IS NULL THEN
        RAISE EXCEPTION 'Roles not found';
    END IF;
    
    -- Step 5: Create admin user
    INSERT INTO users (
        username, password, email, first_name, last_name, 
        is_active, phone, enterprise_id, department_id,
        created_at, updated_at
    )
    SELECT 
        'admin',
        '$2a$10$zFFqEzIYiAIuhXCK55weCejQYZu2p3k9BdXRAhFAB93L2SudLy9Nu', -- admin123 encoded with BCrypt
        'admin@example.com',
        'Admin',
        'User',
        true,
        null,
        v_enterprise_id,
        v_department_id,
        NOW(),
        NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');
    
    -- Get admin user ID
    SELECT id INTO v_admin_user_id FROM users WHERE username = 'admin';
    
    IF v_admin_user_id IS NULL THEN
        RAISE EXCEPTION 'Admin user not created';
    END IF;
    
    -- Step 6: Assign roles to admin user
    INSERT INTO users_roles (user_id, role_id)
    SELECT v_admin_user_id, v_admin_role_id
    WHERE NOT EXISTS (SELECT 1 FROM users_roles WHERE user_id = v_admin_user_id AND role_id = v_admin_role_id);
    
    INSERT INTO users_roles (user_id, role_id)
    SELECT v_admin_user_id, v_user_role_id
    WHERE NOT EXISTS (SELECT 1 FROM users_roles WHERE user_id = v_admin_user_id AND role_id = v_user_role_id);
    
    RAISE NOTICE 'Admin user created successfully!';
    RAISE NOTICE 'Username: admin';
    RAISE NOTICE 'Password: admin123';
    RAISE NOTICE 'Enterprise ID: %', v_enterprise_id;
    RAISE NOTICE 'Department ID: %', v_department_id;
    RAISE NOTICE 'User ID: %', v_admin_user_id;
END $$;
