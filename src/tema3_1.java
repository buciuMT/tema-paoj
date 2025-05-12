import java.io.*;
import java.util.*;
import java.util.function.UnaryOperator;
import java.io.Serializable;


class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}

class Produs implements Serializable {
    private String nume;
    private double pret;
    private int stoc;

    public Produs(String nume, double pret, int stoc) throws InvalidDataException {
        if (pret < 0 || stoc < 0) {
            throw new InvalidDataException("Prețul și stocul nu pot fi negative!");
        }
        this.nume = nume;
        this.pret = pret;
        this.stoc = stoc;
    }

    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    @Override
    public String toString() {
        return nume + " - Pret: " + pret + " - Stoc: " + stoc;
    }
}

public class tema3_1 {
    public static void main(String[] args) {
        var produse = new ArrayList<>();

        try {
            produse.add(new Produs("Lapte", 5.5, 10));
            produse.add(new Produs("Paine", 3.0, 0));
            produse.add(new Produs("Oua", 12.0, 30));
            produse.add(new Produs("Cafea", 20.0, 15));
            produse.add(new Produs("Zahar", 6.0, 0));
            produse.add(new Produs("Faina", 4.5, 5));
            produse.add(new Produs("Ulei", 9.5, 8));
            produse.add(new Produs("Apa", 2.0, 0));
            produse.add(new Produs("Sare", 1.5, 20));
            produse.add(new Produs("Ciocolata", 7.5, 2));
        } catch (InvalidDataException e) {
            System.err.println("Eroare la crearea produselor: " + e.getMessage());
        }
        //scriere
        try (var out = new ObjectOutputStream(new FileOutputStream("produse.dat"))) {
            for (var p : produse) {
                out.writeObject(p);
            }
        } catch (IOException e) {
            try (PrintWriter log = new PrintWriter(new FileWriter("erori.log", true))) {
                log.println("Eroare scriere: " + e.getMessage());
            } catch (IOException ex) {
                System.err.println("Eroare logare: " + ex.getMessage());
            }
        }


        //citire
        var prosduse_citite = new ArrayList<Produs>();

        try (var in = new ObjectInputStream(new FileInputStream("produse.dat"))) {
            while (true) {
                try {
                    var p = (Produs) in.readObject();
                    prosduse_citite.add(p);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("Clasa nedefinită: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare citire: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter("epuizate.txt")) {
            prosduse_citite.stream()
                    .filter(p -> p.getStoc() == 0)
                    .forEach(writer::println);
        } catch (IOException e) {
            System.err.println("Eroare epuizate.txt: " + e.getMessage());
        }

        UnaryOperator<Produs> reducer = p -> {
            int stocNou = (int) Math.floor(p.getStoc() * 0.9);
            p.setStoc(stocNou);
            return p;
        };
        prosduse_citite.replaceAll(reducer);

        prosduse_citite.stream()
                .max(Comparator.comparingDouble(Produs::getPret))
                .ifPresent(p -> System.out.println("max_pret: " + p));
    }
}
