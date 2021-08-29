package com.heytrade.pokedex.core

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsOnly
import com.heytrade.pokedex.core.PokemonSearcher.PokemonFilter
import com.heytrade.pokedex.model.Pokemon
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class PokemonSearcherImplTest {

    private val pokemonDatabase: PokemonDatabase = mockk()
    private val pokemonSearcher = PokemonSearcherImpl(pokemonDatabase)

    @Test
    fun searchByName() {
        // Given that we want to find Bulbasaur
        every { pokemonDatabase.findAll() } returns sequenceOf(bulbasaur(), ivysaur())

        // When we search by its complete name
        val pokemon = pokemonSearcher.searchBy(PokemonFilter("bulbasaur"))

        // Then we find only Bulbasaur
        assertThat(pokemon).containsOnly(bulbasaur())
    }

    @Test
    fun searchBySuffix() {
        // Given that we want to find Bulbasaur and Ivysaur
        every { pokemonDatabase.findAll() } returns sequenceOf(bulbasaur(), ivysaur())

        // When we search by their common suffix
        val pokemon = pokemonSearcher.searchBy(PokemonFilter("SaUr"))

        // Then we find both of them
        assertThat(pokemon).containsExactly(bulbasaur(), ivysaur())
    }

    @Test
    fun findAll() {
        // Given that we want to find all the Pokemon available
        every { pokemonDatabase.findAll() } returns sequenceOf(bulbasaur(), ivysaur())

        // When we search without a filter
        val pokemon = pokemonSearcher.searchBy()

        // Then we get all the Pokemon back
        assertThat(pokemon).containsExactly(bulbasaur(), ivysaur())
    }

    private fun bulbasaur(): Pokemon = Pokemon(1, "Bulbasaur", listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison")))

    private fun ivysaur(): Pokemon = Pokemon(2, "Ivysaur", listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison")))
}