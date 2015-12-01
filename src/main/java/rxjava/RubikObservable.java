package rxjava;

import redis.clients.jedis.Jedis;
import rubik3.model.Rubik;
import rx.Observable;
import rx.Observer;

public class RubikObservable implements Observer<String>{
  final Jedis j = new Jedis("10.69.19.77", 6379);
  final static Observable<Rubik> rubikObservable= Observable.create(s -> {
   });

  @Override
  public void onCompleted() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onError(Throwable arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onNext(String arg0) {
    // TODO Auto-generated method stub
    
  }

}
