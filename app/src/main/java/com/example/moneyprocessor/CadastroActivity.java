package com.example.moneyprocessor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneyprocessor.Models.Usuario;
import com.example.moneyprocessor.Service.UserService;

import java.util.regex.Pattern;

public class CadastroActivity extends AppCompatActivity {

    private static EditText campoNome;
    private static EditText campoEmail;
    private static EditText campoSenha;
    private static Button btnCadastro;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

    }

    private void cadastrar(){

        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        boolean camposNulos = textoNome.isEmpty() || textoEmail.isEmpty() || textoSenha.isEmpty();

        String regexEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        boolean emailValido = Pattern.compile(regexEmail).matcher(textoEmail).matches();

        // checa se input esta preenchido
        if(!camposNulos && emailValido) {

            try {
                // cadastra no banco se email nao existir
                if(!UserService.emailExists(textoEmail)) {
                    usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);

                    if(UserService.cadastroUsuarioDB(usuario.getNome(), usuario.getEmail(), usuario.getSenha())){
                        Toast.makeText(CadastroActivity.this, "Agora você pode se logar", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                    }else{
                        Toast.makeText(CadastroActivity.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CadastroActivity.this, "E-mail já foi cadastrado", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(CadastroActivity.this, "Ocorreu um erro interno", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }else if(!emailValido) {
            Toast.makeText(CadastroActivity.this, "Insira um e-mail correspondente", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(CadastroActivity.this, "Campos não podem ser nulos", Toast.LENGTH_SHORT).show();
        }

    }
}