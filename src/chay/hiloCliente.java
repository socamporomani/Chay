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
    private DataInputStream dis;
    
    public hiloCliente(Socket clientSocket, hiloCliente[] hilo) {
        this.clientSocket=clientSocket;
        this.hilos=hilo;
     //   int 10 =int length = hilo.length;
    }
    
    public void run(){
        int max=10;
         String salir ;
        hiloCliente[] hilo=this.hilos;
        try {
 
         String Usuario;
         
            while (true) {                
                ps.println("Introduce tu nombre de usuario");
                Usuario=dis.readUTF();
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
                salir = dis.readUTF();
                if(salir.startsWith("/bye")){
                break;
            }
 
            
        if (salir.startsWith("@")){
            // revision
            String[] texto =salir.split("\\s",2);
            if (texto.length>1 && texto[1] !=null){
                texto[1]=texto[1].trim();
                if(!texto[1].isEmpty()){
                    synchronized (this){
                        for (int i=0;i< max;i++){
                            if(hilo[i]!=null && hilo[i].nombre.equals(texto[0])){
                                hilo[i].ps.println("<"+Usuario+">");
                                this.ps.println(">@"+ Usuario+">");
                            }
                        }
                    }
                }
            }
            
        }
        else{
            synchronized(this){
                for(int i=0;i<max;i++){
                    if(hilo[i]!= null && hilo[i].nombre !=null){
                        hilo[i].ps.println("<"+Usuario+">");
                    }
                }
            }
        }
            }
            synchronized(this){
                for (int i=0;i<max;i++){
                    if(hilo[i]!=null && hilo[i]!=this&& hilo[i].nombre!=null){
                        hilo[i].ps.println("@"+Usuario+" se fué");
                    }
                }
            }
            ps.println("Has salido del grupo");
           synchronized(this){
               for(int i =0;i<max;i++){
                   if(hilo[i]==this){
                       hilo[i] =null;
                   }
               }
           } 
            dis.close();
            ps.close();
            clientSocket.close();
            
        } catch (Exception e) {
        }
    }
}
