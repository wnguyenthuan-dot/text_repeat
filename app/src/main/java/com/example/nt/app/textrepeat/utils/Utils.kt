package com.example.nt.app.textrepeat.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.nt.app.textrepeat.ui.splash.language.LanguageEntity
import com.example.nt.app.textrepeat.ui.splash.language.LanguageEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


fun setLanguageApp(code: String) {
    val localeList = LocaleListCompat.forLanguageTags(code)
    AppCompatDelegate.setApplicationLocales(localeList)
}

fun getApplicationLocales(): String = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    .ifEmpty { Locale.getDefault().language }

fun getLanguageList() =
    LanguageEnum.values().map { LanguageEntity(it, getApplicationLocales().equals(it.code, true)) }

fun applyFont(position: Int, str: String, onDone: (rs: String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = when (position) {
            0 -> TextStylish.font0(str)
            1 -> TextStylish.font1(str)
            2 -> TextStylish.font2(str)
            3 -> TextStylish.font3(str)
            4 -> TextStylish.font4(str)
            5 -> TextStylish.font6(str)
            6 -> TextStylish.font7(str)
            7 -> TextStylish.font8(str)
            8 -> TextStylish.font9(str)
            9 -> TextStylish.font10(str)
            10 -> TextStylish.font11(str)
            11 -> TextStylish.font12(str)
            12 -> TextStylish.font13(str)
            13 -> TextStylish.font14(str)
            14 -> TextStylish.font15(str)
            15 -> TextStylish.font16(str)
            16 -> TextStylish.font17(str)
            17 -> TextStylish.font18(str)
            18 -> TextStylish.font19(str)
            19 -> TextStylish.font20(str)
            20 -> TextStylish.font21(str)
            21 -> TextStylish.font22(str)
            22 -> TextStylish.font23(str)
            23 -> TextStylish.font24(str)
            24 -> TextStylish.font25(str)
            25 -> TextStylish.font26(str)
            26 -> TextStylish.font27(str)
            27 -> TextStylish.font28(str)
            28 -> TextStylish.font29(str)
            29 -> TextStylish.font30(str)
            30 -> TextStylish.font31(str)
            31 -> TextStylish.font32(str)
            32 -> TextStylish.font33(str)
            33 -> TextStylish.font34(str)
            34 -> TextStylish.font35(str)
            35 -> TextStylish.font36(str)
            36 -> TextStylish.font37(str)
            37 -> TextStylish.font38(str)
            38 -> TextStylish.font39(str)
            39 -> TextStylish.font40(str)
            40 -> TextStylish.font41(str)
            41 -> TextStylish.font42(str)
            42 -> TextStylish.font43(str)
            43 -> TextStylish.font44(str)
            44 -> TextStylish.font45(str)
            45 -> TextStylish.font46(str)
            46 -> TextStylish.font47(str)
            47 -> TextStylish.font48(str)
            48 -> TextStylish.font49(str)
            49 -> TextStylish.font50(str)
            50 -> TextStylish.font51(str)
            51 -> TextStylish.font52(str)
            52 -> TextStylish.font53(str)
            53 -> TextStylish.font54(str)
            54 -> TextStylish.font55(str)
            55 -> TextStylish.font56(str)
            56 -> TextStylish.font57(str)
            57 -> TextStylish.font58(str)
            58 -> TextStylish.font59(str)
            59 -> TextStylish.font60(str)
            60 -> TextStylish.font61(str)
            61 -> TextStylish.font62(str)
            62 -> TextStylish.font63(str)
            63 -> TextStylish.font64(str)
            64 -> TextStylish.font65(str)
            65 -> TextStylish.font66(str)
            66 -> TextStylish.font67(str)
            67 -> TextStylish.font68(str)
            68 -> TextStylish.font69(str)
            69 -> TextStylish.font70(str)
            70 -> TextStylish.font71(str)
            71 -> TextStylish.font72(str)
            72 -> TextStylish.font73(str)
            73 -> TextStylish.font74(str)
            74 -> TextStylish.font75(str)
            75 -> TextStylish.font76(str)
            76 -> TextStylish.font77(str)
            77 -> TextStylish.font78(str)
            78 -> TextStylish.font79(str)
            79 -> TextStylish.font80(str)
            80 -> TextStylish.font81(str)
            81 -> TextStylish.font82(str)
            82 -> TextStylish.font83(str)
            83 -> TextStylish.font84(str)
            84 -> TextStylish.font85(str)
            85 -> TextStylish.font86(str)
            86 -> TextStylish.font87(str)
            87 -> TextStylish.font88(str)
            88 -> TextStylish.font89(str)
            89 -> TextStylish.font90(str)
            90 -> TextStylish.font91(str)
            91 -> TextStylish.font92(str)
            92 -> TextStylish.font93(str)
            93 -> TextStylish.font94(str)
            94 -> TextStylish.font95(str)
            95 -> TextStylish.font96(str)
            96 -> TextStylish.font97(str)
            97 -> TextStylish.font98(str)
            98 -> TextStylish.font99(str)
            99 -> TextStylish.font100(str)
            100 -> TextStylish.font101(str)
            101 -> TextStylish.font102(str)
            102 -> TextStylish.font103(str)
            103 -> TextStylish.font104(str)
            104 -> TextStylish.font105(str)
            105 -> TextStylish.font106(str)
            106 -> TextStylish.font107(str)
            107 -> TextStylish.font108(str)
            108 -> TextStylish.font109(str)
            109 -> TextStylish.font5(str)
            else -> str
        }
        withContext(Dispatchers.Main) {
            onDone.invoke(result)
        }
    }
}