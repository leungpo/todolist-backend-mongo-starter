package com.afs.todolist.service;

import com.afs.todolist.entity.Todo;
import com.afs.todolist.exception.InvalidIdException;
import com.afs.todolist.repository.TodoRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    private void validateObjectId(String id){
        if(!ObjectId.isValid(id)){
            throw new InvalidIdException(id);
        }
    }

    public Todo addTodo(String text) {
        Todo existingTodo = new Todo();
        existingTodo.setText(text);
        existingTodo.setDone(false);
        return todoRepository.save(existingTodo);
    }

    public Todo updateTodo(String id,Todo todo) {
        Todo existingTodo = todoRepository.findById(id).orElseThrow(IllegalArgumentException ::new);
        if(todo.getText() != null){
            existingTodo.setText(todo.getText());
        }
        else{
            existingTodo.setDone(!existingTodo.getDone());
        }
        return todoRepository.save(existingTodo);
    }

    public List<Todo> deleteTodo(String id) {
        todoRepository.deleteById(id);
        return findAll();
    }
}
