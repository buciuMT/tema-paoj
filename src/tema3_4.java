import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

import static java.util.stream.Collectors.*;


class Persoana {
    private String nume;
    private int varsta;
    private String oras;

    public Persoana(String nume, int varsta, String oras) {
        this.nume = nume;
        this.varsta = varsta;
        this.oras = oras;
    }

    public String getNume() {
        return nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public String getOras() {
        return oras;
    }

    @Override
    public String toString() {
        return nume + " (" + varsta + ") - " + oras;
    }
}


public class tema3_4 {
    public static void main(String[] args) {
        var persoane = new ArrayList<Persoana>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("date.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String nume = parts[0];
                    int varsta = Integer.parseInt(parts[1]);
                    String oras = parts[2];
                    persoane.add(new Persoana(nume, varsta, oras));
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare citirea: " + e.getMessage());
            return;
        }
        //Filtrare: persoane peste 30 ani care locuiesc în orașe care încep cu litera "B".
        List<Persoana> filtrate = persoane.stream().filter(p -> p.getVarsta() > 30 && p.getOras().startsWith("B")).toList();
        filtrate.forEach(System.out::println);

        Map<String, List<Persoana>> peOrase = persoane.stream().collect(groupingBy(Persoana::getOras));
        System.out.println(peOrase);

        // Agregare: media de vârstă per oraș
        Map<String, Double> medieVarsta = persoane.stream().collect(groupingBy(Persoana::getOras, averagingInt(Persoana::getVarsta)));
        System.out.println(medieVarsta);
        //Sortare: persoanele sortate după nume și apoi după vârstă
        List<Persoana> persoaneSortate = persoane.stream().sorted((l, r) -> {
            var res = l.getNume().compareTo(r.getNume());
            if (res == 0) {
                return l.getVarsta() - r.getVarsta();
            }
            return res;
        }).toList();
        // Reducere: găsirea persoanei cu vârsta maximă
        Optional<Persoana> maxVarsta = persoane.stream().reduce((p1, p2) -> p1.getVarsta() > p2.getVarsta() ? p1 : p2);
        System.out.println(maxVarsta);

        // 6. Scriere în fișier rezultat.txt
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("rezultat.txt"))) {
            for (Persoana p : filtrate) {
                writer.write(p + "\n");
            }

            for (Persoana p : persoaneSortate) {
                writer.write(p + "\n");
            }
            for (Map.Entry<String, List<Persoana>> entry : peOrase.entrySet()) {
                writer.write(entry.getKey() + ":\n");
                for (Persoana p : entry.getValue()) {
                    writer.write("  " + p + "\n");
                }
            }
            for (Map.Entry<String, Double> entry : medieVarsta.entrySet()) {
                writer.write(entry.getKey() + " - " + String.format("%.2f", entry.getValue()) + "\n");
            }

        } catch (IOException e) {
            System.err.println("Eroare la scrierea în fișier: " + e.getMessage());
        }
    }
}

