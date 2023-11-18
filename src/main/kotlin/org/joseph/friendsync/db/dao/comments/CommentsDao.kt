package org.joseph.friendsync.db.dao.comments

import org.joseph.friendsync.models.comments.Comment

interface CommentsDao {

    suspend fun addCommentToPost(userId: Int, postId: Int, commentText: String): Comment?

    suspend fun deleteCommentById(commentId: Int): Int

    suspend fun editCommentById(commentId: Int, editedText: String): Int

    suspend fun fetchAllPostComments(postId: Int): List<Comment?>

    suspend fun fetchPostCommentsSize(postId: Int): Int
}