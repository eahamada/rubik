package rubik22;

import rubik22.generator.RubikGeneratorRouter;

public class Main {
	public static void main(String[] args) {
		akka.Main.main(new String[] { RubikGeneratorRouter.class
				.getName() });
	}
}
