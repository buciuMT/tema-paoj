import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.*;

class Client {
    String nume;
    int varsta;
    double sumaCont;
    Optional<String> tipClient;

    public Client(String nume, int varsta, double sumaCont, Optional<String> tipClient) {
        this.nume = nume;
        this.varsta = varsta;
        this.sumaCont = sumaCont;
        this.tipClient = tipClient;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public double getSumaCont() {
        return sumaCont;
    }

    public void setSumaCont(double sumaCont) {
        this.sumaCont = sumaCont;
    }

    public Optional<String> getTipClient() {
        return tipClient;
    }

    public void setTipClient(Optional<String> tipClient) {
        this.tipClient = tipClient;
    }
}


public class tema3_3 {
    public static void main(String[] args) {
        var clienti = Arrays.asList(
                new Client("Jhon Ion Banu", 22, 1500.0, Optional.of("VIP")),
                new Client("Klaus Werner Iohanis", 35, 3000.0, Optional.of("Standard")),
                new Client("Traian Basescu", 28, 2200.0, Optional.of("VIP")),
                new Client("Emil Constantinescu", 19, 1800.0, Optional.of("Nou")),
                new Client("Ion Iliescu", 40, 5000.0, Optional.of("VIP")),
                new Client("Nicolae Vacaroiu", 30, 2500.0, Optional.empty()),
                new Client("Crin Antonescu", 24, 1300.0, Optional.of("Standard")),
                new Client("Ilie Bolojan", 26, 1000.0, Optional.of("Nou")),
                new Client("Nicolae Ceausescu", 23, 2700.0, Optional.of("VIP")),
                new Client("Andrei Ionescu", 21, 1900.0, Optional.of("Nou")),
                new Client("Ion Popescu", 45, 3500.0, Optional.of("Standard")),
                new Client("Andrei Popescu", 20, 1200.0, Optional.empty()));
        //Folosiți Predicate, Function, BiFunction, Supplier definite manual, nu doar lambdas inline
        Predicate<Client> esteVIP = c -> c.getTipClient().orElse("Necunoscut").equals("VIP");
        Function<Client, String> formatNumeVarsta = c -> c.getNume() + " - " + c.getVarsta() + " ani";
        BiFunction<Double, Client, Double> sumaTotala = (s, c) -> s + c.getSumaCont();
        Supplier<Map<String, Long>> mapSupplier = HashMap::new;

        double medie = clienti.stream()
                .mapToDouble(Client::getSumaCont)
                .average()
                .orElse(0.0);

        // Filtra clienții de tip VIP care au suma contului peste media tuturor clienților.
        List<Client> vpm = clienti.stream().filter(esteVIP).filter(c -> c.getSumaCont() > medie).toList();

        vpm.forEach(c -> System.out.println(c.getNume()));

        // Mapare: transformați toți clienții într-un format "Nume - Varsta ani".
        List<String> numeVarsta = clienti.stream().map(formatNumeVarsta).toList();
        numeVarsta.forEach(System.out::println);

        // Reducere: calculați suma totală a sumelor conturilor.
        double total = clienti.stream().reduce(0.0, sumaTotala, Double::sum);
        System.out.println(total);

        // Gruparea clienților în funcție de tipul lor (VIP, Standard, Nou) și numărarea lor.
        Map<String, Long> tip_client = clienti.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getTipClient().orElse("Necunoscut"),//Tratați lipsa tipului de client (Optional.empty()) folosind orElse("Necunoscut").
                        Collectors.counting()
                ));
        tip_client.forEach((tip, nr) -> System.out.println(tip + ": " + nr));

        //Colectați toate numele clienților sub 25 ani într-un singur String (joining(", ")).
        String sub25 = clienti.stream()
                .filter(c -> c.getVarsta() < 25)
                .map(Client::getNume)
                .collect(Collectors.joining(", "));

        System.out.println("\nClienți sub 25 de ani: " + sub25);

    }
}
