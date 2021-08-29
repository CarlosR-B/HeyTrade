package com.heytrade.pokedex.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import com.heytrade.pokedex.core.PokemonFavDatabase.NoPokemon
import com.heytrade.pokedex.model.Pokemon
import io.mockk.every
import io.mockk.mockk
import io.vavr.control.Either
import io.vavr.control.Try
import org.junit.jupiter.api.Test

internal class PokemonFaverImplTest {

    private val pokemonFavDatabase: PokemonFavDatabase = mockk()
    private val pokemonFaverImpl = PokemonFaverImpl(pokemonFavDatabase)

    @Test
    fun favPokemon() {
        // Given a valid Pokemon Id
        val id = 1
        every { pokemonFavDatabase.favPokemon(1) } returns Try.of { Either.right(bulbasaur()) }

        // When we fav the Pokemon
        val response: Either<FavError, Pokemon> = pokemonFaverImpl.favPokemon(id)

        // Then we get a Pokemon back
        assertThat(response.isRight).isTrue()
        assertThat(response.get()).isEqualTo(bulbasaur())
    }

    @Test
    fun `fav a Pokemon with an invalid Id`() {
        // Given an invalid Pokemon Id
        val id = -1
        every { pokemonFavDatabase.favPokemon(-1) } returns Try.of { Either.left(NoPokemon()) }

        // When we fav the Pokemon
        val response: Either<FavError, Pokemon> = pokemonFaverImpl.favPokemon(id)

        // Then we get a Pokemon back
        assertThat(response.isLeft).isTrue()
        assertThat(response.left).isInstanceOf(PokemonDoesNotExistError::class)
    }

    @Test
    fun unfavPokemon() {
        // Given a valid Pokemon Id
        val id = 1
        every { pokemonFavDatabase.unfavPokemon(1) } returns Try.of { Either.right(bulbasaur()) }

        // When we fav the Pokemon
        val response: Either<FavError, Pokemon> = pokemonFaverImpl.unfavPokemon(id)

        // Then we get a Pokemon back
        assertThat(response.isRight).isTrue()
        assertThat(response.get()).isEqualTo(bulbasaur())
    }


    @Test
    fun `unfav a Pokemon with an invalid Id`() {
        // Given an invalid Pokemon Id
        val id = -1
        every { pokemonFavDatabase.unfavPokemon(-1) } returns Try.of { Either.left(NoPokemon()) }

        // When we fav the Pokemon
        val response: Either<FavError, Pokemon> = pokemonFaverImpl.unfavPokemon(id)

        // Then we get a Pokemon back
        assertThat(response.isLeft).isTrue()
        assertThat(response.left).isInstanceOf(PokemonDoesNotExistError::class)
    }


    private fun bulbasaur(): Pokemon = Pokemon(1, "Bulbasaur", listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison")))

}