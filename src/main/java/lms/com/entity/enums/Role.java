package lms.com.entity.enums;

public enum Role {
    ADMIN,
    STUDENT,
    INSTRUCTOR;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
