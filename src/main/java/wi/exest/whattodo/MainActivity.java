package wi.exest.whattodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView moodAutoComplete;
    private TextInputEditText timeInput, budgetInput, peopleInput;
    private MaterialButton btnRecommend;
    private BottomNavigationView bottomNav;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Привязка элементов
        initViews();

        // Настройка выпадающего списка настроений
        setupMoodSpinner();

        // Проверка авторизации
        checkAuth();
    }

    private void initViews() {
        moodAutoComplete = findViewById(R.id.moodAutoComplete);
        timeInput = findViewById(R.id.timeInput);
        budgetInput = findViewById(R.id.budgetInput);
        peopleInput = findViewById(R.id.peopleInput);
        btnRecommend = findViewById(R.id.btnRecommend);


        // Установка подсказки для времени в часах
        timeInput.setHint("Доступное время (часы)");

        btnRecommend.setOnClickListener(v -> getRecommendations());
    }

    private void setupMoodSpinner() {
        String[] moods = getResources().getStringArray(R.array.moods);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                moods);
        moodAutoComplete.setAdapter(adapter);
    }


    private void checkAuth() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void getRecommendations() {
        String mood = moodAutoComplete.getText().toString();
        String timeStr = timeInput.getText().toString();
        String budgetStr = budgetInput.getText().toString();
        String peopleStr = peopleInput.getText().toString();

        // Валидация ввода
        if (mood.isEmpty() || timeStr.isEmpty() || budgetStr.isEmpty() || peopleStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Конвертация часов в минуты для внутренней логики
            int hours = Integer.parseInt(timeStr);
            int minutes = hours * 60; // Переводим часы в минуты
            double budget = Double.parseDouble(budgetStr);
            int people = Integer.parseInt(peopleStr);

            if (hours <= 0 || budget < 0 || people <= 0) {
                Toast.makeText(this, "Значения должны быть положительными", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохраняем историю поиска
            saveSearchHistory(mood, hours, budget, people);

            // Переход к рекомендациям
            Intent intent = new Intent(this, RecommendationActivity.class);
            intent.putExtra("mood", mood);
            intent.putExtra("time", minutes); // Передаем в минутах
            intent.putExtra("budget", budget);
            intent.putExtra("people", people);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите корректные числа", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSearchHistory(String mood, int hours, double budget, int people) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Map<String, Object> search = new HashMap<>();
            search.put("mood", mood);
            search.put("hours", hours);
            search.put("budget", budget);
            search.put("people", people);
            search.put("timestamp", System.currentTimeMillis());

            db.collection("users")
                    .document(user.getUid())
                    .collection("history")
                    .add(search);
        }
    }
}