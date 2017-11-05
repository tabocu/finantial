package sistemas.puc.com.finantialapp.entities;

public abstract class IndiceItem {

    public String m_nome;

    public IndiceItem (String nome) {
        m_nome = nome;
    }

    public static class Simples extends IndiceItem {

        public String m_data;
        public String m_taxa;

        public Simples (String nome, String data, String taxa) {
            super(nome);
            m_data = data;
            m_taxa = taxa;
        }
    }

    public static class Duplo extends IndiceItem {

        public String m_nomeMensal;
        public String m_taxaMensal;
        public String m_nomeAnual;
        public String m_taxaAnual;

        public Duplo (String nome,
                        String nomeMensal, String taxaMensal,
                        String nomeAnual, String taxaAnual) {
            super(nome);

            m_nomeMensal = nomeMensal;
            m_taxaMensal = taxaMensal;
            m_nomeAnual = nomeAnual;
            m_taxaAnual = taxaAnual;
        }


    }

}
