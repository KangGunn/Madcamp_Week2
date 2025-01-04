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
                .inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseResponse course = courses.get(position);
        holder.tvCourseNameAndCode.setText(course.getCourseName() + "(" + course.getCourseCode() + ")");
        holder.tvDepartment.setText(course.getDepartment());
        holder.tvCourseType.setText(course.getCourseType());
        holder.tvSubjectType.setText(course.getSubjectType());
        if (course.getSection() == null) {
            holder.tvCourseSectionAndProfessor.setText(course.getProfessor());
        } else {
            holder.tvCourseSectionAndProfessor.setText("분반 " + course.getSection() + " / " + course.getProfessor());
        }
        holder.tvLPC.setText(course.getLpc());
        holder.tvCapacity.setText("정원 " + course.getCapacity());
        holder.tvLectureTime.setText(course.getLectureTime());
        holder.tvLectureRoom.setText(course.getLectureRoom());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseNameAndCode, tvDepartment, tvCourseType, tvSubjectType, tvCourseSectionAndProfessor,
                 tvLPC, tvCapacity, tvLectureTime, tvLectureRoom;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseNameAndCode = itemView.findViewById(R.id.card_course_name_and_code);
            tvDepartment = itemView.findViewById(R.id.card_department);
            tvCourseType = itemView.findViewById(R.id.card_course_type);
            tvSubjectType = itemView.findViewById(R.id.card_subject_type);
            tvCourseSectionAndProfessor = itemView.findViewById(R.id.card_section_and_professor);
            tvLPC = itemView.findViewById((R.id.card_lpc));
            tvCapacity = itemView.findViewById(R.id.card_capacity);
            tvLectureTime = itemView.findViewById(R.id.card_lecture_time);
            tvLectureRoom = itemView.findViewById(R.id.card_lecture_room);
        }
    }
}
