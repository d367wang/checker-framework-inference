import fenum.qual.Fenum;

class LiteralTest {
  
  final static @Fenum("A") int A1 = 0;
  final static @Fenum("A") int A2 = 1;

  final static @Fenum("B") int B1 = 2;

  @Fenum("A") int x;

  public void foo() {
	  x = 0;
	
  }

}
