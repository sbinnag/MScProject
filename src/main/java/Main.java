import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Supervisor> supervisors = new ArrayList<>();

        // Student data collection
        String path = Input.StudentDatabase;
        String line;
        try {
            BufferedReader studentInformation = new BufferedReader(new FileReader(path));
            studentInformation.readLine();
            while ((line = studentInformation.readLine()) != null) {
                String[] lineData = line.split(",", -1);
                Student tempStudent = new Student(lineData[0], lineData[1], new String[]{lineData[2], lineData[3]}, new String[]{lineData[4], lineData[5], lineData[6], lineData[7], lineData[8]});
                students.add(tempStudent);
            }
        } catch (Exception e) {
            System.out.println("[EXCEPTION] " + e);
        }

        // Supervisor data collection
        path = Input.SupervisorDatabase;
        try {
            BufferedReader supervisorInformation = new BufferedReader(new FileReader(path));
            supervisorInformation.readLine();
            while ((line = supervisorInformation.readLine()) != null) {
                String[] lineData = line.split(",", -1);
                Supervisor tempSupervisor = new Supervisor(lineData[0], lineData[1], new String[]{lineData[2], lineData[3], lineData[4], lineData[5], lineData[6]}, new String[]{lineData[7], lineData[8], lineData[9], lineData[10], lineData[11]});
                supervisors.add(tempSupervisor);
            }
        } catch (Exception e) {
            System.out.println("[EXCEPTION] " + e);
        }

        final int maxAssignees = Input.maxAssignees;
        ArrayList<Student> checkingStudents = new ArrayList<>(students);  // Create a copy of all students, checkingStudents allows them to be re-added.  students is the "register" of all students.

        for (Supervisor currentSv : supervisors) {
            if (!currentSv.studentPreferences[0].isEmpty()) {
                for (int x = 0; x < currentSv.studentPreferences.length; x++) {
                    String chosenStudents = currentSv.studentPreferences[x];
                    for (Student iterateStudent : students) {
                        if (iterateStudent.getStudentID().equals(chosenStudents) && Arrays.asList(iterateStudent.getSupervisorPreferences()).contains(currentSv.getSupervisorID())) {
                            if (!iterateStudent.isSupervisorAssigned()) {
                                iterateStudent.setPair(currentSv);
                                currentSv.addAssignee(iterateStudent);
                            } else {
                                Supervisor assignedSv = iterateStudent.getPair();
                                int indexAssigned = Arrays.asList(iterateStudent.getSupervisorPreferences()).indexOf(assignedSv.getSupervisorID());
                                int indexCurrent = Arrays.asList(iterateStudent.getSupervisorPreferences()).indexOf(currentSv.getSupervisorID());
                                if (indexCurrent < indexAssigned) {
                                    assignedSv.removeAssignee(iterateStudent);
                                    currentSv.addAssignee(iterateStudent);
                                    iterateStudent.setPair(currentSv);
                                }
                            }
                            iterateStudent.matched();
                            checkingStudents.remove(iterateStudent);
                        }
                    }
                }
            }
        }


        ArrayList<Student> studentsWithPref = new ArrayList<>();
        for (Student currentSt : students) {
            if (!currentSt.getSupervisorPreferences()[0].isEmpty()) {
                studentsWithPref.add(currentSt);
            }
        }

        while (!studentsWithPref.isEmpty()) {
            for (int z = 0; z < studentsWithPref.size(); z++) {
                Student currentSt = studentsWithPref.get(z);
                currentSt.handle();
                preferenceMet:
                if (currentSt.getTimesHandled() <= 6) {
                    for (int x = 0; x < currentSt.getSupervisorPreferences().length; x++) {
                        if (!currentSt.isSupervisorAssigned()) {
                            String chosenSupervisor = currentSt.getSupervisorPreferences()[x];
                            for (Supervisor iterateSupervisor : supervisors) {
                                if (iterateSupervisor.supervisorID.equals(chosenSupervisor) && Arrays.asList(iterateSupervisor.coursePreferences).contains(currentSt.getCourse())) {
                                    if (iterateSupervisor.countAssignees() < maxAssignees) {
                                        currentSt.setPair(iterateSupervisor);
                                        currentSt.matched();
                                        iterateSupervisor.addAssignee(currentSt);
                                        studentsWithPref.remove(currentSt);
                                        checkingStudents.remove(currentSt);
                                        break preferenceMet;
                                    } else if (iterateSupervisor.countAssignees() > maxAssignees - 1) {
                                        Student worstStudent = null;
                                        int indexOfWorstStudent = -1;
                                        for (int i = 0; i < iterateSupervisor.countAssignees(); i++) {
                                            Student testStudent = iterateSupervisor.getAssignedStudents().get(i);
                                            if (!iterateSupervisor.checkStudentPreference(testStudent)) {
                                                int indexOfTestStudent = Arrays.asList(iterateSupervisor.getCoursePreferences()).indexOf(testStudent.getCourse());
                                                if (indexOfTestStudent >= indexOfWorstStudent) {
                                                    indexOfWorstStudent = indexOfTestStudent;
                                                    worstStudent = testStudent;
                                                }
                                            }
                                        }
                                        int indexOfCurrentStudent = Arrays.asList(iterateSupervisor.getCoursePreferences()).indexOf((currentSt.getCourse()));
                                        if (indexOfCurrentStudent < indexOfWorstStudent) {
                                            iterateSupervisor.removeAssignee(worstStudent);
                                            worstStudent.unmatched();
                                            worstStudent.setPair(null);
                                            checkingStudents.add(worstStudent);
                                            studentsWithPref.add(worstStudent);
                                            iterateSupervisor.addAssignee(currentSt);
                                            currentSt.setPair(iterateSupervisor);
                                            currentSt.matched();
                                            checkingStudents.remove(currentSt);
                                            studentsWithPref.remove(currentSt);
                                            break preferenceMet;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (currentSt.getTimesHandled() > 6) {
                    studentsWithPref.remove(currentSt);
                }
            }
        }

        ArrayList<Supervisor> workingSupervisors = new ArrayList<>();

        for (Supervisor currentSup : supervisors) {
            if (currentSup.countAssignees() < maxAssignees) {
                workingSupervisors.add(currentSup);
            }
        }

        Comparator<Supervisor> supervisorComparator = new Comparator<>() {
            @Override
            public int compare(Supervisor s1, Supervisor s2) {
                return Integer.compare(s1.countAssignees(), s2.countAssignees());
            }
        };

        Comparator<Student> topicPrefComparator = new Comparator<Student>() {
            @Override
            public int compare(Student st1, Student st2) {
                if (st1.getTopicPreferences()[1] == null) {
                    return -1;
                }
                if (st2.getTopicPreferences()[1] == null) {
                    return 1;
                }
                return st1.getTopicPreferences()[1].compareTo(st2.getTopicPreferences()[1]);
            }
        };

        for(Student iterateStudent: checkingStudents){
            iterateStudent.setTimesHandled(0);
        }

        checkingStudents.sort(topicPrefComparator);


        while (!checkingStudents.isEmpty()) { // Contains only bounced-back pref students and students without pref.
            // Topic-based
            for (int z = 0; z < checkingStudents.size(); z++) {
                Student currentSt = checkingStudents.get(z);
                currentSt.handle();
                if(currentSt.getTimesHandled() > 2){
                    checkingStudents.remove(currentSt);
                }
                // Reorder arraylist to ensure least subscribed supervisors are first
                workingSupervisors.sort(supervisorComparator);
                topicPrefAssigned:
                for (int i = 0; i < currentSt.getTopicPreferences().length; i++) {
                    for (int x = 0; x < workingSupervisors.size() ; x++) {
                        Supervisor currentSup = workingSupervisors.get(x);
                        if (currentSup.getTopic().equals(currentSt.getTopicPreferences()[i])) {
                            currentSt.matched();
                            currentSt.setPair(currentSup);
                            currentSup.addAssignee(currentSt);
                            checkingStudents.remove(currentSt);
                            if (currentSup.getAssignedStudents().size() >= maxAssignees) {
                                workingSupervisors.remove(currentSup);
                            }
                            break topicPrefAssigned;
                        }
                    }
                }
            }
        }

        Supervisor empty = new Supervisor(null, null, new String[]{null, null, null, null, null}, new String[]{null, null, null, null, null});
        supervisors.add(empty);

        for (Student iterateStudents : students) {
            if (!iterateStudents.isSupervisorAssigned()) {
                System.out.println("Student " + iterateStudents.getStudentID() + " has not been assigned");
                iterateStudents.setPair(empty);
            }
        }


        File csvFile1 = new File("Supervisor_Assignees");
        try {
            FileWriter output = new FileWriter(csvFile1);
            CSVWriter writer = new CSVWriter(output);
            List<String[]> data1 = new ArrayList<>();
            StringBuilder header1 = new StringBuilder();
            header1.append("SupervisorID");
            header1.append(",");
            for (int x = 1; x < maxAssignees + 1; x++) {
                header1.append("StudentID_");
                header1.append(x);
                header1.append(",");
            }
            data1.add(new String[]{String.valueOf(header1)});
            StringBuilder dataContents = new StringBuilder();
            for (Supervisor iterateSupervisor : supervisors) {
                dataContents.append(iterateSupervisor.getSupervisorID());
                dataContents.append(",");
                for (int i = 0; i < iterateSupervisor.countAssignees(); i++) {
                    dataContents.append(iterateSupervisor.getAssignedStudents().get(i).getStudentID());
                    dataContents.append(",");
                }
                data1.add(new String[]{String.valueOf(dataContents)});
                dataContents.delete(0, dataContents.length());
            }

            writer.writeAll(data1);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File csvFile2 = new File("Student-SupervisorAllocation");
        try {
            FileWriter output2 = new FileWriter(csvFile2);
            CSVWriter writer = new CSVWriter(output2);
            List<String[]> data2 = new ArrayList<>();
            data2.add(new String[]{"StudentID,SupervisorID"});
            StringBuilder IDpairs = new StringBuilder();
            for (Student iterateStudents : students) {
                IDpairs.append(iterateStudents.getStudentID());
                IDpairs.append(",");
                IDpairs.append(iterateStudents.getPair().getSupervisorID());
                IDpairs.append(",");
                data2.add(new String[]{String.valueOf(IDpairs)});
                IDpairs.delete(0, IDpairs.length());
            }
            writer.writeAll(data2);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

