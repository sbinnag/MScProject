import java.util.Arrays;

public class Student {
    private String studentID;
    private String course;
    private String[] topicPreferences = new String[2];
    private String[] supervisorPreferences = new String[5];
    private boolean supervisorAssigned = false;
    private Supervisor pair;
    private int timesHandled = 0;

    public Student(String studentID,
                   String course,
                   String[] topicPreferences,
                   String[] supervisorPreferences) {
        this.studentID = studentID;
        this.course = course;

        this.topicPreferences = topicPreferences;

        this.supervisorPreferences = supervisorPreferences;
    }


    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String[] getTopicPreferences() {
        return topicPreferences;
    }

    public void setTopicPreferences(String[] topicPreferences) {
        this.topicPreferences = topicPreferences;
    }

    public String[] getSupervisorPreferences() {
        return supervisorPreferences;
    }

    public void setSupervisorPreferences(String[] supervisorPreferences) {
        this.supervisorPreferences = supervisorPreferences;
    }

    public boolean isSupervisorAssigned() {
        return supervisorAssigned;
    }

    public void setSupervisorAssigned(boolean supervisorAssigned) {
        this.supervisorAssigned = supervisorAssigned;
    }

    public void matched() {
        supervisorAssigned = true;
    }

    public void unmatched() {
        supervisorAssigned = false;
    }

    public Supervisor getPair() {
        return pair;
    }

    public void setPair(Supervisor pair) {
        this.pair = pair;
    }

    public int getTimesHandled() {
        return timesHandled;
    }

    public void setTimesHandled(int timesHandled) {
        this.timesHandled = timesHandled;
    }

    public void handle() {
        timesHandled += 1;
    }

}
