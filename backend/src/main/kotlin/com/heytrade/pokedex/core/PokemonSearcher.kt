package com.heytrade.pokedex.core

import com.heytrade.pokedex.core.PokemonSearcher.PokemonFilter
import com.heytrade.pokedex.model.Pokemon
import org.springframework.stereotype.Component

interface PokemonSearcher {
    fun searchBy(pokemonFilter: PokemonFilter = PokemonFilter()): List<Pokemon>

    // Unifying all filters inside a single object should make it possible to add new methods to the core logic
    // which should not really change all that much, particularly if we use the correct patterns in the business logic
    data class PokemonFilter(val name: String? = null, val favorite: Boolean = false)
}

interface PokemonDatabase {
    fun findAll(): Sequence<Pokemon>
    fun findFavorites(): Sequence<Pokemon>
}

@Component
internal class PokemonSearcherImpl(private val pokemonDatabase: PokemonDatabase) : PokemonSearcher {
    override fun searchBy(pokemonFilter: PokemonFilter): List<Pokemon> {
        // TODO: use an specification pattern or something similar to it to combine the different filters together
        val pokeSequence = if (pokemonFilter.favorite) pokemonDatabase.findFavorites() else pokemonDatabase.findAll()
        // XXX Filtering in memory is good enough for now, as the database will be static and small
        return pokeSequence
            .filter {
                if (pokemonFilter.name != null) it.name.contains(ignoreCase = true, other = pokemonFilter.name)
                else true
            }
            .toList()
    }
}