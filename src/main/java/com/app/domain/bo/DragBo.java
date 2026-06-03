package com.app.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@NotNull
public class DragBo {
    @NotNull
    private Integer i;
    @NotNull
    private Integer j;
}
