package com.afs.todolist;

import com.afs.todolist.entity.Todo;
import com.afs.todolist.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    TodoRepository todoRepository;

    @BeforeEach
    public void clearDB() {
        todoRepository.deleteAll();
    }
    @Test
    void should_get_all_todo_when_perform_get_given_2_todo() throws Exception {
        //given
        Todo todo1 = new Todo();
        todo1.setText("123");
        Todo todo2 = new Todo();
        todo2.setText("456");
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        //when & then
        client.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text").value("456"));
    }

    @Test
    void should_create_todo_when_perform_post_given_1_new_todo() throws Exception {
        //given
        Todo todo1 = new Todo();
        todo1.setId(new ObjectId().toString());
        todo1.setText("123");
        todo1.setDone(false);
        String newCompanyJson = new ObjectMapper()
                .writeValueAsString(todo1);
        //when & then
        client.perform(MockMvcRequestBuilders.post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCompanyJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false));

    }

    @Test
    void should_update_todo_status_when_perform_put_given_existing_todo() throws Exception {
        //given
        Todo todo1 = new Todo();
        todo1.setId(new ObjectId().toString());
        todo1.setText("123");
        todo1.setDone(false);
        todoRepository.save(todo1);
        Todo updateTodo = new Todo();
        updateTodo.setDone(true);
        updateTodo.setId(new ObjectId().toString());
        String newTodoJson = new ObjectMapper().writeValueAsString(updateTodo);
        //when & then
        client.perform(MockMvcRequestBuilders.put("/todos/{id}", todo1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTodoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(todo1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(true));
        //then
    }

    @Test
    void should_update_todo_text_when_perform_put_given_existing_todo() throws Exception {
        //given
        Todo todo1 = new Todo();
        todo1.setId(new ObjectId().toString());
        todo1.setText("123");
        todo1.setDone(false);
        todoRepository.save(todo1);
        Todo updateTodo = new Todo();
        updateTodo.setText("456");
        updateTodo.setId(new ObjectId().toString());
        String newTodoJson = new ObjectMapper().writeValueAsString(updateTodo);
        //when & then
        client.perform(MockMvcRequestBuilders.put("/todos/{id}", todo1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTodoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(todo1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("456"));

    }


}
