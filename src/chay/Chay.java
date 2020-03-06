/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chay;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author socamporomani
 */
public class Chay {

    
    public static void main(String[] args) {
        
        Object[] servcli={"Servidor", "cliente"};
        String pred ="Servidor";
        Object selec=JOptionPane.showInputDialog(null, "Selecciona rol","chat",JOptionPane.QUESTION_MESSAGE,null,servcli,pred);
        if(selec.equals("Servidor")){
            String[] arguments=new String[]{};
            new MultiHilo().main(arguments);
        }else
            new cliente().main("localhost");
    }
}
        
        class MultiHilo{
        
            private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static final hiloCliente[] hilo=new hiloCliente[10];
    
        public static void main(String[] args) {

        try {
            serverSocket=new ServerSocket(5555);
        } catch (Exception e) {
        }
        
        while (true) {            

            try {
                
                clientSocket=serverSocket.accept();
                int i=0;
                for (i=0;i<10;i++){
                    if(hilo[i]==null){
                        (hilo[i]=new hiloCliente(clientSocket,hilo)).start();
                        break;
                    }
                }
                if(i==10){
                    PrintStream ps=new PrintStream(clientSocket.getOutputStream());
                    ps.print("Servidor lleno");
                    ps.close();
                    clientSocket.close();
                }
            } catch (Exception e) {
            }
        }
        }
    
}
