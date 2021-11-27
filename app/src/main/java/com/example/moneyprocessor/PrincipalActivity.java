package com.example.moneyprocessor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyprocessor.DTO.TransactionDTO;
import com.example.moneyprocessor.Service.TransactionService;
import com.example.moneyprocessor.Service.UserService;
import com.example.moneyprocessor.adapter.MyAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private MaterialCalendarView calendarView;
    private TextView saldoEl;
    private Button recarregar;
    private RecyclerView recyclerView;
    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // set text from fragment child
        changeFragment();

        // check user
        checkUserandLogout();

        // recarregar
        recarregar = findViewById(R.id.refreshBtn);
        recarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recarregar();
            }
        });

        // obtem saldo
        saldoEl = findViewById(R.id.saldoEl);
        getSaldo();

        calendarView = findViewById(R.id.calendarView);

        // calendar handling
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2015, 1, 1))
        .commit();
        calendarView.setTitleMonths(new CharSequence[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"});

        calendarView.setWeekDayLabels(new CharSequence[] {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"});

        CalendarDay dataAtual = calendarView.getCurrentDate();
        mesAnoSelecionado = String.valueOf((dataAtual.getMonth()+1 <= 9 ? "0"+(dataAtual.getMonth()+1) : (dataAtual.getMonth()+1)) + "/" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mes = String.valueOf(date.getMonth() + 1);
                String dia = String.valueOf(date.getDay());
                mes = (Integer.valueOf(mes) <= 9 ? "0"+mes : mes);
                dia = (Integer.valueOf(dia) <= 9 ? "0"+dia : dia);

                String data = String.format("%s/%s/%s", dia, mes, String.valueOf(date.getYear()));

                mesAnoSelecionado = String.valueOf((date.getMonth()+1 <= 9 ? "0"+(date.getMonth()+1) : (date.getMonth()+1)) + "/" + date.getYear());
                System.out.println(mesAnoSelecionado);
            }
        });

        // get transactions
        recyclerView = findViewById(R.id.recyclerView);
        getTransactions();

    }

    public void getTransactions() {
        SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);
        String userId = UserService.getUserIdByEmail(pref.getString("email", null));

        List<TransactionDTO> transactions = TransactionService.getAllTransactions(userId);

        if(transactions != null && !transactions.isEmpty()) {

            MyAdapter myAdapter;

            // data
            String titles[] = new String[transactions.size()];
            String values[] = new String[transactions.size()];
            String dates[] = new String[transactions.size()];

            System.out.println("=================================== nullpex");

            for(int i = 0; i < transactions.size(); i++) {
                System.out.println("transaction:::::::::::::"+transactions.get(i).getTitle());
                titles[i] = transactions.get(i).getTitle();
                values[i] = transactions.get(i).getValue();
                dates[i] = transactions.get(i).getDate();
            }

            if(titles != null && values != null && dates != null) {
                myAdapter = new MyAdapter(this, titles, values, dates);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

            }

        }

    }

    public void recarregar() {
        getSaldo();
        getTransactions();

        Toast.makeText(PrincipalActivity.this, "Recarregou", Toast.LENGTH_SHORT).show();
    }

    public void getSaldo() {
        SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);
        String userId = UserService.getUserIdByEmail(pref.getString("email", null));

        try {
            double receitas = Double.parseDouble(TransactionService.getReceitasTotalByUserId(userId));
            double despesas = Double.parseDouble(TransactionService.getDespesasTotalByUserId(userId));

            Double calculo = Double.valueOf(receitas - despesas);

            String saldo = String.format("Saldo: R$ %.2f", calculo);

            saldoEl.setText(saldo);
        } catch (Exception e) {
            saldoEl.setText("0.00");

            Toast.makeText(PrincipalActivity.this, "Não foi possivel calcular saldo", Toast.LENGTH_SHORT).show();
        }

    }

    public void limpaCache() {
        SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void checkUserandLogout() {
        SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);
        String nome = UserService.getNomeByEmail(pref.getString("email", null));
        if(nome == null) {
            startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
        }
        // botao sair
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCache();
                startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
            }
        });
    }

    public void changeFragment() {
        SharedPreferences pref = getSharedPreferences("userCache", MODE_PRIVATE);
        String nome = UserService.getNomeByEmail(pref.getString("email", null));

        if(nome != null) {
            FirstFragment.txtnomeEl = String.format("Olá, %s", nome);
        }
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(this, DespesasActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}