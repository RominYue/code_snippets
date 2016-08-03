package StrategyPattern;

public class TestMain {

	public static void main(String[] args) {
		Duck maller = new MallardDuck();
		maller.performFly();
		maller.performQuack();
		maller.display();
		System.out.println("");
		
		//set new behavior
		maller.setFlyBehavior(new FlyNoWay());
		maller.performFly();
		System.out.print("\n");
		
		maller.setQuackBehavior(new NoQuack());
		maller.performQuack();
	}

}
