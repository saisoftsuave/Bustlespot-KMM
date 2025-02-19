package org.softsuave.bustlespot.organisation.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.realm.kotlin.ext.query
import io.ktor.http.*
import io.realm.kotlin.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.softsuave.bustlespot.Log
import org.softsuave.bustlespot.SessionManager
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.data.local.realme.objects.OrganisationObj
import org.softsuave.bustlespot.data.local.toLocal
import org.softsuave.bustlespot.data.network.APIEndpoints.GETALLORGANISATIONS
import org.softsuave.bustlespot.data.network.BASEURL
import org.softsuave.bustlespot.data.network.models.response.ErrorResponse
import org.softsuave.bustlespot.data.network.models.response.GetAllOrganisations
import org.softsuave.bustlespot.data.network.models.response.Organisation

class OrganisationRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager,
    private val realmDb: Realm
) : OrganisationRepository {

    // Fetch organisations with an offline-first approach
    override fun getAllOrganisation(): Flow<Result<GetAllOrganisations>> = flow {
        emit(Result.Loading)

        // 1. Emit data from the local database first
        val localData = getLocalOrganisations()
        if (localData.isNotEmpty()) {
            Log.d("data from Realm")
            emit(Result.Success(GetAllOrganisations(listOfOrganisations = localData, message = "")))
        }

        // 2. Attempt to fetch from the API and update the local database
        try {
            val response: HttpResponse = httpClient.get("$BASEURL$GETALLORGANISATIONS") {
                contentType(ContentType.Application.Json)
                bearerAuth(sessionManager.accessToken)
            }

            if (response.status == HttpStatusCode.OK) {
                val result: GetAllOrganisations = response.body()
                // Update local database with fresh data
                saveOrganisationsToLocal(result)
                emit(Result.Success(result))
            } else {
                val res: ErrorResponse = response.body()
                println(res)
                emit(Result.Error("Failed to fetch organisations: ${response.status}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }


    private suspend fun getLocalOrganisations(): List<Organisation> {
        return withContext(Dispatchers.IO) {
            realmDb.query<OrganisationObj>().find()
        }.map { it.toLocal() }
    }

    // Save organisations to the local Realm database
    private suspend fun saveOrganisationsToLocal(data: GetAllOrganisations) {

        withContext(Dispatchers.IO) {
            realmDb.write {
                // Clear old data to avoid duplicates
                deleteAll()
                data.listOfOrganisations.forEach { org ->
                    copyToRealm(OrganisationObj().apply {
                        organisationId = org.organisationId
                        organisationName = org.organisationName
                        organisationDescription = org.organisationDescription
                        isDeleted = org.isDeleted
                    })
                }
                Log.d("saved orgs to realme")
            }
        }
    }
}
