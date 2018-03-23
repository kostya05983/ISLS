package SQL.Parser;
import GUI.Main;
import SQL.Parser.HandlerRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectorRequest implements Runnable{

    private Main main;
    private boolean checkcommand;
    private Matcher Check;
    private Pattern Create_P =  Pattern.compile("CREATE\\s+TABLE");//готов
    private Pattern Insert_P = Pattern.compile("INSERT\\sINTO");//готов
    private Pattern Update_P = Pattern.compile("UPDATE");//готов
    private Pattern Delet_P = Pattern.compile("DELETE\\s+FROM");//готов
    private Pattern Select_P = Pattern.compile("SELECT");//готов
    private Pattern Drop_P = Pattern.compile("\\s*DROP\\s+TABLE\\s+((\\w)+\\s*)");//готов
    private Pattern Truncate_P = Pattern.compile("TRUNCATE\\s+TABLE");//готов
    private Pattern CreateIn_P = Pattern.compile("CREATE\\s+INDEX");
    private Pattern DropIn_P = Pattern.compile("DROP\\s+INDEX");
    private Pattern Alter_P = Pattern.compile("ALTER\\s+TABLE");
    private String mystringfirst;

    public SelectorRequest(String mstring,Main main)
    {
        this.main=main;
        mystringfirst=mstring;
        checkcommand=false;
    }

    private void Checkcom() {
        String mystring = mystringfirst.toUpperCase();
        mystring=mystring.replaceAll("\n"," ");
        String [] strings_command = mystring.split("\\s*;\\s*");
        HandlerRequest handlerRequest=new HandlerRequest(main);

        for (int i=0; i<strings_command.length;i++)
        {
            strings_command[i]+=";";

            Check = Create_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                handlerRequest.createTable(mystring);
                //ВЫЗОВ CREATE TABLE с mystring
            }
            Check = Insert_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                //ВЫЗОВ INSERT INTO с mystring
            }
            Check = Update_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                //ВЫЗОВ UPDATE с mystring
            }
            Check = Delet_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                //ВЫЗОВ DELETE с mystring
            }
            Check = Select_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                //ВЫЗОВ SELECT с mystring
            }
            Check = Drop_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                handlerRequest.dropTable(mystring);
            }
            Check = Truncate_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                //ВЫЗОВ TRUNCATE с mystring
            }
            Check = CreateIn_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                //ВЫЗОВ CREATE INDEX с mystring
            }
            Check = DropIn_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                //ВЫЗОВ DROP INDEX с mystring
            }
            Check = Alter_P.matcher(strings_command[i]);
            if (Check.find())
            {
                checkcommand=true;
                handlerRequest.alterTable(mystring);
                //ВЫЗОВ ALTER TABLE с mystring
            }
            if(!checkcommand)
            {
                //СООБЩЕНИЕ О ТОМ ЧТО КОМАНДА НЕ РАСПОЗНАНА ИЛИ ОШИБОЧНА
            }
            checkcommand=false;
        }
    }

    public void run(){
        Checkcom();
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}