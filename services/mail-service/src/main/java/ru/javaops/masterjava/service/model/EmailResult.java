package ru.javaops.masterjava.service.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmailResult extends BaseEntity {
    @Column("email_to")
    private String emailTo;
    private String subject;
    private String body;
    private String result;
}
