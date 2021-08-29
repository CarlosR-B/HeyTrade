package com.heytrade.pokedex.database

import assertk.assertThat
import assertk.assertions.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class PokemonDatabaseImplTest(@Autowired private val pokemonDatabaseImpl: PokemonDatabaseImpl) {

    @Test
    fun findAll() {
        // Given that we have a database with the first 4 Pokemon
        // When we retrieve all of them from the database
        val pokemons = pokemonDatabaseImpl.findAll().toList()
        // Then we get them back
        assertThat(pokemons).hasSize(4)
    }
}