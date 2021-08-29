package com.heytrade.pokedex.core

import com.heytrade.pokedex.model.Pokemon
import org.springframework.stereotype.Component

interface PokemonSearcher {
    fun searchBy(name: String? = null): List<Pokemon>
}

interface PokemonDatabase {
    fun findAll(): Sequence<Pokemon>
}

@Component
internal class PokemonSearcherImpl(private val pokemonDatabase: PokemonDatabase) : PokemonSearcher {
    override fun searchBy(name: String?): List<Pokemon> {
        // XXX Filtering in memory is good enough for now, as the database will be static and small
        return pokemonDatabase.findAll()
            .filter { if (name != null) it.name.contains(ignoreCase = true, other = name) else true }
            .toList()
    }
}