import { api } from './api'

export interface RegulationDto {
  id: number
  code: string
  title: string
  description?: string
  departmentId?: number
  departmentName?: string
  routeId?: number
  routeName?: string
  version: string
  content: string
  isActive: boolean
  effectiveDate?: string
  createdAt?: string
  createdBy?: string
}

export interface RegulationVersionDto {
  id: number
  regulationId: number
  version: string
  content: string
  changeDescription?: string
  createdAt?: string
  createdBy?: string
}

export interface RegulationResponse {
  content: RegulationDto[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export const RegulationAPI = {
  getAllRegulations: async (
    page: number = 0,
    size: number = 10,
    filter?: string,
    departmentFilter?: string,
    activeFilter?: boolean
  ): Promise<RegulationResponse> => {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    if (filter) params.append('filter', filter)
    if (departmentFilter && departmentFilter !== 'all') params.append('departmentId', departmentFilter)
    if (activeFilter !== undefined) params.append('active', activeFilter.toString())
    
    const response = await api.get<RegulationResponse>(`/regulations?${params.toString()}`)
    return response.data
  },

  getRegulationById: async (id: number): Promise<RegulationDto> => {
    const response = await api.get<RegulationDto>(`/regulations/${id}`)
    return response.data
  },

  getRegulationVersions: async (regulationId: number): Promise<RegulationVersionDto[]> => {
    const response = await api.get<RegulationVersionDto[]>(`/regulations/${regulationId}/versions`)
    return response.data
  },

  getRegulationVersion: async (regulationId: number, version: string): Promise<RegulationVersionDto> => {
    const response = await api.get<RegulationVersionDto>(`/regulations/${regulationId}/versions/${version}`)
    return response.data
  },

  compareVersions: async (regulationId: number, version1: string, version2: string): Promise<any> => {
    const response = await api.post<any>(`/regulations/${regulationId}/compare`, { version1, version2 })
    return response.data
  },

  createRegulation: async (regulation: Partial<RegulationDto>): Promise<RegulationDto> => {
    const response = await api.post<RegulationDto>('/regulations', regulation)
    return response.data
  },

  updateRegulation: async (id: number, regulation: Partial<RegulationDto>): Promise<RegulationDto> => {
    const response = await api.put<RegulationDto>(`/regulations/${id}`, regulation)
    return response.data
  },

  activateRegulation: async (id: number): Promise<RegulationDto> => {
    const response = await api.patch<RegulationDto>(`/regulations/${id}/activate`)
    return response.data
  },

  deactivateRegulation: async (id: number): Promise<RegulationDto> => {
    const response = await api.patch<RegulationDto>(`/regulations/${id}/deactivate`)
    return response.data
  },

  deleteRegulation: async (id: number): Promise<void> => {
    await api.delete(`/regulations/${id}`)
  }
}
