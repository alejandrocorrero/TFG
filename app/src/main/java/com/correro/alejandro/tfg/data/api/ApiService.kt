package com.correro.alejandro.tfg.data.api

import com.correro.alejandro.tfg.data.api.models.LoginResponse
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.AttachmentCreatedResponse
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.AttachmentResponse
import com.correro.alejandro.tfg.data.api.models.chronicresponse.ChronicResponse
import com.correro.alejandro.tfg.data.api.models.citationresponse.CitationResponse
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicResponse
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.ConsultPatientResponse
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsListResponse
import com.correro.alejandro.tfg.data.api.models.createcitationresponse.CitationCreatedResponse
import com.correro.alejandro.tfg.data.api.models.createconsultresponse.CreateConsultResponse
import com.correro.alejandro.tfg.data.api.models.createdResponse.ResponseResponse
import com.correro.alejandro.tfg.data.api.models.historialresponse.HistoricalResponse
import com.correro.alejandro.tfg.data.api.models.medichoraryresponse.MedicHoraryResponse
import com.correro.alejandro.tfg.data.api.models.reciperesponse.RecipesResponse
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.SpecialtiesResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming


interface ApiService {
    @FormUrlEncoded
    @POST("oauth/v2/token")
    fun login(@Field("username") username: String, @Field("password") password: String, @Field("grant_type") type: String, @Field("client_id") clientid: String, @Field("client_secret") clientSecret: String): Observable<LoginResponse>

    @GET("api/patient/user")
    fun getUser(@Header("Authorization") token: String): Observable<UserResponse>

    @GET("api/patient/historical")
    fun getHistorical(@Header("Authorization") token: String): Observable<HistoricalResponse>

    @GET("api/patient/chronic")
    fun getChronics(@Header("Authorization") token: String): Observable<ChronicResponse>

    @GET("api/patient/recipeshistorical/{id}")
    fun getRecipesHistorical(@Header("Authorization") token: String, @Path("id") id: Int): Observable<RecipesResponse>

    @GET("api/patient/recipes")
    fun getRecipes(@Header("Authorization") token: String): Observable<RecipesResponse>

    @GET("api/patient/citations")
    fun getCitationsPatient(@Header("Authorization") token: String): Observable<CitationResponse>

    @GET("api/patient/medichorary")
    fun getHoraryMedic(@Header("Authorization") token: String): Observable<MedicHoraryResponse>

    @GET("api/patient/citationsmedic")
    fun getCitationsMedicUsed(@Header("Authorization") token: String): Observable<CitationMedicResponse>

    @Multipart
    @POST("api/patient/adjunto/new")
    fun postTest(@Header("Authorization") token: String, @Part file: MultipartBody.Part, @Field("name") name: String): Observable<AttachmentCreatedResponse>

    @FormUrlEncoded
    @POST("api/patient/create_citation")
    fun createCitation(@Header("Authorization") token: String, @Field("day") day: String, @Field("time") time: String): Observable<CitationCreatedResponse>

    @GET("api/patient/attachments")
    fun getAttachments(@Header("Authorization") token: String): Observable<AttachmentResponse>

    @Streaming
    @GET("api/patient/file/{url}")
    fun downloadFile(@Header("Authorization") token: String, @Path("url") fileUrl: String): Observable<Response<ResponseBody>>

    @GET("api/patient/consults")
    fun getConsults(@Header("Authorization") token: String): Observable<ConsultsListResponse>

    @GET("api/patient/specialties")
    fun getSpecialties(@Header("Authorization") token: String): Observable<SpecialtiesResponse>


    @POST("api/patient/create_consult_medic")
    fun createConsultMEdic(@Header("Authorization") token: String, @Body body: RequestBody?): Observable<CreateConsultResponse>

    @POST("api/patient/create_consult_specialties")
    fun createConsultSpecialty(@Header("Authorization") token: String, @Body body: RequestBody?): Observable<CreateConsultResponse>

    @GET("api/patient/consults/{id}")
    fun getConsult(@Header("Authorization") token: String, @Path("id") idConsult: Int): Observable<ConsultPatientResponse>

    @FormUrlEncoded
    @POST("api/patient/response")
    fun createResponsePacient(@Header("Authorization") token: String,@Field("response") response:String,@Field("id_consult") id_consult:Int): Observable<ResponseResponse>

}



