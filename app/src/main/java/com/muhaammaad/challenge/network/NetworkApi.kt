package  com.muhaammaad.challenge.network

import com.muhaammaad.challenge.model.unsplashResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * The interface which provides methods to get result of webservices
 */
interface NetworkApi {
    /**
     * Get the list of the photos from the Unsplash API
     */
    @GET
    fun getPhotos(@Url url : String): Single<List<unsplashResponse>>
}

