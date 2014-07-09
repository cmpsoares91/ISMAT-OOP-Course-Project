/**
 * Este package fornece todas as classes para o funcionamento do jogo.
 */
package pootpstarwarslunarlandergame;

/**
 * Classe onde se define a Nave e guarda a informação da mesma enquanto
 * necessária.
 *
 * @author C.M.P.Soares
 */
public class Nave {

    /**
     * Valor inteiro que define a dificuldade do Jogo e o desenho da nave.
     * Nivel mais facil (easy) é definido por valor 1, médio (medium) é 2,
     * dificil (hard) é 3 e o mais dificil (very hard) é definido pelo valor
     * inteiro 4.
     */
    public static final int NAVE_EASY = 1;
    /**
     * @see Nave#NAVE_EASY
     */
    public static final int NAVE_MEDIUM = 2;
    /**
     * @see Nave#NAVE_EASY
     */
    public static final int NAVE_HARD = 3;
    /**
     * @see Nave#NAVE_EASY
     */
    public static final int NAVE_VERY_HARD = 4;
    /**
     * Variável privada constante após de ser inicializada que indica a potência
     * da nave. Define quantos m/s que a nave acelera quando é activado o jato.
     */
    private final int ACELERACAO_POR_JATO;
    /**
     * Variável privada que nos irá indicar quanto combustivel disponível no
     * correr do jogo. É medido em litros e é gasto conforme é utilizado o jato.
     */
    private int combustivel;
    /**
     * Variável privada constante após de ser inicializada que definira quanto a
     * nava gasta de combustível cada vez que é activo o jato.
     */
    private final int GASTO_POR_CLICK;

    /**
     * Construtor obrigatório para definir e inicializar as variáveis da
     * instância criada da classe Nave.
     *
     * @param aceleracaoPorJato Valor inteiro que define qual a força do jato,
     * em aceleração(m/s).
     * @param combustivel Valor inteiro de combustível disponível, em litros.
     * @param gastoPorClick Valor inteiro que define quantos litros de
     * combustível é gasto por jato.
     */
    public Nave(int aceleracaoPorJato, int combustivel, int gastoPorClick) {
        this.ACELERACAO_POR_JATO = aceleracaoPorJato;
        this.combustivel = combustivel;
        this.GASTO_POR_CLICK = gastoPorClick;
    }

    /**
     * @return O valor inteiro por Jato da aceleração da Nave.
     */
    public int getAceleracaoPorJato() {
        return ACELERACAO_POR_JATO;
    }

    /**
     * @return Valor inteiro que representa o combustivel disponível na Nave
     * para poder utilizar o Jato.
     */
    public int getCombustivel() {
        return combustivel;
    }

    /**
     * @return Valor inteiro que a nave gasta de combustivel por click (Jato).
     */
    public int getGastoPorClick() {
        return GASTO_POR_CLICK;
    }

    /**
     * Verifica se existe combustivel para usar, se exisitir o suficiente
     * utiliza a quantidade correspondente ao "Gasto por Click".
     *
     * @return 'true' se foi possivel utilizar combustivel, 'false' se for o
     * contrario.
     * @see #getGastoPorClick()
     * @see #getAceleracaoPorJato()
     * @see #getCombustivel()
     */
    public boolean useCombustivel() {

        if (combustivel <= 0) {
            combustivel = 0;
            return false;
        } else {
            combustivel = combustivel - GASTO_POR_CLICK;
            return true;
        }

    }
}
