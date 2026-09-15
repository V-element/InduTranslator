-- ============================================
-- InduTranslator Enterprise Domain Model
-- Full MVP schema with multi-tenant support
-- ============================================

-- enterprises: Предприятие/контур внедрения
CREATE TABLE enterprises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(50) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- users: Пользователи системы
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL REFERENCES enterprises(id),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- roles: Роли доступа и процессные роли
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT REFERENCES enterprises(id),
    name VARCHAR(50) NOT NULL,
    description TEXT,
    is_system BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- users_roles: Связь пользователей и ролей
CREATE TABLE users_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- departments: Подразделения предприятия
CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL REFERENCES enterprises(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    parent_id BIGINT REFERENCES departments(id),
    head_id BIGINT REFERENCES users(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- departments_competencies: Связь отделов и компетенций
CREATE TABLE departments_competencies (
    department_id BIGINT NOT NULL REFERENCES departments(id) ON DELETE CASCADE,
    competency_id BIGINT NOT NULL REFERENCES competencies(id) ON DELETE CASCADE,
    PRIMARY KEY (department_id, competency_id)
);

-- communication_profiles: Профили коммуникации
CREATE TABLE communication_profiles (
    id BIGSERIAL PRIMARY KEY,
    department_id BIGINT REFERENCES departments(id),
    role_id BIGINT REFERENCES roles(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- competencies: Компетенции и предметные области
CREATE TABLE competencies (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL REFERENCES enterprises(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- users_competencies: Связь пользователей и компетенций
CREATE TABLE users_competencies (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    competency_id BIGINT NOT NULL REFERENCES competencies(id) ON DELETE CASCADE,
    level INTEGER DEFAULT 1,
    verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, competency_id)
);

-- source_systems: Источник задачи или данных
CREATE TABLE source_systems (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL REFERENCES enterprises(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    type VARCHAR(50),
    config JSONB,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- source_tasks: Исходная задача
CREATE TABLE source_tasks (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL REFERENCES enterprises(id),
    source_system_id BIGINT NOT NULL REFERENCES source_systems(id),
    external_id VARCHAR(255),
    title VARCHAR(500) NOT NULL,
    description TEXT,
    priority INTEGER DEFAULT 0,
    status VARCHAR(50) DEFAULT 'NEW',
    created_by_user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- task_contexts: Структурированный контекст задачи
CREATE TABLE task_contexts (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT NOT NULL REFERENCES source_tasks(id) ON DELETE CASCADE,
    department_id BIGINT REFERENCES departments(id),
    role_id BIGINT REFERENCES roles(id),
    deadline TIMESTAMP WITHOUT TIME ZONE,
    priority INTEGER DEFAULT 0,
    context_data JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- regulations: Нормативные документы
CREATE TABLE regulations (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL REFERENCES enterprises(id),
    title VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    category VARCHAR(100),
    effective_from TIMESTAMP WITHOUT TIME ZONE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- department_regulations: Связка отдела и регламента
CREATE TABLE department_regulations (
    id BIGSERIAL PRIMARY KEY,
    department_id BIGINT NOT NULL REFERENCES departments(id) ON DELETE CASCADE,
    regulation_id BIGINT NOT NULL REFERENCES regulations(id) ON DELETE CASCADE,
    is_mandatory BOOLEAN DEFAULT FALSE,
    priority INTEGER DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(department_id, regulation_id)
);

-- regulation_versions: Версии регламентов
CREATE TABLE regulation_versions (
    id BIGSERIAL PRIMARY KEY,
    regulation_id BIGINT NOT NULL REFERENCES regulations(id) ON DELETE CASCADE,
    version_number VARCHAR(20) NOT NULL,
    title VARCHAR(255),
    content TEXT,
    diff JSONB,
    approved BOOLEAN DEFAULT FALSE,
    approved_by_user_id BIGINT REFERENCES users(id),
    approved_at TIMESTAMP WITHOUT TIME ZONE,
    created_by_user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- uploaded_documents: Файлы
CREATE TABLE uploaded_documents (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT REFERENCES source_tasks(id),
    regulation_id BIGINT REFERENCES regulations(id),
    regulation_version_id BIGINT REFERENCES regulation_versions(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    content_type VARCHAR(255) NOT NULL,
    content BYTEA,
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT,
    storage_path VARCHAR(500),
    bucket_name VARCHAR(100),
    uploaded_by_user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- document_chunks: Чанки для поиска/цитирования
CREATE TABLE document_chunks (
    id BIGSERIAL PRIMARY KEY,
    uploaded_document_id BIGINT NOT NULL REFERENCES uploaded_documents(id) ON DELETE CASCADE,
    chunk_index INTEGER NOT NULL,
    content TEXT NOT NULL,
    tokens_count INTEGER,
    embedding JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- task_routes: Маршруты обработки
CREATE TABLE task_routes (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT NOT NULL REFERENCES source_tasks(id) ON DELETE CASCADE,
    name VARCHAR(255),
    description TEXT,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- route_steps: Шаги маршрута
CREATE TABLE route_steps (
    id BIGSERIAL PRIMARY KEY,
    task_route_id BIGINT NOT NULL REFERENCES task_routes(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    department_id BIGINT REFERENCES departments(id),
    role_id BIGINT REFERENCES roles(id),
    user_id BIGINT REFERENCES users(id),
    sla_hours INTEGER,
    status VARCHAR(50) DEFAULT 'PENDING',
    order_number INTEGER NOT NULL,
    dependencies JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- task_versions: Версии формулировки задачи
CREATE TABLE task_versions (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT NOT NULL REFERENCES source_tasks(id) ON DELETE CASCADE,
    parent_version_id BIGINT REFERENCES task_versions(id),
    author_type VARCHAR(20) NOT NULL CHECK (author_type IN ('SYSTEM', 'USER', 'AI')),
    author_user_id BIGINT REFERENCES users(id),
    title VARCHAR(500),
    content TEXT,
    context JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- task_adaptations: Адаптированные представления
CREATE TABLE task_adaptations (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT NOT NULL REFERENCES source_tasks(id) ON DELETE CASCADE,
    department_id BIGINT REFERENCES departments(id),
    role_id BIGINT REFERENCES roles(id),
    task_version_id BIGINT REFERENCES task_versions(id),
    title VARCHAR(500),
    content TEXT,
    ai_model VARCHAR(100),
    template_name VARCHAR(100),
    context_data JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ai_generation_logs: Журнал вызовов AI
CREATE TABLE ai_generation_logs (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT REFERENCES source_tasks(id),
    task_adaptation_id BIGINT REFERENCES task_adaptations(id),
    regulation_version_id BIGINT REFERENCES regulation_versions(id),
    prompt TEXT,
    input_data JSONB,
    output_data JSONB,
    ai_model VARCHAR(100),
    provider VARCHAR(50),
    tokens_used INTEGER,
    latency_ms INTEGER,
    success BOOLEAN DEFAULT TRUE,
    error_message TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- clarifications: Запросы на уточнение
CREATE TABLE clarifications (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT NOT NULL REFERENCES source_tasks(id) ON DELETE CASCADE,
    department_id BIGINT REFERENCES departments(id),
    role_id BIGINT REFERENCES roles(id),
    user_id BIGINT REFERENCES users(id),
    question TEXT NOT NULL,
    answer TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    answered_at TIMESTAMP WITHOUT TIME ZONE
);

-- acknowledgements: Подтверждения
CREATE TABLE acknowledgements (
    id BIGSERIAL PRIMARY KEY,
    route_step_id BIGINT REFERENCES route_steps(id),
    task_adaptation_id BIGINT REFERENCES task_adaptations(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    department_id BIGINT REFERENCES departments(id),
    status VARCHAR(50) NOT NULL,
    comments TEXT,
    acknowledged_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- execution_results: Фактические результаты
CREATE TABLE execution_results (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT REFERENCES source_tasks(id),
    route_step_id BIGINT REFERENCES route_steps(id),
    user_id BIGINT REFERENCES users(id),
    result_type VARCHAR(50),
    result_data JSONB,
    status VARCHAR(50),
    completed_at TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- events: Бизнес-события
CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    source_task_id BIGINT REFERENCES source_tasks(id),
    route_step_id BIGINT REFERENCES route_steps(id),
    user_id BIGINT REFERENCES users(id),
    department_id BIGINT REFERENCES departments(id),
    event_type VARCHAR(100) NOT NULL,
    event_data JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- audit_logs: Аудит
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor_user_id BIGINT REFERENCES users(id),
    actor_department_id BIGINT REFERENCES departments(id),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT,
    old_values JSONB,
    new_values JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- roi_metrics: Метрики ROI
CREATE TABLE roi_metrics (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT REFERENCES enterprises(id),
    department_id BIGINT REFERENCES departments(id),
    period DATE NOT NULL,
    metric_type VARCHAR(50) NOT NULL,
    metric_value DECIMAL(18,2),
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(department_id, period, metric_type)
);

-- integrations: Настройки интеграций
CREATE TABLE integrations (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL REFERENCES enterprises(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    config_encrypted TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- notifications: Уведомления
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    recipient_user_id BIGINT REFERENCES users(id),
    recipient_department_id BIGINT REFERENCES departments(id),
    recipient_role_id BIGINT REFERENCES roles(id),
    channel VARCHAR(50) NOT NULL,
    template_name VARCHAR(100),
    subject VARCHAR(255),
    body TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    delivered_at TIMESTAMP WITHOUT TIME ZONE,
    read_at TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Indexes
-- ============================================

CREATE INDEX idx_enterprises_code ON enterprises(code);
CREATE INDEX idx_users_enterprise_id ON users(enterprise_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_roles_enterprise_id ON roles(enterprise_id);
CREATE INDEX idx_departments_enterprise_id ON departments(enterprise_id);
CREATE INDEX idx_departments_parent_id ON departments(parent_id);
CREATE INDEX idx_source_systems_enterprise_id ON source_systems(enterprise_id);
CREATE INDEX idx_source_tasks_enterprise_id ON source_tasks(enterprise_id);
CREATE INDEX idx_source_tasks_status ON source_tasks(status);
CREATE INDEX idx_regulations_enterprise_id ON regulations(enterprise_id);
CREATE INDEX idx_uploaded_documents_source_task_id ON uploaded_documents(source_task_id);
CREATE INDEX idx_task_routes_source_task_id ON task_routes(source_task_id);
CREATE INDEX idx_route_steps_task_route_id ON route_steps(task_route_id);
CREATE INDEX idx_task_versions_source_task_id ON task_versions(source_task_id);
CREATE INDEX idx_task_adaptations_source_task_id ON task_adaptations(source_task_id);
CREATE INDEX idx_ai_generation_logs_source_task_id ON ai_generation_logs(source_task_id);
CREATE INDEX idx_clarifications_source_task_id ON clarifications(source_task_id);
CREATE INDEX idx_events_source_task_id ON events(source_task_id);
CREATE INDEX idx_audit_logs_actor_user_id ON audit_logs(actor_user_id);
CREATE INDEX idx_roi_metrics_department_id ON roi_metrics(department_id);

-- ============================================
-- Sample Data (Enterprises, Default Roles)
-- ============================================

INSERT INTO enterprises (name, code, description) 
VALUES ('Main Enterprise', 'MAIN', 'Main enterprise for MVP deployment');

INSERT INTO roles (enterprise_id, name, description, is_system) 
VALUES 
    (currval('enterprises_id_seq'), 'ROLE_ADMIN', 'System administrator', true),
    (currval('enterprises_id_seq'), 'ROLE_USER', 'Standard user', true),
    (currval('enterprises_id_seq'), 'ROLE_MODERATOR', 'Content moderator', true),
    (currval('enterprises_id_seq'), 'ROLE_ANALYST', 'Data analyst', true);
