package org.softsuave.bustlespot.organisation.data

import com.example.Database
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softsuave.bustlespot.Log
import org.softsuave.bustlespot.SessionManager
import org.softsuave.bustlespot.auth.signin.data.BaseResponse
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.data.local.toDomain
import org.softsuave.bustlespot.data.network.APIEndpoints.GETALLORGANISATIONS
import org.softsuave.bustlespot.data.network.BASEURL
import org.softsuave.bustlespot.data.network.models.response.ErrorResponse
import org.softsuave.bustlespot.data.network.models.response.GetAllOrganisations
import org.softsuave.bustlespot.data.network.models.response.Organisation

class OrganisationRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager,
    private val db: Database
) : OrganisationRepository {

    // Fetch organisations with an offline-first approach
    override fun getAllOrganisation(): Flow<Result<GetAllOrganisations>> = flow {
        emit(Result.Loading)

        // 1️⃣ Emit cached data from SQLDelight first
        val localData = getLocalOrganisations()
        if (localData.isNotEmpty()) {
            Log.d("Data fetched from local DB")
            emit(Result.Success(GetAllOrganisations(listOfOrganisations = localData)))
        } else {

            // 2️⃣ Fetch from API and update local storage
            try {
                val response: HttpResponse = httpClient.get("$BASEURL$GETALLORGANISATIONS") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(sessionManager.accessToken)
                }

                if (response.status == HttpStatusCode.OK) {
                    val result: BaseResponse<GetAllOrganisations> = response.body()
                    // Update local database with fresh data
                    saveOrganisationsToLocal(result.data ?: GetAllOrganisations(listOf()))
                    emit(Result.Success(result.data ?: GetAllOrganisations(listOf())))
                } else {
                    val res: BaseResponse<ErrorResponse> = response.body()
                    emit(Result.Error("${res.message}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }

    // ✅ Get all organisations from SQLDelight
    private fun getLocalOrganisations(): List<Organisation> {
        Log.d("data is fetching")
     return db.databaseQueries.selectAllOrganisations().executeAsList().map { it.toDomain() }
    }

    // ✅ Save organisations to SQLDelight
    private fun saveOrganisationsToLocal(data: GetAllOrganisations) {

        db.databaseQueries.transaction {
            // Clear old data to prevent duplicates
//            Log.d("Organisations saved to local DB")
            Log.d("${getLocalOrganisations()} orgs in db")
            db.databaseQueries.deleteAllOrganisations()
            Log.d("removed all data")
            Log.d("${getLocalOrganisations()} orgs in db")
            // Insert fresh data
            data.listOfOrganisations.forEach { org ->
                db.databaseQueries.insertOrganisation(
                    organisationId = org.organisationId.toLong(),
                    name = org.name,
                    imageUrl = org.imageUrl,
                    roleId = org.roleId.toLong(),
                    enableScreenshot = org.enableScreenshot.toLong(),
                    description = org.description,
                    role = org.role,
                    otherRoleIds =if(org.otherRoleIds.isNotEmpty()) org.otherRoleIds.joinToString(",") else ""  // Convert list to string
                )
            }
            //if(org.otherRoleIds.isNotEmpty()) org.otherRoleIds.joinToString(",") else
            Log.d("Organisations saved to local DB")
            Log.d("${getLocalOrganisations()} orgs in db")
        }
    }
}
