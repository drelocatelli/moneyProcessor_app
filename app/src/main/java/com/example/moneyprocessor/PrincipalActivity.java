package com.example.moneyprocessor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moneyprocessor.Service.UserService;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private MaterialCalendarView calendarView;

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

        calendarView = findViewById(R.id.calendarView);

        // calendar handling
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2015, 1, 1))
        .commit();
        calendarView.setTitleMonths(new CharSequence[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"});

        calendarView.setWeekDayLabels(new CharSequence[] {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"});
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String mes = String.valueOf(date.getMonth() + 1);
                String dia = String.valueOf(date.getDay());
                mes = (Integer.valueOf(mes) <= 9 ? "0"+mes : mes);
                dia = (Integer.valueOf(dia) <= 9 ? "0"+dia : dia);

                String data = String.format("%s/%s/%s", dia, mes, String.valueOf(date.getYear()));
                System.out.println(data);
            }
        });
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