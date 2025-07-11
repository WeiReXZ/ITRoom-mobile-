package wi.exest.whattodo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wi.exest.whattodo.R;
import wi.exest.whattodo.model.Activity;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private final List<Activity> activities;

    public ActivityAdapter(List<Activity> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = activities.get(position);

        holder.name.setText(activity.getName());
        holder.description.setText(activity.getDescription());
        holder.duration.setText(activity.getFormattedDuration());
        holder.cost.setText(activity.getFormattedCost());
        holder.people.setText(String.format("%d чел", activity.getPeople()));
        holder.popularity.setText(activity.getFormattedPopularity());

        // Установка иконки в зависимости от типа активности
        int iconRes = activity.getType().equals("outdoor") ?
                R.drawable.ic_outdoor : R.drawable.ic_indoor;
        holder.typeIcon.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name, description, duration, cost, people, popularity;
        public final ImageView typeIcon;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            duration = view.findViewById(R.id.duration);
            cost = view.findViewById(R.id.cost);
            people = view.findViewById(R.id.people);
            popularity = view.findViewById(R.id.popularity);
            typeIcon = view.findViewById(R.id.typeIcon);
        }
    }
}