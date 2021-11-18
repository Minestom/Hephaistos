package mca;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.nio.file.Paths;
import java.util.stream.Stream;

public class PathProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(Paths.get("src/test/resources/1.18-pre4regions/r.0.0.mca")),
                Arguments.of(Paths.get("src/test/resources/r.0.0.mca"))
                );
    }
}

