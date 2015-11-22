package rubik22.generator;

import rubik22.model.Rotation;
import rubik22.model.Rubik;

public class RubikRotationImage {
	public final Rubik rubik;
	public final Rotation rotation;
	public final Rubik image;

	private RubikRotationImage(Builder builder) {
		this.rubik = builder.rubik;
		this.rotation = builder.rotation;
		this.image = builder.image;
	}

	public static class Builder {

		private Rubik rubik;
		private Rotation rotation;
		private Rubik image;

		public Builder withRubik(Rubik rubik) {
			this.rubik = rubik;
			return this;
		}

		public Builder withRotation(Rotation rotation) {
			this.rotation = rotation;
			return this;
		}

		public Builder withImage(Rubik image) {
			this.image = image;
			return this;
		}

		public RubikRotationImage build() {
			return new RubikRotationImage(this);
		}
	}
}
