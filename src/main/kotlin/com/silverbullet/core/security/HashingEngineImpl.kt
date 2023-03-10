package com.silverbullet.core.security

import org.mindrot.jbcrypt.BCrypt

class HashingEngineImpl : HashingEngine {

    override fun hash(password: String): SaltedHash {
        val salt = BCrypt.gensalt()
        val hashedPw = BCrypt.hashpw(password, salt)
        return SaltedHash(salt = salt, saltedHash = hashedPw)
    }

    override fun validate(password: String, saltedHash: SaltedHash): Boolean {
        val generatedHash = BCrypt.hashpw(password, saltedHash.salt)
        return generatedHash == saltedHash.saltedHash
    }
}