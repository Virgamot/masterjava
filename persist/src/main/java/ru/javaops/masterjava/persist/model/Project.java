package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class Project extends BaseEntity {
    private @NonNull String description;

    public Project(Integer id, String description) {
        this(description);
        this.id = id;
    }
}
