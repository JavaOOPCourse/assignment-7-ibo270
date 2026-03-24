
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentRecordProcessor {
    // Поля для хранения данных
    private final List<Student> students = new ArrayList<>();

    private static final String INPUT_PATH = "data/students.txt";
    private static final String OUTPUT_PATH = "output/report.txt";

    // _____реализуйте класс Student ниже в этом же файле______

    private double averageScore;
    private Student highestStudent;


    /**
     * Task 1 + Task 2 + Task 5 + Task 6
     */
    public void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    if (score < 0 || score > 100) {
                        throw new IboError("Score out of range: " + score);
                    }
                    students.add(new Student(name, score));
                    System.out.println(line);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException | IboError e) {
                    System.out.println("Invalid data: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + INPUT_PATH);
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Task 3 + Task 8
     */
    public void processData() {
        if (students.isEmpty()) {
            averageScore = 0.0;
            highestStudent = null;
            return;
        }

        int sum = 0;
        highestStudent = students.get(0);
        for (Student student : students) {
            sum += student.score;
            if (student.score > highestStudent.score) {
                highestStudent = student;
            }
        }
        averageScore = sum / (double) students.size();

        Collections.sort(students);
    }

    /**
     * Task 4 + Task 5 + Task 8
     */
    public void writeFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH))) {
            bw.write("Average: " + averageScore);
            bw.newLine();
            if (highestStudent == null) {
                bw.write("Highest: N/A");
            } else {
                bw.write("Highest: " + highestStudent.name + " - " + highestStudent.score);
            }
            bw.newLine();
            for (Student student : students) {
                bw.write(student.name + " - " + student.score);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentRecordProcessor processor = new StudentRecordProcessor();

        try {
            processor.readFile();
            processor.processData();
            processor.writeFile();
            System.out.println("Processing completed. Check output/report.txt");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

class IboError extends Exception {
    public IboError(String message) {
        super(message);
    }
}

class Student implements Comparable<Student> {
    String name;
    int score;

    public Student(String name, int score) throws IboError {
        if (score < 0 || score > 100) {
            throw new IboError("Score out of range: " + score);
        }
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(Student other) {
        int scoreCompare = Integer.compare(other.score, this.score);
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return this.name.compareTo(other.name);
    }
}