package rubik22;

import rubik22.generator.RubikGeneratorRouter;

public class Main {
	public static void main(String[] args) {
		long start = System.nanoTime();
		akka.Main.main(new String[] { RubikGeneratorRouter.class
				.getName() });
		System.out.println((System.nanoTime() - start)/1000+" µs");
	}
}
