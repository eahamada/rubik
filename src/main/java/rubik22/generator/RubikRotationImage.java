package rubik22.generator;

import rubik22.model.AbstractRubik;
import rubik22.model.Rotation;

public class RubikRotationImage {
	public final AbstractRubik rubik;
	public final Rotation rotation;
	public final AbstractRubik image;

	private RubikRotationImage(Builder builder) {
		this.rubik = builder.rubik;
		this.rotation = builder.rotation;
		this.image = builder.image;
	}

	public static class Builder {

		private AbstractRubik rubik;
		private Rotation rotation;
		private AbstractRubik image;

		public Builder withRubik(AbstractRubik rubik) {
			this.rubik = rubik;
			return this;
		}

		public Builder withRotation(Rotation rotation) {
			this.rotation = rotation;
			return this;
		}

		public Builder withImage(AbstractRubik image) {
			this.image = image;
			return this;
		}

		public RubikRotationImage build() {
			return new RubikRotationImage(this);
		}
	}
}
