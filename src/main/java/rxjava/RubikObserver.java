package rxjava;

import rubik3.model.Rubik;
import rx.Observable;
import rx.Observer;

public class RubikObserver implements Observer<String>{

  @Override
  public void onCompleted() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onError(Throwable arg0) {
    arg0.printStackTrace();
  }

  @Override
  public void onNext(String s) {
    Rubik r = Rubik.valueOf(s);
    
  }
 

}
