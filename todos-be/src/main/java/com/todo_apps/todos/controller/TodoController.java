package com.todo_apps.todos.controller;

// dtos
import com.todo_apps.todos.dto.CreateTodoRequest;
import com.todo_apps.todos.dto.CustomResponse;
import com.todo_apps.todos.dto.TodoResponse;
import com.todo_apps.todos.dto.UpdateTodoRequest;

// models
import com.todo_apps.todos.model.Todo;

// services
import com.todo_apps.todos.service.TodoService;

// deps
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<CustomResponse<TodoResponse>> createTodo(@Valid @RequestBody CreateTodoRequest request) {
        TodoResponse data = todoService.createTodo(request);
        CustomResponse<TodoResponse> response = new CustomResponse<>(
                true,
                "Todo created successfully",
                data
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CustomResponse<List<TodoResponse>>> getAllTodos() {
        List<TodoResponse> todos = todoService.getAllTodos();
        CustomResponse<List<TodoResponse>> response = new CustomResponse<>(
                true,
                "Todos retrieved successfully",
                todos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<TodoResponse>> getTodoById(@PathVariable Long id) {
        TodoResponse response = todoService.getTodoById(id);
        CustomResponse<TodoResponse> customResponse = new CustomResponse<>(
                true,
                "Todo retrieved successfully",
                response
        );
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<TodoResponse>> updateTodo(@PathVariable Long id, @Valid @RequestBody UpdateTodoRequest request) {
        try {
            TodoResponse data = todoService.updateTodo(id, request);
            CustomResponse<TodoResponse> response = new CustomResponse<>(
                    true,
                    "Todo updated successfully",
                    data
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            CustomResponse<TodoResponse> response = new CustomResponse<>(
                    false,
                    "Todo is not exist",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> deleteTodo(@PathVariable Long id) {
        try {
            todoService.deleteTodo(id);
            CustomResponse<Void> response = new CustomResponse<>(
                    true,
                    "Todo deleted successfully",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            CustomResponse<Void> response = new CustomResponse<>(
                    false,
                    "Todo is not exist",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}