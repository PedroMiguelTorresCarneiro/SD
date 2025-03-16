package Threads;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.IDCheck.IIDCheck;
import Monitors.PollStation.IPollStation;
import java.util.Map;


public class TPollClerk extends Thread {
    private final IPollStation pollStation;
    private final IIDCheck idCheck;
    private final IEvotingBooth booth;
    private final IExitPoll exitPoll;
    private final int maxVotos; // Número máximo de votos para encerrar
    private boolean encerrado = false;

    public TPollClerk(IPollStation pollStation, IIDCheck idCheck, IEvotingBooth booth, IExitPoll exitPoll, int maxVotos) {
        this.pollStation = pollStation;
        this.idCheck = idCheck;
        this.booth = booth;
        this.exitPoll = exitPoll;
        this.maxVotos = maxVotos;
    }

    @Override
    public void run() {
        try {
            while (!encerrado) {
                pollStation.aguardarSaidaDoAnterior(); // Aguarda o votante anterior sair

                String voterId = pollStation.chamarProximoParaVerificacao();
                if (voterId == null) break;

                System.out.println("📢 TPollClerk verificando ID do votante " + voterId + "...");

                if (!idCheck.verificarID(voterId)) {
                    System.out.println("❌ ID inválido! Votante " + voterId + " expulso.");
                    pollStation.sairDaEstacao(voterId); // Remover o votante da estação
                    continue;
                }

                System.out.println("✅ ID válido! Votante " + voterId + " pode votar.");
                
                pollStation.permitirVoto(); // Libera o Tvoter para votar

                if (booth.getTotalVotos() >= maxVotos) {
                    
                    encerrarEleicao();
                }
            }

            pollStation.fecharEstacao();
            exitPoll.anunciarFim();

            Map<String, String> resultados = booth.gatherVotes();
            System.out.println("\n📊 RESULTADO FINAL DA ELEIÇÃO:");
            long votosA = resultados.values().stream().filter(v -> v.equals("A")).count();
            long votosB = resultados.values().stream().filter(v -> v.equals("B")).count();
            System.out.println("🗳 Candidato A: " + votosA + " votos");
            System.out.println("🗳 Candidato B: " + votosB + " votos");

            exitPoll.exibirResultados();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }




    public void encerrarEleicao() {
        encerrado = true;
        System.out.println("⏹ TPollClerk encerrou a eleição!");
    }
}
