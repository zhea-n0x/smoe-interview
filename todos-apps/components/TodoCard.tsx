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
      className="p-5 rounded-[1.6rem] border border-slate-200 bg-white shadow-sm transition-all duration-200"
    >
      <div>
        <div className="flex items-start justify-between gap-3 mb-3">
          <div className="flex items-start gap-3 flex-1">
            <button
              onClick={() => onToggleComplete(todo.id)}
              className={`mt-1 grid h-6 w-6 place-items-center rounded-full border transition ${
                todo.completed
                  ? 'border-emerald-500 bg-emerald-500 text-white'
                  : 'border-slate-300 bg-white text-slate-400 hover:border-slate-400'
              }`}
              aria-label={todo.completed ? 'Mark as active' : 'Mark as completed'}
            >
              {todo.completed && <Check className="w-3 h-3" />}
            </button>

            {isEditing ? (
              <input
                type="text"
                value={editTitle}
                onChange={(event) => onEditTitleChange(event.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm font-semibold text-slate-900 outline-none transition focus:border-blue-400 focus:ring-2 focus:ring-blue-100"
                placeholder="Task title"
              />
            ) : (
              <div className="flex-1">
                <h3 className={`text-base font-semibold leading-snug ${todo.completed ? 'line-through text-slate-400' : 'text-slate-900'}`}>
                  {todo.title}
                </h3>
                {todo.description ? (
                  <p className={`mt-2 text-sm leading-6 ${todo.completed ? 'text-slate-400 line-through' : 'text-slate-600'}`}>
                    {todo.description}
                  </p>
                ) : (
                  <p className="mt-2 text-sm text-slate-400 italic">No additional details</p>
                )}
              </div>
            )}
          </div>

          <div className="flex items-center gap-2">
            {!isEditing && (
              <button
                onClick={() => !todo.completed && onStartEdit(todo)}
                title={todo.completed ? 'Completed tasks cannot be edited' : 'Edit task'}
                disabled={todo.completed}
                className={`rounded-full p-2 transition ${
                  todo.completed
                    ? 'cursor-not-allowed text-slate-300'
                    : 'text-slate-600 hover:bg-slate-100'
                }`}
              >
                <Pencil className="w-4 h-4" />
              </button>
            )}
            <button
              onClick={() => onDelete(todo.id)}
              title="Delete task"
              className="rounded-full p-2 text-red-600 transition hover:bg-red-50"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </div>
        </div>

        {isEditing && (
          <div className="mt-3">
            <textarea
              rows={3}
              value={editDescription}
              onChange={(event) => onEditDescriptionChange(event.target.value)}
              className="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-900 outline-none transition focus:border-blue-400 focus:ring-2 focus:ring-blue-100 resize-none"
              placeholder="Task description"
            />
          </div>
        )}
      </div>

      <div className="mt-5 flex flex-col gap-3 border-t border-slate-100 pt-4 text-xs text-slate-500">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <span>Created: {todo.createdAt}</span>
          {todo.completed && todo.updatedAt && <span>Finished: {todo.updatedAt}</span>}
        </div>

        {isEditing ? (
          <div className="flex gap-2">
            <button
              onClick={onCancelEdit}
              className="rounded-full border border-slate-200 px-3 py-2 text-xs font-semibold text-slate-700 transition hover:bg-slate-50"
            >
              Cancel
            </button>
            <button
              onClick={() => onSaveEdit(todo.id)}
              className="rounded-full bg-slate-900 px-3 py-2 text-xs font-semibold text-white transition hover:bg-slate-800"
            >
              Save
            </button>
          </div>
        ) : null}
      </div>
    </div>
  );
}
