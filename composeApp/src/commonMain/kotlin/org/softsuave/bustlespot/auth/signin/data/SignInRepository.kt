package org.softsuave.bustlespot.auth.signin.data

import kotlinx.coroutines.flow.Flow
import org.softsuave.bustlespot.auth.utils.Result

interface SignInRepository {
    suspend fun testRoot(): Flow<Result<Any>>
    fun signIn(email: String, password: String): Flow<Result<User>>
}
