package com.heytrade.pokedex.model

data class Pokemon(val id: Int, val name: String, val types: List<Type>) {
    // XXX This could probably handle more than it does right now, like strengths and weaknesses
    data class Type(val type: String)
}
