package com.silverbullet.core.security.hashing

interface HashingEngine {

    /**
     * @param password the password of the user to hash
     * @return the salt and the hashed password
     */
    fun hash(password: String): SaltedHash

    /**
     * @return boolean whether the password is valid or not
     */
    fun validate(password: String, saltedHash: SaltedHash): Boolean
}