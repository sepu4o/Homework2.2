package ru.netology

data class Post(
    val id: Int? = 0,
    val ownerId: Int = 0,
    val fromId: Int = 0,
    val date: Int? = 0,
    val text: String? = "",
    val postType: String = "post",
    val friendsOnly: Boolean = false,
    val canPin: Boolean = true,
    val canDelete: Boolean = true,
    val comments: Comment = Comment(),
    val attachments: Array<Attachment>? = null
)

data class Comment(
    val cid: Int? = 0,
    val postId: Int = 0,
    val ownerId: Int = 0,
    val date: Int? = 0,
    val text: String? = "",
    val replyToUid: Int = 0,
    val replyToCid: Int = 0,
    val attachments: Array<Attachment>? = null
)

data class CommentReport(
    val ownerId: Int = 0,
    val commentId: Int = 0,
    val reason: ReportReason
)

data class NoteComment(
    val commentId: Int = 0,
    val noteId: Int = 0,
    val fromId: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val text: String = "",
    var isDeleted: Boolean = false
)

data class Note(
    val id: Int = 0,
    val title: String = "",
    val text: String = "",
    val ownerId: Int = 0,
    val date: Long = System.currentTimeMillis()
)

enum class ReportReason(val code: Int) {
    SPAM(0),
    CHILD_PORNOGRAPHY(1),
    EXTREMISM(2),
    VIOLENCE(3),
    DRUG_PROPAGANDA(4),
    ADULT_MATERIAL(5),
    INSULT(6),
    SUICIDE_CALLS(7)
}

data class User(
    val id: Int = 0,
    val name: String = "",
)

data class Chat(
    val id: Int = 0,
    val ownerId: Int = 0,
    val companionId: Int = 0,
    var isDeleted: Boolean = false
)

data class Message(

    val id: Int = 0,
    val chatId: Int = 0,
    val fromUserId: Int = 0,
    var text: String = "",
    var isDeleted: Boolean = false,
    var isRead: Boolean = false,
    val authorId: Int = 0  
)

class PostNotFoundException(message: String) : Exception(message)
class CommentNotFoundException(message: String) : RuntimeException(message)
class NoteNotFoundException(message: String) : RuntimeException(message)
class CommentAlreadyDeletedException(message: String) : RuntimeException(message)
class ChatNotFoundException(message: String) : RuntimeException(message)
class MessageNotFoundException(message: String) : RuntimeException(message)