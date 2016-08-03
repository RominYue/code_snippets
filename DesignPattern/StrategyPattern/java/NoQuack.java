package StrategyPattern;

public class NoQuack implements QuackBehavior {

	@Override
	public void quack() {
		System.out.println("No Quack!!!");

	}

}
