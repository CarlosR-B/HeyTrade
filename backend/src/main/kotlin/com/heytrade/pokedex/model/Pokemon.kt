package com.heytrade.pokedex.model

data class Pokemon(
    val id: Int,
    val name: String,
    // XXX At some point this should change into a real type system in order to track strengths and weaknesses
    val types: List<String>
)
