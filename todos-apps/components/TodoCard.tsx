import React from 'react';
import { Check, Pencil, Trash2 } from 'lucide-react';
import type { TodoItem } from '@/lib/api';

type TodoCardProps = {
  todo: TodoItem;
  isEditing: boolean;
  editTitle: string;
  editDescription: string;
  onStartEdit: (todo: TodoItem) => void;
  onDelete: (id: number) => void;
  onToggleComplete: (id: number) => void;
  onSaveEdit: (id: number) => void;
  onCancelEdit: () => void;
  onEditTitleChange: (value: string) => void;
  onEditDescriptionChange: (value: string) => void;
  cardRef?: React.RefObject<HTMLDivElement | null>;
};

export default function TodoCard({
  todo,
  isEditing,
  editTitle,
  editDescription,
  onStartEdit,
  onDelete,
  onToggleComplete,
  onSaveEdit,
  onCancelEdit,
  onEditTitleChange,
  onEditDescriptionChange,
  cardRef,
}: TodoCardProps) {
  return (
    <div
      ref={cardRef}
      className={`p-5 rounded-2xl border transition-all duration-200 shadow-sm flex flex-col justify-between ${todo.bgColor} ${todo.borderColor} ${todo.completed ? 'opacity-90 ring-1 ring-slate-400/40' : ''}`}
    >
      <div>
        <div className="flex items-start justify-between gap-3 mb-2">
          <div className="flex items-start gap-3 flex-1">
            <button
              onClick={() => onToggleComplete(todo.id)}
              className={`w-5 h-5 mt-0.5 rounded border flex items-center justify-center transition ${
                todo.completed ? 'bg-slate-800 border-slate-800 text-white' : 'bg-white border-slate-300'
              }`}
              aria-label={todo.completed ? 'Mark as active' : 'Mark as completed'}
            >
              {todo.completed && <Check className="w-3.5 h-3.5" />}
            </button>

            {isEditing ? (
              <input
                type="text"
                value={editTitle}
                onChange={(event) => onEditTitleChange(event.target.value)}
                className="w-full px-2 py-1 text-sm font-bold bg-white/80 border border-slate-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Title..."
              />
            ) : (
              <div className="flex-1">
                <h3 className={`font-semibold text-base leading-snug ${todo.completed ? 'line-through opacity-60' : ''}`}>
                  {todo.title}
                </h3>
                {todo.description && (
                  <p className={`mt-1 text-xs ${todo.completed ? 'text-slate-500' : 'text-slate-600'}`}>
                    {todo.description}
                  </p>
                )}
              </div>
            )}
          </div>

          <div className="flex items-center gap-1.5">
            {!isEditing && (
              <button
                onClick={() => !todo.completed && onStartEdit(todo)}
                title={todo.completed ? 'Completed tasks cannot be edited' : 'Edit task'}
                disabled={todo.completed}
                className={`p-1 rounded transition ${
                  todo.completed
                    ? 'cursor-not-allowed text-slate-400'
                    : 'hover:bg-black/10 text-slate-600'
                }`}
              >
                <Pencil className="w-4 h-4" />
              </button>
            )}
            <button
              onClick={() => onDelete(todo.id)}
              title="Delete task"
              className="p-1 rounded hover:bg-red-500/10 text-red-600 transition"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </div>
        </div>

        <div className="mt-2 text-xs leading-relaxed opacity-80">
          {isEditing ? (
            <textarea
              rows={3}
              value={editDescription}
              onChange={(event) => onEditDescriptionChange(event.target.value)}
              className="w-full p-2 text-xs bg-white/80 border border-slate-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
              placeholder="Description..."
            />
          ) : (
            <div className="space-y-2">
              {todo.description ? (
                <p className={todo.completed ? 'line-through' : ''}>{todo.description}</p>
              ) : (
                <p className="text-slate-400 italic">No additional details</p>
              )}
              {todo.completed && (
                <span className="inline-flex items-center rounded-full bg-slate-800 px-2.5 py-1 text-[10px] font-semibold uppercase tracking-wide text-white">
                  Completed
                </span>
              )}
            </div>
          )}
        </div>
      </div>

      <div className="mt-4 pt-2 flex items-center justify-between text-xs font-medium gap-2">
        <div className="flex flex-col text-[11px] leading-5 opacity-80">
          <span>Created At: {todo.createdAt}</span>
          {todo.completed && todo.updatedAt && <span>Finished At: {todo.updatedAt}</span>}
        </div>

        {isEditing && (
          <div className="flex items-center gap-2">
            <button
              onClick={onCancelEdit}
              className="px-3 py-1 rounded text-xs transition text-slate-600 hover:bg-white/70"
            >
              Cancel
            </button>
            <button
              onClick={() => onSaveEdit(todo.id)}
              className="bg-slate-900 hover:bg-slate-800 text-white px-3 py-1 rounded text-xs transition"
            >
              Save
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
