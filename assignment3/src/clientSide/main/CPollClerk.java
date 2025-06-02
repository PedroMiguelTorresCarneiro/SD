// package clientSide.main;

// import clientSide.entities.TPollClerk;
// import commInfra.interfaces.PollStation.IPollStation_TPollClerk;
// import commInfra.interfaces.EvotingBooth.IEVotingBooth_TPollClerk;
// import commInfra.interfaces.ExitPoll.IExitPoll_TPollClerk;
// import commInfra.interfaces.IDCheck.IIDCheck_TPollClerk;
// import commInfra.interfaces.Repository.IRepo_TPollClerk;

// import java.rmi.Naming;

// public class CPollClerk {

//     public static void main(String[] args) {
//         try {
//             /* Lê parâmetros do ficheiro .env */
//             String host = "localhost";

//             int psPort = 46000;
//             int boothPort = 44000;
//             int exitPollPort = 45000;
//             int idCheckPort = 42000;

//             // Obter stubs remotos via RMI
//             IPollStation_TPollClerk pollStation = (IPollStation_TPollClerk)
//                 Naming.lookup("rmi://" + host + ":" + psPort + "/PollStation");

//             IEVotingBooth_TPollClerk booth = (IEVotingBooth_TPollClerk)
//                 Naming.lookup("rmi://" + host + ":" + boothPort + "/EVotingBooth");

//             IExitPoll_TPollClerk exitPoll = (IExitPoll_TPollClerk)
//                 Naming.lookup("rmi://" + host + ":" + exitPollPort + "/ExitPoll");

//             IIDCheck_TPollClerk idCheck = (IIDCheck_TPollClerk)
//                 Naming.lookup("rmi://" + host + ":" + idCheckPort + "/IDCheck");
            
//             IRepo_TPollClerk repo = (IRepo_TPollClerk)
//                 Naming.lookup("rmi://localhost:43000/Repository");

//             int maxVotes = repo.getMaxVotes(); // vem do lado do servidor
//             // Iniciar a thread TPollClerk
//             Thread clerk = new Thread(TPollClerk.getInstance(
//                     pollStation, booth, exitPoll, idCheck, maxVotes
//             ));
//             clerk.start();
//             clerk.join();

//         } catch (Exception e) {
//             System.err.println("Erro no cliente CPollClerk: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }
// }

package clientSide.main;

import clientSide.entities.TPollClerk;
import commInfra.interfaces.EvotingBooth.IEVotingBooth_TPollClerk;
import commInfra.interfaces.ExitPoll.IExitPoll_TPollClerk;
import commInfra.interfaces.IDCheck.IIDCheck_TPollClerk;
import commInfra.interfaces.PollStation.IPollStation_TPollClerk;
import commInfra.interfaces.Repository.IRepo_TPollClerk;
import java.rmi.Naming;

public class CPollClerk {

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int registryPort = 1099;

            // Obter stubs do RMI Registry central
            IPollStation_TPollClerk pollStation = (IPollStation_TPollClerk)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/PollStation");

            IEVotingBooth_TPollClerk booth = (IEVotingBooth_TPollClerk)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/EVotingBooth");

            IExitPoll_TPollClerk exitPoll = (IExitPoll_TPollClerk)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/ExitPoll");

            IIDCheck_TPollClerk idCheck = (IIDCheck_TPollClerk)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/IDCheck");

            IRepo_TPollClerk repo = (IRepo_TPollClerk)
                Naming.lookup("rmi://" + host + ":" + registryPort + "/Repository");

            int maxVotes = repo.getMaxVotes();

            // Lançar thread TPollClerk
            Thread clerk = new Thread(TPollClerk.getInstance(
                pollStation, booth, exitPoll, idCheck, maxVotes
            ));
            clerk.start();
            clerk.join();

        } catch (Exception e) {
            System.err.println("❌ Erro no cliente CPollClerk: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


