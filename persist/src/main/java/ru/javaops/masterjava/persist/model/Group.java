package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class Group extends BaseEntity {
    private @NonNull String name;
    private @NonNull GroupType type;

    public Group(Integer id, String name, GroupType type) {
        this(name, type);
        this.id = id;
    }
}
