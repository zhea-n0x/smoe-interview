package com.todo_apps.todos.dto;

public record UpdateTodoRequest(String title, String description, boolean status){}