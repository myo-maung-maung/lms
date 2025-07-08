package lms.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lms.com.entity.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @ElementCollection
    @CollectionTable(name = "course_images", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "image_path")
    private List<String> imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Video> videos;

    // No-args constructor
//    public Course() {
//
//    }

    // All-args constructor
//    public Course(Long id, String title, User instructor, Category category,
//                  List<Lecture> lectures, List<Enrollment> enrollments) {
//        this.id = id;
//        this.title = title;
//        this.instructor = instructor;
//        this.category = category;
//        this.lectures = lectures;
//        this.enrollments = enrollments;
//    }

    // Builder Pattern
//    public static class Builder {
//        private Long id;
//        private String title;
//        private User instructor;
//        private List<Lecture> lectures;
//        private List<Enrollment> enrollments;
//
//        public Builder id(Long id) {
//            this.id = id;
//            return this;
//        }
//
//        public Builder title(String title) {
//            this.title = title;
//            return this;
//        }
//
//        public Builder instructor(User instructor) {
//            this.instructor = instructor;
//            return this;
//        }
//
//        public Builder category(Category category) {
//            this.category = category;
//            return this;
//        }
//
//        public Builder lectures(List<Lecture> lectures) {
//            this.lectures = lectures;
//            return this;
//        }
//
//        public Builder enrollments(List<Enrollment> enrollments) {
//            this.enrollments = enrollments;
//            return this;
//        }
//
//        public Course build() {
//            Course course = new Course();
//            course.setId(id);
//            course.setTitle(title);
//            course.setInstructor(instructor);
//            course.setCategory(category);
//            course.setLectures(lectures);
//            course.setEnrollments(enrollments);
//            return course;
//        }
//    }

    // Getter and Setter
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public User getInstructor() {
//        return instructor;
//    }
//
//    public void setInstructor(User instructor) {
//        this.instructor = instructor;
//    }
//
//    public Category getCategory() {
//        return category;
//    }
//
//    public void setCategory(Category category) {
//        this.category = category;
//    }
//
//    public List<Lecture> getLectures() {
//        return lectures;
//    }
//
//    public void setLectures(List<Lecture> lectures) {
//        this.lectures = lectures;
//    }
//
//    public List<Enrollment> getEnrollments() {
//        return enrollments;
//    }
//
//    public void setEnrollments(List<Enrollment> enrollments) {
//        this.enrollments = enrollments;
//    }

    // equals() and hashCode()
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Course)) return false;
//        Course course = (Course) o;
//        return Objects.equals(getId(), course.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId());
//    }

    // toString()
//    @Override
//    public String toString() {
//        return "Course{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", instructor=" + (instructor != null ? instructor.getId() : null) +
//                ", category=" + (category != null ? category.getId() : null) +
//                '}';
//    }
}
