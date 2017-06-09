import java.io.*;
import java.net.*;

/* Clientから受信した情報をServerを使って処理をする */
class ServerThread extends Thread {
	private int myNumber;
	private BufferedReader myIn;

    /* コンストラクタ */
	ServerThread(int n, BufferedReader in){
		myNumber = n;
		myIn = in;
	}

	public void run() {
		try {
		    /* 名前の読み込み */
			String myName = myIn.readLine();
			Server.setName(myNumber,myName);

            /* Clientからデータが送られてきたら他の全員に送信 */
            String str = null;
			while (true) {
				str = myIn.readLine();
				Server.SendOthers(myNumber,str);
			}
		} catch (Exception ex) {
            Main.errorMessage(ex);
		}
	}
}

