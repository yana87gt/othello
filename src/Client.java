import java.io.*;
import java.net.*;

public class Client extends Thread {
    private static int maxConnection;   //最大人数
    private static int myNumber;    //自分の番号
    private static String myName;   //自分の名前
    private static String name[] = new String[5];   //名前
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static SquarePlay frame;

    /* コンストラクタ */
    Client(String str,Socket s){
        myName = str;
        socket = s;
        try{
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
        }catch (Exception ex)  {
            Main.errorMessage(ex);
		}
    }

    public void run(){
        try{
            /* 自分の名前送信 */
            out.println(myName);
            /* 自分の番号受信 */
            myNumber = Integer.parseInt(in.readLine());
            /* 最大人数受信 */
            maxConnection = Integer.parseInt(in.readLine());
            Main.setMax(maxConnection);
            /* 全員の名前受信 */
            for(int i=1;i<=maxConnection;i++){
                if(i==myNumber)name[i] = myName;
                else name[i] = in.readLine();
                Main.waitRoom(i,name[i]);
            }

            /* ゲーム開始 */
            sleep(500);
            Main.setflag();
            String receiveData;
            String recieveNumber[];
            while(true){
                receiveData = in.readLine();
                recieveNumber = receiveData.split("\\.");
                frame.put_stone(Integer.parseInt(recieveNumber[0]),Integer.parseInt(recieveNumber[1]));
            }

        }
        catch (Exception ex){
            Main.errorMessage(ex);
        }
    }

    /* "I座標.J座標"の形で座標データを送信 */
	static void SendData(int x,int y) {
	    out.println(String.valueOf(x)+"."+String.valueOf(y));
  	}

    /* SquarePlay開始 */
	void OpenGame(String font){
		frame = new SquarePlay("オセロ",maxConnection,name,myNumber,font);
		frame.setVisible(true);
	}
}
