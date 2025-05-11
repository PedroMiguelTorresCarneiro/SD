package serverSide.client;

import serverSide.stubs.STmainGUI;
import utils.EnvReader;

public class CmainGUI {
    public static void main(String[] args) {
        // 1. Lê host e porta do ficheiro .env
        String host = EnvReader.get("HOST");
        int guiPort = 47000; // fallback default

        // 2. Instancia stub para comunicar com a mainGUI
        STmainGUI gui = STmainGUI.getInstance(host, guiPort);

        // 3. Envia mensagens de demonstração
        gui.clean(); // limpa interface
        gui.logPOLLSTATION("OPEN  ");
        gui.addExternalFIFO("V1");
        gui.addExternalFIFO("V2");
        gui.removeExternalFIFO("V1");
        gui.addInternalFIFO("V2");
        gui.removeInternalFIFO("V2");
        gui.addIdcheckFIFO("V2");
        gui.logIDCHECK("V2✔", '✔');
        gui.removeIdcheckFIFO("V2");
        gui.logVOTING("V2");

        gui.updatePartyA(60);
        gui.updatePartyB(40);
        gui.setElecPartyAwinner();

        gui.updatePartyA_survey(45);
        gui.updatePartyB_survey(55);
        gui.setSurveyPartyBwinner();

        System.out.println("✅ Teste de envio para GUI completo.");
    }
}