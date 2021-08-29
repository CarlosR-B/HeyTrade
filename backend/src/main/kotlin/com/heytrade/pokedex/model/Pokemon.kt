package com.heytrade.pokedex.model

data class Pokemon(val id: Int, val name: String, val types: List<Type>) {
    data class Type(val type: String)
}
