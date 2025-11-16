package ru.netology

interface Attachment {
    val type: String
}

class Photo(
   val  id: Int,
   val  ownerId: Int,
   val  photo130: String,
   val  photo604: String
)

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val duration: Int
)

data class Audio(
    val id: Int,
    val ownerId: Int,
    val artist: String,
    val title: String,
    val duration: Int
)

data class Page(
    val id : Int,
    val groupId: Int,
    val title: String,
)

data class Thumb(
    val  id: Int = 0,
    val  ownerId: Int = 0,
    val  photo130: String = "",
    val  photo604: String = ""
)

data class Album(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description : String,
    val created : Long,
    val updated: Long,
    val size: Int,
    val thumb:Thumb = Thumb()
)


data class PhotoAttachment(val photo: Photo) : Attachment {
    override val type = "photo"
}

data class VideoAttachment(val video: Video) : Attachment {
    override val type = "video"
}

data class AudioAttachment(val audio: Audio) : Attachment {
    override val type = "audio"
}

data class PageAttachments(val page: Page) : Attachment {
    override val type = "page"
}

data class AlbumAttachments(val album: Album) : Attachment {
    override val type = "album"
}

