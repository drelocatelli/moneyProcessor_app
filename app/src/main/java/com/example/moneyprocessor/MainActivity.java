package com.example.moneyprocessor;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import java.net.URL;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moneyprocessor.Service.UserService;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import org.json.JSONObject;

public class MainActivity extends IntroActivity {

    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // external api permisions
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        // Slides

        // botoes volta e avança
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(R.layout.intro_1)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_light)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_green_light)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_purple)
                .fragment(R.layout.intro_4)
                // nao fechar apos ultimo slide
                .canGoForward(true)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(R.layout.intro_cadastro)
                // nao fechar apos ultimo slide
                .canGoForward(false)
                .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaUsuarioLogado();

    }

    public void verificaUsuarioLogado() {
        SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);

        boolean hasCache = (pref.getString("email", null) != null) && (pref.getString("nome", null) != null);
        String emailCached = pref.getString("email", null);

        if(hasCache) {
            // verifica cache e abre tela principal
            if(UserService.getNomeByEmail(emailCached) != null){
                startActivity(new Intent(this, PrincipalActivity.class));
                finish();
            }

        }
    }

    public void btEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

}
