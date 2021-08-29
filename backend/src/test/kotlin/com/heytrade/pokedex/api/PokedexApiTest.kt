package com.heytrade.pokedex.api

import com.heytrade.pokedex.core.PokemonDoesNotExistError
import com.heytrade.pokedex.core.PokemonFaver
import com.heytrade.pokedex.core.PokemonSearcher
import com.heytrade.pokedex.model.Pokemon
import io.mockk.every
import io.mockk.mockk
import io.vavr.control.Either
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest(PokedexApiImpl::class)
// FIXME mockMvc is pretty awful, should probably change all of this to Rest Assured
internal class PokedexApiTest {

    @TestConfiguration
    class ApiTestConfig {
        @Bean
        fun pokemonSearcher() = mockk<PokemonSearcher>()

        @Bean
        fun pokemonFaver() = mockk<PokemonFaver>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var pokemonSearcher: PokemonSearcher


    @Autowired
    private lateinit var pokemonFaver: PokemonFaver

    private val ivysaurJson = """{"id":2,"name":"Ivysaur","types":[{"type":"Grass"},{"type":"Poison"}]}"""

    @Test
    fun searchByName() {
        // Given that the search returns an Ivysaur
        val pokemonName = "Ivysaur"
        every { pokemonSearcher.searchBy(pokemonName) } returns listOf(
            Pokemon(
                2,
                pokemonName,
                listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison"))
            )
        )

        // When we query the search by name endpoint
        mockMvc
            .perform(get("/pokedex/search?name=$pokemonName"))
            .andDo(print())

            // Then we get an OK response code and the correct data
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(ivysaurJson)))
    }

    @Test
    fun favPokemon() {
        // Given that we are faving an Ivysaur
        val pokemonId = 1
        every { pokemonFaver.favPokemon(pokemonId) } returns Either.right(ivysaur())

        // When we post to the fav endpoint
        mockMvc
            .perform(post("/pokedex/fav/$pokemonId"))
            .andDo(print())

            // Then we get an OK response code and the correct data
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(ivysaurJson)))
    }

    @Test
    fun unfavPokemon() {
        // Given that we are unfaving an Ivysaur
        val pokemonId = 1
        every { pokemonFaver.unfavPokemon(pokemonId) } returns Either.right(ivysaur())

        // When we delete to the fav endpoint
        mockMvc
            .perform(delete("/pokedex/fav/$pokemonId"))
            .andDo(print())

            // Then we get an OK response code and the correct data
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(ivysaurJson)))
    }

    @Test
    fun `fav incorrect Pokemon`() {
        // Given that we are faving an incorrect Pokemon id
        val pokemonId = -1
        every { pokemonFaver.favPokemon(pokemonId) } returns Either.left(PokemonDoesNotExistError(pokemonId))

        // When we delete to the fav endpoint
        mockMvc
            .perform(delete("/pokedex/fav/$pokemonId"))
            .andDo(print())

            // Then we get an OK response code and the correct data
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("""{"errorMessage":"Pokemon #-1 does not exist"}""")))
    }


    @Test
    fun `unfav incorrect Pokemon`() {
        // Given that we are unfaving an incorrect Pokemon id
        val pokemonId = -1
        every { pokemonFaver.unfavPokemon(pokemonId) } returns Either.left(PokemonDoesNotExistError(pokemonId))

        // When we delete to the fav endpoint
        mockMvc
            .perform(delete("/pokedex/fav/$pokemonId"))
            .andDo(print())

            // Then we get an OK response code and the correct data
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("""{"errorMessage":"Pokemon #-1 does not exist"}""")))
    }


    private fun ivysaur(): Pokemon = Pokemon(2, "Ivysaur", listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison")))

}