package wi.exest.whattodo;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wi.exest.whattodo.Class.LocalActivityDataSource;
import wi.exest.whattodo.adapter.ActivityAdapter;
import wi.exest.whattodo.model.Activity;

public class RecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private TextView title, emptyView;
    private List<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        // Инициализация UI
        initUI();

        // Получение параметров
        String mood = getIntent().getStringExtra("mood");
        int timeMinutes = getIntent().getIntExtra("time", 120);
        double budget = getIntent().getDoubleExtra("budget", 1000);
        int people = getIntent().getIntExtra("people", 2);

        // Форматирование параметров для отображения
        displaySearchParameters(mood, timeMinutes, budget, people);

        // Получение рекомендаций
        getRecommendations(mood, timeMinutes, budget, people);
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        title = findViewById(R.id.title);
        emptyView = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ActivityAdapter(activities);
        recyclerView.setAdapter(adapter);
    }

    private void displaySearchParameters(String mood, int timeMinutes, double budget, int people) {
        int hours = timeMinutes / 60;
        int minutes = timeMinutes % 60;

        String timeText;
        if (hours > 0) {
            timeText = minutes > 0 ?
                    String.format("%d ч %d мин", hours, minutes) :
                    String.format("%d ч", hours);
        } else {
            timeText = String.format("%d мин", minutes);
        }

        title.setText(String.format("Идеи для %s настроения\n%s • %d руб • %d чел",
                mood, timeText, (int) budget, people));
    }

    private void getRecommendations(String mood, int timeMinutes, double budget, int people) {
        // Получаем все возможные активности
        List<Activity> allActivities = LocalActivityDataSource.getAllActivities();

        // Фильтруем по критериям
        List<Activity> filteredActivities = new ArrayList<>();
        for (Activity activity : allActivities) {
            if (activity.getMood().equalsIgnoreCase(mood) &&
                    activity.getDuration() <= timeMinutes &&
                    activity.getCost() <= budget &&
                    activity.getPeople() <= people) {
                filteredActivities.add(activity);
            }
        }

        // Если результатов мало, расширяем критерии
        if (filteredActivities.size() < 5) {
            for (Activity activity : allActivities) {
                if (activity.getMood().equalsIgnoreCase(mood) &&
                        activity.getDuration() <= timeMinutes + 60 && // +1 час
                        activity.getCost() <= budget * 1.7 && // +70% бюджета
                        activity.getPeople() <= people + 3 && // +3 человека
                        !filteredActivities.contains(activity)) {
                    filteredActivities.add(activity);
                }
            }
        }

        // Сортировка по популярности (по убыванию)
        filteredActivities.sort((a1, a2) -> Double.compare(a2.getPopularity(), a1.getPopularity()));

        // Добавляем 3 случайных активности, если результатов все еще мало
        if (filteredActivities.size() < 5) {
            addRandomActivities(filteredActivities, allActivities, mood, 5 - filteredActivities.size());
        }

        // Обновляем список
        activities.clear();
        activities.addAll(filteredActivities);
        updateUI();
    }

    private void addRandomActivities(List<Activity> filtered, List<Activity> all, String mood, int count) {
        List<Activity> moodActivities = new ArrayList<>();
        for (Activity activity : all) {
            if (activity.getMood().equalsIgnoreCase(mood)) {
                moodActivities.add(activity);
            }
        }

        if (moodActivities.isEmpty()) return;

        Random random = new Random();
        for (int i = 0; i < count && !moodActivities.isEmpty(); i++) {
            int index = random.nextInt(moodActivities.size());
            Activity randomActivity = moodActivities.get(index);
            if (!filtered.contains(randomActivity)) {
                filtered.add(randomActivity);
            }
            moodActivities.remove(index);
        }
    }

    private void updateUI() {
        if (activities.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyView.setText("Не найдено подходящих активностей\nПопробуйте изменить параметры поиска");
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
}