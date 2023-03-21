package h1;

import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

public class H1_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H1")
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
