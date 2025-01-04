package com.example.re;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<CourseResponse> courses;

    public CourseAdapter(List<CourseResponse> courses) {
        this.courses = courses;
    }

    public void updateData(List<CourseResponse> newCourses) {
        this.courses.clear();
        this.courses.addAll(newCourses);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseResponse course = courses.get(position);
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvDepartment.setText(course.getDepartment());
        holder.tvCourseType.setText(course.getCourseType());
        holder.tvSubjectType.setText(course.getSubjectType());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvDepartment, tvCourseType, tvSubjectType;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvCourseType = itemView.findViewById(R.id.tvCourseType);
            tvSubjectType = itemView.findViewById(R.id.tvSubjectType);
        }
    }
}
