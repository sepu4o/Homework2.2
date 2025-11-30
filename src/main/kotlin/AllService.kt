package ru.netology

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

    }
}

class BaseService<T> {
    private val items = mutableListOf<T>()

    fun add(item: T): T {
        items.add(item)
        return item
    }

    fun removeIf(predicate: (T) -> Boolean): Boolean {
        return items.removeIf(predicate)
    }

    fun find(predicate: (T) -> Boolean): T? {
        return items.find(predicate)
    }

    fun update(predicate: (T) -> Boolean, updateFn: (T) -> T): Boolean {
        val index = items.indexOfFirst(predicate)
        if (index != -1) {
            items[index] = updateFn(items[index])
            return true
        }
        return false
    }

    fun getAll(): List<T> = items.toList()

    fun clear() = items.clear()
}

object NoteService {
    private var nextCommentId = 1
    private var nextNoteId = 1
    private val noteService = BaseService<Note>()
    private val commentService = BaseService<NoteComment>()


    // создает новую заметку у текущего пользователя
    fun addNote(note: Note): Note {
        val newNote = note.copy(id = nextNoteId++)
        return noteService.add(newNote)
    }

    // добавляет новый комментарий к заметке
    fun createCommentNote(noteId: Int, noteComment: NoteComment): NoteComment {
        val note = noteService.getAll().find { it.id == noteId }
        if (note != null) {
            val newNoteComment = noteComment.copy(commentId = nextCommentId++, noteId = noteId)
            return commentService.add(newNoteComment)
        }
        throw NoteNotFoundException("Заметка с таким id $noteId не найдена")
    }

    // Удаляет заметку текущего пользователя
    fun delete(id: Int): Boolean {
        val note = noteService.find { it.id == id }
        if (note == null) {
            throw NoteNotFoundException("Заметка с таким id $id не найдена")
        }
        noteService.removeIf { it.id == id }
        commentService.removeIf { it.noteId == id }
        return true

    }

    // Удаляет комментарий к заметке
    fun deleteComment(commentId: Int): Boolean {
        val comment = commentService.find { it.commentId == commentId }
            ?: throw CommentNotFoundException("Комментарий с таким id $commentId не найден")
        if (comment.isDeleted) {
            throw CommentAlreadyDeletedException("Комментарий уже удален")
        }
        comment.isDeleted = true // помечаем как удалённый
        return true
    }

    //восстанавливает удаленный комментарий
    fun restoreComment(commentId: Int): Boolean {
        val comment = commentService.find { it.commentId == commentId }
            ?: throw CommentNotFoundException("Комментарий с таким id $commentId не найден")
        if (comment.isDeleted) {
            comment.isDeleted = false
            return true
        }
        return false
    }

    // Возвращает список заметок, созданных пользователем
    fun getNotes(ownerId: Int): List<Note> {
        val userNote = noteService.getAll().filter { it.ownerId == ownerId }
        if (userNote.isEmpty()) {
            throw NoteNotFoundException("У пользователя $ownerId нет заметок")
        }
        return userNote
    }

    //возвращает заметку по ее ID
    fun getByID(id: Int): Note {
        return noteService.find { it.id == id }
            ?: throw NoteNotFoundException("Заметка с id $id не найдена")
    }

    //возвращает список комментариев к заметке
    fun getComments(noteId: Int): List<NoteComment> {
        return commentService.getAll().filter { it.noteId == noteId && !it.isDeleted }
            .ifEmpty { throw CommentNotFoundException("У заметке с id $noteId нет комментариев") }
    }

    //редактирует заметку текущего пользователя
    fun edit(id: Int, newTitle: String, newText: String): Boolean {
        return noteService.update(
            predicate = { it.id == id },
            updateFn = { it.copy(title = newTitle, text = newText) }
        )
    }

    // редактирует комментарий к заметке
    fun editComment(commentId: Int, newText: String): Boolean {
        return commentService.update(
            predicate = { it.commentId == commentId && !it.isDeleted },
            updateFn = { it.copy(text = newText) }
        )
    }

    fun clear() {
        noteService.clear()
        commentService.clear()
        nextNoteId = 1
        nextCommentId = 1
    }
}

