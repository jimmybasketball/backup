import java.awt.*;
import javax.swing.*;

public class L5_2 extends JFrame
{
	JButton an1=null;   //����Ҫ�����ȫ�������ﶨ��
	
	public static void main(String[] args)
	{
         L5_2 lx1=new L5_2();   //������ֻ����ü��ɡ�
	}
	
	public L5_2()         //�ѳ�ʼ����ȫ�������ŵ����캯������ɡ��������ô�С�����⡢λ�õȵȡ�
	{
		this.setTitle("�û�����");  //���ô��ڱ���
		this.setSize(350, 180);     //���ô�С��λ��
		this.setLocation(100, 260);
		an1= new JButton("һ����ť");   //�������
		this.add(an1);                  //������
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}
