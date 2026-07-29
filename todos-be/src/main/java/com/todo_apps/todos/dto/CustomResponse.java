package com.todo_apps.todos.dto;

public record CustomResponse<T>(boolean success, String message, T data) {}
