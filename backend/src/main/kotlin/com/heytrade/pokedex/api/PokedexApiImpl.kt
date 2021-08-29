package com.heytrade.pokedex.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.heytrade.pokedex.core.PokemonFaver
import com.heytrade.pokedex.core.PokemonSearcher
import com.heytrade.pokedex.logger.logger
import com.heytrade.pokedex.model.Pokemon
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("pokedex")
class PokedexApiImpl(val pokemonSearcher: PokemonSearcher, val pokemonFaver: PokemonFaver) {

    @GetMapping("search")
    fun search(@RequestParam(required = false) name: String?): List<Pokemon> {
        logger().info("Search Pokemon by name: $name")
        return pokemonSearcher.searchBy(name)
    }

    @PostMapping("fav/{id}")
    fun favPokemon(@PathVariable("id", required = true) id: Int): FavDto {
        logger().info("Fav Pokemon: $id")
        val response = pokemonFaver.favPokemon(id)
        return if (response.isLeft) FavDto(errorMessage = response.left.errorMessage)
        else FavDto(pokemon = response.get())
    }

    @DeleteMapping("fav/{id}")
    fun unfavPokemon(@PathVariable("id", required = true) id: Int): FavDto {
        logger().info("Unfav Pokemon: $id")
        val response = pokemonFaver.unfavPokemon(id)
        return if (response.isLeft) FavDto(errorMessage = response.left.errorMessage)
        else FavDto(pokemon = response.get())
    }

    @JsonInclude(NON_NULL)
    data class FavDto(val errorMessage: String? = null, val pokemon: Pokemon? = null)
}