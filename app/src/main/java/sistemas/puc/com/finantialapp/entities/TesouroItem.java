package sistemas.puc.com.finantialapp.entities;

public class TesouroItem {
    // TODO: Criar getters e setters pertinentes
    public String m_nome;

    // TODO: Substituir pelos tipo date
    public String m_dataVencimento;

    // TODO: Substituir por double
    public String m_rendimentoCompra;
    public String m_rendimentoVenda;
    public String m_precoUnitarioCompra;
    public String m_precoUnitarioVenda;

    public TesouroItem(String nome, String dataVencimento, String rendimentoCompra,
                       String rendimentoVenda, String precoUnitarioCompra,
                       String precoUnitarioVenda) {
        m_nome = nome;
        m_dataVencimento = dataVencimento;
        m_rendimentoCompra = rendimentoCompra;
        m_rendimentoVenda = rendimentoVenda;
        m_precoUnitarioCompra = precoUnitarioCompra;
        m_precoUnitarioVenda = precoUnitarioVenda;
    }
}
