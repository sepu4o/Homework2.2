package ru.netology

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun add() {
        val newPost = Post(text = "Тест")
        val result = WallService.add(newPost)
        assertEquals(1, result.id)
    }

    @Test
    fun updateExistingPost() {
        val post = WallService.add(Post(text = "Старый текст"))
        val updatedPost = post.copy(text = "Новый текст")
        val result = WallService.update(updatedPost)
        assertTrue(result)
        val posts = WallService.getPosts()
        assertEquals("Новый текст", posts[0].text)
    }

    @Test
    fun updateNonExistingPost() {
        val nonExistingPost = Post(id = 9, text = "Несуществующий")
        val result = WallService.update(nonExistingPost)
        assertFalse(result)
    }

    @Test
    fun createCommentCheck() {
        val post = WallService.add(Post(text = "Новый текст"))
        val newComment = Comment(text = "Новый комментарий")
        val result = WallService.createComment(postId = post.id!!, newComment)
        assertEquals(1, result.cid)
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
        val comment = Comment(text = "Новый комментарий")
        WallService.createComment(568, comment)
    }

    @Test
    fun reportCommentSuccess() {
        val post = WallService.add(Post(text = "Пост"))
        val comment = WallService.createComment(post.id!!, Comment(text = "Комментарий"))
        val result = WallService.reportComment(
            ownerId = 123,
            commentId = comment.cid!!,
            reason = ReportReason.SPAM
        )
        assertEquals(1, result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun checkingException() {
        val badComment = Comment(text = "Новый комментарий")
        WallService.reportComment(256, 4, reason = ReportReason.SPAM)
    }
}
