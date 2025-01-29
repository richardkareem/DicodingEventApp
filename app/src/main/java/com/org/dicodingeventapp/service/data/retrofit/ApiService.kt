package com.org.dicodingeventapp.service.data.retrofit

import com.org.dicodingeventapp.service.data.response.DetailEventResponse
import com.org.dicodingeventapp.service.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") status: Int
    ) : Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id : String
    ) : Call<DetailEventResponse>

    @GET("events?active=-1")
    fun getSearchEvenet(
        @Query("q") query : String
    ) : Call<EventResponse>


//    @FormUrlEncoded
//    @Headers("Authorization: token 12345")
//    @POST("review")
//    fun postReview(
//        @Field("id") id: String,
//        @Field("name") name: String,
//        @Field("review") review: String
//    ): Call<PostReviewResponse>
}