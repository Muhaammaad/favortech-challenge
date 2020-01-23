package  com.muhaammaad.challenge.network

import com.muhaammaad.challenge.model.unsplashResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface BackEndApi {
    @GET
    fun getPhotos(@Url url : String): Call<List<unsplashResponse>>
}

