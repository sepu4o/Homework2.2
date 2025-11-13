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
}