import java.util.ArrayList;

abstract class Vehicul {
    protected String marca, model;
    protected int anFabricatie;

    abstract String descriere();
}

class Masina extends Vehicul {
    Masina(String marca, String model, int anFabricatie) {
        this.marca = marca;
        this.model = model;
        this.anFabricatie = anFabricatie;
    }

    String descriere() {
        return "Masina {marca:" + marca + ", model:" + model + ", an fabricatie:" + anFabricatie + "}";
    }
    String special_masina() {
        return "este o masina";
    }
}

class Motocicleta extends Vehicul {
    Motocicleta(String marca, String model, int anFabricatie) {
        this.marca = marca;
        this.model = model;
        this.anFabricatie = anFabricatie;
    }

    String descriere() {
        return "Motocicleta {marca:" + marca + ", model:" + model + ", an fabricatie:" + anFabricatie + "}";
    }
    String special_moto() {
        return "este o motocicleta";
    }
}

public class pb3 {
    public static void main(String[] args) {
        ArrayList<Vehicul> v = new ArrayList<Vehicul>();
        v.add(new Masina("Dacia", "Sandero", 2001));
        v.add(new Masina("Skoda", "Octavia", 2003));
        v.add(new Motocicleta("Kawasaki", "650 Vulcan S", 2024));
        v.add(new Masina("ZAZ", "Zaporozhets", 1981));
        //Polimorfism
        for(Vehicul i:v)
            System.out.println(i.descriere());

        for(Vehicul i:v){
            if(i instanceof Motocicleta)
                System.out.println(((Motocicleta) i).special_moto());
            else if(i instanceof Masina)
                System.out.println(((Masina) i).special_masina());
            else
                System.out.println("Unreachable");
        }

    }
}
