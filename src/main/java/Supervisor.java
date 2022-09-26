import java.util.*;

public class Supervisor {
    String supervisorID;

    String topic;
    String[] coursePreferences;
    String[] studentPreferences;
    private List<Student> assignedStudents = new ArrayList<>();

    public Supervisor(String supervisorID,
                      String topic,
                      String[] coursePreferences,
                      String[] studentPreferences) {
        this.supervisorID = supervisorID;
        this.topic = topic;
        this.coursePreferences = coursePreferences;
        this.studentPreferences = studentPreferences;
    }

    public String getSupervisorID() {
        return supervisorID;
    }

    public void setSupervisorID(String supervisorID) {
        this.supervisorID = supervisorID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String[] getCoursePreferences() {
        return coursePreferences;
    }

    public void setCoursePreferences(String[] coursePreferences) {
        this.coursePreferences = coursePreferences;
    }

    public String[] getStudentPreferences() {
        return studentPreferences;
    }

    public void setStudentPreferences(String[] studentPreferences) {
        this.studentPreferences = studentPreferences;
    }

    public List<Student> getAssignedStudents() {
        return assignedStudents;
    }

    public void setAssignedStudents(List<Student> assignedStudents) {
        this.assignedStudents = assignedStudents;
    }

    public void addAssignee(Student assignee) {
        assignedStudents.add(assignee);
    }

    public void removeAssignee(Student assignee) {
        assignedStudents.remove(assignee);
    }

    public int countAssignees() {
        return assignedStudents.size();
    }

    public boolean checkStudentPreference(Student student) {
        return Arrays.asList(studentPreferences).contains(student.getStudentID());
    }

    @Override
    public String toString() {
        return supervisorID;
    }


    Comparator<Supervisor> supervisorComparator = new Comparator<Supervisor>() {
        @Override
        public int compare(Supervisor s1, Supervisor s2) {
            return Integer.compare(s1.getAssignedStudents().size(), s2.getAssignedStudents().size());
        }
    };
}
