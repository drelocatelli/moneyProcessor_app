package com.example.moneyprocessor.Service;

import com.example.moneyprocessor.Security.DBConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserService {

    private static String webservice = String.format("%s/rest/v1/users", DBConnection.dbHost);

    public static boolean emailExists(String email) throws Exception {
        // api
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(webservice+"?select=email")
                .addHeader("apikey", DBConnection.apiKey)
                .addHeader("Authorization", DBConnection.dbJWT)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .build();
        Response response = client.newCall(request).execute();

        if (response.body().string().contains(email)) {
            return true;
        }

        return false;
    }

    public static String getUserIdByEmail(String email) {
        // api

        try {

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            String service = String.format("%s?email=eq.%s&select=id", webservice, email);

            Request request = new Request.Builder()
                    .url(service)
                    .addHeader("apikey", DBConnection.apiKey)
                    .addHeader("Authorization", DBConnection.dbJWT)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build();
            Response res = client.newCall(request).execute();

            String responseData = res.body().string();

            JSONObject resArray = (JSONObject) new JSONArray(responseData).get(0);
            String response = resArray.getString("id");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getNomeByEmail(String email) {
        // api

        try {

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            String service = String.format("%s?email=eq.%s&select=nome", webservice, email);

            Request request = new Request.Builder()
                    .url(service)
                    .addHeader("apikey", DBConnection.apiKey)
                    .addHeader("Authorization", DBConnection.dbJWT)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build();
            Response res = client.newCall(request).execute();

            String responseData = res.body().string();

            JSONObject resArray = (JSONObject) new JSONArray(responseData).get(0);
            String response = resArray.getString("nome");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean loginUsuarioDB(String email, String senha) throws Exception {
        // api

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        String service = String.format("%s?email=eq.%s&senha=eq.%s", webservice, email, senha);

        Request request = new Request.Builder()
                .url(service)
                .addHeader("apikey", DBConnection.apiKey)
                .addHeader("Authorization", DBConnection.dbJWT)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .build();
        Response res = client.newCall(request).execute();

        System.out.println(service);

        String responseData = res.body().string();

        JSONArray response = new JSONArray(responseData);

        if(response.length() == 1) {
            return true;
        }

        return false;

    }

    public static boolean cadastroUsuarioDB(String nome, String email, String senha) throws Exception {

        // api

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        String params = String.format("{\"nome\": \"%s\", \"email\": \"%s\", \"senha\": \"%s\" }", nome, email, senha);
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(webservice)
                .method("POST", body)
                .addHeader("apikey", DBConnection.apiKey)
                .addHeader("Authorization", DBConnection.dbJWT)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .build();
        Response response = client.newCall(request).execute();

        if(response.code() == 201) {
            return true;
        }else {
            return false;
        }

    }

}
