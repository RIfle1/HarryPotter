package project.classes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Levels {
    private List<Level> levels;

    public Levels() {
        this.levels = Level.returnAllLevels();
    }
}
