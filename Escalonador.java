import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Escalonador {
    private int tempo;
    private List<Processo> processos;
    private List<Processo> fila;
    private List<Processo> processosConcluidos;
    private int quantum;
    private String executandoPID;
    private int tempoProcessando;

    public Escalonador(List<Processo> processos, int quantum){
        processos.sort(Comparator.comparing(Processo::getChegada));
        
        this.tempo = 0;
        this.processos = processos; 
        this.processosConcluidos = new ArrayList<>();
        this.fila = new ArrayList<>();
        this.quantum = quantum;
        this.executandoPID = processos.get(0).PID;
        this.tempoProcessando = 0;
    }

    public void Executar() throws FileNotFoundException{
        PrintWriter outputFile = new PrintWriter("output.txt");

        outputFile.print("********** TEMPO 0 **********\n");

        while (!processos.isEmpty() || !fila.isEmpty()){
            List<Processo> novoNaFila = processos.stream().filter(o -> o.chegada == tempo).collect(Collectors.toList());
            if (!novoNaFila.isEmpty()){
                fila.addAll(novoNaFila);
                processos.removeAll(novoNaFila);
                outputFile.print("#[evento] CHEGADA < " + getProcessosNovosText(novoNaFila) + ">\n");
            }

            Processo processo = fila.stream().filter(o -> o.PID == executandoPID).findFirst().get();
            int processoIndex = fila.indexOf(processo);
            
            if (tempoProcessando >= quantum){
                trocarProcesso(processoIndex);
                continue;
            }

            if ((tempo == 0 || tempoProcessando != 0) && processo.operacoesIO.contains(processo.instante)){
                outputFile.print("#[evento] OPERACAO I/O < " + executandoPID + " >\n");
                trocarProcesso(processoIndex);
                continue;
            }

            if (processo.instante == processo.duracao){
                outputFile.print("#[evento] ENCERRANDO < " + executandoPID + " >\n");
                trocarProcesso(processoIndex);
                
                fila.remove(processoIndex);
                processosConcluidos.add(processo);
                
                continue;
            }

            outputFile.print("Fila: " + getProcecssosFilaText() + "\n");
            outputFile.print("CPU: " + executandoPID + "(" + String.valueOf(processo.duracao - processo.instante) + ")\n");
            
            processo.instante ++;
            tempoProcessando ++;
            tempo ++;
            outputFile.print("********** TEMPO " + tempo + " **********\n");
            System.out.print(tempo + " | " + executandoPID + "\n");
        }

        outputFile.print("Fila: Nao ha processos na fila\n");
        outputFile.print("ACABARAM OS PROCESSOS!!!\n");
        outputFile.close();
    }

    private void trocarProcesso(int processoIndex){
        executandoPID = processoIndex == fila.size() - 1 ? fila.get(0).PID : fila.get(processoIndex + 1).PID;
        tempoProcessando = 0;
    }

    private String getProcecssosFilaText(){
        String texto = "";
        List<Processo> filaReal = fila.stream().filter(o -> o.PID != executandoPID).collect(Collectors.toList());

        if (filaReal.isEmpty())
            return "Nao ha processos na fila";

        for (Processo processo : filaReal){
            texto += processo.PID + "(" + String.valueOf(processo.duracao - processo.instante) + ") ";
        }

        return texto;
    }

    private String getProcessosNovosText(List<Processo> novosProcessos){
        String texto = "";

        for (Processo processo : novosProcessos){
            texto += processo.PID + " ";
        }

        return texto;   
    }
}
