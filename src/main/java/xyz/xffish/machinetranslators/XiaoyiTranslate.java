/*
参考了
https://github.com/yoyicue/omegat-tencent-plugin
https://github.com/omegat-org/omegat/blob/854b6b5a66a0306e5c27e74c0b5d656ed80b2bd4/src/org/omegat/core/machinetranslators/YandexTranslate.java
GoogleTranslateWithoutApiKey
的写法，感谢上述作者
 */
package xyz.xffish.machinetranslators;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.gui.exttrans.MTConfigDialog;
import org.omegat.util.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.awt.*;
import java.awt.Window;
import java.util.HashMap;

public class XiaoyiTranslate extends BaseTranslate {
    private static final String PROPERTY_API_SECRET_KEY = "xiaoyi.api.secret.Key";
    /**
     *  这是彩云小译的官方测试 key，如果没有在设置里配置自己的 key，那就用这个，以下这是官方 key 的说明<br>
     *  <blockquote>
     *  3975l6lr5pcbvidl6jl2 作为测试 Token，我们不保证该 Token 的可用性，所以如果要持续使用，还请申请正式 Token。
     *  </blockquote>
     */
    protected static final String PROPERTY_API_OFFICIAL_TEST_SECRET_KEY = "3975l6lr5pcbvidl6jl2";
    /**
     * 彩云小译请求 URL
     */
    protected static final String URL = "http://api.interpreter.caiyunai.com/v1/translator";

    private static final Logger logger = LoggerFactory.getLogger(XiaoyiTranslate.class);

    /**
     * 在软件启动时会自动调用该函数来注册插件
     */
    public static void loadPlugins() {
        logger.info("加载 XiaoyiTranslate Plugin");

        Core.registerMachineTranslationClass(XiaoyiTranslate.class);
    }

    public static void unloadPlugins() {
    }

    /**
     * 显示该插件介绍性的话
     * @return 介绍性话语
     */
    @Override
    protected String getPreferenceName() {
        return "allow_caiyun_xiaoyi_translate";
    }

    /**
     * 在软件里显示该翻译插件的名字
     * @return 本翻译插件显示的名字
     */
    @Override
    public String getName() {
        return "Caiyun Xiaoyi Translate";
    }

    @Override
    protected String translate(Language sLang, Language tLang, String text) throws Exception {
        String secretKey = getCredential(PROPERTY_API_SECRET_KEY);
        if(secretKey.isEmpty()){
            // key 是空的那就用官方测试key
            secretKey = PROPERTY_API_OFFICIAL_TEST_SECRET_KEY;
        }
		secretKey = "token " + secretKey;

        logger.info("secretKey = {}", secretKey);


        String lvSourceLang = sLang.getLanguageCode().substring(0, 2).toLowerCase();
        if (!lvSourceLang.equals("en")){
            // https://fanyi.caiyunapp.com/#/api 文档指出
            // 目前只支持 英语 en 和日文 ja 向汉语转
            lvSourceLang = "auto";
        }

        //String lvTargetLang = tLang.getLanguageCode().substring(0, 2).toLowerCase();
        // 只翻译成中文
        String lvTargetLang = "zh";

        // 传递给彩云小译的指示翻译方向的字符串
        String trans_type = String.format("%s2%s", lvSourceLang, lvTargetLang);
        // 打印最后拼接的翻译方向字符串
        logger.info("trans_type = {}", trans_type);

        //判断翻译缓存里有没有
        // U+2026 HORIZONTAL ELLIPSIS 水平省略号 …
        String lvShortText = text.length() > 5000 ? text.substring(0, 4997) + "\u2026" : text;
        String prev = getFromCache(sLang, tLang, lvShortText);
        if(prev != null){
            // 啊，有缓存，那就直接返回不用请求了
            logger.info("啊，有缓存，太美妙了：{}", prev);
            return prev;
        }

        //----------------------------------------------------------------------
        // Omegat 包含了一个 org.omegat.util.JsonParser
        // 它是对 jdk8 Nashorn JavaScript engine 薄薄的封装
        // 但是 JEP 335 宣布弃用 Nashorn，为了以后还是用第三方 json 库

//            x-authorization: token 3975l6lr5pcbvidl6jl2
//
//            Accept-Encoding: gzip
//            User-Agent: okhttp/4.8.1
        // 有错误的话是{"message": "API rate limit exceeded"}
        //构造 json 格式body
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("source", lvShortText); //待翻译的句子
        bodyMap.put("trans_type", trans_type); // 翻译方向字符串
        bodyMap.put("request_id", "demo"); // 写死
//        JSONUtil.parse(bodyMap);
        String bodyStr = JSONUtil.toJsonStr(bodyMap);

        logger.info("bodyStr = {}", bodyStr);

        

//        JSONObject json1 = JSONUtil.createObj();

        HttpRequest post = HttpUtil.createPost(URL)
				.header("x-authorization", secretKey)
//                .header("content-type", "application/json") //body会自动设置contentType
                .body(bodyStr);

        HttpResponse response = post.execute();
        logger.info("response status = {}", response.getStatus());
        logger.info("response isGzip = {}", response.isGzip());
        String headerContentEncoding = response.header(Header.CONTENT_ENCODING);
        logger.info("response headerContentEncoding = {}", headerContentEncoding);

        String translation = "";
        String responseBody = response.body();


        logger.info("response body = {}", responseBody);

        

        JSONObject jsonObject = JSONUtil.parseObj(responseBody);
        if(response.isOk()){

            translation = (String) jsonObject.getObj("target", "");
        }else{
            translation = (String) jsonObject.getObj("message", "");
        }




        return translation;



    }




    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public void showConfigurationUI(Window parent) {
        MTConfigDialog dialog = new MTConfigDialog(parent, getName()) {
            @Override
            protected void onConfirm() {
                String key = panel.valueField1.getText().trim();
                boolean temporary = panel.temporaryCheckBox.isSelected();
                setCredential(PROPERTY_API_SECRET_KEY, key, temporary);
            }
        };
        dialog.panel.valueLabel1.setText("Token");
        dialog.panel.valueField1.setText(getCredential(PROPERTY_API_SECRET_KEY));
        // 只有输入框要填
        dialog.panel.valueLabel2.setVisible(false);
        dialog.panel.valueField2.setVisible(false);

        dialog.panel.temporaryCheckBox.setSelected(isCredentialStoredTemporarily(PROPERTY_API_SECRET_KEY));
        dialog.show();
    }
}
