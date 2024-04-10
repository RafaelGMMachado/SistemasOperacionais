import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Escalonador {
    private int tempo;
    private List<Processo> processosEmEspera;
    private List<Processo> fila;
    private List<Processo> processosConcluidos;
    private int quantum;
    private Processo processoExecutando;
    private int tempoProcessando;

    public Escalonador(List<Processo> processos, int quantum){
        processos.sort(Comparator.comparing(Processo::getChegada));
        
        this.quantum = quantum;
        this.tempo = 0;
        this.tempoProcessando = 0;
        this.processoExecutando = processos.remove(0);
        this.processosEmEspera = processos;
        this.processosConcluidos = new ArrayList<>();
        this.fila = new ArrayList<>();
    }

    public void Executar() throws FileNotFoundException{
        PrintWriter outputFile = new PrintWriter("saida.txt");
        PrintWriter graficoFile = new PrintWriter("grafico.txt");

        outputFile.print("********** TEMPO 0 **********\n");

        while (!processosEmEspera.isEmpty() || !fila.isEmpty()){
            List<Processo> novoNaFila = processosEmEspera.stream().filter(o -> o.chegada == tempo).collect(Collectors.toList());
            if (!novoNaFila.isEmpty()){
                fila.addAll(novoNaFila);
                processosEmEspera.removeAll(novoNaFila);
                outputFile.print("#[evento] CHEGADA < " + getProcessosNovosText(novoNaFila) + ">\n");
            }

            if (tempoProcessando >= quantum){
                trocarProcesso(false);
                continue;
            }

            if ((tempo == 0 || tempoProcessando != 0) && processoExecutando.operacoesIO.contains(processoExecutando.instante)){
                outputFile.print("#[evento] OPERACAO I/O < " + processoExecutando.PID + " >\n");
                trocarProcesso(false);
                continue;
            }

            if (processoExecutando.instante == processoExecutando.duracao){
                outputFile.print("#[evento] ENCERRANDO < " + processoExecutando.PID + " >\n");
                
                processoExecutando.tempoFinal = tempo;
                processosConcluidos.add(processoExecutando);
                
                trocarProcesso(true);
                continue;
            }

            outputFile.print("Fila: " + getProcecssosFilaText() + "\n");
            outputFile.print("CPU: " + processoExecutando.PID + "(" + String.valueOf(processoExecutando.duracao - processoExecutando.instante) + ")\n");
            graficoFile.print(processoExecutando.PID + " | ");
            
            processoExecutando.instante ++;
            tempoProcessando ++;
            tempo ++;
            outputFile.print("********** TEMPO " + tempo + " **********\n");
            System.out.println(tempo + " " + processoExecutando.PID + "\n");
        }

        outputFile.print("Fila: Nao ha processos na fila\n");
        outputFile.print("ACABARAM OS PROCESSOS!!!\n");
        outputFile.close();
        
        graficoFile.print("\n\n********** Tempo de espera **********\n");
        int somaTempo = 0;
        for (Processo processo : processosConcluidos){
            int tempoEspera = processo.tempoFinal-processo.chegada;
            somaTempo += tempoEspera;
            graficoFile.print(processo.PID + ": " + tempoEspera + "\n");
        }
        graficoFile.print("Media: " + somaTempo/(processosConcluidos.size()-1));
        graficoFile.close();
    }

    private void trocarProcesso(Boolean fim){
        fila.remove(processoExecutando);
        if (!fim) fila.add(processoExecutando);

        processoExecutando = fila.size() >= 1 ? fila.get(0) : processoExecutando;
        tempoProcessando = 0;
    }

    private String getProcecssosFilaText(){
        String texto = "";
        List<Processo> filaReal = fila.stream().filter(o -> o.PID != processoExecutando.PID).collect(Collectors.toList());

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
