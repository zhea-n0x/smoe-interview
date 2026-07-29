'use client';

import React, { useEffect, useRef, useState } from 'react';
import { Search, Plus, X } from 'lucide-react';
import TodoCard from '@/components/TodoCard';
import { COLOR_OPTIONS, type ApiEnvelope, type TodoApiItem, type TodoItem, mapTodo, requestJson } from '@/lib/api';

type ModalState = {
  type: 'success' | 'error';
  title: string;
  message: string;
};

export default function TodoApp() {
  const [todos, setTodos] = useState<TodoItem[]>([]);
  const [activeTab, setActiveTab] = useState<'all' | 'active' | 'completed'>('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [modal, setModal] = useState<ModalState | null>(null);

  const [editingId, setEditingId] = useState<number | null>(null);
  const [editTitle, setEditTitle] = useState('');
  const [editDescription, setEditDescription] = useState('');

  const [isAdding, setIsAdding] = useState(false);
  const [newTitle, setNewTitle] = useState('');
  const [newDescription, setNewDescription] = useState('');
  const [selectedColor, setSelectedColor] = useState(COLOR_OPTIONS[0]);
  const editingCardRef = useRef<HTMLDivElement | null>(null);

  const loadTodos = async () => {
    try {
      setLoading(true);
      setError(null);
      const payload = await requestJson<ApiEnvelope<TodoApiItem[]>>('/api/todos');
      setTodos(payload.data.map((todo) => mapTodo(todo)));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to load todos');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadTodos();
  }, []);

  useEffect(() => {
    if (editingId === null) return;

    const handlePointerDown = (event: MouseEvent) => {
      if (editingCardRef.current && !editingCardRef.current.contains(event.target as Node)) {
        setEditingId(null);
      }
    };

    document.addEventListener('mousedown', handlePointerDown);
    return () => document.removeEventListener('mousedown', handlePointerDown);
  }, [editingId]);

  useEffect(() => {
    if (!modal) return;
    const timer = window.setTimeout(() => setModal(null), 2200);
    return () => window.clearTimeout(timer);
  }, [modal]);

  const handleDelete = async (id: number) => {
    try {
      setError(null);
      await requestJson<ApiEnvelope<null>>(`/api/todos/${id}`, { method: 'DELETE' });
      setTodos((prev) => prev.filter((todo) => todo.id !== id));
      setModal({ type: 'success', title: 'Deleted', message: 'Todo deleted successfully.' });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to delete todo');
      setModal({ type: 'error', title: 'Delete failed', message: err instanceof Error ? err.message : 'Unable to delete todo' });
    }
  };

  const handleStartEdit = (todo: TodoItem) => {
    setEditingId(todo.id);
    setEditTitle(todo.title);
    setEditDescription(todo.description);
  };

  const handleSaveEdit = async (id: number) => {
    try {
      setError(null);
      const payload = await requestJson<ApiEnvelope<TodoApiItem>>(`/api/todos/${id}`, {
        method: 'PUT',
        body: JSON.stringify({
          title: editTitle,
          description: editDescription,
          status: todos.find((todo) => todo.id === id)?.completed ?? false,
        }),
      });

      setTodos((prev) =>
        prev.map((todo) => {
          if (todo.id !== id) return todo;
          return {
            ...todo,
            ...mapTodo(payload.data, { bg: todo.bgColor, border: todo.borderColor }),
          };
        })
      );
      setEditingId(null);
      setModal({ type: 'success', title: 'Updated', message: 'Todo edited successfully.' });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to update todo');
      setModal({ type: 'error', title: 'Update failed', message: err instanceof Error ? err.message : 'Unable to update todo' });
    }
  };

  const handleToggleComplete = async (id: number) => {
    const target = todos.find((todo) => todo.id === id);
    if (!target) return;

    try {
      setError(null);
      const payload = await requestJson<ApiEnvelope<TodoApiItem>>(`/api/todos/${id}`, {
        method: 'PUT',
        body: JSON.stringify({
          title: target.title,
          description: target.description,
          status: !target.completed,
        }),
      });

      setTodos((prev) =>
        prev.map((todo) => {
          if (todo.id !== id) return todo;
          return {
            ...todo,
            ...mapTodo(payload.data, { bg: todo.bgColor, border: todo.borderColor }),
            completed: payload.data.status,
          };
        })
      );

      if (payload.data.status) {
        await loadTodos();
        setModal({ type: 'success', title: 'Task completed', message: 'Nice work! The task is now marked completed.' });
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to toggle todo');
      setModal({ type: 'error', title: 'Update failed', message: err instanceof Error ? err.message : 'Unable to toggle todo' });
    }
  };

  const handleAddSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!newTitle.trim()) return;

    try {
      setError(null);
      const payload = await requestJson<ApiEnvelope<TodoApiItem>>('/api/todos', {
        method: 'POST',
        body: JSON.stringify({
          title: newTitle.trim(),
          description: newDescription || 'No description provided.',
          status: false,
        }),
      });

      const newTask: TodoItem = {
        ...mapTodo(payload.data, selectedColor)
      };

      setTodos((prev) => [newTask, ...prev]);
      setNewTitle('');
      setNewDescription('');
      setSelectedColor(COLOR_OPTIONS[0]);
      setIsAdding(false);
      setModal({ type: 'success', title: 'Added', message: 'Todo added successfully.' });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to create todo');
      setModal({ type: 'error', title: 'Add failed', message: err instanceof Error ? err.message : 'Unable to create todo' });
    }
  };

  const filteredTodos = todos.filter((todo) => {
    const matchesTab = activeTab === 'completed'
      ? todo.completed
      : activeTab === 'active'
        ? !todo.completed
        : true;
    const matchesSearch =
      todo.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      todo.description.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesTab && matchesSearch;
  });

  return (
    <div className="min-h-screen bg-slate-50 p-6 md:p-10 font-sans text-slate-800">
      <div className="max-w-6xl mx-auto space-y-6">
        
        <header className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold text-slate-900">Todo List</h1>
        </header>

        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">

          <div className="flex flex-wrap items-center gap-3 w-full sm:w-auto">
            <div className="relative flex-1 sm:w-64">
              <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
              <input
                type="text"
                placeholder="Search List"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-9 pr-4 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              />
            </div>

            <button
              onClick={() => setIsAdding(!isAdding)}
              className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition shadow-sm ${
                isAdding
                  ? 'bg-slate-200 text-slate-700 hover:bg-slate-300'
                  : 'bg-blue-600 text-white hover:bg-blue-700'
              }`}
            >
              {isAdding ? (
                <>
                  <X className="w-4 h-4" /> Close Form
                </>
              ) : (
                <>
                  <Plus className="w-4 h-4" /> Add New List
                </>
              )}
            </button>
          </div>
        </div>

        {isAdding && (
          <form
            onSubmit={handleAddSubmit}
            className="bg-white p-5 rounded-2xl border border-slate-200 shadow-md space-y-4 transition-all duration-300 animate-in fade-in slide-in-from-top-2"
          >
            <div className="flex items-center justify-between border-b border-slate-100 pb-2">
              <h3 className="text-sm font-bold text-slate-800">Create New Task</h3>
              <button
                type="button"
                onClick={() => setIsAdding(false)}
                className="text-slate-400 hover:text-slate-600"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1">Title</label>
                <input
                  type="text"
                  placeholder="e.g. Beli Ayam Goreng Tepung"
                  value={newTitle}
                  onChange={(e) => setNewTitle(e.target.value)}
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-600 mb-1">Description</label>
              <textarea
                rows={2}
                placeholder="Beli ayam di KFC/Richeese"
                value={newDescription}
                onChange={(e) => setNewDescription(e.target.value)}
                className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
              />
            </div>

            <div className="flex items-center justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setIsAdding(false)}
                className="px-4 py-2 text-xs font-medium text-slate-600 hover:text-slate-800 transition"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-4 py-2 text-xs font-medium bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition shadow-sm"
              >
                Save Task
              </button>
            </div>
          </form>
        )}

        <div className="flex flex-wrap gap-2 border-b border-slate-200 pb-2">
          <button
            onClick={() => setActiveTab('all')}
            className={`px-4 py-1.5 rounded-md text-sm font-medium transition ${
              activeTab === 'all'
                ? 'bg-white text-slate-900 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            }`}
          >
            All Tasks
          </button>
          <button
            onClick={() => setActiveTab('active')}
            className={`px-4 py-1.5 rounded-md text-sm font-medium transition ${
              activeTab === 'active'
                ? 'bg-white text-slate-900 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            }`}
          >
            Active Task
          </button>
          <button
            onClick={() => setActiveTab('completed')}
            className={`px-4 py-1.5 rounded-md text-sm font-medium transition ${
              activeTab === 'completed'
                ? 'bg-white text-slate-900 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            }`}
          >
            Completed
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredTodos.map((todo) => {
            const isEditing = editingId === todo.id;

            return (
              <TodoCard
                key={todo.id}
                cardRef={isEditing ? editingCardRef : undefined}
                todo={todo}
                isEditing={isEditing}
                editTitle={editTitle}
                editDescription={editDescription}
                onStartEdit={handleStartEdit}
                onDelete={handleDelete}
                onToggleComplete={handleToggleComplete}
                onSaveEdit={handleSaveEdit}
                onCancelEdit={() => setEditingId(null)}
                onEditTitleChange={setEditTitle}
                onEditDescriptionChange={setEditDescription}
              />
            );
          })}
        </div>

        {filteredTodos.length === 0 && (
          <div className="text-center py-12 text-slate-400 text-sm">
            No tasks found in this section.
          </div>
        )}

        {modal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 px-4">
            <div className={`w-full max-w-sm rounded-2xl border bg-white p-6 shadow-xl ${modal.type === 'error' ? 'border-red-200' : 'border-emerald-200'}`}>
              <h3 className={`text-lg font-semibold ${modal.type === 'error' ? 'text-red-600' : 'text-emerald-600'}`}>
                {modal.title}
              </h3>
              <p className="mt-2 text-sm text-slate-600">{modal.message}</p>
              <div className="mt-5 flex justify-end">
                <button
                  onClick={() => setModal(null)}
                  className="rounded-lg bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        )}

      </div>
    </div>
  );
}