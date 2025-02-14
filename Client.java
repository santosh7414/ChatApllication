import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("ITALIC",Font.PLAIN,20);

    public Client()
    {
        try {
            System.out.println("Sending request to server");
            socket=new Socket("192.168.0.106",7778);
            System.out.println("connection done.");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
    
            createGUI();
            handleEvents();
            startReading();
            
                    } catch (final Exception e){
            
                    }
                }
            
                private void handleEvents() {
                    messageInput.addKeyListener(new KeyListener() {

                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                            if(e.getKeyCode() == 10) {
                                String contentToSend=messageInput.getText();
                                messageArea.append("Me :"+contentToSend+"\n");
                                out.println(contentToSend);
                                out.flush();
                                messageInput.setText("");
                                messageInput.requestFocus();
                            }
                        }
                        
                    });
                    
                }
            
                private void createGUI()
    {

        this.setTitle("Client Messager[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading()
    {
        final Runnable r1=()->{

            System.out.println("reader started...");

        while(true){
            try {
                final String msg=br.readLine();
                if(msg.equals("exit")){
                System.out.println("Server terminated the chat");
                JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                messageInput.setEnabled(false);
                socket.close();
                break;
            }
                messageArea.append("Server : "+ msg+"\n");
            }catch(final Exception e) 
            {
            e.printStackTrace();
            }
        }
    };

    new Thread(r1).start();
}
public void startWriting(){
    final Runnable r2=()->{
        System.out.println("Writer started...");
        while(true)
        {
            try {
                
                final BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                final String content=br1.readLine();
                out.println(content);
                out.flush();

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    };

    new Thread(r2).start();
}

public static void main(final String args[]){
    System.out.println("this is client...");
    new Client();
}
}
