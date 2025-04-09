class Profesor{
    private final int codIdentificare;
    private String nume;
    private String specializere;
    Profesor(int codIdentificare,String nume, String specializere) {
        this.codIdentificare = codIdentificare;
        this.nume=nume;
        this.specializere=specializere;
    }

    public int getCodIdentificare() {
        return codIdentificare;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getSpecializere() {
        return specializere;
    }

    public void setSpecializere(String specializere) {
        this.specializere = specializere;
    }
}

class Student{
    private final String nume;
    private int varsta;
    Student(String nume,int varsta){
        this.nume=nume;
        this.varsta=varsta;
    }

    public String getNume() {
        return nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }
}


class Curs{
    private Profesor profesor;
    private Student[] studenti;
    Curs(Profesor profesor,Student[] studenti){
        this.profesor=profesor;
        this.studenti=studenti;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public Student[] getStudenti() {
        return studenti;
    }

    public void setStudenti(Student[] studenti) {
        this.studenti = studenti;
    }
}


public class pb6 {
    public static void main(String[] args){
        var curs=new Curs(new Profesor(0,"Popescu Ion","Algbera"),new Student[]{new Student("Iliescu Ion",20),new Student("Boian Flavius",45)});
        System.out.println(curs.getProfesor().getCodIdentificare());
        System.out.println(curs.getProfesor().getNume());
        curs.getProfesor().setNume("Ionescu Ion");
        System.out.println(curs.getProfesor().getNume());

        for(Student i:curs.getStudenti()){
            System.out.println(i.getNume());
        }
    }
}
