package Monitors.ExitPoll;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class MExitPoll implements IExitPoll {
    private static MExitPoll instance = null;
    private final ReentrantLock lock;
    private final Queue<String> filaSaida = new LinkedList<>(); // Votantes que saíram da estação
    private final Map<String, String> votosReais = new HashMap<>(); // Armazena o voto real do votante
    private int votosA = 0;
    private int votosB = 0;
    private boolean eleicaoEncerrada = false;
    private final Condition pollsterPodeEntrevistar; // Condition para entrevistas
    private final Random random = new Random();

    private MExitPoll() {
        lock = new ReentrantLock();
        // Condições
        pollsterPodeEntrevistar = lock.newCondition();
    }
    
    public static IExitPoll getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
        }
        return instance;
    }
    
    public void registrarSaida(String voterId, String votoReal) {
        lock.lock();
        try {
            if (eleicaoEncerrada) {
                return; // Se a eleição acabou, não registramos mais ninguém
            }

            filaSaida.add(voterId);
            
            
            
            votosReais.put(voterId, votoReal);
            pollsterPodeEntrevistar.signal(); // Notifica o TPollster de que há alguém para entrevistar
        } finally {
            lock.unlock();
        }
    }

    public String entrevistarProximo() throws InterruptedException {
        lock.lock();
        try {
            // 60% é selecionado para entrevista
            if (random.nextDouble() < 0.60) {
                while (filaSaida.isEmpty()) {
                    if (eleicaoEncerrada) {
                        return null; // Se a eleição acabou, terminamos a thread
                    }
                    pollsterPodeEntrevistar.await(); // Espera até que haja alguém para entrevistar
                }
             
                // Selecionado para entrevista
                // 20% Mente

                //logs.atualizarEstado(voterId, "Vote", "ExitPoll"); // Registra a mudança de fila

                registrarResposta(filaSaida.poll());
                return "Selecionado";
            }else{
                // 40% Não são selecionados
                return "Não Selecionado";

                //logs.atualizarEstado(voterId, "Vote", "SAIU");   // Registra a mudança de fila

            }
        } finally {
            lock.unlock();
        }
    }

    private String getVotoReal(String voterId) {
        lock.lock();
        try {
            return votosReais.getOrDefault(voterId, "N/A");
        } finally {
            lock.unlock();
        }
    }

    private void registrarResposta(String voterId) {
        lock.lock();
        try {
            String votoReal = getVotoReal(voterId);
            boolean mentiu = random.nextDouble() < 0.20;
            String resposta = mentiu ? (votoReal.equals("A") ? "B" : "A") : votoReal;

            if (resposta.equals("A")) {
                votosA++;
            } else if (resposta.equals("B")) {
                votosB++;
            }
            System.out.println("📊 TPollster entrevistou " + voterId + ". Respondeu: " + resposta);
        } finally {
            lock.unlock();
        }
    }

    public void anunciarFim() {
        lock.lock();
        try {
            eleicaoEncerrada = true;
            pollsterPodeEntrevistar.signalAll(); // Notifica o TPollster de que a eleição acabou
            System.out.println("⏹ ExitPoll foi NOTIFICADO do fim da eleição!");
        } finally {
            lock.unlock();
        }
    }

    public boolean isEncerrado() {
        return eleicaoEncerrada;
    }

    public void exibirResultados() {
        lock.lock();
        try {
            System.out.println("\n📊 RESULTADO DA PESQUISA DE SAÍDA:");
            System.out.println("🗳 Candidato A: " + votosA + " votos (pesquisa)");
            System.out.println("🗳 Candidato B: " + votosB + " votos (pesquisa)");
        } finally {
            lock.unlock();
        }
    }
}