/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package footballStats;
import footballStats.PieChart;

/**
 *
 * @author NEEL
 */

public class Main2 {
   public static void main(String[] args) {
          PieChart demo = new PieChart("Comparison", "Which operating system are you using?");
          demo.pack();
          demo.setVisible(true);
      }
}