import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException{
        List<Processo> processos = new ArrayList<>();

        FileReader file = new FileReader("code.txt");
        try (BufferedReader reader = new BufferedReader(file)) {
            String linha = reader.readLine();
            
            while(linha != null){
                processos.add(new Processo(linha)); 
                linha = reader.readLine();
            }
        }

        Escalonador escalonador = new Escalonador(processos, 4);
        escalonador.Executar();
    }
}
