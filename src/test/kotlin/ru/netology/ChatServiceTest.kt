package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    // удалить чат
    @Test
    fun deleteChat() {
        val message = ChatService.createMessage(2, 5, "Hello")
        val result = ChatService.deleteChat(chatId = message.chatId)
        assertTrue(result)
    }

    // получать список имеющихся чатов
    @Test
    fun getChats() {
        val chat = ChatService.createMessage(2, 5, "Hello")
        val result = ChatService.getChats(userId = 2)
        assertEquals(1, result.size)
    }

    //Получить список последних сообщений из чата
    @Test
    fun getLastMessagesFromUserChats() {
        val message = ChatService.createMessage(2, 5, "Hello")
        val message1 = ChatService.createMessage(2, 7, "Hello World")
        val result = ChatService.getLastMessagesFromUserChats(userId = 2)
        assertEquals(2, result.size)
    }

    //Получить список сообщений из чата, указав:ID собеседника
    @Test
    fun getListMessagesIdUser() {
        val message = ChatService.createMessage(2, 5, "Hello")
        val message1 = ChatService.createMessage(2, 5, "Hello World")
        val result = ChatService.getListMessagesIdUser(companionUserId = 5, currentUserId = 2, count = 2)
        assertEquals(2, result.size)
        assertTrue(result.all{it.isRead})
    }

    //Создать новое сообщение
    @Test
    fun createMessage() {
        val result = ChatService.createMessage(2, 5, "Hello")
        assertEquals(1, result.id)
    }

    //редактировать сообщение
    @Test
    fun editMessage() {
        val message = ChatService.createMessage(2, 5, "Hello")
        val result = ChatService.editMessage(1,"Hellooooooo")
        assertTrue(result)
    }

    //удалить сообщение
    @Test
    fun deleteMessage() {
        val message = ChatService.createMessage(2, 5, "Hello")
        val result = ChatService.deleteMessage(messageId = 1)
        assertTrue(result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun checkingExceptionMessage() {
        ChatService.editMessage(86, "Hello")
        ChatService.deleteMessage(896)
    }
}