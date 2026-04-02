package com.example.nt.app.textrepeat.ui.splash.language

enum class LanguageEnum(val title: String, val titleGlobal: String, val code: String) {
    English("English", "English", "EN"),
    Spanish("Español", "Spanish", "ES"),
    Russian("русский", "Russian", "RU"),
    Indonesian("bahasa Indonesia", "Indonesian", "IN"),
    Portugese("Português", "Portugese", "PT"),
    French("Français", "French", "FR"),
    Malay("Bahasa Melayu", "Malay", "MS"),
    Turkish("Türk", "Turkish", "TR"),
    Hindi("हिंदी", "Hindi", "HI"),
    German("Deutsche", "German", "DE"),
    Thai("ไทย", "Thai", "TH"),
    Vietnamese("Tiếng Việt", "Vietnamese", "VI"),
    Arabic("عربي", "Arabic", "AR"),
    Persian("فارسی", "Persian", "FA"),
    Uzbek("o'zbek", "Uzbek", "UZ"),
    Khmer("ខ្មែរ", "Khmer", "KM"),

    //Filipino("Pilipino", "Filipino", "TL"),//
    Bangla("বাংলা", "Bangla", "BN"), //Bengali
    Telugu("తెలుగు", "Telugu", "TE"), //Telugu
    Marathi("मराठी", "Marathi", "MR"),
    Tamil("தமிழ்", "Tamil", "TA"),
    Kannada("ಕನ್ನಡ", "Kannada", "KN"),
    Malayalam("മലയാളം", "Malayalam", "ML");

    companion object {
        fun getLanguageCode(position: Int): String = values()[position].code
    }
}