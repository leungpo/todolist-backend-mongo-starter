package com.afs.todolist;

import com.afs.todolist.entity.Todo;
import com.afs.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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


}
