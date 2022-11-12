package com.aier.app;

import android.util.Log;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
public class AndroidWebServer extends NanoHTTPD {

    public AndroidWebServer(int port) {
        super(port);
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>参数设置:下次启动时生效</h1>\n";
        Map<String, String> parms = session.getParms();
        String url=parms.get("url");
        String ua=parms.get("ua");
        String lastUrl=SpUtil.getInstace().getString("url","");
        String lastUa=SpUtil.getInstace().getString("ua","");
        if (url == null) {
            msg += "<form action='?' method='get'>\n " +
                    " <p>Url:<input type='text' name='url' value='"+lastUrl+"'></p>\n"+
                    " <p>UA:<input type='text' name='ua' value='" + lastUa+ "'></p>\n"+
                    " <p>Did:"+ MyApplication.getDid()+"</p>\n"+
                    " <input type='submit' value='提交'> \n"+
                    "</form>\n";
        } else {
            SpUtil.getInstace().save("url",url);
            SpUtil.getInstace().save("ua",ua);
            msg += "<p>Url 设置成功:" + url + " 重启后生校</p>";
        }
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }
}


//    @Override
//    public Response serve(String uri, Method method,
//                          Map<String, String> header,
//                          Map<String, String> parameters,
//                          Map<String, String> files) {
//        String answer = "";
//        try {
//            // Open file from SD Card
//            File root = Environment.getExternalStorageDirectory();
//            FileReader index = new FileReader(root.getAbsolutePath() +
//                    "/www/index.html");
//            BufferedReader reader = new BufferedReader(index);
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                answer += line;
//            }
//            reader.close();
//
//        } catch(IOException ioe) {
//            Log.w("Httpd", ioe.toString());
//        }
//
//
//        return new NanoHTTPD.Response(answer);
//    }

