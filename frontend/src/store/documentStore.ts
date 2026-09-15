import { create } from 'zustand'

interface Document {
  id: number
  title: string
  description?: string
  contentType: string
  fileName: string
  fileSize?: number
  storagePath?: string
  bucketName?: string
  createdAt?: string
  updatedAt?: string
}

interface DocumentState {
  documents: Document[]
  selectedDocument: Document | null
  addDocument: (document: Document) => void
  removeDocument: (id: number) => void
  selectDocument: (document: Document) => void
  clearSelectedDocument: () => void
}

export const useDocumentStore = create<DocumentState>()((set) => ({
  documents: [],
  selectedDocument: null,

  addDocument: (document) => set((state) => ({
    documents: [...state.documents, document],
  })),

  removeDocument: (id) => set((state) => ({
    documents: state.documents.filter((doc) => doc.id !== id),
    selectedDocument: state.selectedDocument?.id === id ? null : state.selectedDocument,
  })),

  selectDocument: (document) => set({ selectedDocument: document }),

  clearSelectedDocument: () => set({ selectedDocument: null }),
}))
