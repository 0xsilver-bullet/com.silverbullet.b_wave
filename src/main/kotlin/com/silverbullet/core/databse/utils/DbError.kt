package com.silverbullet.core.databse.utils

sealed interface DbError{

    /**
     * @param name the column name which has a duplicate value
     */
    data class DuplicateKey(
        val name: String
    ): DbError

    data class ViolatedConstraint(
        val constraint: String?
    ): DbError

    object UnknownError: DbError
}