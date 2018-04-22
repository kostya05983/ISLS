package SQL.Parser;

import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.Dbf.DataDbf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Where {
    private static String operators = "&|!?";
    private static String delimiters = "()" + operators;
    private static boolean flag = true;

    public Where() {
    }

    private static String repl(String token) {
        token = token.replaceAll("(\\s|[)])AND([(]|\\s)", "&");
        token = token.replaceAll("(\\s|[)])OR([(]|\\s)", "|");
        token = token.replaceAll("(\\s|[)])XOR([(]|\\s)", "?");
        token = token.replaceAll("(\\s|[(]|&|[?]|[|])NOT([(]|\\s)", "!");
        if (token.substring(0, 4).equals("NOT ")) {
            token = token.replaceFirst("NOT ", "!");
        }
        return token;
    }

    private static int priority(String token) {
        if (token.equals("(")) return 0;
        if (token.equals("!")) return 1;
        if (token.equals("&")) return 2;
        if (token.equals("|") || token.equals("?")) return 3;
        return 4;
    }

    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isOperator(String token) {
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    private static List<String> parse(String infix) {
        infix = repl(infix);
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                System.out.println("Некорректное выражение.");
                flag = false;
                return postfix;
            }
            if (curr.equals(" ")) {
            }
            else if (isDelimiter(curr)) {
                switch (curr) {
                    case "(":
                        stack.push(curr);
                        break;
                    case ")":
                        while (!stack.peek().equals("(")) {
                            postfix.add(stack.pop());
                            if (stack.isEmpty()) {
                                System.out.println("Скобки не согласованы.");
                                flag = false;
                                return postfix;
                            }
                        }
                        stack.pop();
                        break;
                    default:
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }
                        stack.push(curr);
                        break;
                }
            } else {
                postfix.add(curr);
            }
        }
        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) postfix.add(stack.pop());
            else {
                System.out.println("Скобки не согласованы.");
                flag = false;
                return postfix;
            }
        }
        return postfix;
    }

    private  ArrayList<Integer> calc(List<String> postfix, DataDbf dataDbf) {
        Deque<ArrayList<Integer>> stack = new ArrayDeque<>();
        for (String x : postfix) {
            switch (x) {
                case "|": {
                    ArrayList<Integer> arr1 = new ArrayList<>(stack.pop()), arr2 = new ArrayList<>(stack.pop());
                    Set<Integer> set = new HashSet<>(arr1);
                    set.addAll(arr2);
                    arr1.clear();
                    arr1.addAll(set);
                    stack.push(arr1);
                    break;
                }
                case "&": {
                    ArrayList<Integer> arr1 = new ArrayList<>(stack.pop()), arr2 = new ArrayList<>(stack.pop());
                    arr1.removeIf(h -> !arr2.contains(h));
                    stack.push(arr1);
                    break;
                }
                case "!": {
                    ArrayList<Integer> arr1 = new ArrayList<>(stack.pop()), arr2 = new ArrayList<>();
                        for(Integer i=0;i<dataDbf.recordsDbf.size();i++){
                            if(!arr1.contains(i)) arr2.add(i);
                        }
                        stack.push(arr2);
                    }
                    break;

                case "?": {
                    ArrayList<Integer> arr1 = new ArrayList<>(stack.pop()), arr2 = new ArrayList<>(stack.pop());
                    for (int i : arr1
                            ) {
                        if (arr2.contains(i)) {
                            arr2.removeIf(h -> h.equals(i));
                            arr1.removeIf(h -> h.equals(i));
                        }
                    }
                    arr1.addAll(arr2);
                    stack.push(arr1);
                    break;
                }
                default:
                    stack.push(searchRecords(x, dataDbf));
                    break;
            }
        }
        return stack.pop();
    }

    private  ArrayList<Integer> searchRecords(String str, DataDbf dataDbf) {
        ArrayList<Integer> records = new ArrayList<>();
        if (str.contains("<>")) {
            records = search(str, "<>", dataDbf);
        } else if (str.contains("<=")) {
            records = search(str, "<=", dataDbf);
        } else if (str.contains(">=")) {
            records = search(str, ">=", dataDbf);
        } else if (str.contains("=")) {
            records = search(str, "=", dataDbf);
        }
        return records;
    }

    private ArrayList<Integer> search(String condition, String operator, DataDbf dbf) {
        //Возвращает список индексов подходящих под условие записей
        ArrayList<Integer> result = new ArrayList<>();
        Column[] columns = dbf.getAllColumns();
        String columnName;
        String title;
        columnName = condition.substring(0, condition.indexOf(operator)).trim();
        System.out.println(columnName);
        for (Column column : columns) {
            title = column.title.trim();
            if (title.equals(columnName)) {
                switch (column.type) {
                    case Character:
                        if (reg(condition.substring(condition.indexOf(operator) + operator.length()).trim())) {
                            for (String record : column.data) {
                                if (checkChar(condition, record, operator))
                                        result.add(Arrays.asList(column.data).indexOf(record));
                            }
                        }
                        break;
                    case Integer:
                        for (String record : column.data) {
                            if (checkInt(condition, record, operator))
                                    result.add(Arrays.asList(column.data).indexOf(record));
                        }
                        break;
                    case Float:
                        for (String record : column.data) {
                            if (checkFloat(condition, record, operator))
                                    result.add(Arrays.asList(column.data).indexOf(record));
                            }
                        break;
                        }

                }
            }
        return result;
    }

    private static boolean checkChar(String condition, String cellData, String operator) {
        boolean b = false;
        switch (check(operator)) {
            case 0:
                b = condition.substring(condition.indexOf("\"") + 1, condition.lastIndexOf("\"")).equals(cellData);
                break;
            case 1:
                b = !(condition.substring(condition.indexOf("\"") + 1, condition.lastIndexOf("\"")).equals(cellData));
                break;
            case 2:
                b = condition.substring(condition.indexOf("\"") + 1, condition.lastIndexOf("\"")).compareTo(cellData) >= 0;
                break;
            case 3:
                b = condition.substring(condition.indexOf("\"") + 1, condition.lastIndexOf("\"")).compareTo(cellData) <= 0;
                break;
        }
        return b;
    }

    private static boolean checkInt(String condition, String cellData, String operator) {
        boolean b = false;
        BigInteger cond = new BigInteger(condition.substring(condition.indexOf(operator) + operator.length()).trim());
        BigInteger cell = new BigInteger(cellData);
        switch (check(operator)) {
            case 0:
                b = cond.compareTo(cell) == 0;
                break;
            case 1:
                b = cond.compareTo(cell) != 0;
                break;
            case 2:
                b = cond.compareTo(cell) != -1;
                break;
            case 3:
                b = cond.compareTo(cell) != 1;
                break;
        }
        return b;
    }

    private static int check(String operator) {
        if (operator.equals("=")) return 0;
        if (operator.equals("<>")) return 1;
        if (operator.equals("<=")) return 2;
        if (operator.equals(">=")) return 3;
        return 4;
    }

    private static boolean checkFloat(String condition, String cellData, String operator) {
        boolean b = false;
        BigDecimal cond = new BigDecimal(condition.substring(condition.indexOf(operator) + operator.length()).trim().replace(",", "."));
        BigDecimal cell = new BigDecimal(cellData);
        switch (check(operator)) {
            case 0:
                b = cond.compareTo(cell) == 0;
                break;
            case 1:
                b = cond.compareTo(cell) != 0;
                break;
            case 2:
                b = cond.compareTo(cell) != -1;
                break;
            case 3:
                b = cond.compareTo(cell) != 1;
                break;
        }
        return b;
    }

    private static boolean reg(String s) {
        Pattern p = Pattern.compile("^\".+\"$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public ArrayList<Integer> getRecs(String request, DataDbf dataDbf) {
        List<String> expression = parse(request);
        if (flag) {
            ArrayList<Integer> result=calc(expression,dataDbf);
            return result;
        } else {
            return null;
        }
    }



}
