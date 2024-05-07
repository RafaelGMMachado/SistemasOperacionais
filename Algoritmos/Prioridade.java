package Algoritmos;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import Base.Escalonador;
import Base.Processo;

public class Prioridade extends Escalonador {

    public Prioridade(List<Processo> processos) throws FileNotFoundException {
        super(processos, "output/prioridadeSaida.txt", "output/prioridadeGrafico.txt");
        
        Random rand = new Random();
        processoExecutando.prioridade = rand.nextInt(10);
        for (Processo processo : processosEmEspera){
            processo.prioridade = rand.nextInt(10);
        }
    }    

    @Override
    public void Executar() throws FileNotFoundException{
        outputFile.print("********** TEMPO 0 **********\n");

        while (!processosEmEspera.isEmpty() || !fila.isEmpty()){
            verificarListaEspera();

            fila.sort(Comparator.comparing(Processo::getPrioridade).reversed());
            if (!fila.isEmpty() && fila.get(0).prioridade > processoExecutando.prioridade){
                trocarProcesso(false);
                continue;
            }

            if ((tempo == 0 || tempoProcessando != 0) && processoExecutando.operacoesIO.contains(processoExecutando.instante)){ // Verifica se o processo está fazendo alguma operação de I/O
                outputFile.print("#[evento] OPERACAO I/O < " + processoExecutando.PID + " >\n");
                trocarProcesso(false);
                continue;
            }

            if (processoExecutando.instante == processoExecutando.duracao){ // Verifica se o processo chegou no final
                outputFile.print("#[evento] ENCERRANDO < " + processoExecutando.PID + " >\n");
                
                processoExecutando.tempoFinal = tempo;
                processosConcluidos.add(processoExecutando);
                
                trocarProcesso(true);
                continue;
            }

            atualizaArquivos();
            atualizaPrioridades();
        }

        printFinalArquivos();
    }

    protected void atualizaPrioridades(){
        List<Processo> filaReal = fila.stream().filter(o -> o.PID != processoExecutando.PID).collect(Collectors.toList());
        for (Processo processo : filaReal){
            processo.prioridade ++;
        }
    }

    @Override
    protected String getProcessoFilaText(Processo processo){
        return "(" + String.valueOf(processo.duracao - processo.instante) + ")" + "(" + String.valueOf(processo.prioridade) + ") ";   
    }

    @Override
    protected String getProcessoExecutandoText(){
        return "(" + String.valueOf(processoExecutando.duracao - processoExecutando.instante) + ")" + "(" + String.valueOf(processoExecutando.prioridade) + ")";   
    }
}
