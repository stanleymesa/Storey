package com.example.storey.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAllStoriesResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem> = arrayListOf(),

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
) : Parcelable

@Entity(tableName = "list_story")
@Parcelize
data class ListStoryItem(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Float? = null,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Float? = null,
) : Parcelable
