package com.todo_apps.todos.service;

import com.todo_apps.todos.dto.CreateTodoRequest;
import com.todo_apps.todos.dto.TodoResponse;
import com.todo_apps.todos.dto.UpdateTodoRequest;
import com.todo_apps.todos.model.Todo;
import com.todo_apps.todos.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoResponse createTodo(CreateTodoRequest request) {
        Todo todo = new Todo(request.title(), request.description(), request.status());
        Todo savedTodo = todoRepository.save(todo);
        return mapToResponse(savedTodo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        return todos.stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public TodoResponse getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found with id: " + id));
        return mapToResponse(todo);
    }

    @Transactional
    public TodoResponse updateTodo(Long id, UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found with id: " + id));

        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setStatus(request.status());

        Todo updatedTodo = todoRepository.save(todo);
        return mapToResponse(updatedTodo);
    }

    @Transactional
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new NoSuchElementException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }

    private TodoResponse mapToResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isStatus(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}