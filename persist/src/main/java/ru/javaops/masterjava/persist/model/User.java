package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    @Column("city_id")
    //some problem in naming with 'camel case style'
    private @NonNull Integer city_id;
    @Column("group_id")
    private @NonNull Integer group_id;

    public User(Integer id, String fullName, String email, UserFlag flag, Integer cityId, Integer groupId) {
        this(fullName, email, flag, cityId, groupId);
        this.id=id;
    }
}