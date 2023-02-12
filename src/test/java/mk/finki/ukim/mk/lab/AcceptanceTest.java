package mk.finki.ukim.mk.lab;

import mk.finki.ukim.mk.lab.model.exceptions.UsernameExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
//@SpringBootTest()
//@ActiveProfiles("test")
@SpringBootTest()
public class AcceptanceTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testDefaultPage() throws Exception{
        MockHttpServletRequestBuilder defaultPageRequest = MockMvcRequestBuilders.get("/");
        mockMvc.perform(defaultPageRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }


    @Test
    public void testRegisterPage() throws Exception {
        MockHttpServletRequestBuilder homePageRequest = MockMvcRequestBuilders.get("/register");
        this.mockMvc.perform(homePageRequest).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }



    @Test
    public void testBalloonsPage() throws Exception {
        MockHttpServletRequestBuilder balloonsPageRequest = MockMvcRequestBuilders.get("/balloons");
        this.mockMvc.perform(balloonsPageRequest).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("listBalloons"));
    }

    @Test
    public void testShoppingCartPage() throws Exception{
        MockHttpServletRequestBuilder balloonsRequest = MockMvcRequestBuilders.get("/shopping-cart");

        mockMvc.perform(balloonsRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testOrderPage() throws Exception{
        MockHttpServletRequestBuilder balloonsRequest = MockMvcRequestBuilders.get("/order");

        mockMvc.perform(balloonsRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }




    @Test
    public void testLoginUser() throws Exception{
        MockHttpServletRequestBuilder loginRequest = MockMvcRequestBuilders.post("/")
                                                           .param("username", "filip")
                                                           .param("password", "filip123");

        this.mockMvc.perform(loginRequest).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }



    @Test
    public void testRegisterUser() throws Exception {

        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register")
                .param("username", "danii")
                .param("password", "daniela123")
                .param("repeatedPassword", "daniela123")
                .param("name", "daniela")
                .param("surname", "danilova")
                .param("date", "")
                .param("role","ROLE_USER");

        this.mockMvc.perform(registerRequest).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

    }





}
