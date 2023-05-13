package project.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.Core;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Wand {
    Core core;
    int size;

}
