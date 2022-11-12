package com.aier.app;

import android.app.AlertDialog;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.sdk.WebView;

public class MyJavaScriptInterface {
    private Context mContext;
    private WebView mWebView;
    private TextToSpeech textToSpeech;

    public MyJavaScriptInterface(Context context, TextToSpeech textToSpeech) {
        this.mContext = context;
//        this.mWebView = x5WebView;
        this.textToSpeech=textToSpeech;
    }

    /**
     * window.AndroidJSBridge.androidTestFunction1('xxxx')
     * 调用该方法，APP 会弹出一个 Alert 对话框，对话框中的内容为 JavaScript 传入的字符串
     * Android 只能接收基本数据类型参数，不能接收引用类型的数据（Object、Array）。
     */
    @JavascriptInterface
    public void androidTestFunction1 (String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(str);
        builder.setNegativeButton("确定", null);
        builder.create().show();
    }

    // 调用该方法，方法会返回一个返回值给 JavaScript 端
    @JavascriptInterface
    public String androidTestFunction2 () {
        return "androidTestFunction2方法的返回值";
    }

    @JavascriptInterface
    public void speakVoice (String str) {
        textToSpeech.speak(str, TextToSpeech.QUEUE_ADD, null);
    }
}
