import java.awt.*;
import javax.swing.*;

public class L5_8 extends JFrame
{
	JPanel mb1,mb2,mb3;
	JButton an1,an2;
	JLabel bq1,bq2;
	JCheckBox fxk1,fxk2,fxk3;
	JRadioButton dx1,dx2;
	//ButtonGroup dxz;//�ѵ�ѡ��ť�Ž�һ������
	
	public static void main(String[] args) 
    {
	   L5_8 lx=new L5_8();
	}
	
	public L5_8()
	{
		mb1=new JPanel();
		mb2=new JPanel();
		mb3=new JPanel();
		
		bq1=new JLabel("�س�");
	    bq2=new JLabel("�Ա�");
	    an1=new JButton("ע��");
		an2=new JButton("ȡ��");
		fxk1=new JCheckBox("����");
		fxk2=new JCheckBox("����");
		fxk3=new JCheckBox("����");
		dx1=new JRadioButton("��");
		dx2=new JRadioButton("Ů");
		
		//dxz=new ButtonGroup();
		//dxz.add(dx1);   dxz.add(dx2);  //��ӽ��飬ʹ��ֻ��ѡ����һ��
		
		this.setLayout(new GridLayout(3,1));
		
		mb1.add(bq1);  mb1.add(fxk1); mb1.add(fxk2);  mb1.add(fxk3);
		mb2.add(bq2);  mb2.add(dx1);  mb2.add(dx2); //����һ��һ����ӣ����ǰ�����ӽ���
		mb3.add(an1);  mb3.add(an2);
		
	     this.add(mb1);
		 this.add(mb2);
		 this.add(mb3);
		 
		 this.setTitle("�û�ע��");
		 this.setSize(240,150);
		 this.setLocation(300,280);
		 this.setResizable(false);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setVisible(true);		
	}
}