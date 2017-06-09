import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

class SquarePlay extends JFrame implements MouseListener{

	private static int maxPlayer;   //プレイヤー数（2 or 4）
	private static int myNumber;  //自分の順番
	private static int currentTurn; //現在の番
	private static String name[] = new String[5];   //プレイヤーの名前
	private static String font;
    private static final int N = 8; //一辺の個数
	private static Section S[][] = new Section[N+2][N+2];   //盤面の各区画の配列
	private static JPanel playinfo = new JPanel();
    private static JLabel info[]= new JLabel[6]; //対戦情况
    private static int score[] = new int[5];    //スコア(石の数)
    private static ImageIcon stone[] = new ImageIcon[5];    //盤面の石の画像
    private static ImageIcon infostone[] = new ImageIcon[5];    //対戦情况の石の画像
    private static ImageIcon backImage[] = new ImageIcon[5];    //背景の画像
    private static Color playerColor[] = new Color[6];    //プレイヤーカラー
    private static boolean SurPlayer[] = new boolean[5];    //投了を押した人
    private static boolean finish = false;  //終了



	/* ----------------------------------------コンストラクタ---------------------------------------- */
	SquarePlay(String title,int maxPlayer,String name[],int n,String font){

        /* MacOSでうまく描画されるように */
	    try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {}

        /* タイトルを登録 */
		setTitle(title);

        /* 引数 */
		this.maxPlayer = maxPlayer;
		this.name = name;
		this.myNumber = n;
		this.font = font;

		/* アイコン */
		setIconImage(new ImageIcon(SquarePlay.class.getResource("image/icon.jpg")).getImage());

		/* 画面の大きさ */
		setSize(50*N+300,50*N+50);
		setResizable(false);

		/* 画面位置(真ん中) */
		setLocationRelativeTo(null);

		/* ウィンドウを消した時プログラムが終了するようにする */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* パネルをコンテナへ */
		JPanel basePanel = new JPanel();
		basePanel.setLayout(null);
		Container c = getContentPane();
		c.add(basePanel);

		/* 黒：1 白：2 赤：3 青：4 */
        /* 盤面の石の画像読み込み */
		stone[0] = new ImageIcon(SquarePlay.class.getResource("image/green1.png"));
        stone[1] = new ImageIcon(SquarePlay.class.getResource("image/Black_stone.png"));
        stone[2] = new ImageIcon(SquarePlay.class.getResource("image/White_stone.png"));
        stone[3] = new ImageIcon(SquarePlay.class.getResource("image/Red_stone.png"));
        stone[4] = new ImageIcon(SquarePlay.class.getResource("image/Blue_stone.png"));

        /* 対戦情况の石の画像読み込み */
        infostone[1] = new ImageIcon(SquarePlay.class.getResource("image/Black_stone.jpg"));
        infostone[2] = new ImageIcon(SquarePlay.class.getResource("image/White_stone.jpg"));
        infostone[3] = new ImageIcon(SquarePlay.class.getResource("image/Red_stone.jpg"));
        infostone[4] = new ImageIcon(SquarePlay.class.getResource("image/Blue_stone.jpg"));

        /* 背景の画像読み込み */
        backImage[0] = new ImageIcon(SquarePlay.class.getResource("image/background.jpg"));
        backImage[1] = new ImageIcon(SquarePlay.class.getResource("image/blackbackground.jpg"));
        backImage[2] = new ImageIcon(SquarePlay.class.getResource("image/whitebackground.jpg"));
        backImage[3] = new ImageIcon(SquarePlay.class.getResource("image/redbackground.jpg"));
        backImage[4] = new ImageIcon(SquarePlay.class.getResource("image/bluebackground.jpg"));

        /* プレイヤーカラーの設定 */
        playerColor[0] = new Color(0,153,0);    //盤面に近い緑
        playerColor[1] = Color.BLACK;
        playerColor[2] = Color.WHITE;
        playerColor[3] = Color.RED;
        playerColor[4] = Color.BLUE;
        playerColor[5] = new Color(255,170,22); //勝者表示の色(オレンジ色)


		/* 盤面の各区画を生成 */
		for (int i = 0; i <= N+1 ; i++) {
			for (int j = 0; j <= N+1 ; j++) {
				S[i][j] = new Section(i,j);
				if(i==0 || j==0 || i==N+1 || j==N+1) continue;  //周りの実際は使わない区画
				S[i][j].setBounds((j-1)*50+10, (i-1)*50+10, 50, 50);
                S[i][j].addMouseListener(this);
                S[i][j].setOpaque(true);
				basePanel.add(S[i][j]);
			}
		}

		/* 対戦情况 */
        CompoundBorder border = new CompoundBorder(new BevelBorder(BevelBorder.RAISED),new BevelBorder(BevelBorder.LOWERED));

        //自分の名前
		info[0]= new JLabel(name[myNumber], JLabel.CENTER);
		info[0].setFont(new Font(font, Font.BOLD, 26));
		info[0].setForeground(playerColor[myNumber]);
		//番・勝敗結果
		info[5] = new JLabel();
		info[5].setFont(new Font(font, Font.BOLD, 23));

		playinfo.setBackground(playerColor[0]);
		playinfo.setOpaque(true);
	    playinfo.setBounds(15+N*50,10,270,105);
	    playinfo.setBorder(border);
	    playinfo.setLayout(new BorderLayout());
	    playinfo.add(info[0],BorderLayout.NORTH);
	    playinfo.add(info[5],BorderLayout.CENTER);
	    basePanel.add(playinfo);

        //スコア
		for(int i=1;i<=maxPlayer;i++){
            info[i]= new JLabel("×"+2+":"+name[i], infostone[i], JLabel.LEFT);
            info[i].setFont(new Font(font, Font.BOLD, 23));
            info[i].setForeground(playerColor[i]);
            info[i].setBackground(playerColor[0]);
            info[i].setOpaque(true);
		    info[i].setBounds(15+N*50,65+i*55,270,50);
		    info[i].setBorder(border);
		    basePanel.add(info[i]);
		}

		/* 投了ボタン */
        if(maxPlayer == 2){
            JButton Surrender = new JButton("投了");
            Surrender.setFont(new Font(font, Font.BOLD, 23));
            Surrender.setForeground(Color.GREEN);
            Surrender.setBackground(playerColor[0]);
            Surrender.setBounds(225+N*50,(N-1)*50,60,60);
            Surrender.setBorder(border);
            Surrender.setOpaque(true);
            Surrender.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(currentTurn!=myNumber || finish)return;
                    Client.SendData(0,0);
                    GameFinish(true);
                }
            });
            basePanel.add(Surrender);
        }

		/* 背景 */
		JLabel background = new JLabel();
		background.setBounds(0, 0, 300+N*50, 50*N+50);
		switch(maxPlayer){
		case 2:
            background.setIcon(backImage[0]);
            break;
		case 4:
		    background.setIcon(backImage[myNumber]);
            break;
		}
		basePanel.add(background);

		/* 石の初期配置 */
		switch(maxPlayer){
        case 2:
            currentTurn = 1;
            change_stone(N/2,N/2+1);
            change_stone(N/2+1,N/2);

            currentTurn = 2;
            change_stone(N/2,N/2);
            change_stone(N/2+1,N/2+1);

            break;

        case 4:
            currentTurn = 1;
            change_stone(N/2,N/2+2);
            change_stone(N/2+1,N/2+1);

            currentTurn = 2;
            change_stone(N/2,N/2);
            change_stone(N/2+1,N/2-1);

            currentTurn = 3;
            change_stone(N/2-1,N/2);
            change_stone(N/2,N/2+1);

            currentTurn = 4;
            change_stone(N/2+1,N/2);
            change_stone(N/2+2,N/2+1);

            break;
		}

        /* 1Pから開始 */
        currentTurn = 1;
        all_put_check();
	}




	/* --------------------------------------------以下、メソッド----------------------------------------------- */

    /* ----------S(i,j)が現在のターン以外の人の石であれば真---------- */
    static boolean AnotherPlayer(int i,int j){
        return !S[i][j].player_is(currentTurn) && S[i][j].has_stone();
    }

	/* ----------S(p,q)に置けるなら真を返し、ついでにそこに置くとなった時のためにdirection[]に石が裏返る方向を記録---------- */
    static boolean put_check(int p,int q){
        int i,j;

        S[p][q].puttable_change(false);
        for(i=0;i<8;i++){
            S[p][q].direction_change(i,false);
        }

        /* 上方向 */
        if(AnotherPlayer(p-1,q)){
            for(i=p-1,j=q ; AnotherPlayer(i,j) ; i--);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(0,true);
                S[p][q].puttable_change(true);
            }
        }

        /* 右上方向 */
        if(AnotherPlayer(p-1,q+1)){
            for(i=p-1,j=q+1 ; AnotherPlayer(i,j) ; i--,j++);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(1,true);
                S[p][q].puttable_change(true);
            }
        }

        /* 右方向 */
        if(AnotherPlayer(p,q+1)){
            for(i=p,j=q+1 ; AnotherPlayer(i,j) ; j++);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(2,true);
                S[p][q].puttable_change(true);
            }
        }

        /* 右下方向 */
        if(AnotherPlayer(p+1,q+1)){
            for(i=p+1,j=q+1 ; AnotherPlayer(i,j) ; i++,j++);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(3,true);
                S[p][q].puttable_change(true);
            }
        }

        /* 下方向 */
        if(AnotherPlayer(p+1,q)){
            for(i=p+1,j=q ; AnotherPlayer(i,j) ; i++);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(4,true);
                S[p][q].puttable_change(true);
            }
        }

        /* 左下方向 */
        if(AnotherPlayer(p+1,q-1)){
            for(i=p+1,j=q-1 ; AnotherPlayer(i,j) ; i++,j--);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(5,true);
                S[p][q].puttable_change(true);
            }
        }

        /* 左方向 */
        if(AnotherPlayer(p,q-1)){
            for(i=p,j=q-1 ; AnotherPlayer(i,j) ; j--);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(6,true);
                S[p][q].puttable_change(true);
            }
        }

        /* 左上方向 */
        if(AnotherPlayer(p-1,q-1)){
            for(i=p-1,j=q-1 ; AnotherPlayer(i,j) ; i--,j--);
            if(S[i][j].player_is(currentTurn)){
                S[p][q].direction_change(7,true);
                S[p][q].puttable_change(true);
            }
        }

        return S[p][q].is_puttable();
    }

    /* ----------盤面全箇所でput_checkを行う---------- */
    static boolean all_put_check(){
        boolean flag = false;

        for(int i=1;i<=N;i++){
            for(int j=1; j<=N;j++){
                if(!S[i][j].has_stone()){
                    flag |= put_check(i,j);
                }
            }
        }

        /* 次の人のスコアの色を変える */
        info[currentTurn].setBackground(Color.GREEN);
        info[currentTurn].setOpaque(true);

        /* 次の人のターン情報の色を変える */
        info[5].setForeground(playerColor[currentTurn]);
        if(currentTurn == myNumber){
            playinfo.setBackground(Color.GREEN);
            playinfo.setOpaque(true);
        }

        if(flag){   //置ける場所があれば
        	info[5].setText(name[currentTurn]+"の番です");

        }else{      //置ける場所がなければ
            info[5].setText(name[currentTurn]+" パス");
            try{
                Thread.sleep(1000);
            }catch(Exception ex){
                Main.errorMessage(ex);
            }
        }
        return flag;
    }

    /* ----------S(i,j)を現在のターンの石にする---------- */
    static void change_stone(int i,int j){
        /* 石を裏返すときにアニメーションっぽく50ミリ秒ずらして石を変える */
        if(S[i][j].has_stone()){
            try{
                Thread.sleep(50);
            }catch(Exception ex){
                Main.errorMessage(ex);
            }
        }

        score[S[i][j].get_player()]--;
        score[currentTurn]++;
        S[i][j].player_change(currentTurn);
        S[i][j].setIcon(null);
        S[i][j].setIcon(stone[currentTurn]);
        S[i][j].puttable_change(false);
    }

    /* ----------S(p,q)に現在のターンの石を置く---------- */
    static void put_stone(int p,int q){
        int i,j;
        int firstPassPlayer;    //最初にパスした人

        if(p==0 && q==0){
            GameFinish(true);
            return;
        }

        /* その位置自身 */
        change_stone(p,q);

        /* 上方向 */
        if(S[p][q].get_direction(0)){
            for(i=p-1,j=q ; AnotherPlayer(i,j) ; i--){
                change_stone(i,j);
            }
        }

        /* 右上方向 */
        if(S[p][q].get_direction(1)){
            for(i=p-1,j=q+1 ; AnotherPlayer(i,j) ; i--,j++){
                change_stone(i,j);
            }
        }

        /* 右方向 */
        if(S[p][q].get_direction(2)){
            for(i=p,j=q+1 ; AnotherPlayer(i,j) ; j++){
                change_stone(i,j);
            }
        }

        /* 右下方向 */
        if(S[p][q].get_direction(3)){
            for(i=p+1,j=q+1 ; AnotherPlayer(i,j) ; i++,j++){
                change_stone(i,j);
            }
        }

        /* 下方向 */
        if(S[p][q].get_direction(4)){
            for(i=p+1,j=q ; AnotherPlayer(i,j) ; i++){
                change_stone(i,j);
            }
        }

        /* 左下方向 */
        if(S[p][q].get_direction(5)){
            for(i=p+1,j=q-1 ; AnotherPlayer(i,j) ; i++,j--){
                change_stone(i,j);
            }
        }

        /* 左方向 */
        if(S[p][q].get_direction(6)){
            for(i=p,j=q-1 ; AnotherPlayer(i,j) ; j--){
                change_stone(i,j);
            }
        }

        /* 左上方向 */
        if(S[p][q].get_direction(7)){
            for(i=p-1,j=q-1 ; AnotherPlayer(i,j) ; i--,j--){
                change_stone(i,j);
            }
        }

        /* スコア表示更新 */
        for(i=1;i<=maxPlayer;i++){
            info[i].setText("×"+score[i]+":"+name[i]);
		}

        /* 全部埋まったらゲーム終了 */
        if(score[0] == -N*N){
            GameFinish(false);
            return;
        }
        /* 全て、ある誰かの石で埋まったらゲーム終了 */
        for(int k=1;k<=maxPlayer;k++){
            if(-score[0] == score[k]){
                GameFinish(false);
                return;
            }
        }

        changeTurn();//次のターン
        if(all_put_check()){//次の人が置けるなら
            firstPassPlayer = 0;
        }else{              //次の人が置けないのなら
            firstPassPlayer = currentTurn;//パスの流れの最初の人を記録
            changeTurn();   //パス
            while(!all_put_check()){    //置ける人が現れるまでパスを続ける
                changeTurn();//パス
                //パスの流れ最初の人まで来てしまったら、誰も置けなくなったのでゲーム終了
                if(firstPassPlayer == currentTurn){
                    GameFinish(false);
                    return;
                }
            }
        }
    }

    /* ----------次のターンへ---------- */
    static void changeTurn(){
        /* つい今、自分のターンだったらプレイ情報の色を戻す */
        if(currentTurn == myNumber){
            playinfo.setBackground(playerColor[0]);
            playinfo.setOpaque(true);
        }
        /* つい今のターンのスコアの色を元に戻す */
        info[currentTurn].setBackground(playerColor[0]);
        info[currentTurn].setOpaque(true);

        /* 次の人へ順番を回す */
        if(currentTurn == maxPlayer){
            currentTurn = 1;
        }else{
            currentTurn++;
        }
    }

    /* ----------ゲーム終了---------- */
    static void GameFinish(boolean Surrender){
        int max=0,max_count=0;
        int winner[] = new int[maxPlayer+1];
        String str = null;

        if(Surrender){
            /* 投了ボタンが押されたとき */
            winner[1] = currentTurn==1 ? 2 : 1 ;
            max_count = 1;
        }else{
            /* 勝者をwinner[]に入れる */
            for(int i=1;i<=maxPlayer;i++){
                if(score[i]>max){
                    max = score[i];
                    winner[max_count=1] = i;
                }else if(score[i]==max){
                    winner[++max_count] = i;
                }
            }
        }

        changeTurn();   //対戦状況の色をリセット

        /* 文字は自分の色に変える */
        info[5].setForeground(Color.GREEN);

        /* 勝者を表示 */
        if(max_count==1){

            /* 勝者のスコアの色を変える*/
            info[winner[1]].setBackground(playerColor[5]);
            info[winner[1]].setOpaque(true);

            /* 勝者のターン情報の色を変える */
            if(winner[1] == myNumber){
                playinfo.setBackground(playerColor[5]);
                playinfo.setOpaque(true);
            }

            str = "勝者は "+name[winner[1]]+" です";
            if(Surrender){
                str = "投了ボタンが押されたので、" + str;
            }
            info[5].setText("<html>"+str+"</html>");

        }else if(max_count<maxPlayer){

            str = "勝者は";
            for(int i=1;i<=max_count;i++){
                str = str + " " + name[winner[i]];
                /* 勝者のスコアの色を変える*/
                info[winner[i]].setBackground(playerColor[5]);
                info[winner[i]].setOpaque(true);
                /* 勝者のターン情報の色を変える */
                if(winner[i] == myNumber){
                    playinfo.setBackground(playerColor[5]);
                    playinfo.setOpaque(true);
                }
            }
            str = str + " です";
            info[5].setText("<html>"+str+"</html>");

        }else{
            info[5].setText("引き分けです");
        }
        finish = true;
    }


    /* ----------------------------------------以下、マウスイベント処理------------------------------------------- */

    /* ----------置ける位置にクリックされた時---------- */
    public void mouseClicked(MouseEvent e){
        if(currentTurn!=myNumber || finish)return;
        Section theSection = (Section)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
        int p = theSection.I();
        int q = theSection.J();

        if(theSection.is_puttable()){
            put_stone(p,q);
            Client.SendData(p,q);
        }
    }

    /* ----------置ける位置にマウスが入った時、その石を表示してあげる---------- */
    public void mouseEntered(MouseEvent e){
        if(currentTurn!=myNumber || finish)return;
        Section theSection = (Section)e.getComponent();

        if(theSection.is_puttable()){
            theSection.setIcon(stone[currentTurn]);
        }
    }

    /* ----------置ける位置からマウスが出ていった時、石を消して元の状態に戻す---------- */
    public void mouseExited(MouseEvent e){
        if(currentTurn!=myNumber || finish)return;
        Section theSection = (Section)e.getComponent();

        if(theSection.is_puttable()){
        	theSection.setIcon(null);
            theSection.setIcon(stone[0]);
        }
    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}

}
