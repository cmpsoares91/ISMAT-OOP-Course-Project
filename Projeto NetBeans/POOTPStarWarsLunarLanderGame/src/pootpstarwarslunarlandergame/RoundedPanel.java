/**
 * Este package fornece todas as classes para o funcionamento do jogo.
 */
package pootpstarwarslunarlandergame;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * É um JPanel da javax.swing personalizado com os cantos arredondados. Esta
 * classe extende o JPanel e cria um painel preto ou transparente relativamente
 * ao Background existente no local onde é instânciado este painel.
 *
 * <b><i>Nota:</i></b> Após de instanciar esta classe deverá definir a opacidade
 * como 'false'
 *
 * @author C.M.P.Soares
 * @see JPanel
 * @see JPanel#setOpaque(boolean)
 */
class RoundedPanel extends JPanel {

    /**
     * Constante que define qual o raio se utiliza para arredondondar os cantos.
     *
     * @see Graphics#drawRoundRect(int, int, int, int, int, int)
     * @see Graphics#fillRoundRect(int, int, int, int, int, int)
     */
    private final int RADIUS;
    /**
     * Constante adicional utilizado para definir se o painel é preto ou
     * transparente.
     */
    private final boolean COLOR_IS_BLACK;

    /**
     * Construtor obrigatório para criar uma instância desta classe.
     *
     * @param cornerRadius Valor que definirá a constante RADIUS.
     * @param colorIsBlack Valor que definirá a constante COLOR_IS_BLACK.
     */
    RoundedPanel(int cornerRadius, boolean colorIsBlack) {
        RADIUS = cornerRadius;
        this.COLOR_IS_BLACK = colorIsBlack;
    }

    /**
     * Override do method que desenha o painel.
     *
     * @param g Parâmetro base do metodo <code>paintComponent()</code>.
     * @see JPanel#paintComponent(java.awt.Graphics) 
     */
    @Override
    public void paintComponent(Graphics g) {
        Color bg = getBackground();
        if (!COLOR_IS_BLACK) {
            g.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 40));
        }
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
        g.setColor(new Color(0, 0, 0, 70));
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
    }
}
