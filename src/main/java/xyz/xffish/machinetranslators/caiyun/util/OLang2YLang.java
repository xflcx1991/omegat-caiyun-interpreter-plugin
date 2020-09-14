package xyz.xffish.machinetranslators.caiyun.util;

import cn.hutool.core.map.MapUtil;

import java.util.Map;

public final class OLang2YLang {
    /*
遵照 ISO-639-1 标准
语言        OmegaT      有道
中文		    zh          zh
英文		    en          en
日文		    ja          ja


     */
    /**
     * OmegaT 语言代码和 Youdao 语言代码对应表.
     * 参见<br>
     * http://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html#section-9 <br>
     * http://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/%E4%BA%A7%E5%93%81%E5%AE%9A%E4%BB%B7/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-%E4%BA%A7%E5%93%81%E5%AE%9A%E4%BB%B7.html <br>
     * 有道不区分具体那种英文，都是 en
     */
    private static final Map<String, String> OLANG_2_YLANG_MAP = MapUtil.<String, String>builder()
            .put("zh", "zh")
            .put("en", "en")
            .put("ja", "ja")
            .map();

    /**
     * 将 OmegaT 的语言代码转换成有道翻译识别的语言代码.
     * 找不到就输出 auto
     *
     * @param sLang OmegaT的语言代码
     * @return 转换后的有道翻译语言代码
     * @see "https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html#section-9"
     */
    public static String translateOLang2YLang(final String sLang) {
        String tLang = OLANG_2_YLANG_MAP.get(sLang);
        // 找不到就改成自动识别
        if (tLang == null) {
            tLang = "auto";
        }
        return tLang;
    }


    /**
     *  Utility classes should not have a public or default constructor.
     */
    private OLang2YLang() {
    }
}
