import java.awt.*;
import javax.swing.*;

public class L5_10 extends JFrame
{
    JSplitPane cf;
    JList lb;
    JLabel bq;
    
	public static void main(String[] args) 
    {
		L5_10 lx=new L5_10();
	}
	
	L5_10()
	{
		String[] jsj={"�������","��Ϸ����","ƽ�����","��������","�������"};
		lb=new JList(jsj);
		
		bq=new JLabel(new ImageIcon("image/javapt.jpg"));
		
		cf=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,lb,bq);
		
		cf.setOneTouchExpandable(true);
		
		this.add(cf);
		
		this.setTitle("�����ѧ�Ʒ���");
		 this.setSize(640,480);
		 this.setLocation(300,280);
		 this.setResizable(false);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setVisible(true);	
		
	}
}
