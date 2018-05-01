package SQL.Parser;
import GUI.Main;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectorRequest implements Runnable{

    private Main main;
    private Matcher check;
    private Pattern Create_P =  Pattern.compile("\\s*CREATE\\s+TABLE\\s+(\\w+)\\s*\\((\\s*(\\w+)\\s+((((CHARACTER|INTEGER)\\s*\\(\\d+\\))|(FLOAT\\((\\d)+.(\\d)+\\)))\\s*)\\s*(,|\\s*))+\\)\\s*;\\s*");//готов
    private Pattern Insert_P = Pattern.compile("((INSERT INTO [a-zA-Z\\d\\$\\%\\^\\&\\*]+ )|(VALUES ))|(\\([\\w ,\\d]+\\))");//готов
    private Pattern Insert_P_QuntatyParams = Pattern.compile("([\\d\\w,\\)]+)");
    private Pattern Update_P = Pattern.compile("\\s*UPDATE\\s+((\\w)+)\\s+SET\\s+((((\\w+)=[\\w\"]+)(,|\\s*))+)\\s+WHERE\\s+([\\w.<>=\\s,\"]+)\\s*;\\s*");//готов
    private Pattern Delet_P = Pattern.compile("\\s*DELETE\\s+FROM\\s+((\\w+)|\\*)\\s+WHERE\\s([\\w.<>=\\s\",]+)\\s*;\\s*");//готов
    private Pattern Select_P = Pattern.compile("\\s*SELECT\\s+(((\\w+)|\\*)\\s*(,|\\s*)\\s*)+\\s+FROM\\s+((\\w+)(\\s+WHERE\\s+([\\w.<>=\\s,\"]+)|\\s*))\\s*;\\s*");//готов
    private Pattern Drop_P = Pattern.compile("\\s*DROP\\s+TABLE\\s+(\\w+)\\s*;\\s*");//готов
    private Pattern Truncate_P = Pattern.compile("\\s*TRUNCATE\\s+TABLE\\s+(\\w+)\\s*;\\s*");//готов
    private Pattern CreateIn_P = Pattern.compile("\\s*CREATE\\s+INDEX\\s+(\\w+)\\s+ON\\s+(\\w+)\\s*\\((((\\w+)(,|\\s*))+)\\)\\s*;\\s*");
    private Pattern DropIn_P = Pattern.compile("\\s*DROP\\s+INDEX\\s+((\\w+).(\\w+))\\s*;\\s*");
    private Pattern Alter_P = Pattern.compile("\\s*ALTER\\s+TABLE\\s+(\\w+)\\s+((((ADD|MODIFY\\s+COLUMN)\\s+(\\w+))\\s+(((CHARACTER|INTEGER)\\s*\\(\\d+\\))|(FLOAT\\((\\d)+.(\\d)+\\))))|((DROP\\s+COLUMN)\\s+(\\w+)))\\s*;\\s*");
    private String myStringFirst;
    private HandlerRequest handlerRequest;

    public SelectorRequest(String mstring,Main main) {
        this.main=main;
        myStringFirst=mstring;
        handlerRequest=new HandlerRequest(main);
    }

    private void checkCom()  {
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

                check = Insert_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateInsertInto(strings_command[i]);
                    continue;
                }

                check = Update_P.matcher(strings_command_upper[i]);
                if (check.find()) {
                    validateUpdate(strings_command[i]);
                    continue;
                }

                check = Delet_P.matcher(strings_command_upper[i]);
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

                    throw new ParserException("Комманда не расознана");
            }
        }catch(ParserException e){
            main.error(e.getMessage());
        }
    }

    private void validateInsertInto(String command){
        if (check.groupCount() == 4) {
            check = Insert_P_QuntatyParams.matcher(command.toUpperCase());
            handlerRequest.insertInto(command);
        } else {
            check.reset();
        }
    }

    private void validateCreateTable (String command){
        handlerRequest.createTable(command);
    }

    private void validateUpdate (String command){
        handlerRequest.update(command);
    }

    private void validateSelect (String command){
        handlerRequest.select(command);
    }

    private void validateDelete(String command){
        handlerRequest.delete(command);
    }

    private void validateDropTable(String command){
        handlerRequest.dropTable(command);
    }

    private void validateCreateIndex(String command) throws ParserException{
        handlerRequest.createIndex(command);
    }

    private void validateDropIndex(String command){
        handlerRequest.dropIndex(command);
    }

    private void validateAlterTable(String command){
        handlerRequest.alterTable(command);
    }

    private void validateTruncate(String command){
        handlerRequest.truncate(command);
    }

    public void run(){
        try {
            checkCom();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}