package com.heytrade.pokedex.core

import com.heytrade.pokedex.core.PokemonFavDatabase.NoPokemon
import com.heytrade.pokedex.logger.logger
import com.heytrade.pokedex.model.Pokemon
import io.vavr.control.Either
import io.vavr.control.Try
import org.springframework.stereotype.Component

interface PokemonFaver {
    fun favPokemon(id: Int): Either<FavError, Pokemon>
    fun unfavPokemon(id: Int): Either<FavError, Pokemon>
}

abstract class FavError(val errorMessage: String)
class PokemonDoesNotExistError(id: Int) : FavError("Pokemon #$id does not exist")
class DatabaseError : FavError("Could not access the database, please try again later")

interface PokemonFavDatabase {
    fun favPokemon(id: Int): Try<Either<NoPokemon, Pokemon>>
    fun unfavPokemon(id: Int): Try<Either<NoPokemon, Pokemon>>

    class NoPokemon
}

@Component
internal class PokemonFaverImpl(private val pokemonFavDatabase: PokemonFavDatabase) : PokemonFaver {
    override fun favPokemon(id: Int): Either<FavError, Pokemon> {
        val favPokemonResponse = pokemonFavDatabase.favPokemon(id)
        return when {
            favPokemonResponse.isFailure -> {
                logger().error("Error accessing the database: ", favPokemonResponse.cause)
                Either.left(DatabaseError())
            }
            else -> manageDatabaseResponse(favPokemonResponse.get(), id)
        }
    }


    override fun unfavPokemon(id: Int): Either<FavError, Pokemon> {
        val favPokemonResponse = pokemonFavDatabase.unfavPokemon(id)
        return when {
            favPokemonResponse.isFailure -> {
                logger().error("Error accessing the database: ", favPokemonResponse.cause)
                Either.left(DatabaseError())
            }
            else -> manageDatabaseResponse(favPokemonResponse.get(), id)
        }
    }

    private fun manageDatabaseResponse(databaseResponse: Either<NoPokemon, Pokemon>, id: Int)
            : Either<FavError, Pokemon> =
        if (databaseResponse.isLeft) Either.left(PokemonDoesNotExistError(id))
        else Either.right(databaseResponse.get())

}