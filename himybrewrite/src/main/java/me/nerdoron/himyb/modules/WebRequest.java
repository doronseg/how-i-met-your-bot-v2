package me.nerdoron.himyb.modules;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class WebRequest {
    private final OkHttpClient client;
    private Request.Builder request;
    private Response response;

    public WebRequest() {
        this.client = new OkHttpClient();
    }

    public Request.Builder newRequest(String url) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        String purl = urlBuilder.build().toString();

        Request.Builder request = new Request.Builder()
                .url(url);

        this.request = request;
        return request;
    }

    public void get() {
        this.request = this.request.get();
    }

    public void post(JSONObject json) {
        this.request = this.request.post(RequestBody.create(json.toString(), MediaType.parse("application/json")));
        return;
    }

    public void addHeader(String name, String value) {
        this.request = this.request.header(name, value);
        return;
    }

    public Response sendRequest() throws IOException {
        Response response = null;
        response = client.newCall(this.request.build()).execute();
        this.response = response;
        return response;
    }

    public File sendFileRequest(String name) throws IOException {
        this.sendRequest();
        InputStream inputStream = null;
        try {
            inputStream = response.body().byteStream();

            byte[] buff = new byte[1024 * 4];
            //long downloaded = 0;
            //long target = response.body().contentLength();
            File file = new File(name);
            OutputStream output = new FileOutputStream(file);

            while (true) {
                int readed = inputStream.read(buff);
                if (readed == -1) {
                    break;
                }
                output.write(buff, 0, readed);
                //downloaded += readed;
            }

            output.flush();
            output.close();
            return file;
        } catch (IOException ignore) {
            return null;
        } finally {
            if (inputStream != null) {
                try {inputStream.close();} catch (Exception e) {}
            }
        }
    }

    public JSONObject getJSONObjectResponce() throws IOException {
        String resStr = null;
        resStr = this.response.body().string();
        JSONObject json = new JSONObject(resStr);
        return json;
    }

    public JSONArray getJSONArrayResponce() throws IOException {
        String resStr = null;
        resStr = this.response.body().string();
        JSONArray json = new JSONArray(resStr);
        return json;
    }
}
