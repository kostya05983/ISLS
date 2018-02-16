package SQL.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectorRequest {

    protected boolean checkcommand;
    protected Matcher Check;
    protected Pattern Create_P =  Pattern.compile("CREATE\\s+TABLE\\s+(\\w|[А-ЯЁ])+\\s*\\((\\s*(\\w|[А-ЯЁ])+\\s+(CHARACTER|FLOAT|INTEGER)\\(\\d+\\)(\\s|,)*)+\\s*\\);");//готов
    protected Pattern Insert_P = Pattern.compile("INSERT\\sINTO\\s+([\\wА-ЯЁ])+\\s+\\((\\s*[\\wА-ЯЁ\"]+(\\s|,)*)+\\)\\s+VALUE\\s+\\((\\s*[\\wА-ЯЁ\"]+(\\s|,)*)+\\);");//готов
    protected Pattern Update_P = Pattern.compile("UPDATE\\s+([\\wА-Яа-яЁё])+\\s+SET\\s+(([\\wА-ЯЁ])+=([\\wА-ЯЁ\"])+(\\s|,)*)+\\s+WHERE\\s+[\\wА-ЯЁ=<>\\(\\)'\",\\s]+;");//готов
    protected Pattern Delet_P = Pattern.compile("DELETE\\s+FROM\\s+([\\wА-ЯЁ])+\\s+WHERE\\s+[\\wА-ЯЁ=<>\\(\\)'\"\\s]+;");//готов
    protected Pattern Select_P = Pattern.compile("SELECT\\s+((([\\wА-ЯЁ])+)(\\s|,)*)+\\s+FROM\\s+[\\wА-ЯЁ]+\\s+WHERE\\s+[\\wА-ЯЁ=<>\\(\\)'\"\\s]+;");//готов
    protected Pattern Drop_P = Pattern.compile("DROP\\s+TABLE\\s+([\\wА-ЯЁ])+;");//готов
    protected Pattern Truncate_P = Pattern.compile("TRUNCATE\\s+TABLE\\s+[\\wА-ЯЁ]+\\s*;");//готов
    protected Pattern CreateIn_P = Pattern.compile("CREATE\\s+INDEX\\s+[\\wА-ЯЁ]+\\s+ON\\s[\\wА-ЯЁ]+\\s\\(([\\wА-ЯЁ]+(\\s|,)*)+\\)\\s*;");
    protected Pattern DropIn_P = Pattern.compile("DROP\\s+INDEX\\s+[\\wА-ЯЁ]+\\s+ON\\s[\\wА-ЯЁ]+\\s*;");
    protected String mystringfirst;
    public Check(String mstring)
    {
        mystringfirst=mstring;
        checkcommand=false;
    }

    protected void Checkcom()
    {
        String mystring = mystringfirst.toUpperCase();
        String [] strings_command = mystring.split("\\s*;\\s*");

        for (int i=0; i<strings_command.length;i++)
        {
            strings_command[i]+=";";

            Check = Create_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ CREATE TABLE
            }
            Check = Insert_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ INSERT INTO
            }
            Check = Update_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ UPDATE
            }
            Check = Delet_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ DELETE
            }
            Check = Select_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ SELECT
            }
            Check = Drop_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
               //ВЫЗОВ DROP TABLE
            }
            Check = Truncate_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ TRUNCATE
            }
            Check = CreateIn_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ CREATE INDEX
            }
            Check = DropIn_P.matcher(strings_command[i]);
            if (Check.find() == true)
            {
                checkcommand=true;
                //ВЫЗОВ DROP INDEX
            }
            if(checkcommand==false)
            {
                //СООБЩЕНИЕ О ТОМ ЧТО КОМАНДА НЕ РАСПОЗНАНА ИЛИ ОШИБОЧНА
            }
            checkcommand=false;
        }
    }
}
