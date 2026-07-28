package com.todo_apps.todos.service;

import com.todo_apps.todos.dto.CreateTodoRequest;
import com.todo_apps.todos.dto.TodoResponse;
import com.todo_apps.todos.model.Todo;
import com.todo_apps.todos.repository.TodoRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TodoServiceTest {

    @Test
    void createTodoMapsEntityToResponse() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);

        when(repository.save(any(Todo.class))).thenAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            todo.setId(1L);
            return todo;
        });

        TodoResponse response = service.createTodo(
                new CreateTodoRequest("Buy milk", "Weekly groceries", true)
        );

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Buy milk", response.title());
        assertEquals("Weekly groceries", response.description());
        assertTrue(response.status());
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());
        assertEquals(response.createdAt(), response.updatedAt());
    }
}
