package com.heytrade.pokedex.database

import assertk.assertThat
import assertk.assertions.*
import com.heytrade.pokedex.core.PokemonFavDatabase.NoPokemon
import com.heytrade.pokedex.model.Pokemon
import com.heytrade.pokedex.model.Pokemon.Type
import org.junit.jupiter.api.AfterEach
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


    @Test
    fun `find fav pokemons`() {
        // Given that we have a database with the first 4 Pokemon and one favorite
        val id = 1
        pokemonDatabaseImpl.favoritePokemon.add(id)
        // When we retrieve all of them from the database
        val pokemons = pokemonDatabaseImpl.findFavorites().toList()
        // Then we get them back
        assertThat(pokemons).hasSize(1)
        assertThat(pokemons).containsOnly(bulbasaur())
    }

    @Test
    fun favPokemon() {
        // Given an existing Pokemon
        val id = 1
        // When we fav the pokemon
        val pokemon = pokemonDatabaseImpl.favPokemon(id)
        // Then the Pokemon is returned and faved
        assertThat(pokemon.isSuccess).isTrue()
        assertThat(pokemon.get().isRight).isTrue()
        assertThat(pokemon.get().get()).isEqualTo(bulbasaur())
        assertThat(pokemonDatabaseImpl.favoritePokemon).containsOnly(1)
    }

    @Test
    fun `fav non-existing Pokemon`() {
        // Given a non-existing Pokemon
        val id = -1
        // When we fav the pokemon
        val pokemon = pokemonDatabaseImpl.favPokemon(id)
        // Then the Pokemon is returned and faved
        assertThat(pokemon.isSuccess).isTrue()
        assertThat(pokemon.get().isLeft).isTrue()
        assertThat(pokemon.get().left).isInstanceOf(NoPokemon::class)
        assertThat(pokemonDatabaseImpl.favoritePokemon).isEmpty()
    }

    @Test
    fun unfavPokemon() {
        // Given an existing Pokemon that is already faved
        val id = 1
        pokemonDatabaseImpl.favoritePokemon.add(1)
        // When we unfav the pokemon
        val pokemon = pokemonDatabaseImpl.unfavPokemon(id)
        // Then the Pokemon is returned and faved
        assertThat(pokemon.isSuccess).isTrue()
        assertThat(pokemon.get().isRight).isTrue()
        assertThat(pokemon.get().get()).isEqualTo(bulbasaur())
        assertThat(pokemonDatabaseImpl.favoritePokemon).isEmpty()
    }

    @Test
    fun `unfav non-existing Pokemon`() {
        // Given a non-existing Pokemon
        val id = -1
        // When we fav the pokemon
        val pokemon = pokemonDatabaseImpl.unfavPokemon(id)
        // Then the Pokemon is returned and faved
        assertThat(pokemon.isSuccess).isTrue()
        assertThat(pokemon.get().isLeft).isTrue()
        assertThat(pokemon.get().left).isInstanceOf(NoPokemon::class)
        assertThat(pokemonDatabaseImpl.favoritePokemon).isEmpty()
    }

    @AfterEach
    fun cleanUpFavs() {
        pokemonDatabaseImpl.favoritePokemon.clear()
    }

    private fun bulbasaur(): Pokemon = Pokemon(1, "Bulbasaur", listOf(Type("Grass"), Type("Poison")))

}