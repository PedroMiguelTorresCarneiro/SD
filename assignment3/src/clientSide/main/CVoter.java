// package clientSide.main;

// import clientSide.entities.TVoter;
// import commInfra.interfaces.EvotingBooth.IEVotingBooth_TVoter;
// import commInfra.interfaces.ExitPoll.IExitPoll_TVoter;
// import commInfra.interfaces.IDCheck.IIDCheck_TVoter;
// import commInfra.interfaces.PollStation.IPollStation_TVoter;
// import commInfra.interfaces.Repository.IRepo_Tvoter;
// import java.rmi.Naming;

// /**
//  * Cliente que instancia e lança os TVoters via RMI.
//  */
// public class CVoter {

//     public static void main(String[] args) {
//         try {
//             /* Lê parâmetros do ficheiro .env */
//             String host = "localhost";

//             // Ports (não usados no lookup direto se o registry estiver na 1099)
//             int psPort = 46000;
//             int idCheckPort = 42000;
//             int boothPort = 44000;
//             int exitPollPort = 45000;

//             // Obtenção dos stubs remotos via RMI
//             IPollStation_TVoter pollStation = (IPollStation_TVoter)
//                 Naming.lookup("rmi://" + host + ":" + psPort + "/PollStation");

//             IIDCheck_TVoter idCheck = (IIDCheck_TVoter)
//                 Naming.lookup("rmi://" + host + ":" + idCheckPort + "/IDCheck");

//             IEVotingBooth_TVoter booth = (IEVotingBooth_TVoter)
//                 Naming.lookup("rmi://" + host + ":" + boothPort + "/EVotingBooth");

//             IExitPoll_TVoter exitPoll = (IExitPoll_TVoter)
//                 Naming.lookup("rmi://" + host + ":" + exitPollPort + "/ExitPoll");
            
//             IRepo_Tvoter repo = (IRepo_Tvoter)
//                 Naming.lookup("rmi://localhost:43000/Repository");

//             int numberOfVoters = repo.getNumberOfVoters(); // vem do lado do servidor


//             // Cria e arranca os TVoters
//             Thread[] voters = new Thread[numberOfVoters];
//             for (int i = 0; i < numberOfVoters; i++) {
//                 voters[i] = new Thread(TVoter.getInstance(
//                         "V" + (i + 1),
//                         pollStation,
//                         idCheck,
//                         booth,
//                         exitPoll));
//                 voters[i].start();
//             }

//             // Espera que todos terminem
//             for (Thread v : voters) {
//                 v.join();
//             }

//         } catch (Exception e) {
//             System.err.println("Erro no cliente CVoter: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }
// }

package clientSide.main;

import clientSide.entities.TVoter;
import commInfra.interfaces.EvotingBooth.IEVotingBooth_TVoter;
import commInfra.interfaces.ExitPoll.IExitPoll_TVoter;
import commInfra.interfaces.IDCheck.IIDCheck_TVoter;
import commInfra.interfaces.PollStation.IPollStation_TVoter;
import commInfra.interfaces.Repository.IRepo_Tvoter;
import java.rmi.Naming;

/**
 * Cliente que instancia e lança os TVoters via RMI.
 */
public class CVoter {

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int registryPort = 1099;

            // Obtenção dos stubs remotos via RMI central
            IPollStation_TVoter pollStation = (IPollStation_TVoter)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/PollStation");

            IIDCheck_TVoter idCheck = (IIDCheck_TVoter)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/IDCheck");

            IEVotingBooth_TVoter booth = (IEVotingBooth_TVoter)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/EVotingBooth");

            IExitPoll_TVoter exitPoll = (IExitPoll_TVoter)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/ExitPoll");

            IRepo_Tvoter repo = (IRepo_Tvoter)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/Repository");

            int numberOfVoters = repo.getNumberOfVoters();

            // Criação e execução das threads TVoter
            Thread[] voters = new Thread[numberOfVoters];
            for (int i = 0; i < numberOfVoters; i++) {
                voters[i] = new Thread(TVoter.getInstance(
                        "V" + (i + 1),
                        pollStation,
                        idCheck,
                        booth,
                        exitPoll));
                voters[i].start();
            }

            for (Thread v : voters) {
                v.join();
            }

        } catch (Exception e) {
            System.err.println("❌ Erro no cliente CVoter: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
