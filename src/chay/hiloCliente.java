/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chay;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author socamporomani
 */
public class hiloCliente extends Thread {

    private String nombre;
    private PrintStream ps;
    private Socket clientSocket;
    private final hiloCliente[] hilos;
    private DataInputStream is;
    
    public hiloCliente(Socket clientSocket, hiloCliente[] hilo) {
        this.clientSocket=clientSocket;
        this.hilos=hilo;
     //   int 10 =int length = hilo.length;
    }
    
    public void run(){
        int max=10;
        hiloCliente[] hilo=this.hilos;
        try {
 
         String Usuario;
         
            while (true) {                
                ps.println("Introduce tu nombre de usuario");
                Usuario=is.readUTF();
            if (Usuario.indexOf('@')==-1){
                break;
            }
            }
            ps.println("Bienvenido al grupo "+Usuario + " \n para salir introduce /bye"
                    + "\n"
                    + "para un mensaje privado pon '@nombre_de_usuario'");
            synchronized(this){
                for(int i=0;i<max;i++){
                    if(hilo[i] != null && hilo[i]==this){
                        nombre="@"+Usuario;
                        break;
                    }
                }
                for(int i=0;i<max;i++){
                    if(hilo[i]!=null&&hilo[i]!= this){
                        hilo[i].ps.println("Un "+ Usuario+" salvaje ha aparecido");
                    }
                }
            }
         
            while (true) {                
                String salir = is.readUTF();
                if(salir.startsWith("/bye"));
                break;
            }
 
            
        } catch (Exception e) {
        }
    }
}
