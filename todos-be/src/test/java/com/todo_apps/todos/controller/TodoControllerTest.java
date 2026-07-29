package com.todo_apps.todos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo_apps.todos.dto.CreateTodoRequest;
import com.todo_apps.todos.dto.TodoResponse;
import com.todo_apps.todos.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @Test
    void createTodoReturnsCustomResponse() throws Exception {
        TodoResponse response = new TodoResponse(
                1L,
                "Buy groceries",
                "Milk and bread",
                false,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 10, 0)
        );

        when(todoService.createTodo(any(CreateTodoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateTodoRequest("Buy groceries", "Milk and bread", false))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Todo created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Buy groceries"));
    }

    @Test
    void updateTodoReturnsCustomNotFoundResponseWhenIdMissing() throws Exception {
        when(todoService.updateTodo(any(Long.class), any())).thenThrow(new java.util.NoSuchElementException("Todo not found"));

        mockMvc.perform(put("/api/todos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new com.todo_apps.todos.dto.UpdateTodoRequest("Updated", "Updated desc", true))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Todo is not exist"));
    }

    @Test
    void deleteTodoReturnsCustomNotFoundResponseWhenIdMissing() throws Exception {
        org.mockito.Mockito.doThrow(new java.util.NoSuchElementException("Todo is not exist"))
                .when(todoService).deleteTodo(any(Long.class));

        mockMvc.perform(delete("/api/todos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Todo is not exist"));
    }
}
