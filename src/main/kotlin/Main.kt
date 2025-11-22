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

class PostNotFoundException(message: String) : Exception(message)
class CommentNotFoundException(message: String) : Exception(message)

data class CommentReport(
    val ownerId: Int = 0,
    val commentId: Int = 0,
    val reason: ReportReason
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


object WallService {
    private var posts = emptyArray<Post>()
    private var nextId = 1
    private var comments = emptyArray<Comment>()
    private var nextCommentId = 1
    private var commentReports = mutableListOf<CommentReport>()


    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts += newPost
        return newPost
    }

    fun update(post: Post): Boolean {
        for ((index, existingPost) in posts.withIndex()) {
            if (existingPost.id != null && post.id != null && existingPost.id == post.id) {
                posts[index] = post.copy(id = existingPost.id)
                return true
            }
        }
        return false
    }

    fun createComment(postId: Int, comment: Comment): Comment {
        for (i in posts.indices) {
            if (posts[i].id == postId) {
                val newComment = comment.copy(cid = nextCommentId++)
                comments = comments.plus(newComment)
                return newComment
            }
        }
        throw PostNotFoundException("Пост c id $postId не найден")
    }

    fun reportComment(ownerId: Int, commentId: Int, reason: ReportReason): Int {
        val commentExists = comments.any { it.cid == commentId }
        if (!commentExists) {
            throw CommentNotFoundException("Комментарий с id $commentId не найден")
        }
        val report = CommentReport(ownerId, commentId, reason)
        commentReports.add(report)
        return 1
    }

    fun getPosts(): Array<Post> {
        return posts.copyOf()
    }

    fun clear() {
        posts = emptyArray()
        comments = emptyArray()
        commentReports.clear()
        nextId = 1
        nextCommentId = 1
    }
}

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