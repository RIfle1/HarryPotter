package project.classes;
import project.enums.Core;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Wand {
    Core core;
    int size;

}
