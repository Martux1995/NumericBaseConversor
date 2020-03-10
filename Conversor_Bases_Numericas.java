import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Conversor_Bases_Numericas {
    private static final String[] chars = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    
    public static void main(String[] args) {
        
        String[] valoresBases = {"2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
        
        final JTextField numero1 = new JTextField(22);
        final JTextField numero2 = new JTextField(22);   numero2.setEditable(false);
        
        final JComboBox base1 = new JComboBox(valoresBases); base1.setSelectedIndex(8);
        final JComboBox base2 = new JComboBox(valoresBases);
        
        JButton boton = new JButton("Convertir");
        
        JFrame x = new JFrame("Conversor de bases numéricas");
        x.setLayout(new GridBagLayout());
        
        x.add(new JLabel("Número: "), new GridBagConstraints(0,0,1,1,0,0,21,0,new Insets(4,4,2,2),0,0));
        x.add(numero1,                new GridBagConstraints(1,0,3,1,0,0,21,2,new Insets(4,2,2,4),0,0));
        x.add(new JLabel("Base: "),   new GridBagConstraints(0,1,1,1,0,0,21,0,new Insets(2,4,2,2),0,0));
        x.add(base1,                  new GridBagConstraints(1,1,1,1,0,0,21,0,new Insets(2,2,2,2),0,0)); 
        
        x.add(new JLabel("Convertir a base "), new GridBagConstraints(2,1,1,1,0,0,22,0,new Insets(2,30,2,2),0,0)); 
        x.add(base2, new GridBagConstraints(3,1,1,1,0,0,21,0,new Insets(2,2,2,2),0,0));
        
        x.add(boton,  new GridBagConstraints(0,2,1,1,0,0,10,0,new Insets(2,4,4,2),0,0));
        x.add(numero2,new GridBagConstraints(1,2,3,1,1,1,10,2,new Insets(2,2,4,4),0,0));
        
        x.setResizable(false);
        x.pack();
        x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        x.setLocationRelativeTo(null);
        
        boton.addActionListener(new ActionListener(){

            @Override public void actionPerformed(ActionEvent e) {
                numero1.setText(numero1.getText().toUpperCase());
                String numeroEditable = numero1.getText();
                if(ComprobarNumero(numeroEditable, base1.getSelectedIndex()+2)){
                    int cont = 0;
                    if (numeroEditable.contains(".")){
                        cont = (-1)*(numeroEditable.substring(numeroEditable.indexOf(".")+1).length());
                    }
                    numeroEditable = transformarBaseADecimal(numeroEditable, base1.getSelectedIndex()+2,cont);
                    if (Double.parseDouble(numeroEditable)>=10000000){
                        numero2.setText("ERROR: Numero grande (Max. 9999999)");
                        return;
                    } else if (Double.parseDouble(numeroEditable)<=-10000000){
                        numero2.setText("ERROR: Numero pequeño (Max. -9999999)");
                        return;
                    }
                    if(numeroEditable.endsWith(".0")) numeroEditable = numeroEditable.replace(".0", "");
                    numero2.setText( transformarDecimalABase(numeroEditable, base2.getSelectedIndex()+2) );
                    
                } else if(numeroEditable.contains(",")){
                    numero2.setText("ERROR: Formato Inválido");
                } else if (numeroEditable.isEmpty()){
                    numero2.setText("Ingrese un número");
                } else {
                    numero2.setText("ERROR: Número no válido");
                }
            }
        });

        x.setVisible(true);
    }
    
    private static String transformarBaseADecimal(String numero, int base, int cont){
        if(numero.isEmpty()){ return "0"; }
        int exp = cont;
        if (!numero.substring(numero.length()-1).equals("."))   exp++;
        String anterior = transformarBaseADecimal(numero.substring(0,numero.length()-1),base, exp);
        double valor;
        switch (numero.substring(numero.length()-1)){
            case "-": return "-";
            case ".": return anterior;
            case "A": valor = ( 10*Math.pow(base,cont) ); break;
            case "B": valor = ( 11*Math.pow(base,cont) ); break;
            case "C": valor = ( 12*Math.pow(base,cont) ); break;
            case "D": valor = ( 13*Math.pow(base,cont) ); break;
            case "E": valor = ( 14*Math.pow(base,cont) ); break;
            case "F": valor = ( 15*Math.pow(base,cont) ); break;
            default:  valor = (Integer.parseInt( numero.substring(numero.length()-1) )*Math.pow(base,cont) );
        }
        if (anterior.equals("-")){
            return anterior+""+String.valueOf(valor);
        } else {
            if (anterior.startsWith("-")){
                return String.valueOf(Double.parseDouble(anterior)-valor);
            } else {
                return String.valueOf(Double.parseDouble(anterior)+valor);
            }
        }
    }
    
    // 2147483647 = Max Value for Int (1111111111111111111111111111111)
    private static String transformarDecimalABase(String numero, int base){
        if(numero.contains(".")){
            String entero = transformarDecimalABase(numero.substring(0,numero.indexOf(".")), base);
            String x = "0."+numero.substring( numero.indexOf(".")+1,numero.length() );
            Double decimal = Double.parseDouble(x);
            x="";
            for(int i=0;i<5;i++){
                x = x + "" +chars[(int)Math.floor(decimal*base)];
                decimal = decimal*base - (int)Math.floor(decimal*base);
                
            }
            return entero+"."+x;
        } else {
            int num = Integer.parseInt(numero);
            if(Math.round(num/base)==0){
                if (Integer.parseInt(numero)<0){
                    return "-"+chars[Math.abs(num)];
                } else {
                    return chars[Math.abs(num)];
                }
            }
            
            String anterior = transformarDecimalABase(String.valueOf(num/base),base);
            int resto = Math.abs(num%base);
            
            return anterior+chars[resto];
        }
    }
    
    private static boolean ComprobarNumero(String numero, int base){
        if (numero.contains("."))   numero = numero.replaceFirst("\\.","");
        if (numero.startsWith("-")) numero = numero.replaceFirst("\\-","");
        if(numero.isEmpty()) return false;
        for(int i=0;i<=15;i++){
            if(numero.isEmpty()) return true;
            if(numero.contains(chars[i]) && i<base)
                numero = numero.replaceAll(chars[i], "");
        }
        return numero.isEmpty();
    }
    
}
