package Threads;
import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.PollStation.IPollStation;
import java.util.Random;

public class TVoter extends Thread {
    private String voterId;
    private final IPollStation pollStation;
    private final IEvotingBooth booth;
    private final IExitPoll exitPoll;
    private String meuVoto;
    private final Random random = new Random();

    public TVoter(String voterId, IPollStation pollStation, IEvotingBooth booth, IExitPoll exitPoll) {
        this.voterId = voterId;
        this.pollStation = pollStation;
        this.booth = booth;
        this.exitPoll = exitPoll;
    }

    @Override
    public void run() {
        try {
            // Entra na estação e aguarda verificação
            pollStation.entrarNaEstacao(voterId);

            // **Aguardar permissão do TPollClerk**
            pollStation.aguardarAutorizacaoParaVotar(voterId);

            // Agora pode votar
            meuVoto = booth.votar(voterId);

            // Sai da estação após votar
            pollStation.sairDaEstacao(voterId);

            // 50% Voters decidem responder
            if (random.nextDouble() < 0.40) {
                // **Registrar saída na ExitPoll**
                exitPoll.registrarSaida(voterId, meuVoto);
            }

            // **Decidir se renasce**
            if (pollStation.isFechada()) {
                System.out.println("⏹ Eleições encerradas! " + voterId + " vai para casa.");
                return;
            }

            // **Probabilidade de renascer**
            boolean sameID = Math.random() > 0.5;
            if (!sameID) {
                voterId = "V" + (int) (Math.random() * 20);
            }

            // **Voltar para a fila externa**
            //run();
            // **Criar uma nova thread em vez de chamar run() diretamente**
            new TVoter(voterId, pollStation, booth, exitPoll).start();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
