class Carte {
    private String titlu, autor;
    private int anAparitie;

    public Carte(String titlu, String autor, int anAparitie) {
        this.titlu = titlu;
        this.autor = autor;
        this.anAparitie = anAparitie;
    }

    public Carte(Carte c) {// constructor de clonare
        titlu = c.titlu;// nu este necesar sa fac deep copy la String deoarece este imutabil
        autor = c.autor;
        anAparitie = c.anAparitie;
    }

    public String afiseazaDetalii() {
        return titlu + " " + autor + " " + anAparitie;
    }

    public String afiseazaDetalii(String s) {
        return titlu + " " + autor;
    }

    public String toString() {
        return "Carte{titlu='" + titlu + "',autor='" + autor + "',anAparitie='" + anAparitie + "'}";
    }

    public void exampleModifiy(String ext) {
        titlu = titlu + ext;
    }

    public Carte[] clone(Carte c) { // Adăugați o metodă de clonare care să furnizeze atât o copie superficială, cât și una profundă a obiectului Carte.Adăugați o metodă de clonare care să furnizeze atât o copie superficială, cât și una profundă a obiectului Carte.
        return new Carte[]{c, new Carte(c)};
    }
}

public class pb5 {
    public static void main(String[] args) {
        var carte = new Carte("Frații Karamazov", "Fiodor Dostoievski", 1788);
        System.out.println(carte);
        System.out.println(carte.afiseazaDetalii());
        System.out.println(carte.afiseazaDetalii(""));
        //shallow copy
        var deepcopy = new Carte(carte);// copie deep
        var shallow_copy = carte;// creez o copie shallow
        System.out.println("DEEP: " + deepcopy);
        System.out.println("SHALLOW: " + shallow_copy);

        carte.exampleModifiy(" vol 1");//modific originalul

        System.out.println("DEEP: " + deepcopy); //deep nu a fost modificat
        System.out.println("SHALLOW: " + shallow_copy); //iar shallow a fost
        /*
            O sa vorbesc in termeni de comparatie de c++
            1. referinta:in java se poate copia referinta catre obiect, fiecare variabila nonprimitiva(int,float,boolean...) fiind doar un pointer catre un obiect din heap(fara a ne gandii la optimizarile jit-ului privind durata de viata a alocarii)
            2. instanta noua:putem sa copiem obiectul creand o copie memoriei obiectuli si pastrand referintele originale.
                In acest caz daca avem o clasa compusa dintr-un ArrayList si un int, daca modificam intul sau referinta catre ArrayList, modificarile nu sunt transmise mai departe, insa daca adaugam un element in ArrayList toate copiile de acest tip vor avea elementul.
                Metoda mea de copiere foloseste aceasta metoda deoarece String este imutabil astfel orice modificare a Stringului este defapt o modificare de referinta
            3. clonare: clonam fiecare obiect atribut, fiecare atribut facand si el o clonare la toate atributele nonprimitive (exista optimizari pentru obiectele imutabile)
        */
    }
}
