import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Escalonador {
    private int tempo;
    private List<Processo> processos;
    private List<Processo> processosConcluidos;
    private int quantum;
    private String executandoPID;

    public Escalonador(List<Processo> processos, int quantum){
        processos.sort(Comparator.comparing(Processo::getChegada));
        
        this.tempo = 0;
        this.processos = processos; 
        this.processosConcluidos = new ArrayList<>();
        this.quantum = quantum;
        this.executandoPID = processos.get(0).PID;
    }

    public void Executar(){
        int quantProcessos = processos.size() - 1;
        int timeInProcess = 0;

        while (!processos.isEmpty()){
            Processo processo = processos.stream().filter(o -> o.PID == executandoPID).findFirst().get();
            int processoIndex = processos.indexOf(processo);
            
            if (timeInProcess >= quantum || 
                ((tempo == 0 || timeInProcess != 0) && processo.operacoesIO.contains(processo.instante)) ||
                tempo < processo.chegada
            ){
                executandoPID = processoIndex == quantProcessos ? processos.get(0).PID : processos.get(processoIndex + 1).PID;
                timeInProcess = 0;
                continue;
            }

            if (processo.instante == processo.duracao){
                executandoPID = processoIndex == quantProcessos ? processos.get(0).PID : processos.get(processoIndex + 1).PID;
                
                processos.remove(processoIndex);
                processosConcluidos.add(processo);
                
                quantProcessos --;
                timeInProcess = 0;
                continue;
            }


            processo.instante ++;
            timeInProcess ++;
            tempo ++;
            System.out.print(tempo + " | " + processo.PID + "\n");
        }
    }
}
