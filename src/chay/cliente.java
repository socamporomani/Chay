package chay;

import javax.swing.*;
import java.awt.*;
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

class cliente {

    
    
      static class Acceso extends Observable {
        private Socket socket;
        private OutputStream outputStream;

        
        public void notifyObservers(Object arg) {
            super.setChanged();
            super.notifyObservers(arg);
        }

        public void InitSocket(String server, int port) throws IOException {
            socket = new Socket(server, port);
            outputStream = socket.getOutputStream();

            Thread recivehilo = new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader r = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        String salir;
                        while ((salir = r.readLine()) != null)
                            notifyObservers(salir);
                    } catch (IOException ex) {
                        notifyObservers(ex);
                    }
                }
            };
            recivehilo.start();
        }

        private static final String intro = "\r\n"; // newline

        public void enviar(String texto) {
            try {
                outputStream.write((texto + intro).getBytes());
                outputStream.flush();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }

        public void cerrar() {
            try {
                socket.close();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
    }

    static class Ventana extends JFrame implements Observer {

        private JTextArea textArea;
        private JTextField TextField;
        private JButton btnEnviar;
        private Acceso salaChat;

        public Ventana(Acceso accesoChat) {
            this.salaChat = accesoChat;
            accesoChat.addObserver(this);
            interfaz();
        }

        private void interfaz() {
            textArea = new JTextArea(20, 50);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            add(new JScrollPane(textArea), BorderLayout.CENTER);

            Box box = Box.createHorizontalBox();
            add(box, BorderLayout.SOUTH);
            TextField = new JTextField();
            btnEnviar = new JButton("Enviar");
            box.add(TextField);
            box.add(btnEnviar);

            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String str = TextField.getText();
                    if (str != null && str.trim().length() > 0)
                        salaChat.enviar(str);
                    TextField.selectAll();
                    TextField.requestFocus();
                    TextField.setText("");
                }
            };
            TextField.addActionListener(listener);
            btnEnviar.addActionListener(listener);

            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    salaChat.cerrar();
                }
            });
        }

        public void update(Observable o, Object arg) {
            final Object finalArg = arg;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    textArea.append(finalArg.toString());
                    textArea.append("\n");
                }
            });
        }
    }

    public static void main(String[] args) {
        String server = args[0];
        int port =5555;
        Acceso accesso = new Acceso();

        JFrame frame = new Ventana(accesso);
frame.setTitle("CHAT multicliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);

        try {
            accesso.InitSocket(server,port);
        } catch (IOException ex) {
            System.out.println("No se puede conector " + server + ":" + port);
            ex.printStackTrace();
            System.exit(0);
        }
    }
}