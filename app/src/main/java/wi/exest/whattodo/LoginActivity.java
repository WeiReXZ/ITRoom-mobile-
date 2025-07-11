package wi.exest.whattodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import wi.exest.whattodo.model.User;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.startButton);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            // Пытаемся войти
            signInOrCreateUser(email, password);
        });
    }

    private void signInOrCreateUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Успешный вход
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            startMainActivity();
                        }
                    } else {
                        // Если ошибка входа, пробуем зарегистрировать
                        createNewUser(email, password);
                    }
                });
    }

    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Успешная регистрация
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Обновляем профиль (опционально)
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(email.split("@")[0]) // Используем имя до @ как имя пользователя
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Создаем запись в Firestore
                                            createUserInFirestore(user.getUid(), email);
                                        }
                                    });
                        }
                    } else {
                        // Ошибка регистрации
                        Toast.makeText(LoginActivity.this,
                                "Ошибка: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserInFirestore(String uid, String email) {
        User newUser = new User(uid, email, null, null);

        db.collection("users").document(uid)
                .set(newUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(LoginActivity.this,
                            "Аккаунт создан!",
                            Toast.LENGTH_SHORT).show();
                    startMainActivity();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this,
                            "Ошибка создания профиля: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}