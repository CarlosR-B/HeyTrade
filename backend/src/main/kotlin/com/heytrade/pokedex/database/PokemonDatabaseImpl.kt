package com.heytrade.pokedex.database

import com.heytrade.pokedex.core.PokemonDatabase
import com.heytrade.pokedex.model.Pokemon
import org.springframework.stereotype.Component

@Component
internal class PokemonDatabaseImpl : PokemonDatabase {
    override fun findAll(): Sequence<Pokemon> {
        return sequenceOf(bulbasaur(), ivysaur())
    }


    private fun bulbasaur(): Pokemon = Pokemon(1, "Bulbasaur", listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison")))

    private fun ivysaur(): Pokemon = Pokemon(2, "Ivysaur", listOf(Pokemon.Type("Grass"), Pokemon.Type("Poison")))
}