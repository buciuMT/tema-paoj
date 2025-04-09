class Animal {
    public String sunet() {
        return "[Animal]";
    }
};

class Mamifer extends Animal {
    @Override
    public String sunet() {
        return super.sunet() + "->[Mamifer]";
    }
}

class Caine extends Mamifer {
    @Override
    public String sunet() {
        return super.sunet() + "->[Caine]: Ham";
    }

    public String ham() {
        return "Ham";
    }
}

class Pisica extends Mamifer {
    @Override
    public String sunet() {
        return super.sunet() + "->[Pisica]: Miau";
    }

    public String miau() {
        return "miau";
    }
}


public class pb7 {
    public static void main(String[] args) {
        var v = new Animal[]{new Caine(), new Pisica()};
        for (Animal i : v) {
            if (i instanceof Caine)
                System.out.println(((Caine) i).ham());
            else if (i instanceof Pisica)
                System.out.println(((Pisica) i).miau());
        }
    }
}
