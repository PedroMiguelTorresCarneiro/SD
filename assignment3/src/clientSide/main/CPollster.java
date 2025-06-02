// package clientSide.main;

// import clientSide.entities.TPollster;
// import commInfra.interfaces.ExitPoll.IExitPoll_TPollster;

// import java.rmi.Naming;

// public class CPollster {

//     public static void main(String[] args) {
//         try {
//             /* Lê parâmetros do ficheiro .env */
//             String host = "localhost";
//             int exitPollPort = 45000;

//             /* Obtém stub remoto da ExitPoll via RMI */
//             IExitPoll_TPollster exitPoll = (IExitPoll_TPollster)
//                 Naming.lookup("rmi://" + host + ":" + exitPollPort + "/ExitPoll");

//             /* Inicia a thread TPollster */
//             Thread pollster = new Thread(TPollster.getInstance(exitPoll));
//             pollster.start();

//             pollster.join();

//         } catch (Exception e) {
//             System.err.println("Erro no cliente CPollster: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }
// }

package clientSide.main;

import clientSide.entities.TPollster;
import commInfra.interfaces.ExitPoll.IExitPoll_TPollster;
import java.rmi.Naming;

public class CPollster {

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int registryPort = 1099;

            // Obter stub remoto da ExitPoll via RMI central
            IExitPoll_TPollster exitPoll = (IExitPoll_TPollster)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/ExitPoll");

            // Iniciar a thread TPollster
            Thread pollster = new Thread(TPollster.getInstance(exitPoll));
            pollster.start();
            pollster.join();

        } catch (Exception e) {
            System.err.println("❌ Erro no cliente CPollster: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
