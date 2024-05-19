package Algoritmos;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

import Base.Escalonador;
import Base.Processo;

public class SJF extends Escalonador {

    public SJF(List<Processo> processos) throws FileNotFoundException {
        super(processos, "output/SJFSaida.txt", "output/SJFGrafico.txt");
    }
    
    @Override
    public void Executar() throws FileNotFoundException {
        outputFile.print("********** TEMPO 0 **********\n");

        while (!processosEmEspera.isEmpty() || !fila.isEmpty()){
            boolean novoNaFila = verificarListaEspera();

            if (novoNaFila){
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
        }

        printFinalArquivos();
    }

    @Override
    protected void trocarProcesso(Boolean fim){
        fila.remove(processoExecutando);
        
        if (!fim)
            fila.add(processoExecutando);
        
        fila.sort(Comparator.comparing(Processo::getTempoRestante));
        processoExecutando = fila.size() >= 1 ? fila.get(0) : processoExecutando;
        tempoProcessando = 0;
    }
}
