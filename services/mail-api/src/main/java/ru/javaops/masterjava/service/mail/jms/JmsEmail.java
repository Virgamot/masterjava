package ru.javaops.masterjava.service.mail.jms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ru.javaops.masterjava.service.mail.Addressee;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@Getter
public class JmsEmail implements Serializable {
    private String subject;
    private String body;
    private String attachedFileName;
    private Byte[] bytes;
    @NonNull
    Set<Addressee> emailsTo;
}
