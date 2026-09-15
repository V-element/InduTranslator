-- Create default enterprise if it doesn't exist
INSERT INTO enterprises (name, description, code, is_active, created_at, updated_at)
SELECT 'Default Enterprise', 'Default enterprise for the system', 'DEFAULT', true, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM enterprises WHERE code = 'DEFAULT'
);

-- Update admin user to have enterprise_id
UPDATE users 
SET enterprise_id = (SELECT id FROM enterprises WHERE code = 'DEFAULT' LIMIT 1)
WHERE username = 'admin';
