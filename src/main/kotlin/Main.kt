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
    val comments: Comments = Comments(),
    val attachment: Array<Attachment>? = null
)

data class Comments(
    val count: Int = 0,
    val canPost: Boolean = false,
    val groupsCanPost: Boolean = false,
    val canClose: Boolean = true,
    val canOpen: Boolean = true
)

object WallService {
    private var posts = emptyArray<Post>()
    private var nextId = 1


    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts += newPost
        return newPost
    }


    fun update(post: Post): Boolean {
        for ((index, existingPost) in posts.withIndex()) {
            if (existingPost.id != null && post.id != null && existingPost.id == post.id){
                posts[index] = post
                return true
            }
        }
        return false
    }

    fun getPosts(): Array<Post> {
        return posts.copyOf()
    }

    fun clear() {
        posts = emptyArray()
        nextId = 1
    }
}

fun main() {
    println("Работу выполнил Евстропов Александр")
}