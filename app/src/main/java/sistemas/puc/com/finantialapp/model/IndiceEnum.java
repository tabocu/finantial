package sistemas.puc.com.finantialapp.model;

public enum IndiceEnum {

    SELIC("Selic", "SELIC", Type.SIMPLE),
    IPCA("Índice Nacional de Preços ao Consumidor Amplo", "IPCA", Type.DOUBLE),
    CDI("Certificados de Depósito Interbancário", "CDI", Type.SIMPLE),
    INPC("Índice Nacional de Preços ao Consumidor", "INPC", Type.DOUBLE),
    IGPM("Índice Geral de Preços do Mercado", "IGP-M", Type.DOUBLE),
    POUPANCA("Poupança", "POUPANÇA", Type.DOUBLE);

    private String m_name, m_code;
    private Type m_type;

    IndiceEnum(String name, String code, Type type) {
        this.m_name = name;
        this.m_code = code;
        this.m_type = type;
    }

    public String getName() {
        return m_name;
    }

    public String getCode() {
        return m_code;
    }

    public Type getType() {
        return m_type;
    }

    public enum Type {
        SIMPLE,
        DOUBLE
    }
}
