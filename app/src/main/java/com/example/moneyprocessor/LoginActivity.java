package com.example.moneyprocessor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneyprocessor.Models.Usuario;
import com.example.moneyprocessor.Service.UserService;

public class LoginActivity extends AppCompatActivity {

    private static EditText campoEmail;
    private static EditText campoSenha;
    private static Button btnCadastro;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        btnCadastro = findViewById(R.id.btnCadastro);


        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    autenticacao();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void autenticacao() throws Exception {

        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        usuario = new Usuario();
        usuario.setSenha(textoSenha);
        usuario.setEmail(textoEmail);

        if(!UserService.emailExists(usuario.getEmail())) {
            Toast.makeText(LoginActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
        }

        if(usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Campos não podem ser nulos", Toast.LENGTH_SHORT).show();
        }else {
            boolean autentica = UserService.loginUsuarioDB(usuario.getEmail(), usuario.getSenha());

            if(autentica) {
                Toast.makeText(LoginActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                preparaUsuario(usuario.getEmail());
            }else {
                Toast.makeText(LoginActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void preparaUsuario(String email) {

        // salva pref
        SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.apply();

        // abre tela principal
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

}