package org.joseph.friendsync.repository.comments

import org.joseph.friendsync.models.comments.CommentListResponse
import org.joseph.friendsync.models.comments.CommentResponse
import org.joseph.friendsync.util.Response

interface CommentsRepository {

    suspend fun addCommentToPost(userId: Int, postId: Int, commentText: String): Response<CommentResponse>


    suspend fun deleteCommentById(commentId: Int): Response<Int>


    suspend fun editCommentById(commentId: Int, editedText: String): Response<Int>


    suspend fun fetchAllPostComments(postId: Int): Response<CommentListResponse>


}