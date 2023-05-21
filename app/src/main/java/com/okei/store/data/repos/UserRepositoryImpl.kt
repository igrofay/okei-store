package com.okei.store.data.repos

import android.util.Log
import com.okei.store.data.data_source.api.vk.VKApi
import com.okei.store.data.data_source.database.UserDatabase
import com.okei.store.domain.model.error.ProfileError
import com.okei.store.domain.model.user.UserModel
import com.okei.store.domain.repos.UserRepository
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val vkApi: VKApi,
    private val userDatabase: UserDatabase,
) : UserRepository {

    override suspend fun getUser(): UserModel {
        return try {
            withContext(Dispatchers.Default) {
                val photo = async { vkApi.getPhotoProfile() }
                    .apply { start() }
                val profile = async { vkApi.getProfile() }
                    .apply { start() }
                profile.await()
                    .fromVKUserToUserModel(
                        photo.await()
                    )
            }
        } catch (e: VKApiExecutionException) {
            throw ProfileError.VKApiError
        }catch (e: JSONException){
            throw ProfileError.JsonProcessingError
        }catch (e: UnknownHostException){
            throw ProfileError.NoNetworkAccess
        }
    }

    override fun isUserAuthorized() =
        userDatabase.getIsAuthorized()

    override fun setUserIsAuth(isAuth: Boolean) {
        userDatabase.setIsAuthorized(isAuth)
    }
}