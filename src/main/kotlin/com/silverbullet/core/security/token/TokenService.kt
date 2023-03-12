package com.silverbullet.core.security.token

import com.silverbullet.core.security.token.model.RefreshTokenData
import com.silverbullet.core.security.token.model.TokenClaim

interface TokenService {

    /**
     * @return pair of (access token, refresh token)
     */
    fun generateUserTokens(vararg claims: TokenClaim): Pair<String, String>

    fun decodeRefreshToken(token: String): RefreshTokenData?

}