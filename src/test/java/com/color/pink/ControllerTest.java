package com.color.pink;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HarrisonLee
 * @date 2020/4/26 17:28
 */

@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test01() throws Exception {
        var resultActions = this.mockMvc.perform(get("/admin/article/1/5"));
        resultActions.andDo(print()).andExpect(status().isOk());
    }
}
