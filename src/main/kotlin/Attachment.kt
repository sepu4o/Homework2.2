package ru.netology

sealed class Attachment(val type: String) {
    data class PhotoAttachment(val photo: Photo) : Attachment("photo")
    data class VideoAttachment(val video: Video) : Attachment("video")
    data class AudioAttachment(val audio: Audio) : Attachment("audio")
    data class PageAttachment(val page: Page) : Attachment("page")
    data class AlbumAttachment(val album: Album) : Attachment("album")
}

data class Photo(val id: Int, val ownerId: Int, val photo130: String, val photo604: String)

data class Video(val id: Int, val ownerId: Int, val title: String, val duration: Int)

data class Audio(val id: Int, val ownerId: Int, val artist: String, val title: String, val duration: Int)

data class Page(val id: Int, val groupId: Int, val title: String)

data class Thumb(val id: Int = 0, val ownerId: Int = 0, val photo130: String = "", val photo604: String = "")

data class Album(
    val id: Int, val ownerId: Int, val title: String, val description: String, val created: Long,
    val updated: Long,
    val size: Int,
    val thumb: Thumb = Thumb()
)


fun getAttachmentInfo(attachment: Attachment): String {
    return when (attachment) {
        is Attachment.PhotoAttachment -> "Фото: ID: ${attachment.photo.id} |" +
                "Владелец: ${attachment.photo.ownerId} | Маленькое фото: ${attachment.photo.photo130} |" +
                "Большое фото: ${attachment.photo.photo604}"

        is Attachment.VideoAttachment -> "Видео: ID: ${attachment.video.id} | Владелец: ${attachment.video.ownerId}  |" +
                "Заглавие: ${attachment.video.title} | Продолжительность: ${attachment.video.duration}"

        is Attachment.AudioAttachment -> "Аудио: ID: ${attachment.audio.id} | Владелец: ${attachment.audio.ownerId} | " +
                "Артист: ${attachment.audio.artist} | Заглавие: ${attachment.audio.title} |" +
                "Продолжительность: ${attachment.audio.duration}"

        is Attachment.PageAttachment -> "Страница: ID: ${attachment.page.id} |Идентификатор группы: ${attachment.page.groupId} |" +
                "Заглавие: ${attachment.page.title}"

        is Attachment.AlbumAttachment -> "Альбом: ID: ${attachment.album.id} | Владелец: ${attachment.album.ownerId} | " +
                "Заглавие: ${attachment.album.title} | Описание: ${attachment.album.description} |" +
                "Создан: ${attachment.album.created} | Обновленный: ${attachment.album.updated} " +
                "Размер: ${attachment.album.size} | ID: ${attachment.album.thumb.id}, ${attachment.album.thumb.photo604}"

    }
}
