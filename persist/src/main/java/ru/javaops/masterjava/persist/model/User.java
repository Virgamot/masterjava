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
    private Integer group_id;

    public User(Integer id, String fullName, String email, UserFlag flag, Integer cityId, Integer groupId) {
        this(fullName, email, flag, cityId);
        this.group_id = groupId;
        this.id=id;
    }

    public User(String fullName, String email, UserFlag flag, Integer city_id, Integer group_id) {
        this.fullName = fullName;
        this.email = email;
        this.flag = flag;
        this.city_id = city_id;
        this.group_id = group_id;
    }
}