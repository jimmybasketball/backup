import java.awt.*;
import javax.swing.*;

public class L5_7 extends JFrame
{
	JPanel mb1,mb2,mb3;
	JButton an1,an2;
	JLabel bq1,bq2;
	JTextField wbk;
	JPasswordField mmk;
	
	public static void main(String[] args) 
	{
		L5_7 lx=new L5_7();
	}
	
	 L5_7()
	 {
		 mb1=new JPanel();
		 mb2=new JPanel();
		 mb3=new JPanel();
		 
		 bq1=new JLabel("�û���");
		 bq2=new JLabel("��     ��");
		 an1=new JButton("��¼");
		 an2=new JButton("ȡ��");
		 wbk=new JTextField(10);
		 mmk=new JPasswordField(10);
		 
		 this.setLayout(new GridLayout(3,1));
		 
		 mb1.add(bq1);   mb1.add(wbk);
		 mb2.add(bq2);   mb2.add(mmk);
		 mb3.add(an1);   mb3.add(an2);
		 
		 this.add(mb1);
		 this.add(mb2);
		 this.add(mb3);
		 
		 this.setTitle("�û���¼");
		 this.setSize(230,150);
		 this.setLocation(300,280);
		 this.setResizable(false);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setVisible(true);
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
	 }
}
