package com.todo_apps.todos.dto;

import java.time.LocalDateTime;

public record TodoResponse(Long id, String title, String description, boolean status, LocalDateTime createdAt, LocalDateTime updatedAt) {}