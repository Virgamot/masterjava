package ru.javaops.masterjava.service.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.model.EmailResult;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailDao implements AbstractDao {

    @SqlUpdate("INSERT INTO email_result (email_to, subject, body, result)  VALUES (:emailTo, :subject, :body, :result)")
    @GetGeneratedKeys
    public abstract int insertGeneratedId(@BindBean EmailResult email);

    @SqlQuery("SELECT * FROM email_result ORDER BY email_to")
    public abstract List<EmailResult> getAll();

    public void insert(EmailResult email) {
        int id = insertGeneratedId(email);
        email.setId(id);
    }

    @SqlUpdate("TRUNCATE email_result CASCADE")
    @Override
    public abstract void clean();
}
