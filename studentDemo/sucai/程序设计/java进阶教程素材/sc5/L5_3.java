import java.awt.*;
import javax.swing.*;

public class L5_3 extends JFrame
{
    JButton an1,an2,an3,an4,an5;
	
	public static void main(String[] args) 
    {
	    L5_3 lx=new L5_3();
	}
	
	public L5_3() 
	{
		an1=new JButton("����");
		an2=new JButton("����");
		an3=new JButton("�Ϸ�");
		an4=new JButton("����");
		an5=new JButton("�в�");
		
	    this.add(an1,BorderLayout.EAST); //�����еĲ������ǹ̶��ģ�˳���ܸı�
	    this.add(an2,BorderLayout.WEST); //ǰ���Ƕ��󣬺����ǲ��ֹ�����
	    //this.add(an3,BorderLayout.SOUTH);
	    this.add(an4,BorderLayout.NORTH);
	    //this.add(an5,BorderLayout.CENTER);
	    //������������ťȫ����ӣ�����������в�Ϊ��������䣬���в����ᱻ�����ĸ���䡣
	    
	    this.setTitle("�߽粼��BorderLayout");
	    this.setSize(380,320);
	    this.setLocation(200,200);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���һ��Ҫд�ԣ���Ϊ���д�ĶԲ�������ʱ����������
	    this.setVisible(true);
	}
}

      /*�ܽ�  1.  �̳�JFrame��
       2. �����Ϸ��������
       3. �ڹ��췽���д������
       4. �ڹ��췽����������
       5. ���ô�������
       6. ��ʾ����    
       7. ���������д������� 
       8. ���в��ֹ�����������������������������ֻ���ð�ť�����ӣ�������ֻ����Ӱ�ť*/