export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  enabled?: boolean;
  roles?: Role[];
}

export interface Role {
  id: number;
  name: string;
}

export interface Document {
  id: number;
  title: string;
  description?: string;
  contentType: string;
  fileName: string;
  fileSize?: number;
  storagePath?: string;
  bucketName?: string;
  createdAt?: string;
  updatedAt?: string;
  userId?: number;
}

export interface AIConfiguration {
  id?: number;
  provider: string;
  apiKey: string;
  baseUrl: string;
  model: string;
  maxTokens: number;
  temperature: number;
  topP: number;
  active: boolean;
}

export interface AIRequest {
  prompt: string;
  model?: string;
  maxTokens?: number;
  temperature?: number;
  topP?: number;
  parameters?: Record<string, unknown>;
}

export interface AIResponse {
  id: string;
  object: string;
  created: number;
  model: string;
  choices: Choice[];
  usage: Usage;
  metadata?: Record<string, unknown>;
}

export interface Choice {
  index: number;
  text: string;
  finishReason: string;
}

export interface Usage {
  promptTokens: number;
  completionTokens: number;
  totalTokens: number;
}

export interface ApiResponse<T> {
  data?: T;
  error?: string;
  message?: string;
}