//Контекст	          Имя поля / аргумента	        Описание
// User	                      id	            Уникальный идентификатор пользователя
// Chat	                      id	            Уникальный идентификатор чата
// Message	                  id	            Уникальный идентификатор сообщения
// Поле внутри Chat	        ownerId	            ID владельца аккаунта
// Поле внутри Chat	       companionId	        ID собеседника
// Поле внутри Message	     chatId	            ID чата, куда входит сообщение
// Поле внутри Message	    authorId	        ID автора сообщения
// Аргумент метода	      companionUserId	    ID пользователя-собеседника (для поиска чата)
// Аргумент метода	        toUserId	        ID пользователя, которому отправляем сообщение
// Аргумент метода	        messageId	        ID сообщения для операций (удалить, редактировать)
// Аргумент метода	          chatId	        ID чата для операций (удалить чат)

// userId - это ID пользователя, для которого мы хотим получить чаты
object ChatService {

    private var chats = mutableListOf<Chat>()
    private var messages = mutableListOf<Message>()
    private var nextChatId = 1
    private var nextMessageId = 1
    private fun generateChatId(): Int = nextChatId++
    private fun generateMessageId(): Int = nextMessageId++


    // удалить чат
    fun deleteChat(chatId: Int): Boolean {
        val chat = chats.find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")
        chat.isDeleted = true
        messages.forEach {
            if (it.chatId == chat.id) it.isDeleted = true
        }
        return true

    }

    // получать список имеющихся чатов
    fun getChats(userId: Int): List<Chat> {
        return chats.getActiveChats().filter {
            it.ownerId == userId || it.companionId == userId
        }

    }

    //Получить список последних сообщений из чата
    fun getLastMessagesFromUserChats(userId: Int): List<String> {
        return chats.getActiveChats()
            .filter { it.ownerId == userId || it.companionId == userId }
            .map { chat ->
                val lastMessage = messages.getActiveMessages()
                    .filter { it.chatId == chat.id }
                    .maxByOrNull { it.id }
                lastMessage?.text ?: "нет сообщений"
            }
    }

    //Получить список сообщений из чата, указав:ID собеседника
    fun getListMessagesIdUser(companionUserId: Int, currentUserId: Int, count: Int): List<Message> {
        val chat = chats.getActiveChats()
            .find {
                it.ownerId == currentUserId && it.companionId == companionUserId ||
                        it.ownerId == companionUserId && it.companionId == currentUserId
            }

        if (chat != null) {
            val chatMessages = messages.getActiveMessages().filter { it.chatId == chat.id }.take(count)
            chatMessages.forEach { it.isRead = true }
            return chatMessages
        }
        return listOf()
    }

    //Создать новое сообщение
    fun createMessage(fromUserId: Int, toUserId: Int, text: String): Message {
        val chat = getOrCreateChat(fromUserId, toUserId)
        val message = Message(
            id = generateMessageId(),
            chatId = chat.id,
            authorId = fromUserId,
            text = text,
            isRead = false
        )
        messages.add(message)
        return message
    }

    //редактировать сообщение
    fun editMessage(messageId: Int, newText: String): Boolean {
        val message = messages.getActiveMessages().find { it.id == messageId }
        if (message == null) {
            throw MessageNotFoundException("Сообщение с таким id $messageId не найдено")
        }
        message.text = newText

        return true
    }

    //удалить сообщение
    fun deleteMessage(messageId: Int): Boolean {
        val message = messages.getActiveMessages().find { it.id == messageId }
        if (message == null) {
            throw MessageNotFoundException("Сообщение с таким id $messageId не найдено")
        }
        message.isDeleted = true
        return true
    }

    //найди или создай чат
    private fun getOrCreateChat(userId1: Int, userId2: Int): Chat {
        val existingChat = chats.getActiveChats().find {
            it.ownerId == userId1 && it.companionId == userId2 ||
                    it.ownerId == userId2 && it.companionId == userId1
        }
        return existingChat ?: createNewChat(userId1, userId2)
    }

    // создает новый чат
    private fun createNewChat(ownerId: Int, companionId: Int): Chat {
        val newChat = Chat(
            id = generateChatId(),
            ownerId = ownerId,
            companionId = companionId
        )
        chats.add(newChat)
        return newChat
    }

    // функция для получения активных чатов
    fun List<Chat>.getActiveChats(): List<Chat> {
        return this.filter { !it.isDeleted }
    }

    // функция для получения активных сообщений
    fun List<Message>.getActiveMessages(): List<Message> {
        return this.filter { !it.isDeleted }
    }

    fun clear() {
        chats.clear()
        messages.clear()

    }
}


