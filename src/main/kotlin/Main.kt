package ru.netology

fun main() {
    println("Работу выполнил Евстропов Александр")

    val postWithAttachments = Post(
        text = "Пост с разными вложениями",
        attachments = arrayOf(
            Attachment.PhotoAttachment(
                Photo(1, 123, "small.jpg", "big.jpg")
            ),
            Attachment.VideoAttachment(
                Video(2, 123, "Мой кот", 60)
            ),
        )
    )
    WallService.add(postWithAttachments)

    postWithAttachments.attachments?.forEach { attachment ->
        println(getAttachmentInfo(attachment))
    }

}