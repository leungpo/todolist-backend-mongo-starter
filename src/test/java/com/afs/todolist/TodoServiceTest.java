package com.afs.todolist;

import com.afs.todolist.entity.Todo;
import com.afs.todolist.repository.TodoRepository;
import com.afs.todolist.service.TodoService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoService todoService;

    @Test
    void should_return_all_todos_when_find_all_given_todos() {
        //given
        Todo todo1 = new Todo();
        todo1.setText("123");
        todo1.setDone(false);
        Todo todo2 = new Todo();
        todo2.setText("456");
        todo2.setDone(false);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        List<Todo> todos = new ArrayList<>(Arrays.asList(todo1,todo2));
        given(todoRepository.findAll()).willReturn(todos);
        //when
        List<Todo> actualTodos = todoService.findAll();
        //then
        assertThat(actualTodos,hasSize(2));
        assertThat(actualTodos.get(0),equalTo(todo1));
        assertThat(actualTodos.get(1),equalTo(todo2));
    }

    @Test
    void should_delete_a_company_when_delete_Todo_given_a_todo() {
        //given
       final String todoId = new ObjectId().toString();
       //when
        todoService.deleteTodo(todoId);

        //then
        verify(todoRepository).deleteById(todoId);
    }

    @Test
    void should_return_Todo_when_update_Todo_text_given_a_todo() {
        //given
        Todo originalTodo = new Todo();
        originalTodo.setId(new ObjectId().toString());
        originalTodo.setText("123");
        originalTodo.setDone(false);
        Todo toUpdateTodo = new Todo();
        toUpdateTodo.setId(new ObjectId().toString());
        toUpdateTodo.setText("456");
        toUpdateTodo.setDone(false);
        String id = originalTodo.getId();
        String text = toUpdateTodo.getText();
        given(todoRepository.findById(id)).willReturn(Optional.of(originalTodo));
        given(todoRepository.save(originalTodo)).willReturn(toUpdateTodo);
        //when
       Todo actualTodo = todoService.updateTodo(id,toUpdateTodo);

        //then
        verify(todoRepository).findById(id);
        assertThat(actualTodo.getText(), equalTo(text));
    }

    @Test
    void should_return_Todo_when_update_Todo_status_given_a_todo() {
        //given
        Todo originalTodo = new Todo();
        originalTodo.setId(new ObjectId().toString());
        originalTodo.setText("123");
        originalTodo.setDone(false);
        Todo toUpdateTodo = new Todo();
        toUpdateTodo.setId(new ObjectId().toString());
        toUpdateTodo.setText("123");
        toUpdateTodo.setDone(true);
        String id = originalTodo.getId();
        Boolean done = toUpdateTodo.getDone();
        given(todoRepository.findById(id)).willReturn(Optional.of(originalTodo));
        given(todoRepository.save(originalTodo)).willReturn(toUpdateTodo);
        //when
        Todo actualTodo = todoService.updateTodo(id,toUpdateTodo);

        //then
        verify(todoRepository).findById(id);
        assertThat(actualTodo.getDone(), equalTo(done));
    }
}
