package Algoritmos;

import java.io.FileNotFoundException;
import java.util.List;

import Base.Escalonador;
import Base.Processo;

public class FCFS extends Escalonador {

    public FCFS(List<Processo> processos) throws FileNotFoundException {
        super(processos, "output/saidaFCFS.txt", "output/graficoFCFS.txt");
    }
    
    @Override
    public void Executar() throws FileNotFoundException {
        outputFile.print("********** TEMPO 0 **********\n");

        while (!processosEmEspera.isEmpty() || !fila.isEmpty()){
            verificarListaEspera();

            if ((tempo == 0 || tempoProcessando != 0) && this.processoExecutando.operacoesIO.contains(processoExecutando.instante)){ // Verifica se o processo está fazendo alguma operação de I/O
                outputFile.print("#[evento] OPERACAO I/O < " + processoExecutando.PID + " >\n");
                atualizaArquivos();
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
}
