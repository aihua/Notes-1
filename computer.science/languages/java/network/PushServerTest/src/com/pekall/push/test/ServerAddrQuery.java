package com.pekall.push.test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerAddrQuery {

    public static ServerAddress query(String querySrvUrl, String deviceId) throws IOException {
        return query(querySrvUrl + deviceId);
    }

    public static ServerAddress query(String url) throws IOException {
        Debug.logVerbose("query push server:" + url);
        ServerAddress serverAddress = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        HttpResponse response1 = httpclient.execute(httpGet);

        Debug.logVerbose("status line");
        Debug.logVerbose(response1.getStatusLine().toString());

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                response1.getEntity().getContent()));

        String line = "";
        Debug.logVerbose("entity:");
        while ((line = rd.readLine()) != null) {
            Debug.logVerbose(line);

            Gson gson = new GsonBuilder().serializeNulls().create();
            serverAddress = gson.fromJson(line, ServerAddress.class);
            break;
        }

        return serverAddress;
    }
}
