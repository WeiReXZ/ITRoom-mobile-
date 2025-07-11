package wi.exest.whattodo.model;

public class Activity {
    private String name;
    private String description;
    private double cost;
    private int duration; // в минутах
    private String mood;
    private int people;
    private String type; // indoor/outdoor
    private String weather; // clear, rain, snow, any
    private double popularity; // 0.0 - 5.0

    // Конструктор
    public Activity(String name, String description, double cost, int duration,
                    String mood, int people, String type, String weather,
                    double popularity) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.duration = duration;
        this.mood = mood;
        this.people = people;
        this.type = type;
        this.weather = weather;
        this.popularity = popularity;
    }

    // Геттеры
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getCost() { return cost; }
    public int getDuration() { return duration; }
    public String getMood() { return mood; }
    public int getPeople() { return people; }
    public String getType() { return type; }
    public String getWeather() { return weather; }
    public double getPopularity() { return popularity; }

    // Форматированные значения для UI
    public String getFormattedDuration() {
        int hours = duration / 60;
        int minutes = duration % 60;
        if (hours > 0) {
            return minutes > 0 ?
                    String.format("%d ч %d мин", hours, minutes) :
                    String.format("%d ч", hours);
        }
        return String.format("%d мин", minutes);
    }

    public String getFormattedCost() {
        if (cost == 0) return "Бесплатно";
        return cost < 1000 ?
                String.format("%d руб", (int) cost) :
                String.format("%.1f тыс. руб", cost / 1000);
    }

    public String getFormattedPopularity() {
        return String.format("%.1f ★", popularity);
    }
}