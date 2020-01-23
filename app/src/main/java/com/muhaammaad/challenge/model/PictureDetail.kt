package com.muhaammaad.challenge.model

import android.os.Parcel
import android.os.Parcelable

class PictureDetail : Parcelable {

    //region Members
    /**
     * All the member variable that use to store information
     */
    var photoId: String? = null
    var photoLikes: Int = 0
    var photoDownloadUrl: String? = null
    var photoThumbnailUrl: String? = null
    var mPhotoBytes: String? = null
    var photoAuthorName: String? = null
    var photoColor: String? = null
    var photoAuthorUserName: String? = null
    var userProfilePictureUrl: String? = null
    //endregion
    //region Constructors
    constructor(
        mPhotoId: String?, mPhotoLikes: Int, mPhotoDownloadUrl: String?,
        mPhotoThumbnailUrl: String?, mPhotoAuthorName: String?,
        mPictureAuthorUserName: String?, mPictureColor: String?,
        mUserProfilePictureUrl: String?
    ) {
        this.photoId = mPhotoId
        this.photoLikes = mPhotoLikes
        this.photoDownloadUrl = mPhotoDownloadUrl
        this.photoThumbnailUrl = mPhotoThumbnailUrl
        this.photoAuthorName = mPhotoAuthorName
        this.photoAuthorUserName = mPictureAuthorUserName
        this.photoColor = mPictureColor
        this.userProfilePictureUrl = mUserProfilePictureUrl
    }

    constructor(
        mPhotoId: String?,
        mPhotoBytes: String?,
        photoAuthorName: String?
    ) {
        this.photoId = mPhotoId
        this.mPhotoBytes = mPhotoBytes
        this.photoAuthorName = photoAuthorName
    }

    private constructor(`in`: Parcel) {
        this.photoId = `in`.readString()
        this.photoLikes = `in`.readInt()
        this.photoDownloadUrl = `in`.readString()
        this.photoThumbnailUrl = `in`.readString()
        this.photoAuthorName = `in`.readString()
        this.photoAuthorUserName = `in`.readString()
        this.photoAuthorUserName = `in`.readString()
        this.mPhotoBytes = `in`.readString()
    }
    //endregion
    //region Parcel Funcations
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(photoId)
        dest.writeInt(photoLikes)
        dest.writeString(photoDownloadUrl)
        dest.writeString(photoThumbnailUrl)
        dest.writeString(photoAuthorName)
        dest.writeString(photoAuthorUserName)
        dest.writeString(userProfilePictureUrl)
        dest.writeString(mPhotoBytes)
    }

    companion object CREATOR : Parcelable.Creator<PictureDetail> {
        override fun createFromParcel(parcel: Parcel): PictureDetail {
            return PictureDetail(parcel)
        }

        override fun newArray(size: Int): Array<PictureDetail?> {
            return arrayOfNulls(size)
        }
    }
    //endregion
}
