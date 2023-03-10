package com.silverbullet.core.databse.utils

sealed interface DbOperation<T> {

    data class Success<T>(
        val data: T
    ) : DbOperation<T>

    data class Failure<T>(
        val error: DbError
    ) : DbOperation<T>
}