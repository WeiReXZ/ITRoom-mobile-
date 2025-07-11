package wi.exest.whattodo.Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wi.exest.whattodo.model.Activity;

public class LocalActivityDataSource {

    public static List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();

        // Веселое настроение
        activities.add(new Activity("Караоке-вечеринка", "Спойте любимые песни с друзьями", 1500, 120, "веселое", 4, "indoor", "any", 4.7));
        activities.add(new Activity("Пейнтбол", "Активная командная игра на свежем воздухе", 2500, 180, "веселое", 8, "outdoor", "clear", 4.8));
        activities.add(new Activity("Настольные игры", "Интеллектуальные и веселые игры в уютной атмосфере", 500, 120, "веселое", 6, "indoor", "any", 4.5));

        // Романтическое настроение
        activities.add(new Activity("Ужин при свечах", "Романтический ужин в ресторане с живой музыкой", 3500, 150, "романтическое", 2, "indoor", "any", 4.9));
        activities.add(new Activity("Прогулка на теплоходе", "Вечерняя прогулка по реке с прекрасными видами", 2000, 120, "романтическое", 2, "outdoor", "clear", 4.6));
        activities.add(new Activity("Мастер-класс по танцам", "Научитесь танцевать вместе под руководством профессионала", 2500, 90, "романтическое", 2, "indoor", "any", 4.4));

        // Спокойное настроение
        activities.add(new Activity("Йога в парке", "Расслабляющая практика на свежем воздухе", 0, 90, "спокойное", 1, "outdoor", "clear", 4.3));
        activities.add(new Activity("Чайная церемония", "Знакомство с традициями чайной культуры", 1200, 120, "спокойное", 4, "indoor", "any", 4.5));
        activities.add(new Activity("Прогулка в ботаническом саду", "Спокойная прогулка среди редких растений", 800, 180, "спокойное", 4, "outdoor", "clear", 4.7));

        // Активное настроение
        activities.add(new Activity("Скалолазание", "Покорение вершин в скалодроме", 1800, 120, "активное", 4, "indoor", "any", 4.6));
        activities.add(new Activity("Велосипедная прогулка", "Исследуйте городские парки на велосипеде", 500, 180, "активное", 4, "outdoor", "clear", 4.8));
        activities.add(new Activity("Квест-комната", "Погрузитесь в атмосферу детектива и решите головоломки", 2500, 90, "активное", 4, "indoor", "any", 4.9));

        // Грустное настроение
        activities.add(new Activity("Просмотр комедии", "Поднимите настроение хорошей комедией", 800, 120, "грустное", 2, "indoor", "any", 4.2));
        activities.add(new Activity("Встреча с друзьями", "Душевный разговор за чашкой кофе", 700, 90, "грустное", 4, "indoor", "any", 4.6));
        activities.add(new Activity("Прогулка у озера", "Созерцание воды успокаивает и настраивает на позитив", 0, 120, "грустное", 1, "outdoor", "clear", 4.4));

        return activities;
    }
}