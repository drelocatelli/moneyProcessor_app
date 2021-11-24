package com.example.moneyprocessor.Service;

import com.example.moneyprocessor.Security.DBConnection;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserService {

    public static void cadastroUsuarioDB(String nome, String email, String senha) throws Exception {

        System.out.println(DBConnection.dbHost);
        System.out.println(String.format("%s, %s, %s" ,nome, email, senha));

        // api
        String webservice = String.format("%s/rest/v1/users", DBConnection.dbHost);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        String params = String.format("{\"nome\": \"%s\", \"email\": \"%s\", \"senha\": \"%s\" }", nome, email, senha);
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(webservice)
                .method("POST", body)
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlhdCI6MTYzNzcwNjUxNSwiZXhwIjoxOTUzMjgyNTE1fQ.Su6U126NNBdUpY-0tj2GY1yVwA_Yrk9kEtIHqIqa2wg")
                .addHeader("Authorization", "0784f80f-087d-4e7a-8c9e-cef27b30c6ad")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .build();
        Response response = client.newCall(request).execute();


    }

}
