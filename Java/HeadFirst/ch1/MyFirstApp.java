public class MyFirstApp {
	public static void main(String[] args) {
		int x = 1;
		System.out.println("Before the Loop");
		while(x < 4) {
			System.out.println("In the loop");
			System.out.println("value x is "+x);
			x++;
		}
		System.out.println("After the Loop");
	}
};
