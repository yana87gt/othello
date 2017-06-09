import java.awt.*;
import javax.swing.*;

class Section extends JButton{

    private int positionI,positionJ;    //座標
    private int player; //その石のプレイヤー(0なら空き)
    private boolean puttable;   //置けるかどうか
    private boolean direction[] = new boolean [8];  //裏返す方向

    /* コンストラクタ */
    Section(int i,int j){
        this.setIcon(new ImageIcon(Section.class.getResource("image/green1.png")));
        this.player = 0;
        this.positionI = i;
        this.positionJ = j;
        this.puttable = false;
    }

    /* 以下、アクセサ */

    boolean player_is(int n){
        return this.player == n;
    }

    boolean has_stone(){
        return this.player != 0;
    }

    void direction_change(int n,boolean x){
        this.direction[n] = x;
    }

    void puttable_change(boolean x){
        this.puttable = x;
    }

    void player_change(int n){
        this.player = n;
    }

    int get_player(){
        return this.player;
    }

    boolean is_puttable(){
        return this.puttable;
    }

    boolean get_direction(int n){
        return this.direction[n];
    }

    int I(){
        return this.positionI;
    }

    int J(){
        return this.positionJ;
    }

}
