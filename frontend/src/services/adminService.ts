import { api } from './api'

export interface UserDto {
  id: number
  username: string
  email: string
  firstName?: string
  lastName?: string
  phone?: string
  active?: boolean
  departmentIds?: number[]
  departmentNames?: string[]
  roleIds?: number[]
  roleNames?: string[]
  enterpriseId?: number
  enterpriseName?: string
  lastLoginAt?: string
  createdAt?: string
  updatedAt?: string
}

export interface RoleDto {
  id: number
  name: string
  code: string
  description?: string
  permissions?: string[]
  isActive: boolean
  createdAt?: string
  updatedAt?: string
}

export interface DictionaryDto {
  id: number
  name: string
  code: string
  items: DictionaryItemDto[]
  isActive: boolean
}

export interface DictionaryItemDto {
  id: number
  code: string
  value: string
  description?: string
  isActive: boolean
}

export interface RoiCoefficientDto {
  id: number
  departmentId: number
  departmentName?: string
  metricType: string
  baselineValue: number
  targetValue: number
  coefficient: number
  isActive: boolean
  effectiveDate?: string
}

export interface AdminResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export const AdminAPI = {
  // Users
  getUsers: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    activeFilter?: boolean,
    departmentId?: number
  ): Promise<AdminResponse<UserDto> | UserDto[]> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (activeFilter !== undefined) params.append('active', activeFilter.toString())
    if (departmentId) params.append('departmentId', departmentId.toString())
    
    const response = await api.get<AdminResponse<UserDto> | UserDto[]>(`/admin/users?${params.toString()}`)
    return response.data
  },

  getUserById: async (id: number): Promise<UserDto> => {
    const response = await api.get<UserDto>(`/admin/users/${id}`)
    return response.data
  },

  createUser: async (user: Partial<UserDto>): Promise<UserDto> => {
    const response = await api.post<UserDto>('/admin/users', user)
    return response.data
  },

  updateUser: async (id: number, user: Partial<UserDto>): Promise<UserDto> => {
    const response = await api.put<UserDto>(`/admin/users/${id}`, user)
    return response.data
  },

  deleteUser: async (id: number): Promise<void> => {
    await api.delete(`/admin/users/${id}`)
  },

  activateUser: async (id: number): Promise<UserDto> => {
    const response = await api.patch<UserDto>(`/admin/users/${id}/activate`)
    return response.data
  },

  deactivateUser: async (id: number): Promise<UserDto> => {
    const response = await api.patch<UserDto>(`/admin/users/${id}/deactivate`)
    return response.data
  },

  // Roles
  getRoles: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    activeFilter?: boolean
  ): Promise<AdminResponse<RoleDto> | RoleDto[]> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (activeFilter !== undefined) params.append('active', activeFilter.toString())
    
    const response = await api.get<AdminResponse<RoleDto> | RoleDto[]>(`/admin/roles?${params.toString()}`)
    return response.data
  },

  getRoleById: async (id: number): Promise<RoleDto> => {
    const response = await api.get<RoleDto>(`/admin/roles/${id}`)
    return response.data
  },

  createRole: async (role: Partial<RoleDto>): Promise<RoleDto> => {
    const response = await api.post<RoleDto>('/admin/roles', role)
    return response.data
  },

  updateRole: async (id: number, role: Partial<RoleDto>): Promise<RoleDto> => {
    const response = await api.put<RoleDto>(`/admin/roles/${id}`, role)
    return response.data
  },

  deleteRole: async (id: number): Promise<void> => {
    await api.delete(`/admin/roles/${id}`)
  },

  // Dictionaries
  getDictionaries: async (): Promise<DictionaryDto[]> => {
    const response = await api.get<DictionaryDto[]>('/admin/dictionaries')
    return response.data
  },

  getDictionaryById: async (id: number): Promise<DictionaryDto> => {
    const response = await api.get<DictionaryDto>(`/admin/dictionaries/${id}`)
    return response.data
  },

  updateDictionary: async (id: number, dictionary: Partial<DictionaryDto>): Promise<DictionaryDto> => {
    const response = await api.put<DictionaryDto>(`/admin/dictionaries/${id}`, dictionary)
    return response.data
  },

  // ROI Coefficients
  getRoiCoefficients: async (
    page: number = 0,
    size: number = 10,
    departmentId?: number
  ): Promise<AdminResponse<RoiCoefficientDto> | RoiCoefficientDto[]> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (departmentId) params.append('departmentId', departmentId.toString())
    
    const response = await api.get<AdminResponse<RoiCoefficientDto> | RoiCoefficientDto[]>(`/admin/roi-coefficients?${params.toString()}`)
    return response.data
  },

  updateRoiCoefficient: async (id: number, coefficient: Partial<RoiCoefficientDto>): Promise<RoiCoefficientDto> => {
    const response = await api.put<RoiCoefficientDto>(`/admin/roi-coefficients/${id}`, coefficient)
    return response.data
  },

  // Configs
  getAIProviderConfigs: async (): Promise<any[]> => {
    const response = await api.get('/admin/ai-configs')
    return response.data
  },

  updateAIProviderConfig: async (id: number, config: any): Promise<any> => {
    const response = await api.put(`/admin/ai-configs/${id}`, config)
    return response.data
  }
}
