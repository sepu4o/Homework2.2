package ru.netology

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NoteServiceTest {

    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    // создает новую заметку у текущего пользователя
    @Test
    fun addNote() {
        val NewNote = Note(title = "New")
        val result = NoteService.addNote(NewNote)
        assertEquals(1, result.id)
    }

    // добавляет новый комментарий к заметке
    @Test
    fun createCommentNote() {
        val note = NoteService.addNote(Note(title = "New"))
        val result = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        assertEquals("New", result.text)
        assertEquals(1, result.noteId)
    }

    // добавляет новый комментарий к заметке
    @Test(expected = NoteNotFoundException::class)
    fun notFoundNote() {
        val note = NoteService.addNote(Note(title = "New"))
        val result = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        NoteService.createCommentNote(2, noteComment = NoteComment(text = "New"))
    }

    // Удаляет заметку текущего пользователя
    @Test
    fun deleteNote() {
        val note = NoteService.addNote(Note(title = "New"))
        val comment = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        val result = NoteService.delete(id = note.id)
        assertTrue(result)
    }

    // Проверка удаления комментария после удаления заметки
    @Test(expected = CommentNotFoundException::class)
    fun commentsDeletedAfterNoteDeletion() {
        val note = NoteService.addNote(Note(title = "New"))
        val comment = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        val result = NoteService.delete(note.id)
        NoteService.getComments(noteId = note.id)
    }

    // Удаляет комментарий к заметке
    @Test
    fun deleteComment() {
        val note = NoteService.addNote(Note(title = "New"))
        val comment = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        val result = NoteService.deleteComment(commentId = comment.commentId)
        assertTrue(result)
    }

    // Заметка не найдена
    @Test(expected = NoteNotFoundException::class)
    fun noteNotFound() {
        NoteService.getByID(999)
        NoteService.delete(999)
        NoteService.edit(999, "title", "text")
    }

    // Комментарий не найден
    @Test(expected = CommentNotFoundException::class)
    fun commentNotFound() {
        NoteService.deleteComment(999)
        NoteService.restoreComment(999)
        NoteService.editComment(999, "New")
    }

    // Комментарий уже удален
    @Test(expected = CommentAlreadyDeletedException::class)
    fun commentAlreadyDeletedAfterCommentDeletion() {
        val note = NoteService.addNote(Note(title = "New"))
        val comment = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        val deleteComment = NoteService.deleteComment(comment.commentId)
        val result = NoteService.deleteComment(comment.commentId)
    }

    //восстанавливает удаленный комментарий
    @Test
    fun restoreComment() {
        val note = NoteService.addNote(Note(title = "New"))
        val comment = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        val deleteComment = NoteService.deleteComment(comment.commentId)
        val result = NoteService.restoreComment(comment.commentId)
        assertTrue(result)
    }

    // Возвращает список заметок, созданных пользователем
    @Test
    fun getNotes() {
        val note = NoteService.addNote(Note(title = "New"))
        val notes = NoteService.getNotes(ownerId = note.ownerId)
        assertEquals(1, notes.size)
    }

    //возвращает заметку по ее ID
    @Test
    fun getByID() {
        val note = NoteService.addNote(Note(title = "New"))
        val result = NoteService.getByID(note.id)
        assertEquals(1, result.id)
    }

    //возвращает список комментариев к заметке
    @Test
    fun getComments() {
        val note = NoteService.addNote(Note(title = "New"))
        val comment = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        val result = NoteService.getComments(noteId = note.id)
        assertEquals(1, result.size)
    }

    //редактирует заметку текущего пользователя
    @Test
    fun edit() {
        val note = NoteService.addNote(Note(title = "New", text = "Hello"))
        val result = NoteService.edit(id = note.id, newTitle = "Hi", newText = "Hello World")
        assertTrue(result)
    }

    // редактирует комментарий к заметке
    @Test
    fun editComment() {
        val note = NoteService.addNote(Note(title = "New"))
        val comment = NoteService.createCommentNote(noteId = note.id, noteComment = NoteComment(text = "New"))
        val result = NoteService.editComment(commentId = comment.commentId, newText = "Hello World")
        assertTrue(result)

    }
}