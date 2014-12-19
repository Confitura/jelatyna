package pl.confitura.jelatyna.sponsors.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.confitura.jelatyna.Application;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.confitura.jelatyna.JsonTestUtil.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SponsorControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void init() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createAndGetSponsorGroupsWithSponsors() throws Exception {
        mockMvc.perform(
                post("/sponsorGroup")
                        .content(json("{'name':'platinum', 'label':'Platynowi'}"))
                        .contentType(APPLICATION_JSON));
        mockMvc.perform(
                post("/sponsor")
                        .content(json("{'name':'Computex','description':'Good company','sponsorGroupName':'platinum'}"))
                        .contentType(APPLICATION_JSON));
        mockMvc.perform(
                post("/sponsor")
                        .content(json("{'name':'Softex','description':'Best soft','sponsorGroupName':'platinum'}"))
                        .contentType(APPLICATION_JSON));

        ResultActions getSponsorGroups = mockMvc.perform(get("/sponsorGroup"));

        getSponsorGroups
                .andExpect(jsonPath("$[0].name", is("platinum")))
                .andExpect(jsonPath("$[0].label", is("Platynowi")))
                .andExpect(jsonPath("$[0].sponsors[0].name", is("Computex")))
                .andExpect(jsonPath("$[0].sponsors[0].description", is("Good company")))
                .andExpect(jsonPath("$[0].sponsors[1].name", is("Softex")))
                .andExpect(jsonPath("$[0].sponsors[1].description", is("Best soft")))
        ;
    }
}