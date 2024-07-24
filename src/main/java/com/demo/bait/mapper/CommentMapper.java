package com.demo.bait.mapper;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    List<CommentDTO> toDtoList(List<Comment> comments);
    CommentDTO toDto(Comment comment);
}
