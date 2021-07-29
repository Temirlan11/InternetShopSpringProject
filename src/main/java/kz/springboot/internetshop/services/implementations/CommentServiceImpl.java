package kz.springboot.internetshop.services.implementations;

import kz.springboot.internetshop.entities.Comment;
import kz.springboot.internetshop.entities.ShopItem;
import kz.springboot.internetshop.repositories.CommentRepository;
import kz.springboot.internetshop.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentByItemId(Long id) {
        return commentRepository.findAllByItemId(id);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public Comment getComment(Long id) {
        return commentRepository.getOne(id);
    }
}
