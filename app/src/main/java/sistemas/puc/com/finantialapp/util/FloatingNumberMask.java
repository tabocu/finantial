package sistemas.puc.com.finantialapp.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


public class FloatingNumberMask implements TextWatcher {
    boolean m_isEdiging = false;
    char m_separator;
    int m_decimals;
    String m_prefix;

    public FloatingNumberMask(char separator, int decimals) {
        super();
        m_separator = separator;
        m_decimals = decimals;
        m_prefix = "";
    }

    public void setPrefix(String prefix) {
        m_prefix = prefix;
    }

    @Override
    public void beforeTextChanged(CharSequence cs, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(m_isEdiging) return;
        m_isEdiging = true;

        String str = s.toString().replaceAll("[^\\d]", "");

        while (str.startsWith("0")) str = str.substring(1, str.length());

        if (str.length() < m_decimals) {
            while (str.length() < m_decimals)
                str = "0" + str;
            str = "0" + m_separator + str;
        } else if (str.charAt(str.length() - m_decimals) != m_separator) {
            str = str.substring(0, str.length() - m_decimals) + m_separator
                    + str.substring(str.length() - m_decimals);
        }
        
        if (str.charAt(0) == m_separator)
            str = "0" + str;

        str = m_prefix + str;

        s.replace(0, s.length(), str);

        m_isEdiging = false;
    }

}