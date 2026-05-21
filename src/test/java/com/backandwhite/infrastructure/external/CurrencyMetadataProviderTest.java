package com.backandwhite.infrastructure.external;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.infrastructure.external.CurrencyMetadataProvider.CurrencyMeta;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CurrencyMetadataProviderTest {

    @Test
    void get_knownCurrency_returnsMetadata() {
        CurrencyMeta usd = CurrencyMetadataProvider.get("USD");
        assertThat(usd).isNotNull();
        assertThat(usd.currencyName()).isEqualTo("United States Dollar");
        assertThat(usd.currencySymbol()).isEqualTo("$");
        assertThat(usd.countryCode()).isEqualTo("US");
        assertThat(usd.timezone()).isEqualTo("America/New_York");
        assertThat(usd.language()).isEqualTo("en");
    }

    @Test
    void get_unknownCurrency_returnsNull() {
        assertThat(CurrencyMetadataProvider.get("ZZZ")).isNull();
    }

    @Test
    void getAll_returnsUnmodifiableViewWithKnownEntries() {
        Map<String, CurrencyMeta> all = CurrencyMetadataProvider.getAll();
        assertThat(all).containsKey("USD").containsKey("EUR").containsKey("BRL").hasSizeGreaterThan(50);
    }

    @Test
    void defaultMeta_longCode_usesFirstTwoCharsAsCountryCode() {
        CurrencyMeta meta = CurrencyMetadataProvider.defaultMeta("ABC");
        assertThat(meta).isNotNull();
        assertThat(meta.currencyName()).isEqualTo("ABC");
        assertThat(meta.currencySymbol()).isEqualTo("ABC");
        assertThat(meta.countryName()).isEqualTo("Unknown");
        assertThat(meta.countryCode()).isEqualTo("AB");
        assertThat(meta.timezone()).isEqualTo("UTC");
        assertThat(meta.language()).isEqualTo("en");
    }

    @Test
    void defaultMeta_shortCode_fallsBackToXX() {
        CurrencyMeta meta = CurrencyMetadataProvider.defaultMeta("X");
        assertThat(meta.countryCode()).isEqualTo("XX");
    }

    @Test
    void defaultMeta_emptyCode_fallsBackToXX() {
        CurrencyMeta meta = CurrencyMetadataProvider.defaultMeta("");
        assertThat(meta.countryCode()).isEqualTo("XX");
    }
}
