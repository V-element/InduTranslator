import { create } from 'zustand'
import { devtools, persist } from 'zustand/middleware'

interface FilterState {
  // Common filters
  periodStart: string | null
  periodEnd: string | null
  departmentId: number | null
  sourceSystemId: number | null
  status: string | null
  
  // Task-specific filters
  priorityMin: number
  priorityMax: number
  searchTerm: string
  
  // Actions
  setPeriod: (start: string | null, end: string | null) => void
  setDepartment: (id: number | null) => void
  setStatus: (status: string | null) => void
  setSearchTerm: (term: string) => void
  setPriorityRange: (min: number, max: number) => void
  resetFilters: () => void
  updateFilter: (key: string, value: any) => void
}

export const useFilterStore = create<FilterState>()(
  devtools(
    persist(
      (set) => ({
        periodStart: null,
        periodEnd: null,
        departmentId: null,
        sourceSystemId: null,
        status: null,
        priorityMin: 0,
        priorityMax: 10,
        searchTerm: '',

        setPeriod: (start, end) => set({ periodStart: start, periodEnd: end }),
        setDepartment: (id) => set({ departmentId: id }),
        setStatus: (status) => set({ status: status }),
        setSearchTerm: (term) => set({ searchTerm: term }),
        setPriorityRange: (min, max) => set({ priorityMin: min, priorityMax: max }),
        resetFilters: () => set({
          periodStart: null,
          periodEnd: null,
          departmentId: null,
          sourceSystemId: null,
          status: null,
          priorityMin: 0,
          priorityMax: 10,
          searchTerm: '',
        }),
        updateFilter: (key, value) => set({ [key]: value }),
      }),
      {
        name: 'filter-storage',
        partialize: (state) => ({
          periodStart: state.periodStart,
          periodEnd: state.periodEnd,
          departmentId: state.departmentId,
          status: state.status,
          searchTerm: state.searchTerm,
        }),
      }
    )
  )
)
