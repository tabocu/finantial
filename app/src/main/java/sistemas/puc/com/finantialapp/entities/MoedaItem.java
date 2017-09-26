package sistemas.puc.com.finantialapp.entities;

public class MoedaItem {
    public String m_nome;
    public String m_cotacao;
    public String m_data;

    public MoedaItem (String nome, String cotacao, String data) {
        m_nome = nome;
        m_cotacao = cotacao;
        m_data = data;
    }

    @Override
    public String toString() {
        return m_nome;
    }
}
