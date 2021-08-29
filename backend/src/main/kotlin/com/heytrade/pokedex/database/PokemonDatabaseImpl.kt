package com.heytrade.pokedex.database

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.heytrade.pokedex.core.PokemonDatabase
import com.heytrade.pokedex.core.PokemonFavDatabase
import com.heytrade.pokedex.core.PokemonFavDatabase.NoPokemon
import com.heytrade.pokedex.model.Pokemon
import com.heytrade.pokedex.model.Pokemon.Type
import io.vavr.control.Either
import io.vavr.control.Try
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

// TODO: translate the repository into JPA and a persistent database
@Component
internal class PokemonDatabaseImpl(
    @Value("classpath:pokemons.json") private val pokemonJsonFile: Resource
) : PokemonDatabase, PokemonFavDatabase {

    // XXX We are loading the static json database from the filesystem once at boot time, so we have it cached on
    // each query
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private val pokemons: List<PokemonDao> = objectMapper.readValue(pokemonJsonFile.file)

    internal val favoritePokemon = mutableSetOf<Int>()

    private data class PokemonDao(val id: Int, val name: String, val types: List<String>)

    private fun toPokemon(dao: PokemonDao) = Pokemon(dao.id, dao.name, dao.types.map { t -> Type(t) })

    override fun findAll(): Sequence<Pokemon> = pokemons.map(this::toPokemon).asSequence()
    override fun findFavorites(): Sequence<Pokemon> =
        pokemons.filter { favoritePokemon.contains(it.id) }.map(this::toPokemon).asSequence()


    override fun favPokemon(id: Int): Try<Either<NoPokemon, Pokemon>> {
        val pokemonDao = pokemons.find { it.id == id }
        return if (pokemonDao != null) {
            favoritePokemon.add(id)
            Try.of { Either.right(toPokemon(pokemonDao)) }
        } else Try.of { Either.left(NoPokemon()) }
    }

    override fun unfavPokemon(id: Int): Try<Either<NoPokemon, Pokemon>> {
        val pokemonDao = pokemons.find { it.id == id }
        return if (pokemonDao != null) {
            favoritePokemon.remove(id)
            Try.of { Either.right(toPokemon(pokemonDao)) }
        } else Try.of { Either.left(NoPokemon()) }
    }

}