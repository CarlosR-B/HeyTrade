package com.heytrade.pokedex.database

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.heytrade.pokedex.core.PokemonDatabase
import com.heytrade.pokedex.model.Pokemon
import com.heytrade.pokedex.model.Pokemon.Type
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
internal class PokemonDatabaseImpl(
    @Value("classpath:pokemons.json") private val pokemonJsonFile: Resource
) : PokemonDatabase {

    // XXX We are loading the static json database from the filesystem once at boot time, so we have it cached on
    // each query
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private val pokemons: List<PokemonDao> = objectMapper.readValue(pokemonJsonFile.file)

    override fun findAll(): Sequence<Pokemon> =
        pokemons.map { Pokemon(it.id, it.name, it.types.map { t -> Type(t) }) }.asSequence()

    private data class PokemonDao(val id: Int, val name: String, val types: List<String>)

}