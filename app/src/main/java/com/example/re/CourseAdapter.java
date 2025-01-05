package com.example.re;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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

        holder.tvCourseNameAndCode.setText(course.getCourseName() + " (" + course.getCourseCode() + ")");
        holder.tvDepartment.setText(course.getDepartment());
        holder.tvCourseType.setText(course.getCourseType());
        holder.tvSubjectType.setText(course.getSubjectType());
        holder.tvCourseSectionAndProfessor.setText(course.getSection() == null
            ? course.getProfessor()
            : "분반 " + course.getSection() + " / " + course.getProfessor());
        holder.tvLPC.setText(course.getLpc());
        holder.tvCapacity.setText("정원 " + course.getCapacity());
        holder.tvLectureTime.setText(course.getLectureTime());
        holder.tvLectureRoom.setText(course.getLectureRoom());

        holder.itemView.setOnClickListener(view -> {
            showCourseDialog(view.getContext(), course);
        });
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

    private void showCourseDialog(Context context, CourseResponse course) {
        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.item_specific_course_card, null);

        TextView tvPopupTitle = dialogView.findViewById(R.id.popup_title);
        TextView tvEvaluation = dialogView.findViewById(R.id.card_evaluation);
        TextView tvLectureType = dialogView.findViewById(R.id.card_lecture_type);
        TextView tvEnglish = dialogView.findViewById(R.id.card_english);
        TextView tvAu = dialogView.findViewById(R.id.card_au);
        TextView tvEdu4q = dialogView.findViewById(R.id.card_edu4q);
        TextView tvSyllabus = dialogView.findViewById(R.id.card_syllabus);
        TextView tvEnrolled = dialogView.findViewById(R.id.card_enrolled);
        TextView tvMutual = dialogView.findViewById(R.id.card_mutual);
        TextView tvSharedCourse = dialogView.findViewById(R.id.card_shared_course);
        TextView tvAlternativeRetake = dialogView.findViewById(R.id.card_alternative_retake);
        TextView tvSubCode = dialogView.findViewById(R.id.card_sub_code);
        TextView tvPrevCourseCode = dialogView.findViewById(R.id.card_prev_course_code);
        TextView tvPrevComCode = dialogView.findViewById(R.id.card_prev_com_code);
        TextView tvAdditional = dialogView.findViewById(R.id.card_additional);
        TextView tvRemarks = dialogView.findViewById(R.id.card_remarks);

        tvPopupTitle.setText(course.getCourseName() + " (" + course.getCourseCode() + ")");
        tvEvaluation.setText("평가유형: " + course.getEvaluation());
        tvLectureType.setText("강의유형: " + course.getLectureType());
        tvEnglish.setText("영어: " + course.getEnglish());
        tvAu.setText("AU: " + course.getAu());
        tvEdu4q.setText("Edu 4.0Q: " + course.getEdu4q());
        tvSyllabus.setText("강의계획서: " + course.getSyllabus());
        tvEnrolled.setText("수강인원: " + course.getEnrolled());
        tvMutual.setText("[기준과목] 공유과목: " + (course.getSharedCourse() == null
                ? "없음"
                : course.getSharedCourse()));
        tvSharedCourse.setText("상호인정: " + course.getMutual());
        tvAlternativeRetake.setText("대체과목(재수강): " + (course.getAlternativeRetake() == null
                ? "없음"
                : course.getAlternativeRetake()));
        tvSubCode.setText("부제코드: " + (course.getSubCode() == null
                ? "없음"
                : course.getSubCode()));
        tvPrevCourseCode.setText("과거 과목코드: " + course.getPrevCourseCode());
        tvPrevComCode.setText("과거 전산코드: " + course.getPrevComCode());
        tvAdditional.setText("부가정보: " + (course.getAdditional() == null
                ? "없음"
                : course.getAdditional()));
        tvRemarks.setText("비고: " + (course.getRemarks() == null
                ? "없음"
                : "\n" + course.getRemarks()));

        new AlertDialog.Builder(context)
                .setView(dialogView)
                .create()
                .show();

    }
}
