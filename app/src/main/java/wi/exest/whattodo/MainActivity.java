package wi.exest.whattodo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.Manifest;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();

        setupMoodSpinner();

        checkAuth();

        checkLocationPermission();

    }

    private void initViews() {
        moodAutoComplete = findViewById(R.id.moodAutoComplete);
        timeInput = findViewById(R.id.timeInput);
        budgetInput = findViewById(R.id.budgetInput);
        peopleInput = findViewById(R.id.peopleInput);
        btnRecommend = findViewById(R.id.btnRecommend);



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

        if (mood.isEmpty() || timeStr.isEmpty() || budgetStr.isEmpty() || peopleStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int hours = Integer.parseInt(timeStr);
            int minutes = hours * 60; // Переводим часы в минуты
            double budget = Double.parseDouble(budgetStr);
            int people = Integer.parseInt(peopleStr);

            if (hours <= 0 || budget < 0 || people <= 0) {
                Toast.makeText(this, "Значения должны быть положительными", Toast.LENGTH_SHORT).show();
                return;
            }

            saveSearchHistory(mood, hours, budget, people);

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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this,
                        "Доступ к местоположению нужен для персонализированных рекомендаций",
                        Toast.LENGTH_LONG).show();
            }

                ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        // Если разрешение уже есть ничего не делаем
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Доступ к местоположению разрешен", Toast.LENGTH_SHORT).show();
            } else {
                // Пользователь отказал
                Toast.makeText(this,
                        "Некоторые функции могут работать ограниченно без доступа к местоположению",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
