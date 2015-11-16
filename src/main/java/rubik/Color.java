package rubik;

public enum Color {
  WHITE('w'), YELLOW('y'), GREEN('g'), BLUE('b'), ORANGE('o'), RED('r');
  public final char value;

  private Color(char value) {
    this.value = value;
  }
  
}
