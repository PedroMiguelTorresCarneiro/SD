package Monitors.PollStation;


import Monitors.IAll;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MPollStation implements IPollStation {
    private final int capacidadeMax;
    private final Queue<String> filaEspera = new LinkedList<>();
    private final Queue<String> filaInterna = new LinkedList<>();
    private int votantesAtuais = 0;
    private boolean fechada = false;
    private final Lock lock;
    private final Condition podeEntrar; 
    private final Condition podeSerChamado;
    private final Condition podeVotar;
    private final Condition podeChamarProximo; 
    private final Condition podeFecharEstacao ;
    private String votanteAtual = null;
    private static MPollStation instance = null;

    private MPollStation(int capacidadeMax) {
        this.capacidadeMax = capacidadeMax;
        lock = new ReentrantLock();
        // Condições
        podeEntrar = lock.newCondition();
        podeSerChamado = lock.newCondition();
        podeVotar = lock.newCondition();
        podeChamarProximo = lock.newCondition();
        podeFecharEstacao = lock.newCondition();
        
    }
    
    public static IPollStation getInstance(int capacidadeMax) {
        if (instance == null) {
            instance =  new MPollStation(capacidadeMax);
        }
        return instance;
    }
    
 
    public void entrarNaEstacao(String voterId) throws InterruptedException {
        lock.lock();
        try {
            if (fechada) {
                System.out.println("❌ Estação fechada! " + voterId + " vai para casa.");
                //logs.registrarVoter(voterId); 
                return;
            }

            filaEspera.add(voterId);
            //logs.setExternal(voterId); // Registra a chegada do votante
            System.out.println("🕒 Votante " + voterId + " entrou na fila externa...");

            while (votantesAtuais >= capacidadeMax || !filaEspera.peek().equals(voterId)) {
                if (fechada) {
                    System.out.println("❌ Estação fechada! " + voterId + " foi removido da fila.");
                    return;
                }
                podeEntrar.await();
            }

            filaEspera.poll();
            votantesAtuais++;
            filaInterna.add(voterId);
            //logs.setInternal(voterId); // Registra a entrada do votante
            System.out.println("✅ Votante " + voterId + " entrou na estação e aguarda verificação de ID.");
            podeSerChamado.signal();
        } finally {
            lock.unlock();
        }
    }

    public String chamarProximoParaVerificacao() throws InterruptedException {
        lock.lock();
        try {
            while (filaInterna.isEmpty()) {
                podeSerChamado.await();
            }

            votanteAtual = filaInterna.poll();
            //logs.setIDCheck(votanteAtual); // Registra a verificação de ID
            return votanteAtual;
        } finally {
            lock.unlock();
        }
    }

    public void permitirVoto() {
        lock.lock();
        try {
            podeVotar.signal();
        } finally {
            lock.unlock();
        }
    }

    public void aguardarAutorizacaoParaVotar(String voterId) throws InterruptedException {
        lock.lock();
        try {
            while (!voterId.equals(votanteAtual)) {
                podeVotar.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void sairDaEstacao(String voterId) {
        lock.lock();
        try {
            votantesAtuais--;
            System.out.println("🚪 Votante " + voterId + " saiu da estação.");
            votanteAtual = null;
            podeChamarProximo.signal(); // Permite ao TPollClerk chamar o próximo
            podeEntrar.signal(); // Agora outro votante pode entrar
        } finally {
            lock.unlock();
        }
    }

    public void aguardarSaidaDoAnterior() throws InterruptedException {
        lock.lock();
        try {
            while (votanteAtual != null) {
                podeChamarProximo.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void fecharEstacao() {
        lock.lock();
        try {
            fechada = true;
            System.out.println("⏹ A estação de votação foi FECHADA!");
            podeEntrar.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean isFechada() {
        return fechada;
    }
    
}
