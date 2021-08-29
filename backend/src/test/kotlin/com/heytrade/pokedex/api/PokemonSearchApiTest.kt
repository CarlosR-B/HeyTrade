package com.heytrade.pokedex.api

import com.heytrade.pokedex.core.PokemonSearcher
import com.heytrade.pokedex.model.Pokemon
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest(PokemonSearchApi::class)
internal class PokemonSearchApiTest {

    @TestConfiguration
    class ApiTestConfig {
        @Bean
        fun pokemonSearcher() = mockk<PokemonSearcher>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var pokemonSearcher: PokemonSearcher

    @Test
    fun searchByName() {
        // Given that the search returns a Bulbasaur
        val pokemonName = "Ivysaur"
        every { pokemonSearcher.searchBy(pokemonName) } returns listOf(
            Pokemon(
                2,
                pokemonName,
                listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison"))
            )
        )

        // FIXME mockMvc is pretty awful, should probably change all of this to Rest Assured
        // When we query the search by name endpoint
        mockMvc
            .perform(get("/pokedex/search?name=$pokemonName"))
            .andDo(print())

            // Then we get an OK response code and the correct data
            .andExpect(status().isOk)
            .andExpect(
                content()
                    .string(containsString("""{"id":2,"name":"Ivysaur","types":[{"type":"Grass"},{"type":"Poison"}]}"""))
            )
    }
}