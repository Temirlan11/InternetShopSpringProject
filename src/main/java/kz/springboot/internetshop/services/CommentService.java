package kz.springboot.internetshop.services;

import kz.springboot.internetshop.entities.Comment;
import kz.springboot.internetshop.entities.ShopItem;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentByItemId(Long id);
    List<Comment> getAllComments();
    Comment addComment(Comment comment);
    void deleteComment(Comment comment);
    Comment getComment(Long id);
}
