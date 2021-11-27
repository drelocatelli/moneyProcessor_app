package com.example.moneyprocessor.Service;

import com.example.moneyprocessor.DTO.TransactionDTO;
import com.example.moneyprocessor.Security.DBConnection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransactionService {

    private static String webservice = String.format("%s/rest/v1/transaction", DBConnection.dbHost);

    public static List<TransactionDTO> getAllTransactions(String id) {
        // api

        try {

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            String service = String.format("%s?user_id=eq.%s&select=*", webservice, id);

            Request request = new Request.Builder()
                    .url(service)
                    .addHeader("apikey", DBConnection.apiKey)
                    .addHeader("Authorization", DBConnection.dbJWT)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build();
            Response res = client.newCall(request).execute();
            String responseData = res.body().string();

            System.out.println("all transactions::::::::::::::" + responseData);

            JSONArray resArray = new JSONArray(responseData);
            int quantity = resArray.length();

            if(quantity > 0) {
                JSONObject resObj = (JSONObject) new JSONArray(responseData).get(0);

                Gson gson= new Gson();
                Type listType = new TypeToken<ArrayList<TransactionDTO>>(){}.getType();
                List<TransactionDTO> list = gson.fromJson(resArray.toString(),listType);

                return list;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getDespesasTotalByUserId(String id) {
        // api

        try {

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            String service = String.format("%s?user_id=eq.%s&type=eq.d&select=value", webservice, id);

            Request request = new Request.Builder()
                    .url(service)
                    .addHeader("apikey", DBConnection.apiKey)
                    .addHeader("Authorization", DBConnection.dbJWT)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build();
            Response res = client.newCall(request).execute();
            String responseData = res.body().string();

            System.out.println("DESPESAS:::::::::::::::::::" + responseData);

            JSONArray resArray = new JSONArray(responseData);
            int quantity = resArray.length();

            double sum = 0.00;

            if(quantity > 0) {

                JSONObject resObj = (JSONObject) new JSONArray(responseData).get(0);

                Gson gson= new Gson();
                Type listType = new TypeToken<ArrayList<TransactionDTO>>(){}.getType();
                List<TransactionDTO> obj = gson.fromJson(resArray.toString(),listType);

                for(TransactionDTO dto : obj) {
                    sum += Double.valueOf(dto.getValue());
                }
            }
            return String.valueOf(sum);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getReceitasTotalByUserId(String id) {
        // api

        try {

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            String service = String.format("%s?user_id=eq.%s&type=eq.r&select=value", webservice, id);

            Request request = new Request.Builder()
                    .url(service)
                    .addHeader("apikey", DBConnection.apiKey)
                    .addHeader("Authorization", DBConnection.dbJWT)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build();
            Response res = client.newCall(request).execute();
            String responseData = res.body().string();

            System.out.println("RECEITAS:::::::::::::::::::" + responseData);


            JSONArray resArray = new JSONArray(responseData);
            int quantity = resArray.length();

            double sum = 0.00;

            if(quantity > 0) {

                JSONObject resObj = (JSONObject) new JSONArray(responseData).get(0);

                Gson gson= new Gson();
                Type listType = new TypeToken<ArrayList<TransactionDTO>>(){}.getType();
                List<TransactionDTO> obj = gson.fromJson(resArray.toString(),listType);


                for(TransactionDTO dto : obj) {
                    sum += Double.valueOf(dto.getValue());
                }

            }

            return String.valueOf(sum);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

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
