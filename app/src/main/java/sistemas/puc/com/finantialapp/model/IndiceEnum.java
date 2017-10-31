package sistemas.puc.com.finantialapp.model;

public enum IndiceEnum {
    
    SELIC("Selic", "SELIC", Type.SIMPLE),
    IPCA("Índice Nacional de Preços ao Consumidor Amplo", "IPCA", Type.DOUBLE),
    CDI("Certificados de Depósito Interbancário", "CDI", Type.SIMPLE),
    INPC("Índice Nacional de Preços ao Consumidor", "INPC", Type.DOUBLE),
    IGPM("Índice Geral de Preços do Mercado", "IGP-M", Type.DOUBLE),
    POUPANCA("Poupança", "POUPANÇA", Type.DOUBLE);

    private String name, code;
    private Type type;

    IndiceEnum(String name, String code, Type type) {
        this.name = name;
        this.code = code;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        SIMPLE,
        DOUBLE
    }
}
