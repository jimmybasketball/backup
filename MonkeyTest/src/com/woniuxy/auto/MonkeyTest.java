package com.woniuxy.auto;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

// 实现一个笨猴子测试的基础演示代码
// 作者：强哥  2017-08-20
public class MonkeyTest {
	
	private Robot robot = null;

	public static void main(String[] args) {
		MonkeyTest monkey = new MonkeyTest();
		try {
			monkey.robot = new Robot();
			Runtime.getRuntime().exec("calc.exe");
			Thread.sleep(2000);
			
			for (int i=0; i<30; i++) {
				monkey.randomMove();
				Thread.sleep(200);
				monkey.randomMouse();
				Thread.sleep(200);
				monkey.randomKey();
				Thread.sleep(200);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void randomKey() {
		int[] keys = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39};
		int index = (int)(Math.random() * keys.length);
		robot.keyPress(keys[index]);
		robot.keyRelease(keys[index]);		
	}
	
	public void randomMouse() {
		int random = (int)(Math.random()*10);
		if (random >= 5) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		else {
			robot.mousePress(InputEvent.BUTTON3_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
		}
	}

	public void randomMove() {
		Random myrand = new Random();
		int x = myrand.nextInt(1250);
		int y = myrand.nextInt(750);
		robot.mouseMove(x, y);
	}
}