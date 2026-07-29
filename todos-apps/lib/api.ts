export type TodoApiItem = {
  id: number;
  title: string;
  description: string;
  status: boolean;
  createdAt: string;
  updatedAt: string;
};

export type ApiEnvelope<T> = {
  success: boolean;
  message: string;
  data: T;
};

export type TodoItem = {
  id: number;
  title: string;
  description: string;
  createdAt: string;
  createdAtRaw: string;
  updatedAt: string | null;
  updatedAtRaw: string | null;
  completed: boolean;
  bgColor: string;
  borderColor: string;
};

// used if api available to store color code
export const COLOR_OPTIONS = [
  { bg: 'bg-blue-100 text-blue-900', border: 'border-blue-200' },
  { bg: 'bg-purple-100 text-purple-900', border: 'border-purple-200' },
  { bg: 'bg-amber-100 text-amber-900', border: 'border-amber-200' },
  { bg: 'bg-pink-100 text-pink-900', border: 'border-pink-200' },
  { bg: 'bg-emerald-100 text-emerald-900', border: 'border-emerald-200' },
];

export const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export const formatTime = (value?: string) => {
  if (!value) return 'No time';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return 'No time';
  return date.toLocaleString();
};

export const mapTodo = (todo: TodoApiItem, fallbackColor = COLOR_OPTIONS[0]): TodoItem => ({
  id: todo.id,
  title: todo.title,
  description: todo.description,
  createdAt: formatTime(todo.createdAt),
  createdAtRaw: todo.createdAt,
  updatedAt: formatTime(todo.updatedAt),
  updatedAtRaw: todo.updatedAt ?? null,
  completed: todo.status,
  bgColor: fallbackColor.bg,
  borderColor: fallbackColor.border,
});

export async function requestJson<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(init?.headers || {}),
    },
    ...init,
  });

  const payload = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(payload?.message || 'Request failed');
  }

  return payload as T;
}
