class Logger {
    private String message;
    private static Logger instance;

    private Logger() {
        instance = this;
        message = "";
    }

    public static Logger getLogger() {
        if (instance == null)
            return new Logger();
        return instance;
    }

    public static void logInfo(String text){
        var l=getLogger();
        l.message+="[INFO]: "+text+'\n';
    }
    public static void logWarning(String text){
        var l=getLogger();
        l.message+="[WARN]: "+text+'\n';
    }
    public static void logError(String text){
        var l=getLogger();
        l.message+="[ERRO]: "+text+'\n';
    }

    public static String getLog(){
        var l=getLogger();
        return l.message;
    }

    public void logln(String text) {
        message += text + '\n';
    }

    public void log(String text) {
        message += text;
    }

    public String getMessage() {
        return message;
    }


}


public class pb4 {
    public static void main(String[] args) {
        var original=Logger.getLogger();
        var secundar=Logger.getLogger();
        original.logln("original");
        secundar.logln("secundar");
        Logger.logInfo("Static");
        Logger.logWarning("Static");
        Logger.logError("static");
        System.out.println(original==secundar);
        System.out.println(original.getMessage());
        System.out.println(secundar.getMessage());
        System.out.println(Logger.getLog());

    }
}
