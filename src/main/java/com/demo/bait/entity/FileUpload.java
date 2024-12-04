package com.demo.bait.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    private String storedFileName;

    private String filePath;

    private Long fileSize;
    private String fileType;

    @Lob
    private byte[] thumbnail;
}
