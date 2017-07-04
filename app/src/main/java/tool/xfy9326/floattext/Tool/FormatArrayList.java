package tool.xfy9326.floattext.Tool;

import java.util.ArrayList;

//ArrayList与String转换

public class FormatArrayList {
    public static ArrayList<String> StringToStringArrayList(String str) {
        ArrayList<String> arr = new ArrayList<>();
        if (str.contains("[") && str.length() >= 3) {
            str = str.substring(1, str.length() - 1);
            if (str.contains(",")) {
                String[] temp = str.split(",");
                for (int i = 0; i < temp.length; i++) {
                    if (i != 0) {
                        temp[i] = temp[i].substring(1, temp[i].length());
                    }
                    arr.add(temp[i]);
                }
            } else {
                arr.add(str);
            }
        }
        return arr;
    }

    public static ArrayList<Float> StringToFloatArrayList(String str) {
        ArrayList<Float> arr = new ArrayList<>();
        if (str.contains("[") && str.length() >= 3) {
            str = str.substring(1, str.length() - 1);
            if (str.contains(",")) {
                String[] temp = str.split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].replaceAll("\\s+", "");
                    arr.add(Float.parseFloat(temp[i]));
                }
            } else {
                arr.add(Float.parseFloat(str));
            }
        }
        return arr;
    }

    public static ArrayList<Integer> StringToIntegerArrayList(String str) {
        ArrayList<Integer> arr = new ArrayList<>();
        if (str.contains("[") && str.length() >= 3) {
            str = str.substring(1, str.length() - 1);
            if (str.contains(",")) {
                String[] temp = str.split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].replaceAll("\\s+", "");
                    arr.add(Integer.parseInt(temp[i]));
                }
            } else {
                arr.add(Integer.parseInt(str));
            }
        }
        return arr;
    }

    public static ArrayList<Boolean> StringToBooleanArrayList(String str) {
        ArrayList<Boolean> arr = new ArrayList<>();
        if (str.contains("[") && str.length() >= 3) {
            str = str.substring(1, str.length() - 1);
            if (str.contains(",")) {
                String[] temp = str.split(",");
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = temp[i].replaceAll("\\s+", "");
                    arr.add(Boolean.parseBoolean(temp[i]));
                }
            } else {
                arr.add(Boolean.parseBoolean(str));
            }
        }
        return arr;
    }
}
