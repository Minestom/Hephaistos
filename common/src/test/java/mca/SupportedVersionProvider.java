package mca;

import org.jglrxavpok.hephaistos.mca.SupportedVersion;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class SupportedVersionProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(SupportedVersion.MC_1_16),
                Arguments.of(SupportedVersion.MC_1_17_0),
                Arguments.of(SupportedVersion.MC_1_18_PRE_4)
        );
    }
}
