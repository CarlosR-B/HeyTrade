package com.heytrade.pokedex.api

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
internal class PokemonSearchApiTest(@Autowired private val mockMvc: MockMvc) {

    @Test
    fun search() {
        // FIXME mockMvc is pretty awful, should probably change all of this to Rest Assured
        mockMvc
            .perform(get("/pokedex/search?name=Bulbasaur"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("{\"id\":1,\"name\":\"Bulbasaur\",\"types\":[\"Grass\",\"Poison\"]}")))
    }
}