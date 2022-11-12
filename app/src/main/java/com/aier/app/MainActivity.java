package com.aier.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

//import com.like.webview.core.X5WebView;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Locale;

//import com.like.webview.core.X5WebView;
//import com.like.webview.ext.SimpleWebViewActivity;
//import com.tencent.bugly.crashreport.CrashReport;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private static final String TAG = "MainActivity";
    private WebView mWebView;
    private Button btnCheckUpdate;
    private TextToSpeech textToSpeech;
    private long firstTime = System.currentTimeMillis();

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCheckUpdate = (Button) findViewById(R.id.btnCheckUpdate);
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, UpgradeActivity.class);
//                intent.putExtra("message", "aaaaa");
//                startActivity(intent);

//                textToSpeech.speak("左左,你是一个小臭屁。 右右，今晚要刷牙哦！",
//                        TextToSpeech.QUEUE_ADD, null);

                Intent intent = new Intent(MainActivity.this, WebActivity.class);
//                intent.putExtra("message", "aaaaa");
                startActivity(intent);
            }
        });

        mWebView = findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true); // websocket 需要
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //没缓存
        String ua=SpUtil.getInstace().getString("ua","");
        mWebView.getSettings().setUserAgent(ua);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                // 增加Javascript异常监控
//                CrashReport.setJavascriptMonitor(webView, true);
                super.onProgressChanged(webView, progress);
            }
        });

        initTTs();
//        mWebView.loadUrl("https://www.bilibili.com");
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(this, textToSpeech), "AndroidJSBridge");
        String url=SpUtil.getInstace().getString("url","");
        if(url.equals("")){
            mWebView.loadUrl("https://www.baidu.com");
        }else{
            mWebView.loadUrl(url);
        }
//        mWebView.loadUrl("http://120.133.128.182:3000/d/RI_Fx-VVz/acloud-outlet-flow-monitor");
        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
//        if(mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
//            super.onBackPressed();
//            finish();
//        }

        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 1500) {
//            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
        } else {
//            Toast.makeText(MainActivity.this, "准备退出", Toast.LENGTH_SHORT).show();
//            super.onBackPressed();


            new AlertDialog.Builder(this)
                    .setTitle("是退出应用还是设置应用?")
                    .setMessage("可设置应用默认的url")
                    .setPositiveButton(getResources().getString(R.string.set_app), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MainActivity.this, WebActivity.class);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.quit_app), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    public void sendMessage(View view) {
        // Do something in response to button click
    }

    private void initTTs() {
        textToSpeech = new TextToSpeech(this, this);
        //设置语言
        int result = textToSpeech.setLanguage(Locale.CHINESE);
        if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                && result != TextToSpeech.LANG_AVAILABLE) {
//            Toast.makeText(MainActivity.this, "TTS暂时不支持这种语音的朗读！",
//                    Toast.LENGTH_SHORT).show();
        }
        //设置音调
        textToSpeech.setPitch(1.0f);
        //设置语速，1.0为正常语速
        textToSpeech.setSpeechRate(0.8f);
    }

    @Override
    public void onInit(int status) {
        //初始化成功
        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG, "init success");
        } else {
            Log.d(TAG, "init fail");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //中断当前话语
        textToSpeech.stop();
        //释放资源
        textToSpeech.shutdown();
    }

    /**
     * 原生端调用 web 方法，方法必须是挂载到 web 端 window 对象下面的方法。
     * 调用 JS 中的方法：onFunction1
     */
    public void onJSFunction1(View v) {
        mWebView.evaluateJavascript("javascript:onFunction('android调用JS方法')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(s);
                builder.setNegativeButton("确定", null);
                builder.create().show();
            }
        });
    }

}
