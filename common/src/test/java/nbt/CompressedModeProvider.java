package nbt;

import org.jglrxavpok.hephaistos.nbt.CompressedMode;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class CompressedModeProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(CompressedMode.NONE),
                Arguments.of(CompressedMode.GZIP),
                Arguments.of(CompressedMode.ZLIB)
        );
    }
}