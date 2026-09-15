import { api } from './api'

export interface DepartmentDto {
  id: number
  name: string
  code?: string
  description?: string
  parentId?: number
  parentName?: string
  parentCode?: string
  enterpriseId?: number
  enterpriseName?: string
  contactEmail?: string
  contactPhone?: string
  communicationProfile?: 'standard' | 'urgent' | 'restricted'
  competencies?: string[]
  regulations?: RegulationReferenceDto[]
  isActive: boolean
  createdAt?: string
  updatedAt?: string
}

export interface RegulationReferenceDto {
  regulationId: number
  regulationCode: string
  regulationTitle: string
  role: 'primary' | 'secondary'
  required: boolean
}

export interface DepartmentResponse {
  content: DepartmentDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export const DepartmentAPI = {
  getAllDepartments: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    activeFilter?: boolean,
    enterpriseId?: number
  ): Promise<DepartmentResponse> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (activeFilter !== undefined) params.append('active', activeFilter.toString())
    if (enterpriseId) params.append('enterpriseId', enterpriseId.toString())
    
    const response = await api.get<DepartmentResponse>(`/departments?${params.toString()}`)
    return response.data
  },

  getDepartmentById: async (id: number): Promise<DepartmentDto> => {
    const response = await api.get<DepartmentDto>(`/departments/${id}`)
    return response.data
  },

  getDepartmentHierarchy: async (enterpriseId?: number): Promise<DepartmentDto[]> => {
    const params = new URLSearchParams()
    if (enterpriseId) params.append('enterpriseId', enterpriseId.toString())
    
    const response = await api.get<DepartmentDto[]>(`/departments/tree${params.toString() ? '?' + params.toString() : ''}`)
    return response.data
  },

  getDepartmentTasks: async (departmentId: number, page: number = 0, size: number = 10): Promise<any> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    const response = await api.get(`/departments/${departmentId}/tasks?${params.toString()}`)
    return response.data
  },

  createDepartment: async (department: Partial<DepartmentDto>): Promise<DepartmentDto> => {
    const response = await api.post<DepartmentDto>('/departments', department)
    return response.data
  },

  updateDepartment: async (id: number, department: Partial<DepartmentDto>): Promise<DepartmentDto> => {
    const response = await api.put<DepartmentDto>(`/departments/${id}`, department)
    return response.data
  },

  deleteDepartment: async (id: number): Promise<void> => {
    await api.delete(`/departments/${id}`)
  },

  activateDepartment: async (id: number): Promise<DepartmentDto> => {
    const response = await api.patch<DepartmentDto>(`/departments/${id}/activate`)
    return response.data
  },

  deactivateDepartment: async (id: number): Promise<DepartmentDto> => {
    const response = await api.patch<DepartmentDto>(`/departments/${id}/deactivate`)
    return response.data
  }
}
