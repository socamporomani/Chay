package chay;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class hiloCliente extends Thread{
     private String nombre = null;
  private DataInputStream dis = null;
  private PrintStream ps = null;
  private Socket clientSocket = null;
  private final hiloCliente[] hilos;
  private int maxClientsCount;

  public hiloCliente(Socket clientSocket, hiloCliente[] hilera) {
    this.clientSocket = clientSocket;
    this.hilos = hilera;
    maxClientsCount = hilera.length;
  }

  public void run() {
    int maxClientsCount = this.maxClientsCount;
    hiloCliente[] hilera = this.hilos;

    try {
      dis = new DataInputStream(clientSocket.getInputStream());
      ps = new PrintStream(clientSocket.getOutputStream());
      String Usuario;
      while (true) {
         ps.println("Introduce tu nombre de usuario");
        Usuario = dis.readLine().trim();
        if (Usuario.indexOf('@') == -1) {
          break;
        }
      }

     ps.println("Bienvenido al grupo "+Usuario + " \n para salir introduce /bye"
                    + "\n"
                    + "para un mensaje privado pon '@nombre_de_usuario'");
            synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (hilera[i] != null && hilera[i] == this) {
            nombre = "@" + Usuario;
            break;
          }
        }
        for (int i = 0; i < maxClientsCount; i++) {
          if (hilera[i] != null && hilera[i] != this) {
         hilera[i].ps.println("Un "+ Usuario+" salvaje ha aparecido");

          }
        }
      }
      while (true) {
        String salirP = dis.readLine();
        if (salirP.startsWith("/bye")) {
          break;
        }
        if (salirP.startsWith("@")) {
          String[] texto = salirP.split("\\s", 2);
          if (texto.length > 1 && texto[1] != null) {
            texto[1] = texto[1].trim();
            if (!texto[1].isEmpty()) {
              synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                  if (hilera[i] != null 
                      && hilera[i].nombre.equals(texto[0])) {
                    hilera[i].ps.println("<@" + Usuario + "> " + texto[1]);
                    this.ps.println(">" + Usuario + "> " + texto[1]);
                    break;
                  }
                }
              }
            }
          }
        } else {
          synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
              if (hilera[i] != null && hilera[i].nombre != null) {
                hilera[i].ps.println("<" + Usuario + "> " + salirP);
              }
            }
          }
        }
      }
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (hilera[i] != null && hilera[i] != this
              && hilera[i].nombre != null) {
                hilera[i].ps.println("@"+Usuario+" se fué");

          }
        }
      }
      ps.println("Has salid del grupo exitosamente");
      dis.close();
      ps.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
}
