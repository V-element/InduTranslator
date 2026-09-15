import React, { useState, useRef, useEffect } from 'react'
import { MessageSquare, X, Send, Settings } from 'lucide-react'
import { Button } from './button'
import { Input } from './input'
import { Card, CardContent, CardHeader } from './card'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from './dialog'
import { Label } from './label'

interface ChatMessage {
  id: string
  text: string
  sender: 'user' | 'ai'
}

interface ChatConfig {
  apiKey: string
  model: string
}

const DEFAULT_CONFIG: ChatConfig = {
  apiKey: '',
  model: 'GigaChat-Pro'
}

const ChatWidget: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false)
  const [messages, setMessages] = useState<ChatMessage[]>([
    { id: '1', text: 'Привет! Я GigaChat AI-ассистент. Чем могу помочь?', sender: 'ai' }
  ])
  const [inputValue, setInputValue] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [showConfig, setShowConfig] = useState(false)
  const [config, setConfig] = useState<ChatConfig>(() => {
    const saved = localStorage.getItem('chatConfig')
    return saved ? JSON.parse(saved) : DEFAULT_CONFIG
  })
  const messagesEndRef = useRef<HTMLDivElement>(null)

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  const handleSend = async () => {
    if (!inputValue.trim()) return

    const userMessage: ChatMessage = {
      id: Date.now().toString(),
      text: inputValue,
      sender: 'user'
    }

    setMessages(prev => [...prev, userMessage])
    setInputValue('')
    setIsLoading(true)

    try {
      if (!config.apiKey) {
        throw new Error('API ключ не настроен. Нажмите на значок настроек (⚙️) в чате, чтобы добавить ключ GigaChat.')
      }

      // Build messages array for API
      const messagesForAPI = [
        { role: 'system', content: 'Ты - полезный AI-ассистент на русском языке. Отвечай кратко и по делу.' },
        ...messages.map(msg => ({
          role: msg.sender === 'user' ? 'user' : 'assistant',
          content: msg.text
        })),
        { role: 'user', content: inputValue }
      ]

      // Call backend API which proxies to GigaChat
      const response = await fetch('/api/ai/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          prompt: inputValue,
          apiKey: config.apiKey,
          model: config.model,
          temperature: 0.7,
          topP: 1.0,
          maxTokens: 2048
        })
      })

      // Handle non-JSON responses
      if (!response.ok) {
        let errorMessage = 'Ошибка API GigaChat'
        try {
          const contentType = response.headers.get('content-type')
          if (contentType && contentType.includes('application/json')) {
            const errorData = await response.json()
            errorMessage = errorData.message || errorData.detail || errorData.error || errorMessage
          } else {
            // Try to get text content for non-JSON responses
            const textError = await response.text()
            errorMessage = `HTTP ${response.status}: ${textError || response.statusText}`
          }
        } catch (e) {
          errorMessage = `HTTP ${response.status}: ${response.statusText}`
        }
        throw new Error(errorMessage)
      }

      const data = await response.json()
      const aiResponse = data.choices?.[0]?.text || data.choices?.[0]?.message?.content || 'No response'

      const aiMessage: ChatMessage = {
        id: (Date.now() + 1).toString(),
        text: aiResponse,
        sender: 'ai'
      }
      setMessages(prev => [...prev, aiMessage])
    } catch (error: any) {
      const errorMessage: ChatMessage = {
        id: (Date.now() + 1).toString(),
        text: `Ошибка: ${error.message || 'Неизвестная ошибка'}`,
        sender: 'ai'
      }
      setMessages(prev => [...prev, errorMessage])
    } finally {
      setIsLoading(false)
    }
  }

  const handleSaveConfig = (newConfig: ChatConfig) => {
    setConfig(newConfig)
    localStorage.setItem('chatConfig', JSON.stringify(newConfig))
    setShowConfig(false)
  }

  const ConfigDialog: React.FC<{ open: boolean; onClose: () => void }> = ({ open, onClose }) => {
    const [tempConfig, setTempConfig] = useState(config)

    return (
      <Dialog open={open} onOpenChange={onClose}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Настройки GigaChat</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="apiKey">GigaChat API Key</Label>
              <Input
                id="apiKey"
                type="password"
                value={tempConfig.apiKey}
                onChange={(e) => setTempConfig({ ...tempConfig, apiKey: e.target.value })}
                placeholder="Bearer token..."
              />
              <p className="text-xs text-gray-500">
                Ключ хранится локально в браузере и не отправляется на наш сервер.
              </p>
            </div>
            <div className="space-y-2">
              <Label htmlFor="model">Модель</Label>
              <Input
                id="model"
                value={tempConfig.model}
                onChange={(e) => setTempConfig({ ...tempConfig, model: e.target.value })}
                placeholder="GigaChat-Pro"
              />
            </div>
            <Button onClick={() => handleSaveConfig(tempConfig)} className="w-full">
              Сохранить
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    )
  }

  return (
    <div className="fixed bottom-6 right-6 z-50">
      {isOpen && (
        <Card className="w-96 mb-4 shadow-lg flex flex-col max-h-[80vh]">
          <CardHeader className="pb-3">
            <div className="flex justify-between items-center">
              <h3 className="font-semibold">GigaChat Assistant</h3>
              <div className="flex gap-2">
                <Dialog open={showConfig} onOpenChange={setShowConfig}>
                  <DialogTrigger asChild>
                    <Button variant="ghost" size="sm">
                      <Settings className="h-4 w-4" />
                    </Button>
                  </DialogTrigger>
                </Dialog>
                <Button variant="ghost" size="sm" onClick={() => setIsOpen(false)}>
                  <X className="h-4 w-4" />
                </Button>
              </div>
            </div>
          </CardHeader>
          <CardContent className="flex-1 overflow-y-auto px-4 py-2">
            <div className="space-y-4">
              {messages.map(msg => (
                <div
                  key={msg.id}
                  className={`flex ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                    className={`max-w-[80%] p-3 rounded-lg ${
                      msg.sender === 'user'
                        ? 'bg-primary text-primary-foreground'
                        : 'bg-gray-100 text-gray-800'
                    }`}
                  >
                    {msg.text}
                  </div>
                </div>
              ))}
              {isLoading && (
                <div className="flex justify-start">
                  <div className="bg-gray-100 text-gray-800 p-3 rounded-lg">
                    <span className="animate-pulse">...</span>
                  </div>
                </div>
              )}
              <div ref={messagesEndRef} />
            </div>
          </CardContent>
          <div className="p-3 border-t">
            <div className="flex gap-2">
              <Input
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault()
                    handleSend()
                  }
                }}
                placeholder="Сообщение..."
                className="flex-1 text-sm"
                disabled={isLoading}
              />
              <Button size="sm" onClick={handleSend} disabled={isLoading}>
                <Send className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </Card>
      )}
      <Button
        variant="default"
        onClick={() => setIsOpen(!isOpen)}
        className={`rounded-full w-14 h-14 shadow-lg transition-all duration-300 ${
          isOpen ? 'bg-red-500 hover:bg-red-600' : 'bg-green-500 hover:bg-green-600'
        }`}
      >
        {isOpen ? <X className="h-6 w-6" /> : <MessageSquare className="h-6 w-6" />}
      </Button>
      <ConfigDialog open={showConfig} onClose={() => setShowConfig(false)} />
    </div>
  )
}

export { ChatWidget }
