package Monitors.PollStation;

public interface IPollStation {
    
    static IPollStation getInstance(int capacidadeMax){
        return MPollStation.getInstance(capacidadeMax);
    }
    
    void entrarNaEstacao(String voterId) throws InterruptedException;
    String chamarProximoParaVerificacao() throws InterruptedException;
    void permitirVoto();
    void aguardarAutorizacaoParaVotar(String voterId) throws InterruptedException;
    void sairDaEstacao(String voterId);
    public void aguardarSaidaDoAnterior() throws InterruptedException;
    void fecharEstacao();
    boolean isFechada();
}
