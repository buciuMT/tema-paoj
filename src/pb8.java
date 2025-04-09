import java.util.Arrays;
import java.util.Objects;

final class Student2 implements Comparable<Student2> {
    final String nume;
    final int varsta;
    final double medie;

    public Student2(String nume, int varsta, double medie) {
        this.nume = nume;
        this.varsta = varsta;
        this.medie = medie;
    }

    @Override
    public int compareTo(Student2 s) {
        if (this.medie == s.medie)
            return this.nume.compareTo(s.nume);
        return this.medie < s.medie ? -1 : 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Student2 s)) return false;
        else return this.compareTo(s) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, varsta, medie);
    }

    @Override
    public String toString() {
        return "Student2{" +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", medie=" + medie +
                '}';
    }
}



class Grup {
    private Student2[] studenti;
    private int numarStudenti;

    // Constructor
    public Grup(int capacitate) {
        studenti = new Student2[capacitate];
        numarStudenti = 0;
    }

    // Adăugarea unui student
    public boolean adaugaStudent(Student2 student) {
        if (numarStudenti < studenti.length) {
            studenti[numarStudenti++] = student;
            return true;
        }
        return false;
    }

    // Căutarea unui student după nume
    public Student2 cautaStudentDupaNume(String nume) {
        for (int i = 0; i < numarStudenti; i++) {
            if (studenti[i].nume.equals(nume)) {
                return studenti[i];
            }
        }
        return null;
    }

    public void sorteazaStudenti() {
        Arrays.sort(studenti, 0, numarStudenti);
    }

    public String raporteazaDetaliat() {
        StringBuilder raport = new StringBuilder();
        raport.append("Raport detaliat despre studenti:\n");
        for (int i = 0; i < numarStudenti; i++) {
            raport.append(studenti[i].toString()).append("\n");
        }
        return raport.toString();
    }

    public String raporteazaSincronizat() {
        StringBuffer raport = new StringBuffer();
        raport.append("Raport sincronizat despre studenti:\n");
        for (int i = 0; i < numarStudenti; i++) {
            raport.append(studenti[i].toString()).append("\n");
        }
        return raport.toString();
    }

}



public class pb8 {
    public static void main(String[] args) {
        Grup grup = new Grup(5);

        grup.adaugaStudent(new Student2("Popescu Ion", 20, 8.5));
        grup.adaugaStudent(new Student2("Popescu Ivan", 22, 9.2));
        grup.adaugaStudent(new Student2("Iliescu Ion", 21, 7.4));

        grup.sorteazaStudenti();

        System.out.println(grup.raporteazaDetaliat());

        System.out.println(grup.raporteazaSincronizat());

        Student2 cautat = grup.cautaStudentDupaNume("Popescu Ion");
        if (cautat != null) {
            System.out.println("Studentul găsit: " + cautat);
        } else {
            System.out.println("Studentul nu a fost găsit.");
        }
    }
}
