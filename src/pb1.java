import java.lang.System;


public class pb1 {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Give 3 arguments int int float");
            System.exit(-1);
        }
        Integer integer1 = null, integer2 = null;
        Float floatc = null;
        try {
            integer1 = Integer.parseInt(args[0]);//Integer
        } catch (NumberFormatException e) {
            System.out.println("Argument number 1 `" + args[0] + "` is not an int");
            System.exit(-1);
        }
        try {
            integer2 = Integer.parseInt(args[1]);//Integer
        } catch (NumberFormatException e) {
            System.out.println("Argument number 2 `" + args[1] + "` is not an int");
            System.exit(-1);
        }
        try {
            floatc = Float.parseFloat(args[2]);//Integer
        } catch (NumberFormatException e) {
            System.out.println("Argument number 3 `" + args[2] + "` is not a float");
            System.exit(-1);
        }

        int int1 = integer1/*conversie implicita*/, int2 = integer2.intValue()/*conversie explicita*/;
        float floatp=floatc;
        System.out.println("Suma: "+(int1+int2+floatp)+", Media: "+((int1+int2+floatp)/3)+", Produsul: "+(int1*int2*floatp));
    }
}