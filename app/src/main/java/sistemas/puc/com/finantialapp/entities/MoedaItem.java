package sistemas.puc.com.finantialapp.entities;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;

public class MoedaItem {

    private String m_codigo;
    private String m_nome;
    private double m_cotacao;
    private long m_time;

    public MoedaItem (@NonNull @Size(3) String codigo,
                      @NonNull String nome,
                      @FloatRange(from=0) double cotacao,
                      @IntRange(from=0) long time) {
        m_codigo = codigo;
        m_nome = nome;
        m_cotacao = cotacao;
        m_time = time;
    }

    public String getCodigo() {
        return m_codigo;
    }

    public String getNome() {
        return m_nome;
    }

    public double getCotacao() {
        return m_cotacao;
    }

    public long getTime() {
        return m_time;
    }

    @Override
    public String toString() {
        return m_nome;
    }
}
