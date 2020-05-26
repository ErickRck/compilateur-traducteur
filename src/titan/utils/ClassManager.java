package titan.utils;

import titan.constants.ClassKind;

public class ClassManager {

    private final static String CLASS_DEFAULT_FORMAT = "public class %s{\n\n}";
    private final static String INTERFACE_DEFAULT_FORMAT = "public interface %s{\n\n}";
    private final static String ENUM_DEFAULT_FORMAT = "public enum %s{\n\n}";
    private final static String ANNOTATION_DEFAULT_FORMAT = "public @interface %s{\n\n}";

    private final static String FONCTION_DEFAULT_FORMAT_ALGORITHME = "algorithme %s;\n\ndebut \n\nfin.";
    private final static String FONCTION_DEFAULT_FORMAT_PASCAL = "program %s;\n\nbegin \n\nend.";

    public static ClassKind getClassKind(String classType) {
        classType = classType.toLowerCase();
        switch (classType) {
            case "class":
                return ClassKind.CLASS;
            case "interface":
                return ClassKind.INTERFACE;
            case "enum":
                return ClassKind.ENUM;
            case "annotation":
                return ClassKind.ANNOTATION;
            case "pascal":
                return ClassKind.PASCAL;
            case "algorithme":
                return ClassKind.ALGORITHME;
            default:
                return ClassKind.CLASS;
        }
    }

    public static String getDefaultValueText(String name, ClassKind kind) {
        switch (kind) {
            case CLASS:
                return String.format(CLASS_DEFAULT_FORMAT, name);
            case INTERFACE:
                return String.format(INTERFACE_DEFAULT_FORMAT, name);
            case ENUM:
                return String.format(ENUM_DEFAULT_FORMAT, name);
            case ANNOTATION:
                return String.format(ANNOTATION_DEFAULT_FORMAT, name);
            case PASCAL:
                return String.format(FONCTION_DEFAULT_FORMAT_PASCAL, name);
            case ALGORITHME:
                return String.format(FONCTION_DEFAULT_FORMAT_ALGORITHME, name);
        }
        return "";
    }
}
