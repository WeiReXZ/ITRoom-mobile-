package wi.exest.whattodo.model;


import java.util.List;

public class User {
    private String uid;
    private String email;
    private List<String> preferences;
    private List<String> favorites;

    public User() {}

    public User(String uid, String email, List<String> preferences, List<String> favorites) {
        this.uid = uid;
        this.email = email;
        this.preferences = preferences;
        this.favorites = favorites;
    }
    // Геттеры и сеттеры
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getPreferences() { return preferences; }
    public void setPreferences(List<String> preferences) { this.preferences = preferences; }
    public List<String> getFavorites() { return favorites; }
    public void setFavorites(List<String> favorites) { this.favorites = favorites; }
}