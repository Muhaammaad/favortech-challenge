package  com.muhaammaad.challenge.model

import com.google.gson.annotations.SerializedName

/**
 * Pojo classes of unsplash response
 */
class unsplashResponse {

    @SerializedName("id")
    var photoId: String? = null
    var createdAt: String? = null
    var updatedAt: String? = null
    var promotedAt: String? = null
    var width: Int = 0
    var height: Int = 0
    var color: String? = null
    var description: String? = null
    var altDescription: Any? = null
    var urls: Urls? = null
    var links: Links? = null
    var categories: List<Any>? = null
    var likes: Int = 0
    var likedByUser: Boolean? = null
    var currentUserCollections: List<Any>? = null
    var user: User? = null
    var sponsorship: Sponsorship? = null
}

class Links {

    var self: String? = null
    var html: String? = null
    var photos: String? = null
    var likes: String? = null
    var portfolio: String? = null
    var following: String? = null
    var followers: String? = null
    var download: String? = null

}

class User {

    var id: String? = null
    var updatedAt: String? = null
    var username: String? = null
    var name: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var twitterUsername: String? = null
    var portfolioUrl: String? = null
    var bio: String? = null
    var location: String? = null
    var links: Links? = null
    var profileImage: ProfileImage? = null
    var instagramUsername: String? = null
    var totalCollections: Int = 0
    var totalLikes: Int = 0
    var totalPhotos: Int = 0
    var acceptedTos: Boolean? = null

}

class Sponsorship {

    var impressionUrls: List<String>? = null
    var tagline: String? = null
    var sponsor: Sponsor? = null

}

class Urls {

    var raw: String? = null
    var full: String? = null
    var regular: String? = null
    var small: String? = null
    var thumb: String? = null

}

class ProfileImage {

    var small: String? = null
    var medium: String? = null
    var large: String? = null

}

class Sponsor {

    var id: String? = null
    var updatedAt: String? = null
    var username: String? = null
    var name: String? = null
    var firstName: String? = null
    var lastName: Any? = null
    var twitterUsername: String? = null
    var portfolioUrl: String? = null
    var bio: String? = null
    var location: String? = null
    var links: Links? = null
    var profileImage: ProfileImage? = null
    var instagramUsername: String? = null
    var totalCollections: Int = 0
    var totalLikes: Int = 0
    var totalPhotos: Int = 0
    var acceptedTos: Boolean? = null

}