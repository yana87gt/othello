import java.io.*;
import java.net.*;

class Server extends Thread{
	private static int maxConnection = 0;   //最大人数
	private static Socket socket[] = new Socket[5];
	private static BufferedReader in[] = new BufferedReader[5];
	private static PrintWriter out[] = new PrintWriter[5];
	private static ServerThread ST[] = new ServerThread[5];
    private static String name[] = new String[5];   //名前

    /* コンストラクタ */
    Server (int maxC){
        this.maxConnection = maxC;
    }

    /* ServerThreadから名前が送られてきたら登録 */
    static void setName(int n,String str){
        name[n] = str;
    }

    /* 自分(number)以外の全員にデータを送信 */
	static void SendOthers(int number,String str){
		for(int i=1;i<=maxConnection;i++){
			if(i==number) continue;
			out[i].println(str);
			out[i].flush();
		}
	}

	public void run() {
		try {
			ServerSocket server = new ServerSocket(10000);

			/* 最大人数分受け付ける */
			for(int i=1; i<=maxConnection;i++){
                /* ソケット通信受け付け */
				socket[i] = server.accept();

                /* 専用のServerTHreadに通信割り振り */
				in[i] = new BufferedReader(new InputStreamReader(socket[i].getInputStream()));
				out[i] = new PrintWriter(socket[i].getOutputStream(), true);
				ST[i] = new ServerThread(i, in[i]);
				ST[i].start();

				/* プレイヤー番号送信 */
				out[i].println(i);
				/* 最大人数送信 */
				out[i].println(maxConnection);
				/* i-1人分の名前送信 */
				for(int k=1;k<i;k++){
                    out[i].println(name[k]);
				}
				/* 名前が送られてきたら他の人たちに送信 */
				while(name[i] == null){
                    Thread.sleep(100);
                }
				for(int k=1;k<i;k++){
                    out[k].println(name[i]);
				}
            }
		} catch (Exception ex) {
		    Main.errorMessage(ex);
		}
	}
}
