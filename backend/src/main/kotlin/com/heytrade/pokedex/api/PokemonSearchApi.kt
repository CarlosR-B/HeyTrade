package com.heytrade.pokedex.api

import com.heytrade.pokedex.model.Pokemon
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("pokedex")
class PokemonSearchApi {

    @GetMapping("search")
    fun search(@RequestParam(required = false) name: String?): List<Pokemon> {
        return listOf(Pokemon(1, "Bulbasaur", listOf("Grass", "Poison")))
    }
}