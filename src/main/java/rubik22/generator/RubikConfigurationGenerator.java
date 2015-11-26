package rubik22.generator;

import rubik22.model.AbstractRubik;
import rubik22.model.Rotation;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.google.common.collect.ImmutableList;

public class RubikConfigurationGenerator extends UntypedActor {
	ActorRef worker;

	@Override
	public void preStart() throws Exception {
		worker = getContext().actorOf(Props.create(Neo4JWriter.class));
	};

	@Override
	public void onReceive(Object arg0) throws Exception {
		if (arg0 instanceof AbstractRubik) {
			AbstractRubik rubik = (AbstractRubik) arg0;
			ImmutableList.Builder<RubikRotationImage> builder = ImmutableList
					.builder();
			for (Rotation rotation : Rotation.values()) {
				builder.add(new RubikRotationImage.Builder()
						.withRubik(rubik)
						.withRotation(rotation)
						.withImage(rubik.rotate(rotation)).build());
			}
			worker.tell(builder.build(), context().parent());
		} else {
			unhandled(arg0);
		}

	}
}
