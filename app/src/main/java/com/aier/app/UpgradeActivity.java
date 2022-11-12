package com.aier.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;

import org.json.JSONObject;

/**
 * 自定义Activity.  调用 volley 的例子
 */
public class UpgradeActivity extends Activity {
    private TextView tv;
    private TextView title;
    private TextView version;
    private TextView size;
    private TextView time;
    private TextView content;
    private Button cancel;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upgrade);
        tv = getView(R.id.tv);
        title = getView(R.id.title);
        version = getView(R.id.version);
        size = getView(R.id.size);
        time = getView(R.id.time);
        content = getView(R.id.content);
        cancel = getView(R.id.cancel);
        start = getView(R.id.start);

        content.setText("aaaaaaaa");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask task = Beta.startDownload();
                updateBtn(task);
                if (task.getStatus() == DownloadTask.DOWNLOADING) {
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.cancelDownload();
                finish();
            }
        });
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask task) {
                updateBtn(task);
                tv.setText(task.getSavedLength() + "");
            }

            @Override
            public void onCompleted(DownloadTask task) {
                updateBtn(task);
                tv.setText(task.getSavedLength() + "");
            }

            @Override
            public void onFailed(DownloadTask task, int code, String extMsg) {
                updateBtn(task);
                tv.setText("failed");

            }
        });

        //------------
        String url = "https://paysit.aierchina.com/pay/api/order/queryRefundChannelOrder?refundTrxNo=1370236364234166274";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        content.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        time.setText("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                time.setText("That didn't work!");
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Beta.unregisterDownloadListener();
    }


    public void updateBtn(DownloadTask task) {
//        switch (task.getStatus()) {
//            case DownloadTask.INIT:
//            case DownloadTask.DELETED:
//            case DownloadTask.FAILED: {
//                start.setText("开始下载");
//            }
//                break;
//            case DownloadTask.COMPLETE: {
//                start.setText("安装");
//            }
//                break;
//            case DownloadTask.DOWNLOADING: {
//                start.setText("暂停");
//            }
//                break;
//            case DownloadTask.PAUSED: {
//                start.setText("继续下载");
//            }
//                break;
//            default:
//                break;
//        }
    }

    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }
}
