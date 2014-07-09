/**
 * Este package fornece todas as classes para o funcionamento do jogo.
 */
package pootpstarwarslunarlandergame;

/**
 * Classe que define o Planeta/Astro e guarda a informação da mesma enquanto
 * necessário.
 *
 * @author C.M.P.Soares
 */
public class Astro {

    /**
     * Constante que define a aceleração/gravidade do Astro.
     */
    private final int ACELERACAO;
    /**
     * Constante que define em que índice do nomeArray está o nome do Astro.
     */
    private final int NOME;
    /**
     * Matriz constante de String em que estão armazenados os nomes dos Astros
     * todos.
     * 
     * @see Astro#getNomeAstro()
     */
    private final String[] NOME_ARRAY = new String[]{"Lua", "Terra", "Abafar", "Aleen", "Balnab", "Bespin", "Carlac", "Alderaan", "Christophsis", "Coruscant", "Dagobah", "Dathomir", "Felucia", "Florrum", "Geonosis", "Hoth", "Ilum", "Kadavo", "Kamino", "Kiros", "Lotho Minor", "Mandalore", "Moncala", "Mustafar", "Nal Hutta", "Naboo"};

    /**
     * Construtor obrigatório para definir e inicializar as variáveis da
     * instância criada da classe Astro.
     *
     * @param acel Valor inteiro com que é inicializada da aceleração/gravidade
     * do Astro.
     * @param nome Valor inteiro que vai definir o índice em que encontra o nome
     * do Astro.
     */
    Astro(int acel, int nome) {
        while (nome > NOME_ARRAY.length) {
            nome = nome - NOME_ARRAY.length;
        }
        this.NOME = nome;
        this.ACELERACAO = acel;
    }

    /**
     * Método simples que retorna a aceleração perante aquele tipo de Astro
     * (Gravidade).
     *
     * @return Valor inteiro que representa a aceleração no sentido do astro
     * (Gravidade).
     */
    public int getAceleracao() {
        return ACELERACAO;
    }

    /**
     * Devolve o nome do Planeta/Astro.
     *
     * @return Um String com o nome do Astro.
     */
    public String getNomeAstro() {
        return NOME_ARRAY[NOME - 1];
    }
}
