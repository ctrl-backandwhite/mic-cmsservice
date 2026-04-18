package com.backandwhite.infrastructure.external;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static metadata provider for all currencies returned by CurrencyLayer API.
 * Maps currency code → (currencyName, symbol, countryName, countryCode, flag,
 * timezone, language).
 */
public final class CurrencyMetadataProvider {

    private CurrencyMetadataProvider() {
    }

    public record CurrencyMeta(String currencyName, String currencySymbol, String countryName, String countryCode,
            String flagEmoji, String timezone, String language) {
    }

    private static final Map<String, CurrencyMeta> META = new ConcurrentHashMap<>();

    static {
        // ── Americas ───────────────────────────────────────────────────
        put("USD", "United States Dollar", "$", "United States", "US", "🇺🇸", "America/New_York", "en");
        put("CAD", "Canadian Dollar", "CA$", "Canada", "CA", "🇨🇦", "America/Toronto", "en");
        put("MXN", "Mexican Peso", "$", "Mexico", "MX", "🇲🇽", "America/Mexico_City", "es");
        put("ARS", "Argentine Peso", "$", "Argentina", "AR", "🇦🇷", "America/Argentina/Buenos_Aires", "es");
        put("BRL", "Brazilian Real", "R$", "Brazil", "BR", "🇧🇷", "America/Sao_Paulo", "pt");
        put("CLP", "Chilean Peso", "$", "Chile", "CL", "🇨🇱", "America/Santiago", "es");
        put("COP", "Colombian Peso", "$", "Colombia", "CO", "🇨🇴", "America/Bogota", "es");
        put("PEN", "Peruvian Sol", "S/", "Peru", "PE", "🇵🇪", "America/Lima", "es");
        put("UYU", "Uruguayan Peso", "$U", "Uruguay", "UY", "🇺🇾", "America/Montevideo", "es");
        put("PYG", "Paraguayan Guarani", "₲", "Paraguay", "PY", "🇵🇾", "America/Asuncion", "es");
        put("BOB", "Bolivian Boliviano", "Bs", "Bolivia", "BO", "🇧🇴", "America/La_Paz", "es");
        put("VEF", "Venezuelan Bolívar Fuerte", "Bs.F", "Venezuela", "VE", "🇻🇪", "America/Caracas", "es");
        put("VES", "Venezuelan Bolívar Soberano", "Bs.S", "Venezuela", "VE", "🇻🇪", "America/Caracas", "es");
        put("CRC", "Costa Rican Colón", "₡", "Costa Rica", "CR", "🇨🇷", "America/Costa_Rica", "es");
        put("PAB", "Panamanian Balboa", "B/", "Panama", "PA", "🇵🇦", "America/Panama", "es");
        put("HNL", "Honduran Lempira", "L", "Honduras", "HN", "🇭🇳", "America/Tegucigalpa", "es");
        put("SVC", "Salvadoran Colón", "₡", "El Salvador", "SV", "🇸🇻", "America/El_Salvador", "es");
        put("GTQ", "Guatemalan Quetzal", "Q", "Guatemala", "GT", "🇬🇹", "America/Guatemala", "es");
        put("NIO", "Nicaraguan Córdoba", "C$", "Nicaragua", "NI", "🇳🇮", "America/Managua", "es");
        put("DOP", "Dominican Peso", "RD$", "Dominican Republic", "DO", "🇩🇴", "America/Santo_Domingo", "es");
        put("CUP", "Cuban Peso", "₱", "Cuba", "CU", "🇨🇺", "America/Havana", "es");
        put("CUC", "Cuban Convertible Peso", "$", "Cuba", "CU", "🇨🇺", "America/Havana", "es");
        put("JMD", "Jamaican Dollar", "J$", "Jamaica", "JM", "🇯🇲", "America/Jamaica", "en");
        put("TTD", "Trinidad & Tobago Dollar", "TT$", "Trinidad and Tobago", "TT", "🇹🇹", "America/Port_of_Spain",
                "en");
        put("BBD", "Barbadian Dollar", "Bds$", "Barbados", "BB", "🇧🇧", "America/Barbados", "en");
        put("BSD", "Bahamian Dollar", "B$", "Bahamas", "BS", "🇧🇸", "America/Nassau", "en");
        put("BZD", "Belize Dollar", "BZ$", "Belize", "BZ", "🇧🇿", "America/Belize", "en");
        put("GYD", "Guyanese Dollar", "GY$", "Guyana", "GY", "🇬🇾", "America/Guyana", "en");
        put("SRD", "Surinamese Dollar", "SR$", "Suriname", "SR", "🇸🇷", "America/Paramaribo", "en");
        put("HTG", "Haitian Gourde", "G", "Haiti", "HT", "🇭🇹", "America/Port-au-Prince", "en");
        put("AWG", "Aruban Florin", "ƒ", "Aruba", "AW", "🇦🇼", "America/Aruba", "en");
        put("ANG", "Neth. Antillean Guilder", "ƒ", "Curaçao", "CW", "🇨🇼", "America/Curacao", "en");
        put("BMD", "Bermudian Dollar", "BD$", "Bermuda", "BM", "🇧🇲", "Atlantic/Bermuda", "en");
        put("KYD", "Cayman Islands Dollar", "CI$", "Cayman Islands", "KY", "🇰🇾", "America/Cayman", "en");
        put("FKP", "Falkland Islands Pound", "£", "Falkland Islands", "FK", "🇫🇰", "Atlantic/Stanley", "en");
        put("XCD", "East Caribbean Dollar", "EC$", "Eastern Caribbean", "AG", "🇦🇬", "America/Antigua", "en");

        // ── Europe ─────────────────────────────────────────────────────
        put("EUR", "Euro", "€", "España", "ES", "🇪🇸", "Europe/Madrid", "es");
        put("GBP", "British Pound Sterling", "£", "United Kingdom", "GB", "🇬🇧", "Europe/London", "en");
        put("CHF", "Swiss Franc", "CHF", "Switzerland", "CH", "🇨🇭", "Europe/Zurich", "en");
        put("NOK", "Norwegian Krone", "kr", "Norway", "NO", "🇳🇴", "Europe/Oslo", "en");
        put("SEK", "Swedish Krona", "kr", "Sweden", "SE", "🇸🇪", "Europe/Stockholm", "en");
        put("DKK", "Danish Krone", "kr", "Denmark", "DK", "🇩🇰", "Europe/Copenhagen", "en");
        put("PLN", "Polish Zloty", "zł", "Poland", "PL", "🇵🇱", "Europe/Warsaw", "en");
        put("CZK", "Czech Koruna", "Kč", "Czech Republic", "CZ", "🇨🇿", "Europe/Prague", "en");
        put("HUF", "Hungarian Forint", "Ft", "Hungary", "HU", "🇭🇺", "Europe/Budapest", "en");
        put("RON", "Romanian Leu", "lei", "Romania", "RO", "🇷🇴", "Europe/Bucharest", "en");
        put("BGN", "Bulgarian Lev", "лв", "Bulgaria", "BG", "🇧🇬", "Europe/Sofia", "en");
        put("HRK", "Croatian Kuna", "kn", "Croatia", "HR", "🇭🇷", "Europe/Zagreb", "en");
        put("RSD", "Serbian Dinar", "din", "Serbia", "RS", "🇷🇸", "Europe/Belgrade", "en");
        put("BAM", "Bosnia-Herz. Mark", "KM", "Bosnia and Herzegovina", "BA", "🇧🇦", "Europe/Sarajevo", "en");
        put("ALL", "Albanian Lek", "L", "Albania", "AL", "🇦🇱", "Europe/Tirane", "en");
        put("MKD", "Macedonian Denar", "ден", "North Macedonia", "MK", "🇲🇰", "Europe/Skopje", "en");
        put("MDL", "Moldovan Leu", "L", "Moldova", "MD", "🇲🇩", "Europe/Chisinau", "en");
        put("UAH", "Ukrainian Hryvnia", "₴", "Ukraine", "UA", "🇺🇦", "Europe/Kyiv", "en");
        put("RUB", "Russian Ruble", "₽", "Russia", "RU", "🇷🇺", "Europe/Moscow", "en");
        put("GEL", "Georgian Lari", "₾", "Georgia", "GE", "🇬🇪", "Asia/Tbilisi", "en");
        put("ISK", "Icelandic Króna", "kr", "Iceland", "IS", "🇮🇸", "Atlantic/Reykjavik", "en");
        put("TRY", "Turkish Lira", "₺", "Turkey", "TR", "🇹🇷", "Europe/Istanbul", "en");
        put("GIP", "Gibraltar Pound", "£", "Gibraltar", "GI", "🇬🇮", "Europe/Gibraltar", "en");
        put("BYN", "Belarusian Ruble", "Br", "Belarus", "BY", "🇧🇾", "Europe/Minsk", "en");

        // Legacy EUR-zone (kept for backward compat)
        put("LTL", "Lithuanian Litas", "Lt", "Lithuania", "LT", "🇱🇹", "Europe/Vilnius", "en");
        put("LVL", "Latvian Lats", "Ls", "Latvia", "LV", "🇱🇻", "Europe/Riga", "en");

        // ── Asia ───────────────────────────────────────────────────────
        put("JPY", "Japanese Yen", "¥", "Japan", "JP", "🇯🇵", "Asia/Tokyo", "en");
        put("CNY", "Chinese Yuan", "¥", "China", "CN", "🇨🇳", "Asia/Shanghai", "en");
        put("KRW", "South Korean Won", "₩", "South Korea", "KR", "🇰🇷", "Asia/Seoul", "en");
        put("INR", "Indian Rupee", "₹", "India", "IN", "🇮🇳", "Asia/Kolkata", "en");
        put("IDR", "Indonesian Rupiah", "Rp", "Indonesia", "ID", "🇮🇩", "Asia/Jakarta", "en");
        put("MYR", "Malaysian Ringgit", "RM", "Malaysia", "MY", "🇲🇾", "Asia/Kuala_Lumpur", "en");
        put("SGD", "Singapore Dollar", "S$", "Singapore", "SG", "🇸🇬", "Asia/Singapore", "en");
        put("THB", "Thai Baht", "฿", "Thailand", "TH", "🇹🇭", "Asia/Bangkok", "en");
        put("PHP", "Philippine Peso", "₱", "Philippines", "PH", "🇵🇭", "Asia/Manila", "en");
        put("VND", "Vietnamese Dong", "₫", "Vietnam", "VN", "🇻🇳", "Asia/Ho_Chi_Minh", "en");
        put("TWD", "New Taiwan Dollar", "NT$", "Taiwan", "TW", "🇹🇼", "Asia/Taipei", "en");
        put("HKD", "Hong Kong Dollar", "HK$", "Hong Kong", "HK", "🇭🇰", "Asia/Hong_Kong", "en");
        put("PKR", "Pakistani Rupee", "₨", "Pakistan", "PK", "🇵🇰", "Asia/Karachi", "en");
        put("BDT", "Bangladeshi Taka", "৳", "Bangladesh", "BD", "🇧🇩", "Asia/Dhaka", "en");
        put("LKR", "Sri Lankan Rupee", "Rs", "Sri Lanka", "LK", "🇱🇰", "Asia/Colombo", "en");
        put("NPR", "Nepalese Rupee", "Rs", "Nepal", "NP", "🇳🇵", "Asia/Kathmandu", "en");
        put("MMK", "Myanmar Kyat", "K", "Myanmar", "MM", "🇲🇲", "Asia/Yangon", "en");
        put("KHR", "Cambodian Riel", "៛", "Cambodia", "KH", "🇰🇭", "Asia/Phnom_Penh", "en");
        put("LAK", "Lao Kip", "₭", "Laos", "LA", "🇱🇦", "Asia/Vientiane", "en");
        put("MNT", "Mongolian Tugrik", "₮", "Mongolia", "MN", "🇲🇳", "Asia/Ulaanbaatar", "en");
        put("KZT", "Kazakhstani Tenge", "₸", "Kazakhstan", "KZ", "🇰🇿", "Asia/Almaty", "en");
        put("UZS", "Uzbekistani Som", "сўм", "Uzbekistan", "UZ", "🇺🇿", "Asia/Tashkent", "en");
        put("KGS", "Kyrgystani Som", "сом", "Kyrgyzstan", "KG", "🇰🇬", "Asia/Bishkek", "en");
        put("TJS", "Tajikistani Somoni", "SM", "Tajikistan", "TJ", "🇹🇯", "Asia/Dushanbe", "en");
        put("TMT", "Turkmenistani Manat", "T", "Turkmenistan", "TM", "🇹🇲", "Asia/Ashgabat", "en");
        put("AZN", "Azerbaijani Manat", "₼", "Azerbaijan", "AZ", "🇦🇿", "Asia/Baku", "en");
        put("AMD", "Armenian Dram", "֏", "Armenia", "AM", "🇦🇲", "Asia/Yerevan", "en");
        put("BND", "Brunei Dollar", "B$", "Brunei", "BN", "🇧🇳", "Asia/Brunei", "en");
        put("MOP", "Macanese Pataca", "MOP$", "Macau", "MO", "🇲🇴", "Asia/Macau", "en");
        put("KPW", "North Korean Won", "₩", "North Korea", "KP", "🇰🇵", "Asia/Pyongyang", "en");
        put("BTN", "Bhutanese Ngultrum", "Nu", "Bhutan", "BT", "🇧🇹", "Asia/Thimphu", "en");
        put("MVR", "Maldivian Rufiyaa", "Rf", "Maldives", "MV", "🇲🇻", "Indian/Maldives", "en");
        put("AFN", "Afghan Afghani", "؋", "Afghanistan", "AF", "🇦🇫", "Asia/Kabul", "en");

        // ── Middle East ────────────────────────────────────────────────
        put("AED", "UAE Dirham", "د.إ", "United Arab Emirates", "AE", "🇦🇪", "Asia/Dubai", "en");
        put("SAR", "Saudi Riyal", "﷼", "Saudi Arabia", "SA", "🇸🇦", "Asia/Riyadh", "en");
        put("QAR", "Qatari Riyal", "ر.ق", "Qatar", "QA", "🇶🇦", "Asia/Qatar", "en");
        put("OMR", "Omani Rial", "ر.ع", "Oman", "OM", "🇴🇲", "Asia/Muscat", "en");
        put("KWD", "Kuwaiti Dinar", "د.ك", "Kuwait", "KW", "🇰🇼", "Asia/Kuwait", "en");
        put("BHD", "Bahraini Dinar", "BD", "Bahrain", "BH", "🇧🇭", "Asia/Bahrain", "en");
        put("JOD", "Jordanian Dinar", "JD", "Jordan", "JO", "🇯🇴", "Asia/Amman", "en");
        put("ILS", "Israeli New Shekel", "₪", "Israel", "IL", "🇮🇱", "Asia/Jerusalem", "en");
        put("LBP", "Lebanese Pound", "ل.ل", "Lebanon", "LB", "🇱🇧", "Asia/Beirut", "en");
        put("IQD", "Iraqi Dinar", "ع.د", "Iraq", "IQ", "🇮🇶", "Asia/Baghdad", "en");
        put("IRR", "Iranian Rial", "﷼", "Iran", "IR", "🇮🇷", "Asia/Tehran", "en");
        put("SYP", "Syrian Pound", "£S", "Syria", "SY", "🇸🇾", "Asia/Damascus", "en");
        put("YER", "Yemeni Rial", "﷼", "Yemen", "YE", "🇾🇪", "Asia/Aden", "en");

        // ── Africa ─────────────────────────────────────────────────────
        put("ZAR", "South African Rand", "R", "South Africa", "ZA", "🇿🇦", "Africa/Johannesburg", "en");
        put("NGN", "Nigerian Naira", "₦", "Nigeria", "NG", "🇳🇬", "Africa/Lagos", "en");
        put("EGP", "Egyptian Pound", "£", "Egypt", "EG", "🇪🇬", "Africa/Cairo", "en");
        put("KES", "Kenyan Shilling", "KSh", "Kenya", "KE", "🇰🇪", "Africa/Nairobi", "en");
        put("GHS", "Ghanaian Cedi", "₵", "Ghana", "GH", "🇬🇭", "Africa/Accra", "en");
        put("TZS", "Tanzanian Shilling", "TSh", "Tanzania", "TZ", "🇹🇿", "Africa/Dar_es_Salaam", "en");
        put("UGX", "Ugandan Shilling", "USh", "Uganda", "UG", "🇺🇬", "Africa/Kampala", "en");
        put("MAD", "Moroccan Dirham", "د.م", "Morocco", "MA", "🇲🇦", "Africa/Casablanca", "en");
        put("TND", "Tunisian Dinar", "د.ت", "Tunisia", "TN", "🇹🇳", "Africa/Tunis", "en");
        put("DZD", "Algerian Dinar", "د.ج", "Algeria", "DZ", "🇩🇿", "Africa/Algiers", "en");
        put("LYD", "Libyan Dinar", "ل.د", "Libya", "LY", "🇱🇾", "Africa/Tripoli", "en");
        put("SDG", "Sudanese Pound", "£", "Sudan", "SD", "🇸🇩", "Africa/Khartoum", "en");
        put("ETB", "Ethiopian Birr", "Br", "Ethiopia", "ET", "🇪🇹", "Africa/Addis_Ababa", "en");
        put("RWF", "Rwandan Franc", "FRw", "Rwanda", "RW", "🇷🇼", "Africa/Kigali", "en");
        put("CDF", "Congolese Franc", "FC", "DR Congo", "CD", "🇨🇩", "Africa/Kinshasa", "en");
        put("MGA", "Malagasy Ariary", "Ar", "Madagascar", "MG", "🇲🇬", "Indian/Antananarivo", "en");
        put("MUR", "Mauritian Rupee", "₨", "Mauritius", "MU", "🇲🇺", "Indian/Mauritius", "en");
        put("SCR", "Seychellois Rupee", "₨", "Seychelles", "SC", "🇸🇨", "Indian/Mahe", "en");
        put("GMD", "Gambian Dalasi", "D", "Gambia", "GM", "🇬🇲", "Africa/Banjul", "en");
        put("XOF", "West African CFA Franc", "CFA", "West Africa", "SN", "🇸🇳", "Africa/Dakar", "en");
        put("XAF", "Central African CFA", "FCFA", "Central Africa", "CM", "🇨🇲", "Africa/Douala", "en");
        put("CVE", "Cape Verdean Escudo", "$", "Cape Verde", "CV", "🇨🇻", "Atlantic/Cape_Verde", "pt");
        put("GNF", "Guinean Franc", "FG", "Guinea", "GN", "🇬🇳", "Africa/Conakry", "en");
        put("SLL", "Sierra Leonean Leone", "Le", "Sierra Leone", "SL", "🇸🇱", "Africa/Freetown", "en");
        put("LRD", "Liberian Dollar", "L$", "Liberia", "LR", "🇱🇷", "Africa/Monrovia", "en");
        put("BIF", "Burundian Franc", "FBu", "Burundi", "BI", "🇧🇮", "Africa/Bujumbura", "en");
        put("DJF", "Djiboutian Franc", "Fdj", "Djibouti", "DJ", "🇩🇯", "Africa/Djibouti", "en");
        put("ERN", "Eritrean Nakfa", "Nfk", "Eritrea", "ER", "🇪🇷", "Africa/Asmara", "en");
        put("KMF", "Comorian Franc", "CF", "Comoros", "KM", "🇰🇲", "Indian/Comoro", "en");
        put("LSL", "Lesotho Loti", "L", "Lesotho", "LS", "🇱🇸", "Africa/Maseru", "en");
        put("MWK", "Malawian Kwacha", "MK", "Malawi", "MW", "🇲🇼", "Africa/Blantyre", "en");
        put("MZN", "Mozambican Metical", "MT", "Mozambique", "MZ", "🇲🇿", "Africa/Maputo", "pt");
        put("NAD", "Namibian Dollar", "N$", "Namibia", "NA", "🇳🇦", "Africa/Windhoek", "en");
        put("SZL", "Swazi Lilangeni", "E", "Eswatini", "SZ", "🇸🇿", "Africa/Mbabane", "en");
        put("ZMW", "Zambian Kwacha", "ZK", "Zambia", "ZM", "🇿🇲", "Africa/Lusaka", "en");
        put("ZMK", "Zambian Kwacha (old)", "ZK", "Zambia", "ZM", "🇿🇲", "Africa/Lusaka", "en");
        put("ZWD", "Zimbabwean Dollar", "Z$", "Zimbabwe", "ZW", "🇿🇼", "Africa/Harare", "en");
        put("SOS", "Somali Shilling", "Sh", "Somalia", "SO", "🇸🇴", "Africa/Mogadishu", "en");
        put("BWP", "Botswanan Pula", "P", "Botswana", "BW", "🇧🇼", "Africa/Gaborone", "en");
        put("MRO", "Mauritanian Ouguiya", "UM", "Mauritania", "MR", "🇲🇷", "Africa/Nouakchott", "en");
        put("STD", "São Tomé Dobra", "Db", "São Tomé and Príncipe", "ST", "🇸🇹", "Africa/Sao_Tome", "pt");
        put("SHP", "Saint Helena Pound", "£", "Saint Helena", "SH", "🇸🇭", "Atlantic/St_Helena", "en");
        put("PGK", "Papua New Guinean Kina", "K", "Papua New Guinea", "PG", "🇵🇬", "Pacific/Port_Moresby", "en");

        // ── Oceania ────────────────────────────────────────────────────
        put("AUD", "Australian Dollar", "A$", "Australia", "AU", "🇦🇺", "Australia/Sydney", "en");
        put("NZD", "New Zealand Dollar", "NZ$", "New Zealand", "NZ", "🇳🇿", "Pacific/Auckland", "en");
        put("FJD", "Fijian Dollar", "FJ$", "Fiji", "FJ", "🇫🇯", "Pacific/Fiji", "en");
        put("SBD", "Solomon Islands Dollar", "SI$", "Solomon Islands", "SB", "🇸🇧", "Pacific/Guadalcanal", "en");
        put("TOP", "Tongan Paʻanga", "T$", "Tonga", "TO", "🇹🇴", "Pacific/Tongatapu", "en");
        put("VUV", "Vanuatu Vatu", "VT", "Vanuatu", "VU", "🇻🇺", "Pacific/Efate", "en");
        put("WST", "Samoan Tala", "WS$", "Samoa", "WS", "🇼🇸", "Pacific/Apia", "en");
        put("XPF", "CFP Franc", "₣", "French Polynesia", "PF", "🇵🇫", "Pacific/Tahiti", "en");
        put("TVD", "Tuvaluan Dollar", "TV$", "Tuvalu", "TV", "🇹🇻", "Pacific/Funafuti", "en");

        // ── Portugal & Equatorial Guinea (special language rules) ──────
        // Portugal
        // (EUR already mapped above — won't duplicate, but we note language linkage)

        // Equatorial Guinea (Spanish-speaking Africa)
        // XAF already above; GQ uses XAF but language = "es"
        // We handle this via the language logic in the UseCase, not metadata

        // ── Precious Metals & Special (no country) ─────────────────────
        put("XAG", "Silver (troy ounce)", "XAG", "N/A", "XX", "🥈", "UTC", "en");
        put("XAU", "Gold (troy ounce)", "XAU", "N/A", "XX", "🥇", "UTC", "en");
        put("XPT", "Platinum (troy ounce)", "XPT", "N/A", "XX", "⚪", "UTC", "en");
        put("XPD", "Palladium (troy ounce)", "XPD", "N/A", "XX", "⬜", "UTC", "en");
        put("XDR", "IMF Special Drawing Rights", "XDR", "N/A", "XX", "🏦", "UTC", "en");
        put("BTC", "Bitcoin", "₿", "Decentralized", "XX", "🪙", "UTC", "en");
        put("SPL", "Seborgan Luigino", "SPL", "Seborga", "XX", "🏳️", "Europe/Rome", "en");
        put("CLF", "Chilean Unidad de Fomento", "UF", "Chile", "CL", "🇨🇱", "America/Santiago", "es");
        put("BYR", "Belarusian Ruble (old)", "Br", "Belarus", "BY", "🇧🇾", "Europe/Minsk", "en");

        // ── Remaining currencies ───────────────────────────────────────
        put("IMP", "Isle of Man Pound", "£", "Isle of Man", "IM", "🇮🇲", "Europe/Isle_of_Man", "en");
        put("JEP", "Jersey Pound", "£", "Jersey", "JE", "🇯🇪", "Europe/Jersey", "en");
        put("GGP", "Guernsey Pound", "£", "Guernsey", "GG", "🇬🇬", "Europe/Guernsey", "en");
    }

    private static void put(String code, String name, String symbol, String country, String cc, String flag, String tz,
            String lang) {
        META.put(code, new CurrencyMeta(name, symbol, country, cc, flag, tz, lang));
    }

    /**
     * Get metadata for a currency code. Returns null if unknown.
     */
    public static CurrencyMeta get(String currencyCode) {
        return META.get(currencyCode);
    }

    /**
     * Get the full metadata map (unmodifiable view).
     */
    public static Map<String, CurrencyMeta> getAll() {
        return Map.copyOf(META);
    }

    /**
     * Build a default CurrencyMeta for an unknown currency code.
     */
    public static CurrencyMeta defaultMeta(String currencyCode) {
        return new CurrencyMeta(currencyCode, currencyCode, "Unknown",
                currencyCode.length() >= 2 ? currencyCode.substring(0, 2) : "XX", "🏳️", "UTC", "en");
    }
}
