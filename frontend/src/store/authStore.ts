import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { authAPI } from '../services/api'

interface User {
  id: number
  username: string
  email: string
  firstName?: string
  lastName?: string
  phone?: string
  active?: boolean
  departmentIds?: number[]
  roleIds?: number[]
  enterpriseId?: number
  createdAt?: string
}

interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
  login: (credentials: { username: string; password: string }) => Promise<void>
  register: (credentials: {
    username: string
    password: string
    email: string
    firstName?: string
    lastName?: string
  }) => Promise<void>
  logout: () => void
  fetchUserProfile: () => Promise<void>
  clearError: () => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,

      login: async (credentials) => {
        set({ isLoading: true, error: null })
        try {
          const response = await authAPI.login(credentials)
          console.log('Login response:', response)
          authAPI.saveToken(response.token)
          authAPI.saveUser(response.user)
          set({
            token: response.token,
            user: response.user,
            isAuthenticated: true,
            isLoading: false,
            error: null,
          })
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Login failed'
          set({ isLoading: false, error: errorMessage })
          throw new Error(errorMessage)
        }
      },

      register: async (credentials) => {
        set({ isLoading: true, error: null })
        try {
          const response = await authAPI.register(credentials)
          authAPI.saveToken(response.token)
          authAPI.saveUser(response.user)
          set({
            token: response.token,
            user: response.user,
            isAuthenticated: true,
            isLoading: false,
            error: null,
          })
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Registration failed'
          set({ isLoading: false, error: errorMessage })
          throw new Error(errorMessage)
        }
      },

      logout: () => {
        authAPI.logout()
        set({ user: null, token: null, isAuthenticated: false, error: null })
      },

      fetchUserProfile: async () => {
        const token = get().token
        console.log('fetchUserProfile - token:', token)
        if (!token) return

        set({ isLoading: true, error: null })
        try {
          const user = await authAPI.me()
          console.log('fetchUserProfile - user:', user)
          authAPI.saveUser(user)
          set({ user, isLoading: false, error: null })
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Failed to fetch profile'
          console.error('fetchUserProfile error:', error)
          set({ isLoading: false, error: errorMessage })
          throw new Error(errorMessage)
        }
      },

      clearError: () => set({ error: null }),
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)
