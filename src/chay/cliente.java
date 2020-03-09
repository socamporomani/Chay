/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chay;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author socamporomani
 */
class cliente {

    
    
    static class acceso extends Observable{
        private Socket socket;
       private OutputStream outputStream;
       
       @Override
       public void notifyobservs(Object arg){
           super.setChanged();
           super.notifyObservers(arg);
       }
    
    public void InitSocket(String server, int port) throws IOException{
        socket=new Socket("localhost",5555);
        outputStream=socket.getOutputStream();
        
        Thread recivehilo =new Thread(){
            @Override
            public void run(){
                try{
                    BufferedReader r= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String salir;
                    while((salir=r.readLine())!=null)
                        notifyObservers(salir);
                } catch (IOException ex) {
                        notifyObservers(ex);
                }
            }
        };
        recivehilo.start();
    }
    private static final String intro ="\r\n";
    
    
    public void enviar (String texto){
        try{
            outputStream.write((texto+intro).getBytes());
            outputStream.flush();
        }catch(IOException ex){
            notifyObservers(ex);
        }
    }
    public void cerrar() throws IOException{
        socket.close();
    }
    
    
    }
    static class ventana extends JFrame implements Observer{
    

        private JTextArea textArea;
        private JTextField textField;
        private JButton btnEnviar;
        private acceso access;
        
        public ventana(acceso access){
            this.access=access;
            access.addObserver(this);
            interfaz();
        }
        
        private void interfaz(){
            textArea=new JTextArea(20, 60);
            //textArea=setEditable(false);
            //textArea=setLineWrap(true);
            add(new JScrollPane(textArea),BorderLayout.CENTER);
            Box box=Box.createHorizontalBox();
            add(box, BorderLayout.SOUTH);
            textField=new JTextField();
            btnEnviar=new JButton("Enviar");
            box.add(textField);
            box.add(btnEnviar);
            
            ActionListener listener=new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                       String str =textField.getText();
                       if (str != null && str.trim().length()>0)
                           access.enviar(str);
                       textField.selectAll();
                       textField.requestFocus();
                       textField.setText("");
                }
            };
            textField.addActionListener(listener);
            btnEnviar.addActionListener(listener);
            
            this.addWindowListener(new WindowAdapter() {
                @Override
               public void cerrarVentana(WindowEvent e) throws IOException{
                   access.cerrar();
               }
            });
        }
            public void update(Observable o, Object arg){
                final Object finalarg=arg;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textArea.append(finalarg.toString());
                        textArea.append("\n");
                    }
                });
                
            }
        }
        
    public static void main(String[] args) {
String ser
    }
    
}
