package com.demo.bait.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;

    @Lob
    private byte[] thumbnail;

//    @CreationTimestamp
//    private Timestamp uploadTime;

}
