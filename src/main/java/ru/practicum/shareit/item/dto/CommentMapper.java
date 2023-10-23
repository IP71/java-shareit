package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

/*
Класс для преобразования экземпляров Comment и CommentDto друг в друга
*/

public class CommentMapper {
    // Метод получает экземпляр класса Comment и возвращает экземпляр CommentDto
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemDto(ItemMapper.toItemDto(comment.getItem()))
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    // Метод получает экземпляры классов CommentDto, Item и User и возвращает экземпляр Comment
    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(item)
                .author(author)
                .created(commentDto.getCreated())
                .build();
    }
}
