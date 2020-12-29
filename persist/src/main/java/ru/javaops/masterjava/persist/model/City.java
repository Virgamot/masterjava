package ru.javaops.masterjava.persist.model;


import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class City extends BaseEntity {
    private @NonNull String name;
    @Column("short_name")
    private @NonNull String short_name;

    public City(Integer id, String name, String shortName) {
        this(name, shortName);
        this.id = id;
    }
}
