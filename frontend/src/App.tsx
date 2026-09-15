import React, { useEffect } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuthStore } from './store/authStore'
import { Button } from './components/ui/button'
import { Alert, AlertDescription } from './components/ui/alert'
import { Avatar, AvatarImage, AvatarFallback } from './components/ui/avatar'
import { LogOut } from 'lucide-react'
import { ChatWidget } from './components/ui/chat-widget'

// Pages
import DashboardPage from './pages/DashboardPage'
import DocumentsPage from './pages/DocumentsPage'
import SettingsPage from './pages/SettingsPage'
import LoginPage from './pages/LoginPage'
import TasksPage from './pages/TasksPage'
import TaskDetailPage from './pages/TaskDetailPage'
import TaskRoutePage from './pages/TaskRoutePage'
import TaskAdaptationsPage from './pages/TaskAdaptationsPage'
import RegulationsPage from './pages/RegulationsPage'
import ExecutionPage from './pages/ExecutionPage'
import ROIPage from './pages/ROIPage'
import AnalyticsPage from './pages/AnalyticsPage'
import ImportPage from './pages/ImportPage'
import DepartmentsPage from './pages/DepartmentsPage'
import IntegrationsPage from './pages/IntegrationsPage'
import AdminPage from './pages/AdminPage'

// Protected Route Component
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuthStore()

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
          <p className="text-gray-600">Проверка авторизации...</p>
        </div>
      </div>
    )
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return <>{children}</>
}

const App: React.FC = () => {
  const { user, logout, error } = useAuthStore()

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <div className="flex-shrink-0 flex items-center">
                <span className="text-xl font-bold text-gray-900">InduTranslator</span>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              {error && (
                <Alert variant="destructive" className="mb-0 mx-4">
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}
              {user && (
                <div className="flex items-center space-x-3">
                  <Avatar>
                    <AvatarImage src="/avatar.png" alt={user.username} />
                    <AvatarFallback>{user.username.charAt(0)}</AvatarFallback>
                  </Avatar>
                  <span className="text-sm font-medium text-gray-700">{user.username}</span>
                  <Button variant="ghost" size="sm" onClick={logout}>
                    <LogOut className="h-4 w-4" />
                  </Button>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/dashboard" element={
            <ProtectedRoute>
              <DashboardPage />
            </ProtectedRoute>
          } />
          <Route path="/tasks" element={
            <ProtectedRoute>
              <TasksPage />
            </ProtectedRoute>
          } />
          <Route path="/tasks/:id" element={
            <ProtectedRoute>
              <TaskDetailPage />
            </ProtectedRoute>
          } />
          <Route path="/tasks/:taskId/routes/:routeId" element={
            <ProtectedRoute>
              <TaskRoutePage />
            </ProtectedRoute>
          } />
          <Route path="/adaptations" element={
            <ProtectedRoute>
              <TaskAdaptationsPage />
            </ProtectedRoute>
          } />
          <Route path="/regulations" element={
            <ProtectedRoute>
              <RegulationsPage />
            </ProtectedRoute>
          } />
          <Route path="/executions" element={
            <ProtectedRoute>
              <ExecutionPage />
            </ProtectedRoute>
          } />
          <Route path="/roi" element={
            <ProtectedRoute>
              <ROIPage />
            </ProtectedRoute>
          } />
          <Route path="/analytics" element={
            <ProtectedRoute>
              <AnalyticsPage />
            </ProtectedRoute>
          } />
          <Route path="/import" element={
            <ProtectedRoute>
              <ImportPage />
            </ProtectedRoute>
          } />
          <Route path="/departments" element={
            <ProtectedRoute>
              <DepartmentsPage />
            </ProtectedRoute>
          } />
          <Route path="/integrations" element={
            <ProtectedRoute>
              <IntegrationsPage />
            </ProtectedRoute>
          } />
          <Route path="/admin" element={
            <ProtectedRoute>
              <AdminPage />
            </ProtectedRoute>
          } />
          <Route path="/documents" element={
            <ProtectedRoute>
              <DocumentsPage />
            </ProtectedRoute>
          } />
          <Route path="/settings" element={
            <ProtectedRoute>
              <SettingsPage />
            </ProtectedRoute>
          } />
        </Routes>
      </div>

      {/* Chat Widget - Bottom Right Corner */}
      <ChatWidget />
    </div>
  )
}

export default App
