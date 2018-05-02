package SQL.Parser;

import GUI.Main;


import javax.swing.text.html.parser.Parser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectorRequest implements Runnable {

    private Main main;
    private Matcher check;
    private Pattern Create_P = Pattern.compile("CREATE \\s*TABLE \\s*\\w* \\s*");//готов
    private Pattern Insert_P_With_Params = Pattern.compile("(INSERT \\s*INTO \\s*\\w*\\s* \\([\\w,\\d]+\\)\\s)(VALUES \\s*\\([\\w,\\d]+\\))|(INSERT \\s*INTO \\s*\\w*\\s*)(VALUES \\s*\\([\\w,\\d]+\\))");//готов
    private Pattern Insert_P = Pattern.compile("INSERT \\s*INTO \\s*\\w*\\s*VALUES \\s*\\([\\w,\\d]+\\)");
    private Pattern Update_P = Pattern.compile("\\s*UPDATE\\s+((\\w)+)\\s+SET\\s+((((\\w+)=[\\w\"]+)(,|\\s*))+)\\s+WHERE\\s+([\\w.<>=\\s,\"]+)\\s*;\\s*");//готов
    private Pattern Delete_P = Pattern.compile("\\s*DELETE\\s+FROM\\s+((\\w+)|\\*)\\s+WHERE\\s([\\w.<>=\\s\",]+)\\s*;\\s*");//готов
    private Pattern Select_P = Pattern.compile("\\s*SELECT\\s+(((\\w+)|\\*)\\s*(,|\\s*)\\s*)+\\s+FROM\\s+((\\w+)(\\s+WHERE\\s+([\\w.<>=\\s,\"]+)|\\s*))\\s*;\\s*");//готов
    private Pattern Drop_P = Pattern.compile("\\s*DROP\\s+TABLE\\s+(\\w+)\\s*;\\s*");//готов
    private Pattern Truncate_P = Pattern.compile("\\s*TRUNCATE\\s+TABLE\\s+(\\w+)\\s*;\\s*");//готов
    private Pattern CreateIn_P = Pattern.compile("\\s*CREATE\\s+INDEX\\s+(\\w+)\\s+ON\\s+(\\w+)\\s*\\((((\\w+)(,|\\s*))+)\\)\\s*;\\s*");
    private Pattern DropIn_P = Pattern.compile("\\s*DROP\\s+INDEX\\s+((\\w+).(\\w+))\\s*;\\s*");
    private Pattern Alter_P = Pattern.compile("\\s*ALTER\\s+TABLE\\s+(\\w+)\\s+((((ADD|MODIFY\\s+COLUMN)\\s+(\\w+))\\s+(((CHARACTER|INTEGER)\\s*\\(\\d+\\))|(FLOAT\\((\\d)+.(\\d)+\\))))|((DROP\\s+COLUMN)\\s+(\\w+)))\\s*;\\s*");
    private String myStringFirst;
    private HandlerRequest handlerRequest;

    public SelectorRequest(String mstring, Main main) {
        this.main = main;
        myStringFirst = mstring;
        handlerRequest = new HandlerRequest(main);
    }

    private void checkCom() {
        try {
            String mystring = myStringFirst.replaceAll("\n", " ");
            String[] strings_command = mystring.split("\\s*(u|U)(n|N)(i|I)(o|O)(n|N)\\s*");
            mystring = mystring.toUpperCase();
            String[] strings_command_upper = mystring.split("\\s*UNION\\s*");

            for (int i = 0; i < strings_command.length; i++) {//в методы отправлять строки strings_command[i]{

                check = Create_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateCreateTable(strings_command[i]);
                    continue;
                }

                check = Insert_P_With_Params.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateInsertInto(strings_command[i]);
                    continue;
                }

                check = Update_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateUpdate(strings_command[i]);
                    continue;
                }

                check = Delete_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateDelete(strings_command[i]);
                    continue;
                }

                check = Select_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateSelect(strings_command[i]);
                    continue;
                }

                check = Drop_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateDropTable(strings_command[i]);
                    continue;
                }

                check = Truncate_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateTruncate(strings_command[i]);
                    continue;
                }

                check = CreateIn_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateCreateIndex(strings_command[i]);
                    continue;
                }

                check = DropIn_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateDropIndex(strings_command[i]);
                    continue;
                }

                check = Alter_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateAlterTable(strings_command[i]);
                    continue;
                }

                throw new ParserException("Комманда не распознана");
            }
        } catch (ParserException e) {
            main.error(e.getMessage());
        }
    }

    private void validateInsertInto(String command) throws ParserException {
        if (!(check = Insert_P.matcher(command.toUpperCase())).find())
            checkAmount(command);

        checkEnd(command);

        handlerRequest.insertInto(command);

        check.reset();

    }

    private void checkEnd(String command) throws ParserException {
        if (command.length() - 1 != command.indexOf(";"))
            throw new ParserException("Комманда должна заканчиваться точкой с запятой");
    }

    private void checkAmount(String command) throws ParserException {
        String[] firstPart = command.substring(command.indexOf("(") + 1, command.indexOf(")")).split("[,]");
        command = command.substring(command.indexOf(")") + 1);
        String[] secondPart = command.substring(command.indexOf("(") + 1, command.indexOf(")")).split("[,]");
        if (firstPart.length != secondPart.length)
            throw new ParserException("Колличество имен в названйи столбцов и в введенных значениях не совпадает");
    }

    //region CreateTable
    private void validateCreateTable(String command) throws ParserException {
        checkEnd(command);
        checkColumns(command);
        handlerRequest.createTable(command);
    }

    private void checkColumns(String command) throws ParserException {
        command = command.substring(command.indexOf("(") + 1).toUpperCase().trim();
        var end="\\s*\\)\\s*;\\s*";
        command=command.replaceAll(end,");");

        while (true) {
            //Проверка на допустимый диапозон
            if (command.substring(0, command.indexOf(" ")).length() <= 10) {
                command = command.substring(command.indexOf(" "));

                //Проверяем на концовку
                if (!command.substring(command.indexOf(")")+1).equals(");")) {
                    if(command.substring(command.indexOf(")")).indexOf(",")>0) {
                        command = checkType(command);
                        command = command.substring(command.indexOf(",") + 1).trim();
                    }else
                        throw new ParserException("Не хватает запятых");
                } else {
                    checkType(command);
                    break;
                }
            } else
                throw new ParserException("Название столбца не должно превышать 10 символов");
        }
    }

    private String checkType(String command) throws ParserException {
        var integerCharacterRegex = "\\(\\s*\\d*\\s*\\)";
        var floatRegex = "\\(\\s*\\d*\\s*.\\s*\\d*\\s*\\)";

        //проверка на соответствии типу
        if (command.substring(0, command.indexOf("(")).trim().matches("INTEGER|CHARACTER|FLOAT")) {

            if (command.substring(0, command.indexOf("(")).trim().matches("INTEGER|CHARACTER")) {
                command = command.substring(command.indexOf("(")).trim();

                //Проверка на формат скобок
                if (command.substring(command.indexOf("("), command.indexOf(")") + 1).matches(integerCharacterRegex)) {

                    //Проверка на допустимый диапозон
                    if (Short.parseShort(command.substring(command.indexOf("(") + 1, command.indexOf(")")).replaceAll("[ ]", "")) < 255){
                        command = command.substring(command.indexOf(")") + 1).trim();
                        return command;
                    }

                    else
                        throw new ParserException("Значение в скобках превышает допустимый диапозон 255");
                } else
                    throw new ParserException("Значения в скобках не соответствуют типу");
            }

            if (command.substring(0, command.indexOf("(")).trim().matches("FLOAT")) {
                command = command.substring(command.indexOf("(")).trim();

                //Проверка на формат скобок
                if (command.substring(command.indexOf("("), command.indexOf(")") + 1).matches(floatRegex)) {

                    //Проверка на допустиммый диапозон
                    if (Short.parseShort(command.substring(command.indexOf("(") + 1, command.indexOf(",")).replaceAll("[ ]", ""))+
                            Short.parseShort(command.substring(command.indexOf(",") + 1, command.indexOf(")")).replaceAll("[ ]", "")) < 255) {
                        command = command.substring(command.indexOf(")")+1);
                        return command;
                    }
                    else
                        throw new ParserException("Значения в скобках превышают допустимый диапозон");
                } else {
                    throw new ParserException("Значения в скобках не соответствуют типу");
                }
            }
        } else
            throw new ParserException("Не соответствуют типы, или не все запятые на своем месте");

        return command;
    }
    //endregion

    private void validateUpdate(String command) {
        handlerRequest.update(command);
    }

    private void validateSelect(String command) {
        handlerRequest.select(command);
    }

    private void validateDelete(String command) {
        handlerRequest.delete(command);
    }

    private void validateDropTable(String command) {
        handlerRequest.dropTable(command);
    }

    private void validateCreateIndex(String command) throws ParserException {
        handlerRequest.createIndex(command);
    }

    private void validateDropIndex(String command) {
        handlerRequest.dropIndex(command);
    }

    private void validateAlterTable(String command) {
        handlerRequest.alterTable(command);
    }

    private void validateTruncate(String command) {
        handlerRequest.truncate(command);
    }

    public void run() {
        try {
            checkCom();
        } catch (Exception e) {
            e.printStackTrace();
            main.error("что-то пошло не так... Проверьте синтаксис команды");
        }
    }


}