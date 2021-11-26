package com.example.moneyprocessor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyprocessor.Models.Transaction;
import com.example.moneyprocessor.Models.Usuario;
import com.example.moneyprocessor.Service.TransactionService;
import com.example.moneyprocessor.Service.UserService;

import java.util.regex.Pattern;

public class DespesasActivity extends AppCompatActivity {

    private static Button salvarBtn;
    private static EditText valueIn;
    private static EditText dateIn;
    private static EditText titleIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        salvarBtn = findViewById(R.id.salvarBtn);
        valueIn = findViewById(R.id.valueIn);
        dateIn = findViewById(R.id.dateIn);
        titleIn = findViewById(R.id.titleIn);

        salvarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarDespesa();
            }
        });

    }

    public void cadastrarDespesa() {

        boolean camposNulos = valueIn.getText().toString().isEmpty() ||
                dateIn.getText().toString().isEmpty() ||
                titleIn.getText().toString().isEmpty();

        if(!camposNulos) {
            SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);
            String userId = UserService.getUserIdByEmail(pref.getString("email", null));

            // sanitizacao
            double value = Double.valueOf(valueIn.getText().toString().replace(",","."));
            String date = dateIn.getText().toString();
            String title = titleIn.getText().toString();

            String regexDate = "^(\\d{1,2})\\/(\\d{1,2})\\/(\\d{4})$";
            boolean validaDate = Pattern.compile(regexDate).matcher(date).matches();

            // cadastra despesa
            if(validaDate) {
                Transaction transaction = new Transaction();
                transaction.setTipo("d");
                transaction.setTitulo(title);
                transaction.setValor(value);
                transaction.setDate(date);

                if(TransactionService.cadastraDespesa(transaction.getValor(), transaction.getDate(), transaction.getTitulo(), userId)) {
                    // volta pra tela inicial
                    Toast.makeText(DespesasActivity.this, "Despesa cadastrada", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DespesasActivity.this, PrincipalActivity.class));
                }else {
                    Toast.makeText(DespesasActivity.this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(DespesasActivity.this, "Insira uma data válida dd/mm/aaaa", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(DespesasActivity.this, "Campos não podem ser nulos", Toast.LENGTH_SHORT).show();
        }


    }

}