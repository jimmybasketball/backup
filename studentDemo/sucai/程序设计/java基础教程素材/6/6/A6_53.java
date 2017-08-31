import java.util.Random;
class FK{
	void bx(){
		System.out.println("变形");
	}
}
class FK_szx extends FK{//山子型
	void bx(){
		System.out.println("山子型在变形");
	}
}
class FK_lzx extends FK {//L字型
	void bx(){
		System.out.println("L字型在变形");
	}
}
class FK_zzx extends FK{//Z字形
	void bx(){
		System.out.println("Z字型在变形");
	}
}
class FK_cfx extends FK{//长方形
	void bx(){
		System.out.println("长方型在变形");
	}
}
class FK_tzx extends FK{
	void bx(){
		System.out.println("田字型在变形");
	}
}
class A6_53{
	public static void main(String[] args){
		Random r=new Random();
		int a=r.nextInt(5);
		FK fk=null;
		switch(a){
		case 0:
			fk=new FK_szx();
			break;
		case 1:
			fk=new FK_lzx();
			break;
		case 2:
			fk=new FK_zzx();
			break;
		case 3:
			fk=new FK_cfx();
			break;
		case 4:
			fk=new FK_tzx();
			break;
		}
		fk.bx();
	}
}






