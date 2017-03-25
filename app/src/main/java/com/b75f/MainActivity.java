package com.b75f;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.b75f.view.CustomView;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Client mKinveyClient;
    HttpURLConnection c = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            loginUser();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loginUser() throws MalformedURLException {

        mKinveyClient = new Client.Builder("kid_BJEhzSRil", "6509f61714dd4e21bf036fa4c1c417e0"
                , getApplicationContext()).build();


        if (!mKinveyClient.user().isUserLoggedIn()){
        mKinveyClient.user().login(new KinveyUserCallback() {
            @Override
            public void onFailure(Throwable error) {
                Log.e("Failure", "Login Failure", error);
            }

            @Override
            public void onSuccess(User result) {
                Log.i("NewUser", "Logged in a new implicit user with id: " + result.getId());
            }
        });

        mKinveyClient.ping(new KinveyPingCallback() {
            public void onFailure(Throwable t) {
                Log.e("NewUser", "Kinvey Ping Failed", t);
            }

            public void onSuccess(Boolean b) {
                Log.d("NewUser", "Kinvey Ping Success");
            }
        });
    }
    else {
            Toast.makeText(getApplicationContext(), "USing Cached user", Toast.LENGTH_SHORT).show();
        }

        getData();
    }

    private void getData() throws MalformedURLException {
        new LoadRequest().execute();
    }

    class LoadRequest extends AsyncTask<Void, Void, Void> {

        String result;
        @Override
        protected Void doInBackground(Void... params) {
            Authenticator.setDefault(new Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("ashutoshsmartcool@gmail.com","ashutosh".toCharArray());
                }});
            try {
                c = (HttpURLConnection) new URL
                        ("https://baas.kinvey.com/appdata/kid_BJEhzSRil/floor").openConnection();
                c.setUseCaches(false);
                c.connect();
                BufferedReader br;
                String output;
                StringBuilder sb = new StringBuilder();
                if (200 <= c.getResponseCode() && c.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((c.getInputStream())));

                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                }else {
                    br = new BufferedReader(new InputStreamReader((c.getInputStream())));
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                }

                result = sb.toString();

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

            @Override
        protected void onPostExecute(Void aVoid) {

                try {
                    JSONArray array = new JSONArray(result);
                    JSONArray object = array.getJSONObject(0).getJSONArray("first floor");
                    CustomView view = new CustomView(getApplicationContext(),object);
                    setContentView(view);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                super.onPostExecute(aVoid);
        }
    }

}
