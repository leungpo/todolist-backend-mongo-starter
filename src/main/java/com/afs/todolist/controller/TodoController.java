package com.afs.todolist.controller;

import com.afs.todolist.controller.dto.TodoCreateRequest;
import com.afs.todolist.controller.dto.TodoUpdateRequest;
import com.afs.todolist.controller.mapper.TodoMapper;
import com.afs.todolist.entity.Todo;
import com.afs.todolist.service.TodoService;
import com.mongodb.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoMapper todoMapper;

    public TodoController(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @GetMapping
    List<Todo> getAll() {
        return todoService.findAll();
    }

    @PostMapping
    Todo addTodo(@RequestBody TodoCreateRequest request){
        Todo todo = todoMapper.toEntity(request);
        return todoService.addTodo(todo.getText());
    }

    @PutMapping("/{id}")
    Todo updateTodoStatus(@PathVariable String id, @RequestBody TodoUpdateRequest request) {
        return todoService.updateTodoStatus(id);
    }
}
