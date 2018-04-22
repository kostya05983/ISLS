package SQL.Parser;
import GUI.Main;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectorRequest implements Runnable{

    private Main main;
    private boolean checkcommand;
    private Matcher Check;
    private Pattern Create_P =  Pattern.compile("\\s*CREATE\\s+TABLE\\s+(\\w+)\\s*\\((\\s*(\\w+)\\s+((((CHARACTER|INTEGER)\\s*\\(\\d+\\))|(FLOAT\\((\\d)+.(\\d)+\\)))\\s*)\\s*(,|\\s*))+\\)\\s*;\\s*");//готов
    private Pattern Insert_P = Pattern.compile("\\s*INSERT\\s+INTO\\s+(\\w+)\\s*\\((\\s*(\\w+)\\s*(,|\\s*))+\\s*\\)\\s*VALUE\\s*\\((\\s*(((\"\\w+\")|\\d|((\\d)+.(\\d)+))+)\\s*(,|\\s*))+\\s*\\)\\s*;\\s*");//готов
    private Pattern Update_P = Pattern.compile("\\s*UPDATE\\s+((\\w)+)\\s+SET\\s+((((\\w+)=[\\w\"]+)(,|\\s*))+)\\s+WHERE\\s+([\\w.<>=\\s,\"]+)\\s*;\\s*");//готов
    private Pattern Delet_P = Pattern.compile("\\s*DELETE\\s+FROM\\s+((\\w+)|\\*)\\s+WHERE\\s([\\w.<>=\\s\",]+)\\s*;\\s*");//готов
    private Pattern Select_P = Pattern.compile("\\s*SELECT\\s+((\\w+)|\\*)\\s+FROM\\s+(\\w+)(\\s+WHERE\\s+([\\w.<>=\\s,\"]+)|\\s*)\\s*;\\s*");//готов
    private Pattern Drop_P = Pattern.compile("\\s*DROP\\s+TABLE\\s+(\\w+)\\s*;\\s*");//готов
    private Pattern Truncate_P = Pattern.compile("\\s*TRUNCATE\\s+TABLE\\s+(\\w+)\\s*;\\s*");//готов
    private Pattern CreateIn_P = Pattern.compile("\\s*CREATE\\s+INDEX\\s+(\\w+)\\s+ON\\s+(\\w+)\\s*\\((((\\w+)(,|\\s*))+)\\)\\s*;\\s*");
    private Pattern DropIn_P = Pattern.compile("\\s*DROP\\s+INDEX\\s+((\\w+).(\\w+))\\s*;\\s*");
    private Pattern Alter_P = Pattern.compile("\\s*ALTER\\s+TABLE\\s+(\\w+)\\s+((((ADD|MODIFY\\s+COLUMN)\\s+(\\w+))\\s+(((CHARACTER|INTEGER)\\s*\\(\\d+\\))|(FLOAT\\((\\d)+.(\\d)+\\))))|((DROP\\s+COLUMN)\\s+(\\w+)))\\s*;\\s*");
    private String mystringfirst;

    public SelectorRequest(String mstring,Main main) {
        this.main=main;
        mystringfirst=mstring;
        checkcommand=false;
    }

    private void Checkcom() throws Exception {
        String mystring = mystringfirst.replaceAll("\n"," ");
        String [] strings_command = mystring.split("\\s*(u|U)(n|N)(i|I)(o|O)(n|N)\\s*");
        mystring = mystring.toUpperCase();
        String [] strings_command_upper = mystring.split("\\s*UNION\\s*");
        HandlerRequest handlerRequest=new HandlerRequest(main);

        for (int i=0; i<strings_command.length;i++){//в методы отправлять строки strings_command[i]{
            Check = Create_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.createTable(strings_command[i]);
            }
            Check = Insert_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.insertInto(strings_command[i]);
            }
            Check = Update_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.update(strings_command[i]);
            }
            Check = Delet_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                //handlerRequest.delete(strings_command[i]);
            }
            Check = Select_P.matcher(strings_command_upper[i]);
            if (Check.find()||true) {
                checkcommand=true;
                handlerRequest.select(strings_command[i]);
            }
            Check = Drop_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.dropTable(strings_command[i]);
            }
            Check = Truncate_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.truncate(strings_command[i]);
            }
            Check = CreateIn_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.createIndex(strings_command[i]);
            }
            Check = DropIn_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.dropIndex(strings_command[i]);
            }
            Check = Alter_P.matcher(strings_command_upper[i]);
            if (Check.find()) {
                checkcommand=true;
                handlerRequest.alterTable(strings_command[i]);
            }
            if(!checkcommand) {
                //СООБЩЕНИЕ О ТОМ ЧТО КОМАНДА НЕ РАСПОЗНАНА ИЛИ ОШИБОЧНА
            }
            checkcommand=false;
        }
    }

    public void run(){
        try {
            Checkcom();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}