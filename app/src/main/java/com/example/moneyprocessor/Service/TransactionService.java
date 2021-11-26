package com.example.moneyprocessor.Service;

import com.example.moneyprocessor.Security.DBConnection;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransactionService {

    private static String webservice = String.format("%s/rest/v1/transaction", DBConnection.dbHost);

    public static boolean cadastraDespesa(double value, String date, String title, String userId) {
        // api

        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");

            String params = String.format(" {\"date\": \"%s\", \"title\": \"%s\", \"value\": \"%.2f\", \"type\": \"d\", \"user_id\": \"%s\"} ", date, title, value, userId);
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

            System.out.println(response.code());
            System.out.println(params);
            System.out.println(webservice);

            if(response.code() == 201) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean cadastraReceita(double value, String date, String title, String userId) {
        // api

        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");

            String params = String.format(" {\"date\": \"%s\", \"title\": \"%s\", \"value\": \"%.2f\", \"type\": \"r\", \"user_id\": \"%s\"} ", date, title, value, userId);
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

            System.out.println(response.code());
            System.out.println(params);
            System.out.println(webservice);

            if(response.code() == 201) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
