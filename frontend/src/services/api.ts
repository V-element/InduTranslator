import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || '/api'

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
  timeout: 10000, // 10 seconds timeout
})

// Add request interceptor for auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth-token')
    console.log('API Request:', config.url, 'Token present:', !!token)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('Authorization header set')
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.code === 'ECONNABORTED') {
      console.error('Request timeout')
    }
    if (error.response?.status === 401) {
      localStorage.removeItem('auth-token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: {
    id: number
    username: string
    email: string
  }
}

export const authAPI = {
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post<LoginResponse>('/auth/login', credentials)
    return response.data
  },
  register: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post<LoginResponse>('/auth/register', credentials)
    return response.data
  },
  me: async (): Promise<LoginResponse['user']> => {
    const userStr = localStorage.getItem('auth-user')
    const username = userStr ? JSON.parse(userStr).username : 'admin'
    const response = await api.get<LoginResponse['user']>(`/auth/profile?username=${username}`)
    return response.data
  },
  saveToken: (token: string): void => {
    localStorage.setItem('auth-token', token)
  },
  saveUser: (user: LoginResponse['user']): void => {
    localStorage.setItem('auth-user', JSON.stringify(user))
  },
  logout: (): void => {
    localStorage.removeItem('auth-token')
    localStorage.removeItem('auth-user')
  },
}
