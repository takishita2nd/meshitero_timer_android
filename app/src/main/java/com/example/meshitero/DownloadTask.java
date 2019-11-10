package com.example.meshitero;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, Bitmap> {
    private Listener listener = null;

    @Override
    protected Bitmap doInBackground(String... param) {
        return downloadImage(param[0]);
    }

    @Override
    protected void onProgressUpdate(Void... progress) {

    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        if (listener != null) {
            listener.onSuccess(bmp);
        }
    }

    private Bitmap downloadImage(String address) {
        Bitmap bmp = null;

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL( address );

            // HttpURLConnection インスタンス生成
            urlConnection = (HttpURLConnection) url.openConnection();

            // タイムアウト設定
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);

            // リクエストメソッド
            urlConnection.setRequestMethod("GET");

            // リダイレクトを自動で許可しない設定
            urlConnection.setInstanceFollowRedirects(false);

            // ヘッダーの設定(複数設定可能)
            urlConnection.setRequestProperty("Accept-Language", "jp");

            // 接続
            urlConnection.connect();

            int resp = urlConnection.getResponseCode();

            switch (resp){
                case HttpURLConnection.HTTP_OK:
                    try(InputStream is = urlConnection.getInputStream()){
                        bmp = BitmapFactory.decodeStream(is);
                        is.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.d("debug", "downloadImage error");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return bmp;
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(Bitmap bmp);
    }
}
