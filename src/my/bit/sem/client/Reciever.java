package my.bit.sem.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import my.bit.sem.ctrl.RecieveCtrl;
import my.bit.sem.gui.MainWindow;
import my.bit.sem.message.Message;
import my.bit.sem.message.MessageType;
import my.bit.sem.rsa.RSA;


public class Reciever implements Runnable {

    public static final Logger logger = LogManager.getLogger();

    private Buffer buffer;
    private boolean run = true;
    private MainWindow mw;
    private RSA rsa;


    public Reciever(Buffer buffer, MainWindow mw, RSA rsa) {
        this.buffer = buffer;
        this.mw = mw;
        this.rsa = rsa;
        logger.trace("Handle runnable for incoming message was create");
    }


    @Override
    public void run() {
        logger.trace("Handle for incoming mesage begin work");
        Thread.currentThread().setName("handler-incoming-message");
        while (run) {
            Message message = buffer.process();
            switch (message.getType()) {
                case MESSAGE:
//                    logger.entry();
                    String msg = new String(rsa.decription(message.getMessage()).toByteArray());
//                    String msg = message.getMessage();
//                    logger.exit();
                    logger.trace("Case message");
                    mw.recieve(msg);
                    break;
                case PUBLIC_KEY:
                    logger.trace("Case publicKey");
                    rsa.setServerKey(message.getKey());
                    break;
                case LOGOUT:
                    logger.trace("Case logout");
                    stop();
                    break;
                default:
                    logger.error("Unknow type of message");
                    break;
            }

        }

    }


    public void stop() {
        run = false;
    }
}
