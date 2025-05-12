import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Comanda implements Serializable {
    int id;
    String numeClient;
    double valoare;
    boolean finalizata;

    public String getNumeClient() {
        return numeClient;
    }

    public double getValoare() {
        return valoare;
    }

    public void setValoare(double valoare) {
        this.valoare = valoare;
    }

    public boolean isFinalizata() {
        return finalizata;
    }

    public void setFinalizata(boolean finalizata) {
        this.finalizata = finalizata;
    }

    public Comanda(boolean finalizata, double valoare, String numeClient, int id) {
        this.finalizata = finalizata;
        this.valoare = valoare;
        this.numeClient = numeClient;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", numeClient='" + numeClient + '\'' +
                ", valoare=" + valoare +
                ", finalizata=" + finalizata +
                '}';
    }
}

public class tema3_2 {
    public static void main(String[] args) {
        var comenzi = new ArrayList<Comanda>();
        comenzi.add(new Comanda(false, 80000,"Jhon Ion Banu", 0));//Banu, are bani
        comenzi.add(new Comanda(false, 4977, "Klaus Werner Iohanis", 1));//Presedinti
        comenzi.add(new Comanda(false, 4656, "Traian Basescu", 2));
        comenzi.add(new Comanda(false, 4682, "Emil Constantinescu", 3));
        comenzi.add(new Comanda(false, 4032, "Ion Iliescu", 4));
        comenzi.add(new Comanda(false, 3282, "Nicolae Vacaroiu", 5));//interimari
        comenzi.add(new Comanda(false, 4204, "Crin Antonescu", 6));
        comenzi.add(new Comanda(false, 3214, "Ilie Bolojan", 7));
        comenzi.add(new Comanda(false, 5001, "Nicolae Ceausescu", 8));// Tehnic primul presedinte
        comenzi.add(new Comanda(false, 3123, "Ion Ionescu", 9));
        comenzi.add(new Comanda(false, 4698, "Ion Popescu", 10));
        comenzi.add(new Comanda(false, 3920, "Andrei Popescu", 11));
        comenzi.add(new Comanda(false, 3290, "Andrei Ionescu", 12));
        comenzi.add(new Comanda(false, 5001, "Gigel Popescu", 13));
        comenzi.add(new Comanda(false, 8075, "Jhon Ion Banu", 14));

        try (var out = new ObjectOutputStream(new FileOutputStream("comenzi.dat"))) {
            for (var p : comenzi) {
                out.writeObject(p);
            }
        } catch (IOException ex) {
            System.err.println("Eroare IOException : " + ex.getMessage());
        } catch (SecurityException ex) {
            System.err.println("Eroare SecurityException : " + ex.getMessage());
        }
        try (var w = new RandomAccessFile("comenzi.dat", "rw")) {
            var r = new RandomAccessFile("comenzi.dat", "r");
            var or = new ObjectInputStream(new FileInputStream(r.getFD()));
            var ir = new ObjectOutputStream(new FileOutputStream(w.getFD()));
            while (true) {
                try {
                    long fp = r.getFilePointer();
                    var comanda = (Comanda) or.readObject();
                    if (comanda.getValoare() >= 5000) {
                        comanda.setFinalizata(true);
                        w.seek(fp);
                        ir.writeObject(comanda);
                    }
                } catch (EOFException err) {
                    break;
                } catch (IOException | ClassNotFoundException err) {
                    err.printStackTrace();
                    break;
                }

            }
        } catch (SecurityException | FileNotFoundException err) {
            err.printStackTrace();
        } catch (IOException err) {
            err.printStackTrace();
        }


        List<Comanda> out = new ArrayList<Comanda>();
        try (var in = new ObjectInputStream(new FileInputStream("comenzi.dat"))) {
            while (true) {
                try {
                    out.add((Comanda) in.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException err) {
            err.printStackTrace();
        }
        out.stream().filter(Comanda::isFinalizata).forEach(System.out::println);
        Map<String, List<Comanda>> res = out.stream().filter(Comanda::isFinalizata).collect(Collectors.groupingBy(Comanda::getNumeClient));
        System.out.println(res);
    }
}
